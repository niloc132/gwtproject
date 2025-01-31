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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.user.datepicker.client.CalendarModel;
import org.gwtproject.user.datepicker.client.CalendarView;
import org.gwtproject.user.datepicker.client.DatePicker;
import org.gwtproject.user.datepicker.client.DefaultMonthSelector;

/** Tests DatePicker's public api. */
@SuppressWarnings("deprecation")
// Due to Date
@J2clTestInput(DatePickerTest.class)
public class DatePickerTest extends GWTTestCase {

  private static class DatePickerWithView extends DatePicker {
    DatePickerWithView(MockCalendarView view) {
      super(new DefaultMonthSelector(), view, new CalendarModel());
    }

    // give access to the month selector for testing purpose
    public DefaultMonthSelector getDefaultMonthSelector() {
      return (DefaultMonthSelector) super.getMonthSelector();
    }

    public CalendarModel getCalendarModel() {
      return super.getModel();
    }
  }
  /**
   * Mock calendar view pretends to show datesVisibleList from the first of the month to the 30th
   * day after that.
   */
  private static class MockCalendarView extends CalendarView {
    Map<Date, Set<String>> dateStyles = new HashMap<Date, Set<String>>();
    Set<Date> disabledDates = new HashSet<Date>();

    MockCalendarView() {
      initWidget(new Label());
    }

    @Override
    public void addStyleToDate(String styleName, Date date) {
      Set<String> fred = dateStyles.get(date);
      if (fred == null) {
        fred = new HashSet<String>();
        dateStyles.put(date, fred);
      }
      fred.add(styleName);
    }

    @Override
    public Date getFirstDate() {
      Date thisMonth = getModel().getCurrentMonth();
      return new Date(thisMonth.getYear(), thisMonth.getMonth(), 1);
    }

    @Override
    public Date getLastDate() {
      Date thisMonth = getModel().getCurrentMonth();
      return new Date(thisMonth.getYear(), thisMonth.getMonth(), 30);
    }

    @Override
    public boolean isDateEnabled(Date date) {
      return !disabledDates.contains(date);
    }

    @Override
    public void refresh() {}

    @Override
    public void removeStyleFromDate(String styleName, Date date) {
      Set<String> fred;
      assertNotNull(fred = dateStyles.get(date));
      assertTrue(fred.remove(styleName));
    }

    @Override
    public void setEnabledOnDate(boolean enabled, Date date) {
      if (enabled) {
        disabledDates.remove(date);
      } else {
        disabledDates.add(date);
      }
    }

    @Override
    protected void setup() {}
  }

  private static final String STYLE_LATER = "styleLater";

  private static final String STYLE = "style1";

  private DatePickerWithView mockedDatePicker;
  private MockCalendarView view;

  private final Date dateVisible1 = new Date(65, 6, 12);
  private final Date dateVisible2 = new Date(65, 6, 13);
  private final Date dateVisible3 = new Date(65, 6, 14);
  private final Date dateVisible4 = new Date(65, 6, 15);
  private final Date dateVisible5 = new Date(65, 6, 16);
  private final List<Date> datesVisibleList = new ArrayList<Date>();
  private final Date dateLater1 =
      new Date(dateVisible1.getYear(), dateVisible1.getMonth() + 1, dateVisible1.getDay());

  private final Date dateLater2 =
      new Date(dateVisible2.getYear(), dateVisible2.getMonth() + 1, dateVisible2.getDay());
  private final Date dateLater3 =
      new Date(dateVisible1.getYear(), dateVisible3.getMonth() + 1, dateVisible3.getDay());
  private final Date dateLater4 =
      new Date(dateVisible2.getYear(), dateVisible4.getMonth() + 1, dateVisible4.getDay());
  private final Date dateLater5 =
      new Date(dateVisible1.getYear(), dateVisible5.getMonth() + 1, dateVisible5.getDay());
  private final List<Date> datesLaterList = new ArrayList<Date>();

  {
    datesVisibleList.add(dateVisible4);
    datesVisibleList.add(dateVisible5);
  }

  {
    datesLaterList.add(dateLater4);
    datesLaterList.add(dateLater5);
  }

