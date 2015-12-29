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
import org.ojai.types.OTime;

public class TestTime extends BaseTest {
  private final DateFormat SHORT_TIME_FORMAT = new SimpleDateFormat("HH:mm");
  private final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:sss");
  private final DateFormat FULL_TIME_FORMAT = new SimpleDateFormat("HH:mm:sss.SSS");

  @Test
  public void testTimeParsed() throws ParseException {
    OTime time = OTime.parse("00:00");
    assertEquals(0, time.getHour());
    assertEquals(0, time.getMinute());
    assertEquals(0, time.getSecond());
    assertEquals(0, time.getMilliSecond());
    assertEquals(0, time.toTimeInMillis());
    assertEquals(SHORT_TIME_FORMAT.parse("00:00"), time.toDate());
    assertEquals("00:00:00.000", time.toString());
    assertEquals("00", time.toString("HH"));
    assertEquals("00:00", time.toString("HH:mm"));
    assertEquals("00:00:00", time.toString("HH:mm:ss"));
    assertEquals("00:00:00.000", time.toString("HH:mm:ss.SSS"));

    time = OTime.parse("1:25:29.345");
    assertEquals(1, time.getHour());
    assertEquals(25, time.getMinute());
    assertEquals(29, time.getSecond());
    assertEquals(345, time.getMilliSecond());
    assertEquals(5129345, time.toTimeInMillis());
    assertEquals(FULL_TIME_FORMAT.parse("1:25:29.345"), time.toDate());
    assertEquals("01:25:29.345", time.toString());
    assertEquals("01", time.toString("HH"));
    assertEquals("01:25", time.toString("HH:mm"));
    assertEquals("01:25:29", time.toString("HH:mm:ss"));
    assertEquals("01:25:29.345", time.toString("HH:mm:ss.SSS"));

    time = OTime.parse("23:59:59.999");
    assertEquals(23, time.getHour());
    assertEquals(59, time.getMinute());
    assertEquals(59, time.getSecond());
    assertEquals(999, time.getMilliSecond());
    assertEquals(86399999, time.toTimeInMillis());
    assertEquals(FULL_TIME_FORMAT.parse("23:59:59.999"), time.toDate());
    assertEquals("23:59:59.999", time.toString());
    assertEquals("23", time.toString("HH"));
    assertEquals("23:59", time.toString("HH:mm"));
    assertEquals("23:59:59", time.toString("HH:mm:ss"));
    assertEquals("23:59:59.999", time.toString("HH:mm:ss.SSS"));
  }

  @Test
  public void testTimeFromMillisOfDay() throws ParseException {
    OTime time = OTime.fromMillisOfDay(0);
    assertEquals(0, time.getHour());
    assertEquals(0, time.getMinute());
    assertEquals(0, time.getSecond());
    assertEquals(0, time.getMilliSecond());
    assertEquals(0, time.toTimeInMillis());
    assertEquals(TIME_FORMAT.parse("00:00:00"), time.toDate());
    assertEquals("00:00:00.000", time.toString());
    assertEquals("00", time.toString("HH"));
    assertEquals("00:00", time.toString("HH:mm"));
    assertEquals("00:00:00", time.toString("HH:mm:ss"));
    assertEquals("00:00:00.000", time.toString("HH:mm:ss.SSS"));
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testTimeFromJavaUtilDate() throws ParseException {
    Date utilDate = new Date(0, 0, 0, 16, 32, 43);
    OTime time = new OTime(utilDate);
    assertEquals(16, time.getHour());
    assertEquals(32, time.getMinute());
    assertEquals(43, time.getSecond());
    assertEquals(0, time.getMilliSecond());
    assertEquals(59563000, time.toTimeInMillis());
    assertEquals(TIME_FORMAT.parse("16:32:43"), time.toDate());
    assertEquals("16:32:43.000", time.toString());
    assertEquals("16", time.toString("HH"));
    assertEquals("16:32", time.toString("HH:mm"));
    assertEquals("16:32:43", time.toString("HH:mm:ss"));
    assertEquals("16:32:43.000", time.toString("HH:mm:ss.SSS"));
  }

  @Test
  // must set "-Duser.timezone=GMT-07" to pass
  public void testTimeFromEpochMillis() throws ParseException {
    long epoch = 1451498715641L;
    OTime time = new OTime(epoch);
    assertEquals(11, time.getHour());
    assertEquals(05, time.getMinute());
    assertEquals(15, time.getSecond());
    assertEquals(641, time.getMilliSecond());
    assertEquals(39915641, time.toTimeInMillis());
    assertEquals(FULL_TIME_FORMAT.parse("11:05:15.641"), time.toDate());
    assertEquals("11:05:15.641", time.toString());
    assertEquals("11", time.toString("HH"));
    assertEquals("11:05", time.toString("HH:mm"));
    assertEquals("11:05:15", time.toString("HH:mm:ss"));
    assertEquals("11:05:15.641", time.toString("HH:mm:ss.SSS"));

    epoch = 0L;
    time = new OTime(epoch);
    assertEquals(17, time.getHour()); // 00:00 hrs in GMT is 17:00 hrs in GMT-07
    assertEquals(0, time.getMinute());
    assertEquals(0, time.getSecond());
    assertEquals(0, time.getMilliSecond());
    assertEquals(61200000, time.toTimeInMillis());
    assertEquals(FULL_TIME_FORMAT.parse("17:00:00.000"), time.toDate());
    assertEquals("17:00:00.000", time.toString());
    assertEquals("17", time.toString("HH"));
    assertEquals("17:00", time.toString("HH:mm"));
    assertEquals("17:00:00", time.toString("HH:mm:ss"));
    assertEquals("17:00:00.000", time.toString("HH:mm:ss.SSS"));
  }

}
