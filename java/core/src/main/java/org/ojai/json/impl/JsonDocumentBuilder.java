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

import static org.ojai.Value.Type.ARRAY;
import static org.ojai.Value.Type.MAP;
import static org.ojai.util.Constants.MILLISECONDSPERDAY;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
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
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.util.impl.ContainerContext;
import org.ojai.util.Decimals;
import org.ojai.util.Types;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.google.common.base.Preconditions;

@API.Internal
public class JsonDocumentBuilder implements DocumentBuilder {

  private JsonGenerator jsonGenerator;
  private ByteArrayWriterOutputStream b;
  private String cachedJson;
  private Stack<ContainerContext> ctxStack;
  private ContainerContext currentContext;
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
        DefaultIndenter.SYSTEM_LINEFEED_INSTANCE.withLinefeed("\n"));
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
    try {
      JsonFactory jFactory = new JsonFactory();
      jsonGenerator = jFactory.createGenerator(out, JsonEncoding.UTF8);
      ctxStack = new Stack<ContainerContext>();
      currentContext = ContainerContext.NULL;
      checkContext = true;
      setJsonOptions(JsonOptions.WITH_TAGS);
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  private RuntimeException transformIOException(IOException ie) {
    return ie instanceof JsonGenerationException
        ? new IllegalStateException(ie) : new EncodingException(ie);
  }

  private void preparePut() {
    checkContext(MAP);
  }

  private void prepareAdd() {
    checkContext(ARRAY);
    if (currentContext.getType() == ARRAY) {
      currentContext.incrementIndex();
    }
  }

  private void checkContext(Type type) {
    Preconditions.checkState((!checkContext || currentContext.getType() == type),
        "Mismatch in writeContext. Expected %s but found %s",
        type, currentContext.getType());
  }

  @Override
  public JsonDocumentBuilder put(String field, boolean value) {
    try {
      preparePut();
      jsonGenerator.writeBooleanField(field, value);
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder put(String field, String value) {
    try {
      preparePut();
      jsonGenerator.writeStringField(field, value);
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder put(String field, byte value) {
    return putLongWithTag(field, Types.TAG_BYTE, value);
  }

  @Override
  public JsonDocumentBuilder put(String field, short value) {
    return putLongWithTag(field, Types.TAG_SHORT, value);
  }

  @Override
  public JsonDocumentBuilder put(String field, int value) {
    return putLongWithTag(field, Types.TAG_INT, value);
  }

  @Override
  public JsonDocumentBuilder put(String field, long value) {
    return putLongWithTag(field, Types.TAG_LONG, value);
  }

  @Override
  public JsonDocumentBuilder put(String field, float value) {
    return put(field, (double)value);
  }

  @Override
  public JsonDocumentBuilder put(String field, double value) {
    try {
      preparePut();
      if (isWholeNumberInLongRange(value)) {
        jsonGenerator.writeNumberField(field, (long)value);
      } else {
        jsonGenerator.writeNumberField(field, value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder put(String field, BigDecimal value) {
    try {
      preparePut();
      if (jsonOptions.isWithTags()) {
        putNewMap(field);
        jsonGenerator.writeStringField(Types.TAG_DECIMAL, value.toString());
        endMap();
      } else {
        jsonGenerator.writeNumberField(field, value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
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
    try {
      preparePut();
      if (jsonOptions.isWithTags()) {
        putNewMap(field);
        jsonGenerator.writeBinaryField(Types.TAG_BINARY, value);
        endMap();
      } else {
        jsonGenerator.writeBinaryField(field, value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder put(String field, byte[] value, int offset, int length) {
    try {
      preparePut();
      if (jsonOptions.isWithTags()) {
        putNewMap(field);
        jsonGenerator.writeFieldName(Types.TAG_BINARY);
        jsonGenerator.writeBinary(value, offset, length);
        endMap();
      } else {
        jsonGenerator.writeFieldName(field);
        jsonGenerator.writeBinary(value, offset, length);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder put(String field, ByteBuffer value) {
    byte[] bytes = new byte[value.remaining()];
    value.slice().get(bytes);
    return put(field, bytes);
  }

  private JsonDocumentBuilder putLongWithTag(String fieldname, String fieldTag, long value) {
    try {
      preparePut();
      if (jsonOptions.isWithTags()) {
        putNewMap(fieldname);
        jsonGenerator.writeNumberField(fieldTag, value);
        endMap();
      } else {
        jsonGenerator.writeNumberField(fieldname, value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  private JsonDocumentBuilder putStringWithTag(String fieldname, String fieldTag, String value) {
    try {
      preparePut();
      if (jsonOptions.isWithTags()) {
        putNewMap(fieldname);
        jsonGenerator.writeStringField(fieldTag, value);
        endMap();
      } else {
        jsonGenerator.writeStringField(fieldname, value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder put(String field, ODate value) {
    return putStringWithTag(field, Types.TAG_DATE, value.toDateStr());
  }

  @Override
  public JsonDocumentBuilder putDate(String field, int days) {
    return putStringWithTag(field, Types.TAG_DATE, ODate.fromDaysSinceEpoch(days).toDateStr());
  }

  @Override
  public JsonDocumentBuilder put(String field, OTime value) {
    return putStringWithTag(field, Types.TAG_TIME, value.toTimeStr());
  }

  @Override
  public JsonDocumentBuilder putTime(String field, int millis) {
    if (millis > MILLISECONDSPERDAY) {
      throw new IllegalArgumentException("Long value exceeds "
          + Long.toString(MILLISECONDSPERDAY) + " " + Long.toString(millis));
    }
    return putStringWithTag(field, Types.TAG_TIME, OTime.fromMillisOfDay(millis).toTimeStr());
  }

  @Override
  public JsonDocumentBuilder put(String field, OTimestamp value) {
    return putStringWithTag(field, Types.TAG_TIMESTAMP, value.toUTCString());
  }

  @Override
  public JsonDocumentBuilder putTimestamp(String field, long timeMillis) {
    return putStringWithTag(field, Types.TAG_TIMESTAMP, new OTimestamp(timeMillis).toUTCString());
  }

  @Override
  public JsonDocumentBuilder put(String field, OInterval value) {
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
    try {
      preparePut();
      jsonGenerator.writeFieldName(field);
      jsonGenerator.writeStartObject();
      ctxStack.push(currentContext);
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder putNewArray(String field) {
    try {
      preparePut();
      jsonGenerator.writeFieldName(field);
      jsonGenerator.writeStartArray();
      currentContext = ctxStack.push(new ContainerContext(ARRAY));
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder putNull(String field) {
    try {
      preparePut();
      jsonGenerator.writeNullField(field);
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
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
      if (value.getType() == MAP) {
        putNewMap(key);
        iterDocument(((Document) value).iterator());
      } else if (value.getType() == ARRAY) {
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
    preparePut();
    Iterator<Entry<String, Value>> it = value.iterator();
    putNewMap(field);
    return iterDocument(it);
  }

  @Override
  public DocumentBuilder setArrayIndex(int index) {
    checkContext(ARRAY);
    int lastIndex = currentContext.getIndex();
    if (index <= lastIndex) {
      throw new IllegalArgumentException(String.format(
          "Specified index %d is not larger than the last written index %d",
          index, lastIndex));
    }
    int nullCount = index - lastIndex;
    for (int i = 1; i < nullCount; i++) {
      addNull();
    }
    return this;
  }

  @Override
  public JsonDocumentBuilder add(boolean value) {
    try {
      prepareAdd();
      jsonGenerator.writeBoolean(value);
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder add(String value) {
    try {
      prepareAdd();
      jsonGenerator.writeString(value);
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
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
    try {
      prepareAdd();
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(tagName, value);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder add(float value) {
    return add((double)value);
  }

  @Override
  public JsonDocumentBuilder add(double value) {
    try {
      prepareAdd();
      if (isWholeNumberInLongRange(value)) {
        jsonGenerator.writeNumber((long)value);
      } else {
        jsonGenerator.writeNumber(value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder add(BigDecimal value) {
    try {
      prepareAdd();
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(Types.TAG_DECIMAL, value.toPlainString());
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder addDecimal(long decimalValue) {
    try {
      prepareAdd();
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(Types.TAG_DECIMAL, String.valueOf(decimalValue));
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(decimalValue);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder addDecimal(double decimalValue) {
    try {
      prepareAdd();
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(Types.TAG_DECIMAL, String.valueOf(decimalValue));
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(decimalValue);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
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
    try {
      prepareAdd();
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeBinaryField(Types.TAG_BINARY, value);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeBinary(value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder add(byte[] value, int offset, int length) {
    try {
      prepareAdd();
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName(Types.TAG_BINARY);
        jsonGenerator.writeBinary(value, offset, length);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeBinary(value, offset, length);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder add(ByteBuffer value) {
    byte[] bytes = new byte[value.remaining()];
    value.slice().get(bytes);
    return add(bytes);
  }

  @Override
  public JsonDocumentBuilder addNull() {
    try {
      prepareAdd();
      jsonGenerator.writeNull();
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
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
    try {
      prepareAdd();
      jsonGenerator.writeStartArray();
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
    currentContext = ctxStack.push(new ContainerContext(ARRAY));
    return this;
  }

  @Override
  public JsonDocumentBuilder addNewMap() {
    try {
      Preconditions.checkState((currentContext.getType() != MAP),
          "Context mismatch : addNewMap() can not be called at %s", currentContext.getType());
      if (currentContext.getType() == ARRAY) {
        prepareAdd();
      }
      jsonGenerator.writeStartObject();
      currentContext = ctxStack.push(new ContainerContext(MAP));
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  /* helper function to write date, time and timestamp types as string */
  private JsonDocumentBuilder addStringWithTag(String tagName, String value) {
    try {
      prepareAdd();
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(tagName, value);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeString(value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder add(OTime value) {
    return addStringWithTag(Types.TAG_TIME, value.toTimeStr());
  }

  @Override
  public JsonDocumentBuilder addTime(int millis) {
    return addStringWithTag(Types.TAG_TIME, OTime.fromMillisOfDay(millis).toTimeStr());
  }

  @Override
  public JsonDocumentBuilder add(ODate value) {
    return addStringWithTag(Types.TAG_DATE, value.toDateStr());
  }

  @Override
  public JsonDocumentBuilder addDate(int days) {
    return addStringWithTag(Types.TAG_DATE, ODate.fromDaysSinceEpoch(days).toDateStr());
  }

  @Override
  public JsonDocumentBuilder add(OTimestamp value) {
    return addStringWithTag(Types.TAG_TIMESTAMP, value.toUTCString());
  }

  @Override
  public JsonDocumentBuilder addTimestamp(long timeMillis) {
    return addStringWithTag(Types.TAG_TIMESTAMP, new OTimestamp(timeMillis).toUTCString());
  }

  @Override
  public JsonDocumentBuilder add(OInterval value) {
    return addLongWithTag(Types.TAG_INTERVAL, value.getTimeInMillis());
  }

  @Override
  public JsonDocumentBuilder addInterval(long durationInMs) {
    return addLongWithTag(Types.TAG_INTERVAL, durationInMs);
  }

  private JsonDocumentBuilder addLongWithTag(String tagName, long value) {
    try {
      prepareAdd();
      if (jsonOptions.isWithTags()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(tagName, value);
        jsonGenerator.writeEndObject();
      } else {
        jsonGenerator.writeNumber(value);
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder endArray() {
    try {
      checkContext(ARRAY);
      jsonGenerator.writeEndArray();
      ctxStack.pop();
      currentContext = ctxStack.isEmpty() ? ContainerContext.NULL : ctxStack.peek();
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
  }

  @Override
  public JsonDocumentBuilder endMap() {
    if (jsonGenerator.isClosed()) {
      throw new IllegalStateException("The document has already been built.");
    }

    try {
      preparePut();
      jsonGenerator.writeEndObject();
      ctxStack.pop();
      if (!ctxStack.empty()) {
        currentContext = ctxStack.peek();
      } else {
        //close the generator
        jsonGenerator.close();
      }
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
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
      } catch (IOException ie) {
        throw transformIOException(ie);
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
        checkContext(MAP);
        jsonGenerator.writeFieldName(field);
      } else {
        checkContext(ARRAY);
      }
      jsonGenerator.writeStartArray();
      currentContext = ctxStack.push(new ContainerContext(ARRAY));

      for (Object e : values) {
        add(JsonValueBuilder.initFromObject(e)); //FIXME: unnecessary object creation
      }
      jsonGenerator.writeEndArray();
      ctxStack.pop();
      currentContext = ctxStack.peek();
      return this;
    } catch (IOException ie) {
      throw transformIOException(ie);
    }
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
      DocumentStream documentStream = Json.newDocumentStream(inputStream);
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
