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
package org.jackhammer.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jackhammer.Record;
import org.jackhammer.RecordStream;
import org.jackhammer.RecordWriter;
import org.jackhammer.Value;
import org.jackhammer.Value.Type;
import org.jackhammer.annotation.API;
import org.jackhammer.exceptions.EncodingException;
import org.jackhammer.types.Interval;
import org.jackhammer.util.Constants;
import org.jackhammer.util.Decimals;
import org.jackhammer.util.Types;
import org.jackhammer.util.Values;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

@API.Internal
public class JsonRecordWriter implements RecordWriter, Constants {

  private JsonGenerator jsonGenerator;
  private ByteArrayWriterOutputStream b;
  private String cachedJson;

  JsonRecordWriter() {
    b = new ByteArrayWriterOutputStream();
    this.initJsonGenerator(b);
  }

  protected JsonRecordWriter(OutputStream out) {
    this.initJsonGenerator(out);
  }

  private void initJsonGenerator(OutputStream out) {
    JsonFactory jFactory = new JsonFactory();
    try {
      jsonGenerator = jFactory.createGenerator(out, JsonEncoding.UTF8);
      jsonGenerator.writeStartObject();
    } catch (IOException io) {
      throw new EncodingException(io);
    }
  }

  @Override
  public JsonRecordWriter put(String field, boolean value) {

    try {
      jsonGenerator.writeBooleanField(field, value);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, String value) {

    try {
      jsonGenerator.writeStringField(field, value);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, byte value) {

    putNewMap(field);
    try {
      jsonGenerator.writeNumberField(Types.TAG_BYTE, value);
      endMap();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, short value) {
    try {
      this.putNewMap(field);
      jsonGenerator.writeNumberField(Types.TAG_SHORT, value);
      this.endMap();

    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new EncodingException(e);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, int value) {
    try {
      this.putNewMap(field);
      jsonGenerator.writeNumberField(Types.TAG_INT, value);
      this.endMap();

    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new EncodingException(e);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, long value) {
    try {
      jsonGenerator.writeNumberField(field, value);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, float value) {
    try {
      putNewMap(field);
      jsonGenerator.writeNumberField(Types.TAG_FLOAT, value);
      endMap();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, double value) {
    try {
      jsonGenerator.writeNumberField(field, value);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, BigDecimal value) {
    try {
      putNewMap(field);
      jsonGenerator.writeNumberField(Types.TAG_DECIMAL, value);
      endMap();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter putDecimal(String field, long decimalValue) {
    return put(field, new BigDecimal(decimalValue));
  }

  @Override
  public JsonRecordWriter putDecimal(String field, double decimalValue) {
    return put(field, new BigDecimal(decimalValue));
  }

  @Override
  public JsonRecordWriter putDecimal(String field, int unscaledValue, int scale) {
    return put(field, Decimals.convertIntToDecimal(unscaledValue, scale));
  }

  @Override
  public JsonRecordWriter putDecimal(String field, long unscaledValue, int scale) {
    return put(field, Decimals.convertLongToDecimal(unscaledValue, scale));
  }

  @Override
  public JsonRecordWriter putDecimal(String field, byte[] unscaledValue,
      int scale) {
    return put(field,
        Decimals.convertByteToBigDecimal(unscaledValue, scale));
  }

  @Override
  public JsonRecordWriter put(String field, byte[] value) {
    try {
      putNewMap(field);
      jsonGenerator.writeBinaryField(Types.TAG_BINARY, value);
      endMap();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, byte[] value, int offset, int length) {
    try {
      putNewMap(field);
      jsonGenerator.writeFieldName(Types.TAG_BINARY);
      jsonGenerator.writeBinary(value, offset, length);
      endMap();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, ByteBuffer value) {
    byte[] bytes = new byte[value.remaining()];
    value.get(bytes);
    return put(field, bytes);
  }

  private JsonRecordWriter putLongWithTag(String fieldname, String fieldTag, long value) {
    try {
      putNewMap(fieldname);
      jsonGenerator.writeNumberField(fieldTag, value);
      endMap();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  private JsonRecordWriter putStringWithTag(String fieldname, String fieldTag, String value) {
    try {
      putNewMap(fieldname);
      jsonGenerator.writeStringField(fieldTag, value);
      endMap();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, Date value) {
    return putStringWithTag(field, Types.TAG_DATE, Values.toDateStr(value));
  }

  @Override
  public JsonRecordWriter putDate(String field, int days) {
    return putStringWithTag(field, Types.TAG_DATE, Values.toDateStr(new Date(days * MILLISECONDSPERDAY)));
  }

  @Override
  public JsonRecordWriter put(String field, Time value) {
    return putStringWithTag(field, Types.TAG_TIME, Values.toTimeStr(value));
  }

  @Override
  public JsonRecordWriter putTime(String field, int millis) {
    if (millis > MILLISECONDSPERDAY) {
      throw new IllegalArgumentException("Long value exceeds "
          + Long.toString(MILLISECONDSPERDAY) + " " + Long.toString(millis));
    }
    return putStringWithTag(field, Types.TAG_TIME, Values.toTimeStr(new Time(millis)));
  }

  @Override
  public JsonRecordWriter put(String field, Timestamp value) {
    return putStringWithTag(field, Types.TAG_TIMESTAMP, Values.toTimestampString(value));
  }

  @Override
  public JsonRecordWriter putTimestamp(String field, long timeMillis) {
    return putStringWithTag(field, Types.TAG_TIMESTAMP, Values.toTimestampString(new Timestamp(timeMillis)));
  }

  @Override
  public JsonRecordWriter put(String field, Interval value) {
    return putLongWithTag(field, Types.TAG_INTERVAL, value.getInterval());
  }

  @Override
  public JsonRecordWriter putInterval(String field, long durationInMs) {
    return putLongWithTag(field, Types.TAG_INTERVAL, durationInMs);
  }

  @Override
  public JsonRecordWriter putInterval(String field, int months, int days, int milliseconds) {
    long total_milliseconds = milliseconds + (days + (long) months * 30) * MILLISECONDSPERDAY;
    return putLongWithTag(field, Types.TAG_INTERVAL, total_milliseconds);
  }

  @Override
  public JsonRecordWriter putNewMap(String field) {
    try {
      jsonGenerator.writeFieldName(field);
      jsonGenerator.writeStartObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter putNewArray(String field) {
    try {
      jsonGenerator.writeFieldName(field);
      jsonGenerator.writeStartArray();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter putNull(String field) {
    try {
      jsonGenerator.writeNullField(field);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  /*
   * Implementation for adding a map to JSON document.
   */
  private JsonRecordWriter putMap(String field, Map<String, Object> map) {
    try {
      jsonGenerator.writeFieldName(field);
      jsonGenerator.writeStartObject();
      Iterator<Entry<String, Object>> entries = map.entrySet().iterator();
      while (entries.hasNext()) {
        Entry<String, Object> keyvaluepair = entries.next();
        String key = keyvaluepair.getKey();
        Value val = (Value) keyvaluepair.getValue();
        put(key, val);
      }
      jsonGenerator.writeEndObject();

    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, Value value) {

    Value.Type t = value.getType();
    switch (t) {
    case NULL:
      putNull(field);
      break;
    case BOOLEAN:
      put(field, value.getBoolean());
      break;
    case STRING:
      put(field, value.getString());
      break;
    case BYTE:
      put(field, value.getByte());
      break;
    case SHORT:
      put(field, value.getShort());
      break;
    case INT:
      put(field, value.getInt());
      break;
    case LONG:
      put(field, value.getLong());
      break;
    case FLOAT:
      put(field, value.getFloat());
      break;
    case DOUBLE:
      put(field, value.getDouble());
      break;
    case DECIMAL:
      put(field, value.getDecimal());
      break;
    case DATE:
      put(field, value.getDate());
      break;
    case TIME:
      put(field, value.getTime());
      break;
    case TIMESTAMP:
      put(field, value.getTimestamp());
      break;
    case INTERVAL:
      put(field, value.getInterval());
      break;
    case BINARY:
      put(field, value.getBinary());
      break;
    case MAP:
      put(field, (Record)value);
      break;
    case ARRAY:
      addArray(field, value.getList());
      break;
    default:
      break;
    }
    return this;
  }

  private JsonRecordWriter iterRecord(Iterator<Entry<String, Value>> it) {
    while (it.hasNext()) {
      Entry<String, Value> kv = it.next();
      String key = kv.getKey();
      Value value = kv.getValue();
      if (value.getType() == Type.MAP) {
        putNewMap(key);
        iterRecord(((Record) value).iterator());
      } else if (value.getType() == Type.ARRAY) {
        addArray(key, value.getList());
      } else {
        // process element.
        put(key, value);
      }
    }
    endMap();
    return this;
  }

  @Override
  public JsonRecordWriter put(String field, Record value) {
    // iterate over the record interface and extract tokens.
    // Add them to the writer.

    Iterator<Entry<String, Value>> it = value.iterator();
    putNewMap(field);
    return iterRecord(it);
  }

  @Override
  public JsonRecordWriter add(boolean value) {
    try {
      jsonGenerator.writeBoolean(value);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(String value) {
    try {
      jsonGenerator.writeString(value);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(byte value) {
    return AddInt(Types.TAG_BYTE, value);
  }

  @Override
  public JsonRecordWriter add(short value) {
    return AddInt(Types.TAG_SHORT, value);
  }

  private JsonRecordWriter AddInt(String field, int value) {
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField(field, value);
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(int value) {
    return AddInt(Types.TAG_INT, value);
  }

  @Override
  public JsonRecordWriter add(long value) {
    try {
      jsonGenerator.writeNumber(value);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(float value) {
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField(Types.TAG_FLOAT, value);
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(double value) {
    try {
      jsonGenerator.writeNumber(value);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(BigDecimal value) {
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField(Types.TAG_DECIMAL, value);
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter addDecimal(long decimalValue) {
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField(Types.TAG_DECIMAL, decimalValue);
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter addDecimal(double decimalValue) {

    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField(Types.TAG_DECIMAL, decimalValue);
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter addDecimal(int unscaledValue, int scale) {
    return add(Decimals.convertIntToDecimal(unscaledValue, scale));
  }

  @Override
  public JsonRecordWriter addDecimal(long unscaledValue, int scale) {
    return add(Decimals.convertLongToDecimal(unscaledValue, scale));
  }

  @Override
  public JsonRecordWriter addDecimal(byte[] unscaledValue, int scale) {
    return add(Decimals.convertByteToBigDecimal(unscaledValue, scale));
  }

  @Override
  public JsonRecordWriter add(byte[] value) {
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeBinaryField(Types.TAG_BINARY, value);
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(byte[] value, int offset, int length) {
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeFieldName(Types.TAG_BINARY);
      jsonGenerator.writeBinary(value, offset, length);
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(ByteBuffer value) {
    byte[] bytes = new byte[value.remaining()];
    value.get(bytes);
    return add(bytes);
  }

  @Override
  public JsonRecordWriter addNull() {
    try {
      jsonGenerator.writeNull();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(Value value) {
    Value.Type t = value.getType();
    switch (t) {
    case NULL:
      addNull();
      break;
    case BOOLEAN:
      add(value.getBoolean());
      break;
    case STRING:
      add(value.getString());
      break;
    case BYTE:
      add(value.getByte());
      break;
    case SHORT:
      add(value.getShort());
      break;
    case INT:
      add(value.getInt());
      break;
    case LONG:
      add(value.getLong());
      break;
    case FLOAT:
      add(value.getFloat());
      break;
    case DOUBLE:
      add(value.getDouble());
      break;
    case DATE:
      add(value.getDate());
      break;
    case TIME:
      add(value.getTime());
      break;
    case TIMESTAMP:
      add(value.getTimestamp());
      break;
    case INTERVAL:
      add(value.getInterval());
      break;
    case BINARY:
      add(value.getBinary());
      break;
    case MAP:
      add((Record)value);
      break;
    case DECIMAL:
      add(value.getDecimal());
      break;
    case ARRAY:
      addArray(null, value.getList());
      break;
    default:
      throw new IllegalStateException("Unknown object type");
    }

    return this;
  }

  @Override
  public JsonRecordWriter add(Record value) {
    addNewMap();
    Iterator<Entry<String, Value>> it = value.iterator();
    return iterRecord(it);
  }

  @Override
  public JsonRecordWriter addNewArray() {
    try {
      jsonGenerator.writeStartArray();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter addNewMap() {
    try {
      jsonGenerator.writeStartObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  private JsonRecordWriter addLongWithTag(String tagName, long value) {
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField(tagName, value);
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  /* helper function to write date, time and timestamp types as string */
  private JsonRecordWriter addStringWithTag(String tagName, String value) {
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField(tagName, value);
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter add(Time value) {
    return addStringWithTag(Types.TAG_TIME, Values.toTimeStr(value));
  }

  @Override
  public JsonRecordWriter addTime(int millis) {
    return addStringWithTag(Types.TAG_TIME, Values.toTimeStr(new Time(millis)));
  }

  @Override
  public JsonRecordWriter add(Date value) {
    return addStringWithTag(Types.TAG_DATE, Values.toDateStr(value));
  }

  @Override
  public JsonRecordWriter addDate(int days) {
    return addStringWithTag(Types.TAG_DATE, Values.toDateStr(new Date(days * MILLISECONDSPERDAY)));
  }

  @Override
  public JsonRecordWriter add(Timestamp value) {
    return addStringWithTag(Types.TAG_TIMESTAMP, Values.toTimestampString(value));
  }

  @Override
  public JsonRecordWriter addTimestamp(long timeMillis) {
    return addStringWithTag(Types.TAG_TIMESTAMP, Values.toTimestampString(new Timestamp(timeMillis)));
  }

  @Override
  public JsonRecordWriter add(Interval value) {
    return addLongWithTag(Types.TAG_INTERVAL, value.getInterval());
  }

  @Override
  public JsonRecordWriter addInterval(long durationInMs) {
    return addLongWithTag(Types.TAG_INTERVAL, durationInMs);
  }

  @Override
  public JsonRecordWriter endArray() {
    try {
      jsonGenerator.writeEndArray();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonRecordWriter endMap() {
    try {
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public Record build() {
    if (jsonGenerator.isClosed()) {
      throw new IllegalStateException("The record has already been built.");
    }

    try {
      jsonGenerator.close();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }

    if (b != null) {
      byte[] barray = b.getByteArray();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(barray);
      RecordStream<Record> recordStream = Json.newRecordStream(inputStream);
      Iterator<Record> iter = recordStream.iterator();
      if (iter.hasNext()) {
        Record r = iter.next();
        return r;
      }
    }


    return null;
  }

  @Override
  public String toString() {
    try {
      return b.toString("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public String asUTF8String() {
    if (!jsonGenerator.isClosed()) {
      throw new IllegalStateException("The record has not been built.");
    }

    if (cachedJson == null) {
      cachedJson = toString();
    }
    return cachedJson;
  }

  public byte[] getOutputStream() {
    return b.getByteArray();
  }

  private JsonRecordWriter addArray(String field, List<Object> values) {
    try {
      if (field != null) {
        jsonGenerator.writeFieldName(field);
      }
      jsonGenerator.writeStartArray();
      for (Iterator<Object> it = values.iterator(); it
          .hasNext();) {
        Object e = it.next();
        add(JsonValueBuilder.initFromObject(e));
      }
      jsonGenerator.writeEndArray();
    } catch (JsonGenerationException je) {
      throw new IllegalStateException(je);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  public void enablePrettyPrinting(boolean enable) {
    if (enable) {
      jsonGenerator.useDefaultPrettyPrinter();
    } else {
      jsonGenerator.setPrettyPrinter(null);
    }
  }

}
