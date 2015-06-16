/**
 * Copyright (c) 2014 MapR, Inc.
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
package org.jackhammer;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.jackhammer.annotation.API;
import org.jackhammer.exceptions.TypeException;
import org.jackhammer.types.Interval;

@API.Public
public interface Record extends Iterable<Map.Entry<String, Value>> {

  Record set(String fieldPath, String value);

  Record set(FieldPath fieldPath, String value);

  Record set(String fieldPath, boolean value);

  Record set(FieldPath fieldPath, boolean value);

  Record set(String fieldPath, byte value);

  Record set(FieldPath fieldPath, byte value);

  Record set(String fieldPath, short value);

  Record set(FieldPath fieldPath, short value);

  Record set(String fieldPath, int value);

  Record set(FieldPath fieldPath, int value);

  Record set(String fieldPath, long value);

  Record set(FieldPath fieldPath, long value);

  Record set(String fieldPath, float value);

  Record set(FieldPath fieldPath, float value);

  Record set(String fieldPath, double value);

  Record set(FieldPath fieldPath, double value);

  Record set(String fieldPath, BigDecimal value);

  Record set(FieldPath fieldPath, BigDecimal value);

  Record set(String fieldPath, Time value);

  Record set(FieldPath fieldPath, Time value);

  Record set(String fieldPath, Date value);

  Record set(FieldPath fieldPath, Date value);

  Record set(String fieldPath, Timestamp value);

  Record set(FieldPath fieldPath, Timestamp value);

  Record set(String fieldPath, Interval value);

  Record set(FieldPath fieldPath, Interval value);

  Record set(String fieldPath, byte[] value);

  Record set(FieldPath fieldPath, byte[] value);

  Record set(String fieldPath, byte[] value, int off, int len);

  Record set(FieldPath fieldPath, byte[] value, int off, int len);

  Record set(String fieldPath, ByteBuffer value);

  Record set(FieldPath fieldPath, ByteBuffer value);

  Record set(String fieldPath, Map<String, ? extends Object> value);

  Record set(FieldPath fieldPath, Map<String, ? extends Object> value);

  Record set(String fieldPath, Record value);

  Record set(FieldPath fieldPath, Record value);

  Record set(String fieldPath, Value value);

  Record set(FieldPath fieldPath, Value value);

  Record set(String fieldPath, List<? extends Object> value);

  Record set(FieldPath fieldPath, List<? extends Object> value);

  Record setArray(String fieldPath, boolean[] values);

  Record setArray(FieldPath fieldPath, boolean[] values);

  Record setArray(String fieldPath, byte[] values);

  Record setArray(FieldPath fieldPath, byte[] values);

  Record setArray(String fieldPath, short[] values);

  Record setArray(FieldPath fieldPath, short[] values);

  Record setArray(String fieldPath, int[] values);

  Record setArray(FieldPath fieldPath, int[] values);

  Record setArray(String fieldPath, long[] values);

  Record setArray(FieldPath fieldPath, long[] values);

  Record setArray(String fieldPath, float[] values);

  Record setArray(FieldPath fieldPath, float[] values);

  Record setArray(String fieldPath, double[] values);

  Record setArray(FieldPath fieldPath, double[] values);

  Record setArray(String fieldPath, String[] values);

  Record setArray(FieldPath fieldPath, String[] values);

  Record setArray(String fieldPath, Object... values);

  Record setArray(FieldPath fieldPath, Object... values);

  Record setNull(String fieldPath);

  Record setNull(FieldPath fieldPath);

  /**
   * Deletes the value at the specified {@code FieldPath} if exists.
   * @param fieldPath The {@code fieldPath} to delete from the record.
   * @return {@code this} for chaining.
   */
  Record delete(String fieldPath);

  /**
   * Deletes the value at the specified {@code FieldPath} if exists.
   * @param fieldPath The {@code fieldPath} to delete from the record.
   * @return {@code this} for chaining.
   */
  Record delete(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code String}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>STRING</code> type.
   */
  String getString(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code String}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>STRING</code> type.
   */
  String getString(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code boolean}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  boolean getBoolean(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code boolean}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  boolean getBoolean(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Boolean}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type.
   */
  Boolean getBooleanObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Boolean}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type.
   */
  Boolean getBooleanObj(FieldPath fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code byte}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BYTE</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  byte getByte(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code byte}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BYTE</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  byte getByte(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Byte}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BYTE</code> type.
   */
  Byte getByteObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Byte}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BYTE</code> type.
   */
  Byte getByteObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code short}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>SHORT</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  short getShort(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code short}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>SHORT</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  short getShort(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Short}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>SHORT</code> type.
   */
  Short getShortObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Short}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>SHORT</code> type.
   */
  Short getShortObj(FieldPath fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code int}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INT</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  int getInt(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code int}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INT</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  int getInt(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Integer}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INT</code> type.
   */
  Integer getIntObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Integer}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INT</code> type.
   */
  Integer getIntObj(FieldPath fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code long}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>LONG</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  long getLong(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code long}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>LONG</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  long getLong(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Long}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>LONG</code> type.
   */
  Long getLongObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Long}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>LONG</code> type.
   */
  Long getLongObj(FieldPath fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code float}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>FLOAT</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  float getFloat(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code float}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>FLOAT</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  float getFloat(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Float}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>FLOAT</code> type.
   */
  Float getFloatObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Float}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>FLOAT</code> type.
   */
  Float getFloatObj(FieldPath fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code double}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DOUBLE</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  double getDouble(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code double}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DOUBLE</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Record}.
   */
  double getDouble(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Double}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DOUBLE</code> type.
   */
  Double getDoubleObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Double}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DOUBLE</code> type.
   */
  Double getDoubleObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DECIMAL</code> type.
   */
  BigDecimal getDecimal(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DECIMAL</code> type.
   */
  BigDecimal getDecimal(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Time}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>TIME</code> type.
   */
  Time getTime(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Time}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>TIME</code> type.
   */
  Time getTime(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Date}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DATE</code> type.
   */
  Date getDate(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Date}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DATE</code> type.
   */
  Date getDate(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Timestamp}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>TIMESTAMP</code> type.
   */
  Timestamp getTimestamp(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Timestamp}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>TIMESTAMP</code> type.
   */
  Timestamp getTimestamp(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BINARY</code> type.
   */
  ByteBuffer getBinary(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BINARY</code> type.
   */
  ByteBuffer getBinary(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Interval}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INTERVAL</code> type.
   */
  Interval getInterval(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Interval}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INTERVAL</code> type.
   */
  Interval getInterval(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Value}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   */
  Value getValue(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Value}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   */
  Value getValue(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Map}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>MAP</code> type.
   */
  Map<String, Object> getMap(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Map}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>MAP</code> type.
   */
  Map<String, Object> getMap(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link List}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>ARRAY</code> type.
   */
  List<Object> getList(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link List}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the record. Modifying the returned object does not alter the
   * content of the record.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>ARRAY</code> type.
   */
  List<Object> getList(FieldPath fieldPath);

  /**
   * @return A new {@link RecordReader} over the current <code>Record</code>.
   */
  RecordReader asReader();

  /**
   * @return A new {@link RecordReader} over the node specified by the
   *         fieldPath or <code>null</code> if the node does not exist.
   */
  RecordReader asReader(String fieldPath);

  /**
   * @return A new {@link RecordReader} over the node specified by the
   *         fieldPath or <code>null</code> if the node does not exist.
   */
  RecordReader asReader(FieldPath fieldPath);

}
