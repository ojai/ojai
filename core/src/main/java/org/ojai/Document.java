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
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.TypeException;
import org.ojai.json.JsonOptions;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

@API.Public
public interface Document extends Iterable<Map.Entry<String, Value>> {

  /**
   * Sets the the "_id" field of this Document to the specified Value.
   * @param _id Value to set as the value of "_id" field
   * @return {@code this} for chaining
   */
  public Document setId(Value _id);

  /**
   * @return The "_id" field of this Document
   */
  public Value getId();

  /**
   * Sets the the "_id" field of this Document to the specified string.
   * @param _id String to set as the value of the "_id" field
   * @return {@code this} for chaining
   */
  public Document setId(String _id);

  /**
   * @return the String _id of this document
   * @throws TypeException if the _id of this Document is not of the String type
   */
  public String getIdString();

  /**
   * Sets the the "_id" field of this Document to the specified string.
   * @param _id ByteBuffer to set as the value of "_id" field
   * @return {@code this} for chaining
   */
  public Document setId(ByteBuffer _id);

  /**
   * @return the binary _id of this document
   * @throws TypeException if the _id of this Document is not of the BINARY type
   */
  public ByteBuffer getIdBinary();

  /**
   * Returns {@code true} if this Document does not support any write
   * operations like set/delete etc.
   */
  public boolean isReadOnly();

  /**
   * @return The number of top level entries in the document.
   */
  int size();

