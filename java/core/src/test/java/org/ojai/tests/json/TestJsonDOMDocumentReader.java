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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.json.Json;
import org.ojai.tests.BaseTest;

public class TestJsonDOMDocumentReader extends BaseTest {

  Document document = Json.newDocument();

  @Before
  public void setUp() {
    document.set("map.num1", (byte)127);
    document.set("map.name.first", "John");
    document.set("map.name.last", "Doe");
    document.set("map.address.street.no", 350);
    document.set("map.address.street.name", "Front St");
    document.set("map.address.zip", (long)95134);
    List<Object> values = new ArrayList<Object>();
    values.add("Field1");
    values.add(new Short((short)500));
    values.add(new Double(5555.5555));
    Map<String, Object> m = new LinkedHashMap<String, Object>();
    m.put("key1", 100);
    m.put("key2", "xyz");
    values.add(m);
    List<Object> l = new ArrayList<Object>();
    l.add(123.4567);
    l.add(true);
    values.add(l);
    document.set("map.list", values);
  }

  @Test
  public void testDOMDocumentReader() throws IOException {
    DocumentReader r = document.asReader();
    EventType et = null;

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertNull(r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals("map", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.BYTE, et);
    assertEquals("num1", r.getFieldName());
    assertEquals(127, r.getByte());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals("name", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals("first", r.getFieldName());
    assertEquals("John", r.getString());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals("last", r.getFieldName());
    assertEquals("Doe", r.getString());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertEquals("name", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals("address", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals("street", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.INT, et);
    assertEquals("no", r.getFieldName());
    assertEquals(350, r.getInt());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals("name", r.getFieldName());
    assertEquals("Front St", r.getString());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertEquals("street", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.LONG, et);
    assertEquals("zip", r.getFieldName());
    assertEquals(95134, r.getLong());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertEquals("address", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_ARRAY, et);
    assertEquals("list", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals("Field1", r.getString());
    assertEquals(0, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.SHORT, et);
    assertEquals(500, r.getShort());
    assertEquals(1, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.DOUBLE, et);
    assertEquals(5555.5555, r.getDouble(), 0);
    assertEquals(2, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals(3, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.INT, et);
    assertEquals("key1", r.getFieldName());
    assertEquals(100, r.getInt());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals("key2", r.getFieldName());
    assertEquals("xyz", r.getString());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertEquals(3, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.START_ARRAY, et);
    assertEquals(4, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.DOUBLE, et);
    assertEquals(0, r.getArrayIndex());
    assertEquals(123.4567, r.getDouble(), 0);

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.BOOLEAN, et);
    assertEquals(1, r.getArrayIndex());
    assertEquals(true, r.getBoolean());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.END_ARRAY, et);
    assertEquals(4, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.END_ARRAY, et);
    assertEquals("list", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertEquals("map", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertNull(r.getFieldName());

    assertNull((et = r.next()));
  }

  @Test
  public void testDOMReader() {
    DocumentReader reader = document.asReader("map.list");
    EventType et;

    while ((et = reader.next()) != null) {
      if (et == EventType.BOOLEAN) {
        assertEquals(true, reader.getBoolean());
      }
    }
  }

  @Test
  public void testScalarTypeReader() {
    DocumentReader reader = document.asReader("map.num1");
    EventType et;
    while ((et = reader.next()) != null) {
      assertEquals(EventType.BYTE, et);
      assertEquals((byte)127, reader.getByte());
    }
  }

}
