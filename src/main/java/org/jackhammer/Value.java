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
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import org.joda.time.Interval;

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
     * 32-bit integer representing the number of days since epoch,
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
    DATETIME,

    /**
     * Arbitrary precision, fixed point decimal value.
     */
    DECIMAL,

    /**
     * A value representing a period of time between two instants.
     */
    INTERVAL,

    /**
     * Uninterpreted sequence bytes.
     */
    BINARY,

    /**
     * Mapping of {@link String} and {@link Value}.
     */
    MAP,

    /**
     * A list of {@link Value}.
     */
    ARRAY
  }

  Value.Type getType();

  /**
   * Returns the value as a <code>byte</code>. May result in rounding or truncation.
   * <br><br>
   * Supported for {@link Type#BYTE}, {@link Type#SHORT}, {@link Type#INT},
   * {@link Type#LONG}, {@link Type#FLOAT}, {@link Type#DOUBLE}, {@link Type#DECIMAL}.
   *
   * @throws ValueTypeException if the underlying value can not be converted
   * to a <code>byte</code>.
   */
  byte getByte();

  /**
   * Returns the value as a <code>short</code>. May result in rounding or truncation.
   * <br><br>
   * Supported for {@link Type#BYTE}, {@link Type#SHORT}, {@link Type#INT},
   * {@link Type#LONG}, {@link Type#FLOAT}, {@link Type#DOUBLE}, {@link Type#DECIMAL}.
   *
   * @throws ValueTypeException if the underlying value can not be converted
   * to a <code>short</code>.
   */
  short getShort();

  /**
   * Returns the value as an <code>int</code>. May result in rounding or truncation.
   * <br><br>
   * Supported for {@link Type#BYTE}, {@link Type#SHORT}, {@link Type#INT},
   * {@link Type#LONG}, {@link Type#FLOAT}, {@link Type#DOUBLE}, {@link Type#DECIMAL}.
   *
   * @throws ValueTypeException if the underlying value can not be converted
   * to a <code>int</code>.
   */
  int getInt();

  /**
   * Returns the value as a <code>long</code>. May result in rounding or truncation.
   * <br><br>
   * Supported for {@link Type#BYTE}, {@link Type#SHORT}, {@link Type#INT},
   * {@link Type#LONG}, {@link Type#FLOAT}, {@link Type#DOUBLE}, {@link Type#DECIMAL}.
   *
   * @throws ValueTypeException if the underlying value can not be converted
   * to a <code>long</code>.
   */
  long getLong();

  /**
   * Returns the value as a <code>float</code>. May result in rounding or truncation.
   * <br><br>
   * Supported for {@link Type#BYTE}, {@link Type#SHORT}, {@link Type#INT},
   * {@link Type#LONG}, {@link Type#FLOAT}, {@link Type#DOUBLE}, {@link Type#DECIMAL}.
   *
   * @throws ValueTypeException if the underlying value can not be converted
   * to a <code>float</code>.
   */
  float getFloat();

  /**
   * Returns the value as a <code>double</code>. May result in rounding or truncation.
   * <br><br>
   * Supported for {@link Type#BYTE}, {@link Type#SHORT}, {@link Type#INT},
   * {@link Type#LONG}, {@link Type#FLOAT}, {@link Type#DOUBLE}, {@link Type#DECIMAL}.
   *
   * @throws ValueTypeException if the underlying value can not be converted
   * to a <code>double</code>.
   */
  double getDouble();

  /**
   * Returns the value as a <code>boolean</code>.
   *
   * @throws ValueTypeException if the underlying value is not a <code>boolean</code>.
   */
  boolean getBoolean();

  /**
   * Returns the value as a {@link String}.
   *
   * @throws ValueTypeException if the underlying value is not of type {@link Type#STRING}.
   */
  String getString();

  /**
   * Returns the value as a {@link BigDecimal}. May result in rounding or truncation.
   * <br><br>
   * Supported for {@link Type#BYTE}, {@link Type#SHORT}, {@link Type#INT},
   * {@link Type#LONG}, {@link Type#FLOAT}, {@link Type#DOUBLE}, {@link Type#DECIMAL}.
   *
   * @throws ValueTypeException if the underlying value can not be converted
   * to a {@link BigDecimal}.
   */
  BigDecimal getDecimal();

  long getTimeStamp();

  Date getDate();

  Time getTime();

  Interval getInterval();

  /**
   * Returns the value as a {@link Map}.
   *
   * @throws ValueTypeException if the underlying value is not of type {@link Type#MAP}.
   */
  Map<String, Value> getMap();

  /**
   * Returns the value as a {@link List}.
   *
   * @throws ValueTypeException if the underlying value is not of type {@link Type#ARRAY}.
   */
  List<Value> getList();

  /**
   * Returns the value as an {@link Object} of the underlying type.
   *
   */
  Object getObject();

  StreamReader getStream();
}
