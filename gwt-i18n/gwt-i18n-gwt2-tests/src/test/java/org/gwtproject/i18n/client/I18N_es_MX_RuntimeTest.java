/*
 * Copyright 2007 Google Inc.
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
package org.gwtproject.i18n.client;

import com.google.gwt.junit.client.GWTTestCase;
import org.gwtproject.i18n.client.I18N_es_MX_Test.MyConstants;
import org.gwtproject.i18n.client.I18N_es_MX_Test.MyMessages;
import org.gwtproject.i18n.shared.cldr.CurrencyData;
import org.gwtproject.i18n.shared.cldr.CurrencyList;

/** Tests regional inheritance for es_MX. */
public class I18N_es_MX_RuntimeTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "org.gwtproject.i18n.I18NTest_es_MX_runtime";
  }

  public void testCurrencyNames() {
    assertEquals("peso argentino", CurrencyList.get().lookupName("ARS"));
    assertEquals("peso mexicano", CurrencyList.get().lookupName("MXN"));
    assertEquals("dólar estadounidense", CurrencyList.get().lookupName("USD"));
  }

  public void testDefaultCurrency() {
    CurrencyData data = CurrencyList.get().getDefault();
    assertEquals("MXN", data.getCurrencyCode());
    assertEquals("$", data.getCurrencySymbol());
    assertEquals(2, data.getDefaultFractionDigits());
  }

  public void testOtherCurrency() {
    CurrencyData ars = CurrencyList.get().lookup("ARS");
    assertEquals("ARS", ars.getCurrencyCode());
    assertEquals("AR$", ars.getCurrencySymbol());
    assertEquals(2, ars.getDefaultFractionDigits());
    CurrencyData data = CurrencyList.get().lookup("MXN");
    assertEquals("MXN", data.getCurrencyCode());
    assertEquals("$", data.getCurrencySymbol());
    assertEquals(2, data.getDefaultFractionDigits());
    CurrencyData usd = CurrencyList.get().lookup("USD");
    assertEquals("USD", usd.getCurrencyCode());
    assertEquals("US$", usd.getCurrencySymbol());
    assertEquals(2, usd.getDefaultFractionDigits());
    boolean found = false;
    for (CurrencyData it : CurrencyList.get()) {
      if ("USD".equals(it.getCurrencyCode())) {
        assertEquals("US$", it.getCurrencySymbol());
        assertEquals(2, it.getDefaultFractionDigits());
        found = true;
        break;
      }
    }
    assertTrue("Did not find USD in iterator", found);
  }

  public void testRegionalInheritance() {
    MyMessages msg = I18N_es_MX_TestMyMessagesFactory.get();
    assertEquals("es_419", msg.getSourceLocale());
    MyConstants cst = new I18N_es_MX_TestMyConstants_();
    // Since our compile-time locale is es_419 (Latin America), we do
    // not get es_019 (Central America) in the inheritance chain for
    // es_MX as only the compile-time locales are used for translation
    // inheritance.
    assertEquals("default", cst.getSourceLocale());
  }

  public void testRuntimeLocale() {
    assertEquals("es_MX", LocaleInfo.getCurrentLocale().getLocaleName());
  }
}
