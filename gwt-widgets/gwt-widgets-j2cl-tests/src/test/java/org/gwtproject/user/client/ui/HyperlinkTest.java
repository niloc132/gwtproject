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
package org.gwtproject.user.client.ui;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.Locale;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.event.logical.shared.ValueChangeEvent;
import org.gwtproject.event.logical.shared.ValueChangeHandler;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.safehtml.shared.SafeHtmlUtils;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.history.client.History;

/** Tests {@link HyperlinkTest}. */
@J2clTestInput(HyperlinkTest.class)
public class HyperlinkTest extends GWTTestCase {

  private static final String TEST_HTML = "<b>hello</b><i>world</i>";
  private static final String TEST_HISTORY_TOKEN = "myToken %~`!:{}%20+%37#&gt;";

  @Override
  public String getModuleName() {
    return "org.gwtproject.user.DebugTest";
  }

  public void testDebugId() {
    Hyperlink link = new Hyperlink("Click Me", TEST_HISTORY_TOKEN);
    link.ensureDebugId("myLink");
    UIObjectTest.assertDebugId("myLink-wrapper", link.getElement());
    UIObjectTest.assertDebugId("myLink", DOM.getFirstChild(link.getElement()));
  }

  public void testSafeHtmlConstructor() {
    Hyperlink link = new Hyperlink(SafeHtmlUtils.fromSafeConstant(TEST_HTML), TEST_HISTORY_TOKEN);
    assertEquals(TEST_HTML, link.getHTML().toLowerCase(Locale.ROOT));
  }

  public void testSetSafeHtml() {
    Hyperlink link = new Hyperlink("foobar", TEST_HISTORY_TOKEN);
    link.setHTML(SafeHtmlUtils.fromSafeConstant(TEST_HTML));
    assertEquals(TEST_HTML, link.getHTML().toLowerCase(Locale.ROOT));
  }

  public void testLinkToken() {
    Hyperlink link = new Hyperlink("foobar", TEST_HISTORY_TOKEN);
    assertEquals(TEST_HISTORY_TOKEN, link.getTargetHistoryToken());
  }

  public void testLinkHrefProperty() {
    Hyperlink link = new Hyperlink("foobar", TEST_HISTORY_TOKEN);
    Element element = link.getElement();
    Element anchorElement = (Element) element.getFirstChildElement();
    String propertyString = anchorElement.getPropertyString("href");
    int index = propertyString.indexOf('#');
    assertFalse(index == -1);
    String fragment = propertyString.substring(index + 1);
    String expected = History.encodeHistoryToken(TEST_HISTORY_TOKEN);
    assertEquals(expected, fragment);
  }

  public void testLinkTraversal() {
    final String testHistoryToken = TEST_HISTORY_TOKEN;
    Hyperlink link = new Hyperlink("foobar", testHistoryToken);
    HandlerRegistration registration = null;
    try {
      RootPanel.get().add(link);
      registration =
          History.addValueChangeHandler(
              new ValueChangeHandler<String>() {
                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                  assertEquals(testHistoryToken, event.getValue());
                  assertEquals(testHistoryToken, History.getToken());
                }
              });
      Document document = Document.get();
      NativeEvent event = document.createClickEvent(1, 0, 0, 0, 0, false, false, false, false);
      link.getElement().dispatchEvent(event);
    } finally {
      RootPanel.get().remove(link);
      if (registration != null) {
        registration.removeHandler();
      }
    }
  }
}
