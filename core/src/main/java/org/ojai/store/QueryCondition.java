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
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.exceptions.TypeException;
import org.ojai.types.Interval;

public interface QueryCondition {

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
  public QueryCondition condition(QueryCondition conditionToAdd);

  /**
   * Adds a condition that tests for existence of the specified
   * {@code FieldPath}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition exists(String path);

  /**
   * Adds a condition that tests for existence of the specified
   * {@code FieldPath}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition exists(FieldPath path);

  /**
   * Adds a condition that tests for non-existence of the specified
   * {@code FieldPath}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition notExists(String path);

  /**
   * Adds a condition that tests for non-existence of the specified
   * {@code FieldPath}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition notExists(FieldPath path);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is equal to at least one of the values in the
   * specified {@code List}.
   *
   * @param path the {@code FieldPath} to test
   * @param listOfValue the {@code List} of values to test against
   * @return {@code this} for chained invocation
   */
  public QueryCondition in(String path, List<? extends Object> listOfValue);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is equal to at least one of the values in the
   * specified {@code List}.
   *
   * @param path the {@code FieldPath} to test
   * @param listOfValue the {@code List} of values to test against
   * @return {@code this} for chained invocation
   */
  public QueryCondition in(FieldPath path, List<? extends Object> listOfValue);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is not equal to any of the values in the
   * specified {@code List}.
   *
   * @param path the {@code FieldPath} to test
   * @param listOfValue the {@code List} of values to test against
   * @return {@code this} for chained invocation
   */
  public QueryCondition notIn(String path, List<? extends Object> listOfValue);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is not equal to any of the values in the
   * specified {@code List}.
   *
   * @param path the {@code FieldPath} to test
   * @param listOfValue the {@code List} of values to test against
   * @return {@code this} for chained invocation
   */
  public QueryCondition notIn(FieldPath path, List<? extends Object> listOfValue);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is of the specified {@code Type}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition typeOf(String path, Value.Type type);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is of the specified {@code Type}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition typeOf(FieldPath path, Value.Type type);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is not of the specified {@code Type}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition notTypeOf(String path, Value.Type type);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is not of the specified {@code Type}.
   *
   * @param path the {@code FieldPath} to test
   * @return {@code this} for chained invocation
   */
  public QueryCondition notTypeOf(FieldPath path, Value.Type type);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and matches the specified regular
   * expression.
   *
   * @param path the {@code FieldPath} to test
   * @param regex the reference regular expression
   * @return {@code this} for chained invocation
   */
  public QueryCondition matches(String path, String regex);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and matches the specified regular
   * expression.
   *
   * @param path the {@code FieldPath} to test
   * @param regex the reference regular expression
   * @return {@code this} for chained invocation
   */
  public QueryCondition matches(FieldPath path, String regex);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and does not match the specified
   * regular expression.
   *
   * @param path the {@code FieldPath} to test
   * @param regex the reference regular expression
   * @return {@code this} for chained invocation
   */
  public QueryCondition notMatches(String path, String regex);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} is a String and does not match the specified
   * regular expression.
   *
   * @param path the {@code FieldPath} to test
   * @param regex the reference regular expression
   * @return {@code this} for chained invocation
   */
  public QueryCondition notMatches(FieldPath path, String regex);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code boolean} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference boolean {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, boolean value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code boolean} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference boolean {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, boolean value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code String} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference String {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, String value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code String} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference String {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, String value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code byte} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference byte {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, byte value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code byte} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference byte {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, byte value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code short} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference short {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, short value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code short} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference short {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, short value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code int} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference int {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, int value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code int} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference int {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, int value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code long} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference long {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, long value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code long} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference long {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, long value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code float} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference float {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, float value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code float} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference float {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, float value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code double} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference double {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, double value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code double} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference double {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, double value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code BigDecimal} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference BigDecimal {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, BigDecimal value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code BigDecimal} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference BigDecimal {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, BigDecimal value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Date} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference Date {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, Date value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Date} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference Date {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, Date value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Time} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference Time {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, Time value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Time} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference Time {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, Time value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Timestamp} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference Timestamp {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, Timestamp value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Timestamp} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference Timestamp {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, Timestamp value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Interval} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference Interval {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, Interval value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code Interval} value.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference Interval {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, Interval value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code ByteBuffer} value. Only the byte sequence between
   * the {@code position} and {@code limit} in the {@code ByteBuffer} is
   * used as the reference value. The function does not alter the passed
   * ByteBuffer state or content.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference ByteBuffer {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(String path, Op op, ByteBuffer value);

  /**
   * Adds a condition that tests if the {@code Value} at the specified
   * {@code FieldPath} satisfies the given {@link Op} against
   * the specified {@code ByteBuffer} value. Only the byte sequence between
   * the {@code position} and {@code limit} in the {@code ByteBuffer} is
   * used as the reference value. The function does not alter the passed
   * ByteBuffer state or content.
   *
   * @param path the {@code FieldPath} to test
   * @param op the {@code ConditionOp} to apply
   * @param value the reference ByteBuffer {@code Value}
   * @return {@code this} for chained invocation
   */
  public QueryCondition is(FieldPath path, Op op, ByteBuffer value);

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
  public QueryCondition equals(String path, Map<String, ? extends Object> value);

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
  public QueryCondition equals(FieldPath path, Map<String, ? extends Object> value);

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
  public QueryCondition equals(String path, List<? extends Object> value);

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
  public QueryCondition equals(FieldPath path, List<? extends Object> value);

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
  public QueryCondition notEquals(String path, Map<String, ? extends Object> value);

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
  public QueryCondition notEquals(FieldPath path, Map<String, ? extends Object> value);

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
  public QueryCondition notEquals(String path, List<? extends Object> value);

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
  public QueryCondition notEquals(FieldPath path, List<? extends Object> value);

}
