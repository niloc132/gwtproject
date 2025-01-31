/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwtproject.i18n.shared;

/**
 * Performs all the tests in {@link BidiUtilsTest} using the GWT implementation of {@link
 * com.google.gwt.regexp.shared.RegExp}. This is needed due to several differences between the two
 * implementations of RegExp (see {@link com.google.gwt.regexp.shared.RegExpTest} for details).
 */
public class GwtBidiUtilsTest extends BidiUtilsTest {

  // @Override
  public String getModuleName() {
    return "org.gwtproject.i18n.I18NTest_shared";
  }
}
