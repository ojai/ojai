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

import org.ojai.annotation.API;
import org.ojai.exceptions.TypeException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

@API.Public
public interface Value extends JsonString {

  public static final byte TYPE_CODE_NULL       = 1;
  public static final byte TYPE_CODE_BOOLEAN    = 2;
  public static final byte TYPE_CODE_STRING     = 3;
  public static final byte TYPE_CODE_BYTE       = 4;
  public static final byte TYPE_CODE_SHORT      = 5;
  public static final byte TYPE_CODE_INT        = 6;
  public static final byte TYPE_CODE_LONG       = 7;
  public static final byte TYPE_CODE_FLOAT      = 8;
  public static final byte TYPE_CODE_DOUBLE     = 9;
  public static final byte TYPE_CODE_DECIMAL    = 10;
  public static final byte TYPE_CODE_DATE       = 11;
  public static final byte TYPE_CODE_TIME       = 12;
  public static final byte TYPE_CODE_TIMESTAMP  = 13;
  public static final byte TYPE_CODE_INTERVAL   = 14;
  public static final byte TYPE_CODE_BINARY     = 15;
  public static final byte TYPE_CODE_MAP        = 16;
  public static final byte TYPE_CODE_ARRAY      = 17;

  public enum Type {
    /**
     * A non-existing value of unknown type and quantity.
     */
    NULL(TYPE_CODE_NULL),

    /**
     * A boolean value.
     */
    BOOLEAN(TYPE_CODE_BOOLEAN),

    /**
     * Character sequence.
     */
    STRING(TYPE_CODE_STRING),

    /**
     * 8-bit signed integer.
     */
    BYTE(TYPE_CODE_BYTE),

    /**
     * 16-bit signed integer.
     */
    SHORT(TYPE_CODE_SHORT),

    /**.
     * 32-bit signed integer.
     */
    INT(TYPE_CODE_INT),

    /**
     * 64-bit signed integer.
     */
    LONG(TYPE_CODE_LONG),

    /**
     * Single-precision 32-bit floating point number.
     */
    FLOAT(TYPE_CODE_FLOAT),

    /**
     * Double-precision 64-bit floating point number.
     */
    DOUBLE(TYPE_CODE_DOUBLE),

    /**
     * Arbitrary precision, fixed point decimal value.
     */
    DECIMAL(TYPE_CODE_DECIMAL),

    /**
     * 32-bit integer representing the number of DAYS since Unix epoch,
     * i.e. January 1, 1970 00:00:00 UTC. The value is absolute and
     * is time-zone independent. Negative values represents dates before
     * epoch.
     */
    DATE(TYPE_CODE_DATE),

    /**
     * 32-bit integer representing time of the day in milliseconds.
     * The value is absolute and is time-zone independent.
     */
    TIME(TYPE_CODE_TIME),

    /**
     * 64-bit integer representing the number of milliseconds since epoch,
     * i.e. January 1, 1970 00:00:00 UTC. Negative values represent dates
     * before epoch.
     */
    TIMESTAMP(TYPE_CODE_TIMESTAMP),

    /**
     * A value representing a period of time between two instants.
     */
    INTERVAL(TYPE_CODE_INTERVAL),

    /**
     * Uninterpreted sequence of bytes.
     */
    BINARY(TYPE_CODE_BINARY),

    /**
     * Mapping of String and <code>Value</code>.
     */
    MAP(TYPE_CODE_MAP),

    /**
     * A list of <code>Value</code>.
     */
    ARRAY(TYPE_CODE_ARRAY);

    private final byte code;

    private Type(int code) {
      this.code = (byte) code;
    }

    public byte getCode() {
      return code;
    }

    public boolean isScalar() {
      return this != MAP && this != ARRAY;
    }

    public static Type valueOf(int typeCode) {
      switch (typeCode) {
        case TYPE_CODE_NULL: return NULL;
        case TYPE_CODE_BOOLEAN: return BOOLEAN;
        case TYPE_CODE_STRING: return STRING;
        case TYPE_CODE_BYTE: return BYTE;
        case TYPE_CODE_SHORT: return SHORT;
        case TYPE_CODE_INT: return INT;
        case TYPE_CODE_LONG: return LONG;
        case TYPE_CODE_FLOAT: return FLOAT;
        case TYPE_CODE_DOUBLE: return DOUBLE;
        case TYPE_CODE_DECIMAL: return DECIMAL;
        case TYPE_CODE_DATE: return DATE;
        case TYPE_CODE_TIME: return TIME;
        case TYPE_CODE_TIMESTAMP: return TIMESTAMP;
        case TYPE_CODE_INTERVAL: return INTERVAL;
        case TYPE_CODE_BINARY: return BINARY;
        case TYPE_CODE_MAP: return MAP;
        case TYPE_CODE_ARRAY: return ARRAY;
        default: return null;
      }
    }

  }

  /**
   * @return The <code>Type</code> of this Value.
   */
  Value.Type getType();

