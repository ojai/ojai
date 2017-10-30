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
package org.ojai.store.cdc;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.TypeException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.w3c.dom.ranges.RangeException;

/**
 * A parser that traverses over the individual change nodes in a {@link ChangeDataRecord}.
 * It provides a cursor-like semantics which can be moved, one node at a time by invoking
 * {@link #next()}.
 * <p/>
 * The properties of individual change node. For example, data type, field name, 
 * field value, etc. can be retrieved using various methods of this interface.
 */
@API.Public
@API.NotThreadSafe
public interface ChangeDataReader extends Iterator<ChangeEvent> {

  /**
   * Moves the cursor to the next change node and returns the {@link ChangeEvent}
   * associated with the node.
   */
  @Override
  ChangeEvent next();

  /**
   * Returns the {@link ChangeOp} of the current change node.
   */
  ChangeOp getOp();

  /**
   * @return the field name of the current change node
   */
  String getFieldName();

  /**
   * @return the byte array equivalent of the field name of the current change node
   */
  ByteBuffer getFieldNameBytes();

  /**
   * @return the index of the current node in an Array
   */
  int getArrayIndex();

  /**
   * If it returns true, the current field is in a map; 
   * otherwise, the current field is not in a map.
   * 
   * @return whether this node is in a Map. {@code `true`} for all top level fields
   */
  boolean inMap();

  /**
   * If it returns true, the current field is in an array; 
   * otherwise, the current field is not in an array.
   * 
   * @return whether this node is an Array
   */
  boolean inArray();

  /**
   * 
   * @return the type of the OJAI Value of the current node
   */
  Type getType();

  /**
   * Returns the logical time of the data.
   * The version is the same as the timestamp when this operation occurs.
   * For a multi-version DocumentStore, the version is specified by the user. 
   */
  long getOpTimestamp();
  
  /**
   * @return the timestamp when the operation arrived on the server
   */
  long getServerTimestamp();

  /**
   * @return the {@code byte} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code BYTE}
   */
  byte getByte();

  /**
   * @return the {@code byte} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code SHORT}
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
   * @return A byte array containing the two's complement representation of
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
   *         representing the number of milliseconds since midnight
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

  /**
   * Returns the value of this interval duration in milliseconds computed from
   * individual fields. This value will be approximate if the months or years
   * field of this interval is non-zero.
   *
   * @throws TypeException if the current {@code EventType} is not
   *         {@code INTERVAL}
   */
  long getIntervalMillis();

  /**
   * @return the {@code ByteBuffer} value of the current node
   * @throws TypeException if the current {@code EventType} is not
   *         {@code BINARY}
   */
  ByteBuffer getBinary();

}
