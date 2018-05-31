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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.ByteBuffer;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.Value;
import org.ojai.exceptions.TypeException;
import org.ojai.json.Json;
import org.ojai.json.impl.JsonValueBuilder;
import org.ojai.tests.BaseTest;
import org.ojai.util.Values;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class TestValues extends BaseTest {
  final Class<? extends Throwable> TYPE_EXCEPTION = TypeException.class;

  @Test
  public void testEmptyByteBufferSerialization() throws Exception {
    ByteBuffer hbb = ByteBuffer.allocate(0);
    Value hbbValue = JsonValueBuilder.initFrom(hbb);
    assertEquals("{\"$binary\":\"\"}", hbbValue.toString());

    ByteBuffer dbb = ByteBuffer.allocateDirect(0);
    Value dbbValue = JsonValueBuilder.initFrom(dbb);
    assertEquals("{\"$binary\":\"\"}", dbbValue.toString());
  }

  @Test
  public void testAsJsonString() throws IOException {
    URL url = Resources.getResource("org/ojai/test/data/test0.json");
    String content = Resources.toString(url, Charsets.UTF_8);
    Document document = Json.newDocument(content);

    assertEquals("true", document.getValue("boolean").asJsonString());
    assertEquals("\"eureka\"", document.getValue("string").asJsonString());
    assertEquals("{\"$numberByte\":127}", document.getValue("byte").asJsonString());
    assertEquals("{\"$numberShort\":32767}", document.getValue("short").asJsonString());
    assertEquals("{\"$numberInt\":2147483647}", document.getValue("int").asJsonString());
    assertEquals("{\"$numberLong\":9223372036854775807}", document.getValue("long").asJsonString());
    assertEquals("{\"$numberFloat\":3.015625}", document.getValue("float").asJsonString());
    assertEquals("1.7976931348623157E308", document.getValue("double").asJsonString());
    assertEquals("\"123456789012345678901234567890123456789012345678901.23456789\"", document.getValue("decimal").asJsonString());
    assertEquals("{\"$dateDay\":\"2012-10-20\"}", document.getValue("date").asJsonString());
    assertEquals("{\"$time\":\"07:42:46.123\"}", document.getValue("time").asJsonString());
    assertEquals("{\"$date\":\"2012-10-20T14:42:46.123Z\"}", document.getValue("timestamp").asJsonString());
    assertEquals("172800000", document.getValue("interval").asJsonString());
    assertEquals("{\"$binary\":\"YWJjZA==\"}", document.getValue("binary").asJsonString());
  }

  @Test
  public void testIsNumeric() throws IOException {
    URL url = Resources.getResource("org/ojai/test/data/test.json");
    String content = Resources.toString(url, Charsets.UTF_8);
    Document document = Json.newDocument(content);

    assertFalse(document.getValue("map.boolean").getType().isNumeric());
    assertFalse(document.getValue("map.string").getType().isNumeric());
    assertTrue(document.getValue("map.byte").getType().isNumeric());
    assertTrue(document.getValue("map.short").getType().isNumeric());
    assertTrue(document.getValue("map.int").getType().isNumeric());
    assertTrue(document.getValue("map.long").getType().isNumeric());
    assertTrue(document.getValue("map.float").getType().isNumeric());
    assertTrue(document.getValue("map.double").getType().isNumeric());
    assertTrue(document.getValue("map.decimal").getType().isNumeric());
    assertFalse(document.getValue("map.date").getType().isNumeric());
    assertFalse(document.getValue("map.time").getType().isNumeric());
    assertFalse(document.getValue("map.timestamp").getType().isNumeric());
    assertFalse(document.getValue("map.interval").getType().isNumeric());
    assertFalse(document.getValue("map.binary").getType().isNumeric());
  }

  @Test
  public void test_asBoolean() throws IOException {
    URL url = Resources.getResource("org/ojai/test/data/test5.json");
    String content = Resources.toString(url, Charsets.UTF_8);
    Document document = Json.newDocument(content);

    assertFalse(Values.asBoolean(document.getValue("map.null")));
    assertTrue(Values.asBoolean(document.getValue("map.booleanTrue")));
    assertFalse(Values.asBoolean(document.getValue("map.booleanFalse")));
    assertTrue(Values.asBoolean(document.getValue("map.stringTrue")));
    assertFalse(Values.asBoolean(document.getValue("map.stringFalse")));
    assertTrue(Values.asBoolean(document.getValue("map.byteTrue")));
    assertFalse(Values.asBoolean(document.getValue("map.byteFalse")));
    assertTrue(Values.asBoolean(document.getValue("map.shortTrue")));
    assertFalse(Values.asBoolean(document.getValue("map.shortFalse")));
    assertTrue(Values.asBoolean(document.getValue("map.intTrue")));
    assertFalse(Values.asBoolean(document.getValue("map.intFalse")));
    assertTrue(Values.asBoolean(document.getValue("map.longTrue")));
    assertFalse(Values.asBoolean(document.getValue("map.longFalse")));
    assertTrue(Values.asBoolean(document.getValue("map.floatTrue")));
    assertFalse(Values.asBoolean(document.getValue("map.floatFalse")));
    assertTrue(Values.asBoolean(document.getValue("map.doubleTrue")));
    assertFalse(Values.asBoolean(document.getValue("map.doubleFalse")));
    assertTrue(Values.asBoolean(document.getValue("map.decimalTrue")));
    assertFalse(Values.asBoolean(document.getValue("map.decimalFalse")));

    expectException(TYPE_EXCEPTION, () -> { Values.asBoolean(document.getValue("map.string"));});
    expectException(TYPE_EXCEPTION, () -> { Values.asBoolean(document.getValue("map.stringEmpty"));});
    expectException(TYPE_EXCEPTION, () -> { Values.asBoolean(document.getValue("map.date"));});
    expectException(TYPE_EXCEPTION, () -> { Values.asBoolean(document.getValue("map.time"));});
    expectException(TYPE_EXCEPTION, () -> { Values.asBoolean(document.getValue("map.timestamp"));});
    expectException(TYPE_EXCEPTION, () -> { Values.asBoolean(document.getValue("map.interval"));});
    expectException(TYPE_EXCEPTION, () -> { Values.asBoolean(document.getValue("map.binary"));});
    expectException(TYPE_EXCEPTION, () -> { Values.asBoolean(document.getValue("map.array"));});
    expectException(TYPE_EXCEPTION, () -> { Values.asBoolean(document.getValue("map"));});
  }

  @Test
  public void test_asString() throws IOException {
    URL url = Resources.getResource("org/ojai/test/data/test5.json");
    String content = Resources.toString(url, Charsets.UTF_8);
    Document document = Json.newDocument(content);

    assertEquals(null, Values.asString(document.getValue("map.null")));
    assertEquals("true", Values.asString(document.getValue("map.booleanTrue")));
    assertEquals("false", Values.asString(document.getValue("map.booleanFalse")));
    assertEquals("True", Values.asString(document.getValue("map.stringTrue")));
    assertEquals("faLse", Values.asString(document.getValue("map.stringFalse")));
    assertEquals("127", Values.asString(document.getValue("map.byteTrue")));
    assertEquals("0", Values.asString(document.getValue("map.byteFalse")));
    assertEquals("32767", Values.asString(document.getValue("map.shortTrue")));
    assertEquals("0", Values.asString(document.getValue("map.shortFalse")));
    assertEquals("2147483647", Values.asString(document.getValue("map.intTrue")));
    assertEquals("0", Values.asString(document.getValue("map.intFalse")));
    assertEquals("9223372036854775807", Values.asString(document.getValue("map.longTrue")));
    assertEquals("0", Values.asString(document.getValue("map.longFalse")));
    assertEquals("3.015625", Values.asString(document.getValue("map.floatTrue")));
    assertEquals("0.0", Values.asString(document.getValue("map.floatFalse")));
    assertEquals("1.7976931348623157E308", Values.asString(document.getValue("map.doubleTrue")));
    assertEquals("0", Values.asString(document.getValue("map.doubleFalse")));
    assertEquals("123456789012345678901234567890123456789012345678901.23456789", Values.asString(document.getValue("map.decimalTrue")));
    assertEquals("0.0", Values.asString(document.getValue("map.decimalFalse")));

    assertEquals("eureka", Values.asString(document.getValue("map.string")));
    assertEquals("", Values.asString(document.getValue("map.stringEmpty")));
    assertEquals("2012-10-20", Values.asString(document.getValue("map.date")));
    assertEquals("07:42:46.123", Values.asString(document.getValue("map.time")));
    assertEquals("2012-10-20T14:42:46.123Z", Values.asString(document.getValue("map.timestamp")));
    assertEquals("172800000", Values.asString(document.getValue("map.interval")));
    assertEquals("\"YWJjZA==\"", Values.asString(document.getValue("map.binary")));
    assertEquals("[42,\"open sesame\",3.14,\"2015-01-21\"]", Values.asString(document.getValue("map.array")));
    assertEquals("{\"a\":4,\"b\":\"c\"}", Values.asString(document.getValue("map.map")));
  }


  @Test
  public void test_asNumber() throws IOException {
    URL url = Resources.getResource("org/ojai/test/data/test5.json");
    String content = Resources.toString(url, Charsets.UTF_8);
    Document document = Json.newDocument(content);

    assertEquals(127, (byte)Values.asNumber(document.getValue("map.byteTrue")));
    assertEquals(32767, (short)Values.asNumber(document.getValue("map.shortTrue")));
    assertEquals(2147483647, (int)Values.asNumber(document.getValue("map.intTrue")));
    assertEquals(3.015625f, (float)Values.asNumber(document.getValue("map.floatTrue")), 0.0);
    assertEquals(9223372036854775807L, (long)Values.asNumber(document.getValue("map.longTrue")));
    assertEquals(1.7976931348623157E308, (double)Values.asNumber(document.getValue("map.doubleTrue")), 0.0);
    assertEquals(new BigDecimal("123456789012345678901234567890123456789012345678901.23456789"),
        Values.asNumber(document.getValue("map.decimalTrue")));

    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.null")); });
    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.booleanTrue")); });
    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.string")); });
    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.date")); });
    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.time")); });
    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.timestamp")); });
    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.interval")); });
    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.binary")); });
    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.array")); });
    expectException(TYPE_EXCEPTION, () -> { Values.asNumber(document.getValue("map.map")); });
  }

}
