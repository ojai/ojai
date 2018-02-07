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

import java.util.Date;

import org.junit.Test;
import org.ojai.tests.BaseTest;
import org.ojai.types.OTimestamp;

public class TestTimestamp extends BaseTest {

  @Test
  public void testTimeParsed() {
    OTimestamp timestamp = OTimestamp.parse("0000-01-01T00:00:00.000Z");
    assertEquals(0, timestamp.getYear());
    assertEquals(1, timestamp.getMonth());
    assertEquals(1, timestamp.getDayOfMonth());
    assertEquals(0, timestamp.getHour());
    assertEquals(0, timestamp.getMinute());
    assertEquals(0, timestamp.getSecond());
    assertEquals(0, timestamp.getMilliSecond());
    assertEquals(-62167219200000L, timestamp.getMillis());
    assertEquals("0000-01-01T00:00:00.000Z", timestamp.toString());
    assertEquals("0000-01-01T00:00:00.000Z", timestamp.toUTCString());
    assertEquals("-0001-12-31T17:00:00.000-0700", timestamp.toLocalString());
    assertEquals("00", timestamp.toString("HH"));
    assertEquals("00:00", timestamp.toString("HH:mm"));
    assertEquals("00:00:00", timestamp.toString("HH:mm:ss"));
    assertEquals("00:00:00.000", timestamp.toString("HH:mm:ss.SSS"));
    assertEquals("00", timestamp.toString("yy"));
    assertEquals("0000", timestamp.toString("yyyy"));
    assertEquals("0000-01", timestamp.toString("yyyy-MM"));
    assertEquals("0000-01-01", timestamp.toString("yyyy-MM-dd"));

    timestamp = OTimestamp.parse("1970-1-1T0:0:0.0Z");
    assertEquals(1970, timestamp.getYear());
    assertEquals(1, timestamp.getMonth());
    assertEquals(1, timestamp.getDayOfMonth());
    assertEquals(0, timestamp.getHour());
    assertEquals(0, timestamp.getMinute());
    assertEquals(0, timestamp.getSecond());
    assertEquals(0, timestamp.getMilliSecond());
    assertEquals(0, timestamp.getMillis());
    assertEquals("1970-01-01T00:00:00.000Z", timestamp.toString());
    assertEquals("1970-01-01T00:00:00.000Z", timestamp.toUTCString());
    assertEquals("1969-12-31T17:00:00.000-0700", timestamp.toLocalString());
    assertEquals("00", timestamp.toString("HH"));
    assertEquals("00:00", timestamp.toString("HH:mm"));
    assertEquals("00:00:00", timestamp.toString("HH:mm:ss"));
    assertEquals("00:00:00.000", timestamp.toString("HH:mm:ss.SSS"));
    assertEquals("70", timestamp.toString("yy"));
    assertEquals("1970", timestamp.toString("yyyy"));
    assertEquals("1970-01", timestamp.toString("yyyy-MM"));
    assertEquals("1970-01-01", timestamp.toString("yyyy-MM-dd"));

    timestamp = OTimestamp.parse("1969-12-31T23:59:59.999Z");
    assertEquals(1969, timestamp.getYear());
    assertEquals(12, timestamp.getMonth());
    assertEquals(31, timestamp.getDayOfMonth());
    assertEquals(23, timestamp.getHour());
    assertEquals(59, timestamp.getMinute());
    assertEquals(59, timestamp.getSecond());
    assertEquals(999, timestamp.getMilliSecond());
    assertEquals(-1, timestamp.getMillis());
    assertEquals("1969-12-31T23:59:59.999Z", timestamp.toString());
    assertEquals("1969-12-31T23:59:59.999Z", timestamp.toUTCString());
    assertEquals("1969-12-31T16:59:59.999-0700", timestamp.toLocalString());
    assertEquals("23", timestamp.toString("HH"));
    assertEquals("23:59", timestamp.toString("HH:mm"));
    assertEquals("23:59:59", timestamp.toString("HH:mm:ss"));
    assertEquals("23:59:59.999", timestamp.toString("HH:mm:ss.SSS"));
    assertEquals("69", timestamp.toString("yy"));
    assertEquals("1969", timestamp.toString("yyyy"));
    assertEquals("1969-12", timestamp.toString("yyyy-MM"));
    assertEquals("1969-12-31", timestamp.toString("yyyy-MM-dd"));

    timestamp = OTimestamp.parse("2015-12-31T23:59:59.999Z");
    assertEquals(2015, timestamp.getYear());
    assertEquals(12, timestamp.getMonth());
    assertEquals(31, timestamp.getDayOfMonth());
    assertEquals(23, timestamp.getHour());
    assertEquals(59, timestamp.getMinute());
    assertEquals(59, timestamp.getSecond());
    assertEquals(999, timestamp.getMilliSecond());
    assertEquals(1451606399999L, timestamp.getMillis());
    assertEquals("2015-12-31T23:59:59.999Z", timestamp.toString());
    assertEquals("2015-12-31T23:59:59.999Z", timestamp.toUTCString());
    assertEquals("2015-12-31T16:59:59.999-0700", timestamp.toLocalString());
    assertEquals("23", timestamp.toString("HH"));
    assertEquals("23:59", timestamp.toString("HH:mm"));
    assertEquals("23:59:59", timestamp.toString("HH:mm:ss"));
    assertEquals("23:59:59.999", timestamp.toString("HH:mm:ss.SSS"));
    assertEquals("15", timestamp.toString("yy"));
    assertEquals("2015", timestamp.toString("yyyy"));
    assertEquals("2015-12", timestamp.toString("yyyy-MM"));
    assertEquals("2015-12-31", timestamp.toString("yyyy-MM-dd"));

    timestamp = OTimestamp.parse("2015-12-31T23:59:59.999");
    assertEquals(2016, timestamp.getYear());
    assertEquals(1, timestamp.getMonth());
    assertEquals(1, timestamp.getDayOfMonth());
    assertEquals(6, timestamp.getHour());
    assertEquals(59, timestamp.getMinute());
    assertEquals(59, timestamp.getSecond());
    assertEquals(999, timestamp.getMilliSecond());
    assertEquals(1451631599999L, timestamp.getMillis()); // in GMT-07
    assertEquals("2016-01-01T06:59:59.999Z", timestamp.toString());
    assertEquals("2016-01-01T06:59:59.999Z", timestamp.toUTCString());
    assertEquals("2015-12-31T23:59:59.999-0700", timestamp.toLocalString());
    assertEquals("23", timestamp.toString("HH"));
    assertEquals("23:59", timestamp.toString("HH:mm"));
    assertEquals("23:59:59", timestamp.toString("HH:mm:ss"));
    assertEquals("23:59:59.999", timestamp.toString("HH:mm:ss.SSS"));
    assertEquals("15", timestamp.toString("yy"));
    assertEquals("2015", timestamp.toString("yyyy"));
    assertEquals("2015-12", timestamp.toString("yyyy-MM"));
    assertEquals("2015-12-31", timestamp.toString("yyyy-MM-dd"));
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testTimeFromJavaUtilDate() {
    Date utilDate = new Date(70, 0, 1, 0, 0, 0);
    OTimestamp timestamp = new OTimestamp(utilDate);
    assertEquals(utilDate, timestamp.toDate());
    assertEquals(1970, timestamp.getYear());
    assertEquals(1, timestamp.getMonth());
    assertEquals(1, timestamp.getDayOfMonth());
    assertEquals(7, timestamp.getHour());
    assertEquals(0, timestamp.getMinute());
    assertEquals(0, timestamp.getSecond());
    assertEquals(0, timestamp.getMilliSecond());
    assertEquals(25200000L, timestamp.getMillis());
    assertEquals("1970-01-01T07:00:00.000Z", timestamp.toString());
    assertEquals("1970-01-01T07:00:00.000Z", timestamp.toUTCString());
    assertEquals("1970-01-01T00:00:00.000-0700", timestamp.toLocalString());
  }

  @Test
  // must set "-Duser.timezone=GMT-07" to pass
  public void testTimeFromEpochMillis() {
    long epoch = 1451498715641L;
    OTimestamp timestamp = new OTimestamp(epoch);
    assertEquals(18, timestamp.getHour());
    assertEquals(05, timestamp.getMinute());
    assertEquals(15, timestamp.getSecond());
    assertEquals(641, timestamp.getMilliSecond());
    assertEquals(1451498715641L, timestamp.getMillis());
    assertEquals("2015-12-30T18:05:15.641Z", timestamp.toString());
    assertEquals("18", timestamp.toString("HH"));
    assertEquals("18:05", timestamp.toString("HH:mm"));
    assertEquals("18:05:15", timestamp.toString("HH:mm:ss"));
    assertEquals("18:05:15.641", timestamp.toString("HH:mm:ss.SSS"));

    epoch = 0L;
    timestamp = new OTimestamp(epoch);
    assertEquals(new Date(epoch), timestamp.toDate());
    assertEquals(0, timestamp.getHour()); // 00:00 hrs in GMT is 17:00 hrs in GMT-07
    assertEquals(0, timestamp.getMinute());
    assertEquals(0, timestamp.getSecond());
    assertEquals(0, timestamp.getMilliSecond());
    assertEquals(0, timestamp.getMillis());
    assertEquals("1970-01-01T00:00:00.000Z", timestamp.toString());
    assertEquals("1970-01-01T00:00:00.000Z", timestamp.toUTCString());
    assertEquals("1969-12-31T17:00:00.000-0700", timestamp.toLocalString());
    assertEquals("00", timestamp.toString("HH"));
    assertEquals("00:00", timestamp.toString("HH:mm"));
    assertEquals("00:00:00", timestamp.toString("HH:mm:ss"));
    assertEquals("00:00:00.000", timestamp.toString("HH:mm:ss.SSS"));
  }

  @Test
  public void testTimestampFromFields() {
    OTimestamp timestamp = new OTimestamp(1970, 1, 1, 0, 0, 0, 0);
    assertEquals(1970, timestamp.getYear());
    assertEquals(1, timestamp.getMonth());
    assertEquals(1, timestamp.getDayOfMonth());
    assertEquals(7, timestamp.getHour());
    assertEquals(0, timestamp.getMinute());
    assertEquals(0, timestamp.getSecond());
    assertEquals(0, timestamp.getMilliSecond());
    assertEquals(25200000L, timestamp.getMillis()); // in GMT-07
    assertEquals("1970-01-01T07:00:00.000Z", timestamp.toString());
    assertEquals("1970-01-01T07:00:00.000Z", timestamp.toUTCString());
    assertEquals("1970-01-01T00:00:00.000-0700", timestamp.toLocalString());
  }

}
