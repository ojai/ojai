/**
 * Copyright (c) 2016 MapR, Inc.
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.json.Json;
import org.ojai.tests.BaseTest;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.util.Values;

import com.google.common.collect.Maps;

public class TestTypeMappedJsonDocumentReader extends BaseTest {

  @Test
  public void testTypeMappings() throws IOException {
    Map<FieldPath, Value.Type> fieldPathTypeMap = Maps.newHashMap();
    fieldPathTypeMap.put(FieldPath.parseFrom("null"), Value.Type.NULL);
    fieldPathTypeMap.put(FieldPath.parseFrom("boolean"), Value.Type.BOOLEAN);
    fieldPathTypeMap.put(FieldPath.parseFrom("string"), Value.Type.STRING);
    fieldPathTypeMap.put(FieldPath.parseFrom("byte"), Value.Type.BYTE);
    fieldPathTypeMap.put(FieldPath.parseFrom("short"), Value.Type.SHORT);
    fieldPathTypeMap.put(FieldPath.parseFrom("int"), Value.Type.INT);
    fieldPathTypeMap.put(FieldPath.parseFrom("long"), Value.Type.LONG);
    fieldPathTypeMap.put(FieldPath.parseFrom("float"), Value.Type.FLOAT);
    fieldPathTypeMap.put(FieldPath.parseFrom("double"), Value.Type.DOUBLE);
    fieldPathTypeMap.put(FieldPath.parseFrom("decimal"), Value.Type.DECIMAL);
    fieldPathTypeMap.put(FieldPath.parseFrom("date"), Value.Type.DATE);
    fieldPathTypeMap.put(FieldPath.parseFrom("time"), Value.Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("timestamp"), Value.Type.TIMESTAMP);
    fieldPathTypeMap.put(FieldPath.parseFrom("interval"), Value.Type.INTERVAL);
    fieldPathTypeMap.put(FieldPath.parseFrom("binary"), Value.Type.BINARY);

    try (InputStream in = getJsonStream("org/ojai/test/data/test3.json");
         DocumentStream stream = Json.newDocumentStream(in, fieldPathTypeMap)) {
      for (Document document : stream) {
        Value value = document.getValue("null");
        assertNotNull(value);
        assertEquals(Type.NULL, value.getType());
        assertEquals(null, value.getObject());

        value = document.getValue("boolean");
        assertNotNull(value);
        assertEquals(Type.BOOLEAN, value.getType());
        assertEquals(true, value.getObject());

        value = document.getValue("string");
        assertNotNull(value);
        assertEquals(Type.STRING, value.getType());
        assertEquals("123456789012345678901234567890123456789012345678901234567890", value.getString());

        value = document.getValue("byte");
        assertNotNull(value);
        assertEquals(Type.BYTE, value.getType());
        assertEquals(127, value.getByte());

        value = document.getValue("short");
        assertNotNull(value);
        assertEquals(Type.SHORT, value.getType());
        assertEquals(32767, value.getShort());

        value = document.getValue("int");
        assertNotNull(value);
        assertEquals(Type.INT, value.getType());
        assertEquals(2147483647, value.getInt());

        value = document.getValue("long");
        assertNotNull(value);
        assertEquals(Type.LONG, value.getType());
        assertEquals(9223372036854775807L, value.getLong());

        value = document.getValue("float");
        assertNotNull(value);
        assertEquals(Type.FLOAT, value.getType());
        assertEquals(3.4028235, value.getFloat(), 0.0000001);

        value = document.getValue("double");
        assertNotNull(value);
        assertEquals(Type.DOUBLE, value.getType());
        assertEquals(1.7976931348623157e308, value.getDouble(), 0.00000000000001);

        value = document.getValue("decimal");
        assertNotNull(value);
        assertEquals(Type.DECIMAL, value.getType());
        assertEquals(new BigDecimal("123456789012345678901234567890123456789012345678901.23456789"), value.getDecimal());

        value = document.getValue("date");
        assertNotNull(value);
        assertEquals(Type.DATE, value.getType());
        assertEquals(ODate.parse("2012-10-20"), value.getDate());

        value = document.getValue("time");
        assertNotNull(value);
        assertEquals(Type.TIME, value.getType());
        assertEquals(OTime.parse("07:42:46.123"), value.getTime());

        value = document.getValue("timestamp");
        assertNotNull(value);
        assertEquals(Type.TIMESTAMP, value.getType());
        assertEquals(OTimestamp.parse("2012-10-20T07:42:46.123-07:00"), value.getTimestamp());

        value = document.getValue("interval");
        assertNotNull(value);
        assertEquals(Type.INTERVAL, value.getType());
        assertEquals(new OInterval(172800000), value.getInterval());

        value = document.getValue("binary");
        assertNotNull(value);
        assertEquals(Type.BINARY, value.getType());
        assertEquals(Values.parseBinary("YWJjZA=="), value.getBinary());
      }
    }
  }

  @Test
  public void testInvalidUsage() throws IOException {
    Map<FieldPath, Value.Type> fieldPathTypeMap = Maps.newHashMap();

    fieldPathTypeMap.put(FieldPath.parseFrom("array"), Value.Type.ARRAY);
    try (InputStream in = getJsonStream("org/ojai/test/data/test3.json");
         DocumentStream stream = Json.newDocumentStream(in, fieldPathTypeMap)) {
      for (Document document : stream) {
        fail(document.asJsonString());
      }
    } catch (IllegalArgumentException e) { }
    fieldPathTypeMap.clear();

    fieldPathTypeMap.put(FieldPath.parseFrom("map"), Value.Type.MAP);
    try (InputStream in = getJsonStream("org/ojai/test/data/test3.json");
         DocumentStream stream = Json.newDocumentStream(in, fieldPathTypeMap)) {
      for (Document document : stream) {
        fail(document.asJsonString());
      }
    } catch (IllegalArgumentException e) { }
    fieldPathTypeMap.clear();

    fieldPathTypeMap.put(FieldPath.parseFrom("a.b[9].c"), Value.Type.DATE);
    try (InputStream in = getJsonStream("org/ojai/test/data/test3.json");
         DocumentStream stream = Json.newDocumentStream(in, fieldPathTypeMap)) {
      for (Document document : stream) {
        fail(document.asJsonString());
      }
    } catch (IllegalArgumentException e) { }
    fieldPathTypeMap.clear();
  }

  @Test
  public void testTypeMappingsMultiLevelWithTags() throws IOException {
    Map<FieldPath, Value.Type> fieldPathTypeMap = Maps.newHashMap();
    fieldPathTypeMap.put(FieldPath.parseFrom("map.boolean"), Value.Type.BOOLEAN);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.string"), Value.Type.STRING);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.byte"), Value.Type.BYTE);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.short"), Value.Type.SHORT);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.int"), Value.Type.INT);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.long"), Value.Type.LONG);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.float"), Value.Type.FLOAT);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.double"), Value.Type.DOUBLE);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.decimal"), Value.Type.DECIMAL);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.date"), Value.Type.DATE);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.time"), Value.Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.timestamp"), Value.Type.TIMESTAMP);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.interval"), Value.Type.INTERVAL);
    fieldPathTypeMap.put(FieldPath.parseFrom("map.binary"), Value.Type.BINARY);

    try (InputStream in = getJsonStream("org/ojai/test/data/test4.json");
         DocumentStream jsonRecordStream = Json.newDocumentStream(in, fieldPathTypeMap)) {
      Iterator<Document> docIterator = jsonRecordStream.iterator();
      assertTrue(docIterator.hasNext());
      Document doc = docIterator.next();
      assertTrue(doc.getBoolean("map.boolean"));
      assertEquals("eureka", doc.getString("map.string"));
      assertEquals((byte) 127, doc.getByte("map.byte"));
      assertEquals((short) 32767, doc.getShort("map.short"));
      assertEquals(2147483647, doc.getInt("map.int"));
      assertEquals(9223372036854775807L, doc.getLong("map.long"));
      assertEquals((float) 3.4028235, doc.getFloat("map.float"), 0);
      assertEquals(1.7976931348623157e308, doc.getDouble("map.double"), 0);
      assertEquals(ODate.parse("2012-10-20"), doc.getDate("map.date"));
      assertEquals(OTime.parse("07:42:46.123"), doc.getTime("map.time"));
      assertEquals(OTimestamp.parse("2012-10-20T07:42:46.123-07:00"), doc.getTimestamp("map.timestamp"));
      assertEquals(new OInterval(172800000), doc.getInterval("map.interval"));
      assertEquals(Values.parseBinary("YWJjZA=="), doc.getBinary("map.binary"));
    }
  }

}
