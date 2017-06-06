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
package org.ojai.store;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.ojai.Buildable;
import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.exceptions.TypeException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

@API.Public
@API.ImmutableOnBuild
public interface QueryCondition extends Buildable {

  public enum Op {
    /**
     * The Value at the specified path is less than the reference value.
     * </br></br>
     * Reference value type: All scalars {@link Type} i.e {@code
     * [NULL, BOOLEAN, STRING, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE,
     * DECIMAL, DATE, TIME, TIMESTAMP, INTERVAL, BINARY]}.
     */
    LESS,

    /**
     * The Value at the specified path is less than or equal to the
     * reference value.</br></br>
     * Reference value type: All scalars {@link Type} i.e {@code
     * [NULL, BOOLEAN, STRING, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE,
     * DECIMAL, DATE, TIME, TIMESTAMP, INTERVAL, BINARY]}.
     */
    LESS_OR_EQUAL,

    /**
     * The Value at the specified path is equal to the reference value.
     * </br></br>
     * Reference value type: All {@link org.ojai.Value.Type}.
     */
    EQUAL,

    /**
     * The Value at the specified path is not equal to the reference value.
     * </br></br>
     * Reference value type: All {@link org.ojai.Value.Type}.
     */
    NOT_EQUAL,

    /**
     * The Value at the specified path is greater than or equal to the
     * reference value.</br></br>
     * Reference value type: All scalars {@link Type} i.e {@code
     * [NULL, BOOLEAN, STRING, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE,
     * DECIMAL, DATE, TIME, TIMESTAMP, INTERVAL, BINARY]}.
     */
    GREATER_OR_EQUAL,

    /**
     * The Value at the specified path is greater than the reference value.
     * </br></br>
     * Reference value type: All scalars {@link Type} i.e {@code
     * [NULL, BOOLEAN, STRING, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE,
     * DECIMAL, DATE, TIME, TIMESTAMP, INTERVAL, BINARY]}.
     */
    GREATER;
  }

  /**
   * @return {@code true} if this condition is empty
   */
  public boolean isEmpty();

  /**
   * @return {@code true} if this condition is built
   */
  public boolean isBuilt();

  /**
   * Begins a new AND compound condition block.
   * @return {@code this} for chaining
   */
  public QueryCondition and();

  /**
   * Begins a new OR compound condition block.
   * @return {@code this} for chaining
   */
  public QueryCondition or();

  /**
   * Closes a compound condition block.
   * @return {@code this} for chaining
   */
  public QueryCondition close();

  /**
   * Closes all nested compound condition blocks.
   * @return {@code this}
   */
  public QueryCondition build();

  /**
   * Appends the specified condition to the current condition
   * block.
   * @return {@code this} for chaining
   */
  public QueryCondition condition(@NonNullable QueryCondition conditionToAdd);

