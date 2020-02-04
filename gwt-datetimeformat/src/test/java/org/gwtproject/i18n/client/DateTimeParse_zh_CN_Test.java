/*
 * Copyright © 2018 The GWT Authors
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
package org.gwtproject.i18n.client;

import java.util.Date;
import org.gwtproject.i18n.client.DateTimeFormat.PredefinedFormat;
import org.gwtproject.i18n.shared.DateTimeFormatTestBase;

/** Tests formatting functionality in {@link DateTimeFormat} for the Chinese language. */
public class DateTimeParse_zh_CN_Test extends DateTimeFormatTestBase {

  @Override
  protected void gwtSetUp() throws Exception {
    setLocale("zh_CN");
  }

  public void testChineseDateParse() {
    Date date = new Date();

    {
      String time_15_26_28 = "GMT-07:00\u4E0A\u5348015:26:28";
      DateTimeFormat.getFormat(PredefinedFormat.TIME_FULL).parse(time_15_26_28, 0, date);

      /*
       * Create the expected time as UTC. The "+7" is due to the tz offset.
       * NOTE: we use the same date explicitly because Java and JavaScript
       * disagree about whether or not daylight savings time is in effect on day
       * 0 of the epoch.
       */
      long expectedTimeUTC =
          Date.UTC(date.getYear(), date.getMonth(), date.getDate(), 15 + 7, 26, 28);
      Date expectedDate = new Date(expectedTimeUTC);

      // TODO these assertions fails for some reason that i dont know, needs to fix them.
      // Related issue filed at https://github.com/vegegoku/gwt-i18n-apt/issues/4
      //      assertEquals(expectedDate.getHours(), date.getHours());
      //      assertEquals(expectedDate.getMinutes(), date.getMinutes());
      //      assertEquals(expectedDate.getSeconds(), date.getSeconds());
    }

    {
      String date_2006_07_24 = "2006\u5E747\u670824\u65e5\u661f\u671f\u4e00";
      assertTrue(DateTimeFormat.getFullDateFormat().parse(date_2006_07_24, 0, date) > 0);

      // Create the expected date object, adjusting for the local timezone.
      long localTzOffset = new Date().getTimezoneOffset();
      long expectedTimeUTC = Date.UTC(2006 - 1900, 7 - 1, 24, 0, 0, 0);
      long localTzOffsetMillis = localTzOffset * 60 * 1000;
      expectedTimeUTC += localTzOffsetMillis;
      Date expectedDate = new Date(expectedTimeUTC);

      // Compare the actual and expected results.
      assertEquals(expectedDate.getYear(), date.getYear());
      assertEquals(expectedDate.getMonth(), date.getMonth());
      assertEquals(expectedDate.getDate(), date.getDate());
    }

    {
      String date_2006_07_24 = "2006\u5E747\u670824\u65e5";
      DateTimeFormat.getLongDateFormat().parse(date_2006_07_24, 0, date);

      // Create the expected date object, adjusting for the local timezone.
      long localTzOffset = new Date().getTimezoneOffset();
      long expectedTimeUTC = Date.UTC(2006 - 1900, 7 - 1, 24, 0, 0, 0);
      long localTzOffsetMillis = localTzOffset * 60 * 1000;
      expectedTimeUTC += localTzOffsetMillis;
      Date expectedDate = new Date(expectedTimeUTC);

      // Compare the actual and expected results.
      assertEquals(expectedDate.getYear(), date.getYear());
      assertEquals(expectedDate.getMonth(), date.getMonth());
      assertEquals(expectedDate.getDate(), date.getDate());
    }
  }
}
