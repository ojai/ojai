/**
 * Copyright (c) 2018 MapR, Inc.
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
package org.ojai.util.impl;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ojai.Document;
import org.ojai.DocumentReader;
import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.ReadOnlyObjectException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

import com.google.common.base.Preconditions;

@API.Internal
public class ReadOnlyDocument implements Document {

  private final Document wrapped;

  public ReadOnlyDocument(final Document wrapped) {
    this.wrapped = Preconditions.checkNotNull(wrapped);
  }

  @Override
  public Map<String, Object> asMap() {
    return wrapped.asMap();
  }

  @Override
  public DocumentReader asReader() {
    return wrapped.asReader();
  }

  @Override
  public DocumentReader asReader(FieldPath fieldPath) {
    return wrapped.asReader(fieldPath);
  }

  @Override
  public DocumentReader asReader(String fieldPath) {
    return wrapped.asReader(fieldPath);
  }

  @Override
  public Document delete(FieldPath fieldPath) {
    throw readOnly();
  }

  @Override
  public Document delete(String fieldPath) {
    throw readOnly();
  }

  @Override
  public Document empty() {
    throw readOnly();
  }

  @Override
  public ByteBuffer getBinary(FieldPath fieldPath) {
    return wrapped.getBinary(fieldPath);
  }

  @Override
  public ByteBuffer getBinary(String fieldPath) {
    return wrapped.getBinary(fieldPath);
  }

  @Override
  public boolean getBoolean(FieldPath fieldPath) {
    return wrapped.getBoolean(fieldPath);
  }

  @Override
  public boolean getBoolean(String fieldPath) {
    return wrapped.getBoolean(fieldPath);
  }

  @Override
  public Boolean getBooleanObj(FieldPath fieldPath) {
    return wrapped.getBooleanObj(fieldPath);
  }

  @Override
  public Boolean getBooleanObj(String fieldPath) {
    return wrapped.getBooleanObj(fieldPath);
  }

  @Override
  public byte getByte(FieldPath fieldPath) {
    return wrapped.getByte(fieldPath);
  }

  @Override
  public byte getByte(String fieldPath) {
    return wrapped.getByte(fieldPath);
  }

  @Override
  public Byte getByteObj(FieldPath fieldPath) {
    return wrapped.getByteObj(fieldPath);
  }

  @Override
  public Byte getByteObj(String fieldPath) {
    return wrapped.getByteObj(fieldPath);
  }

  @Override
  public ODate getDate(FieldPath fieldPath) {
    return wrapped.getDate(fieldPath);
  }

  @Override
  public ODate getDate(String fieldPath) {
    return wrapped.getDate(fieldPath);
  }

  @Override
  public BigDecimal getDecimal(FieldPath fieldPath) {
    return wrapped.getDecimal(fieldPath);
  }

  @Override
  public BigDecimal getDecimal(String fieldPath) {
    return wrapped.getDecimal(fieldPath);
  }

  @Override
  public double getDouble(FieldPath fieldPath) {
    return wrapped.getDouble(fieldPath);
  }

  @Override
  public double getDouble(String fieldPath) {
    return wrapped.getDouble(fieldPath);
  }

  @Override
  public Double getDoubleObj(FieldPath fieldPath) {
    return wrapped.getDoubleObj(fieldPath);
  }

  @Override
  public Double getDoubleObj(String fieldPath) {
    return wrapped.getDoubleObj(fieldPath);
  }

  @Override
  public float getFloat(FieldPath fieldPath) {
    return wrapped.getFloat(fieldPath);
  }

  @Override
  public float getFloat(String fieldPath) {
    return wrapped.getFloat(fieldPath);
  }

  @Override
  public Float getFloatObj(FieldPath fieldPath) {
    return wrapped.getFloatObj(fieldPath);
  }

  @Override
  public Float getFloatObj(String fieldPath) {
    return wrapped.getFloatObj(fieldPath);
  }

  @Override
  public Value getId() {
    return wrapped.getId();
  }

  @Override
  public ByteBuffer getIdBinary() {
    return wrapped.getIdBinary();
  }

  @Override
  public String getIdString() {
    return wrapped.getIdString();
  }

  @Override
  public int getInt(FieldPath fieldPath) {
    return wrapped.getInt(fieldPath);
  }

  @Override
  public int getInt(String fieldPath) {
    return wrapped.getInt(fieldPath);
  }

  @Override
  public OInterval getInterval(FieldPath fieldPath) {
    return wrapped.getInterval(fieldPath);
  }

  @Override
  public OInterval getInterval(String fieldPath) {
    return wrapped.getInterval(fieldPath);
  }

  @Override
  public Integer getIntObj(FieldPath fieldPath) {
    return wrapped.getIntObj(fieldPath);
  }

  @Override
  public Integer getIntObj(String fieldPath) {
    return wrapped.getIntObj(fieldPath);
  }

  @Override
  public List<Object> getList(FieldPath fieldPath) {
    return wrapped.getList(fieldPath);
  }

  @Override
  public List<Object> getList(String fieldPath) {
    return wrapped.getList(fieldPath);
  }

  @Override
  public long getLong(FieldPath fieldPath) {
    return wrapped.getLong(fieldPath);
  }

  @Override
  public long getLong(String fieldPath) {
    return wrapped.getLong(fieldPath);
  }

  @Override
  public Long getLongObj(FieldPath fieldPath) {
    return wrapped.getLongObj(fieldPath);
  }

  @Override
  public Long getLongObj(String fieldPath) {
    return wrapped.getLongObj(fieldPath);
  }

  @Override
  public Map<String, Object> getMap(FieldPath fieldPath) {
    return wrapped.getMap(fieldPath);
  }

  @Override
  public Map<String, Object> getMap(String fieldPath) {
    return wrapped.getMap(fieldPath);
  }

  @Override
  public short getShort(FieldPath fieldPath) {
    return wrapped.getShort(fieldPath);
  }

  @Override
  public short getShort(String fieldPath) {
    return wrapped.getShort(fieldPath);
  }

  @Override
  public Short getShortObj(FieldPath fieldPath) {
    return wrapped.getShortObj(fieldPath);
  }

  @Override
  public Short getShortObj(String fieldPath) {
    return wrapped.getShortObj(fieldPath);
  }

  @Override
  public String getString(FieldPath fieldPath) {
    return wrapped.getString(fieldPath);
  }

  @Override
  public String getString(String fieldPath) {
    return wrapped.getString(fieldPath);
  }

  @Override
  public OTime getTime(FieldPath fieldPath) {
    return wrapped.getTime(fieldPath);
  }

  @Override
  public OTime getTime(String fieldPath) {
    return wrapped.getTime(fieldPath);
  }

  @Override
  public OTimestamp getTimestamp(FieldPath fieldPath) {
    return wrapped.getTimestamp(fieldPath);
  }

  @Override
  public OTimestamp getTimestamp(String fieldPath) {
    return wrapped.getTimestamp(fieldPath);
  }

  @Override
  public Value getValue(FieldPath fieldPath) {
    return wrapped.getValue(fieldPath);
  }

  @Override
  public Value getValue(String fieldPath) {
    return wrapped.getValue(fieldPath);
  }

  @Override
  public boolean isReadOnly() {
    return true;
  }

  @Override
  public Iterator<Entry<String, Value>> iterator() {
    return wrapped.iterator();
  }

  private ReadOnlyObjectException readOnly() {
    return new ReadOnlyObjectException("Attempt to modify a read-only document");
  }

  @Override
  public Document set(FieldPath fieldPath, BigDecimal value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, boolean value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, byte value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, byte[] value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, byte[] value, int off, int len) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, ByteBuffer value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, Document value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, double value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, float value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, int value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, List<? extends Object> value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, long value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, Map<String, ? extends Object> value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, ODate value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, OInterval value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, OTime value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, OTimestamp value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, short value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, String value) {
    throw readOnly();
  }

  @Override
  public Document set(FieldPath fieldPath, Value value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, BigDecimal value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, boolean value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, byte value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, byte[] value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, byte[] value, int off, int len) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, ByteBuffer value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, Document value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, double value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, float value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, int value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, List<? extends Object> value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, long value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, Map<String, ? extends Object> value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, ODate value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, OInterval value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, OTime value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, OTimestamp value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, short value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, String value) {
    throw readOnly();
  }

  @Override
  public Document set(String fieldPath, Value value) {
    throw readOnly();
  }

  @Override
  public Document setArray(FieldPath fieldPath, boolean[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(FieldPath fieldPath, byte[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(FieldPath fieldPath, double[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(FieldPath fieldPath, float[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(FieldPath fieldPath, int[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(FieldPath fieldPath, long[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(FieldPath fieldPath, Object... values) {
    throw readOnly();
  }

  @Override
  public Document setArray(FieldPath fieldPath, short[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(FieldPath fieldPath, String[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(String fieldPath, boolean[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(String fieldPath, byte[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(String fieldPath, double[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(String fieldPath, float[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(String fieldPath, int[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(String fieldPath, long[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(String fieldPath, Object... values) {
    throw readOnly();
  }

  @Override
  public Document setArray(String fieldPath, short[] values) {
    throw readOnly();
  }

  @Override
  public Document setArray(String fieldPath, String[] values) {
    throw readOnly();
  }

  @Override
  public Document setId(ByteBuffer _id) {
    throw readOnly();
  }

  @Override
  public Document setId(String _id) {
    throw readOnly();
  }

  @Override
  public Document setId(Value _id) {
    throw readOnly();
  }

  @Override
  public Document setNull(FieldPath fieldPath) {
    throw readOnly();
  }

  @Override
  public Document setNull(String fieldPath) {
    throw readOnly();
  }

  @Override
  public int size() {
    return wrapped.size();
  }

  @Override
  public <T> T toJavaBean(Class<T> beanClass) throws DecodingException {
    return wrapped.toJavaBean(beanClass);
  }

}
