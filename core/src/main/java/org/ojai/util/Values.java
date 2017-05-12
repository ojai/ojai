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
package org.ojai.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.exceptions.TypeException;
import org.ojai.json.Json;
import org.ojai.json.JsonOptions;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;

/**
 * A helper class that provides convenience methods
 * to operate on a {@code Value}.
 */
@API.Public
public class Values {

  /**
   * @return The specified value as a <code>byte</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static byte asByte(@NonNullable Value value) {
    return asNumber(value).byteValue();
  }

  /**
   * @return The specified value as a <code>short</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static short asShort(@NonNullable Value value) {
    return asNumber(value).shortValue();
  }

  /**
   * @return The specified value as an <code>int</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static int asInt(@NonNullable Value value) {
    return asNumber(value).intValue();
  }

  /**
   * @return The specified value as a <code>long</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static long asLong(Value value) {
    return asNumber(value).longValue();
  }

  /**
   * @return The specified value as a <code>float</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static float asFloat(Value value) {
    return asNumber(value).floatValue();
  }

  /**
   * @return The specified value as a <code>double</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static double asDouble(Value value) {
    return asNumber(value).doubleValue();
  }

  /**
   * @return The specified value as a <code>BigDecimal</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException uf the specified value is
   * not one of the number types
   */
  public static BigDecimal asDecimal(Value value) {
    Number val = asNumber(value);
    return (val instanceof BigDecimal)
        ? (BigDecimal) val
            : ((val instanceof Long)
                ? new BigDecimal(val.longValue())
            : new BigDecimal(val.doubleValue()));
  }

  /**
   * @return The specified value as a <code>Number</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static Number asNumber(@NonNullable Value value) {
    Preconditions.checkNotNull(value);
    switch (value.getType()) {
    case BYTE:
      return value.getByte();
    case SHORT:
      return value.getShort();
    case INT:
      return value.getInt();
    case LONG:
      return value.getLong();
    case FLOAT:
      return value.getFloat();
    case DOUBLE:
      return value.getDouble();
    case DECIMAL:
      return value.getDecimal();
    default:
      throw new TypeException(value.getType() + " can not be converted to a Number.");
    }
  }

  /**
   * Converts a string to BigDecimal object.
   */
  public static BigDecimal parseBigDecimal(@NonNullable String s) {
    return new BigDecimal(s);
  }

  /**
   * Converts a base-64 encoded string to ByteBuffer.
   */
  public static ByteBuffer parseBinary(@NonNullable String s) {
    return ByteBuffer.wrap(BaseEncoding.base64().decode(s));
  }

  /**
   * Converts a {@code Value} to its extended JSON representation.<br/><br/>
   * The 7 intrinsic types, &nbsp;{@code null, boolean, string, long,
   * double, array, and map }, are represented in regular JSON. The
   * extended types are converted to a singleton map with the type tag name
   * as the key and the value of the given {@code Value} as its value.
   * The following sample illustrates the string representation of the
   * various types.
   *
   * <pre>
   * {
   *   "map": {
   *     "null": null,
   *     "boolean" : true,
   *     "string": "eureka",
   *     "byte" : {"$numberLong": 127},
   *     "short": {"$numberLong": 32767},
   *     "int": {"$numberLong": 2147483647},
   *     "long": {"$numberLong":9223372036854775807},
   *     "float" : 3.4028235E38,
   *     "double" : 1.7976931348623157e308,
   *     "decimal": {"$decimal": "12345678901234567890189012345678901.23456789"},
   *     "date": {"$dateDay": "&lt;yyyy-mm-dd&gt;"},
   *     "time" : {"$time" : "&lt;HH:mm:ss[.sss]&gt;"},
   *     "timestamp" : {"$date" : "&lt;yyyy-MM-ddTHH:mm:ss.SSSXXX&gt;"},
   *     "interval" : {"$interval" : &lt;number_of_millisecods&gt;},
   *     "binary" : {"$binary" : "&lt;base64_encoded_binary_value&gt;"},
   *     "array" : [42, "open sesame", 3.14, {"$dateDay": "2015-01-21"}]
   *   }
   * }
   * </pre>
   *
   * @param value a <code>Value</code> that should be converted to JSON string
   * @return The extended JSON representation of the given value
   */
  public static String asJsonString(@NonNullable Value value) {
    return Json.toJsonString(value.asReader(), JsonOptions.WITH_TAGS);
  }

}
