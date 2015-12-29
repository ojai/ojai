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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.DocumentStream;
import org.ojai.Value.Type;
import org.ojai.exceptions.EncodingException;
import org.ojai.json.Json;
import org.ojai.json.JsonOptions;
import org.ojai.tests.BaseTest;
import org.ojai.types.ODate;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.util.MapEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJsonDocument extends BaseTest {
  private static Logger logger = LoggerFactory
      .getLogger(TestJsonDocument.class);

  public static final String docStrWithoutTags =
      "{\"map\":{"
        + "\"null\":null,"
        + "\"boolean\":true,"
        + "\"string\":\"eureka\","
        + "\"byte\":127,"
        + "\"short\":32767,"
        + "\"int\":2147483647,"
        + "\"long\":9223372036854775807,"
        + "\"float\":3.4028235,"
        + "\"double\":1.7976931348623157E308,"
        + "\"decimal\":123456789012345678901234567890123456789012345678901.23456789,"
        + "\"date\":\"2012-10-20\","
        + "\"time\":\"07:42:46.123\","
        + "\"timestamp\":\"2012-10-20T14:42:46.123Z\","
        + "\"interval\":172800000,"
        + "\"binary\":\"YWJjZA==\","
        + "\"array\":[42,\"open sesame\",3.14,\"2015-01-21\"]"
        + "}"
      + "}";

  public static final String docStrWithTags =
      "{\"map\":{"
        + "\"null\":null,"
        + "\"boolean\":true,"
        + "\"string\":\"eureka\","
        + "\"byte\":{\"$numberLong\":127},"
        + "\"short\":{\"$numberLong\":32767},"
        + "\"int\":{\"$numberLong\":2147483647},"
        + "\"long\":{\"$numberLong\":9223372036854775807},"
        + "\"float\":3.4028235,"
        + "\"double\":1.7976931348623157E308,"
        + "\"decimal\":{\"$decimal\":\"123456789012345678901234567890123456789012345678901.23456789\"},"
        + "\"date\":{\"$dateDay\":\"2012-10-20\"},"
        + "\"time\":{\"$time\":\"07:42:46.123\"},"
        + "\"timestamp\":{\"$date\":\"2012-10-20T14:42:46.123Z\"},"
        + "\"interval\":{\"$interval\":172800000},"
        + "\"binary\":{\"$binary\":\"YWJjZA==\"},"
        + "\"array\":[42,\"open sesame\",3.14,{\"$dateDay\":\"2015-01-21\"}]"
        + "}"
      + "}";

  @Test
  public void testAllTypes() {
    Document rec = Json.newDocument();
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
    assertEquals(readBuf, bbuf);

    try {
      List<Object> l = rec.getValue("map.list").getList();
      assertEquals(values, l);
    } catch (Exception e) {
      logger.error("Exception from list test " + e.getMessage());
    }
  }

  @Test
  public void testAsReaderFull() {
    Document document = Json.newDocument();
    document.set("map.byte", (byte)127);
    document.set("map.long", 123456789L);
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    map.put("first","John");
    map.put("last", "Doe");
    document.set("map.name", map);
    List<Object> mylist = new ArrayList<Object>();
    mylist.add(true);
    mylist.add("string");
    mylist.add(123.456);
    document.set("map.array", mylist);

    DocumentReader r = document.asReader();
    EventType et = null;
    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals("map", r.getFieldName());

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.BYTE, et);
    assertEquals("byte", r.getFieldName());
    assertEquals(127, r.getByte());

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.LONG, et);
    assertEquals("long", r.getFieldName());
    assertEquals(123456789, r.getLong());

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals("name", r.getFieldName());

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals("first", r.getFieldName());
    assertEquals("John", r.getString());

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals("last", r.getFieldName());
    assertEquals("Doe", r.getString());

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertEquals("name", r.getFieldName());

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.START_ARRAY, et);
    assertEquals("array", r.getFieldName());

    assertTrue((et = r.next()) != null);
    assertTrue(!r.inMap());
    assertEquals(EventType.BOOLEAN, et);
    assertEquals(0, r.getArrayIndex());
    assertEquals(true, r.getBoolean());

    assertTrue((et = r.next()) != null);
    assertTrue(!r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals(1, r.getArrayIndex());
    assertEquals("string", r.getString());

    assertTrue((et = r.next()) != null);
    assertTrue(!r.inMap());
    assertEquals(EventType.DOUBLE, et);
    assertEquals(2, r.getArrayIndex());
    assertEquals(123.456, r.getDouble(), 0.000001);

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.END_ARRAY, et);
    assertEquals("array", r.getFieldName());

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertEquals("map", r.getFieldName());

    assertTrue((et = r.next()) != null);
    assertTrue(r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertNull(r.getFieldName());
  }

  @Test
  public void testAsReaderPartial() {
    Document document = Json.newDocument();
    document.set("map.byte", (byte)127);
    document.set("map.num", 12345);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("first","John");
    map.put("last", "Doe");
    map.put("id", (long)123456789);
    document.set("map.name", map);
    List<Object> mylist = new ArrayList<Object>();
    mylist.add(true);
    mylist.add("string");
    mylist.add(123.456);
    document.set("map.array", mylist);
    DocumentReader r = document.asReader("map.name");
    EventType event;
    while ((event = r.next()) != null) {
      if (event == EventType.LONG) {
        assertEquals("id", r.getFieldName());
        assertEquals(123456789, r.getLong());
      }
    }
  }

  /*
   * Unit test for asReader created on a leaf node of DOM tree.
   */
  @Test
  public void testAsReaderLeaf() {
    Document document = Json.newDocument();
    document.set("map.byte", (byte)127);
    document.set("map.num", 12345);
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("first", "John");
    m.put("last", "Doe");
    m.put("age", (short)45);
    document.set("map.info", m);
    DocumentReader myReader = document.asReader("map.info.age");
    EventType event;
    int numtokens = 0;
    while ((event = myReader.next()) != null) {
      if (event == EventType.SHORT) {
        numtokens++;
        assertEquals((short)45, myReader.getShort());
      }
    }
    assertEquals(1, numtokens);
  }

  @Test
  public void testGetMap() {
    Document document = Json.newDocument();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("a", 1);
    map.put("b", "A");
    document.set("map", map);
    assertEquals(map, document.getMap("map"));
  }

  @Test
  public void testSetBooleanArray() {
    Document document = Json.newDocument();
    document.set("map.int", 111);
    boolean[] boolArray = new boolean[3];
    boolArray[0] = true;
    boolArray[1] = false;
    boolArray[2] = true;
    document.setArray("map.boolarray", boolArray);

    assertEquals(false, document.getBoolean("map.boolarray[1]"));
    assertEquals(true, document.getBoolean("map.boolarray[2]"));
  }

  @Test
  public void testDateWithIntegerMaxMin() {
    Document doc = Json.newDocument();
    ODate d1 = new ODate(Integer.MAX_VALUE);
    ODate d2 = new ODate(Integer.MIN_VALUE);
    doc.set("maxdate", d1);
    doc.set("boolean", false);
    doc.set("mindate", d2);

    logger.info("{}", d1);
    logger.info("{}", doc.getDate("maxdate"));

    assertEquals(true, doc.getValue("maxdate").equals(d1));
    assertEquals(true, doc.getValue("mindate").equals(d2));
  }

  @Test
  public void testDate() {
    Document doc = Json.newDocument();
    doc.set("d1", ODate.parse("2005-06-22"));
    ODate d = new ODate(new java.util.Date());
    doc.set("d2", d);
    logger.info("{}", doc.getDate("d1"));
    logger.info("{}", doc.getDate("d2"));

    assertEquals(true, doc.getDate("d1").toString().equals("2005-06-22"));
    assertEquals(true, doc.getDate("d2").toString().equals(d.toString()));
  }

  @Test
  public void testToString() throws IOException {
    try (InputStream in = getJsonStream("test.json");
         DocumentStream<Document> stream = Json.newDocumentStream(in)) {
      Document doc = stream.iterator().next();
      assertEquals(docStrWithoutTags, doc.toString());
      assertEquals(docStrWithoutTags, doc.asJsonString());
      assertEquals(docStrWithTags, doc.asJsonString(JsonOptions.WITH_TAGS));
    }

  }

  @Test
  public void testToMap() throws Exception {
    Document doc = Json.newDocument();
    Document docInArr1 = Json.newDocument();
    Document docInArr2 = Json.newDocument();
    Document docInArr3 = Json.newDocument();
    Document dateTimeDoc = Json.newDocument();

    dateTimeDoc.set("dateLong", new ODate(System.currentTimeMillis() / 1000))
               .set("dateParsed", ODate.parse("2015-11-25"))
               .set("dateTime", OTime.parse("10:25:32"))
               .set("dateTimestamp", OTimestamp.parse("2015-11-25T13:26:59.223Z"));

    docInArr1.set("arr1string", "string")
             .set("arr1boolean", false)
             .set("arr1long", (long)Integer.MIN_VALUE);

    String byteStr = "bytebuffer";
    docInArr2.set("arr2int", (int)123456)
             .set("arr2long", (long)234567)
             .set("arr2short", (short)123)
             .set("arr2null", "")
             .set("arr2bytes", ByteBuffer.wrap(byteStr.getBytes()))
             .set("arr2bigdecimal", new BigDecimal("1000000000.11111111111111111111"));

    docInArr3.setArray("arr3", new Object[] {"arr3", true, (long)123456, (float)12345.6789, (double)123.4567890, dateTimeDoc});

    doc.set("a.b", "value1")
       .set("a.c", true)
       .setArray("a.docInception", new Object[] {docInArr1, docInArr2, docInArr3});

    Map<String, Object> mapDoc = doc.asMap();
    assertNotNull(mapDoc);

    Document copyDoc = Json.newDocument(mapDoc);
    assertEquals(copyDoc, doc);

    //Negative tests
    //1. Malformed document with null
    String jsonStr = "{";
    DocumentReader dr = Json.newDocumentReader(jsonStr);
    try {
      doc = (Document) MapEncoder.encode(dr);
    } catch (EncodingException e) {
      logger.info("Encoding document '{}' as map got exception: {}", jsonStr, e.getMessage());
    }

    //2. Malformed document with Array not closed
    jsonStr = "{\"a\":{\"b\":[true,1234,\"string\"}}";
    dr = Json.newDocumentReader(jsonStr);
    try {
      doc = (Document) MapEncoder.encode(dr);
    } catch (EncodingException e) {
      logger.info("Encoding document '{}' as map got exception: {}", jsonStr, e.getMessage());
    }

    //3. Malformed document with Map not closed
    jsonStr = "{\"a\":{\"b\":[true,1234,\"string\"]}";
    dr = Json.newDocumentReader(jsonStr);
    try {
      doc = (Document) MapEncoder.encode(dr);
    } catch (EncodingException e) {
      logger.info("Encoding document '{}' as map got exception: {}", jsonStr, e.getMessage());
    }
  }

}
