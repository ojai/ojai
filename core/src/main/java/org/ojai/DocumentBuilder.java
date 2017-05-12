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
import java.util.Map;

import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

@API.Public
public interface DocumentBuilder {

  /* ===========
   * Map Methods
   * ===========
   */

  /**
   * Associates the specified {@code boolean} value with the specified
   * {@code field} in the current map. Any previous association will be
   * overwritten.
   *
   * @param field the name of the field
   * @param value the {@code boolean} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in a MAP segment
   */
  DocumentBuilder put(@NonNullable String field, boolean value);

  /**
   * Associates the specified {@code String} value with the specified
   * {@code field} in the current map. Any previous association will be
   * overwritten.
   *
   * @param field the name of the field
   * @param value the {@code String} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in a MAP segment
   */
  DocumentBuilder put(@NonNullable String field, @NonNullable String value);

  DocumentBuilder put(@NonNullable String field, byte value);
  DocumentBuilder put(@NonNullable String field, short value);
  DocumentBuilder put(@NonNullable String field, int value);
  DocumentBuilder put(@NonNullable String field, long value);
  DocumentBuilder put(@NonNullable String field, float value);
  DocumentBuilder put(@NonNullable String field, double value);
  DocumentBuilder put(@NonNullable String field, @NonNullable BigDecimal value);
  DocumentBuilder putDecimal(@NonNullable String field, long decimalValue);
  DocumentBuilder putDecimal(@NonNullable String field, double decimalValue);
  DocumentBuilder putDecimal(@NonNullable String field, int unscaledValue, int scale);
  DocumentBuilder putDecimal(@NonNullable String field, long unscaledValue, int scale);
  DocumentBuilder putDecimal(@NonNullable String field, @NonNullable byte[] unscaledValue, int scale);

  DocumentBuilder put(@NonNullable String field, @NonNullable byte[] value);
  DocumentBuilder put(@NonNullable String field, @NonNullable byte[] value, int offset, int length);
  DocumentBuilder put(@NonNullable String field, @NonNullable ByteBuffer value);

  DocumentBuilder put(@NonNullable String field, @NonNullable ODate value);

  /**
   * Associates the specified {@code date} value represented as the number
   * of days since epoch with the specified {@code field} in the
   * current map. Any previous association will be overwritten.
   *
   * @param  field the name of the field
   * @param  days the {@code date} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in a MAP segment
   */
  DocumentBuilder putDate(@NonNullable String field, int days);

  DocumentBuilder put(@NonNullable String field, @NonNullable OTime value);

  /**
   * Associates the specified {@code time} value represented as number of
   * milliseconds since midnight with the specified {@code field} in the
   * current map. Any previous association will be overwritten.
   *
   * @param  field the name of the field
   * @param  millis the {@code time} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in a MAP segment
   * @throws IllegalArgumentException if the value of {@code millis} is greater
   *         than 86400000
   */
  DocumentBuilder putTime(@NonNullable String field, int millis);

  DocumentBuilder put(@NonNullable String field, @NonNullable OTimestamp value);

  /**
   * Associates the specified {@code timestamp} value represented as the number
   * of milliseconds since epoch with the specified {@code field} in the
   * current map. Any previous association will be overwritten.
   *
   * @param  field the name of the field
   * @param  timeMillis the {@code timestamp} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in a MAP segment
   */
  DocumentBuilder putTimestamp(@NonNullable String field, long timeMillis);

  DocumentBuilder put(@NonNullable String field, @NonNullable OInterval value);
  DocumentBuilder putInterval(@NonNullable String field, long durationInMs);
  DocumentBuilder putInterval(@NonNullable String field, int months, int days, int milliseconds);

  DocumentBuilder putNewMap(@NonNullable String field);
  DocumentBuilder putNewArray(@NonNullable String field);

  DocumentBuilder putNull(@NonNullable String field);

  DocumentBuilder put(@NonNullable String field, @NonNullable Value value);
  DocumentBuilder put(@NonNullable String field, @NonNullable Document value);
  DocumentBuilder put(@NonNullable String field, @NonNullable Map<String, Object> value);

  /* =============
   * Array Methods
   * =============
   */

  /**
   * Sets the index in the current array at which the next value will be added.
   *
   * @param index the index at which the next value will be added.
   * @return {@code this} for chained invocation
   *
   * @throws IllegalArgumentException if the index is not larger than the last
   *         written index.
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder setArrayIndex(int index);

  /**
   * Adds a {@code boolean} value at the current index in the current array and
   * advances the current index by 1.
   *
   * @param value the {@code boolean} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(boolean value);

  /**
   * Adds a {@code String} value at the current index in the current array and
   * advances the current index by 1.
   *
   * @param value the {@code String} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(@NonNullable String value);

  /**
   * Adds a {@code byte} value at the current index in the current array and
   * advances the current index by 1.
   *
   * @param value the {@code byte} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(byte value);

  /**
   * Adds a {@code short} value at the current index in the current array and
   * advances the current index by 1.
   *
   * @param value the {@code short} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(short value);

  /**
   * Adds an {@code int} value at the current index in the current array and
   * advances the current index by 1.
   *
   * @param value the {@code int} value to append
   * @return {@code this} for chained invocation.
   * @throws IllegalStateException if the builder is not in an ARRAY segment.
   */
  DocumentBuilder add(int value);

