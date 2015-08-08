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
package org.argonaut.util;

import java.util.Map;

import org.argonaut.Value;
import org.argonaut.RecordReader.EventType;
import org.argonaut.Value.Type;
import org.argonaut.annotation.API;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumBiMap;
import com.google.common.collect.Maps;

/**
 * A helper class which provide convenience methods
 * to operate on a {@code Type}.
 */
@API.Public
public class Types {

  public static final String TAG_BINARY     = "$binary";
  public static final String TAG_INTERVAL   = "$interval";
  public static final String TAG_TIMESTAMP  = "$date";
  public static final String TAG_TIME       = "$time";
  public static final String TAG_DATE       = "$dateDay";
  public static final String TAG_DECIMAL    = "$decimal";
  public static final String TAG_LONG        = "$numberLong";
  public static final String TAG_BYTE       = TAG_LONG;
  public static final String TAG_SHORT      = TAG_LONG;
  public static final String TAG_INT        = TAG_LONG;

  /**
   * The map between type and their tag names for the non intrinsic JSON types.
   */
  private static final Map<Type, String> TYPE_TAG_MAP = Maps.newEnumMap(Type.class);
  static {
    TYPE_TAG_MAP.put(Type.BYTE, TAG_BYTE);
    TYPE_TAG_MAP.put(Type.SHORT, TAG_SHORT);
    TYPE_TAG_MAP.put(Type.INT, TAG_INT);
    TYPE_TAG_MAP.put(Type.LONG, TAG_LONG);
    TYPE_TAG_MAP.put(Type.DECIMAL, TAG_DECIMAL);
    TYPE_TAG_MAP.put(Type.DATE, TAG_DATE);
    TYPE_TAG_MAP.put(Type.TIME, TAG_TIME);
    TYPE_TAG_MAP.put(Type.TIMESTAMP, TAG_TIMESTAMP);
    TYPE_TAG_MAP.put(Type.INTERVAL, TAG_INTERVAL);
    TYPE_TAG_MAP.put(Type.BINARY, TAG_BINARY);

    assert TYPE_TAG_MAP.size() == (Type.values().length - (7 /*Json intrinsic types*/))
        : "Map is missing some of the Type enum elements";
  }

  public static final BiMap<Type, EventType> TYPE_EVENTTYPE_MAP =
      EnumBiMap.create(Type.class, EventType.class);
  static {
    TYPE_EVENTTYPE_MAP.put(Type.NULL, EventType.NULL);
    TYPE_EVENTTYPE_MAP.put(Type.BOOLEAN, EventType.BOOLEAN);
    TYPE_EVENTTYPE_MAP.put(Type.STRING, EventType.STRING);
    TYPE_EVENTTYPE_MAP.put(Type.BYTE, EventType.BYTE);
    TYPE_EVENTTYPE_MAP.put(Type.SHORT, EventType.SHORT);
    TYPE_EVENTTYPE_MAP.put(Type.INT, EventType.INT);
    TYPE_EVENTTYPE_MAP.put(Type.LONG, EventType.LONG);
    TYPE_EVENTTYPE_MAP.put(Type.FLOAT, EventType.FLOAT);
    TYPE_EVENTTYPE_MAP.put(Type.DOUBLE, EventType.DOUBLE);
    TYPE_EVENTTYPE_MAP.put(Type.DECIMAL, EventType.DECIMAL);
    TYPE_EVENTTYPE_MAP.put(Type.DATE, EventType.DATE);
    TYPE_EVENTTYPE_MAP.put(Type.TIME, EventType.TIME);
    TYPE_EVENTTYPE_MAP.put(Type.TIMESTAMP, EventType.TIMESTAMP);
    TYPE_EVENTTYPE_MAP.put(Type.INTERVAL, EventType.INTERVAL);
    TYPE_EVENTTYPE_MAP.put(Type.BINARY, EventType.BINARY);
    assert TYPE_EVENTTYPE_MAP.size() == (Type.values().length - (2 /*Map and Array*/))
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

  /**
   * Returns the {@code EventType} for the specified {@code Type}.
   * @param type The {@code Type} to lookup
   * @return The corresponding {@code EventType}
   */
  public static EventType getEventTypeForType(Type type) {
    return TYPE_EVENTTYPE_MAP.get(type);
  }

  /**
   * Returns the {@code Type} for the specified {@code EventType}.
   * @param event The {@code EventType} to lookup
   * @return The corresponding {@code Type}
   */
  public static Type getTypeForEventType(EventType event) {
    return TYPE_EVENTTYPE_MAP.inverse().get(event);
  }

}
