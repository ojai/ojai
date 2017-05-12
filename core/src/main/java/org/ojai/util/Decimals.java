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
import java.math.BigInteger;

import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;

/**
 * Utility class with functions for data type conversions.
 */
@API.Public
public class Decimals {

  public static BigDecimal convertIntToDecimal(int value, int scale) {
    return new BigDecimal(BigInteger.valueOf(value), scale);
  }

  public static BigDecimal convertLongToDecimal(long value, int scale) {
    return new BigDecimal(BigInteger.valueOf(value), scale);
  }

  public static BigDecimal convertByteToBigDecimal(@NonNullable byte[] value, int scale) {
    return new BigDecimal(new BigInteger(value), scale);
  }

}
