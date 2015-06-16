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

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jackhammer.Record;
import org.jackhammer.RecordWriter;
import org.jackhammer.Value;
import org.jackhammer.json.Json;
import org.jackhammer.json.JsonValueBuilder;
import org.jackhammer.tests.BaseTest;
import org.jackhammer.types.Interval;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJsonRecordWriter extends BaseTest {
  private static Logger logger = LoggerFactory
      .getLogger(TestJsonRecordWriter.class);

  private byte[] getByteArray(int size) {
    byte[] bytes = new byte[size];
    for (int i = 0; i < bytes.length; ++i) {
      bytes[i] = (byte) i;
    }
    return bytes;
  }

  @Test
  public void testAllTypes() {

    RecordWriter jsonWriter = Json.newRecordWriter();
    jsonWriter.put("boolean", true);
    jsonWriter.put("string", "santanu");
    jsonWriter.put("bytefield", (byte) 16);
    jsonWriter.put("short", (short) 1000);
    jsonWriter.put("integer", 32000);
    jsonWriter.put("long", 123456789L);
    jsonWriter.put("float", 10.123f);
    jsonWriter.put("double", 10.12345678d);
    jsonWriter.put("decimal1", new BigDecimal(12345.6789));
    jsonWriter.putDecimal("decimal2", (long) 13456, 4);
    jsonWriter.putDecimal("decimal3", 123456789L);
    jsonWriter.putDecimal("decimal4", 9876.54321d);
    jsonWriter.putDecimal("decimal5", 32700, 5);
    jsonWriter.put("binary1", getByteArray(5));
    jsonWriter.put("binary2", getByteArray(20), 10, 5);
    // bytebuffer test
    jsonWriter.put("binary3", ByteBuffer.wrap(getByteArray(10)));
    jsonWriter.put("date1", Date.valueOf("2010-01-10"));
    jsonWriter.put(("time1"), Time.valueOf("19:15:12"));
    jsonWriter.put("timestamp1", Timestamp.valueOf("2010-10-15 14:20:00"));
    jsonWriter.put("interval1", new Interval(10234567));

    // test array
    jsonWriter.putNewArray("array1");
    jsonWriter.add("santanu");
    jsonWriter.add(10.123f);
    jsonWriter.add((byte) 127);
    jsonWriter.add((short) 1000);
    jsonWriter.add(32000);
    jsonWriter.add(false);
    jsonWriter.add(123456789L);
    jsonWriter.add(32767);
    jsonWriter.addNull();
    jsonWriter.add(10.12345678d);
    jsonWriter.add(new BigDecimal(1234.567891));
    jsonWriter.add(Date.valueOf("2010-01-10"));
    jsonWriter.add(Time.valueOf("19:15:12"));
    jsonWriter.add(Timestamp.valueOf("2010-10-15 14:20:00"));
    jsonWriter.add(new Interval(10234567));
    jsonWriter.add(ByteBuffer.wrap(getByteArray(15)));
    jsonWriter.endArray();

    Record record = jsonWriter.build();

    System.out.println(record);

  }

  @Rule
  public ExpectedException exception = ExpectedException.none();

  /*
   * disabling these negative test cases until we add manual error checking for
   * out of context writing on outputStream.
   */
  // @Test
  public void TestWrongArrayInsertion() {

    RecordWriter writer = Json.newRecordWriter();
    writer.put("string", "santanu");
    exception.expect(IllegalStateException.class);
    writer.endMap();
    // exception.expect(IllegalStateException.class);
    writer.add((long) 12345);
    writer.add((long) 23456);
    writer.add((long) 5555);
    // jsonWriter.EndDocument();
    logger.debug(writer.toString());

  }

  // @Test
  public void TestWrongMapInsertion() {

    RecordWriter writer = Json.newRecordWriter();
    writer.putNewArray("array");
    writer.add((short) 1000);
    exception.expect(IllegalStateException.class);
    writer.put("string", "value");

  }

  @Test
  @SuppressWarnings("unused")
  public void testPutListAsValue() {
    RecordWriter jsonRecordWriter = Json.newRecordWriter();
    List<Object> list = new ArrayList<>();
    list.add(1);
    list.add("2");
    Value v = JsonValueBuilder.initFrom(list);
    jsonRecordWriter.put("value", v);
    Record r = jsonRecordWriter.build();
    //logger.debug(r.toString()); NPE
  }

  @Test
  public void TestDecimalRange() {
    RecordWriter writer = Json.newRecordWriter();
    writer.putDecimal("d1", Integer.MAX_VALUE, 7);
    writer.putDecimal("d2", Integer.MIN_VALUE, 7);
    writer.putDecimal("d3", Long.MAX_VALUE, 9);
    writer.putDecimal("d4", Long.MIN_VALUE, 9);
    writer.putDecimal("d5", Integer.MAX_VALUE, 15);
    writer.putDecimal("d6", Integer.MIN_VALUE, 15);
    writer.putDecimal("d7", Long.MAX_VALUE, 25);
    writer.putDecimal("d8", Long.MIN_VALUE, 25);
    writer.build();
    System.out.println(writer);
  }

  @Test
  public void TestRecordPut() {
    Record record = Json.newRecord();
    record.set("recValue1", "string");
    record.set("recValue2", 1);
    Record record2 = Json.newRecord();
    record2.set("val1", true);
    record2.set("val2", 100);
    List<Object> l = new ArrayList<Object>();
    l.add("abcd");
    l.add(false);
    record2.set("list", l);
    record.set("rec", record2);
    RecordWriter recordWriter = Json.newRecordWriter();
    recordWriter.put("record", record);
    recordWriter.put("rootValue1", 1)
      .put("rootValue2", "2").build();
    System.out.println(recordWriter);

  }

}
