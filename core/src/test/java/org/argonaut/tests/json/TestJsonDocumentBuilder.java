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
package org.argonaut.tests.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.argonaut.Document;
import org.argonaut.DocumentBuilder;
import org.argonaut.Value;
import org.argonaut.json.Json;
import org.argonaut.json.impl.JsonDocumentBuilder;
import org.argonaut.json.impl.JsonValueBuilder;
import org.argonaut.tests.BaseTest;
import org.argonaut.types.Interval;
import org.argonaut.util.Values;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestJsonDocumentBuilder extends BaseTest {

  private byte[] getByteArray(int size) {
    byte[] bytes = new byte[size];
    for (int i = 0; i < bytes.length; ++i) {
      bytes[i] = (byte) i;
    }
    return bytes;
  }

  @Test
  public void testAllTypes() {

    DocumentBuilder jsonBuilder = Json.newDocumentBuilder();
    jsonBuilder.addNewMap();
    jsonBuilder.put("boolean", true);
    jsonBuilder.put("string", "santanu");
    jsonBuilder.put("bytefield", (byte) 16);
    jsonBuilder.put("short", (short) 1000);
    jsonBuilder.put("integer", 32000);
    jsonBuilder.put("long", 123456789L);
    jsonBuilder.put("float", 10.123f);
    jsonBuilder.put("double", 10.12345678d);
    jsonBuilder.put("decimal1", new BigDecimal(12345.6789));
    jsonBuilder.putDecimal("decimal2", (long) 13456, 4);
    jsonBuilder.putDecimal("decimal3", 123456789L);
    jsonBuilder.putDecimal("decimal4", 9876.54321d);
    jsonBuilder.putDecimal("decimal5", 32700, 5);
    jsonBuilder.put("binary1", getByteArray(5));
    jsonBuilder.put("binary2", getByteArray(20), 10, 5);
    // bytebuffer test
    jsonBuilder.put("binary3", ByteBuffer.wrap(getByteArray(10)));
    jsonBuilder.put("date1", Values.parseDate("2013-10-22"));
    jsonBuilder.put(("time1"), Values.parseTime("10:42:46"));
    jsonBuilder.put("timestamp1", new Timestamp(System.currentTimeMillis()));
    jsonBuilder.put("interval1", new Interval(10234567));

    // test array
    jsonBuilder.putNewArray("array1");
    jsonBuilder.add("santanu");
    jsonBuilder.add(10.123f);
    jsonBuilder.add((byte) 127);
    jsonBuilder.add((short) 1000);
    jsonBuilder.add(32000);
    jsonBuilder.add(false);
    jsonBuilder.add(123456789L);
    jsonBuilder.add(32767);
    jsonBuilder.addNull();
    jsonBuilder.add(10.12345678d);
    jsonBuilder.add(new BigDecimal(1234.567891));
    jsonBuilder.add(Values.parseDate("2014-11-14"));
    jsonBuilder.add(Values.parseTime("11:22:33"));
    jsonBuilder.add(new Timestamp(System.currentTimeMillis()));
    jsonBuilder.add(new Interval(10234567));
    jsonBuilder.add(ByteBuffer.wrap(getByteArray(15)));
    jsonBuilder.endArray();
    jsonBuilder.endMap(); //end of document

    Document document = jsonBuilder.getDocument();

    System.out.println(jsonBuilder);

    assertEquals("santanu", document.getString("array1[0]"));
    assertEquals(true, document.getValue("date1").equals(Values.parseDate("2013-10-22")));

  }

  @Rule
  public ExpectedException exception = ExpectedException.none();

  /*
   * disabling these negative test cases until we add manual error checking for
   * out of context writing on outputStream.
   */
  @Test
  public void testWrongEndmap() {

    DocumentBuilder builder = Json.newDocumentBuilder();
    builder.addNewMap();
    builder.put("string", "santanu");
    builder.endMap();
    exception.expect(IllegalStateException.class);
    builder.endMap();
  }

  @Test
  public void TestWrongMapInsertion() {

    DocumentBuilder builder = Json.newDocumentBuilder();
    builder.addNewMap();
    builder.putNewArray("array");
    builder.add((short) 1000);
    exception.expect(IllegalStateException.class);
    builder.put("string", "value");

  }

  //add array element in map context
  @Test
  public void testWrongArrayInsertion() {
    DocumentBuilder builder = Json.newDocumentBuilder();
    builder.addNewMap();
    builder.put("a", "abcd");
    exception.expect(IllegalStateException.class);
    builder.add(1234);
  }

  //add new array in map context
  @Test
  public void testWrongArrayInitiation() {
    DocumentBuilder builder = Json.newDocumentBuilder();
    builder.addNewMap();
    builder.put("a", "abcd");
    exception.expect(IllegalStateException.class);
    builder.addNewArray();
  }


  //do a put new map in array context
  @Test
  public void testPutNewMapInArray() {
    DocumentBuilder w = Json.newDocumentBuilder();
    w.addNewMap();
    w.put("a", "abc");
    w.putNewArray("array");
    w.add(1.111);
    exception.expect(IllegalStateException.class);
    w.putNewMap("map");
  }

  @Test
  public void testExceptionForIncompleteDocument() {
    JsonDocumentBuilder w = (JsonDocumentBuilder)Json.newDocumentBuilder();
    w.addNewMap();
    w.put("f1", "abcd");
    exception.expect(IllegalStateException.class);
    Document r = w.getDocument();
    assertNotNull(r);
  }

  @Test
  @SuppressWarnings("unused")
  public void testPutListAsValue() {
    DocumentBuilder jsonDocumentBuilder = Json.newDocumentBuilder();
    jsonDocumentBuilder.addNewMap();
    List<Object> list = new ArrayList<>();
    list.add(1);
    list.add("2");
    Value v = JsonValueBuilder.initFrom(list);
    jsonDocumentBuilder.put("value", v);
    jsonDocumentBuilder.endMap();
    Document r = jsonDocumentBuilder.getDocument();

  }

  @Test
  public void TestDecimalRange() {
    DocumentBuilder builder = Json.newDocumentBuilder();
    builder.addNewMap();
    builder.putDecimal("d1", Integer.MAX_VALUE, 7);
    builder.putDecimal("d2", Integer.MIN_VALUE, 7);
    builder.putDecimal("d3", Long.MAX_VALUE, 9);
    builder.putDecimal("d4", Long.MIN_VALUE, 9);
    builder.putDecimal("d5", Integer.MAX_VALUE, 15);
    builder.putDecimal("d6", Integer.MIN_VALUE, 15);
    builder.putDecimal("d7", Long.MAX_VALUE, 25);
    builder.putDecimal("d8", Long.MIN_VALUE, 25);
    builder.endMap();
    System.out.println(builder);
  }

  @Test
  public void testDocumentPut() {
    Document document = Json.newDocument();
    document.set("recValue1", "string");
    document.set("recValue2", 1);
    Document document2 = Json.newDocument();
    document2.set("val1", true);
    document2.set("val2", 100);
    List<Object> l = new ArrayList<Object>();
    l.add("abcd");
    l.add(false);
    document2.set("list", l);
    document.set("rec", document2);
    DocumentBuilder documentBuilder = Json.newDocumentBuilder();
    documentBuilder.addNewMap();
    documentBuilder.put("document", document);
    documentBuilder.put("rootValue1", 1)
    .put("rootValue2", "2").endMap();
    System.out.println(documentBuilder);

  }


  @Test
  public void testDocumentAdd() {
    Map<String, Object> map = new LinkedHashMap<String, Object>();

    map.put("null", null);
    map.put("boolean", true);
    map.put("string", "eureka");
    List<Object> l = new ArrayList<Object>();
    l.add("test1");
    Map<String, Object> m2 = new LinkedHashMap<String, Object>();
    m2.put("int", 32900);
    l.add(m2);
    l.add(Long.valueOf("9223372036854775807").longValue());
    l.add(new BigDecimal(7126353.167263));
    map.put("array", l);

    Document innerDocument = Json.newDocument();
    innerDocument.set("map", map);

    //assert on integer field inside innerDocument
    assertEquals(32900, innerDocument.getInt("map.array[1].int"));

    JsonDocumentBuilder builder = (JsonDocumentBuilder)Json.newDocumentBuilder();
    builder.addNewMap();
    builder.putNewMap("map");
    builder.putNewArray("array");
    builder.add(innerDocument);
    builder.add(true);
    builder.endArray();
    builder.endMap();
    builder.endMap();

    Document rec = builder.getDocument();
    //rerun assert on built document
    assertEquals(32900, rec.getInt("map.array[0].map.array[1].int"));
    System.out.println(builder.asUTF8String());

  }

  @Test
  public void testPutMapUsingBuilder() {
    JsonDocumentBuilder w = (JsonDocumentBuilder)Json.newDocumentBuilder();
    w.addNewMap();
    w.putNull("a1");
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("k1", "abcd");
    m.put("k2", Arrays.asList(1, 2, 3));
    Map<String, Object> m2 = new HashMap<String, Object>();
    m2.put("k3", Values.parseDate("2005-10-22"));
    m2.put("k4", Arrays.asList(1.111, 2.222, 3.333));
    m.put("k5", m2);
    w.put("map", m);
    w.endMap();

    Document r = w.getDocument();
    System.out.println(w.asUTF8String());
    assertEquals(2, r.getInt("map.k2[1]"));
  }

  @Test
  public void testArrayWithinArray() {
    JsonDocumentBuilder w = (JsonDocumentBuilder)Json.newDocumentBuilder();
    w.addNewMap();
    w.putNewArray("array");
    w.add("abcd");
    Document r = Json.newDocument();
    List<Object> l = new ArrayList<Object>();
    l.add(123);
    l.add(456);
    r.set("list", l);
    w.add(r.getValue("list"));
    w.endArray();
    w.endMap();
    r = w.getDocument();
    assertEquals(456, r.getInt("array[1][1]"));
  }

  @Test
  public void testArrayAndMapWithinMap() {
    JsonDocumentBuilder w = (JsonDocumentBuilder)Json.newDocumentBuilder();
    w.addNewMap();
    w.putNewArray("array");
    w.add("abcd");
    Document r = Json.newDocument();
    List<Object> l = new ArrayList<Object>();
    l.add(123);
    l.add(456);
    r.set("list", l);
    w.add(r.getValue("list"));
    w.endArray();
    Map<String, Object> m = new HashMap<String, Object>();
    Map<String, Object> m2 = new HashMap<String, Object>();
    m2.put("a1", true);
    m2.put("a2", 11.456);
    List<Object> ll = new ArrayList<Object>();
    ll.add(new BigDecimal(111.11111));
    m2.put("arr2",ll);
    m.put("a3", 5555);
    m.put("map", m2);
    w.put("f", m);
    w.endMap();
    r = w.getDocument();

    assertEquals(5555, r.getInt("f.a3"));
    assertEquals(true, r.getBoolean("f.map.a1"));

  }


}
