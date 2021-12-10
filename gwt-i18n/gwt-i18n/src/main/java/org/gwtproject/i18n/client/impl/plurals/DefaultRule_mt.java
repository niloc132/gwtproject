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

package org.gwtproject.i18n.client.impl.plurals;

/** Plural forms for Maltese are 1, 0/x2-x10, x11-x19, and n. */
public class DefaultRule_mt extends DefaultRule {

  @Override
  public PluralForm[] pluralForms() {
    return new PluralForm[] {
      new PluralForm("other", "Default plural form"),
      new PluralForm("one", "Count is 1"),
      new PluralForm("few", "Count is 0 or ends in 02-10"),
      new PluralForm("many", "Count ends in 11-19"),
    };
  }

  @Override
  public int select(int n) {
    return n == 1
        ? 1
        : n == 0 || (n % 100 > 1 && n % 100 < 11) ? 2 : (n % 100 > 10 && n % 100 < 20) ? 3 : 0;
  }
}
