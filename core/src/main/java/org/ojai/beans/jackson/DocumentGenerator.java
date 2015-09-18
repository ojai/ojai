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
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Stack;

import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.annotation.API;
import org.ojai.types.Interval;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;

@API.Internal
public class DocumentGenerator extends JsonGenerator {

  protected final DocumentBuilder       b;
  protected final Stack<Boolean> mapCtxts;

  protected boolean  closed = false;
  protected String    currFieldName;
  protected ObjectCodec objectCodec;

  public DocumentGenerator(DocumentBuilder db) {
    b = db;
    mapCtxts = new Stack<Boolean>();
  }

  public Document getDocument() {
    return b.getDocument();
  }

  @Override
  public void flush() throws IOException { /*no-op*/ }

  @Override
  public void close() throws IOException { closed = true; }

  @Override
  public boolean isClosed() {
    return closed;
  }

  @Override
  public JsonGenerator disable(Feature feature) { return this; }

  @Override
  public JsonGenerator enable(Feature feature) { return this; }

  @Override
  public ObjectCodec getCodec() {
    return objectCodec;
  }

  @Override
  public JsonGenerator setCodec(ObjectCodec c) {
    objectCodec = c;
    return this;
  }

  @Override
  public JsonStreamContext getOutputContext() {
    return null;
  }

  @Override
  public JsonGenerator useDefaultPrettyPrinter() {
    return this;
  }

  @Override
  public Version version() {
    return JacksonHelper.VERSION;
  }

  @Override
  public void writeBinary(Base64Variant bv,
      byte[] data, int offset, int len) throws IOException {
    ByteBuffer buff = ByteBuffer.wrap(data, offset, len);
    if (inMap()) {
      b.put(currFieldName, buff);
    } else {
      b.add(buff);
    }
  }

  @Override
  public void writeBoolean(boolean value) throws IOException {
    if (inMap()) {
      b.put(currFieldName, value);
    } else {
      b.add(value);
    }
  }

  @Override
  public void writeEndArray() throws IOException {
    b.endArray();
    mapCtxts.pop();
  }

  @Override
  public void writeEndObject() throws IOException {
    b.endMap();
    mapCtxts.pop();
  }

  @Override
  public void writeFieldName(String fieldName) throws IOException {
    currFieldName = fieldName;
  }

  @Override
  public void writeFieldName(SerializableString fieldName)
      throws IOException {
    currFieldName = fieldName.getValue();
  }

  @Override
  public void writeNull() throws IOException {
    if (inMap()) {
      b.putNull(currFieldName);
    } else {
      b.addNull();
    }
  }

  @Override
  public void writeNumber(short value) throws IOException {
    if (inMap()) {
      b.put(currFieldName, value);
    } else {
      b.add(value);
    }
  }

  @Override
  public void writeNumber(int value) throws IOException {
    if (inMap()) {
      b.put(currFieldName, value);
    } else {
      b.add(value);
    }
  }

  @Override
  public void writeNumber(long value) throws IOException {
    if (inMap()) {
      b.put(currFieldName, value);
    } else {
      b.add(value);
    }
  }

  @Override
  public void writeNumber(BigInteger value) throws IOException {
    if (inMap()) {
      b.put(currFieldName, new BigDecimal(value));
    } else {
      b.add(new BigDecimal(value));
    }
  }

  @Override
  public void writeNumber(double value) throws IOException {
    if (inMap()) {
      b.put(currFieldName, value);
    } else {
      b.add(value);
    }
  }

  @Override
  public void writeNumber(float value) throws IOException {
    if (inMap()) {
      b.put(currFieldName, value);
    } else {
      b.add(value);
    }
  }

  @Override
  public void writeNumber(BigDecimal value) throws IOException {
    if (inMap()) {
      b.put(currFieldName, value);
    } else {
      b.add(value);
    }
  }

