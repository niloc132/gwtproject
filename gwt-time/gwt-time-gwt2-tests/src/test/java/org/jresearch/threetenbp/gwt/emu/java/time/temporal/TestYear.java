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
package org.jresearch.threetenbp.gwt.emu.java.time.temporal;

import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.ERA;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.YEAR;
import static org.jresearch.threetenbp.gwt.emu.java.time.temporal.ChronoField.YEAR_OF_ERA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jresearch.threetenbp.gwt.emu.java.time.AbstractDateTimeTest;
import org.jresearch.threetenbp.gwt.emu.java.time.Clock;
import org.jresearch.threetenbp.gwt.emu.java.time.DateTimeException;
import org.jresearch.threetenbp.gwt.emu.java.time.Instant;
import org.jresearch.threetenbp.gwt.emu.java.time.LocalDate;
import org.jresearch.threetenbp.gwt.emu.java.time.LocalDateTime;
import org.jresearch.threetenbp.gwt.emu.java.time.LocalTime;
import org.jresearch.threetenbp.gwt.emu.java.time.Month;
import org.jresearch.threetenbp.gwt.emu.java.time.MonthDay;
import org.jresearch.threetenbp.gwt.emu.java.time.Year;
import org.jresearch.threetenbp.gwt.emu.java.time.YearMonth;
import org.jresearch.threetenbp.gwt.emu.java.time.ZoneId;
import org.jresearch.threetenbp.gwt.emu.java.time.ZoneOffset;
import org.jresearch.threetenbp.gwt.emu.java.time.chrono.IsoChronology;
import org.jresearch.threetenbp.gwt.emu.java.time.format.DateTimeFormatter;
import org.jresearch.threetenbp.gwt.emu.java.time.format.DateTimeParseException;
import org.junit.Test;

/** Test Year. */
// @Test
public class TestYear extends AbstractDateTimeTest {

  private static final Year TEST_2008 = Year.of(2008);

  //    @BeforeMethod
  //    public void setUp() {
  //    }

  // -----------------------------------------------------------------------
  @Override
  protected List<TemporalAccessor> samples() {
    TemporalAccessor[] array = {
      TEST_2008,
    };
    return Arrays.asList(array);
  }

  @Override
  protected List<TemporalField> validFields() {
    TemporalField[] array = {
      YEAR_OF_ERA, YEAR, ERA,
    };
    return Arrays.asList(array);
  }

  @Override
  protected List<TemporalField> invalidFields() {
    List<TemporalField> list =
        new ArrayList<TemporalField>(Arrays.<TemporalField>asList(ChronoField.values()));
    list.removeAll(validFields());
    list.add(JulianFields.JULIAN_DAY);
    list.add(JulianFields.MODIFIED_JULIAN_DAY);
    list.add(JulianFields.RATA_DIE);
    return list;
  }

  // -----------------------------------------------------------------------
  //    @Test
  //    public void test_immutable() {
  //        assertImmutable(Year.class);
  //    }

  //    @Test
  //    public void test_serialization() throws ClassNotFoundException, IOException {
  //        assertSerializable(Year.of(-1));
  //    }

  //    @Test
  //    public void test_serialization_format() throws ClassNotFoundException, IOException {
  //        assertEqualsSerialisedForm(Year.of(2012));
  //    }

  // -----------------------------------------------------------------------
  // now()
  // -----------------------------------------------------------------------
  @Test
  public void test_now() {
    Year expected = Year.now(Clock.systemDefaultZone());
    Year test = Year.now();
    for (int i = 0; i < 100; i++) {
      if (expected.equals(test)) {
        return;
      }
      expected = Year.now(Clock.systemDefaultZone());
      test = Year.now();
    }
    assertEquals(test, expected);
  }

