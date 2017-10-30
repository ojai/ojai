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

import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.RangeException;
import org.ojai.exceptions.TypeException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

@API.Public
@API.NotThreadSafe
public interface DocumentReader {

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
    START_MAP,
    END_MAP,
    START_ARRAY,
    END_ARRAY,
  }

  /**
   * Moves the {@code DocumentReader} to the next node and returns the node
   * type as {@code EventType}.
   *
   * @return the {@code EventType} for the current node or {@code null} if the
   *         reader is past the end of the document
   * @throws DecodingException if the document is malformed or incomplete.
   */
  EventType next();

  /**
   * @return the event emitted by the last call to {@link #next()}
   */
  EventType getCurrentEvent();

  /**
   * When called after {@link EventType#START_MAP} or {@link EventType#START_ARRAY} event,
   * moves this {@code DocumentReader} to the corresponding {@link EventType#END_MAP} or
   * {@link EventType#END_ARRAY}, skipping the entire sub-tree.
   * <p/>
   * For all other events, this is a NO-OP.
   *
   * @return {@code this} for chaining
   */
  DocumentReader skipChildren();

  /**
   * @return {@code true} while traversing a Map, {@code false} if traversing
   * an Array
   */
  boolean inMap();

  /**
   * @return the name of the current field, {@code null} for the top level Document
   * @throws IllegalStateException if the reader is not traversing a Map
   */
  String getFieldName();

  /**
   * @return the index of the current element in the Array
   * @throws IllegalStateException if the reader is not traversing an Array
   */
  int getArrayIndex();

  /**
   * @return the {@code byte} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code BYTE}
   */
  byte getByte();

  /**
   * @return the {@code byte} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code SHORT}.
   */
  short getShort();

  /**
   * @return the {@code int} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code INT}
   */
  int getInt();

  /**
   * @return the {@code long} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code LONG}
   */
  long getLong();

  /**
   * @return the {@code float} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code FLOAT}
   */
  float getFloat();

  /**
   * @return the {@code double} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code DOUBLE}
   */
  double getDouble();

  /**
   * @return the {@code BigDecimal} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code DECIMAL}
   */
  BigDecimal getDecimal();

  /**
   * Returns the <i>precision</i> of the current {@code DECIMAL} node.
   * (The precision is the number of digits in the unscaled value.)
   *
   * <p>The precision of a zero value is 1.
   * @return the precision of current {@code DECIMAL} node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code DECIMAL}
   */
  int getDecimalPrecision();

  /**
   * Returns the <i>scale</i> of the current {@code DECIMAL} node. If the returned
   * value is zero or positive, the scale is the number of digits to the right
   * of the decimal point.  If negative, the unscaled value of the number is
   * multiplied by ten to the power of the negation of the scale.  For example,
   * a scale of {@code -3} means the unscaled value is multiplied by 1000.
   *
   * @return the scale of current {@code DECIMAL} node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code DECIMAL}
   */
  int getDecimalScale();

  /**
   * Returns an {@code int} whose value is the <i>unscaled value</i> of this
   * {@code DECIMAL}.
   *
   * @return the unscaled value of current {@code DECIMAL} node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code DECIMAL}
   * @throws RangeException if the precision of the decimal number is greater
   *         than 9
   */
  int getDecimalValueAsInt();

  /**
   * Returns a {@code long} whose value is the <i>unscaled value</i> of this
   * {@code DECIMAL}.
   *
   * @return the unscaled value of current {@code DECIMAL} node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code DECIMAL}
   * @throws RangeException if the precision of the decimal number is greater
   *         than 18
   */
  long getDecimalValueAsLong();

  /**
   * Returns a {@code ByteBuffer} containing the two's complement representation
   * of the current {@code DECIMAL} node. The byte array will be in <i>big-endian
   * </i> byte order: the most significant byte is in the zeroth element. The
   * array will contain the minimum number of bytes required to represent this
   * {@code DECIMAL}, including one sign bit.
   *
   * @return a byte array containing the two's complement representation of
   *         current {@code DECIMAL} node
   */
  ByteBuffer getDecimalValueAsBytes();

  /**
   * @return the {@code boolean} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code BOOLEAN}
   */
  boolean getBoolean();

  /**
   * @return the {@code String} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code STRING}
   */
  String getString();

  /**
   * @return the {@code Timestamp} value of the current node as a {@code long}
   *         representing the number of milliseconds since epoch
   * @throws TypeException if the current {@code EventType} is not
   *         {@code TIMESTAMP}
   */
  long getTimestampLong();

  /**
   * @return the {@code Timestamp} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code TIMESTAMP}
   */
  OTimestamp getTimestamp();

  /**
   * @return the {@code Date} value of the current node as an {@code int}
   *         representing the number of DAYS since epoch
   * @throws TypeException if the current {@code EventType} is not
   *         {@code DATE}
   */
  int getDateInt();

  /**
   * @return the {@code Date} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code DATE}
   */
  ODate getDate();

  /**
   * @return the {@code Time} value of the current node as an {@code int}
   *         representing the number of milliseconds since midnight.
   * @throws TypeException if the current {@code EventType} is not
   *         {@code TIME}
   */
  int getTimeInt();

  /**
   * @return the {@code Time} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code TIME}
   */
  OTime getTime();

  /**
   * @return the {@code Interval} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code INTERVAL}
   */
  OInterval getInterval();
  int getIntervalDays();
  long getIntervalMillis();

  /**
   * @return the {@code ByteBuffer} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code BINARY}
   */
  ByteBuffer getBinary();

}
