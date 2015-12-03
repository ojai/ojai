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
package org.ojai.json.impl;

import static org.ojai.util.Constants.MILLISECONDSPERDAY;

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
import java.util.Stack;

import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.DocumentStream;
import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.EncodingException;
import org.ojai.json.Json;
import org.ojai.json.JsonOptions;
import org.ojai.types.Interval;
import org.ojai.util.Decimals;
import org.ojai.util.Types;
import org.ojai.util.Values;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Lf2SpacesIndenter;
import com.google.common.base.Preconditions;

@API.Internal
public class JsonDocumentBuilder implements DocumentBuilder {

  private enum BuilderContext {
    MAPCONTEXT,
    ARRAYCONTEXT,
    NONE
  }

  private JsonGenerator jsonGenerator;
  private ByteArrayWriterOutputStream b;
  private String cachedJson;
  private Stack<BuilderContext> ctxStack;
  private BuilderContext currentContext;
  private JsonOptions jsonOptions;
  private boolean checkContext;

  public JsonDocumentBuilder() {
    b = new ByteArrayWriterOutputStream();
    initJsonGenerator(b);
  }

  protected JsonDocumentBuilder(OutputStream out) {
    initJsonGenerator(out);
  }

  private static final DefaultPrettyPrinter PRETTY_PRINTER;
  static {
    PRETTY_PRINTER = new DefaultPrettyPrinter();
    PRETTY_PRINTER.indentObjectsWith(// standardize on Unix line terminator
        Lf2SpacesIndenter.instance.withLinefeed("\n"));
  }
  public JsonDocumentBuilder setJsonOptions(JsonOptions options) {
    if (jsonOptions == null || jsonOptions.isPretty() != options.isPretty()) {
      if (options.isPretty()) {
        jsonGenerator.setPrettyPrinter(PRETTY_PRINTER);
      } else {
        jsonGenerator.setPrettyPrinter(null);
      }
    }
    jsonOptions = options;
    return this;
  }

  private void initJsonGenerator(OutputStream out) {
    JsonFactory jFactory = new JsonFactory();
    try {
      jsonGenerator = jFactory.createGenerator(out, JsonEncoding.UTF8);
      ctxStack = new Stack<BuilderContext>();
      currentContext = BuilderContext.NONE;
      checkContext = true;
      setJsonOptions(JsonOptions.WITH_TAGS);
    } catch (IOException io) {
      throw new EncodingException(io);
    }
  }

  private void checkContext(BuilderContext expectedContext) {
    Preconditions.checkState((!checkContext || currentContext == expectedContext),
        "Mismatch in writeContext. Expected %s but found %s",
        expectedContext.name(), currentContext.name());
  }