  /**
   * Adds a {@code long} value at the current index in the current array and
   * advances the current index by 1.
   *
   * @param value the {@code long} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(long value);

  /**
   * Adds a {@code float} value at the current index in the current array and
   * advances the current index by 1.
   *
   * @param value the {@code float} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(float value);

  /**
   * Adds a {@code double} value at the current index in the current array and
   * advances the current index by 1.
   *
   * @param value the {@code double} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(double value);

  /**
   * Adds a {@code BigDecimal} value at the current index in the current array
   * and advances the current index by 1.
   *
   * @param value the {@code BigDecimal} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(@NonNullable BigDecimal value);

  /**
   * Adds a long number as a {@code DECIMAL} value at the current index in the
   * current array and advances the current index by 1.
   *
   * @param decimalValue the {@code long} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder addDecimal(long decimalValue);

  /**
   * Adds a double number as a {@code DECIMAL} value at the current index in the
   * current array and advances the current index by 1.
   *
   * @param decimalValue the {@code double} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder addDecimal(double decimalValue);

  /**
   * Adds an {@code int} unscaled value and an {@code int} scale as a
   * {@code DECIMAL} value at the current index in the current array
   * and advances the current index by 1. The {@code DECIMAL} value is
   * <tt>(unscaledValue &times; 10<sup>-scale</sup>)</tt>.
   *
   * @param unscaledValue unscaled value of the {@code DECIMAL}
   * @param scale scale of the {@code DECIMAL}
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder addDecimal(int unscaledValue, int scale);

  /**
   * Adds an {@code long} unscaled value and an {@code int} scale as a
   * {@code DECIMAL} value at the current index in the current array
   * and advances the current index by 1. The {@code DECIMAL} value is
   * <tt>(unscaledValue &times; 10<sup>-scale</sup>)</tt>.
   *
   * @param unscaledValue unscaled value of the {@code DECIMAL}
   * @param scale scale of the {@code DECIMAL}
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder addDecimal(long unscaledValue, int scale);

  /**
   * Adds a byte array containing the two's complement binary representation
   * and an {@code int} scale as a {@code DECIMAL} value at the current index
   * in the current array and advances the current index by 1. The input array
   * is assumed to be in <i>big-endian</i> byte-order: the most significant
   * byte is in the zeroth element.
   *
   * @param unscaledValue unscaled value of the {@code DECIMAL}
   * @param scale scale of the {@code DECIMAL}
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder addDecimal(@NonNullable byte[] unscaledValue, int scale);

  /**
   * Appends the byte array as a {@code BINARY} value to the current array.
   * @param value the byte array to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(@NonNullable byte[] value);

  /**
   * Appends the byte array bounded by offset and length as a {@code BINARY}
   * value to the current array.
   * @param value the byte array to append
   * @param offset the start offset in the byte array
   * @param length the length from the offset
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   * @throws IndexOutOfBoundsException if the offset or offset+length are outside
   *         of byte array range
   */
  DocumentBuilder add(@NonNullable byte[] value, int offset, int length);

  /**
   * Appends the {@code ByteBuffer} as a {@code BINARY} value to the current array.
   * @param value the {@code ByteBuffer} to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(@NonNullable ByteBuffer value);

  /**
   * Appends a {@code NULL} value to the current array.
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder addNull();

  /**
   * Appends the {@code Value} to the current array.
   * @param value the {@code Value} to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(@NonNullable Value value);

  /**
   * Appends the {@code Document} to the current array.
   * @param value the {@code Document} to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in an ARRAY segment
   */
  DocumentBuilder add(@NonNullable Document value);

  /* Advanced Array Methods */
  DocumentBuilder addNewArray();
  DocumentBuilder addNewMap();

  DocumentBuilder add(@NonNullable OTime value);

  /**
   * Appends the specified {@code time} value represented as number of
   * milliseconds since midnight to the current array.
   *
   * @param  millis the {@code time} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in a ARRAY segment
   * @throws IllegalArgumentException if the value of {@code millis} is greater
   *         than 86400000
   */
  DocumentBuilder addTime(int millis);

  DocumentBuilder add(@NonNullable ODate value);

  /**
   * Appends the specified {@code date} value represented as the number of
   * days since epoch to the current array.
   *
   * @param  days the {@code date} value to append
   * @return {@code this} for chained invocation
   * @throws IllegalStateException if the builder is not in a ARRAY segment
   */
  DocumentBuilder addDate(int days);

  DocumentBuilder add(@NonNullable OTimestamp value);
  DocumentBuilder addTimestamp(long timeMillis);

  DocumentBuilder add(@NonNullable OInterval value);
  DocumentBuilder addInterval(long durationInMs);

  /* Lifecycle methods */
  DocumentBuilder endArray();
  DocumentBuilder endMap();

  Document getDocument();

}
