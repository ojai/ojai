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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.json.Json;
import org.ojai.tests.BaseTest;
import org.ojai.types.ODate;
import org.ojai.types.OTime;

public class TestDateTime extends BaseTest {
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
    ODate midnightFeb29th2012 = new ODate(MIDNIGHT_FEB29TH_2012);
    ODate milliSecondBeforeMidnightFeb29th2012 = new ODate(MILLIS_BEFORE_MIDNIGHT_FEB29TH_2012);
    Document doc = Json.newDocument();
    doc.set("midnightFeb29th2012", midnightFeb29th2012);
    doc.set("milliSecondBeforeMidnightFeb29th2012", milliSecondBeforeMidnightFeb29th2012);

    midnightFeb29th2012 = doc.getDate("midnightFeb29th2012");
    milliSecondBeforeMidnightFeb29th2012 = doc.getDate("milliSecondBeforeMidnightFeb29th2012");
    assertEquals("2012-02-29", midnightFeb29th2012.toString());
    assertEquals("2012-02-28", milliSecondBeforeMidnightFeb29th2012.toString());

    assertEquals(doc.getDate("midnightFeb29th2012"), ODate.parse("2012-02-29"));
    assertEquals(doc.getDate("milliSecondBeforeMidnightFeb29th2012"), ODate.parse("2012-02-28"));
  }

  @Test
  public void testTimes() throws ParseException {
    OTime midnight = new OTime(MIDNIGHT_FEB29TH_2012);
    OTime milliSecondBeforeMidnight = new OTime(MILLIS_BEFORE_MIDNIGHT_FEB29TH_2012);
    Document doc = Json.newDocument();
    doc.set("midnight", midnight);
    doc.set("milliSecondBeforeMidnight", milliSecondBeforeMidnight);

    midnight = doc.getTime("midnight");
    milliSecondBeforeMidnight = doc.getTime("milliSecondBeforeMidnight");
    assertEquals("00:00:00.000", midnight.toString());
    assertEquals("23:59:59.999", milliSecondBeforeMidnight.toString());

    OTime parsedTime = OTime.parse("00:00:00");
    OTime storedTime = doc.getTime("midnight");
    assertEquals(storedTime, parsedTime);

    parsedTime = OTime.parse("23:59:59.999");
    storedTime = doc.getTime("milliSecondBeforeMidnight");
    assertEquals(storedTime, parsedTime);
  }

}