  /**
   * Returns the value as a <code>byte</code>.
   *
   * @throws TypeException if this value is not one of the numeric types.
   */
  byte getByte();

  /**
   * Returns the value as a <code>short</code>.
   *
   * @throws TypeException if this value is not one of the numeric types.
   */
  short getShort();

  /**
   * Returns the value as an {@code int}.
   *
   * @throws TypeException if this value is not one of the numeric types.
   */
  int getInt();

  /**
   * Returns the value as a <code>long</code>.
   *
   * @throws TypeException if this value is not one of the numeric types.
   */
  long getLong();

  /**
   * Returns the value as a <code>float</code>.
   *
   * @throws TypeException if this value is not one of the numeric types.
   */
  float getFloat();

  /**
   * Returns the value as a <code>double</code>.
   *
   * @throws TypeException if this value is not one of the numeric types.
   */
  double getDouble();

  /**
   * Returns the value as a <code>BigDecimal</code>.
   *
   * @throws TypeException if this value is not one of the numeric types.
   */
  BigDecimal getDecimal();

  /**
   * Returns the value as a <code>boolean</code>.
   *
   * @throws TypeException if this value is not of <code>BOOLEAN</code> type.
   */
  boolean getBoolean();

  /**
   * Returns the value as a <code>String</code>.
   *
   * @throws TypeException if this value is not of <code>STRING</code> type.
   */
  String getString();

  /**
   * Returns the value as a {@link org.ojai.types.OTimestamp} object.
   *
   * @throws TypeException if this value is not of <code>TIMESTAMP</code> type.
   */
  OTimestamp getTimestamp();

  /**
   * Returns a long value representing the number of milliseconds since epoch.
   *
   * @throws TypeException if this value is not of <code>TIMESTAMP</code> type.
   */
  long getTimestampAsLong();

  /**
   * Returns the value as a {@link org.ojai.types.ODate} object.
   *
   * @throws TypeException if this value is not of <code>DATE</code> type.
   */
  ODate getDate();

  /**
   * Returns a {@code int} representing the number of DAYS since Unix epoch.
   *
   * @throws TypeException if this value is not of <code>DATE</code> type.
   */
  int getDateAsInt();

  /**
   * Returns the value as a {@link org.ojai.types.OTime} object. Modifying the
   * returned object does not alter the content of the Value.
   *
   * @throws TypeException if this value is not of <code>TIME</code> type.
   */
  OTime getTime();

  /**
   * Returns a {@code int} representing the number of milliseconds since midnight.
   *
   * @throws TypeException if this value is not of <code>TIME</code> type.
   */
  int getTimeAsInt();

  /**
   * Returns the value as a {@link org.ojai.types.OInterval} object.
   * Modifying the returned object does not alter the content of the Value.
   *
   * @throws TypeException if this value is not of <code>INTERVAL</code> type.
   */
  OInterval getInterval();

  /**
   * Returns a <code>long</code> representing interval duration in milliseconds.
   *
   * @throws TypeException if this value is not of <code>INTERVAL</code> type.
   */
  long getIntervalAsLong();

  /**
   * Returns the value as a {@link java.nio.ByteBuffer}. Modifying the
   * returned object does not alter the content of the Value.
   *
   * @throws TypeException if this value is not of <code>BINARY</code> type.
   */
  ByteBuffer getBinary();

  /**
   * Returns the value as a <code>Map<String, Object></code>. The returned
   * Map could be mutable or immutable however, modifying the returned Map
   * does not alter the content of the Value.
   *
   * @throws TypeException if this value is not of <code>MAP<code> type.
   */
  Map<String, Object> getMap();

  /**
   * Returns the value as a <code>List&lt;Object></code>. The returned List
   * could be mutable or immutable however, modifying the returned List does
   * not alter the content of the Value.
   *
   * @throws TypeException If this value is not of <code>ARRAY</code> type.
   */
  List<Object> getList();

  /**
   * Returns the value as an <code>Object} of the underlying type.
   * <pre>
   * Type.NULL      => null
   * Type.BOOLEAN   => Boolean
   * Type.STRING    => String
   * Type.BYTE      => Byte
   * Type.SHORT     => Short
   * Type.INT       => Integer
   * Type.LONG      => Long
   * Type.FLOAT     => Float
   * Type.DOUBLE    => Double
   * Type.DECIMAL   => BigDecimal
   * Type.DATE      => org.ojai.types.ODate
   * Type.TIME      => org.ojai.types.OTime
   * Type.TIMESTAMP => org.ojai.types.OTimestamp
   * Type.INTERVAL  => org.ojai.types.OInterval
   * Type.BINARY    => java.nio.ByteBuffer
   * Type.MAP       => Map<String, Object>
   * Type.ARRAY     => List<Object>
   * </pre>
   * <br/>Modifying the returned object does not alter the content of the Value.
   */
  Object getObject();

  /**
   * Returns a {@link DocumentReader} over the current document.
   * @return
   */
  DocumentReader asReader();

}
