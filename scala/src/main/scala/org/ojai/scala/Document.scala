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
package org.ojai.scala;

import java.util.NoSuchElementException;

import java.nio.ByteBuffer
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.TypeException;
import org.ojai.json.JsonOptions;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.FieldPath
import org.ojai.Value
import org.ojai.DocumentReader

trait Document extends Iterable[Tuple2[String, Value]] {

  /**
    * Sets the the "_id" field of this Document to the specified Value.
    * @param _id Value to set as the value of "_id" field
    * @return {@code this} for chaining
    */
  def setId(id: Value): Document;

  /**
    * @return The "_id" field of this Document
    */
  def getId(): Value;

  /**
    * Sets the the "_id" field of this Document to the specified string.
    * @param _id String to set as the value of the "_id" field
    * @return {@code this} for chaining
    */
  def setId(_id: String): Document;

  /**
    * @return the String _id of this document
    * @throws TypeException if the _id of this Document is not of the String type
    */
  def getIdString(): String;

  /**
    * Sets the the "_id" field of this Document to the specified string.
    * @param _id ByteBuffer to set as the value of "_id" field
    * @return {@code this} for chaining
    */
  def setId(_id: ByteBuffer): Document;

  /**
    * @return the binary _id of this document
    * @throws TypeException if the _id of this Document is not of the BINARY type
    */
  def getIdBinary(): ByteBuffer;

  /**
    * Returns {@code true} if this Document does not support any write
    * operations like set/delete etc.
    */
  def isReadOnly(): Boolean;

  /**
    * @return The number of top level entries in the document.
    */
  def size(): Int;

  /**
    * Converts this Document to an instance of the specified class.
    *
    * @param beanClass the class of instance
    * @return An instance of the specified class converted from this Document
    */
  @throws(classOf[DecodingException])
  def toJavaBean[T](beanClass: Class[T]) : T ;