  @Override
  public JsonDocumentBuilder put(String field, boolean value) {
    checkContext(BuilderContext.MAPCONTEXT);
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
  public JsonDocumentBuilder put(String field, String value) {
    checkContext(BuilderContext.MAPCONTEXT);
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
  public JsonDocumentBuilder put(String field, byte value) {
    checkContext(BuilderContext.MAPCONTEXT);
    return putLongWithTag(field, Types.TAG_BYTE, value);
  }

  @Override
  public JsonDocumentBuilder put(String field, short value) {
    checkContext(BuilderContext.MAPCONTEXT);
    return putLongWithTag(field, Types.TAG_SHORT, value);
  }

  @Override
  public JsonDocumentBuilder put(String field, int value) {
    checkContext(BuilderContext.MAPCONTEXT);
    return putLongWithTag(field, Types.TAG_INT, value);
  }

  @Override
  public JsonDocumentBuilder put(String field, long value) {
    checkContext(BuilderContext.MAPCONTEXT);
    return putLongWithTag(field, Types.TAG_LONG, value);
  }

  @Override
  public JsonDocumentBuilder put(String field, float value) {
    return put(field, (double)value);
  }

  @Override
  public JsonDocumentBuilder put(String field, double value) {
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      if (isWholeNumberInLongRange(value)) {
        jsonGenerator.writeNumberField(field, (long)value);
      } else {
        jsonGenerator.writeNumberField(field, value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder put(String field, BigDecimal value) {
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        putNewMap(field);
        jsonGenerator.writeStringField(Types.TAG_DECIMAL, value.toString());
        endMap();
      } else {
        jsonGenerator.writeNumberField(field, value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder putDecimal(String field, long decimalValue) {
    return put(field, new BigDecimal(decimalValue));
  }

  @Override
  public JsonDocumentBuilder putDecimal(String field, double decimalValue) {
    return put(field, new BigDecimal(decimalValue));
  }

  @Override
  public JsonDocumentBuilder putDecimal(String field, int unscaledValue, int scale) {
    return put(field, Decimals.convertIntToDecimal(unscaledValue, scale));
  }

  @Override
  public JsonDocumentBuilder putDecimal(String field, long unscaledValue, int scale) {
    return put(field, Decimals.convertLongToDecimal(unscaledValue, scale));
  }

  @Override
  public JsonDocumentBuilder putDecimal(String field, byte[] unscaledValue, int scale) {
    return put(field, Decimals.convertByteToBigDecimal(unscaledValue, scale));
  }

  @Override
  public JsonDocumentBuilder put(String field, byte[] value) {
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        putNewMap(field);
        jsonGenerator.writeBinaryField(Types.TAG_BINARY, value);
        endMap();
      } else {
        jsonGenerator.writeBinaryField(field, value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder put(String field, byte[] value, int offset, int length) {
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        putNewMap(field);
        jsonGenerator.writeFieldName(Types.TAG_BINARY);
        jsonGenerator.writeBinary(value, offset, length);
        endMap();
      } else {
        jsonGenerator.writeFieldName(field);
        jsonGenerator.writeBinary(value, offset, length);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder put(String field, ByteBuffer value) {
    byte[] bytes = new byte[value.remaining()];
    value.slice().get(bytes);
    return put(field, bytes);
  }

  private JsonDocumentBuilder putLongWithTag(String fieldname, String fieldTag, long value) {
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        putNewMap(fieldname);
        jsonGenerator.writeNumberField(fieldTag, value);
        endMap();
      } else {
        jsonGenerator.writeNumberField(fieldname, value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  private JsonDocumentBuilder putStringWithTag(String fieldname, String fieldTag, String value) {
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        putNewMap(fieldname);
        jsonGenerator.writeStringField(fieldTag, value);
        endMap();
      } else {
        jsonGenerator.writeStringField(fieldname, value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder put(String field, Date value) {
    return putStringWithTag(field, Types.TAG_DATE, Values.toDateStr(value));
  }

  @Override
  public JsonDocumentBuilder putDate(String field, int days) {
    return putStringWithTag(field, Types.TAG_DATE, Values.toDateStr(JsonUtils.numDaysToDate(days)));
  }

  @Override
  public JsonDocumentBuilder put(String field, Time value) {
    return putStringWithTag(field, Types.TAG_TIME, Values.toTimeStr(value));
  }

  @Override
  public JsonDocumentBuilder putTime(String field, int millis) {
    if (millis > MILLISECONDSPERDAY) {
      throw new IllegalArgumentException("Long value exceeds "
          + Long.toString(MILLISECONDSPERDAY) + " " + Long.toString(millis));
    }
    return putStringWithTag(field, Types.TAG_TIME, Values.toTimeStr(new Time(millis)));
  }

  @Override
  public JsonDocumentBuilder put(String field, Timestamp value) {
    return putStringWithTag(field, Types.TAG_TIMESTAMP, Values.toTimestampString(value));
  }

  @Override
  public JsonDocumentBuilder putTimestamp(String field, long timeMillis) {
    return putStringWithTag(field, Types.TAG_TIMESTAMP, Values.toTimestampString(new Timestamp(timeMillis)));
  }

  @Override
  public JsonDocumentBuilder put(String field, Interval value) {
    return putLongWithTag(field, Types.TAG_INTERVAL, value.getTimeInMillis());
  }

  @Override
  public JsonDocumentBuilder putInterval(String field, long durationInMs) {
    return putLongWithTag(field, Types.TAG_INTERVAL, durationInMs);
  }

  @Override
  public JsonDocumentBuilder putInterval(String field, int months, int days, int milliseconds) {
    long total_milliseconds = milliseconds + (days + (long) months * 30) * MILLISECONDSPERDAY;
    return putLongWithTag(field, Types.TAG_INTERVAL, total_milliseconds);
  }

  @Override
  public JsonDocumentBuilder putNewMap(String field) {
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      jsonGenerator.writeFieldName(field);
      jsonGenerator.writeStartObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    ctxStack.push(currentContext);
    return this;
  }

  @Override
  public JsonDocumentBuilder putNewArray(String field) {
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      jsonGenerator.writeFieldName(field);
      jsonGenerator.writeStartArray();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    currentContext = BuilderContext.ARRAYCONTEXT;
    ctxStack.push(currentContext);
    return this;
  }

  @Override
  public JsonDocumentBuilder putNull(String field) {
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      jsonGenerator.writeNullField(field);
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }


  @Override
  public JsonDocumentBuilder put(String field, Value value) {

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
      put(field, (Document)value);
      break;
    case ARRAY:
      putArray(field, value.getList());
      break;
    default:
      break;
    }
    return this;
  }

  private JsonDocumentBuilder iterDocument(Iterator<Entry<String, Value>> it) {
    while (it.hasNext()) {
      Entry<String, Value> kv = it.next();
      String key = kv.getKey();
      Value value = kv.getValue();
      if (value.getType() == Type.MAP) {
        putNewMap(key);
        iterDocument(((Document) value).iterator());
      } else if (value.getType() == Type.ARRAY) {
        putArray(key, value.getList());
      } else {
        // process element.
        put(key, value);
      }
    }
    endMap();
    return this;
  }

  @Override
  public JsonDocumentBuilder put(String field, Map<String, Object> value) {
    return put(field, JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonDocumentBuilder put(String field, Document value) {
    // iterate over the document interface and extract tokens.
    // Add them to the writer.
    checkContext(BuilderContext.MAPCONTEXT);
    Iterator<Entry<String, Value>> it = value.iterator();
    putNewMap(field);
    return iterDocument(it);
  }

  @Override
  public JsonDocumentBuilder add(boolean value) {
    checkContext(BuilderContext.ARRAYCONTEXT);
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
  public JsonDocumentBuilder add(String value) {
    checkContext(BuilderContext.ARRAYCONTEXT);
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
  public JsonDocumentBuilder add(byte value) {
    return addLong(Types.TAG_BYTE, value);
  }

  @Override
  public JsonDocumentBuilder add(short value) {
    return addLong(Types.TAG_SHORT, value);
  }

  @Override
  public JsonDocumentBuilder add(int value) {
    return addLong(Types.TAG_INT, value);
  }

  @Override
  public JsonDocumentBuilder add(long value) {
    return addLong(Types.TAG_LONG, value);
  }

  private JsonDocumentBuilder addLong(String tagName, long value) {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(tagName, value);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder add(float value) {
    return add((double)value);
  }

  @Override
  public JsonDocumentBuilder add(double value) {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      if (isWholeNumberInLongRange(value)) {
        jsonGenerator.writeNumber((long)value);
      } else {
        jsonGenerator.writeNumber(value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder add(BigDecimal value) {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(Types.TAG_DECIMAL, value.toPlainString());
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder addDecimal(long decimalValue) {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(Types.TAG_DECIMAL, String.valueOf(decimalValue));
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(decimalValue);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder addDecimal(double decimalValue) {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(Types.TAG_DECIMAL, String.valueOf(decimalValue));
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(decimalValue);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder addDecimal(int unscaledValue, int scale) {
    return add(Decimals.convertIntToDecimal(unscaledValue, scale));
  }

  @Override
  public JsonDocumentBuilder addDecimal(long unscaledValue, int scale) {
    return add(Decimals.convertLongToDecimal(unscaledValue, scale));
  }

  @Override
  public JsonDocumentBuilder addDecimal(byte[] unscaledValue, int scale) {
    return add(Decimals.convertByteToBigDecimal(unscaledValue, scale));
  }

  @Override
  public JsonDocumentBuilder add(byte[] value) {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeBinaryField(Types.TAG_BINARY, value);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeBinary(value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder add(byte[] value, int offset, int length) {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName(Types.TAG_BINARY);
        jsonGenerator.writeBinary(value, offset, length);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeBinary(value, offset, length);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder add(ByteBuffer value) {
    byte[] bytes = new byte[value.remaining()];
    value.slice().get(bytes);
    return add(bytes);
  }

  @Override
  public JsonDocumentBuilder addNull() {
    checkContext(BuilderContext.ARRAYCONTEXT);
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
  public JsonDocumentBuilder add(Value value) {
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
      add((Document)value);
      break;
    case DECIMAL:
      add(value.getDecimal());
      break;
    case ARRAY:
      putArray(null, value.getList());
      break;
    default:
      throw new IllegalStateException("Unknown object type");
    }

    return this;
  }

  @Override
  public JsonDocumentBuilder add(Document value) {
    addNewMap();
    Iterator<Entry<String, Value>> it = value.iterator();
    return iterDocument(it);
  }

  @Override
  public JsonDocumentBuilder addNewArray() {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      jsonGenerator.writeStartArray();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    currentContext = BuilderContext.ARRAYCONTEXT;
    ctxStack.push(currentContext);
    return this;
  }

  @Override
  public JsonDocumentBuilder addNewMap() {
    Preconditions.checkState((currentContext != BuilderContext.MAPCONTEXT),
          "Context mismatch : addNewMap() can not be called at %s", currentContext.name());
    try {
      jsonGenerator.writeStartObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    currentContext = BuilderContext.MAPCONTEXT;
    ctxStack.push(currentContext);

    return this;
  }

  /* helper function to write date, time and timestamp types as string */
  private JsonDocumentBuilder addStringWithTag(String tagName, String value) {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(tagName, value);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeString(value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder add(Time value) {
    return addStringWithTag(Types.TAG_TIME, Values.toTimeStr(value));
  }

  @Override
  public JsonDocumentBuilder addTime(int millis) {
    return addStringWithTag(Types.TAG_TIME, Values.toTimeStr(new Time(millis)));
  }

  @Override
  public JsonDocumentBuilder add(Date value) {
    return addStringWithTag(Types.TAG_DATE, Values.toDateStr(value));
  }

  @Override
  public JsonDocumentBuilder addDate(int days) {
    return addStringWithTag(Types.TAG_DATE, Values.toDateStr(JsonUtils.numDaysToDate(days)));
  }

  @Override
  public JsonDocumentBuilder add(Timestamp value) {
    return addStringWithTag(Types.TAG_TIMESTAMP, Values.toTimestampString(value));
  }

  @Override
  public JsonDocumentBuilder addTimestamp(long timeMillis) {
    return addStringWithTag(Types.TAG_TIMESTAMP, Values.toTimestampString(new Timestamp(timeMillis)));
  }

  @Override
  public JsonDocumentBuilder add(Interval value) {
    return addLongWithTag(Types.TAG_INTERVAL, value.getTimeInMillis());
  }

  @Override
  public JsonDocumentBuilder addInterval(long durationInMs) {
    return addLongWithTag(Types.TAG_INTERVAL, durationInMs);
  }

  private JsonDocumentBuilder addLongWithTag(String tagName, long value) {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(tagName, value);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(value);
      }
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder endArray() {
    checkContext(BuilderContext.ARRAYCONTEXT);
    try {
      jsonGenerator.writeEndArray();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    ctxStack.pop();

    currentContext = ctxStack.isEmpty() ? BuilderContext.NONE : ctxStack.peek();

    return this;
  }

  @Override
  public JsonDocumentBuilder endMap() {
    if (jsonGenerator.isClosed()) {
      throw new IllegalStateException("The document has already been built.");
    }
    checkContext(BuilderContext.MAPCONTEXT);
    try {
      jsonGenerator.writeEndObject();
    } catch (JsonGenerationException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    ctxStack.pop();
    if (!ctxStack.empty()) {
      currentContext = ctxStack.peek();
    } else {
      //close the generator
      try {
        jsonGenerator.close();
      } catch (JsonGenerationException e) {
        throw new IllegalStateException(e);
      } catch (IOException ie) {
        throw new EncodingException(ie);
      }
    }

    return this;
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
      try {
        jsonGenerator.close();
      } catch (IOException e) {
      throw new RuntimeException(e);
      }
    }
    if (cachedJson == null) {
      cachedJson = toString();
    }
    return cachedJson;
  }

  public byte[] getOutputStream() {
    return b.getByteArray();
  }

  /* private function that adds an array as value of k-v pair */
  private JsonDocumentBuilder putArray(String field, List<Object> values) {
    try {
      if (field != null) {
        checkContext(BuilderContext.MAPCONTEXT);
        jsonGenerator.writeFieldName(field);
      } else {
        checkContext(BuilderContext.ARRAYCONTEXT);
      }
      jsonGenerator.writeStartArray();
      currentContext = BuilderContext.ARRAYCONTEXT;
      ctxStack.push(currentContext);

      for (Object e : values) {
        add(JsonValueBuilder.initFromObject(e));
      }
      jsonGenerator.writeEndArray();
      ctxStack.pop();
      currentContext = ctxStack.peek();
    } catch (JsonGenerationException je) {
      throw new IllegalStateException(je);
    } catch (IOException ie) {
      throw new EncodingException(ie);
    }
    return this;
  }

  private boolean isWholeNumberInLongRange(double value) {
    return (value == Math.floor(value))
        && !Double.isInfinite(value)
        && (value >= Long.MIN_VALUE && value <= Long.MAX_VALUE);
  }

  @Override
  public Document getDocument() {
    Preconditions.checkState(jsonGenerator.isClosed(), "The document has not been built.");

    if (b != null) {
      byte[] barray = b.getByteArray();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(barray);
      DocumentStream<Document> documentStream = Json.newDocumentStream(inputStream);
      Iterator<Document> iter = documentStream.iterator();
      if (iter.hasNext()) {
        Document r = iter.next();
        return r;
      }
    }
    return null;
  }

  @API.Internal
  public JsonDocumentBuilder setCheckContext(boolean value) {
    this.checkContext = value;
    return this;
  }

}
