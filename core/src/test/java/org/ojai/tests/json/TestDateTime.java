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
package org.ojai.tests.json;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.json.Json;
import org.ojai.util.Values;

public class TestDateTime {
  static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
  static long MIDNIGHT_FEB29TH_2012;
  static long MILLIS_BEFORE_MIDNIGHT_FEB29TH_2012;
  static {
    try {
      MIDNIGHT_FEB29TH_2012 = df.parse("2012-02-29T00:00:00.000").getTime();
      MILLIS_BEFORE_MIDNIGHT_FEB29TH_2012 = df.parse("2012-02-28T23:59:59.999").getTime();
    } catch (ParseException e) {}
  }

  @Test
  public void testDates() throws ParseException {
    Date midnightFeb29th2012 = new Date(MIDNIGHT_FEB29TH_2012);
    Date milliSecondBeforeMidnightFeb29th2012 = new Date(MILLIS_BEFORE_MIDNIGHT_FEB29TH_2012);
    Document doc = Json.newDocument();
    doc.set("midnightFeb29th2012", midnightFeb29th2012);
    doc.set("milliSecondBeforeMidnightFeb29th2012", milliSecondBeforeMidnightFeb29th2012);

    midnightFeb29th2012 = doc.getDate("midnightFeb29th2012");
    milliSecondBeforeMidnightFeb29th2012 = doc.getDate("milliSecondBeforeMidnightFeb29th2012");
    assertEquals("2012-02-29", midnightFeb29th2012.toString());
    assertEquals("2012-02-28", milliSecondBeforeMidnightFeb29th2012.toString());

    assertEquals(doc.getDate("midnightFeb29th2012"), Values.parseDate("2012-02-29"));
    assertEquals(doc.getDate("milliSecondBeforeMidnightFeb29th2012"), Values.parseDate("2012-02-28"));
  }

  @Test
  public void testTimes() throws ParseException {
    Time midnight = new Time(MIDNIGHT_FEB29TH_2012);
    Time milliSecondBeforeMidnight = new Time(MILLIS_BEFORE_MIDNIGHT_FEB29TH_2012);
    Document doc = Json.newDocument();
    doc.set("midnight", midnight);
    doc.set("milliSecondBeforeMidnight", milliSecondBeforeMidnight);

    midnight = doc.getTime("midnight");
    milliSecondBeforeMidnight = doc.getTime("milliSecondBeforeMidnight");
    assertEquals("00:00:00", midnight.toString());
    assertEquals("23:59:59", milliSecondBeforeMidnight.toString());

    /*
     * java.sql.Time does not override the equals() method and the call is actually handled by java.util.Date
     * which, incorrectly, compares the milliseconds value thus taking the date fields into account
     */
    Time parsedTime = Values.parseTime("00:00:00");
    Time storedTime = doc.getTime("midnight");
    assertEquals(storedTime.toString(), parsedTime.toString());

    parsedTime = Values.parseTime("23:59:59");
    storedTime = doc.getTime("milliSecondBeforeMidnight");
    assertEquals(storedTime.toString(), parsedTime.toString());
  }

}
