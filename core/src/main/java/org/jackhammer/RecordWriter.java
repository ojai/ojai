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

import org.jackhammer.annotation.API;
import org.jackhammer.types.Interval;

@API.Public
public interface RecordWriter {

  /* ===========
   * Map Methods
   * ===========
   */

  /**
   * Associates the specified {@code boolean} value with the specified
   * {@code field} in the current map. Any previous association will be
   * overwritten.
   *
   * @param field The name of the field.
   * @param value The {@code boolean} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in a MAP segment.
   */
  RecordWriter put(String field, boolean value);

  /**
   * Associates the specified {@code String} value with the specified
   * {@code field} in the current map. Any previous association will be
   * overwritten.
   *
   * @param field The name of the field.
   * @param value The {@code String} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in a MAP segment.
   */
  RecordWriter put(String field, String value);

  RecordWriter put(String field, byte value);
  RecordWriter put(String field, short value);
  RecordWriter put(String field, int value);
  RecordWriter put(String field, long value);
  RecordWriter put(String field, float value);
  RecordWriter put(String field, double value);
  RecordWriter put(String field, BigDecimal value);
  RecordWriter putDecimal(String field, long decimalValue);
  RecordWriter putDecimal(String field, double decimalValue);
  RecordWriter putDecimal(String field, int unscaledValue, int scale);
  RecordWriter putDecimal(String field, long unscaledValue, int scale);
  RecordWriter putDecimal(String field, byte[] unscaledValue, int scale);

  RecordWriter put(String field, byte[] value);
  RecordWriter put(String field, byte[] value, int offset, int length);
  RecordWriter put(String field, ByteBuffer value);

  RecordWriter put(String field, Date value);

  /**
   * Associates the specified {@code date} value represented as number of
   * days since epoch with the specified {@code field} in the
   * current map. Any previous association will be overwritten.
   *
   * @param  field The name of the field.
   * @param  value The {@code date} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in a MAP segment.
   */
  RecordWriter putDate(String field, int days);

  RecordWriter put(String field, Time value);

  /**
   * Associates the specified {@code time} value represented as number of
   * milliseconds since midnight with the specified {@code field} in the
   * current map. Any previous association will be overwritten.
   *
   * @param  field The name of the field.
   * @param  value The {@code time} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in a MAP segment.
   * @throws IllegalArgumentException If the value of {@code millis} is greater
   *         than 86400000.
   */
  RecordWriter putTime(String field, int millis);

  RecordWriter put(String field, Timestamp value);

  /**
   * Associates the specified {@code timestamp} value represented as number of
   * milliseconds since epoch with the specified {@code field} in the
   * current map. Any previous association will be overwritten.
   *
   * @param  field The name of the field.
   * @param  value The {@code timestamp} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in a MAP segment.
   */
  RecordWriter putTimestamp(String field, long timeMillis);

  RecordWriter put(String field, Interval value);
  RecordWriter putInterval(String field, long durationInMs);
  RecordWriter putInterval(String field, int months, int days, int milliseconds);

  RecordWriter putNewMap(String field);
  RecordWriter putNewArray(String field);

  RecordWriter putNull(String field);

  RecordWriter put(String field, Value value);
  RecordWriter put(String field, Record value);

  /* =============
   * Array Methods
   * =============
   */

