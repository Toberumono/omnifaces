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
package org.omnifaces.event;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.NamedEvent;
import javax.faces.event.PhaseId;

import org.omnifaces.eventlistener.InvokeActionEventListener;

/**
 * Use this event to have a hook on a listener method during the beforephase of the {@link PhaseId#INVOKE_APPLICATION}.
 * This event is supported on {@link UIViewRoot}, {@link UIForm}, {@link UIInput} and {@link UICommand} components.
 * <p>
 * This event is particularly helpful as a replacement of <code>&lt;f:event type="preRenderView"&gt;</code> and also
 * provides the possibility to invoke multiple action listeners on a single {@link UIInput} and {@link UICommand}
 * components on an easy manner.
 * <pre>
 * &lt;f:event type="preInvokeAction" listener="#{bean.preInvokeAction}" /&gt;
 * </pre>
 *
 * @author Bauke Scholtz
 * @see PostInvokeActionEvent
 * @see InvokeActionEventListener
 * @since 1.1
 */
@NamedEvent(shortName = "preInvokeAction")
public class PreInvokeActionEvent extends ComponentSystemEvent {

	// Constants ------------------------------------------------------------------------------------------------------

	private static final long serialVersionUID = 2620719313947908572L;

	// Constructors ---------------------------------------------------------------------------------------------------

	/**
	 * Construct a new pre invoke action event on the given component.
	 * @param component The component to invoke the event on.
	 */
	public PreInvokeActionEvent(UIComponent component) {
		super(component);
	}

}