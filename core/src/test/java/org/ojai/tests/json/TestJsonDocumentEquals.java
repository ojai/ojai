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
import static org.junit.Assert.assertNotEquals;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.Value;
import org.ojai.json.Json;
import org.ojai.json.impl.JsonValueBuilder;
import org.ojai.tests.BaseTest;
import org.ojai.types.ODate;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

public class TestJsonDocumentEquals extends BaseTest {

  @Test
  public void testEquals() {
    Document rec = Json.newDocument();
    rec.set("map.field1", (byte) 100);
    rec.set("map.field2", (short) 10000);
    rec.set("map.string", "eureka");
    rec.set("map.boolean", false);
    rec.set("map.date", ODate.parse("2013-02-12"));
    OTime time = OTime.parse("07:42:46");
    rec.set("map.time", time);
    OTimestamp timeStamp = OTimestamp.parse("2012-10-20T07:42:46");
    rec.set("map.timestamp", timeStamp);
    rec.set("map.int", 12345678);
    byte[] byteArray = "abracadabra".getBytes();
    rec.set("map.bytearray", byteArray);
    rec.setNull("map.null");

    Map<String, Object> m = new HashMap<String, Object>();
    m.put("a", 500);
    m.put("b", "abcd");
    List<Object> newlist = new ArrayList<Object>();
    newlist.add("aaaaa");
    newlist.add(1234567.89);
    m.put("c",newlist);
    rec.set("map2", m);

    List<Object> l = new ArrayList<Object>();
    l.add(12345.678901);
    l.add("abracadabra");

    Map<String, Object> m2 = new HashMap<String, Object>();
    m2.put("a1", 111);
    m2.put("b1", false);
    l.add(m2);

    rec.set("list1", l);

    Document rec2 = rec;
    assertEquals(true, rec2.equals(rec));

    Value stringValue = rec.getValue("map.string");
    assertEquals(true, stringValue.equals("eureka"));

    byte b = rec.getByte("map.field1");
    assertEquals(true, rec.getValue("map.field1").equals(b));

    Byte bite = new Byte(b);
    assertEquals(true, bite.equals(rec.getByte("map.field1")));

    short s = rec.getShort("map.field2");
    assertEquals(true, rec.getValue("map.field2").equals(s));

    assertEquals(true, rec.getValue("map.field2").equals(JsonValueBuilder.initFrom(s)));

    assertEquals(true, rec.getValue("map.boolean").equals(false));

    assertEquals(true, rec.getValue("map.date").equals(ODate.parse("2013-02-12")));

    assertEquals(true, rec.getValue("map2").equals(m));

    int x = rec.getInt("map.int");
    assertEquals(true, rec.getValue("map.int").equals(x));

    assertEquals(true, rec.getList("list1").equals(l));

    assertEquals(true, rec.getValue("map.time").equals(time));

    assertEquals(true, rec.getValue("map.timestamp").equals(timeStamp));

    assertEquals(true, rec.getValue("map.bytearray").equals(ByteBuffer.wrap(byteArray)));

    Value myValue;
    myValue = rec.getValue("map.date");
    assertEquals(true, rec.getValue("map.date").equals(myValue));

    myValue = rec.getValue("map.time");
    assertEquals(true, rec.getValue("map.time").equals(myValue));

    myValue = rec.getValue("map.timestamp");
    assertEquals(true, rec.getValue("map.timestamp").equals(myValue));

    myValue = rec.getValue("list1");
    assertEquals(true,rec.getValue("list1").equals(myValue));

    myValue = rec.getValue("map2");
    assertEquals(true,rec.getValue("map2").equals(myValue));

    Value nval = rec.getValue("map.null");
    assertEquals(true, rec.getValue("map.null").equals(nval));

    Document r1 = Json.newDocument();
    Document r2 = Json.newDocument();
    Document r3 = Json.newDocument();
    r1.setNull("a");
    r2.setNull("a");
    r3.setNull("b");
    assertEquals(true, r1.equals(r2));
    assertEquals(false, r1.equals(r3));

  }

  @Test
  public void testDateTimeEquals() {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    // original time
    cal.set(2015, 12, 31, 11, 59, 59);
    long ts1 = cal.getTime().getTime();

    // change date, keep the time same
    cal.set(2015, 12, 30, 11, 59, 59);
    long ts2 = cal.getTime().getTime();

    // change time, keep the date same
    cal.set(2015, 12, 31, 11, 59, 58);
    long ts3 = cal.getTime().getTime();


    Document r = Json.newDocument()
        .set("date1", new ODate(ts1))
        .set("date2", new ODate(ts2))
        .set("date3", new ODate(ts3))
        .set("time1", new OTime(ts1))
        .set("time2", new OTime(ts2))
        .set("time3", new OTime(ts3));

    assertEquals(r.getValue("date1"), new ODate(ts1));
    assertNotEquals(r.getValue("date1"), new ODate(ts2));
    assertEquals(r.getValue("date1"), new ODate(ts3));
    assertEquals(r.getValue("date1"), r.getValue("date3"));
    assertNotEquals(r.getValue("date1"), r.getValue("date2"));

    assertEquals(r.getValue("time1"), new OTime(ts1));
    assertEquals(r.getValue("time1"), new OTime(ts2));
    assertNotEquals(r.getValue("time1"), new OTime(ts3));
    assertEquals(r.getValue("time1"), r.getValue("time2"));
    assertNotEquals(r.getValue("time1"), r.getValue("time3"));
  }

}