  /**
   * Adds a condition that tests for existence of the specified
   * {@code FieldPath}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition exists(@NonNullable String path);

  /**
   * Adds a condition that tests for existence of the specified
   * {@code FieldPath}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition exists(@NonNullable FieldPath path);

  /**
   * Adds a condition that tests for non-existence of the specified
   * {@code FieldPath}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition notExists(@NonNullable String path);

  /**
   * Adds a condition that tests for non-existence of the specified
   * {@code FieldPath}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition notExists(@NonNullable FieldPath path);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is equal to at least one of the values in the
   * specified {@code List}.
   *
   * @param path the {@code FieldPath} to test
   * @param listOfValue the {@code List} of values to test against
   * @return {@code this} for chained invocation
   */
  public QueryCondition in(@NonNullable String path, @NonNullable List<? extends Object> listOfValue);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is equal to at least one of the values in the
   * specified {@code List}.
   *
   * @param path the {@code FieldPath} to test
   * @param listOfValue the {@code List} of values to test against
   * @return {@code this} for chained invocation
   */
  public QueryCondition in(@NonNullable FieldPath path, @NonNullable List<? extends Object> listOfValue);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is not equal to any of the values in the
   * specified {@code List}.
   *
   * @param path the {@code FieldPath} to test
   * @param listOfValue the {@code List} of values to test against
   * @return {@code this} for chained invocation
   */
  public QueryCondition notIn(@NonNullable String path, @NonNullable List<? extends Object> listOfValue);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is not equal to any of the values in the
   * specified {@code List}.
   *
   * @param path the {@code FieldPath} to test
   * @param listOfValue the {@code List} of values to test against
   * @return {@code this} for chained invocation
   */
  public QueryCondition notIn(@NonNullable FieldPath path, @NonNullable List<? extends Object> listOfValue);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is of the specified {@code Type}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition typeOf(@NonNullable String path, @NonNullable Value.Type type);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is of the specified {@code Type}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition typeOf(@NonNullable FieldPath path, @NonNullable Value.Type type);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is not of the specified {@code Type}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition notTypeOf(@NonNullable String path, @NonNullable Value.Type type);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is not of the specified {@code Type}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition notTypeOf(@NonNullable FieldPath path, @NonNullable Value.Type type);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and matches the specified regular
   * expression.
   *
   * @param path the {@code FieldPath} to test
   * @param regex the reference regular expression
   * @return {@code this} for chained invocation
   * @throws PatternSyntaxException if the expression's syntax is invalid
   */
  public QueryCondition matches(@NonNullable String path, @NonNullable String regex);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and matches the specified regular
   * expression.
   *
   * @param path the {@code FieldPath} to test
   * @param regex the reference regular expression
   * @return {@code this} for chained invocation
   * @throws PatternSyntaxException if the expression's syntax is invalid
   */
  public QueryCondition matches(@NonNullable FieldPath path, @NonNullable String regex);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and does not match the specified
   * regular expression.
   *
   * @param path the {@code FieldPath} to test
   * @param regex the reference regular expression
   * @return {@code this} for chained invocation
   * @throws PatternSyntaxException if the expression's syntax is invalid
   */
  public QueryCondition notMatches(@NonNullable String path, @NonNullable String regex);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and does not match the specified
   * regular expression.
   *
   * @param path the {@code FieldPath} to test
   * @param regex the reference regular expression
   * @return {@code this} for chained invocation
   * @throws PatternSyntaxException if the expression's syntax is invalid
   */
  public QueryCondition notMatches(@NonNullable FieldPath path, @NonNullable String regex);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and matches the specified SQL LIKE
   * expression.
   *
   * @param path the {@code FieldPath} to test
   * @param likeExpression the reference LIKE pattern
   * @return {@code this} for chained invocation
   */
  public QueryCondition like(@NonNullable String path, @NonNullable String likeExpression);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and matches the specified SQL LIKE
   * expression.
   *
   * @param path the {@code FieldPath} to test
   * @param likeExpression the reference LIKE pattern
   * @return {@code this} for chained invocation
   */
  public QueryCondition like(@NonNullable FieldPath path, @NonNullable String likeExpression);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and matches the specified SQL LIKE
   * expression optionally escaped with the specified escape character.
   *
   * @param path the {@code FieldPath} to test
   * @param likeExpression the reference LIKE pattern
   * @param escapeChar the escape character in the LIKE pattern
   * @return {@code this} for chained invocation
   */
  public QueryCondition like(@NonNullable String path,
      @NonNullable String likeExpression, @NonNullable Character escapeChar);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and matches the specified SQL LIKE
   * expression optionally escaped with the specified escape character.
   *
   * @param path the {@code FieldPath} to test
   * @param likeExpression the reference LIKE pattern
   * @param escapeChar the escape character in the LIKE pattern
   * @return {@code this} for chained invocation
   */
  public QueryCondition like(@NonNullable FieldPath path,
      @NonNullable String likeExpression, @NonNullable Character escapeChar);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and does not match the specified
   * SQL LIKE expression.
   *
   * @param path the {@code FieldPath} to test
   * @param likeExpression the reference LIKE pattern
   * @return {@code this} for chained invocation
   */
  public QueryCondition notLike(@NonNullable String path, @NonNullable String likeExpression);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and does not match the specified
   * SQL LIKE expression.
   *
   * @param path the {@code FieldPath} to test
   * @param likeExpression the reference LIKE pattern
   * @return {@code this} for chained invocation
   */
  public QueryCondition notLike(@NonNullable FieldPath path, @NonNullable String likeExpression);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and does not match the specified
   * SQL LIKE expression optionally escaped with the specified escape character.
   *
   * @param path the {@code FieldPath} to test
   * @param likeExpression the reference LIKE pattern
   * @param escapeChar the escape character in the LIKE pattern
   * @return {@code this} for chained invocation
   */
  public QueryCondition notLike(@NonNullable String path,
      @NonNullable String likeExpression, @NonNullable Character escapeChar);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and does not match the specified
   * SQL LIKE expression optionally escaped with the specified escape character.
   *
   * @param path the {@code FieldPath} to test
   * @param likeExpression the reference LIKE pattern
   * @param escapeChar the escape character in the LIKE pattern
   * @return {@code this} for chained invocation
   */
  public QueryCondition notLike(@NonNullable FieldPath path,
      @NonNullable String likeExpression, @NonNullable Character escapeChar);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code boolean} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference boolean {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, boolean value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code boolean} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference boolean {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, boolean value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code String} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference String {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, @NonNullable String value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code String} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference String {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, String value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code byte} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference byte {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, byte value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code byte} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference byte {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, byte value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code short} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference short {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, short value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code short} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference short {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, short value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code int} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference int {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, int value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code int} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference int {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, int value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code long} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference long {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, long value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code long} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference long {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, long value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code float} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference float {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, float value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code float} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference float {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, float value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code double} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference double {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, double value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code double} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference double {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, double value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code BigDecimal} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference BigDecimal {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, @NonNullable BigDecimal value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code BigDecimal} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference BigDecimal {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, @NonNullable BigDecimal value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Date} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference Date {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, @NonNullable ODate value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Date} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference Date {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, @NonNullable ODate value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Time} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference Time {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, @NonNullable OTime value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Time} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference Time {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, @NonNullable OTime value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Timestamp} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference Timestamp {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, @NonNullable OTimestamp value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Timestamp} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference Timestamp {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, @NonNullable OTimestamp value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Interval} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference Interval {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, @NonNullable OInterval value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Interval} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference Interval {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, @NonNullable OInterval value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code ByteBuffer} value. Only the byte sequence between
   * the {@code position} and {@code limit} in the {@code ByteBuffer} is
   * used as the reference value. The function does not alter the passed
   * ByteBuffer state or content.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference ByteBuffer {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable String path, @NonNullable Op op, @NonNullable ByteBuffer value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code ByteBuffer} value. Only the byte sequence between
   * the {@code position} and {@code limit} in the {@code ByteBuffer} is
   * used as the reference value. The function does not alter the passed
   * ByteBuffer state or content.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param value the reference ByteBuffer {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(@NonNullable FieldPath path, @NonNullable Op op, @NonNullable ByteBuffer value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} equals the specified {@code Map} value.
   * Two Maps are considered equal if and only if they contain the same
   * key-value pair in the same order.
   *
   * @param path the {@code FieldPath} to test
   * @param value the reference Map {@code Value}
   * @return {@code this} for chained invocation
   * @throws TypeException if a value at any level in the specified
   *         Map is not one of the {@code Value} types
   */
  public QueryCondition equals(@NonNullable String path, @NonNullable Map<String, ? extends Object> value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} equals the specified {@code Map} value.
   * Two Maps are considered equal if and only if they contain the same
   * key-value pair in the same order.
   *
   * @param path the {@code FieldPath} to test
   * @param value the reference Map {@code Value}
   * @return {@code this} for chained invocation
   * @throws TypeException if a value at any level in the specified
   *         Map is not one of the {@code Value} types
   */
  public QueryCondition equals(@NonNullable FieldPath path, @NonNullable Map<String, ? extends Object> value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} equals the specified {@code List} value.
   *
   * @param path the {@code FieldPath} to test
   * @param value the reference List {@code Value}
   * @return {@code this} for chained invocation
   * @throws TypeException if a value in the specified List is not one of
   *         the {@code Value} types
   */
  public QueryCondition equals(@NonNullable String path, @NonNullable List<? extends Object> value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} equals the specified {@code List} value.
   *
   * @param path the {@code FieldPath} to test
   * @param value the reference List {@code Value}
   * @return {@code this} for chained invocation
   * @throws TypeException if a value in the specified List is not one of
   *         the {@code Value} types
   */
  public QueryCondition equals(@NonNullable FieldPath path, @NonNullable List<? extends Object> value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} does not equal the specified {@code Map} value.
   * Two Maps are considered equal if and only if they contain the same
   * key-value pair in the same order.
   *
   * @param path the {@code FieldPath} to test
   * @param value the reference Map {@code Value}
   * @return {@code this} for chained invocation
   * @throws TypeException if a value at any level in the specified
   *         Map is not one of the {@code Value} types
   */
  public QueryCondition notEquals(@NonNullable String path, @NonNullable Map<String, ? extends Object> value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} does not equal the specified {@code Map} value.
   * Two Maps are considered equal if and only if they contain the same
   * key-value pair in the same order.
   *
   * @param path the {@code FieldPath} to test
   * @param value the reference Map {@code Value}
   * @return {@code this} for chained invocation
   * @throws TypeException if a value at any level in the specified
   *         Map is not one of the {@code Value} types
   */
  public QueryCondition notEquals(@NonNullable FieldPath path, @NonNullable Map<String, ? extends Object> value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} does not equal the specified {@code List} value.
   *
   * @param path the {@code FieldPath} to test
   * @param value the reference List {@code Value}
   * @return {@code this} for chained invocation
   * @throws TypeException if a value in the specified List is not one of
   *         the {@code Value} types
   */
  public QueryCondition notEquals(@NonNullable String path, @NonNullable List<? extends Object> value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} does not equal the specified {@code List} value.
   *
   * @param path the {@code FieldPath} to test
   * @param value the reference List {@code Value}
   * @return {@code this} for chained invocation
   * @throws TypeException if a value in the specified List is not one of
   *         the {@code Value} types
   */
  public QueryCondition notEquals(@NonNullable FieldPath path, @NonNullable List<? extends Object> value);

  /**
   * Adds a condition that tests if the size of the {@code Value} at the
   * specified {@code FieldPath} satisfies the given {@link Op} and the size.
   * The value must be one of the following types: {@code STRING},
   * {@code BINARY}, {@code MAP} or {@code ARRAY}.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param size the reference size of {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition sizeOf(@NonNullable String path, @NonNullable Op op, long size);

  /**
   * Adds a condition that tests if the size of the {@code Value} at the
   * specified {@code FieldPath} satisfies the given {@link Op} and the size.
   * The value must be one of the following types: {@code STRING},
   * {@code BINARY}, {@code MAP} or {@code ARRAY}.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code QueryCondition.Op} to apply
   * @param size the reference size of {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition sizeOf(@NonNullable FieldPath path, @NonNullable Op op, long size);

}
