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
package org.ojai;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.TypeException;
import org.ojai.json.JsonOptions;
import org.ojai.types.Interval;

@API.Public
public interface Document extends Iterable<Map.Entry<String, Value>> {

  /**
   * @return the number of top level entries in the document.
   */
  int size();

  /**
   * Convert this Document to an instance of the specified class.
   *
   * @param beanClass the class of instance
   * @return an instance of the specified class converted from this Document
   */
  public <T> T toJavaBean(Class<T> beanClass) throws DecodingException;

  /**
   * Removes all of the entries from this document.
   */
  Document empty();

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String.
   *
   * @param fieldPath the FieldPath to set
   * @param value the String value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, String value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String.
   *
   * @param fieldPath the FieldPath to set
   * @param value the String value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, String value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the boolean value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, boolean value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the boolean value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, boolean value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, byte value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, byte value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the short value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, short value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the short value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, short value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the int value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, int value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the int value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, int value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the long value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, long value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the long value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, long value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the float value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, float value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the float value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, float value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the double value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, double value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the double value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, double value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified BigDecimal.
   *
   * @param fieldPath the FieldPath to set
   * @param value the BigDecimal value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, BigDecimal value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified BigDecimal.
   *
   * @param fieldPath the FieldPath to set
   * @param value the BigDecimal value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, BigDecimal value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Time.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Time value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, Time value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Time.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Time value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, Time value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Date.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Date value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, Date value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Date.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Date value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, Date value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Timestamp.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Timestamp value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, Timestamp value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Timestamp.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Timestamp value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, Timestamp value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Interval.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Interval value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, Interval value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Interval.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Interval value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, Interval value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, byte[] value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, byte[] value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, byte[] value, int off, int len);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, byte[] value, int off, int len);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified ByteBuffer.
   *
   * @param fieldPath the FieldPath to set
   * @param value the ByteBuffer
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, ByteBuffer value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified ByteBuffer.
   *
   * @param fieldPath the FieldPath to set
   * @param value the ByteBuffer
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, ByteBuffer value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Map.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Map value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, Map<String, ? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Map.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Map value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, Map<String, ? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Document.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Document
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, Document value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Document.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Document
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, Document value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Value
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, Value value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Value
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, Value value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object List.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Object List
   * @return {@code this} for chaining.
   */
  Document set(String fieldPath, List<? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object List.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Object List
   * @return {@code this} for chaining.
   */
  Document set(FieldPath fieldPath, List<? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the boolean array
   * @return {@code this} for chaining.
   */
  Document setArray(String fieldPath, boolean[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the boolean array
   * @return {@code this} for chaining.
   */
  Document setArray(FieldPath fieldPath, boolean[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array
   * @return {@code this} for chaining.
   */
  Document setArray(String fieldPath, byte[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array
   * @return {@code this} for chaining.
   */
  Document setArray(FieldPath fieldPath, byte[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the short array
   * @return {@code this} for chaining.
   */
  Document setArray(String fieldPath, short[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the short array
   * @return {@code this} for chaining.
   */
  Document setArray(FieldPath fieldPath, short[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the int array
   * @return {@code this} for chaining.
   */
  Document setArray(String fieldPath, int[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the int array
   * @return {@code this} for chaining.
   */
  Document setArray(FieldPath fieldPath, int[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the long array
   * @return {@code this} for chaining.
   */
  Document setArray(String fieldPath, long[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the long array
   * @return {@code this} for chaining.
   */
  Document setArray(FieldPath fieldPath, long[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the float array
   * @return {@code this} for chaining.
   */
  Document setArray(String fieldPath, float[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the float array
   * @return {@code this} for chaining.
   */
  Document setArray(FieldPath fieldPath, float[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the double array
   * @return {@code this} for chaining.
   */
  Document setArray(String fieldPath, double[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the double array
   * @return {@code this} for chaining.
   */
  Document setArray(FieldPath fieldPath, double[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the String array
   * @return {@code this} for chaining.
   */
  Document setArray(String fieldPath, String[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the String array
   * @return {@code this} for chaining.
   */
  Document setArray(FieldPath fieldPath, String[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Object array
   * @return {@code this} for chaining.
   */
  Document setArray(String fieldPath, Object... values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object array.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Object array
   * @return {@code this} for chaining.
   */
  Document setArray(FieldPath fieldPath, Object... values);

  /**
   * Sets the value of the specified fieldPath in this Document to
   * {@link Type#NULL}.
   *
   * @param fieldPath the FieldPath to set
   * @return {@code this} for chaining.
   */
  Document setNull(String fieldPath);

  /**
   * Sets the value of the specified fieldPath in this Document to
   * {@link Type#NULL}.
   *
   * @param fieldPath the FieldPath to set
   * @return {@code this} for chaining.
   */
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
   * @return this Document serialized as Json string using the default options.
   */
  String asJsonString();

  /**
   * @return this Document serialized as Json string using the specified options.
   */
  String asJsonString(JsonOptions options);

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
