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
package org.omnifaces.test.el;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.omnifaces.test.OmniFacesIT;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ExpressionInspectorIT extends OmniFacesIT {

	@FindBy(id="valueReferenceBase")
	private WebElement valueReferenceBase;

	@FindBy(id="valueReferenceProperty")
	private WebElement valueReferenceProperty;

	@FindBy(id="getterReferenceBase")
	private WebElement getterReferenceBase;

	@FindBy(id="getterReferenceMethodName")
	private WebElement getterReferenceMethodName;

	@FindBy(id="getterReferenceReturnType")
	private WebElement getterReferenceReturnType;

	@FindBy(id="getterReferenceParamTypes")
	private WebElement getterReferenceParamTypes;

	@FindBy(id="getterReferenceActualParameters")
	private WebElement getterReferenceActualParameters;

	@FindBy(id="methodReferenceBase")
	private WebElement methodReferenceBase;

	@FindBy(id="methodReferenceMethodName")
	private WebElement methodReferenceMethodName;

	@FindBy(id="methodReferenceReturnType")
	private WebElement methodReferenceReturnType;

	@FindBy(id="methodReferenceFirstParamType")
	private WebElement methodReferenceFirstParamType;

	@FindBy(id="methodReferenceFirstActualParameter")
	private WebElement methodReferenceFirstActualParameter;

	@FindBy(id="valueMethodReferenceBase")
	private WebElement valueMethodReferenceBase;

	@FindBy(id="valueMethodReferenceMethodName")
	private WebElement valueMethodReferenceMethodName;

	@FindBy(id="valueMethodReferenceReturnType")
	private WebElement valueMethodReferenceReturnType;

	@FindBy(id="valueMethodReferenceParamTypes")
	private WebElement valueMethodReferenceParamTypes;

	@FindBy(id="valueMethodReferenceActualParameters")
	private WebElement valueMethodReferenceActualParameters;

	@FindBy(id="methodValueReferenceBase")
	private WebElement methodValueReferenceBase;

	@FindBy(id="methodValueReferenceProperty")
	private WebElement methodValueReferenceProperty;

	@FindBy(id="methodMethodReferenceBase")
	private WebElement methodMethodReferenceBase;

	@FindBy(id="methodMethodReferenceMethodName")
	private WebElement methodMethodReferenceMethodName;

	@FindBy(id="methodMethodReferenceReturnType")
	private WebElement methodMethodReferenceReturnType;

	@FindBy(id="methodMethodReferenceParamTypes")
	private WebElement methodMethodReferenceParamTypes;

	@FindBy(id="methodMethodReferenceActualParameters")
	private WebElement methodMethodReferenceActualParameters;

	@Deployment(testable=false)
	public static WebArchive createDeployment() {
		return createWebArchive(ExpressionInspectorIT.class);
	}

	@Test
	public void test() {
		assertEquals("Bar", valueReferenceBase.getText(), "valueReferenceBase is 'Bar'");
		assertEquals("selected", valueReferenceProperty.getText(), "valueReferenceProperty is 'selected'");

		assertEquals("Bar", getterReferenceBase.getText(), "getterReferenceBase is 'Bar'");
		assertEquals("getSelected", getterReferenceMethodName.getText(), "getterReferenceMethodName is 'getSelected'");
		assertEquals("Baz", getterReferenceReturnType.getText(), "getterReferenceReturnType is 'Baz'");
		assertEquals("", getterReferenceParamTypes.getText(), "getterReferenceParamTypes is empty");
		assertEquals("", getterReferenceActualParameters.getText(), "getterReferenceActualParameters is empty");

		assertEquals("Foo", methodReferenceBase.getText(), "methodReferenceBase is 'Foo'");
		assertEquals("create", methodReferenceMethodName.getText(), "methodReferenceMethodName is 'create'");
		assertEquals("void", methodReferenceReturnType.getText(), "methodReferenceReturnType is 'void'");
		assertEquals("Baz", methodReferenceFirstParamType.getText(), "methodReferenceFirstParamType is 'Baz'");
		assertEquals("Baz", methodReferenceFirstActualParameter.getText(), "methodReferenceFirstActualParameter is 'Baz'");

		assertEquals("Bar", valueMethodReferenceBase.getText(), "valueMethodReferenceBase is 'Bar'");
		assertEquals("getSelected", valueMethodReferenceMethodName.getText(), "valueMethodReferenceMethodName is 'getSelected'");
		assertEquals("Baz", valueMethodReferenceReturnType.getText(), "valueMethodReferenceReturnType is 'Baz'");
		assertEquals("", valueMethodReferenceParamTypes.getText(), "valueMethodReferenceParamTypes is empty");
		assertEquals("", valueMethodReferenceActualParameters.getText(), "valueMethodReferenceActualParameters is empty");

		assertEquals("Bar", methodValueReferenceBase.getText(), "methodValueReferenceBase is 'Bar'");
		assertEquals("selected", methodValueReferenceProperty.getText(), "methodValueReferenceProperty is 'selected'");

		assertEquals("Bar", methodMethodReferenceBase.getText(), "methodMethodReferenceBase is 'Bar'");
		assertEquals("getSelected", methodMethodReferenceMethodName.getText(), "methodMethodReferenceMethodName is 'getSelected'");
		assertEquals("Baz", methodMethodReferenceReturnType.getText(), "methodMethodReferenceReturnType is 'Baz'");
		assertEquals("", methodMethodReferenceParamTypes.getText(), "methodMethodReferenceParamTypes is empty");
		assertEquals("", methodMethodReferenceActualParameters.getText(), "methodMethodReferenceActualParameters is empty");
	}

}