  /**
    * Removes all of the entries from this document.
    */
  def empty(): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified String.
    *
    * @param fieldPath the FieldPath to set
    * @param value the String value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: String): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified String.
    *
    * @param fieldPath the FieldPath to set
    * @param value the String value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: String): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified boolean value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the boolean value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Boolean): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified boolean value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the boolean value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Boolean): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified byte value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the byte value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Byte): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified byte value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the byte value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Byte): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified short value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the short value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Short): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified short value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the short value
    * @return {@code this} for chaining.
    */
  def set(fieldPath: FieldPath, value: Short): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified int value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the int value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Integer): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified int value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the int value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Integer): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified long value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the long value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Long): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified long value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the long value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Long): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified float value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the float value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Float): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified float value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the float value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Float): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified double value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the double value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Double): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified double value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the double value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Double): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified BigDecimal.
    *
    * @param fieldPath the FieldPath to set
    * @param value the BigDecimal value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: BigDecimal): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified BigDecimal.
    *
    * @param fieldPath the FieldPath to set
    * @param value the BigDecimal value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: BigDecimal): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Time.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Time value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: OTime): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Time.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Time value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: OTime): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Date.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Date value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: ODate): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Date.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Date value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: ODate): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Timestamp.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Timestamp value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: OTimestamp): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Timestamp.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Timestamp value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: OTimestamp): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Interval.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Interval value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: OInterval): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Interval.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Interval value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: OInterval): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified binary value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the byte array containing the binary value
    * @return {@code this} for chaining
    */
  //  def set(fieldPath: String, value: Seq[Byte]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified binary value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the byte array containing the binary value
    * @return {@code this} for chaining.
    */
  //  def set(fieldPath: FieldPath, value: Seq[Byte]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified binary value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the byte array containing the binary value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Seq[Byte],off: Integer,len: Integer): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified binary value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the byte array containing the binary value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Seq[Byte], off:  Integer, len: Integer): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified ByteBuffer.
    *
    * @param fieldPath the FieldPath to set
    * @param value the ByteBuffer
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: ByteBuffer): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified ByteBuffer.
    *
    * @param fieldPath the FieldPath to set
    * @param value the ByteBuffer
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value:ByteBuffer): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Map.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Map value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Map[String, _ <: AnyRef]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Map.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Map value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Map[String, _<: AnyRef]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Document. Recursive set such as set("a", doc) followed
    * by set("a.b", doc) is undefined.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Document
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Document): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Document. Recursive set such as set("a", doc) followed
    * by set("a.b", doc) is undefined.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Document
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Document): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Value
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Value): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Value.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Value
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Value): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Object List.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Object List
    * @return {@code this} for chaining
    */
  def set(fieldPath: String, value: Seq[_ <: AnyRef]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Object List.
    *
    * @param fieldPath the FieldPath to set
    * @param value the Object List
    * @return {@code this} for chaining
    */
  def set(fieldPath: FieldPath, value: Seq[_ <: AnyRef]): Document;
  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified boolean array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the boolean array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: String, values: Array[Boolean]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified boolean array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the boolean array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: FieldPath, values: Array[Boolean]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified byte array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the byte array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: String, values: Array[Byte]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified byte array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the byte array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: FieldPath, values: Array[Byte]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified short array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the short array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: String, values: Array[Short]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified short array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the short array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: FieldPath, values: Array[Short]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified int array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the int array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: String, values: Array[Int]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified int array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the int array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: FieldPath, values: Array[Int]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified long array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the long array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: String, values:Array[Long]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified long array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the long array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: FieldPath, values: Array[Long]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified float array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the float array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: String, values: Array[Float]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified float array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the float array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: FieldPath, values: Array[Float]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified double array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the double array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: String, values: Array[Double]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified double array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the double array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: FieldPath, values: Array[Double]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified String array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the String array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: String, values: Array[String]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified String array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the String array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: FieldPath, values: Array[String]): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Object array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the Object array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: String, values: AnyRef*): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to the
    * specified Object array.
    *
    * @param fieldPath the FieldPath to set
    * @param values the Object array
    * @return {@code this} for chaining
    */
  def setArray(fieldPath: FieldPath, values: AnyRef*): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to
    * {@link Type#NULL}.
    *
    * @param fieldPath the FieldPath to set
    * @return {@code this} for chaining
    */
  def setNull(fieldPath: String): Document;

  /**
    * Sets the value of the specified fieldPath in this Document to
    * {@link Type#NULL}.
    *
    * @param fieldPath the FieldPath to set
    * @return {@code this} for chaining
    */
  def setNull(fieldPath: FieldPath): Document;

  /**
    * Deletes the value at the specified {@code FieldPath} if it exists.
    * @param fieldPath The {@code fieldPath} to delete from the document
    * @return {@code this} for chaining
    */
  def delete(fieldPath: String): Document;

  /**
    * Deletes the value at the specified {@code FieldPath} if it exists.
    * @param fieldPath the {@code fieldPath} to delete from the document
    * @return {@code this} for chaining
    */
  def delete(fieldPath: FieldPath): Document;

  /**
    * Returns the value at the specified fieldPath as a {@code String}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>STRING</code> type
    */
  def getString(fieldPath: String): String;

  /**
    * Returns the value at the specified fieldPath as a {@code String}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>STRING</code> type
    */
  def getString(fieldPath: FieldPath): String;

  /**
    * Returns the value at the specified fieldPath as a {@code boolean}.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         <code>BOOLEAN</code> type
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getBoolean(fieldPath: String): Boolean;

  /**
    * Returns the value at the specified fieldPath as a {@code boolean}.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         <code>BOOLEAN</code> type
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getBoolean(fieldPath: FieldPath): Boolean;

  /**
    * Returns the value at the specified fieldPath as a {@code Boolean}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>BOOLEAN</code> type
    */
  def getBooleanObj(fieldPath: String): java.lang.Boolean;

  /**
    * Returns the value at the specified fieldPath as a {@code Boolean}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>BOOLEAN</code> type
    */
  def getBooleanObj(fieldPath: FieldPath): java.lang.Boolean;

  /**
    * Returns the value at the specified fieldPath as a {@code byte}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getByte(fieldPath: String): Byte;

  /**
    * Returns the value at the specified fieldPath as a {@code byte}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getByte(fieldPath: FieldPath): Byte;

  /**
    * Returns the value at the specified fieldPath as a {@code Byte}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getByteObj(fieldPath: String): java.lang.Byte;

  /**
    * Returns the value at the specified fieldPath as a {@code Byte}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getByteObj(fieldPath: FieldPath): java.lang.Byte;

  /**
    * Returns the value at the specified fieldPath as a {@code short}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getShort(fieldPath: String): Short;

  /**
    * Returns the value at the specified fieldPath as a {@code short}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getShort(fieldPath: FieldPath): Short;

  /**
    * Returns the value at the specified fieldPath as a {@code Short}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getShortObj(fieldPath: String): java.lang.Short;

  /**
    * Returns the value at the specified fieldPath as a {@code Short}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getShortObj(fieldPath: FieldPath): java.lang.Short;

  /**
    * Returns the value at the specified fieldPath as an {@code int}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getInt(fieldPath: String): Int;

  /**
    * Returns the value at the specified fieldPath as an {@code int}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getInt(fieldPath: FieldPath): Int;

  /**
    * Returns the value at the specified fieldPath as an {@code Integer}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getIntObj(fieldPath: String): Integer;

  /**
    * Returns the value at the specified fieldPath as an {@code Integer}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getIntObj(fieldPath: FieldPath): Integer;

  /**
    * Returns the value at the specified fieldPath as a {@code long}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getLong(fieldPath: String): Long;

  /**
    * Returns the value at the specified fieldPath as a {@code long}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getLong(fieldPath: FieldPath): Long;

  /**
    * Returns the value at the specified fieldPath as a {@code Long}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getLongObj(fieldPath: String):java.lang.Long;

  /**
    * Returns the value at the specified fieldPath as a {@code Long}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getLongObj(fieldPath: FieldPath): java.lang.Long;

  /**
    * Returns the value at the specified fieldPath as a {@code float}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getFloat(fieldPath: String): Float;

  /**
    * Returns the value at the specified fieldPath as a {@code float}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getFloat(fieldPath: FieldPath): Float;

  /**
    * Returns the value at the specified fieldPath as a {@code Float}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getFloatObj(fieldPath: String): java.lang.Float;

  /**
    * Returns the value at the specified fieldPath as a {@code Float}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getFloatObj(fieldPath: FieldPath): java.lang.Float;

  /**
    * Returns the value at the specified fieldPath as a {@code double}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getDouble(fieldPath: String): Double;

  /**
    * Returns the value at the specified fieldPath as a {@code double}.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    * @throws NoSuchElementException if the specified field does not
    *         exist in the {@code Document}
    */
  def getDouble(fieldPath: FieldPath): Double;

  /**
    * Returns the value at the specified fieldPath as a {@code Double}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getDoubleObj(fieldPath: String): java.lang.Double;

  /**
    * Returns the value at the specified fieldPath as a {@code Double}
    * object or {@code null} if the specified {@code FieldPath} does
    * not exist in the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getDoubleObj(fieldPath: FieldPath): java.lang.Double;

  /**
    * Returns the value at the specified fieldPath as a {@link BigDecimal}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getDecimal(fieldPath: String): BigDecimal;

  /**
    * Returns the value at the specified fieldPath as a {@link BigDecimal}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not one of
    *         the numeric types
    */
  def getDecimal(fieldPath: FieldPath): BigDecimal;

  /**
    * Returns the value at the specified fieldPath as a {@link OTime}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>TIME</code> type
    */
  def getTime(fieldPath: String): OTime;

  /**
    * Returns the value at the specified fieldPath as a {@link OTime}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>TIME</code> type
    */
  def getTime(fieldPath: FieldPath): OTime;

  /**
    * Returns the value at the specified fieldPath as a {@link ODate}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>DATE</code> type
    */
  def getDate(fieldPath: String): ODate;

  /**
    * Returns the value at the specified fieldPath as a {@link ODate}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>DATE</code> type
    */
  def getDate(fieldPath: FieldPath): ODate;

  /**
    * Returns the value at the specified fieldPath as a {@link OTimestamp}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>TIMESTAMP</code> type
    */
  def getTimestamp(fieldPath: String): OTimestamp;

  /**
    * Returns the value at the specified fieldPath as a {@link OTimestamp}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>TIMESTAMP</code> type
    */
  def getTimestamp(fieldPath: FieldPath): OTimestamp;

  /**
    * Returns the value at the specified fieldPath as a {@link ByteBuffer}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>BINARY</code> type
    */
  def getBinary(fieldPath: String): ByteBuffer;

  /**
    * Returns the value at the specified fieldPath as a {@link ByteBuffer}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         <code>BINARY</code> type
    */
  def getBinary(fieldPath: FieldPath): ByteBuffer;

  /**
    * Returns the value at the specified fieldPath as an {@link OInterval}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>INTERVAL</code> type
    */
  def getInterval(fieldPath: String): OInterval;

  /**
    * Returns the value at the specified fieldPath as an {@link OInterval}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>INTERVAL</code> type
    */
  def getInterval(fieldPath: FieldPath): OInterval;

  /**
    * Returns the value at the specified fieldPath as a {@link Value}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    */
  def getValue(fieldPath: String): Value;

  /**
    * Returns the value at the specified fieldPath as a {@link Value}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    */
  def getValue(fieldPath: FieldPath): Value;

  /**
    * Returns the value at the specified fieldPath as a {@link Map}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>MAP</code> type
    */
  def getMap(fieldPath: String): Map[String, AnyRef] ;

  /**
    * Returns the value at the specified fieldPath as a {@link Map}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>MAP</code> type
    */
  def getMap(fieldPath: FieldPath): Map[String, AnyRef] ;

  /**
    * Returns the value at the specified fieldPath as a {@link List}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>ARRAY</code> type
    */
  def getList(fieldPath: String): Seq[AnyRef];

  /**
    * Returns the value at the specified fieldPath as a {@link List}
    * object or {@code null} if the specified {@code FieldPath} does not
    * exist in the document. Modifying the returned object does not alter the
    * content of the document.
    *
    * @throws TypeException if the value at the fieldPath is not of
    *         the <code>ARRAY</code> type
    */
  def getList(fieldPath: FieldPath): Seq[AnyRef];

  /**
    * @return This Document serialized as Json string using the default options.
    */
  def asJsonString(): String;

  /**
    * @return This Document serialized as Json string using the specified options
    */
  def asJsonString(options: JsonOptions): String;

  /**
    * @return A new {@link DocumentReader} over the current <code>document</code>
    */
  def asReader(): DocumentReader;

  /**
    * @return A new {@link DocumentReader} over the node specified by the
    *         fieldPath or <code>null</code> if the node does not exist
    */
  def asReader(fieldPath: String): DocumentReader;

  /**
    * @return A new {@link DocumentReader} over the node specified by the
    *         fieldPath or <code>null</code> if the node does not exist
    */
  def asReader(fieldPath: FieldPath): DocumentReader;

  /**
    * @return A new {@link Map} representing the Document.
    */
  def asMap(): Map[String, AnyRef] ;

}
