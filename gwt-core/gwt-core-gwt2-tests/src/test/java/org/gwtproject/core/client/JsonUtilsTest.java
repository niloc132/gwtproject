/*
 * Copyright © 2019 The GWT Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.core.client;

import com.google.gwt.junit.client.GWTTestCase;

/** Tests {@link com.google.gwt.core.client.JsonUtils}. */
public class JsonUtilsTest extends GWTTestCase {
  @Override
  public String getModuleName() {
    return "org.gwtproject.core.Core";
  }

  public void testStringify() throws Exception {
    assertEquals("{\"a\":2}", JsonUtils.stringify(createJson()));
    assertEquals("{\n\t\"a\": 2\n}", JsonUtils.stringify(createJson(), "\t"));
    assertEquals("{\nXYZ\"a\": 2\n}", JsonUtils.stringify(createJson(), "XYZ"));
  }

  private native JavaScriptObject createJson() /*-{
      return { a: 2 };
    }-*/;
}
