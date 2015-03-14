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

import java.util.Map;

import org.jackhammer.Value;
import org.jackhammer.Value.Type;

import com.google.common.collect.Maps;

/**
 * A helper class which provide convenience methods
 * to operate on a {@code Type}.
 */
public class Types {

  /**
   * The map between type and their tag names for the non intrinsic JSON types.
   */
  private static final Map<Type, String> TYPE_TAG_MAP = Maps.newEnumMap(Type.class);
  static {
    TYPE_TAG_MAP.put(Type.BYTE, "$byte");
    TYPE_TAG_MAP.put(Type.SHORT, "$short");
    TYPE_TAG_MAP.put(Type.INT, "$int");
    TYPE_TAG_MAP.put(Type.FLOAT, "$float");
    TYPE_TAG_MAP.put(Type.DECIMAL, "$decimal");
    TYPE_TAG_MAP.put(Type.DATE, "$date");
    TYPE_TAG_MAP.put(Type.TIME, "$time");
    TYPE_TAG_MAP.put(Type.TIMESTAMP, "$timestamp");
    TYPE_TAG_MAP.put(Type.INTERVAL, "$interval");
    TYPE_TAG_MAP.put(Type.BINARY, "$binary");
    assert TYPE_TAG_MAP.size() == (Type.values().length - (7 /*Json intrinsic types*/))
        : "Map is missing some of the Type enum elements";
  }

  /**
   * @param value A <code>Value</code> which should tested.
   * @return {@code true} if the given value is not of an intrinsic
   *         JSON types.
   */
  public static boolean isExtendedType(Value value) {
    return isExtendedType(value.getType());
  }

  /**
   * @param type A <code>Type</code> which should tested.
   * @return {@code true} if the given type is not an intrinsic
   *         JSON types.
   */
  public static boolean isExtendedType(Type type) {
    return TYPE_TAG_MAP.containsKey(type);
  }

  /**
   * @param value A <code>Value</code> whose tag name should be returned.
   * @return A {@code String} representing the extended tag name of the
   *         {@code Type} of the given value, if the type is not of an
   *         intrinsic JSON types, {@code null} otherwise.
   */
  public static String getTypeTag(Value value) {
    return getTypeTag(value.getType());
  }

  /**
   * @param type A <code>Type</code> whose tag name should be returned.
   * @return A {@code String} representing the extended tag name of the
   *         given {@code Type} if the type is not of an intrinsic JSON
   *         types, {@code null} otherwise.
   */
  public static String getTypeTag(Type type) {
    return TYPE_TAG_MAP.get(type);
  }

}
