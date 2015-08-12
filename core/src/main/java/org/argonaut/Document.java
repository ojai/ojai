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
package org.argonaut;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.argonaut.annotation.API;
import org.argonaut.exceptions.TypeException;
import org.argonaut.types.Interval;

@API.Public
public interface Document extends Iterable<Map.Entry<String, Value>> {

  /**
   * @return the number of top level entries in the document.
   */
  int size();

  /**
   * Removes all of the entries from this document.
   */
  Document empty();

  Document set(String fieldPath, String value);

  Document set(FieldPath fieldPath, String value);

  Document set(String fieldPath, boolean value);

  Document set(FieldPath fieldPath, boolean value);

  Document set(String fieldPath, byte value);

  Document set(FieldPath fieldPath, byte value);

  Document set(String fieldPath, short value);

  Document set(FieldPath fieldPath, short value);

  Document set(String fieldPath, int value);

  Document set(FieldPath fieldPath, int value);

  Document set(String fieldPath, long value);

  Document set(FieldPath fieldPath, long value);

  Document set(String fieldPath, float value);

  Document set(FieldPath fieldPath, float value);

  Document set(String fieldPath, double value);

  Document set(FieldPath fieldPath, double value);

  Document set(String fieldPath, BigDecimal value);

  Document set(FieldPath fieldPath, BigDecimal value);

  Document set(String fieldPath, Time value);

  Document set(FieldPath fieldPath, Time value);

  Document set(String fieldPath, Date value);

  Document set(FieldPath fieldPath, Date value);

  Document set(String fieldPath, Timestamp value);

  Document set(FieldPath fieldPath, Timestamp value);

  Document set(String fieldPath, Interval value);

  Document set(FieldPath fieldPath, Interval value);

  Document set(String fieldPath, byte[] value);

  Document set(FieldPath fieldPath, byte[] value);

  Document set(String fieldPath, byte[] value, int off, int len);

  Document set(FieldPath fieldPath, byte[] value, int off, int len);

  Document set(String fieldPath, ByteBuffer value);

  Document set(FieldPath fieldPath, ByteBuffer value);

  Document set(String fieldPath, Map<String, ? extends Object> value);

  Document set(FieldPath fieldPath, Map<String, ? extends Object> value);

  Document set(String fieldPath, Document value);

  Document set(FieldPath fieldPath, Document value);

  Document set(String fieldPath, Value value);

  Document set(FieldPath fieldPath, Value value);

  Document set(String fieldPath, List<? extends Object> value);

  Document set(FieldPath fieldPath, List<? extends Object> value);

  Document setArray(String fieldPath, boolean[] values);

  Document setArray(FieldPath fieldPath, boolean[] values);

  Document setArray(String fieldPath, byte[] values);

  Document setArray(FieldPath fieldPath, byte[] values);

  Document setArray(String fieldPath, short[] values);

  Document setArray(FieldPath fieldPath, short[] values);

  Document setArray(String fieldPath, int[] values);

  Document setArray(FieldPath fieldPath, int[] values);

  Document setArray(String fieldPath, long[] values);

  Document setArray(FieldPath fieldPath, long[] values);

  Document setArray(String fieldPath, float[] values);

  Document setArray(FieldPath fieldPath, float[] values);

  Document setArray(String fieldPath, double[] values);

  Document setArray(FieldPath fieldPath, double[] values);

  Document setArray(String fieldPath, String[] values);

  Document setArray(FieldPath fieldPath, String[] values);

  Document setArray(String fieldPath, Object... values);

  Document setArray(FieldPath fieldPath, Object... values);

  Document setNull(String fieldPath);

  Document setNull(FieldPath fieldPath);

  /**
   * Deletes the value at the specified {@code FieldPath} if exists.
   * @param fieldPath The {@code fieldPath} to delete from the document.
   * @return {@code this} for chaining.
   */
  Document delete(String fieldPath);

