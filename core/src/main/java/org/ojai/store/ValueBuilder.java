/**
 * Copyright (c) 2017 MapR, Inc.
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
package org.ojai.store;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

/**
 * Provides APIs to create instance of {@link Value} interface.
 */
@API.Public
@API.ThreadSafe
public interface ValueBuilder {

  /**
   * Returns a new {@link Value} of {@link Type#NULL}.
   */
  public Value newNullValue();

  /**
   * Returns a new {@link Value} of {@link Type#BOOLEAN} from the specified boolean value.
   */
  public Value newValue(boolean value);

  /**
   * Returns a new {@link Value} of {@link Type#STRING} from the specified String.
   */
  public Value newValue(@NonNullable String value);

  /**
   * Returns a new {@link Value} of {@link Type#BYTE} from the specified byte value.
   */
  public Value newValue(byte value);

  /**
   * Returns a new {@link Value} of {@link Type#SHORT} from the specified short value.
   */
  public Value newValue(short value);

  /**
   * Returns a new {@link Value} of {@link Type#INT} from the specified integer value.
   */
  public Value newValue(int value);

  /**
   * Returns a new {@link Value} of {@link Type#LONG} from the specified long value.
   */
  public Value newValue(long value);

  /**
   * Returns a new {@link Value} of {@link Type#FLOAT} from the specified float value.
   */
  public Value newValue(float value);

  /**
   * Returns a new {@link Value} of {@link Type#DOUBLE} from the specified double value.
   */
  public Value newValue(double value);

  /**
   * Returns a new {@link Value} of {@link Type#TIME} from the specified {@link OTime}.
   */
  public Value newValue(@NonNullable OTime value);

  /**
   * Returns a new {@link Value} of {@link Type#DATE} from the specified {@link ODate}.
   */
  public Value newValue(@NonNullable ODate value);

  /**
   * Returns a new {@link Value} of {@link Type#DECIMAL} from the specified {@link BigDecimal}.
   */
  public Value newValue(@NonNullable BigDecimal value);

  /**
   * Returns a new {@link Value} of {@link Type#TIMESTAMP} from the specified {@link OTimestamp}.
   */
  public Value newValue(@NonNullable OTimestamp value);

  /**
   * Returns a new {@link Value} of {@link Type#INTERVAL} from the specified {@link OInterval}.
   */
  public Value newValue(@NonNullable OInterval value);

  /**
   * Returns a new {@link Value} of {@link Type#BINARY} from the specified {@link ByteBuffer}.
   */
  public Value newValue(@NonNullable ByteBuffer value);

  /**
   * Returns a new {@link Value} of {@link Type#BINARY} from the specified byte array.
   */
  public Value newValue(@NonNullable byte[] value);

  /**
   * Returns a new {@link Value} of {@link Type#ARRAY} from the specified {@link List}.
   */
  public Value newValue(@NonNullable List<? extends Object> list);

  /**
   * Returns a new {@link Value} of {@link Type#MAP} from the specified {@link Map}.
   */
  public Value newValue(@NonNullable Map<String, ? extends Object> map);

}
