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
package org.gwtproject.cell.client;

import org.gwtproject.resources.client.ClientBundle;
import org.gwtproject.resources.client.ImageResource;

/** Resources used by the tests in this package. */
class Resources {

  // @Resource
  interface Bundle extends ClientBundle {
    @Source("prettyPiccy.png")
    ImageResource prettyPiccy();
  }

  private static Bundle bundle;

  private static Bundle getBundle() {
    if (bundle == null) {
      bundle = new Resources_BundleImpl();
    }
    return bundle;
  }

  public static ImageResource prettyPiccy() {
    return getBundle().prettyPiccy();
  }
}
