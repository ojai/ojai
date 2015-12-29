/**
 * Copyright (c) 2015 MapR, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ojai.tests.types;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.ojai.tests.BaseTest;
import org.ojai.types.ODate;

public class TestDate extends BaseTest {

  private final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  @Test
  public void testDateParsed() throws ParseException {
    ODate date = ODate.parse("1970-1-2");
    assertEquals(1970, date.getYear());
    assertEquals(1, date.getMonth());
    assertEquals(2, date.getDayOfMonth());
    assertEquals(1, date.toDaysSinceEpoch());
    assertEquals(ISO_DATE_FORMAT.parse("1970-1-2"), date.toDate());

    date = ODate.parse("1970-1-1");
    assertEquals(1970, date.getYear());
    assertEquals(1, date.getMonth());
    assertEquals(1, date.getDayOfMonth());
    assertEquals(0, date.toDaysSinceEpoch());
    assertEquals(ISO_DATE_FORMAT.parse("1970-1-1"), date.toDate());

    date = ODate.parse("1969-12-31");
    assertEquals(1969, date.getYear());
    assertEquals(12, date.getMonth());
    assertEquals(31, date.getDayOfMonth());
    assertEquals(-1, date.toDaysSinceEpoch());
    assertEquals(ISO_DATE_FORMAT.parse("1969-12-31"), date.toDate());

    date = ODate.parse("2015-12-31");
    assertEquals(2015, date.getYear());
    assertEquals(12, date.getMonth());
    assertEquals(31, date.getDayOfMonth());
    assertEquals(16800, date.toDaysSinceEpoch());
    assertEquals(ISO_DATE_FORMAT.parse("2015-12-31"), date.toDate());
  }

  @Test
  // must set "-Duser.timezone=GMT-07" to pass
  public void testDateFromEpochMillis() throws ParseException {
    long epoch = 1451498715641L;
    ODate date = new ODate(epoch);
    assertEquals(2015, date.getYear());
    assertEquals(12, date.getMonth());
    assertEquals(30, date.getDayOfMonth());
    assertEquals(16799, date.toDaysSinceEpoch());
    assertEquals(ISO_DATE_FORMAT.parse("2015-12-30"), date.toDate());

    epoch = 0L; // 1970-01-01 hrs in GMT is 1969-12-31 hrs in GMT-07
    date = new ODate(epoch);
    assertEquals(1969, date.getYear());
    assertEquals(12, date.getMonth());
    assertEquals(31, date.getDayOfMonth());
    assertEquals(-1, date.toDaysSinceEpoch());
    assertEquals(ISO_DATE_FORMAT.parse("1969-12-31"), date.toDate());
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testDateFromJavaUtilDate() throws ParseException {
    Date utilDate = ISO_DATE_FORMAT.parse("2015-12-31");
    ODate date = new ODate(utilDate);
    assertEquals(2015, date.getYear());
    assertEquals(12, date.getMonth());
    assertEquals(31, date.getDayOfMonth());
    assertEquals(16800, date.toDaysSinceEpoch());
    assertEquals(utilDate, date.toDate());

    utilDate = new Date(2015-1900, 12-1, 31);
    date = new ODate(utilDate);
    assertEquals(2015, date.getYear());
    assertEquals(12, date.getMonth());
    assertEquals(31, date.getDayOfMonth());
    assertEquals(16800, date.toDaysSinceEpoch());
    assertEquals(utilDate, date.toDate());
  }

  @Test
  public void testDateFromFields() throws ParseException {
    ODate date = new ODate(2015, 12, 31);
    assertEquals(2015, date.getYear());
    assertEquals(12, date.getMonth());
    assertEquals(31, date.getDayOfMonth());
    assertEquals(16800, date.toDaysSinceEpoch());
    assertEquals(ISO_DATE_FORMAT.parse("2015-12-31"), date.toDate());
  }

}
