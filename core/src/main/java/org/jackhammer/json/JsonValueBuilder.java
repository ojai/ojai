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
package org.jackhammer.json;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.jackhammer.Record;
import org.jackhammer.Value;
import org.jackhammer.Value.Type;
import org.jackhammer.annotation.API;
import org.jackhammer.exceptions.TypeException;
import org.jackhammer.types.Interval;
import org.jackhammer.util.Constants;

/**
 * Helper class providing set of static methods to create instance of
 * JsonValue class from different input types
 */
@API.Internal
public class JsonValueBuilder implements Constants {

  public static JsonValue initFromNull() {
    return JsonValue.NULLKEYVALUE;
  }

  public static JsonValue initFrom(boolean value) {
    JsonValue v = new JsonValue(Type.BOOLEAN);
    v.setPrimValue((value) ? 1 : 0);
    return v;
  }

  public static JsonValue initFrom(String value) {
    JsonValue v = new JsonValue(Type.STRING);
    v.setObjValue(value);
    return v;
  }

  public static JsonValue initFrom(byte value) {
    JsonValue v = new JsonValue(Type.BYTE);
    v.setPrimValue(value);
    return v;
  }

  public static JsonValue initFrom(short value) {
    JsonValue v = new JsonValue(Type.SHORT);
    v.setPrimValue(value);
    return v;
  }

  public static JsonValue initFrom(int value) {
    JsonValue v = new JsonValue(Type.INT);
    v.setPrimValue(value);
    return v;
  }

  public static JsonValue initFrom(long value) {
    JsonValue v = new JsonValue(Type.LONG);
    v.setPrimValue(value);
    return v;
  }

  public static JsonValue initFrom(float value) {
    JsonValue v = new JsonValue(Type.FLOAT);
    v.setPrimValue(Float.floatToIntBits(value));
    return v;
  }

  public static JsonValue initFrom(double value) {
    JsonValue v = new JsonValue(Type.DOUBLE);
    v.setPrimValue(Double.doubleToLongBits(value));
    return v;
  }

  public static JsonValue initFrom(Time value) {
    JsonValue v = new JsonValue(Type.TIME);
    /* just take the portion of time which is after midnight */
    v.setPrimValue((int) (value.getTime() % MILLISECONDSPERDAY));
    return v;
  }

  public static JsonValue initFrom(Date value) {
    JsonValue v = new JsonValue(Type.DATE);
    /* just take the portion of time which has completed days */
    v.setPrimValue((int) (value.getTime() / MILLISECONDSPERDAY));
    return v;
  }

  public static JsonValue initFrom(BigDecimal value) {
    JsonValue v = new JsonValue(Type.DECIMAL);
    v.setObjValue(value);
    return v;
  }

  // NOTE : We are ignoring the nano part of the timestamp here
  public static JsonValue initFrom(Timestamp value) {
    JsonValue v = new JsonValue(Type.TIMESTAMP);
    v.setPrimValue(value.getTime());
    return v;
  }

  public static JsonValue initFrom(Interval value) {
    JsonValue v = new JsonValue(Type.INTERVAL);
    v.setPrimValue(value.getInterval());
    return v;
  }

  public static JsonValue initFrom(ByteBuffer value) {
    JsonValue v = new JsonValue(Type.BINARY);
    v.setObjValue(value);
    return v;
  }

  public static<T extends Object> JsonValue initFrom(List<T> list) {
    if (list instanceof JsonList) {
      return (JsonValue) list;
    }

    JsonList l = new JsonList();
    for (Object o : list) {
      l.addToList(initFromObject(o));
    }
    return l;
  }

  public static <T extends Object> JsonValue initFrom(Map<String, T> map) {
    // We can't put the JsonRecord directly into the map as
    // it could be part of some other document as well and we
    // will be putting information like order of field in each
    // keyvalue which would change based on which document tree
    // this value is part of. Hence make a shallow copy of the
    // value before return it.
    if (map instanceof JsonRecord) {
      return ((JsonRecord) map).shallowCopy();
    }

    JsonRecord rec = new JsonRecord();
    for (String k : map.keySet()) {
      rec.set(k, initFromObject(map.get(k)));
      //rec.put(k, map.get(k));
    }
    return rec;
  }

  public static JsonValue initFrom(Record value) {
    if (value instanceof JsonRecord) {
      return ((JsonRecord) value).shallowCopy();
    }

    JsonRecord r = new JsonRecord();
    for (Map.Entry<String, Value> e : value) {
      r.set(e.getKey(), initFromObject(e.getValue()));
    }
    return r;
  }

  public static JsonValue initFrom(Value value) {
    if (value instanceof JsonValue) {
      return ((JsonValue) value).shallowCopy();
    }
    return initFromObject(value.getObject());
  }