  @Override
  public String getModuleName() {
    return "org.gwtproject.user.Widgets";
  }

  @Override
  public void gwtSetUp() throws Exception {
    super.gwtSetUp();

    view = new MockCalendarView();
    mockedDatePicker = new DatePickerWithView(view);
    mockedDatePicker.setCurrentMonth(dateVisible1);
  }

  public void testDisabling() {
    mockedDatePicker.setTransientEnabledOnDates(false, dateVisible1);
    mockedDatePicker.setTransientEnabledOnDates(false, dateVisible2, dateVisible3);
    mockedDatePicker.setTransientEnabledOnDates(false, datesVisibleList);

    assertTrue(view.disabledDates.contains(dateVisible1));
    assertTrue(view.disabledDates.contains(dateVisible2));
    assertTrue(view.disabledDates.contains(dateVisible3));
    assertTrue(view.disabledDates.contains(dateVisible4));
    assertTrue(view.disabledDates.contains(dateVisible5));

    mockedDatePicker.setTransientEnabledOnDates(true, dateVisible1);
    mockedDatePicker.setTransientEnabledOnDates(true, dateVisible2, dateVisible3);
    mockedDatePicker.setTransientEnabledOnDates(true, datesVisibleList);

    assertFalse(view.disabledDates.contains(dateVisible1));
    assertFalse(view.disabledDates.contains(dateVisible2));
    assertFalse(view.disabledDates.contains(dateVisible3));
    assertFalse(view.disabledDates.contains(dateVisible4));
    assertFalse(view.disabledDates.contains(dateVisible5));
  }

  public void testMonthNavigation() {
    DatePickerWithView dp = new DatePickerWithView(new MockCalendarView());
    RootPanel.get().add(dp);

    Date actualDate = new Date(dp.getCalendarModel().getCurrentMonth().getTime());
    Date dateAfterOneMonth =
        new Date(actualDate.getYear(), actualDate.getMonth() + 1, actualDate.getDate());

    clickOnNavigationElement(dp.getDefaultMonthSelector().getForwardButtonElement());

    Date currentlyDisplayedDate = dp.getCalendarModel().getCurrentMonth();
    assertEquals(dateAfterOneMonth.getMonth(), currentlyDisplayedDate.getMonth());
    assertEquals(dateAfterOneMonth.getYear(), currentlyDisplayedDate.getYear());

    clickOnNavigationElement(dp.getDefaultMonthSelector().getBackwardButtonElement());

    assertEquals(actualDate.getMonth(), currentlyDisplayedDate.getMonth());
    assertEquals(actualDate.getYear(), currentlyDisplayedDate.getYear());
  }

  public void testMonthNavigationByDropDown() {
    DatePickerWithView dp = new DatePickerWithView(new MockCalendarView());
    dp.setYearAndMonthDropdownVisible(true);
    RootPanel.get().add(dp);

    Date actualDate = new Date(dp.getCalendarModel().getCurrentMonth().getTime());

    ListBox monthSelect = dp.getDefaultMonthSelector().getMonthSelectListBox();
    int newMonth = (monthSelect.getSelectedIndex() + 6) % 12;
    monthSelect.setSelectedIndex(newMonth);
    monthSelect.getElement().dispatchEvent(Document.get().createChangeEvent());

    Date dateAfter = new Date(actualDate.getYear(), newMonth, actualDate.getDate());
    Date currentlyDisplayedDate = dp.getCalendarModel().getCurrentMonth();

    assertEquals(dateAfter.getMonth(), currentlyDisplayedDate.getMonth());
    assertEquals(dateAfter.getYear(), currentlyDisplayedDate.getYear());
  }

  public void testMonthYearNotSelectableByDefault() {
    DatePickerWithView dp = new DatePickerWithView(new MockCalendarView());
    RootPanel.get().add(dp);

    assertFalse(dp.getDefaultMonthSelector().getYearSelectListBox().isAttached());
    assertFalse(dp.getDefaultMonthSelector().getMonthSelectListBox().isAttached());

    String formattedDate = dp.getCalendarModel().formatCurrentMonthAndYear();
    String expectedInnerText = "‹" + formattedDate + "›";
    assertEquals(expectedInnerText, dp.getDefaultMonthSelector().getElement().getInnerText());
  }

