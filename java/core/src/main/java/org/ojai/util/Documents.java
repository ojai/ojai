/**
 * Copyright (c) 2016 MapR, Inc.
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
package org.ojai.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.DocumentReader;
import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.annotation.API.Nullable;
import org.ojai.exceptions.TypeException;
import org.ojai.json.impl.JsonUtils;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

import com.google.common.collect.Maps;

/**
 * This class contains utility methods for {@link Document} interface.
 */
@API.Public
public class Documents {

  /**
   * Compares two documents for equality.
   * @param d1 the first document to compare
   * @param d2 the second document to compare
   * @return {@code true} if both the documents are equal,
   *         {@code false} otherwise.
   */
  public static boolean equals(@Nullable Document d1, @Nullable Document d2) {
    if (d1 == d2) {
      return true; // both are null or same reference
    } else if (d1 == null || d2 == null
        || d1.size() != d2.size()) {
      return false;
    } else {
      Map<String, Value> keyValues = Maps.newTreeMap();
      Iterator<Entry<String, Value>> i = d2.iterator();
      while (i.hasNext()) {
        Entry<String, Value> e = i.next();
        keyValues.put(e.getKey(), e.getValue());
      }
      Iterator<Entry<String, Value>> j = d1.iterator();
      while (j.hasNext()) {
        Entry<String, Value> e = j.next();
        String k = e.getKey();
        Value v = keyValues.get(k);
        if (v == null || !e.getValue().equals(v)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * This method can be used to build a {@link Document} (via {@link DocumentBuilder}) from
   * a {@link DocumentReader} instance.
   *
   * @param reader instance of DocumentReader to read the fields from
   * @param builder instance of DocumentBuilder to write the field to
   */
  public static void writeReaderToBuilder(@NonNullable DocumentReader reader, @NonNullable DocumentBuilder builder) {
    JsonUtils.addToMap(reader, builder);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer} object or
   * the specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BINARY</code> type
   */
  public static ByteBuffer getBinary(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable ByteBuffer defaultValue) {
    ByteBuffer docValue = document.getBinary(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link ByteBuffer} object or
   * the specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BINARY</code> type
   */
  public static ByteBuffer getBinary(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable ByteBuffer defaultValue) {
    return getBinary(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link boolean} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type
   */
  public static boolean getBoolean(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable boolean defaultValue) {
    Boolean docValue = document.getBooleanObj(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link boolean} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         <code>BOOLEAN</code> type
   */
  public static boolean getBoolean(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable boolean defaultValue) {
    return getBoolean(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link byte} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static byte getByte(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable byte defaultValue) {
    Byte docValue = document.getByteObj(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link byte} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static byte getByte(@NonNullable Document document,
      @NonNullable String fieldPath, byte defaultValue) {
    return getByte(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link ODate} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>DATE</code> type
   */
  public static ODate getDate(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable ODate defaultValue) {
    ODate docValue = document.getDate(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link ODate} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>DATE</code> type
   */
  public static ODate getDate(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable ODate defaultValue) {
    return getDate(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static BigDecimal getDecimal(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable BigDecimal defaultValue) {
    BigDecimal docValue = document.getDecimal(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link BigDecimal} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static BigDecimal getDecimal(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable BigDecimal defaultValue) {
    return getDecimal(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link double} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static double getDouble(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, double defaultValue) {
    Double docValue = document.getDoubleObj(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link double} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static double getDouble(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable double defaultValue) {
    return getDouble(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link float} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static float getFloat(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, float defaultValue) {
    Float docValue = document.getFloatObj(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link float} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static float getFloat(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable float defaultValue) {
    return getFloat(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link int} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static int getInt(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, int defaultValue) {
    Integer docValue = document.getIntObj(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link int} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static int getInt(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable int defaultValue) {
    return getInt(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link OInterval} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>INTERVAL</code> type
   */
  public static OInterval getInterval(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable OInterval defaultValue) {
    OInterval docValue = document.getInterval(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link OInterval} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>INTERVAL</code> type
   */
  public static OInterval getInterval(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable OInterval defaultValue) {
    return getInterval(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link List} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>ARRAY</code> type
   */
  public static List<Object> getList(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable List<Object> defaultValue) {
    List<Object> docValue = document.getList(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link List} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>ARRAY</code> type
   */
  public static List<Object> getList(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable List<Object> defaultValue) {
    return getList(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link long} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static long getLong(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, long defaultValue) {
    Long docValue = document.getLongObj(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link long} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static long getLong(@NonNullable Document document,
      @NonNullable String fieldPath, long defaultValue) {
    return getLong(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link Map} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>MAP</code> type
   */
  public static Map<String, Object> getMap(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable Map<String, Object> defaultValue) {
    Map<String, Object> docValue = document.getMap(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link Map} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>MAP</code> type
   */
  public static Map<String, Object> getMap(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable Map<String, Object> defaultValue) {
    return getMap(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link short} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static short getShort(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, short defaultValue) {
    Short docValue = document.getShortObj(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link short} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not one of
   *         the numeric types
   */
  public static short getShort(@NonNullable Document document,
      @NonNullable String fieldPath, short defaultValue) {
    return getShort(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link String} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>STRING</code> type
   */
  public static String getString(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable String defaultValue) {
    String docValue = document.getString(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link String} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>STRING</code> type
   */
  public static String getString(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable String defaultValue) {
    return getString(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link OTime} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIME</code> type
   */
  public static OTime getTime(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable OTime defaultValue) {
    OTime docValue = document.getTime(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link OTime} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIME</code> type
   */
  public static OTime getTime(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable OTime defaultValue) {
    return getTime(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link OTimestamp} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIMESTAMP</code> type
   */
  public static OTimestamp getTimestamp(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable OTimestamp defaultValue) {
    OTimestamp docValue = document.getTimestamp(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link OTimestamp} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   *
   * @throws TypeException if the value at the fieldPath is not of
   *         the <code>TIMESTAMP</code> type
   */
  public static OTimestamp getTimestamp(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable OTimestamp defaultValue) {
    return getTimestamp(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

  /**
   * Returns the value at the specified fieldPath as a {@link Value} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   */
  public static Value getValue(@NonNullable Document document,
      @NonNullable FieldPath fieldPath, @Nullable Value defaultValue) {
    Value docValue = document.getValue(fieldPath);
    return docValue != null ? docValue : defaultValue;
  }

  /**
   * Returns the value at the specified fieldPath as a {@link Value} or the
   * specified {@code defaultValue} if the specified {@code FieldPath} does not
   * exist in the document.
   */
  public static Value getValue(@NonNullable Document document,
      @NonNullable String fieldPath, @Nullable Value defaultValue) {
    return getValue(document, FieldPath.parseFrom(fieldPath), defaultValue);
  }

}
