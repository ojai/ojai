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
package org.jackhammer.tests.json;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jackhammer.Value.Type;
import org.jackhammer.json.JsonRecord;
import org.junit.Test;

public class TestJsonRecord {

  @Test
  public void testAllTypes() {

    JsonRecord rec = new JsonRecord();
    rec.set("map.field1", (byte) 100);
    rec.set("map.field2", (short) 10000);
    rec.set("map.longfield2verylongverylong", 12.345678);
    rec.set("FIELD2", "VERY LONG STRING IS THIS YOU KNOW");

    rec.set("map2.field1", (byte) 100);
    rec.set("map2.field2", (short) 10000);
    rec.set("map2.longfield2verylongverylong", 12.345678);
    rec.set("FIELD3", "VERY LONG STRING IS THIS YOU KNOW");
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("Name", "Anurag");
    map.put("Age", 20);
    rec.set("newmap.map", map);
    /*
     * StringBuffer buf = new StringBuffer(); rec.toStringBuffer(buf);
     * System.out.println("Record is " + buf);
     */

    // rec.set("map.null");
    rec.set("map.boolean", false);
    rec.set("map.string", "string");
    rec.set("map.byte", (byte) 100);
    rec.set("map.short", (short) 10000);
    rec.set("map.int", 50000);
    rec.set("map.long", 12345678999L);
    rec.set("map.float", 10.1234f);
    rec.set("map.double", 10.12345678910d);
    // rec.set("map.interval", new Interval(1000, 2000));
    rec.set("map.decimal", new BigDecimal("1000000000.11111111111111111111"));
    byte[] bytes = new byte[5];
    for (int i = 0; i < bytes.length; ++i) {
      bytes[i] = (byte) i;
    }
    rec.set("map.binary1", bytes);
    rec.set("map.binary2", bytes, 1, 3);
    ByteBuffer bbuf = ByteBuffer.allocate(100);
    for (int i = 0; i < bbuf.capacity(); ++i) {
      bbuf.put((byte) i);
    }
    rec.set("map.binary3", bbuf);

    map = new HashMap<String, Object>();
    map.put("Name", "Anurag");
    map.put("Age", 20);
    List<Integer> scores = new ArrayList<Integer>();
    scores.add(100);
    scores.add(200);
    scores.add(300);
    scores.add(400);
    // map.put("Score", scores);
    rec.set("map.map", map);

    List<Object> values = new ArrayList<Object>();
    values.add("Field1");
    values.add(new Integer(500));
    values.add(new Double(5555.5555));
    rec.set("map.list", values);

    assertEquals(rec.getValue("map").getType(), Type.MAP);
    assertEquals(rec.getBoolean("map.boolean"), false);
    assertEquals(rec.getString("map.string"), "string");
    assertEquals(rec.getByte("map.byte"), (byte) 100);
    assertEquals(rec.getShort("map.short"), (short) 10000);
    assertEquals(rec.getInt("map.int"), 50000);
    assertEquals(rec.getLong("map.long"), 12345678999L);
    assertEquals(rec.getFloat("map.float"), (float) 10.1234, 0.0);
    assertEquals(rec.getDouble("map.double"), 10.12345678910d, 0.0);
    // assertEquals(rec.getInterval("map.interval"), new Interval(1000, 2000));
    assertEquals(rec.getDecimal("map.decimal"), new BigDecimal(
        "1000000000.11111111111111111111"));

    java.nio.ByteBuffer readBuf;
    readBuf = rec.getBinary("map.binary1");
    for (int i = 0; i < bytes.length; ++i) {
      assertEquals(readBuf.get(i), bytes[i]);
    }

    readBuf = rec.getBinary("map.binary2");
    for (int i = 0; i < 3; ++i) {
      assertEquals(readBuf.get(), bytes[1 + i]);
    }
    readBuf = rec.getBinary("map.binary3");
    for (int i = 0; i < bbuf.capacity(); ++i) {
      assertEquals(readBuf.get(i), bbuf.get(i));
    }

    try {
      List<Object> l = rec.getValue("map.list").getList();
      assertEquals(values, l);
    } catch (Exception e) {
      System.out.println("Exception from list test " + e.getMessage());
    }
  }

}