  public void testStyleSetting() {
    mockedDatePicker.addStyleToDates(STYLE, dateVisible1);
    mockedDatePicker.addStyleToDates(STYLE, dateVisible2, dateVisible3);
    mockedDatePicker.addStyleToDates(STYLE, datesVisibleList);

    assertViewHasStyleOnVisibleDates(STYLE);
    assertPickerHasStyleOnVisibleDates(STYLE);

    // See that styles on an invisible datesVisibleList don't

    mockedDatePicker.addStyleToDates(STYLE_LATER, dateLater1);
    mockedDatePicker.addStyleToDates(STYLE_LATER, dateLater2, dateLater3);
    mockedDatePicker.addStyleToDates(STYLE_LATER, datesLaterList);

    assertViewHasNoStyleOnHiddenDates();
    assertPickerLacksStyleOnHiddenDates(STYLE_LATER);

    // Remove a style from a visible date, and it should leave the view too
    mockedDatePicker.removeStyleFromDates(STYLE, dateVisible1);
    mockedDatePicker.removeStyleFromDates(STYLE, dateVisible2, dateVisible3);
    mockedDatePicker.removeStyleFromDates(STYLE, datesVisibleList);

    assertViewLacksStyleOnVisibleDates(STYLE);
    assertPickerLacksStyleOnVisibleDates();

    // Remove a style from an invisible date, and the view should not hear
    // about it (the mock will explode if asked to remove a style it doesn't
    // have)
    mockedDatePicker.removeStyleFromDates(STYLE_LATER, dateLater1);
    mockedDatePicker.removeStyleFromDates(STYLE_LATER, dateLater2, dateLater3);
    mockedDatePicker.removeStyleFromDates(STYLE_LATER, datesLaterList);
    assertPickerHasNoStyleOnInvisibleDates();
  }

  public void testTransientStyles() {
    mockedDatePicker.addTransientStyleToDates(STYLE, dateVisible1);
    mockedDatePicker.addTransientStyleToDates(STYLE, dateVisible2, dateVisible3);
    mockedDatePicker.addTransientStyleToDates(STYLE, datesVisibleList);
    assertViewHasStyleOnVisibleDates(STYLE);
    assertPickerLacksStyleOnVisibleDates();

    mockedDatePicker.removeStyleFromDates(STYLE, dateVisible1);
    mockedDatePicker.removeStyleFromDates(STYLE, dateVisible2, dateVisible3);
    mockedDatePicker.removeStyleFromDates(STYLE, datesVisibleList);
    assertViewLacksStyleOnVisibleDates(STYLE);
    assertPickerLacksStyleOnVisibleDates();
  }

  public void testValueChangeEvent() {
    DatePicker dp = new DatePicker();
    RootPanel.get().add(dp);
    new DateValueChangeTester(dp).run();
  }

  public void testValueStyle() {
    assertNull(mockedDatePicker.getStyleOfDate(dateVisible4));

    mockedDatePicker.setValue(dateVisible4);
    assertTrue(mockedDatePicker.getStyleOfDate(dateVisible4).contains("datePickerDayIsValue"));
    assertTrue(view.dateStyles.get(dateVisible4).contains("datePickerDayIsValue"));

    mockedDatePicker.setValue(dateVisible5);
    assertNull(mockedDatePicker.getStyleOfDate(dateVisible4));
    assertFalse(view.dateStyles.get(dateVisible4).contains("datePickerDayIsValue"));
  }