  @SuppressWarnings({ "unchecked" })
  public static JsonValue initFromObject(Object value) {
    if (value instanceof JsonValue) {
      return (JsonValue) value;
    }

    // If the passed value is null the convert it to NULL type
    if (value == null) {
      return JsonValue.NULLKEYVALUE;
    }

    /* based on the type of object initialize the value */
    if (value instanceof Byte) {
      return initFrom(((Byte) value).byteValue());
    }

    if (value instanceof Boolean) {
      return initFrom(((Boolean) value).booleanValue());
    }

    if (value instanceof String) {
      return initFrom((String) value);
    }

    if (value instanceof Short) {
      return initFrom(((Short) value).shortValue());
    }

    if (value instanceof Integer) {
      return initFrom(((Integer) value).intValue());
    }

    if (value instanceof Long) {
      return initFrom(((Long) value).longValue());
    }

    if (value instanceof Float) {
      return initFrom(((Float) value).floatValue());
    }

    if (value instanceof Double) {
      return initFrom(((Double) value).doubleValue());
    }

    if (value instanceof Time) {
      return initFrom(((Time) value));
    }

    if (value instanceof Date) {
      return initFrom(((Date) value));
    }

    if (value instanceof BigDecimal) {
      return initFrom(((BigDecimal) value));
    }

    if (value instanceof ByteBuffer) {
      return initFrom(((ByteBuffer) value));
    }

    if (value instanceof Interval) {
      return initFrom(((Interval) value));
    }

    if (value instanceof Record) {
      return initFrom(((Record) value));
    }

    if (value instanceof Map) {
      return initFrom((Map<String, Object>) value);
    }

    if (value instanceof List<?>) {
      return initFrom((List<Object>) value);
    }

    // Its an array of primitive or object type
    if (value.getClass().isArray()) {
      return initFromArray(value);
    }

    throw new TypeException("Unsupported object type for value " + value);
  }

  public static JsonValue initFromArray(Object value) {
    Class<?> c = value.getClass().getComponentType();
    if (c.isPrimitive()) {
      if (c == byte.class) {
        byte[] v = (byte[]) value;
        return initFromArray(v);
      }
      if (c == short.class) {
        short[] v = (short[]) value;
        return initFromArray(v);
      }
      if (c == int.class) {
        int[] v = (int[]) value;
        return initFromArray(v);
      }
      if (c == long.class) {
        long[] v = (long[]) value;
        return initFromArray(v);
      }
      if (c == float.class) {
        float[] v = (float[]) value;
        return initFromArray(v);
      }
      if (c == double.class) {
        double[] v = (double[]) value;
        return initFromArray(v);
      }
      if (c == boolean.class) {
        boolean[] v = (boolean[]) value;
        return initFromArray(v);
      }
      if (c == char.class) {
        char[] v = (char[]) value;
        return initFromArray(v);
      }
    } else {
      return initFromArray((Object [])value);
    }
    return null;
  }

  private static JsonValue initFromArray(byte[] values) {
    JsonList list = new JsonList();
    for (int i = 0; i < values.length; ++i) {
      list.addToList(JsonValueBuilder.initFrom(values[i]));
    }
    return list;
  }

  private static JsonValue initFromArray(short[] values) {
    JsonList list = new JsonList();
    for (int i = 0; i < values.length; ++i) {
      list.addToList(JsonValueBuilder.initFrom(values[i]));
    }
    return list;
  }

  private static JsonValue initFromArray(int[] values) {
    JsonList list = new JsonList();
    for (int i = 0; i < values.length; ++i) {
      list.addToList(JsonValueBuilder.initFrom(values[i]));
    }
    return list;
  }

  private static JsonValue initFromArray(long[] values) {
    JsonList list = new JsonList();
    for (int i = 0; i < values.length; ++i) {
      list.addToList(JsonValueBuilder.initFrom(values[i]));
    }
    return list;
  }

  private static JsonValue initFromArray(float[] values) {
    JsonList list = new JsonList();
    for (int i = 0; i < values.length; ++i) {
      list.addToList(JsonValueBuilder.initFrom(values[i]));
    }
    return list;
  }

  private static JsonValue initFromArray(double[] values) {
    JsonList list = new JsonList();
    for (int i = 0; i < values.length; ++i) {
      list.addToList(JsonValueBuilder.initFrom(values[i]));
    }
    return list;
  }

  private static JsonValue initFromArray(boolean[] values) {
    JsonList list = new JsonList();
    for (int i = 0; i < values.length; ++i) {
      list.addToList(JsonValueBuilder.initFrom(values[i]));
    }
    return list;
  }

  private static JsonValue initFromArray(char[] values) {
    JsonList list = new JsonList();
    for (int i = 0; i < values.length; ++i) {
      list.addToList(JsonValueBuilder.initFrom((short)values[i]));
    }
    return list;
  }

  private static JsonValue initFromArray(Object[] values) {
    JsonList list = new JsonList();
    for (int i = 0; i < values.length; ++i) {
      list.addToList(JsonValueBuilder.initFromObject(values[i]));
    }
    return list;
  }

}
