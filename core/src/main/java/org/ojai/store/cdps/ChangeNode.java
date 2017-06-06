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
package org.ojai.store.cdps;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.ojai.Document;
import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.TypeException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

/**
 * An encapsulation of a change to a single field in a {@link Document}.
 */
@API.Public
public interface ChangeNode {
  /**
   * Returns the {@link ChangeOp} of the current change node.
   */
  ChangeOp getOp();

  /**
   * Returns the type of the {@link Value OJAI Value} of the
   * current node.<br/> Returns null if {@link ChangeOp} is not
   * {@link ChangeOp#SET SET} or {@link ChangeOp#MERGE MERGE}.
   */
  Type getType();

  /**
   * Returns the timestamp associated with the current change
   * operation as number of milliseconds since Unix epoch.
   */
  long getOpTimestamp();

  /**
   * Returns the server timestamp of this change operation as
   * number of milliseconds since Unix epoch.
   */
  long getServerTimestamp();

  /**
   * Returns The {@code byte} value of the current node.
   *
   * @throws TypeException if this change node is not one of the numeric types.
   */
  byte getByte();

  /**
   * Returns the value as a <code>short</code>.
   *
   * @throws TypeException if this change node is not one of the numeric types.
   */
  short getShort();

  /**
   * Returns the value as an {@code int}.
   *
   * @throws TypeException if this change node is not one of the numeric types.
   */
  int getInt();

  /**
   * Returns the value as a <code>long</code>.
   *
   * @throws TypeException if this change node is not one of the numeric types.
   */
  long getLong();

  /**
   * Returns the value as a <code>float</code>.
   *
   * @throws TypeException if this change node is not one of the numeric types.
   */
  float getFloat();

  /**
   * Returns the value as a <code>double</code>.
   *
   * @throws TypeException if this change node is not one of the numeric types.
   */
  double getDouble();

  /**
   * Returns the value as a <code>BigDecimal</code>.
   *
   * @throws TypeException if this change node is not one of the numeric types.
   */
  BigDecimal getDecimal();

  /**
   * Returns the value as a <code>boolean</code>.
   *
   * @throws TypeException if this change node is not of <code>BOOLEAN</code> type.
   */
  boolean getBoolean();

  /**
   * Returns the value as a <code>String</code>.
   *
   * @throws TypeException if this change node is not of <code>STRING</code> type.
   */
  String getString();

  /**
   * Returns the value as a {@link org.ojai.types.OTimestamp} object.
   *
   * @throws TypeException if this change node is not of <code>TIMESTAMP</code> type.
   */
  OTimestamp getTimestamp();

  /**
   * Returns a long value representing the number of milliseconds since epoch.
   *
   * @throws TypeException if this change node is not of <code>TIMESTAMP</code> type.
   */
  long getTimestampAsLong();

  /**
   * Returns the value as a {@link org.ojai.types.ODate} object.
   *
   * @throws TypeException if this change node is not of <code>DATE</code> type.
   */
  ODate getDate();

  /**
   * Returns a {@code int} representing the number of DAYS since Unix epoch.
   *
   * @throws TypeException if this change node is not of <code>DATE</code> type.
   */
  int getDateAsInt();

  /**
   * Returns the value as a {@link org.ojai.types.OTime} object. Modifying the
   * returned object does not alter the content of the Value.
   *
   * @throws TypeException if this change node is not of <code>TIME</code> type.
   */
  OTime getTime();

  /**
   * Returns a {@code int} representing the number of milliseconds since midnight.
   *
   * @throws TypeException if this change node is not of <code>TIME</code> type.
   */
  int getTimeAsInt();

  /**
   * Returns the value as a {@link org.ojai.types.OInterval} object.
   * Modifying the returned object does not alter the content of the Value.
   *
   * @throws TypeException if this change node is not of <code>INTERVAL</code> type.
   */
  OInterval getInterval();

  /**
   * Returns a <code>long</code> representing interval duration in milliseconds.
   *
   * @throws TypeException if this change node is not of <code>INTERVAL</code> type.
   */
  long getIntervalAsLong();

  /**
   * Returns the value as a {@link java.nio.ByteBuffer}. Modifying the
   * returned object does not alter the content of the Value.
   *
   * @throws TypeException if this change node is not of <code>BINARY</code> type.
   */
  ByteBuffer getBinary();

  /**
   * Returns the value as a <code>Map&lt;String, Object&gt;</code>. The returned
   * Map could be mutable or immutable however, modifying the returned Map
   * does not alter the content of the Value.
   *
   * @throws TypeException if this change node is not of <code>MAP<code> type.
   */
  Map<String, Object> getMap();

  /**
   * Returns the value as a <code>List&lt;Object&gt;</code>. The returned List
   * could be mutable or immutable however, modifying the returned List does
   * not alter the content of the Value.
   *
   * @throws TypeException If this change node is not of <code>ARRAY</code> type.
   */
  List<Object> getList();

  /**
   * Returns the value as an {@code Object} of the underlying type.
   * <pre>
   * Type.NULL      =&gt; null
   * Type.BOOLEAN   =&gt; Boolean
   * Type.STRING    =&gt; String
   * Type.BYTE      =&gt; Byte
   * Type.SHORT     =&gt; Short
   * Type.INT       =&gt; Integer
   * Type.LONG      =&gt; Long
   * Type.FLOAT     =&gt; Float
   * Type.DOUBLE    =&gt; Double
   * Type.DECIMAL   =&gt; BigDecimal
   * Type.DATE      =&gt; org.ojai.types.ODate
   * Type.TIME      =&gt; org.ojai.types.OTime
   * Type.TIMESTAMP =&gt; org.ojai.types.OTimestamp
   * Type.INTERVAL  =&gt; org.ojai.types.OInterval
   * Type.BINARY    =&gt; java.nio.ByteBuffer
   * Type.MAP       =&gt; Map&lt;String, Object&gt;
   * Type.ARRAY     =&gt; List&lt;Object&gt;
   * </pre>
   * <br/>Modifying the returned object does not alter the content of the Value.
   */
  Object getObject();

  /**
   * Returns The OJAI {@code Value} of the current node.
   */
  Value getValue();

}