  public void testYearNavigation() {
    DatePickerWithView dp = new DatePickerWithView(new MockCalendarView());
    dp.setYearArrowsVisible(true);
    RootPanel.get().add(dp);

    Date actualDate = new Date(dp.getCalendarModel().getCurrentMonth().getTime());
    Date dateAfterOneYear =
        new Date(actualDate.getYear() + 1, actualDate.getMonth(), actualDate.getDate());

    clickOnNavigationElement(dp.getDefaultMonthSelector().getYearForwardButtonElement());

    Date currentlyDisplayedDate = dp.getCalendarModel().getCurrentMonth();
    assertEquals(dateAfterOneYear.getMonth(), currentlyDisplayedDate.getMonth());
    assertEquals(dateAfterOneYear.getYear(), currentlyDisplayedDate.getYear());

    clickOnNavigationElement(dp.getDefaultMonthSelector().getYearBackwardButtonElement());

    assertEquals(actualDate.getMonth(), currentlyDisplayedDate.getMonth());
    assertEquals(actualDate.getYear(), currentlyDisplayedDate.getYear());
  }

  public void testYearNavigationByDropDown() {
    DatePickerWithView dp = new DatePickerWithView(new MockCalendarView());
    dp.setYearAndMonthDropdownVisible(true);
    RootPanel.get().add(dp);

    Date actualDate = new Date(dp.getCalendarModel().getCurrentMonth().getTime());

    ListBox yearSelect = dp.getDefaultMonthSelector().getYearSelectListBox();
    int newYear = yearSelect.getSelectedIndex() + 5;
    yearSelect.setSelectedIndex(newYear);
    yearSelect.getElement().dispatchEvent(Document.get().createChangeEvent());

    Date dateAfter =
        new Date(actualDate.getYear() + 5, actualDate.getMonth(), actualDate.getDate());
    Date currentlyDisplayedDate = dp.getCalendarModel().getCurrentMonth();

    assertEquals(dateAfter.getMonth(), currentlyDisplayedDate.getMonth());
    assertEquals(dateAfter.getYear(), currentlyDisplayedDate.getYear());
  }

  public void testVisibleYearCount() {
    DatePickerWithView dp = new DatePickerWithView(new MockCalendarView());
    dp.setYearAndMonthDropdownVisible(true);
    dp.setCurrentMonth(new Date(82, 6, 10));
    RootPanel.get().add(dp);

    ListBox yearSelect = dp.getDefaultMonthSelector().getYearSelectListBox();

    assertEquals(21 /* the default */, yearSelect.getItemCount());
    assertEquals("1972", yearSelect.getItemText(0));
    assertEquals("1982", yearSelect.getItemText(10));
    assertEquals("1992", yearSelect.getItemText(20));

    dp.setVisibleYearCount(1);
    assertEquals(1, yearSelect.getItemCount());
    assertEquals("1982", yearSelect.getItemText(0));

    dp.setVisibleYearCount(2);
    assertEquals(2, yearSelect.getItemCount());
    assertEquals("1982", yearSelect.getItemText(0));
    assertEquals("1983", yearSelect.getItemText(1));

    dp.setVisibleYearCount(3);
    assertEquals(3, yearSelect.getItemCount());
    assertEquals("1981", yearSelect.getItemText(0));
    assertEquals("1982", yearSelect.getItemText(1));
    assertEquals("1983", yearSelect.getItemText(2));

    dp.setVisibleYearCount(10);
    assertEquals(10, yearSelect.getItemCount());
    assertEquals("1978", yearSelect.getItemText(0));
    assertEquals("1982", yearSelect.getItemText(4));
    assertEquals("1987", yearSelect.getItemText(9));

    dp.setVisibleYearCount(11);
    assertEquals(11, yearSelect.getItemCount());
    assertEquals("1977", yearSelect.getItemText(0));
    assertEquals("1982", yearSelect.getItemText(5));
    assertEquals("1987", yearSelect.getItemText(10));
  }

  public void testYearArrowsVisibility() {
    DatePickerWithView dp = new DatePickerWithView(new MockCalendarView());
    RootPanel.get().add(dp);

    assertNull(dp.getDefaultMonthSelector().getYearForwardButtonElement().getParentElement());
    assertNull(dp.getDefaultMonthSelector().getYearBackwardButtonElement().getParentElement());

    dp.setYearArrowsVisible(true);
    assertNotNull(dp.getDefaultMonthSelector().getYearForwardButtonElement().getParentElement());
    assertNotNull(dp.getDefaultMonthSelector().getYearBackwardButtonElement().getParentElement());

    dp.setYearArrowsVisible(false);
    assertNull(dp.getDefaultMonthSelector().getYearForwardButtonElement().getParentElement());
    assertNull(dp.getDefaultMonthSelector().getYearBackwardButtonElement().getParentElement());
  }

