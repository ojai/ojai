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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentConstants;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.DocumentStream;
import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.exceptions.EncodingException;
import org.ojai.exceptions.TypeException;
import org.ojai.json.Json;
import org.ojai.json.JsonOptions;
import org.ojai.tests.BaseTest;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.util.DocumentReaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class TestJsonDocument extends BaseTest {
  private static Logger logger = LoggerFactory .getLogger(TestJsonDocument.class);

  private static final FieldPath FIELD_MAP_ARRAY = FieldPath.parseFrom("map.array");
  private static final FieldPath FIELD_MAP_BINARY = FieldPath.parseFrom("map.binary");
  private static final FieldPath FIELD_MAP_INTERVAL = FieldPath.parseFrom("map.interval");
  private static final FieldPath FIELD_MAP_TIMESTAMP = FieldPath.parseFrom("map.timestamp");
  private static final FieldPath FIELD_MAP_TIME = FieldPath.parseFrom("map.time");
  private static final FieldPath FIELD_MAP_DATE = FieldPath.parseFrom("map.date");
  private static final FieldPath FIELD_MAP_DECIMAL = FieldPath.parseFrom("map.decimal");
  private static final FieldPath FIELD_MAP_DOUBLE = FieldPath.parseFrom("map.double");
  private static final FieldPath FIELD_MAP_FLOAT = FieldPath.parseFrom("map.float");
  private static final FieldPath FIELD_MAP_LONG = FieldPath.parseFrom("map.long");
  private static final FieldPath FIELD_MAP_INT = FieldPath.parseFrom("map.int");
  private static final FieldPath FIELD_MAP_SHORT = FieldPath.parseFrom("map.short");
  private static final FieldPath FIELD_MAP_BYTE = FieldPath.parseFrom("map.byte");
  private static final FieldPath FIELD_MAP_STRING = FieldPath.parseFrom("map.string");
  private static final FieldPath FIELD_MAP_BOOLEAN = FieldPath.parseFrom("map.boolean");

  public static final String docStrWithoutTags =
      "{\"map\":{"
          + "\"null\":null,"
          + "\"boolean\":true,"
          + "\"string\":\"eureka\","
          + "\"byte\":127,"
          + "\"short\":32767,"
          + "\"int\":2147483647,"
          + "\"long\":9223372036854775807,"
          + "\"float\":3.015625,"
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
          + "\"byte\":{\"$numberByte\":127},"
          + "\"short\":{\"$numberShort\":32767},"
          + "\"int\":{\"$numberInt\":2147483647},"
          + "\"long\":{\"$numberLong\":9223372036854775807},"
          + "\"float\":{\"$numberFloat\":3.015625},"
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

  public static final Document docWithAllTypes1 = Json.newDocument()
      .setNull("map.null")
      .set(FIELD_MAP_BOOLEAN, false)
      .set(FIELD_MAP_STRING, "eureka")
      .set(FIELD_MAP_BYTE, Byte.MAX_VALUE)
      .set(FIELD_MAP_SHORT, Short.MAX_VALUE)
      .set(FIELD_MAP_INT, Integer.MAX_VALUE)
      .set(FIELD_MAP_LONG, Long.MAX_VALUE)
      .set(FIELD_MAP_FLOAT, Float.MAX_VALUE)
      .set(FIELD_MAP_DOUBLE, Double.MAX_VALUE)
      .set(FIELD_MAP_DECIMAL, new BigDecimal("9223372036854775807.23456789"))
      .set(FIELD_MAP_DATE, ODate.parse("2012-10-20"))
      .set(FIELD_MAP_TIME, OTime.parse("07:42:46.123"))
      .set(FIELD_MAP_TIMESTAMP, OTimestamp.parse("2012-10-20T14:42:46.123Z"))
      .set(FIELD_MAP_INTERVAL, new OInterval(172800000))
      .set(FIELD_MAP_BINARY, ByteBuffer.wrap(new byte[] {'h', 'e', 'l', 'l', 'o'}))
      .setArray(FIELD_MAP_ARRAY, new Object[] {42, "open sesame", 3.14, (short)'l', ODate.parse("2012-10-20")});

  public static final Document docWithAllTypes2 = Json.newDocument()
      .setNull("map.null")
      .set(FIELD_MAP_BOOLEAN, false)
      .set(FIELD_MAP_STRING, "eureka")
      .set(FIELD_MAP_BYTE, (byte)121.625)
      .set(FIELD_MAP_SHORT, (short)121.625)
      .set(FIELD_MAP_INT, (int)121.625)
      .set(FIELD_MAP_LONG, (long)121.625)
      .set(FIELD_MAP_FLOAT, (float)121.625)
      .set(FIELD_MAP_DOUBLE, 121.625)
      .set(FIELD_MAP_DECIMAL, new BigDecimal("121.625"))
      .set(FIELD_MAP_DATE, ODate.parse("2012-10-20"))
      .set(FIELD_MAP_TIME, OTime.parse("07:42:46.123"))
      .set(FIELD_MAP_TIMESTAMP, OTimestamp.parse("2012-10-20T14:42:46.123Z"))
      .set(FIELD_MAP_INTERVAL, new OInterval(172800000))
      .set(FIELD_MAP_BINARY, ByteBuffer.wrap(new byte[] {'h', 'e', 'l', 'l', 'o'}))
      .setArray(FIELD_MAP_ARRAY, new Object[] {42, "open sesame", 3.14, (short)'l', ODate.parse("2012-10-20")});

  private static final float DELTA = 0;

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

    rec.set(FIELD_MAP_BOOLEAN, false);
    rec.set(FIELD_MAP_STRING, "string");
    rec.set(FIELD_MAP_BYTE, (byte) 100);
    rec.set(FIELD_MAP_SHORT, (short) 10000);
    rec.set(FIELD_MAP_INT, 50000);
    rec.set(FIELD_MAP_LONG, 12345678999L);
    rec.set(FIELD_MAP_FLOAT, 10.1234f);
    rec.set(FIELD_MAP_DOUBLE, 10.12345678910d);
    // rec.set("map.interval", new Interval(1000, 2000));
    rec.set(FIELD_MAP_DECIMAL, new BigDecimal("1000000000.11111111111111111111"));
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
    assertEquals(rec.getBoolean(FIELD_MAP_BOOLEAN), false);
    assertEquals(rec.getString(FIELD_MAP_STRING), "string");
    assertEquals(rec.getByte(FIELD_MAP_BYTE), (byte) 100);
    assertEquals(rec.getShort(FIELD_MAP_SHORT), (short) 10000);
    assertEquals(rec.getInt(FIELD_MAP_INT), 50000);
    assertEquals(rec.getLong(FIELD_MAP_LONG), 12345678999L);
    assertEquals(rec.getFloat(FIELD_MAP_FLOAT), (float) 10.1234, 0.0);
    assertEquals(rec.getDouble(FIELD_MAP_DOUBLE), 10.12345678910d, 0.0);
    // assertEquals(rec.getInterval("map.interval"), new Interval(1000, 2000));
    assertEquals(rec.getDecimal(FIELD_MAP_DECIMAL), new BigDecimal(
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
    document.set(FIELD_MAP_BYTE, (byte)127);
    document.set(FIELD_MAP_LONG, 123456789L);
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    map.put("first","John");
    map.put("last", "Doe");
    document.set("map.name", map);
    List<Object> mylist = new ArrayList<Object>();
    mylist.add(true);
    mylist.add("string");
    mylist.add(123.456);
    document.set(FIELD_MAP_ARRAY, mylist);

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
    document.set(FIELD_MAP_BYTE, (byte)127);
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
    document.set(FIELD_MAP_ARRAY, mylist);
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
    document.set(FIELD_MAP_BYTE, (byte)127);
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
    document.set(FIELD_MAP_INT, 111);
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
    try (InputStream in = getJsonStream("org/ojai/test/data/test.json");
        DocumentStream stream = Json.newDocumentStream(in)) {
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
    docInArr2.set("arr2int", 123456)
    .set("arr2long", (long)234567)
    .set("arr2short", (short)123)
    .set("arr2null", "")
    .set("arr2bytes", ByteBuffer.wrap(byteStr.getBytes()))
    .set("arr2bigdecimal", new BigDecimal("1000000000.11111111111111111111"));

    docInArr3.setArray("arr3", new Object[] {"arr3", true, (long)123456, (float)12345.6789, (double)123.4567890, dateTimeDoc});
    doc.set("emptyList", ImmutableList.of());

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
      doc = (Document) DocumentReaders.encode(dr);
      fail();
    } catch (EncodingException e) {
      logger.info("Encoding document '{}' as map got exception: {}", jsonStr, e.getMessage());
    }

    //2. Malformed document with Array not closed
    jsonStr = "{\"a\":{\"b\":[true,1234,\"string\"}}";
    dr = Json.newDocumentReader(jsonStr);
    try {
      doc = (Document) DocumentReaders.encode(dr);
      fail();
    } catch (EncodingException e) {
      logger.info("Encoding document '{}' as map got exception: {}", jsonStr, e.getMessage());
    }

    //3. Malformed document with Map not closed
    jsonStr = "{\"a\":{\"b\":[true,1234,\"string\"]}";
    dr = Json.newDocumentReader(jsonStr);
    try {
      doc = (Document) DocumentReaders.encode(dr);
      fail();
    } catch (EncodingException e) {
      logger.info("Encoding document '{}' as map got exception: {}", jsonStr, e.getMessage());
    }
  }

  @Test
  public void testJsonList() throws Exception {
    Object[] objArr = new Object[11];
    objArr[0] = null;
    objArr[1] = true;
    objArr[2] = (byte) 127;
    objArr[3] = (short) 32767;
    objArr[4] = 2147483647;
    objArr[5] = (long) 123456789;
    objArr[6] = (float) 3.015625;
    objArr[7] = 1.7976931348623157e308;
    objArr[8] = ODate.parse("2015-12-31");
    objArr[9] = OTime.parse("11:59:59");
    objArr[10] = ByteBuffer.wrap("ola".getBytes());

    List<Object> listOfObjs = Arrays.asList(objArr);
    Document doc = Json.newDocument();
    doc.set("objarray", listOfObjs);
    List<Object> newList = doc.getList("objarray");
    assertEquals(newList, listOfObjs);
    assertEquals(listOfObjs, newList);
  }

  @Test
  public void testSetGetId() {
    final String DOCUMENT_0001 = "document_0001";
    Document doc = Json.newDocument();
    doc.setId(DOCUMENT_0001);
    Value idValue = doc.getValue(DocumentConstants.ID_FIELD);
    assertEquals(Type.STRING, idValue.getType());
    assertEquals(DOCUMENT_0001, idValue.getString());
    assertEquals(DOCUMENT_0001, doc.getIdString());

    doc = Json.newDocument();
    assertNull(doc.getId());
    doc.setId(idValue);
    assertNotNull(doc.getId());
    idValue = doc.getValue(DocumentConstants.ID_FIELD);
    assertEquals(Type.STRING, idValue.getType());
    assertEquals(DOCUMENT_0001, idValue.getString());
    assertEquals(DOCUMENT_0001, doc.getIdString());
    try {
      doc.getIdBinary();
      fail();
    } catch (TypeException e) {}

    final ByteBuffer DOCUMENT_0002 = ByteBuffer.wrap("document_0002".getBytes());
    doc = Json.newDocument();
    assertNull(doc.getId());
    doc.setId(DOCUMENT_0002);
    idValue = doc.getValue(DocumentConstants.ID_FIELD);
    assertEquals(Type.BINARY, idValue.getType());
    assertEquals(DOCUMENT_0002, idValue.getBinary());
    assertEquals(DOCUMENT_0002, doc.getIdBinary());
    try {
      doc.getIdString();
      fail();
    } catch (TypeException e) {}
  }

  @Test
  public void testNonExistingPrimitives() {
    Document doc = Json.newDocument();

    assertNull(doc.getBooleanObj("non-existent-field"));
    try {
      doc.getBoolean("non-existent-field");
      fail();
    } catch (NoSuchElementException e) {}

    assertNull(doc.getByteObj("non-existent-field"));
    try {
      doc.getByte("non-existent-field");
      fail();
    } catch (NoSuchElementException e) {}

    assertNull(doc.getShortObj("non-existent-field"));
    try {
      doc.getShort("non-existent-field");
      fail();
    } catch (NoSuchElementException e) {}

    assertNull(doc.getIntObj("non-existent-field"));
    try {
      doc.getInt("non-existent-field");
      fail();
    } catch (NoSuchElementException e) {}

    assertNull(doc.getLongObj("non-existent-field"));
    try {
      doc.getLong("non-existent-field");
      fail();
    } catch (NoSuchElementException e) {}

    assertNull(doc.getFloatObj("non-existent-field"));
    try {
      doc.getFloat("non-existent-field");
      fail();
    } catch (NoSuchElementException e) {}

    assertNull(doc.getDoubleObj("non-existent-field"));
    try {
      doc.getDouble("non-existent-field");
      fail();
    } catch (NoSuchElementException e) {}
  }

  @Test
  public void testNumericInterchange() {
    /* byte */
    assertEquals(Byte.MAX_VALUE, docWithAllTypes1.getByte(FIELD_MAP_BYTE));
    assertEquals(-1, docWithAllTypes1.getByte(FIELD_MAP_SHORT));
    assertEquals(-1, docWithAllTypes1.getByte(FIELD_MAP_INT));
    assertEquals(-1, docWithAllTypes1.getByte(FIELD_MAP_LONG));
    assertEquals(-1, docWithAllTypes1.getByte(FIELD_MAP_FLOAT));
    assertEquals(-1, docWithAllTypes1.getByte(FIELD_MAP_DOUBLE));
    assertEquals(-1, docWithAllTypes1.getByte(FIELD_MAP_DECIMAL));

    assertEquals(121, docWithAllTypes2.getByte(FIELD_MAP_BYTE));
    assertEquals(121, docWithAllTypes2.getByte(FIELD_MAP_SHORT));
    assertEquals(121, docWithAllTypes2.getByte(FIELD_MAP_INT));
    assertEquals(121, docWithAllTypes2.getByte(FIELD_MAP_LONG));
    assertEquals(121, docWithAllTypes2.getByte(FIELD_MAP_FLOAT));
    assertEquals(121, docWithAllTypes2.getByte(FIELD_MAP_DOUBLE));
    assertEquals(121, docWithAllTypes2.getByte(FIELD_MAP_DECIMAL));

    /* short */
    assertEquals(Byte.MAX_VALUE, docWithAllTypes1.getShort(FIELD_MAP_BYTE));
    assertEquals(Short.MAX_VALUE, docWithAllTypes1.getShort(FIELD_MAP_SHORT));
    assertEquals(-1, docWithAllTypes1.getShort(FIELD_MAP_INT));
    assertEquals(-1, docWithAllTypes1.getShort(FIELD_MAP_LONG));
    assertEquals(-1, docWithAllTypes1.getShort(FIELD_MAP_FLOAT));
    assertEquals(-1, docWithAllTypes1.getShort(FIELD_MAP_DOUBLE));
    assertEquals(-1, docWithAllTypes1.getShort(FIELD_MAP_DECIMAL));

    assertEquals(121, docWithAllTypes2.getShort(FIELD_MAP_BYTE));
    assertEquals(121, docWithAllTypes2.getShort(FIELD_MAP_SHORT));
    assertEquals(121, docWithAllTypes2.getShort(FIELD_MAP_INT));
    assertEquals(121, docWithAllTypes2.getShort(FIELD_MAP_LONG));
    assertEquals(121, docWithAllTypes2.getShort(FIELD_MAP_FLOAT));
    assertEquals(121, docWithAllTypes2.getShort(FIELD_MAP_DOUBLE));
    assertEquals(121, docWithAllTypes2.getShort(FIELD_MAP_DECIMAL));

    /* int */
    assertEquals(Byte.MAX_VALUE, docWithAllTypes1.getInt(FIELD_MAP_BYTE));
    assertEquals(Short.MAX_VALUE, docWithAllTypes1.getInt(FIELD_MAP_SHORT));
    assertEquals(Integer.MAX_VALUE, docWithAllTypes1.getInt(FIELD_MAP_INT));
    assertEquals(-1, docWithAllTypes1.getInt(FIELD_MAP_LONG));
    assertEquals(Integer.MAX_VALUE, docWithAllTypes1.getInt(FIELD_MAP_FLOAT));
    assertEquals(Integer.MAX_VALUE, docWithAllTypes1.getInt(FIELD_MAP_DOUBLE));
    assertEquals(-1, docWithAllTypes1.getInt(FIELD_MAP_DECIMAL));

    assertEquals(121, docWithAllTypes2.getInt(FIELD_MAP_BYTE));
    assertEquals(121, docWithAllTypes2.getInt(FIELD_MAP_SHORT));
    assertEquals(121, docWithAllTypes2.getInt(FIELD_MAP_INT));
    assertEquals(121, docWithAllTypes2.getInt(FIELD_MAP_LONG));
    assertEquals(121, docWithAllTypes2.getInt(FIELD_MAP_FLOAT));
    assertEquals(121, docWithAllTypes2.getInt(FIELD_MAP_DOUBLE));
    assertEquals(121, docWithAllTypes2.getInt(FIELD_MAP_DECIMAL));

    /* long */
    assertEquals(Byte.MAX_VALUE, docWithAllTypes1.getLong(FIELD_MAP_BYTE));
    assertEquals(Short.MAX_VALUE, docWithAllTypes1.getLong(FIELD_MAP_SHORT));
    assertEquals(Integer.MAX_VALUE, docWithAllTypes1.getLong(FIELD_MAP_INT));
    assertEquals(Long.MAX_VALUE, docWithAllTypes1.getLong(FIELD_MAP_LONG));
    assertEquals(Long.MAX_VALUE, docWithAllTypes1.getLong(FIELD_MAP_FLOAT));
    assertEquals(Long.MAX_VALUE, docWithAllTypes1.getLong(FIELD_MAP_DOUBLE));
    assertEquals(Long.MAX_VALUE, docWithAllTypes1.getLong(FIELD_MAP_DECIMAL));

    assertEquals(121, docWithAllTypes2.getLong(FIELD_MAP_BYTE));
    assertEquals(121, docWithAllTypes2.getLong(FIELD_MAP_SHORT));
    assertEquals(121, docWithAllTypes2.getLong(FIELD_MAP_INT));
    assertEquals(121, docWithAllTypes2.getLong(FIELD_MAP_LONG));
    assertEquals(121, docWithAllTypes2.getLong(FIELD_MAP_FLOAT));
    assertEquals(121, docWithAllTypes2.getLong(FIELD_MAP_DOUBLE));
    assertEquals(121, docWithAllTypes2.getLong(FIELD_MAP_DECIMAL));

    /* float */
    assertEquals(Byte.MAX_VALUE, docWithAllTypes1.getFloat(FIELD_MAP_BYTE), DELTA);
    assertEquals(Short.MAX_VALUE, docWithAllTypes1.getFloat(FIELD_MAP_SHORT), DELTA);
    assertEquals(Integer.MAX_VALUE, docWithAllTypes1.getFloat(FIELD_MAP_INT), DELTA);
    assertEquals(Long.MAX_VALUE, docWithAllTypes1.getFloat(FIELD_MAP_LONG), DELTA);
    assertEquals(Float.MAX_VALUE, docWithAllTypes1.getFloat(FIELD_MAP_FLOAT), DELTA);
    assertEquals(Float.POSITIVE_INFINITY, docWithAllTypes1.getFloat(FIELD_MAP_DOUBLE), DELTA);
    assertEquals(Long.MAX_VALUE, docWithAllTypes1.getFloat(FIELD_MAP_DECIMAL), DELTA);

    assertEquals(121, docWithAllTypes2.getFloat(FIELD_MAP_BYTE), DELTA);
    assertEquals(121, docWithAllTypes2.getFloat(FIELD_MAP_SHORT), DELTA);
    assertEquals(121, docWithAllTypes2.getFloat(FIELD_MAP_INT), DELTA);
    assertEquals(121, docWithAllTypes2.getFloat(FIELD_MAP_LONG), DELTA);
    assertEquals(121.625, docWithAllTypes2.getFloat(FIELD_MAP_FLOAT), DELTA);
    assertEquals(121.625, docWithAllTypes2.getFloat(FIELD_MAP_DOUBLE), DELTA);
    assertEquals(121.625, docWithAllTypes2.getFloat(FIELD_MAP_DECIMAL), DELTA);

    /* double */
    assertEquals(Byte.MAX_VALUE, docWithAllTypes1.getDouble(FIELD_MAP_BYTE), DELTA);
    assertEquals(Short.MAX_VALUE, docWithAllTypes1.getDouble(FIELD_MAP_SHORT), DELTA);
    assertEquals(Integer.MAX_VALUE, docWithAllTypes1.getDouble(FIELD_MAP_INT), DELTA);
    assertEquals(Long.MAX_VALUE, docWithAllTypes1.getDouble(FIELD_MAP_LONG), DELTA);
    assertEquals(Float.MAX_VALUE, docWithAllTypes1.getDouble(FIELD_MAP_FLOAT), DELTA);
    assertEquals(Double.MAX_VALUE, docWithAllTypes1.getDouble(FIELD_MAP_DOUBLE), DELTA);
    assertEquals(Long.MAX_VALUE, docWithAllTypes1.getDouble(FIELD_MAP_DECIMAL), DELTA);

    assertEquals(121, docWithAllTypes2.getDouble(FIELD_MAP_BYTE), DELTA);
    assertEquals(121, docWithAllTypes2.getDouble(FIELD_MAP_SHORT), DELTA);
    assertEquals(121, docWithAllTypes2.getDouble(FIELD_MAP_INT), DELTA);
    assertEquals(121, docWithAllTypes2.getDouble(FIELD_MAP_LONG), DELTA);
    assertEquals(121.625, docWithAllTypes2.getDouble(FIELD_MAP_FLOAT), DELTA);
    assertEquals(121.625, docWithAllTypes2.getDouble(FIELD_MAP_DOUBLE), DELTA);
    assertEquals(121.625, docWithAllTypes2.getDouble(FIELD_MAP_DECIMAL), DELTA);

    /* decimal */
    assertEquals(new BigDecimal(Byte.MAX_VALUE), docWithAllTypes1.getDecimal(FIELD_MAP_BYTE));
    assertEquals(new BigDecimal(Short.MAX_VALUE), docWithAllTypes1.getDecimal(FIELD_MAP_SHORT));
    assertEquals(new BigDecimal(Integer.MAX_VALUE), docWithAllTypes1.getDecimal(FIELD_MAP_INT));
    assertEquals(new BigDecimal(Long.MAX_VALUE), docWithAllTypes1.getDecimal(FIELD_MAP_LONG));
    assertEquals(new BigDecimal(Float.MAX_VALUE), docWithAllTypes1.getDecimal(FIELD_MAP_FLOAT));
    assertEquals(new BigDecimal(Double.MAX_VALUE), docWithAllTypes1.getDecimal(FIELD_MAP_DOUBLE));
    assertEquals(new BigDecimal("9223372036854775807.23456789"), docWithAllTypes1.getDecimal(FIELD_MAP_DECIMAL));

    assertEquals(new BigDecimal(121), docWithAllTypes2.getDecimal(FIELD_MAP_BYTE));
    assertEquals(new BigDecimal(121), docWithAllTypes2.getDecimal(FIELD_MAP_SHORT));
    assertEquals(new BigDecimal(121), docWithAllTypes2.getDecimal(FIELD_MAP_INT));
    assertEquals(new BigDecimal(121), docWithAllTypes2.getDecimal(FIELD_MAP_LONG));
    assertEquals(new BigDecimal(121.625), docWithAllTypes2.getDecimal(FIELD_MAP_FLOAT));
    assertEquals(new BigDecimal(121.625), docWithAllTypes2.getDecimal(FIELD_MAP_DOUBLE));
    assertEquals(new BigDecimal(121.625), docWithAllTypes2.getDecimal(FIELD_MAP_DECIMAL));
  }

  @Test
  public void testJSONDocumentWithArrayAbsIndex() {
    Document doc = Json.newDocument().set("c[0]",true).set("c[3]", "mapr").set("a.b", 10);

    assertTrue(doc.getBoolean("c[0]"));
    assertEquals(Type.NULL, doc.getValue("c[1]").getType());
    assertEquals(Type.NULL, doc.getValue("c[2]").getType());
    assertEquals("mapr", doc.getString("c[3]"));
  }

}
