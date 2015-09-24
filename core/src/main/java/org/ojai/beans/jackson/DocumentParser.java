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
package org.ojai.beans.jackson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.annotation.API;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.base.ParserMinimalBase;

@API.Internal
public class DocumentParser extends ParserMinimalBase {

  protected final DocumentReader r;

  protected EventType _currEventType;
  protected ObjectCodec _objectCodec;
  protected boolean _closed = false;

  public DocumentParser(DocumentReader dr) {
    r = dr;
  }

  @Override
  public ObjectCodec getCodec() {
    return _objectCodec;
  }

  @Override
  public void setCodec(ObjectCodec c) {
    _objectCodec = c;
  }

  @Override
  public Version version() {
    return JacksonHelper.VERSION;
  }

  @Override
  public void close() throws IOException {
    _closed = true;
  }

  @Override
  public JsonToken nextToken() throws IOException, JsonParseException {
    _currEventType = r.next();
    switch (_currEventType) {
    case FIELD_NAME:
      _currToken = JsonToken.FIELD_NAME;
      break;
    case START_ARRAY:
      _currToken = JsonToken.START_ARRAY;
      break;
    case END_ARRAY:
      _currToken = JsonToken.END_ARRAY;
      break;
    case START_MAP:
      _currToken = JsonToken.START_OBJECT;
      break;
    case END_MAP:
      _currToken = JsonToken.END_OBJECT;
      break;
    case NULL:
      _currToken = JsonToken.VALUE_NULL;
      break;
    case STRING:
      _currToken = JsonToken.VALUE_STRING;
      break;
    case BYTE: case SHORT: case INT: case LONG:
      _currToken = JsonToken.VALUE_NUMBER_INT;
      break;
    case DECIMAL: case DOUBLE: case FLOAT:
      _currToken = JsonToken.VALUE_NUMBER_FLOAT;
      break;
    case BOOLEAN:
      _currToken = r.getBoolean() ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
      break;
    case DATE: case TIME: case TIMESTAMP: case INTERVAL: case BINARY:
      _currToken = JsonToken.VALUE_EMBEDDED_OBJECT;
      break;
    }
    return _currToken;
  }

  @Override
  public boolean isClosed() {
    return _closed;
  }

  @Override
  public String getCurrentName() throws IOException {
    return r.getFieldName();
  }

  @Override
  public JsonStreamContext getParsingContext() {
    return null;
  }

  @Override
  public String getText() throws IOException {
    return _currEventType == EventType.FIELD_NAME ? r.getFieldName() : r.getString();
  }

  @Override
  public Number getNumberValue() throws IOException {
    switch (_currEventType) {
    case BYTE:
      return r.getByte();
    case SHORT:
      return r.getShort();
    case INT:
      return r.getInt();
    case LONG:
      return r.getLong();
    case FLOAT:
      return r.getFloat();
    case DOUBLE:
      return r.getDouble();
    case DECIMAL:
      return r.getDecimal();
    default:
      throw new IllegalStateException("getNumberValue() called for event " + _currEventType);
    }
  }

  @Override
  public NumberType getNumberType() throws IOException {
    switch (_currEventType) {
    case BYTE:
    case SHORT:
    case INT:
      return NumberType.INT;
    case LONG:
      return NumberType.LONG;
    case FLOAT:
      return NumberType.FLOAT;
    case DOUBLE:
      return NumberType.DOUBLE;
    case DECIMAL:
      return NumberType.BIG_DECIMAL;
    default:
      throw new IllegalStateException("getNumberType() called for event " + _currEventType);
    }
  }

  @Override
  public int getIntValue() throws IOException {
    return (int) getLongValue();
  }

  @Override
  public long getLongValue() throws IOException {
    switch (_currEventType) {
    case BYTE:
      return r.getByte();
    case SHORT:
      return r.getShort();
    case INT:
      return r.getInt();
    case LONG:
      return r.getLong();
    case FLOAT:
      return (long) r.getFloat();
    case DOUBLE:
      return (long) r.getDouble();
    case DECIMAL:
      return r.getDecimal().longValue();
    default:
      throw new IllegalStateException("getLongValue() called for event " + _currEventType);
    }
  }

  @Override
  public BigInteger getBigIntegerValue() throws IOException {
    return getDecimalValue().toBigInteger();
  }

  @Override
  public float getFloatValue() throws IOException {
    return (float) getDoubleValue();
  }

  @Override
  public double getDoubleValue() throws IOException {
    switch (_currEventType) {
    case BYTE:
      return r.getByte();
    case SHORT:
      return r.getShort();
    case INT:
      return r.getInt();
    case LONG:
      return r.getLong();
    case FLOAT:
      return r.getFloat();
    case DOUBLE:
      return r.getDouble();
    case DECIMAL:
      return r.getDecimal().doubleValue();
    default:
      throw new IllegalStateException("getDoubleValue() called for event " + _currEventType);
    }
  }

  @Override
  public BigDecimal getDecimalValue() throws IOException {
    switch (_currEventType) {
    case BYTE:
      return new BigDecimal(r.getByte());
    case SHORT:
      return new BigDecimal(r.getShort());
    case INT:
      return new BigDecimal(r.getInt());
    case LONG:
      return new BigDecimal(r.getLong());
    case FLOAT:
      return new BigDecimal(r.getFloat());
    case DOUBLE:
      return new BigDecimal(r.getDouble());
    case DECIMAL:
      return r.getDecimal();
    default:
      throw new IllegalStateException("getDecimalValue() called for event " + _currEventType);
    }
  }

  @Override
  public Object getEmbeddedObject() throws IOException {
    switch (_currEventType) {
    case BINARY:
      return r.getBinary();
    case DATE:
      return r.getDate();
    case TIME:
      return r.getTime();
    case TIMESTAMP:
      return r.getTimestamp();
    case INTERVAL:
      return r.getInterval();
    default:
      throw new IllegalStateException("getEmbeddedObject() called for event " + _currEventType);
    }
  }

  @Override
  public byte[] getBinaryValue(Base64Variant bv) throws IOException {
    ByteBuffer buf = r.getBinary();
    byte[] result = new byte[buf.remaining()];
    buf.get(result);
    return result;
  }

  @Override
  public boolean hasTextCharacters() {
    return false;
  }

  @Override
  public char[] getTextCharacters() throws IOException {
    notImplemented();
    return null;
  }

  @Override
  public int getTextLength() throws IOException {
    notImplemented();
    return 0;
  }

  @Override
  public int getTextOffset() throws IOException {
    notImplemented();
    return 0;
  }

  @Override
  public JsonLocation getTokenLocation() {
    notImplemented();
    return null;
  }

  @Override
  public JsonLocation getCurrentLocation() {
    notImplemented();
    return null;
  }

  @Override
  public void overrideCurrentName(String name) {
    notImplemented();
  }

  @Override
  protected void _handleEOF() throws JsonParseException {
    notImplemented();
  }

  private void notImplemented() {
    throw new RuntimeException("Called operation not implemented for DocumentParser.");
  }

}