  private void assertPickerHasNoStyleOnInvisibleDates() {
    assertNull(mockedDatePicker.getStyleOfDate(dateLater1));
    assertNull(mockedDatePicker.getStyleOfDate(dateLater2));
    assertNull(mockedDatePicker.getStyleOfDate(dateLater3));
    assertNull(mockedDatePicker.getStyleOfDate(dateLater4));
    assertNull(mockedDatePicker.getStyleOfDate(dateLater5));
  }

  private void assertPickerHasStyleOnVisibleDates(String style) {
    assertTrue(mockedDatePicker.getStyleOfDate(dateVisible1).contains(style));
    assertTrue(mockedDatePicker.getStyleOfDate(dateVisible2).contains(style));
    assertTrue(mockedDatePicker.getStyleOfDate(dateVisible3).contains(style));
    assertTrue(mockedDatePicker.getStyleOfDate(dateVisible4).contains(style));
    assertTrue(mockedDatePicker.getStyleOfDate(dateVisible5).contains(style));
  }

  private void assertPickerLacksStyleOnHiddenDates(String styleLater) {
    assertTrue(mockedDatePicker.getStyleOfDate(dateLater1).contains(styleLater));
    assertTrue(mockedDatePicker.getStyleOfDate(dateLater2).contains(styleLater));
    assertTrue(mockedDatePicker.getStyleOfDate(dateLater3).contains(styleLater));
    assertTrue(mockedDatePicker.getStyleOfDate(dateLater4).contains(styleLater));
    assertTrue(mockedDatePicker.getStyleOfDate(dateLater5).contains(styleLater));
  }

  private void assertPickerLacksStyleOnVisibleDates() {
    assertNull(mockedDatePicker.getStyleOfDate(dateVisible1));
    assertNull(mockedDatePicker.getStyleOfDate(dateVisible2));
    assertNull(mockedDatePicker.getStyleOfDate(dateVisible3));
    assertNull(mockedDatePicker.getStyleOfDate(dateVisible4));
    assertNull(mockedDatePicker.getStyleOfDate(dateVisible5));
  }

  private void assertViewHasNoStyleOnHiddenDates() {
    assertNull(view.dateStyles.get(dateLater1));
    assertNull(view.dateStyles.get(dateLater2));
    assertNull(view.dateStyles.get(dateLater3));
    assertNull(view.dateStyles.get(dateLater4));
    assertNull(view.dateStyles.get(dateLater5));
  }

  private void assertViewHasStyleOnVisibleDates(String style) {
    assertTrue(view.dateStyles.get(dateVisible1).contains(style));
    assertTrue(view.dateStyles.get(dateVisible2).contains(style));
    assertTrue(view.dateStyles.get(dateVisible3).contains(style));
    assertTrue(view.dateStyles.get(dateVisible4).contains(style));
    assertTrue(view.dateStyles.get(dateVisible5).contains(style));
  }

  private void assertViewLacksStyleOnVisibleDates(String style) {
    assertFalse(view.dateStyles.get(dateVisible1).contains(style));
    assertFalse(view.dateStyles.get(dateVisible2).contains(style));
    assertFalse(view.dateStyles.get(dateVisible3).contains(style));
    assertFalse(view.dateStyles.get(dateVisible4).contains(style));
    assertFalse(view.dateStyles.get(dateVisible5).contains(style));
  }

  private void clickOnNavigationElement(Element e) {
    e.dispatchEvent(
        Document.get()
            .createMouseOverEvent(
                1, 0, 0, 0, 0, false, false, false, false, NativeEvent.BUTTON_LEFT, null));
    e.dispatchEvent(
        Document.get()
            .createMouseDownEvent(
                1, 0, 0, 0, 0, false, false, false, false, NativeEvent.BUTTON_LEFT));
    e.dispatchEvent(
        Document.get()
            .createMouseUpEvent(
                1, 0, 0, 0, 0, false, false, false, false, NativeEvent.BUTTON_LEFT));
  }
}
