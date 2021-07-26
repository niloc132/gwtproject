/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jresearch.threetenbp.gwt.emu.java.time.format;

import org.jresearch.threetenbp.gwt.emu.java.time.format.DateTimeFormatterBuilder.StringLiteralPrinterParser;
import org.jresearch.threetenbp.gwt.emu.java.time.temporal.TemporalQueries;
import org.junit.Test;

/** Test StringLiteralPrinterParser. */
// @Test
public class TestStringLiteralParser extends AbstractTestPrinterParser {

  // @DataProvider(name="success")
  Object[][] data_success() {
    return new Object[][] {
      // match
      {new StringLiteralPrinterParser("hello"), true, "hello", 0, 5},
      {new StringLiteralPrinterParser("hello"), true, "helloOTHER", 0, 5},
      {new StringLiteralPrinterParser("hello"), true, "OTHERhelloOTHER", 5, 10},
      {new StringLiteralPrinterParser("hello"), true, "OTHERhello", 5, 10},

      // no match
      {new StringLiteralPrinterParser("hello"), true, "", 0, ~0},
      {new StringLiteralPrinterParser("hello"), true, "a", 1, ~1},
      {new StringLiteralPrinterParser("hello"), true, "HELLO", 0, ~0},
      {new StringLiteralPrinterParser("hello"), true, "hlloo", 0, ~0},
      {new StringLiteralPrinterParser("hello"), true, "OTHERhllooOTHER", 5, ~5},
      {new StringLiteralPrinterParser("hello"), true, "OTHERhlloo", 5, ~5},
      {new StringLiteralPrinterParser("hello"), true, "h", 0, ~0},
      {new StringLiteralPrinterParser("hello"), true, "OTHERh", 5, ~5},

      // case insensitive
      {new StringLiteralPrinterParser("hello"), false, "hello", 0, 5},
      {new StringLiteralPrinterParser("hello"), false, "HELLO", 0, 5},
      {new StringLiteralPrinterParser("hello"), false, "HelLo", 0, 5},
      {new StringLiteralPrinterParser("hello"), false, "HelLO", 0, 5},
    };
  }

  @Test(/* dataProvider="success" */ )
  public void test_parse_success() throws Exception {
    Object[][] data = data_success();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_success(
          (StringLiteralPrinterParser) objects[0],
          (boolean) objects[1],
          (String) objects[2],
          (int) objects[3],
          (int) objects[4]);
    }
  }

  public void test_parse_success(
      StringLiteralPrinterParser pp, boolean caseSensitive, String text, int pos, int expectedPos) {
    parseContext.setCaseSensitive(caseSensitive);
    int result = pp.parse(parseContext, text, pos);
    assertEquals(result, expectedPos);
    assertEquals(parseContext.toParsed().query(TemporalQueries.chronology()), null);
    assertEquals(parseContext.toParsed().query(TemporalQueries.zoneId()), null);
  }

  // -----------------------------------------------------------------------
  // @DataProvider(name="error")
  Object[][] data_error() {
    return new Object[][] {
      {new StringLiteralPrinterParser("hello"), "hello", -1, IndexOutOfBoundsException.class},
      {new StringLiteralPrinterParser("hello"), "hello", 6, IndexOutOfBoundsException.class},
    };
  }

  @Test(/* dataProvider="error" */ )
  public void test_parse_error() throws Exception {
    Object[][] data = data_error();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      gwtSetUp();
      test_parse_error(
          (StringLiteralPrinterParser) objects[0],
          (String) objects[1],
          (int) objects[2],
          (Class<?>) objects[3]);
    }
  }

  public void test_parse_error(
      StringLiteralPrinterParser pp, String text, int pos, Class<?> expected) {
    try {
      pp.parse(parseContext, text, pos);
    } catch (RuntimeException ex) {
      // GWT Specific
      assertEquals(expected.getName(), ex.getClass().getName());
      assertEquals(parseContext.toParsed().query(TemporalQueries.chronology()), null);
      assertEquals(parseContext.toParsed().query(TemporalQueries.zoneId()), null);
    }
  }
}
