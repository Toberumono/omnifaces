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
package org.omnifaces.test.cdi.param;

import static org.omnifaces.util.Faces.isValidationFailed;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.Size;

import org.omnifaces.cdi.Param;

@Named
@RequestScoped
@ParamITCustomAnnotation
public class ParamITCustomAnnotationBean {

	@Param @Size(min = 2)
	private String stringParam;

	private String initResult;

	@PostConstruct
	public void init() {
		if (isValidationFailed()) {
			initResult = "initValidationFailed";
		}
		else {
			initResult = "initSuccess";
		}
	}

	public String getStringParam() {
		return stringParam;
	}

	public String getInitResult() {
		return initResult;
	}

}