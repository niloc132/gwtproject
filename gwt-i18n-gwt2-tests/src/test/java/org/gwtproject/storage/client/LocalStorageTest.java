/*
 * Copyright 2011 Google Inc.
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
package org.gwtproject.storage.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.junit.client.GWTTestCase;

public class LocalStorageTest extends GWTTestCase {

  @Override
  protected void gwtSetUp() throws Exception {

  }

  @Override
  public String getModuleName() {
    return "org.gwtproject.storage.StorageTest";
  }

  @Override
  protected void gwtTearDown() {

  }

  public void testLength() {

    assertEquals("z", LocaleInfo.getCurrentLocale().getLocaleName());
    assertEquals(0, 1);
  }
}