  // -----------------------------------------------------------------------
  // now(ZoneId)
  // -----------------------------------------------------------------------
  @Test(expected = NullPointerException.class)
  public void test_now_ZoneId_nullZoneId() {
    try {
      Year.now((ZoneId) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void test_now_ZoneId() {
    ZoneId zone = ZoneId.of("UTC+01:02:03");
    Year expected = Year.now(Clock.system(zone));
    Year test = Year.now(zone);
    for (int i = 0; i < 100; i++) {
      if (expected.equals(test)) {
        return;
      }
      expected = Year.now(Clock.system(zone));
      test = Year.now(zone);
    }
    assertEquals(test, expected);
  }

  // -----------------------------------------------------------------------
  // now(Clock)
  // -----------------------------------------------------------------------
  @Test
  public void test_now_Clock() {
    Instant instant = LocalDateTime.of(2010, 12, 31, 0, 0).toInstant(ZoneOffset.UTC);
    Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
    Year test = Year.now(clock);
    assertEquals(test.getValue(), 2010);
  }

  @Test(expected = NullPointerException.class)
  public void test_now_Clock_nullClock() {
    try {
      Year.now((Clock) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_factory_int_singleton() {
    for (int i = -4; i <= 2104; i++) {
      Year test = Year.of(i);
      assertEquals(test.getValue(), i);
      assertEquals(Year.of(i), test);
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_int_tooLow() {
    try {
      Year.of(Year.MIN_VALUE - 1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_int_tooHigh() {
    try {
      Year.of(Year.MAX_VALUE + 1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  @Test
  public void test_factory_CalendricalObject() {
    assertEquals(Year.from(LocalDate.of(2007, 7, 15)), Year.of(2007));
  }

  @Test(expected = DateTimeException.class)
  public void test_factory_CalendricalObject_invalid_noDerive() {
    try {
      Year.from(LocalTime.of(12, 30));
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_CalendricalObject_null() {
    try {
      Year.from((TemporalAccessor) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // parse()
  // -----------------------------------------------------------------------
  // @DataProvider(name="goodParseData")
  Object[][] provider_goodParseData() {
    return new Object[][] {
      {"0000", Year.of(0)},
      {"9999", Year.of(9999)},
      {"2000", Year.of(2000)},
      {"+12345678", Year.of(12345678)},
      {"+123456", Year.of(123456)},
      {"-1234", Year.of(-1234)},
      {"-12345678", Year.of(-12345678)},
      {"+" + Year.MAX_VALUE, Year.of(Year.MAX_VALUE)},
      {"" + Year.MIN_VALUE, Year.of(Year.MIN_VALUE)},
    };
  }

  @Test(/* dataProvider = "goodParseData" */ )
  public void test_factory_parse_success() {
    Object[][] data = provider_goodParseData();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_factory_parse_success((String) objects[0], (Year) objects[1]);
    }
  }

  public void test_factory_parse_success(String text, Year expected) {
    Year year = Year.parse(text);
    assertEquals(year, expected);
  }

  // @DataProvider(name="badParseData")
  Object[][] provider_badParseData() {
    return new Object[][] {
      {"", 0},
      {"-00", 1},
      {"--01-0", 1},
      {"A01", 0},
      {"200", 0},
      {"2009/12", 4},
      {"-0000-10", 0},
      {"-12345678901-10", 11},
      {"+1-10", 1},
      {"+12-10", 1},
      {"+123-10", 1},
      {"+1234-10", 0},
      {"12345-10", 0},
      {"+12345678901-10", 11},
    };
  }

  @Test(/* dataProvider = "badParseData", */ expected = DateTimeParseException.class)
  public void test_factory_parse_fail() {
    Object[][] data = provider_badParseData();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_factory_parse_fail((String) objects[0], (int) objects[1]);
    }
  }

  public void test_factory_parse_fail(String text, int pos) {
    try {
      try {
        Year.parse(text);
        fail("Parse should have failed for " + text + " at position " + pos);
      } catch (DateTimeParseException ex) {
        assertEquals(ex.getParsedString(), text);
        assertEquals(ex.getErrorIndex(), pos);
        throw ex;
      }
      fail("Missing exception");
    } catch (DateTimeParseException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_parse_nullText() {
    try {
      Year.parse(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // parse(DateTimeFormatter)
  // -----------------------------------------------------------------------
  @Test
  public void test_factory_parse_formatter() {
    DateTimeFormatter f = DateTimeFormatter.ofPattern("u");
    Year test = Year.parse("2010", f);
    assertEquals(test, Year.of(2010));
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_parse_formatter_nullText() {
    try {
      DateTimeFormatter f = DateTimeFormatter.ofPattern("u");
      Year.parse((String) null, f);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_factory_parse_formatter_nullFormatter() {
    try {
      Year.parse("ANY", null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // get(DateTimeField)
  // -----------------------------------------------------------------------
  @Test
  public void test_get_DateTimeField() {
    assertEquals(TEST_2008.getLong(ChronoField.YEAR), 2008);
    assertEquals(TEST_2008.getLong(ChronoField.YEAR_OF_ERA), 2008);
    assertEquals(TEST_2008.getLong(ChronoField.ERA), 1);
  }

  @Test(expected = NullPointerException.class)
  public void test_get_DateTimeField_null() {
    try {
      TEST_2008.getLong((TemporalField) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_get_DateTimeField_invalidField() {
    try {
      TEST_2008.getLong(MockFieldNoValue.INSTANCE);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_get_DateTimeField_timeField() {
    try {
      TEST_2008.getLong(ChronoField.AMPM_OF_DAY);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // isLeap()
  // -----------------------------------------------------------------------
  @Test
  public void test_isLeap() {
    assertEquals(Year.of(1999).isLeap(), false);
    assertEquals(Year.of(2000).isLeap(), true);
    assertEquals(Year.of(2001).isLeap(), false);

    assertEquals(Year.of(2007).isLeap(), false);
    assertEquals(Year.of(2008).isLeap(), true);
    assertEquals(Year.of(2009).isLeap(), false);
    assertEquals(Year.of(2010).isLeap(), false);
    assertEquals(Year.of(2011).isLeap(), false);
    assertEquals(Year.of(2012).isLeap(), true);

    assertEquals(Year.of(2095).isLeap(), false);
    assertEquals(Year.of(2096).isLeap(), true);
    assertEquals(Year.of(2097).isLeap(), false);
    assertEquals(Year.of(2098).isLeap(), false);
    assertEquals(Year.of(2099).isLeap(), false);
    assertEquals(Year.of(2100).isLeap(), false);
    assertEquals(Year.of(2101).isLeap(), false);
    assertEquals(Year.of(2102).isLeap(), false);
    assertEquals(Year.of(2103).isLeap(), false);
    assertEquals(Year.of(2104).isLeap(), true);
    assertEquals(Year.of(2105).isLeap(), false);

    assertEquals(Year.of(-500).isLeap(), false);
    assertEquals(Year.of(-400).isLeap(), true);
    assertEquals(Year.of(-300).isLeap(), false);
    assertEquals(Year.of(-200).isLeap(), false);
    assertEquals(Year.of(-100).isLeap(), false);
    assertEquals(Year.of(0).isLeap(), true);
    assertEquals(Year.of(100).isLeap(), false);
    assertEquals(Year.of(200).isLeap(), false);
    assertEquals(Year.of(300).isLeap(), false);
    assertEquals(Year.of(400).isLeap(), true);
    assertEquals(Year.of(500).isLeap(), false);
  }

  // -----------------------------------------------------------------------
  // plusYears()
  // -----------------------------------------------------------------------
  @Test
  public void test_plusYears() {
    assertEquals(Year.of(2007).plusYears(-1), Year.of(2006));
    assertEquals(Year.of(2007).plusYears(0), Year.of(2007));
    assertEquals(Year.of(2007).plusYears(1), Year.of(2008));
    assertEquals(Year.of(2007).plusYears(2), Year.of(2009));

    assertEquals(Year.of(Year.MAX_VALUE - 1).plusYears(1), Year.of(Year.MAX_VALUE));
    assertEquals(Year.of(Year.MAX_VALUE).plusYears(0), Year.of(Year.MAX_VALUE));

    assertEquals(Year.of(Year.MIN_VALUE + 1).plusYears(-1), Year.of(Year.MIN_VALUE));
    assertEquals(Year.of(Year.MIN_VALUE).plusYears(0), Year.of(Year.MIN_VALUE));
  }

  @Test
  public void test_plusYear_zero_equals() {
    Year base = Year.of(2007);
    assertEquals(base.plusYears(0), base);
  }

  @Test
  public void test_plusYears_big() {
    long years = 20L + Year.MAX_VALUE;
    assertEquals(Year.of(-40).plusYears(years), Year.of((int) (-40L + years)));
  }

  @Test(expected = DateTimeException.class)
  public void test_plusYears_max() {
    try {
      Year.of(Year.MAX_VALUE).plusYears(1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_plusYears_maxLots() {
    try {
      Year.of(Year.MAX_VALUE).plusYears(1000);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_plusYears_min() {
    try {
      Year.of(Year.MIN_VALUE).plusYears(-1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_plusYears_minLots() {
    try {
      Year.of(Year.MIN_VALUE).plusYears(-1000);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // minusYears()
  // -----------------------------------------------------------------------
  @Test
  public void test_minusYears() {
    assertEquals(Year.of(2007).minusYears(-1), Year.of(2008));
    assertEquals(Year.of(2007).minusYears(0), Year.of(2007));
    assertEquals(Year.of(2007).minusYears(1), Year.of(2006));
    assertEquals(Year.of(2007).minusYears(2), Year.of(2005));

    assertEquals(Year.of(Year.MAX_VALUE - 1).minusYears(-1), Year.of(Year.MAX_VALUE));
    assertEquals(Year.of(Year.MAX_VALUE).minusYears(0), Year.of(Year.MAX_VALUE));

    assertEquals(Year.of(Year.MIN_VALUE + 1).minusYears(1), Year.of(Year.MIN_VALUE));
    assertEquals(Year.of(Year.MIN_VALUE).minusYears(0), Year.of(Year.MIN_VALUE));
  }

  @Test
  public void test_minusYear_zero_equals() {
    Year base = Year.of(2007);
    assertEquals(base.minusYears(0), base);
  }

  @Test
  public void test_minusYears_big() {
    long years = 20L + Year.MAX_VALUE;
    assertEquals(Year.of(40).minusYears(years), Year.of((int) (40L - years)));
  }

  @Test(expected = DateTimeException.class)
  public void test_minusYears_max() {
    try {
      Year.of(Year.MAX_VALUE).minusYears(-1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_minusYears_maxLots() {
    try {
      Year.of(Year.MAX_VALUE).minusYears(-1000);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_minusYears_min() {
    try {
      Year.of(Year.MIN_VALUE).minusYears(1);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_minusYears_minLots() {
    try {
      Year.of(Year.MIN_VALUE).minusYears(1000);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // doAdjustment()
  // -----------------------------------------------------------------------
  @Test
  public void test_adjustDate() {
    LocalDate base = LocalDate.of(2007, 2, 12);
    for (int i = -4; i <= 2104; i++) {
      Temporal result = Year.of(i).adjustInto(base);
      assertEquals(result, LocalDate.of(i, 2, 12));
    }
  }

  @Test
  public void test_adjustDate_resolve() {
    Year test = Year.of(2011);
    assertEquals(test.adjustInto(LocalDate.of(2012, 2, 29)), LocalDate.of(2011, 2, 28));
  }

  @Test(expected = NullPointerException.class)
  public void test_adjustDate_nullLocalDate() {
    try {
      Year test = Year.of(1);
      test.adjustInto((LocalDate) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // length()
  // -----------------------------------------------------------------------
  @Test
  public void test_length() {
    assertEquals(Year.of(1999).length(), 365);
    assertEquals(Year.of(2000).length(), 366);
    assertEquals(Year.of(2001).length(), 365);

    assertEquals(Year.of(2007).length(), 365);
    assertEquals(Year.of(2008).length(), 366);
    assertEquals(Year.of(2009).length(), 365);
    assertEquals(Year.of(2010).length(), 365);
    assertEquals(Year.of(2011).length(), 365);
    assertEquals(Year.of(2012).length(), 366);

    assertEquals(Year.of(2095).length(), 365);
    assertEquals(Year.of(2096).length(), 366);
    assertEquals(Year.of(2097).length(), 365);
    assertEquals(Year.of(2098).length(), 365);
    assertEquals(Year.of(2099).length(), 365);
    assertEquals(Year.of(2100).length(), 365);
    assertEquals(Year.of(2101).length(), 365);
    assertEquals(Year.of(2102).length(), 365);
    assertEquals(Year.of(2103).length(), 365);
    assertEquals(Year.of(2104).length(), 366);
    assertEquals(Year.of(2105).length(), 365);

    assertEquals(Year.of(-500).length(), 365);
    assertEquals(Year.of(-400).length(), 366);
    assertEquals(Year.of(-300).length(), 365);
    assertEquals(Year.of(-200).length(), 365);
    assertEquals(Year.of(-100).length(), 365);
    assertEquals(Year.of(0).length(), 366);
    assertEquals(Year.of(100).length(), 365);
    assertEquals(Year.of(200).length(), 365);
    assertEquals(Year.of(300).length(), 365);
    assertEquals(Year.of(400).length(), 366);
    assertEquals(Year.of(500).length(), 365);
  }

  // -----------------------------------------------------------------------
  // isValidMonthDay(Month)
  // -----------------------------------------------------------------------
  @Test
  public void test_isValidMonthDay_june() {
    Year test = Year.of(2007);
    MonthDay monthDay = MonthDay.of(6, 30);
    assertEquals(test.isValidMonthDay(monthDay), true);
  }

  @Test
  public void test_isValidMonthDay_febNonLeap() {
    Year test = Year.of(2007);
    MonthDay monthDay = MonthDay.of(2, 29);
    assertEquals(test.isValidMonthDay(monthDay), false);
  }

  @Test
  public void test_isValidMonthDay_febLeap() {
    Year test = Year.of(2008);
    MonthDay monthDay = MonthDay.of(2, 29);
    assertEquals(test.isValidMonthDay(monthDay), true);
  }

  @Test
  public void test_isValidMonthDay_null() {
    Year test = Year.of(2008);
    assertEquals(test.isValidMonthDay(null), false);
  }

  // -----------------------------------------------------------------------
  // atMonth(Month)
  // -----------------------------------------------------------------------
  @Test
  public void test_atMonth() {
    Year test = Year.of(2008);
    assertEquals(test.atMonth(Month.JUNE), YearMonth.of(2008, 6));
  }

  @Test(expected = NullPointerException.class)
  public void test_atMonth_nullMonth() {
    try {
      Year test = Year.of(2008);
      test.atMonth((Month) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // atMonth(int)
  // -----------------------------------------------------------------------
  @Test
  public void test_atMonth_int() {
    Year test = Year.of(2008);
    assertEquals(test.atMonth(6), YearMonth.of(2008, 6));
  }

  @Test(expected = DateTimeException.class)
  public void test_atMonth_int_invalidMonth() {
    try {
      Year test = Year.of(2008);
      test.atMonth(13);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // atMonthDay(MonthDay)
  // -----------------------------------------------------------------------
  // @DataProvider(name="atMonthDay")
  Object[][] data_atMonthDay() {
    return new Object[][] {
      {Year.of(2008), MonthDay.of(6, 30), LocalDate.of(2008, 6, 30)},
      {Year.of(2008), MonthDay.of(2, 29), LocalDate.of(2008, 2, 29)},
      {Year.of(2009), MonthDay.of(2, 29), LocalDate.of(2009, 2, 28)},
    };
  }

  @Test(/* dataProvider = "atMonthDay" */ )
  public void test_atMonthDay() {
    Object[][] data = data_atMonthDay();
    for (int i = 0; i < data.length; i++) {
      Object[] objects = data[i];
      test_atMonthDay((Year) objects[0], (MonthDay) objects[1], (LocalDate) objects[2]);
    }
  }

  public void test_atMonthDay(Year year, MonthDay monthDay, LocalDate expected) {
    assertEquals(year.atMonthDay(monthDay), expected);
  }

  @Test(expected = NullPointerException.class)
  public void test_atMonthDay_nullMonthDay() {
    try {
      Year test = Year.of(2008);
      test.atMonthDay((MonthDay) null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // atDay(int)
  // -----------------------------------------------------------------------
  @Test
  public void test_atDay_notLeapYear() {
    Year test = Year.of(2007);
    LocalDate expected = LocalDate.of(2007, 1, 1);
    for (int i = 1; i <= 365; i++) {
      assertEquals(test.atDay(i), expected);
      expected = expected.plusDays(1);
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_atDay_notLeapYear_day366() {
    try {
      Year test = Year.of(2007);
      test.atDay(366);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test
  public void test_atDay_leapYear() {
    Year test = Year.of(2008);
    LocalDate expected = LocalDate.of(2008, 1, 1);
    for (int i = 1; i <= 366; i++) {
      assertEquals(test.atDay(i), expected);
      expected = expected.plusDays(1);
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_atDay_day0() {
    try {
      Year test = Year.of(2007);
      test.atDay(0);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  @Test(expected = DateTimeException.class)
  public void test_atDay_day367() {
    try {
      Year test = Year.of(2007);
      test.atDay(367);
      fail("Missing exception");
    } catch (DateTimeException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // query(TemporalQuery)
  // -----------------------------------------------------------------------
  @Test
  public void test_query() {
    assertEquals(TEST_2008.query(TemporalQueries.chronology()), IsoChronology.INSTANCE);
    assertEquals(TEST_2008.query(TemporalQueries.localDate()), null);
    assertEquals(TEST_2008.query(TemporalQueries.localTime()), null);
    assertEquals(TEST_2008.query(TemporalQueries.offset()), null);
    assertEquals(TEST_2008.query(TemporalQueries.precision()), ChronoUnit.YEARS);
    assertEquals(TEST_2008.query(TemporalQueries.zone()), null);
    assertEquals(TEST_2008.query(TemporalQueries.zoneId()), null);
  }

  @Test(expected = NullPointerException.class)
  public void test_query_null() {
    try {
      TEST_2008.query(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // compareTo()
  // -----------------------------------------------------------------------
  @Test
  public void long_test_compareTo() {
    for (int i = -4; i <= 2104; i++) {
      Year a = Year.of(i);
      for (int j = -4; j <= 2104; j++) {
        Year b = Year.of(j);
        if (i < j) {
          assertEquals(a.compareTo(b) < 0, true);
          assertEquals(b.compareTo(a) > 0, true);
          assertEquals(a.isAfter(b), false);
          assertEquals(a.isBefore(b), true);
          assertEquals(b.isAfter(a), true);
          assertEquals(b.isBefore(a), false);
        } else if (i > j) {
          assertEquals(a.compareTo(b) > 0, true);
          assertEquals(b.compareTo(a) < 0, true);
          assertEquals(a.isAfter(b), true);
          assertEquals(a.isBefore(b), false);
          assertEquals(b.isAfter(a), false);
          assertEquals(b.isBefore(a), true);
        } else {
          assertEquals(a.compareTo(b), 0);
          assertEquals(b.compareTo(a), 0);
          assertEquals(a.isAfter(b), false);
          assertEquals(a.isBefore(b), false);
          assertEquals(b.isAfter(a), false);
          assertEquals(b.isBefore(a), false);
        }
      }
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_compareTo_nullYear() {
    try {
      Year doy = null;
      Year test = Year.of(1);
      test.compareTo(doy);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }

  // -----------------------------------------------------------------------
  // equals() / hashCode()
  // -----------------------------------------------------------------------
  @Test
  public void long_test_equals() {
    for (int i = -4; i <= 2104; i++) {
      Year a = Year.of(i);
      for (int j = -4; j <= 2104; j++) {
        Year b = Year.of(j);
        assertEquals(a.equals(b), i == j);
        assertEquals(a.hashCode() == b.hashCode(), i == j);
      }
    }
  }

  @Test
  public void test_equals_same() {
    Year test = Year.of(2011);
    assertEquals(test.equals(test), true);
  }

  @Test
  public void test_equals_nullYear() {
    Year doy = null;
    Year test = Year.of(1);
    assertEquals(test.equals(doy), false);
  }

  @Test
  public void test_equals_incorrectType() {
    Year test = Year.of(1);
    assertEquals(test.equals("Incorrect type"), false);
  }

  // -----------------------------------------------------------------------
  // toString()
  // -----------------------------------------------------------------------
  @Test
  public void test_toString() {
    for (int i = -4; i <= 2104; i++) {
      Year a = Year.of(i);
      assertEquals(a.toString(), "" + i);
    }
  }

  // -----------------------------------------------------------------------
  // format(DateTimeFormatter)
  // -----------------------------------------------------------------------
  @Test
  public void test_format_formatter() {
    DateTimeFormatter f = DateTimeFormatter.ofPattern("y");
    String t = Year.of(2010).format(f);
    assertEquals(t, "2010");
  }

  @Test(expected = NullPointerException.class)
  public void test_format_formatter_null() {
    try {
      Year.of(2010).format(null);
      fail("Missing exception");
    } catch (NullPointerException e) {
      // expected
    }
  }
}
