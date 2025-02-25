/*
 * Copyright OmniFaces
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.taghandler;

import static java.lang.String.format;
import static org.omnifaces.util.FacesLocal.getFaceletContext;
import static org.omnifaces.util.Reflection.invokeMethod;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.facelets.ConverterHandler;
import javax.faces.view.facelets.DelegatingMetaTagHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagHandlerDelegate;
import javax.faces.view.facelets.ValidatorHandler;

/**
 * Helper class for OmniFaces {@link Converter} and {@link Validator}. It can't be an abstract class as they have to
 * extend from {@link ConverterHandler} and {@link ValidatorHandler}.
 *
 * @author Bauke Scholtz
 */
final class DeferredTagHandlerHelper {

	// Private constants ----------------------------------------------------------------------------------------------

	private static final String ERROR_MISSING_ID =
		"o:%1$s '%1$sId' or 'binding' attribute must be specified.";
	private static final String ERROR_INVALID_ID =
		"o:%1$s '%1$sId' attribute must refer an valid %1$s ID. The %1$s ID '%2$s' cannot be found.";

	// Constructors ---------------------------------------------------------------------------------------------------

	/**
	 * Hide constructor.
	 */
	private DeferredTagHandlerHelper() {
		//
	}

	// Actions --------------------------------------------------------------------------------------------------------

	/**
	 * Create the tag instance based on the <code>binding</code> and/or <code>instanceId</code> attribute.
	 * @param context The involved EL context.
	 * @param binding The involved binding attribute.
	 * @param id The involved id attribute.
	 * @param factory The factory to obtain the instance by id.
	 * @param name The name of the deferred tag, solely for error reporting.
	 * @return The created instance.
	 * @throws IllegalArgumentException If the <code>validatorId</code> attribute is invalid or missing while the
	 * <code>binding</code> attribute is also missing.
	 * @throws ClassCastException When <code>T</code> is of wrong type.
	 */
	@SuppressWarnings("unchecked")
	static <T> T createInstance(ELContext context, ValueExpression binding, ValueExpression id, Function<String, T> factory, String name) {
		T instance = null;

		if (binding != null) {
			instance = (T) binding.getValue(context);
		}

		if (id != null) {
			try {
				instance = factory.apply((String) id.getValue(context));
			}
			catch (FacesException e) {
				throw new IllegalArgumentException(
					format(ERROR_INVALID_ID, name, id), e);
			}

			if (binding != null) {
				binding.setValue(context, instance);
			}
		}
		else if (instance == null) {
			throw new IllegalArgumentException(
				format(ERROR_MISSING_ID, name));
		}

		return instance;
	}

	/**
	 * Collect the deferred attributes of the given object. If the property is a literal text (i.e. no EL expression),
	 * then it will just be set directly on the given object, else it will be collected as {@link ValueExpression} and
	 * setter method pairs and returned.
	 * @param context The involved facelet context.
	 * @param instance The instance to collect EL properties for.
	 * @return The deferred attributes of the given object.
	 */
	static <T> DeferredAttributes collectDeferredAttributes
		(FaceletContext context, DeferredTagHandler tag, T instance)
	{
		DeferredAttributes attributes = new DeferredAttributes();

		try {
			for (PropertyDescriptor property : Introspector.getBeanInfo(instance.getClass()).getPropertyDescriptors()) {
				Method setter = property.getWriteMethod();
				ValueExpression valueExpression = getValueExpression(context, tag, property.getName(), property.getPropertyType());

				if (setter == null || valueExpression == null) {
					continue;
				}

				if (valueExpression.isLiteralText()) {
					setter.invoke(instance, valueExpression.getValue(context));
				}
				else {
					attributes.add(setter, valueExpression);
				}
			}
		}
		catch (Exception e) {
			throw new FacesException(e);
		}

		return attributes;
	}

	/**
	 * Convenience method to get the given attribute as a {@link ValueExpression}, or <code>null</code> if there is
	 * no such attribute.
	 * @param context The involved facelet context.
	 * @param name The attribute name to return the value expression for.
	 * @param type The type of the value expression.
	 * @return The given attribute as a {@link ValueExpression}.
	 */
	static <T> ValueExpression getValueExpression
		(FaceletContext context, DeferredTagHandler tag, String name, Class<T> type)
	{
		TagAttribute attribute = tag.getTagAttribute(name);
		return (attribute != null) ? attribute.getValueExpression(context, type) : null;
	}

	// Nested classes -------------------------------------------------------------------------------------------------

	/**
	 * So that we can extract tag attributes from both {@link ConverterHandler} and {@link ValidatorHandler} and create
	 * concrete {@link Converter} and {@link Validator} instances.
	 *
	 * @author Bauke Scholtz
	 */
	interface DeferredTagHandler {

		/**
		 * Just return TagHandler#getAttribute() via a public method (it's by default protected and otherwise thus
		 * unavailable inside collectDeferredAttributes().
		 * @param name The attribute name.
		 * @return The tag attribute associated with given attribute name.
		 */
		TagAttribute getTagAttribute(String name);
	}

	/**
	 * Convenience class which holds all deferred attribute setters and value expressions.
	 *
	 * @author Bauke Scholtz
	 */
	static final class DeferredAttributes implements Serializable {

		private static final long serialVersionUID = 1L;

		private Map<String, ValueExpression> attributes;

		private DeferredAttributes() {
			attributes = new HashMap<>();
		}

		private void add(Method setter, ValueExpression valueExpression) {
			attributes.put(setter.getName(), valueExpression);
		}

		public void invokeSetters(ELContext elContext, Object object) {
			for (Entry<String, ValueExpression> entry : attributes.entrySet()) {
				try {
					invokeMethod(object, entry.getKey(), entry.getValue().getValue(elContext));
				}
				catch (Exception e) {
					throw new FacesException(e);
				}
			}
		}
	}

	/**
	 * Convenience tag handler delegate which delegates {@link #applyAttachedObject(FacesContext, UIComponent)} to the
	 * owning tag itself. This all is used when composite components come into picture.
	 *
	 * @author Bauke Scholtz
	 */
	static class DeferredTagHandlerDelegate extends TagHandlerDelegate implements AttachedObjectHandler {

		private DelegatingMetaTagHandler tag;
		private TagHandlerDelegate delegate;

		public DeferredTagHandlerDelegate(DelegatingMetaTagHandler tag, TagHandlerDelegate delegate) {
			this.tag = tag;
			this.delegate = delegate;
		}

		@Override
		public void apply(FaceletContext context, UIComponent component) throws IOException {
			delegate.apply(context, component);
		}

		@Override
		public String getFor() {
			return ((AttachedObjectHandler) delegate).getFor();
		}

		@Override
		public void applyAttachedObject(FacesContext context, UIComponent parent) {
			try {
				tag.apply(getFaceletContext(context), parent);
			}
			catch (IOException e) {
				throw new FacesException(e);
			}
		}

		@Override
		public MetaRuleset createMetaRuleset(@SuppressWarnings("rawtypes") Class type) {
			return delegate.createMetaRuleset(type);
		}

	}

}