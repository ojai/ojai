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

import org.ojai.annotation.API;
import org.ojai.exceptions.TypeException;
import org.ojai.types.Interval;

@API.Public
public interface Value {

  public enum Type {
    /**
     * A non-existing value of unknown type and quantity.
     */
    NULL(1),

    /**
     * A boolean value.
     */
    BOOLEAN(2),

    /**
     * Character sequence.
     */
    STRING(3),

    /**
     * 8-bit signed integer.
     */
    BYTE(4),

    /**
     * 16-bit signed integer.
     */
    SHORT(5),

    /**.
     * 32-bit signed integer.
     */
    INT(6),

    /**
     * 64-bit signed integer.
     */
    LONG(7),

    /**
     * Single-precision 32-bit floating point number.
     */
    FLOAT(8),

    /**
     * Double-precision 64-bit floating point number.
     */
    DOUBLE(9),

    /**
     * Arbitrary precision, fixed point decimal value.
     */
    DECIMAL(10),

    /**
     * 32-bit integer representing the number of DAYS since epoch,
     * i.e. January 1, 1970 00:00:00 UTC. The value is absolute and
     * is time-zone independent.
     */
    DATE(11),

    /**
     * 32-bit integer representing time of the day in milliseconds.
     * The value is absolute and is time-zone independent.
     */
    TIME(12),

    /**
     * 64-bit integer representing the number of milliseconds since epoch,
     * i.e. January 1, 1970 00:00:00 UTC. Negative values represent dates
     * before epoch.
     */
    TIMESTAMP(13),

    /**
     * A value representing a period of time between two instants.
     */
    INTERVAL(14),

    /**
     * Uninterpreted sequence of bytes.
     */
    BINARY(15),

    /**
     * Mapping of String and <code>Value</code>.
     */
    MAP(16),

    /**
     * A list of <code>Value</code>.
     */
    ARRAY(17);

    private byte code;

    Type(int code) {
      this.code = (byte) code;
    }

    public byte getCode() {
      return code;
    }

    public static Type valueOf(int typeCode) {
      switch (typeCode) {
        case 1: return NULL;
        case 2: return BOOLEAN;
        case 3: return STRING;
        case 4: return BYTE;
        case 5: return SHORT;
        case 6: return INT;
        case 7: return LONG;
        case 8: return FLOAT;
        case 9: return DOUBLE;
        case 10: return DECIMAL;
        case 11: return DATE;
        case 12: return TIME;
        case 13: return TIMESTAMP;
        case 14: return INTERVAL;
        case 15: return BINARY;
        case 16: return MAP;
        case 17: return ARRAY;
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
   * @throws TypeException if this value is not of <code>BYTE</code> type.
   */
  byte getByte();

  /**
   * Returns the value as a <code>short</code>.
   *
   * @throws TypeException if this value is not of <code>SHORT</code> type.
   */
  short getShort();

  /**
   * Returns the value as an {@code int}.
   *
   * @throws TypeException if this value is not of <code>INT</code> type.
   */
  int getInt();

  /**
   * Returns the value as a <code>long</code>.
   *
   * @throws TypeException if this value is not of <code>LONG</code> type.
   */
  long getLong();

  /**
   * Returns the value as a <code>float</code>.
   *
   * @throws TypeException if this value is not of <code>FLOAT</code> type.
   */
  float getFloat();

  /**
   * Returns the value as a <code>double</code>.
   *
   * @throws TypeException if this value is not of <code>DOUBLE</code> type.
   */
  double getDouble();

  /**
   * Returns the value as a <code>BigDecimal</code>.
   *
   * @throws TypeException if this value is not of Type.DECIMAL type.
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
   * Returns the value as a {@link java.sql.Timestamp} object. Modifying the
   * returned object does not alter the content of the Value.
   *
   * @throws TypeException if this value is not of <code>TIMESTAMP</code> type.
   */
  Timestamp getTimestamp();

  /**
   * Returns a long value representing the number of milliseconds since epoch.
   *
   * @throws TypeException if this value is not of <code>TIMESTAMP</code> type.
   */
  long getTimestampAsLong();

  /**
   * Returns the value as a {@link java.sql.Date} object. Modifying the
   * returned object does not alter the content of the Value.
   *
   * @throws TypeException if this value is not of <code>DATE</code> type.
   */
  Date getDate();

  /**
   * Returns a {@code int} representing the number of DAYS since Unix epoch.
   *
   * @throws TypeException if this value is not of <code>DATE</code> type.
   */
  int getDateAsInt();

  /**
   * Returns the value as a {@link java.sql.Time} object. Modifying the
   * returned object does not alter the content of the Value.
   *
   * @throws TypeException if this value is not of <code>TIME</code> type.
   */
  Time getTime();

  /**
   * Returns a {@code int} representing the number of milliseconds since midnight.
   *
   * @throws TypeException if this value is not of <code>TIME</code> type.
   */
  int getTimeAsInt();

  /**
   * Returns the value as a {@link org.ojai.types.Interval} object.
   * Modifying the returned object does not alter the content of the Value.
   *
   * @throws TypeException if this value is not of <code>INTERVAL</code> type.
   */
  Interval getInterval();

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
   * Type.DATE      => java.sql.Date
   * Type.TIME      => java.sql.Time
   * Type.TIMESTAMP => java.sql.Timestamp
   * Type.INTERVAL  => org.ojai.types.Interval
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
  DocumentReader getStream();

}
