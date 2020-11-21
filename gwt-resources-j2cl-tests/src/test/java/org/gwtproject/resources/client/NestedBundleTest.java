/*
 * Copyright 2008 Google Inc.
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
package org.gwtproject.resources.client;

import com.google.gwt.junit.client.GWTTestCase;

/** Verify that nested bundles work correctly. */
public class NestedBundleTest extends GWTTestCase {

  @Resource
  interface NestedBundle extends ClientBundle {
    @Source("hello.txt")
    TextResource hello();

    NestedBundle nested();
  }

  @Override
  public String getModuleName() {
    return "org.gwtproject.resources.ResourcesTestsModule";
  }

  public void testNestedBundle() {
    NestedBundle b = new NestedBundleTest_NestedBundleImpl();
    assertSame(b.hello(), b.nested().hello());
  }

  public void testNestedCreateOverride() {
    NestedBundle b = new NestedBundleTest_NestedBundleImpl();
    assertTrue(b.nested() instanceof NestedBundle);
  }
}
