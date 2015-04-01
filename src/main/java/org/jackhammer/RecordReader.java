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

import org.jackhammer.exceptions.TypeException;
import org.jackhammer.types.Interval;

public interface RecordReader {

  public enum EventType {
    NULL,
    BOOLEAN,
    STRING,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    DECIMAL,
    DATE,
    TIME,
    TIMESTAMP,
    INTERVAL,
    BINARY,
    FIELD_NAME,
    START_MAP,
    END_MAP,
    START_ARRAY,
    END_ARRAY,
  }

  /**
   * Move the {@code RecordReader} to the next node and returns the node type
   * as {@code EventType}.
   *
   * @return The {@code EventType} for the current node or {@code null} if the
   *         reader is past the end of the record.
   */
  RecordReader.EventType next();

  /**
   * @return The name of the current field.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code FIELD_NAME}.
   */
  String getFieldName();

  /**
   * @return The {@code byte} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code BYTE}.
   */
  byte getByte();

  /**
   * @return The {@code byte} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code SHORT}.
   */
  short getShort();

  /**
   * @return The {@code int} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code INT}.
   */
  int getInt();

  /**
   * @return The {@code long} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code LONG}.
   */
  long getLong();

  /**
   * @return The {@code float} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code FLOAT}.
   */
  float getFloat();

  /**
   * @return The {@code double} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code DOUBLE}.
   */
  double getDouble();

  /**
   * @return The {@code BigDecimal} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code DECIMAL}.
   */
  BigDecimal getDecimal();

  /**
   * Returns the <i>precision</i> of current {@code DECIMAL} node.
   * (The precision is the number of digits in the unscaled value.)
   *
   * <p>The precision of a zero value is 1.
   * @return the precision of current {@code DECIMAL} node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code DECIMAL}.
   */
  int getDecimalPrecision();

  /**
   * Returns the <i>scale</i> of current {@code DECIMAL} node. If the returned
   * value is zero or positive, the scale is the number of digits to the right
   * of the decimal point.  If negative, the unscaled value of the number is
   * multiplied by ten to the power of the negation of the scale.  For example,
   * a scale of {@code -3} means the unscaled value is multiplied by 1000.
   *
   * @return the scale of current {@code DECIMAL} node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code DECIMAL}.
   */
  int getDecimalScale();

  /**
   * Returns an {@code int} whose value is the <i>unscaled value</i> of this
   * {@code DECIMAL}.
   *
   * @return the unscaled value of current {@code DECIMAL} node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code DECIMAL}.
   * @throws RangeException - If the precision of the decimal number is greater
   *         than 9.
   */
  int getDecimalValueAsInt();

  /**
   * Returns an {@code long} whose value is the <i>unscaled value</i> of this
   * {@code DECIMAL}.
   *
   * @return the unscaled value of current {@code DECIMAL} node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code DECIMAL}.
   * @throws RangeException - If the precision of the decimal number is greater
   *         than 18.
   */
  long getDecimalValueAsLong();

  /**
   * Returns a {@code ByteBuffer} containing the two's-complement representation
   * of the current {@code DECIMAL} node. The byte array will be in <i>big-endian
   * </i> byte-order: the most significant byte is in the zeroth element. The
   * array will contain the minimum number of bytes required to represent this
   * {@code DECIMAL}, including at one sign bit.
   *
   * @return A byte array containing the two's-complement representation of
   *         current {@code DECIMAL} node.
   */
  ByteBuffer getDecimalValueAsBytes();

  /**
   * @return The {@code boolean} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code BOOLEAN}.
   */
  boolean getBoolean();

  /**
   * @return The {@code String} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code STRING}.
   */
  String getString();

  /**
   * @return The {@code Timestamp} value of the current node as a {@code long}
   *         representing the number of milliseconds since epoch.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code TIMESTAMP}.
   */
  long getTimeStamp();

  /**
   * @return The {@code Timestamp} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code TIMESTAMP}.
   */
  Timestamp getTimestamp();

  /**
   * @return The {@code Date} value of the current node as an {@code int}
   *         representing the number of DAYS since epoch.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code DATE}.
   */
  int getDateInt();

  /**
   * @return The {@code Date} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code DATE}.
   */
  Date getDate();

  /**
   * @return The {@code Time} value of the current node as an {@code int}
   *         representing the number of milliseconds since midnight.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code TIME}.
   */
  int getTimeInt();

  /**
   * @return The {@code Time} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code TIME}.
   */
  Time getTime();

  /**
   * @return The {@code Interval} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code INTERVAL}.
   */
  Interval getInterval();
  int getIntervalDays();
  int getIntervalMillis();

  /**
   * @return The {@code ByteBuffer} value of the current node.
   * @throws TypeException If the current {@code EventType} is not
   *         {@code BINARY}.
   */
  ByteBuffer getBinary();

}
