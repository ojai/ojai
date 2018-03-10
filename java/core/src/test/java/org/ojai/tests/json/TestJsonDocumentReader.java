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

import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.Test;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.DocumentStream;
import org.ojai.json.Json;
import org.ojai.tests.BaseTest;
import org.ojai.util.Values;

public class TestJsonDocumentReader extends BaseTest {

  @Test
  public void testStreamReader() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test2.json");
         DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader r = stream.documentReaders().iterator().next();
      testReader(r);
    }
  }

  @Test
  public void testDOMReader() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test2.json");
         DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader r = stream.iterator().next().asReader();
      testReader(r);
    }
  }

  @Test
  public void testSkipChildren() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/complex.json");
         DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader r = stream.iterator().next().asReader();
      EventType et = null;
      assertNotNull((et = r.next()));
      assertTrue(r.inMap());
      assertEquals(EventType.START_MAP, et);

      assertNotNull((et = r.next())); // first
      assertTrue(r.inMap());
      assertEquals(EventType.STRING, et);
      r.skipChildren();

      assertNotNull((et = r.next())); // last
      r.skipChildren();

      assertNotNull((et = r.next())); // age
      r.skipChildren();

      assertNotNull((et = r.next())); // sex
      r.skipChildren();

      assertNotNull((et = r.next())); // salary
      r.skipChildren();

      assertNotNull((et = r.next())); // active
      r.skipChildren();

      assertNotNull((et = r.next())); // START_ARRAY interests
      assertEquals(EventType.START_ARRAY, et);
      r.skipChildren();

      assertNotNull((et = r.next())); // START_MAP favorites
      assertEquals(EventType.START_MAP, et);
      r.skipChildren();

      assertNotNull((et = r.next())); // START_ARRAY skills
      assertEquals(EventType.START_ARRAY, et);
      r.skipChildren();

      assertNotNull((et = r.next())); // END_MAP $$document
      assertEquals(EventType.END_MAP, et);
      r.skipChildren();

      assertNull((et = r.next()));
    }
  }

  private void testReader(DocumentReader r) {
    EventType et = null;
    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals("map", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.NULL, et);
    assertEquals("null", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.BOOLEAN, et);
    assertEquals("boolean", r.getFieldName());
    assertEquals(true, r.getBoolean());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals("string", r.getFieldName());
    assertEquals("eureka", r.getString());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.BYTE, et);
    assertEquals("byte", r.getFieldName());
    assertEquals(127, r.getByte());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.SHORT, et);
    assertEquals("short", r.getFieldName());
    assertEquals(32767, r.getShort());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.INT, et);
    assertEquals("int", r.getFieldName());
    assertEquals(2147483647, r.getInt());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.LONG, et);
    assertEquals("long", r.getFieldName());
    assertEquals(9223372036854775807L, r.getLong());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.FLOAT, et);
    assertEquals("float", r.getFieldName());
    assertEquals(3.015625, r.getFloat(), 0);

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.DOUBLE, et);
    assertEquals("double", r.getFieldName());
    assertEquals(1.7976931348623157e308, r.getDouble(), 0);

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.DECIMAL, et);
    assertEquals("decimal", r.getFieldName());
    assertEquals(new BigDecimal("123456789012345678901234567890123456789012345678901.23456789"), r.getDecimal());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.DATE, et);
    assertEquals("date", r.getFieldName());
    assertEquals("2012-10-20", r.getDate().toString());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.TIME, et);
    assertEquals("time", r.getFieldName());
    assertEquals("07:42:46.123", r.getTime().toString());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.TIMESTAMP, et);
    assertEquals("timestamp", r.getFieldName());
    assertEquals("2012-10-20T14:42:46.123Z", r.getTimestamp().toString());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.INTERVAL, et);
    assertEquals("interval", r.getFieldName());
    assertEquals(172800000, r.getInterval().getTimeInMillis());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.BINARY, et);
    assertEquals("binary", r.getFieldName());
    assertEquals(Values.parseBinary("YWJjZA=="), r.getBinary());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_ARRAY, et);
    assertEquals("array", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.DOUBLE, et);
    assertEquals(0, r.getArrayIndex());
    assertEquals(42, r.getDouble(), 0);

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals(1, r.getArrayIndex());
    assertEquals("open sesame", r.getString());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.DOUBLE, et);
    assertEquals(2, r.getArrayIndex());
    assertEquals(3.14, r.getDouble(), 0);

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.DATE, et);
    assertEquals(3, r.getArrayIndex());
    assertEquals("2015-01-21", r.getDate().toString());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals(4, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.START_ARRAY, et);
    assertEquals("nested", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.START_ARRAY, et);
    assertEquals(0, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.END_ARRAY, et);
    assertEquals(0, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.START_MAP, et);
    assertEquals(1, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertEquals(1, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.DOUBLE, et);
    assertEquals(2, r.getArrayIndex());
    assertEquals(0, r.getDouble(), 0);

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.STRING, et);
    assertEquals(3, r.getArrayIndex());
    assertEquals("", r.getString());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.END_ARRAY, et);
    assertEquals("nested", r.getFieldName());

    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(EventType.END_MAP, et);
    assertEquals(4, r.getArrayIndex());

    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(EventType.END_ARRAY, et);
    assertEquals("array", r.getFieldName());

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

}