  /**
   * Appends a {@code boolean} value to the current array.
   * @param value The {@code boolean} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(boolean value);

  /**
   * Appends a {@code String} value to the current array.
   * @param value The {@code String} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(String value);

  /**
   * Appends a {@code byte} value to the current array.
   * @param value The {@code byte} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(byte value);

  /**
   * Appends a {@code short} value to the current array.
   * @param value The {@code short} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(short value);

  /**
   * Appends a {@code int} value to the current array.
   * @param value The {@code int} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(int value);

  /**
   * Appends a {@code long} value to the current array.
   * @param value The {@code long} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(long value);

  /**
   * Appends a {@code float} value to the current array.
   * @param value The {@code float} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(float value);

  /**
   * Appends a {@code double} value to the current array.
   * @param value The {@code double} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(double value);

  /**
   * Appends a {@code BigDecimal} value to the current array.
   * @param value The {@code BigDecimal} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(BigDecimal value);

  /**
   * Appends a long number as a {@code DECIMAL} value to the current array.
   * @param value The {@code long} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter addDecimal(long decimalValue);

  /**
   * Appends a double number as a {@code DECIMAL} value to the current array.
   * @param value The {@code double} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter addDecimal(double decimalValue);

  /**
   * Appends an {@code int} unscaled value and an {@code int} scale as a
   * {@code DECIMAL} value. The {@code DECIMAL} value is
   * <tt>(unscaledValue &times; 10<sup>-scale</sup>)</tt>.
   *
   * @param unscaledValue unscaled value of the {@code DECIMAL}.
   * @param scale scale of the {@code DECIMAL}.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter addDecimal(int unscaledValue, int scale);

  /**
   * Appends an {@code long} unscaled value and an {@code int} scale as a
   * {@code DECIMAL} value. The {@code DECIMAL} value is
   * <tt>(unscaledValue &times; 10<sup>-scale</sup>)</tt>.
   *
   * @param unscaledValue unscaled value of the {@code DECIMAL}.
   * @param scale scale of the {@code DECIMAL}.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter addDecimal(long unscaledValue, int scale);

  /**
   * Appends a byte array containing the two's-complement binary representation
   * and an {@code int} scale as a {@code DECIMAL} value. The input array is
   * assumed to be in <i>big-endian</i> byte-order: the most significant
   * byte is in the zeroth element.
   *
   * @param unscaledValue unscaled value of the {@code DECIMAL}.
   * @param scale scale of the {@code DECIMAL}.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter addDecimal(byte[] unscaledValue, int scale);

  /**
   * Appends the byte array as a {@code BINARY} value to the current array.
   * @param value The byte array to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(byte[] value);

  /**
   * Appends the byte array bounded by offset and length as a {@code BINARY}
   * value to the current array.
   * @param value The byte array to append.
   * @param offset The start offset in the byte array.
   * @param length The length from the offset.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   * @throws IndexOutOfBoundsException If the offset or offset+length are outside
   *         of byte array range.
   */
  RecordWriter add(byte[] value, int offset, int length);

  /**
   * Appends the {@code ByteBuffer} as a {@code BINARY} value to the current array.
   * @param value The {@code ByteBuffer} to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(ByteBuffer value);

  /**
   * Appends a {@code NULL} value to the current array.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter addNull();

  /**
   * Appends the {@code Value} to the current array.
   * @param value The {@code Value} to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(Value value);

  /**
   * Appends the {@code Record} to the current array.
   * @param value The {@code Record} to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in an ARRAY segment.
   */
  RecordWriter add(Record value);

  /* Advanced Array Methods */
  RecordWriter addNewArray();
  RecordWriter addNewMap();

  RecordWriter add(Time value);

  /**
   * Appends the specified {@code time} value represented as number of
   * milliseconds since midnight to the current array.
   *
   * @param  field The name of the field.
   * @param  value The {@code time} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in a ARRAY segment.
   * @throws IllegalArgumentException If the value of {@code millis} is greater
   *         than 86400000.
   */
  RecordWriter addTime(int millis);

  RecordWriter add(Date value);

  /**
   * Appends the specified {@code date} value represented as number of
   * days since epoch to the current array.
   *
   * @param  field The name of the field.
   * @param  value The {@code date} value to append.
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException If the writer is not in a ARRAY segment.
   */
  RecordWriter addDate(int days);

  RecordWriter add(Timestamp value);
  RecordWriter addTimestamp(long timeMillis);

  RecordWriter add(Interval value);
  RecordWriter addInterval(long durationInMs);

  /* Lifecycle methods */
  RecordWriter endArray();
  RecordWriter endMap();

  Record build();

}
