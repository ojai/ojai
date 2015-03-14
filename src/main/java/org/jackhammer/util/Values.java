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
package org.jackhammer.util;

import java.math.BigDecimal;

import org.jackhammer.Value;
import org.jackhammer.exceptions.TypeException;

/**
 * A helper class which provide convenience methods
 * to operate on a {@code Value}.
 */
public class Values {

  public static byte asByte(Value value) {
    return asNumber(value).byteValue();
  }

  public static short asShort(Value value) {
    return asNumber(value).shortValue();
  }

  public static int asInt(Value value) {
    return asNumber(value).intValue();
  }

  public static long asLong(Value value) {
    return asNumber(value).longValue();
  }

  public static float asFloat(Value value) {
    return asNumber(value).floatValue();
  }

  public static double asDouble(Value value) {
    return asNumber(value).doubleValue();
  }

  public static BigDecimal asDecimal(Value value) {
    Number val = asNumber(value);
    return (val instanceof BigDecimal) 
        ? (BigDecimal) val
        : ((val instanceof Long) 
            ? new BigDecimal(val.longValue()) 
            : new BigDecimal(val.doubleValue()));
  }

  public static Number asNumber(Value value) {
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

}