  /**
   * Deletes the value at the specified {@code FieldPath} if exists.
   * @param fieldPath The {@code fieldPath} to delete from the document.
   * @return {@code this} for chaining.
   */
  Document delete(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code String}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>STRING</code> type.
   */
  String getString(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code String}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
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
   *         exist in the {@code Document}.
   */
  boolean getBoolean(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code boolean}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Document}.
   */
  boolean getBoolean(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Boolean}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type.
   */
  Boolean getBooleanObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Boolean}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
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
   *         exist in the {@code Document}.
   */
  byte getByte(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code byte}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BYTE</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Document}.
   */
  byte getByte(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Byte}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BYTE</code> type.
   */
  Byte getByteObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Byte}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
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
   *         exist in the {@code Document}.
   */
  short getShort(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code short}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>SHORT</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Document}.
   */
  short getShort(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Short}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>SHORT</code> type.
   */
  Short getShortObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Short}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
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
   *         exist in the {@code Document}.
   */
  int getInt(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code int}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INT</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Document}.
   */
  int getInt(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Integer}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INT</code> type.
   */
  Integer getIntObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Integer}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
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
   *         exist in the {@code Document}.
   */
  long getLong(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code long}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>LONG</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Document}.
   */
  long getLong(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Long}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>LONG</code> type.
   */
  Long getLongObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Long}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
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
   *         exist in the {@code Document}.
   */
  float getFloat(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code float}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>FLOAT</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Document}.
   */
  float getFloat(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Float}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>FLOAT</code> type.
   */
  Float getFloatObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Float}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
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
   *         exist in the {@code Document}.
   */
  double getDouble(String fieldPath);


  /**
   * Returns the value at the specified fieldPath as a {@code double}.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DOUBLE</code> type.
   * @throws NoSuchElementException If the specified field does not
   *         exist in the {@code Document}.
   */
  double getDouble(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Double}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DOUBLE</code> type.
   */
  Double getDoubleObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Double}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DOUBLE</code> type.
   */
  Double getDoubleObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DECIMAL</code> type.
   */
  BigDecimal getDecimal(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DECIMAL</code> type.
   */
  BigDecimal getDecimal(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Time}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>TIME</code> type.
   */
  Time getTime(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Time}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>TIME</code> type.
   */
  Time getTime(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Date}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DATE</code> type.
   */
  Date getDate(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Date}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>DATE</code> type.
   */
  Date getDate(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Timestamp}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>TIMESTAMP</code> type.
   */
  Timestamp getTimestamp(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link java.sql.Timestamp}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>TIMESTAMP</code> type.
   */
  Timestamp getTimestamp(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BINARY</code> type.
   */
  ByteBuffer getBinary(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>BINARY</code> type.
   */
  ByteBuffer getBinary(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Interval}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INTERVAL</code> type.
   */
  Interval getInterval(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Interval}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>INTERVAL</code> type.
   */
  Interval getInterval(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Value}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   */
  Value getValue(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Value}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   */
  Value getValue(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Map}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>MAP</code> type.
   */
  Map<String, Object> getMap(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Map}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>MAP</code> type.
   */
  Map<String, Object> getMap(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link List}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>ARRAY</code> type.
   */
  List<Object> getList(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link List}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException If the value at the fieldPath is not of
   *         <code>ARRAY</code> type.
   */
  List<Object> getList(FieldPath fieldPath);

  /**
   * @return A new {@link documentReader} over the current <code>document</code>.
   */
  DocumentReader asReader();

  /**
   * @return A new {@link DocumentReader} over the node specified by the
   *         fieldPath or <code>null</code> if the node does not exist.
   */
  DocumentReader asReader(String fieldPath);

  /**
   * @return A new {@link DocumentReader} over the node specified by the
   *         fieldPath or <code>null</code> if the node does not exist.
   */
  DocumentReader asReader(FieldPath fieldPath);

}
