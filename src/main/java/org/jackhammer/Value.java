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

import org.jackhammer.types.Interval;

public interface Value {

  public enum Type {
    /**
     * A non-existing value of unknown type and quantity.
     */
    NULL,

    /**
     * A boolean value.
     */
    BOOLEAN,

    /**
     * Character sequence.
     */
    STRING,

    /**
     * 8-bit signed integer.
     */
    BYTE,

    /**
     * 16-bit signed integer.
     */
    SHORT,

    /**.
     * 32-bit signed integer.
     */
    INT,

    /**
     * 64-bit signed integer.
     */
    LONG,

    /**
     * Single-precision 32-bit floating point number.
     */
    FLOAT,

    /**
     * Double-precision 64-bit floating point number.
     */
    DOUBLE,

    /**
     * Arbitrary precision, fixed point decimal value.
     */
    DECIMAL,

    /**
     * 32-bit integer representing the number of DAYS since epoch,
     * i.e. January 1, 1970 00:00:00 UTC.
     */
    DATE,

    /**
     * 32-bit integer representing time of the day in milliseconds.
     */
    TIME,

    /**
     * 64-bit integer representing the number of milliseconds since epoch,
     * i.e. January 1, 1970 00:00:00 UTC. Negative values represent dates
     * before epoch.
     */
    TIMESTAMP,

    /**
     * A value representing a period of time between two instants.
     */
    INTERVAL,

    /**
     * Uninterpreted sequence bytes.
     */
    BINARY,

    /**
     * Mapping of String and <code>Value</code>.
     */
    MAP,

    /**
     * A list of <code>Value</code>.
     */
    ARRAY
  }

  /**
   * @return The <code>Type</code> of this value.
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
   * Returns the value as an <code>int</code>.
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
   * Returns the value as a {@link java.sql.Timestamp} object.
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
   * Returns the value as a {@link java.sql.Date} object.
   *
   * @throws TypeException if this value is not of <code>DATE</code> type.
   */
  Date getDate();

  /**
   * Returns a <code>int</code> representing the number of DAYS since epoch.
   *
   * @throws TypeException if this value is not of <code>DATE</code> type.
   */
  int getDateAsInt();

  /**
   * Returns the value as a {@link java.sql.Time} object.
   *
   * @throws TypeException if this value is not of <code>TIME</code> type.
   */
  Time getTime();

  /**
   * Returns a <code>int</code> representing the number of milliseconds since midnight.
   *
   * @throws TypeException if this value is not of <code>TIME</code> type.
   */
  int getTimeAsInt();

  /**
   * Returns the value as a {@link org.jackhammer.types.Interval} object.
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
   * Returns the value as a {@link java.nio.ByteBuffer}.
   *
   * @throws TypeException if this value is not of <code>BINARY</code> type.
   */
  ByteBuffer getBinary();

  /**
   * Returns the value as a <code>Map<String, Object></code>.
   *
   * @throws TypeException if this value is not of <code>MAP<code> type.
   */
  Map<String, Object> getMap();

  /**
   * Returns the value as a <code>List&lt;Object></code>.
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
   * Type.INTERVAL  => org.jackhammer.types.Interval
   * Type.BINARY    => java.nio.ByteBuffer
   * Type.MAP       => Map<String, Object>
   * Type.ARRAY     => List<Object>
   * </pre>
   */
  Object getObject();

  /**
   * Returns a {@link RecordReader} over the current record.
   * @return
   */
  RecordReader getStream();

}
