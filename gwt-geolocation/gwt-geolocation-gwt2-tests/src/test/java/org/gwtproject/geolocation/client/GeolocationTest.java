/*
 * Copyright © 2019 The GWT Project Authors
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
package org.gwtproject.geolocation.client;

import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.junit.client.GWTTestCase;

/** Tests for {@link Geolocation}. */
public class GeolocationTest extends GWTTestCase {

  protected Geolocation geolocation;

  @Override
  public String getModuleName() {
    return "org.gwtproject.geolocation.GeolocationTestModule";
  }

  @Override
  protected void gwtSetUp() throws Exception {
    geolocation = Geolocation.getIfSupported();
  }

  @Override
  protected void gwtTearDown() throws Exception {
    geolocation = null;
  }

  public void testNullIfUnsupported() {
    if (!Geolocation.isSupported()) {
      assertNull(geolocation);
    } else {
      assertNotNull(geolocation);
    }
  }
}
