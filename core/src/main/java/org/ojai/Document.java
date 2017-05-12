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
import org.ojai.annotation.API.NonNullable;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.TypeException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

@API.Public
public interface Document extends Iterable<Map.Entry<String, Value>>, JsonString {

  /**
   * Sets the the "_id" field of this Document to the specified Value.
   * @param _id Value to set as the value of "_id" field
   * @return {@code this} for chaining
   */
  public Document setId(@NonNullable Value _id);

  /**
   * @return The "_id" field of this Document
   */
  public Value getId();

  /**
   * Sets the the "_id" field of this Document to the specified string.
   * @param _id String to set as the value of the "_id" field
   * @return {@code this} for chaining
   */
  public Document setId(@NonNullable String _id);

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
  public Document setId(@NonNullable ByteBuffer _id);

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
  public <T> T toJavaBean(@NonNullable Class<T> beanClass) throws DecodingException;

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
  Document set(@NonNullable String fieldPath, @NonNullable String value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String.
   *
   * @param fieldPath the FieldPath to set
   * @param value the String value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable String value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the boolean value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, boolean value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the boolean value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, boolean value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, byte value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, byte value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the short value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, short value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the short value
   * @return {@code this} for chaining.
   */
  Document set(@NonNullable FieldPath fieldPath, short value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the int value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, int value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the int value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, int value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the long value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, long value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the long value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, long value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the float value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, float value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the float value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, float value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the double value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, double value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the double value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, double value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified BigDecimal.
   *
   * @param fieldPath the FieldPath to set
   * @param value the BigDecimal value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable BigDecimal value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified BigDecimal.
   *
   * @param fieldPath the FieldPath to set
   * @param value the BigDecimal value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable BigDecimal value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Time.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Time value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable OTime value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Time.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Time value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable OTime value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Date.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Date value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable ODate value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Date.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Date value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable ODate value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Timestamp.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Timestamp value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable OTimestamp value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Timestamp.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Timestamp value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable OTimestamp value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Interval.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Interval value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable OInterval value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Interval.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Interval value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable OInterval value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable byte[] value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining.
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable byte[] value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable byte[] value, int off, int len);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified binary value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the byte array containing the binary value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable byte[] value, int off, int len);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified ByteBuffer.
   *
   * @param fieldPath the FieldPath to set
   * @param value the ByteBuffer
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable ByteBuffer value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified ByteBuffer.
   *
   * @param fieldPath the FieldPath to set
   * @param value the ByteBuffer
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable ByteBuffer value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Map.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Map value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable Map<String, ? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Map.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Map value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable Map<String, ? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Document. Recursive set such as set("a", doc) followed
   * by set("a.b", doc) is undefined.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Document
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable Document value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Document. Recursive set such as set("a", doc) followed
   * by set("a.b", doc) is undefined.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Document
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable Document value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable Value value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Value.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Value
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable Value value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object List.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Object List
   * @return {@code this} for chaining
   */
  Document set(@NonNullable String fieldPath, @NonNullable List<? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object List.
   *
   * @param fieldPath the FieldPath to set
   * @param value the Object List
   * @return {@code this} for chaining
   */
  Document set(@NonNullable FieldPath fieldPath, @NonNullable List<? extends Object> value);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the boolean array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable String fieldPath, @NonNullable boolean[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified boolean array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the boolean array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable FieldPath fieldPath, @NonNullable boolean[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the byte array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable String fieldPath, @NonNullable byte[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified byte array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the byte array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable FieldPath fieldPath, @NonNullable byte[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the short array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable String fieldPath, @NonNullable short[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified short array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the short array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable FieldPath fieldPath, @NonNullable short[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the int array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable String fieldPath, @NonNullable int[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified int array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the int array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable FieldPath fieldPath, @NonNullable int[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the long array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable String fieldPath, @NonNullable long[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified long array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the long array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable FieldPath fieldPath, @NonNullable long[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the float array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable String fieldPath, @NonNullable float[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified float array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the float array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable FieldPath fieldPath, @NonNullable float[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the double array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable String fieldPath, @NonNullable double[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified double array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the double array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable FieldPath fieldPath, @NonNullable double[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the String array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable String fieldPath, @NonNullable String[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified String array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the String array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable FieldPath fieldPath, @NonNullable String[] values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the Object array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable String fieldPath, @NonNullable Object... values);

  /**
   * Sets the value of the specified fieldPath in this Document to the
   * specified Object array.
   *
   * @param fieldPath the FieldPath to set
   * @param values the Object array
   * @return {@code this} for chaining
   */
  Document setArray(@NonNullable FieldPath fieldPath, @NonNullable Object... values);

  /**
   * Sets the value of the specified fieldPath in this Document to
   * {@link Type#NULL}.
   *
   * @param fieldPath the FieldPath to set
   * @return {@code this} for chaining
   */
  Document setNull(@NonNullable String fieldPath);

  /**
   * Sets the value of the specified fieldPath in this Document to
   * {@link Type#NULL}.
   *
   * @param fieldPath the FieldPath to set
   * @return {@code this} for chaining
   */
  Document setNull(@NonNullable FieldPath fieldPath);

  /**
   * Deletes the value at the specified {@code FieldPath} if it exists.
   * @param fieldPath The {@code fieldPath} to delete from the document
   * @return {@code this} for chaining
   */
  Document delete(@NonNullable String fieldPath);

  /**
   * Deletes the value at the specified {@code FieldPath} if it exists.
   * @param fieldPath the {@code fieldPath} to delete from the document
   * @return {@code this} for chaining
   */
  Document delete(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code String}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>STRING</code> type
   */
  String getString(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code String}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>STRING</code> type
   */
  String getString(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code boolean}.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  boolean getBoolean(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code boolean}.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  boolean getBoolean(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Boolean}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>BOOLEAN</code> type
   */
  Boolean getBooleanObj(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Boolean}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>BOOLEAN</code> type
   */
  Boolean getBooleanObj(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code byte}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  byte getByte(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code byte}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  byte getByte(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Byte}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Byte getByteObj(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Byte}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Byte getByteObj(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code short}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  short getShort(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code short}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  short getShort(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Short}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Short getShortObj(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Short}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Short getShortObj(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@code int}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  int getInt(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@code int}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  int getInt(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@code Integer}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Integer getIntObj(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@code Integer}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Integer getIntObj(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code long}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  long getLong(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code long}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  long getLong(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Long}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Long getLongObj(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Long}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Long getLongObj(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code float}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  float getFloat(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code float}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  float getFloat(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Float}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Float getFloatObj(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Float}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Float getFloatObj(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code double}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  double getDouble(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code double}.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   * @throws NoSuchElementException if the specified field does not
   *         exist in the {@code Document}
   */
  double getDouble(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Double}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Double getDoubleObj(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@code Double}
   * object or {@code null} if the specified {@code FieldPath} does
   * not exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  Double getDoubleObj(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  BigDecimal getDecimal(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  BigDecimal getDecimal(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link OTime}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIME</code> type
   */
  OTime getTime(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link OTime}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIME</code> type
   */
  OTime getTime(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ODate}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>DATE</code> type
   */
  ODate getDate(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ODate}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>DATE</code> type
   */
  ODate getDate(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link OTimestamp}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIMESTAMP</code> type
   */
  OTimestamp getTimestamp(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link OTimestamp}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIMESTAMP</code> type
   */
  OTimestamp getTimestamp(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>BINARY</code> type
   */
  ByteBuffer getBinary(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BINARY</code> type
   */
  ByteBuffer getBinary(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@link OInterval}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>INTERVAL</code> type
   */
  OInterval getInterval(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as an {@link OInterval}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>INTERVAL</code> type
   */
  OInterval getInterval(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Value}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   */
  Value getValue(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Value}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   */
  Value getValue(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Map}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>MAP</code> type
   */
  Map<String, Object> getMap(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link Map}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>MAP</code> type
   */
  Map<String, Object> getMap(@NonNullable FieldPath fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link List}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>ARRAY</code> type
   */
  List<Object> getList(@NonNullable String fieldPath);

  /**
   * Returns the value at the specified fieldPath as a {@link List}
   * object or {@code null} if the specified {@code FieldPath} does not
   * exist in the document. Modifying the returned object does not alter the
   * content of the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>ARRAY</code> type
   */
  List<Object> getList(@NonNullable FieldPath fieldPath);

  /**
   * @return This Document serialized as Json string using the default options.
   */
  @Override
  String toString();

  /**
   * @return A new {@link DocumentReader} over the current <code>document</code>
   */
  DocumentReader asReader();

  /**
   * @return A new {@link DocumentReader} over the node specified by the
   *         fieldPath or <code>null</code> if the node does not exist
   */
  DocumentReader asReader(@NonNullable String fieldPath);

  /**
   * @return A new {@link DocumentReader} over the node specified by the
   *         fieldPath or <code>null</code> if the node does not exist
   */
  DocumentReader asReader(@NonNullable FieldPath fieldPath);

  /**
   * @return A new {@link Map} representing the Document.
   */
  Map<String, Object> asMap();

}
