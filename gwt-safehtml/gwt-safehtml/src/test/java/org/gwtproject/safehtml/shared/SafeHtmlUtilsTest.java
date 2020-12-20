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
package org.gwtproject.safehtml.shared;

import junit.framework.TestCase;

/** Unit tests for SafeHtmlUtils. */
public class SafeHtmlUtilsTest extends TestCase {

  private static final String CONSTANT_HTML =
      "<a href=\"javascript:trusted()\">click here &amp; enjoy</a>";

  {
    // Since we can't assume assertions are enabled, force
    // SafeHtmlHostedModeUtils#maybeCheckCompleteHtml to perform its check
    // when running in JRE.
    SafeHtmlHostedModeUtils.setForceCheckCompleteHtml(true);
  }

  public void testEscape_noEscape() {
    String escaped = SafeHtmlUtils.htmlEscape("foobar");
    assertEquals("foobar", escaped);
  }

  public void testEscape_ampersand() {
    String escaped = SafeHtmlUtils.htmlEscape("foo&bar");
    assertEquals("foo&amp;bar", escaped);
  }

  public void testEscape_ampersandAndBrackets() {
    String escaped = SafeHtmlUtils.htmlEscape("fo<o&b<em>ar");
    assertEquals("fo&lt;o&amp;b&lt;em&gt;ar", escaped);
  }

  public void testEscape_allMetaCharacters() {
    String escaped = SafeHtmlUtils.htmlEscape("f\"bar \'<&em><e/m>oo&bar");
    assertEquals("f&quot;bar &#39;&lt;&amp;em&gt;&lt;e/m&gt;oo&amp;bar", escaped);
  }

  public void testEscape_withEntities1() {
    String escaped = SafeHtmlUtils.htmlEscapeAllowEntities("f\"bar \'<&em><e/m>oo&bar");
    assertEquals("f&quot;bar &#39;&lt;&amp;em&gt;&lt;e/m&gt;oo&amp;bar", escaped);
  }

  public void testEscape_withEntities2() {
    String escaped = SafeHtmlUtils.htmlEscapeAllowEntities("& foo &lt;");
    assertEquals("&amp; foo &lt;", escaped);
  }

  public void testEscape_withEntities3() {
    String escaped = SafeHtmlUtils.htmlEscapeAllowEntities("<foo> &amp; <em> bar &#39; baz");
    assertEquals("&lt;foo&gt; &amp; &lt;em&gt; bar &#39; baz", escaped);
  }

  public void testEscape_withEntities4() {
    String escaped = SafeHtmlUtils.htmlEscapeAllowEntities("&foo &&amp; bar &#39; baz&");
    assertEquals("&amp;foo &amp;&amp; bar &#39; baz&amp;", escaped);
  }

  public void testEscape_withEntitiesInvalidEntities() {
    String escaped = SafeHtmlUtils.htmlEscapeAllowEntities("&a mp;&;&x;&#;&#x;");
    assertEquals("&amp;a mp;&amp;;&x;&amp;#;&amp;#x;", escaped);
  }

  public void testFromSafeConstant() {
    SafeHtml h = SafeHtmlUtils.fromSafeConstant(CONSTANT_HTML);
    assertEquals(CONSTANT_HTML, h.asString());
  }

  public void testFromSafeConstant_withIncompleteHtml() {
    try {
      SafeHtml h = SafeHtmlUtils.fromSafeConstant("<a href=\"");
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  public void testFromString() {
    SafeHtml h = SafeHtmlUtils.fromString(CONSTANT_HTML);
    assertEquals(SafeHtmlUtils.htmlEscape(CONSTANT_HTML), h.asString());
  }

  public void testEscape_chars() {
    String escaped = SafeHtmlUtils.htmlEscape('a');
    assertEquals("a", escaped);

    escaped = SafeHtmlUtils.htmlEscape('&');
    assertEquals("&amp;", escaped);

    escaped = SafeHtmlUtils.htmlEscape('<');
    assertEquals("&lt;", escaped);

    escaped = SafeHtmlUtils.htmlEscape('>');
    assertEquals("&gt;", escaped);

    escaped = SafeHtmlUtils.htmlEscape('"');
    assertEquals("&quot;", escaped);

    escaped = SafeHtmlUtils.htmlEscape('\'');
    assertEquals("&#39;", escaped);
  }
}