  /**
   * Converts this Document to an instance of the specified class.
   *
   * @param beanClass the class of instance
   * @return An instance of the specified class converted from this Document
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
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, String value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String.
   *
   * @param fieldPath the FieldPath to set
   * @param value the String value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, String value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the boolean value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, boolean value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the boolean value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, boolean value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, byte value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, byte value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the short value
   * @return {@code this} for chaining
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
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, int value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the int value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, int value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the long value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, long value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the long value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, long value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the float value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, float value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the float value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, float value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the double value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, double value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the double value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, double value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified BigDecimal.
   *
   * @param fieldPath the FieldPath to set
   * @param value the BigDecimal value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, BigDecimal value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified BigDecimal.
   *
   * @param fieldPath the FieldPath to set
   * @param value the BigDecimal value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, BigDecimal value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Time.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Time value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, OTime value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Time.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Time value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, OTime value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Date.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Date value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, ODate value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Date.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Date value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, ODate value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Timestamp.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Timestamp value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, OTimestamp value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Timestamp.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Timestamp value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, OTimestamp value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Interval.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Interval value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, OInterval value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Interval.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Interval value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, OInterval value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining
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
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, byte[] value, int off, int len);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, byte[] value, int off, int len);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified ByteBuffer.
   *
   * @param fieldPath the FieldPath to set
   * @param value the ByteBuffer
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, ByteBuffer value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified ByteBuffer.
   *
   * @param fieldPath the FieldPath to set
   * @param value the ByteBuffer
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, ByteBuffer value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Map.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Map value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, Map<String, ? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Map.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Map value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, Map<String, ? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Document. Recursive set such as set("a", doc) followed
   * by set("a.b", doc) is undefined.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Document
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, Document value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Document. Recursive set such as set("a", doc) followed
   * by set("a.b", doc) is undefined.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Document
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, Document value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Value
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, Value value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Value
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, Value value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object List.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Object List
   * @return {@code this} for chaining
   */
  Document set(String fieldPath, List<? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object List.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Object List
   * @return {@code this} for chaining
   */
  Document set(FieldPath fieldPath, List<? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the boolean array
   * @return {@code this} for chaining
   */
  Document setArray(String fieldPath, boolean[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the boolean array
   * @return {@code this} for chaining
   */
  Document setArray(FieldPath fieldPath, boolean[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the byte array
   * @return {@code this} for chaining
   */
  Document setArray(String fieldPath, byte[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the byte array
   * @return {@code this} for chaining
   */
  Document setArray(FieldPath fieldPath, byte[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the short array
   * @return {@code this} for chaining
   */
  Document setArray(String fieldPath, short[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the short array
   * @return {@code this} for chaining
   */
  Document setArray(FieldPath fieldPath, short[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the int array
   * @return {@code this} for chaining
   */
  Document setArray(String fieldPath, int[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the int array
   * @return {@code this} for chaining
   */
  Document setArray(FieldPath fieldPath, int[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the long array
   * @return {@code this} for chaining
   */
  Document setArray(String fieldPath, long[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the long array
   * @return {@code this} for chaining
   */
  Document setArray(FieldPath fieldPath, long[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the float array
   * @return {@code this} for chaining
   */
  Document setArray(String fieldPath, float[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the float array
   * @return {@code this} for chaining
   */
  Document setArray(FieldPath fieldPath, float[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the double array
   * @return {@code this} for chaining
   */
  Document setArray(String fieldPath, double[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the double array
   * @return {@code this} for chaining
   */
  Document setArray(FieldPath fieldPath, double[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the String array
   * @return {@code this} for chaining
   */
  Document setArray(String fieldPath, String[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the String array
   * @return {@code this} for chaining
   */
  Document setArray(FieldPath fieldPath, String[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the Object array
   * @return {@code this} for chaining
   */
  Document setArray(String fieldPath, Object... values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the Object array
   * @return {@code this} for chaining
   */
  Document setArray(FieldPath fieldPath, Object... values);

  /**
   * Sets the value of the specified fieldPath in this Document to
   * {@link Type#NULL}.
   *
   * @param fieldPath the FieldPath to set
   * @return {@code this} for chaining
   */
  Document setNull(String fieldPath);

  /**
   * Sets the value of the specified fieldPath in this Document to
   * {@link Type#NULL}.
   *
   * @param fieldPath the FieldPath to set
   * @return {@code this} for chaining
   */
  Document setNull(FieldPath fieldPath);

  /**
   * Deletes the value at the specified {@code FieldPath} if it exists.
   * @param fieldPath The {@code fieldPath} to delete from the document
   * @return {@code this} for chaining
   */
  Document delete(String fieldPath);

  /**
   * Deletes the value at the specified {@code FieldPath} if it exists.
   * @param fieldPath the {@code fieldPath} to delete from the document
   * @return {@code this} for chaining
   */
  Document delete(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code String}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>STRING</code> type
   */
  String getString(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code String}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>STRING</code> type
   */
  String getString(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code boolean}.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  boolean getBoolean(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code boolean}.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  boolean getBoolean(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Boolean}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>BOOLEAN</code> type
   */
  Boolean getBooleanObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Boolean}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>BOOLEAN</code> type
   */
  Boolean getBooleanObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code byte}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  byte getByte(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code byte}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  byte getByte(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Byte}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Byte getByteObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Byte}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Byte getByteObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code short}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  short getShort(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code short}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  short getShort(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Short}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Short getShortObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Short}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Short getShortObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@code int}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  int getInt(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@code int}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  int getInt(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@code Integer}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Integer getIntObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@code Integer}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Integer getIntObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code long}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  long getLong(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code long}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  long getLong(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Long}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Long getLongObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Long}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Long getLongObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code float}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  float getFloat(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code float}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  float getFloat(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Float}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Float getFloatObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Float}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Float getFloatObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code double}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  double getDouble(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code double}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  double getDouble(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Double}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Double getDoubleObj(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Double}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Double getDoubleObj(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  BigDecimal getDecimal(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  BigDecimal getDecimal(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link OTime}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIME</code> type
   */
  OTime getTime(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link OTime}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIME</code> type
   */
  OTime getTime(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ODate}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>DATE</code> type
   */
  ODate getDate(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ODate}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>DATE</code> type
   */
  ODate getDate(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link OTimestamp}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIMESTAMP</code> type
   */
  OTimestamp getTimestamp(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link OTimestamp}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIMESTAMP</code> type
   */
  OTimestamp getTimestamp(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>BINARY</code> type
   */
  ByteBuffer getBinary(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BINARY</code> type
   */
  ByteBuffer getBinary(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@link OInterval}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>INTERVAL</code> type
   */
  OInterval getInterval(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@link OInterval}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>INTERVAL</code> type
   */
  OInterval getInterval(FieldPath fieldPath);

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
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>MAP</code> type
   */
  Map<String, Object> getMap(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Map}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>MAP</code> type
   */
  Map<String, Object> getMap(FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link List}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>ARRAY</code> type
   */
  List<Object> getList(String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link List}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>ARRAY</code> type
   */
  List<Object> getList(FieldPath fieldPath);

  /**
   * @return This Document serialized as Json string using the default options.
   */
  @Override
  String toString();

  /**
   * @return This Document serialized as Json string using the default options.
   */
  String asJsonString();

  /**
   * @return This Document serialized as Json string using the specified options
   */
  String asJsonString(JsonOptions options);

  /**
   * @return A new {@link DocumentReader} over the current <code>document</code>
   */
  DocumentReader asReader();

  /**
   * @return A new {@link DocumentReader} over the node specified by the
   *         fieldPath or <code>null</code> if the node does not exist
   */
  DocumentReader asReader(String fieldPath);

  /**
   * @return A new {@link DocumentReader} over the node specified by the
   *         fieldPath or <code>null</code> if the node does not exist
   */
  DocumentReader asReader(FieldPath fieldPath);

  /**
   * @return A new {@link Map} representing the Document.
   */
  Map<String, Object> asMap();

}