  @Override
  public void writeNumber(String value) throws IOException {
    if (value.contains(".")) {
      writeNumber(Double.valueOf(value));
    } else {
      writeNumber(Long.valueOf(value));
    }
  }

  @Override
  public void writeObject(Object value) throws IOException {
    if (value instanceof Byte) {
      if (inMap()) {
        b.put(currFieldName, ((Byte) value).byteValue());
      } else {
        b.add(((Byte) value).byteValue());
      }
    } else if (value instanceof Interval) {
      if (inMap()) {
        b.put(currFieldName, ((Interval) value));
      } else {
        b.add(((Interval) value));
      }
    } else if (value instanceof Date) {
      if (inMap()) {
        b.put(currFieldName, ((Date) value));
      } else {
        b.add(((Date) value));
      }
    } else if (value instanceof Time) {
      if (inMap()) {
        b.put(currFieldName, ((Time) value));
      } else {
        b.add(((Time) value));
      }
    } else if (value instanceof Timestamp) {
      if (inMap()) {
        b.put(currFieldName, ((Timestamp) value));
      } else {
        b.add(((Timestamp) value));
      }
    } else {
      throw new IllegalStateException("writeObject() called for type " + value.getClass());
    }
  }

  @Override
  public void writeStartArray() throws IOException {
    if (inMap()) {
      b.putNewArray(currFieldName);
    } else {
      b.addNewArray();
    }
    mapCtxts.push(false);
  }

  @Override
  public void writeStartObject() throws IOException {
    if (inMap()) {
      b.putNewMap(currFieldName);
    } else {
      b.addNewMap();
    }
    mapCtxts.push(true);
  }

  @Override
  public void writeString(String value) throws IOException {
    if (inMap()) {
      b.put(currFieldName, value);
    } else {
      b.add(value);
    }
  }

  @Override
  public void writeString(SerializableString value) throws IOException {
    writeString(value.getValue());
  }

  @Override
  public void writeString(char[] text, int offset, int len) throws IOException {
    writeString(new String(text, offset, len));
  }

  @Override
  public void writeUTF8String(byte[] text, int offset, int length)
      throws IOException {
    writeString(new String(text, offset, length, "UTF-8"));
  }

  @Override
  public void writeTree(TreeNode rootNode) throws IOException {
    if (rootNode == null) {
      writeNull();
    } else {
      if (objectCodec == null) {
        throw new IllegalStateException("No ObjectCodec defined");
      }
      objectCodec.writeValue(this, rootNode);
    }
  }

  @Override
  public int getFeatureMask() {
    notImplemented();
    return 0;
  }

  @Override
  public boolean isEnabled(Feature feature) {
    notImplemented();
    return false;
  }

  @Override
  public JsonGenerator setFeatureMask(int featureMask) {
    notImplemented();
    return this;
  }

  @Override
  public int writeBinary(Base64Variant bv,
      InputStream data, int dataLength) throws IOException {
    notImplemented();
    return 0;
  }

  @Override
  public void writeRaw(String value) throws IOException {
    notImplemented();
  }

  @Override
  public void writeRaw(char value) throws IOException {
    notImplemented();
  }

  @Override
  public void writeRaw(String text, int offset, int len) throws IOException {
    notImplemented();
  }

  @Override
  public void writeRaw(char[] text, int offset, int len) throws IOException {
    notImplemented();
  }

  @Override
  public void writeRawUTF8String(byte[] text, int offset, int length)
      throws IOException {
    notImplemented();
  }

  @Override
  public void writeRawValue(String value) throws IOException {
    notImplemented();
  }

  @Override
  public void writeRawValue(String text, int offset, int len) throws IOException {
    notImplemented();
  }

  @Override
  public void writeRawValue(char[] text, int offset, int len) throws IOException {
    notImplemented();
  }

  private void notImplemented() {
    throw new RuntimeException("Called operation not implemented for DocumentGenerator.");
  }

  private boolean inMap() {
    return !mapCtxts.isEmpty() && mapCtxts.peek();
  }

}
