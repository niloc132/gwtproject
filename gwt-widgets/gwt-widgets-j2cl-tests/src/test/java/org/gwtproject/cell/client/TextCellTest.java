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
package org.gwtproject.cell.client;

import com.google.j2cl.junit.apt.J2clTestInput;

/** Tests for {@link org.gwtproject.cell.client.TextCell}. */
@J2clTestInput(TextCellTest.class)
public class TextCellTest extends CellTestBase<String> {

  @Override
  protected Cell<String> createCell() {
    return new TextCell();
  }

  @Override
  protected String createCellValue() {
    return "helloworld";
  }

  @Override
  protected boolean dependsOnSelection() {
    return false;
  }

  @Override
  protected String[] getConsumedEvents() {
    return null;
  }

  @Override
  protected String getExpectedInnerHtml() {
    return "helloworld";
  }

  @Override
  protected String getExpectedInnerHtmlNull() {
    return "";
  }

  @Override
  public String getModuleName() {
    return "";
  }
}
