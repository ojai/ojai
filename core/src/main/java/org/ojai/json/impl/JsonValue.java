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
package org.ojai.json.impl;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.ojai.DocumentReader;
import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.exceptions.TypeException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.util.Values;

@API.Internal
public class JsonValue implements Value {

  Type valueType;
  long jsonValue;
  Object objValue;

  String key;

  JsonValue() {
  }

  JsonValue(Type t) {
    valueType = t;
  }

  JsonValue(Object o, Type t) {
    objValue = o;
    valueType = t;
  }

  JsonValue(boolean bool, Type t) {
    valueType = t;
    jsonValue = bool ? 1 : 0;
  }

  JsonValue(long l, Type t) {
    jsonValue = l;
    valueType = t;
  }

  JsonValue(int l, Type t) {
    jsonValue = l;
    valueType = t;
  }

  void setKey(String k) {
    key = k;
  }

  String getKey() {
    return key;
  }

  private void checkType(Type t) throws TypeException {
    if (valueType != t) {
      throw new TypeException("Value is of type " + valueType
          + " but requested type is " + t);
    }
    return;
  }

  void setPrimValue(long value) {
    this.jsonValue = value;
  }

  void setObjValue(Object value) {
    this.objValue = value;
  }

  @Override
  public Object getObject() {
    switch (valueType) {
    case BOOLEAN: {
      return new Boolean(getBoolean());
    }
    case BYTE: {
      return new Byte(getByte());
    }
    case SHORT: {
      return new Short(getShort());
    }
    case INT: {
      return new Integer(getInt());
    }
    case LONG: {
      return new Long(getLong());
    }
    case FLOAT: {
      return new Float(getFloat());
    }
    case DOUBLE: {
      return new Double(getDouble());
    }
    case TIME: {
      return getTime();
    }
    case TIMESTAMP: {
      return getTimestamp();
    }
    case DATE: {
      return getDate();
    }
    case INTERVAL: {
      return getInterval();
    }
    case BINARY:
    case DECIMAL:
    case STRING:
    case NULL:
      return objValue;
    case MAP:
    case ARRAY:
      return this;
    }
    throw new TypeException("Invalid type " + valueType);
  }

  @Override
  public Type getType() {
    return valueType;
  }

  @Override
  public byte getByte() {
    switch(valueType) {
    case BYTE:
      return (byte) (jsonValue & 0xff);
    case SHORT:
      return (byte) getShort();
    case INT:
      return (byte) getInt();
    case LONG:
      return (byte) getLong();
    case FLOAT:
      return (byte) getFloat();
    case DOUBLE:
      return (byte) getDouble();
    case DECIMAL:
      return ((BigDecimal) objValue).byteValue();
    default:
      throw new TypeException("Expected a numeric type, found: " + valueType);
    }
  }

  @Override
  public short getShort() {
    switch(valueType) {
    case SHORT:
      return (short) (jsonValue & 0xffff);
    case BYTE:
      return getByte();
    case INT:
      return (short) getInt();
    case LONG:
      return (short) getLong();
    case FLOAT:
      return (short) getFloat();
    case DOUBLE:
      return (short) getDouble();
    case DECIMAL:
      return ((BigDecimal) objValue).shortValue();
    default:
      throw new TypeException("Expected a numeric type, found: " + valueType);
    }
  }

  @Override
  public int getInt() {
    switch(valueType) {
    case INT:
      return (int) (jsonValue & 0xffffffffL);
    case BYTE:
      return getByte();
    case SHORT:
      return getShort();
    case LONG:
      return (int) getLong();
    case FLOAT:
      return (int) getFloat();
    case DOUBLE:
      return (int) getDouble();
    case DECIMAL:
      return ((BigDecimal) objValue).intValue();
    default:
      throw new TypeException("Expected a numeric type, found: " + valueType);
    }
  }

  @Override
  public long getLong() {
    switch(valueType) {
    case LONG:
      return jsonValue;
    case BYTE:
      return getByte();
    case SHORT:
      return getShort();
    case INT:
      return getInt();
    case FLOAT:
      return (long) getFloat();
    case DOUBLE:
      return (long) getDouble();
    case DECIMAL:
      return ((BigDecimal) objValue).longValue();
    default:
      throw new TypeException("Expected a numeric type, found: " + valueType);
    }
  }

  @Override
  public float getFloat() {
    switch(valueType) {
    case FLOAT:
      return Float.intBitsToFloat((int) (jsonValue & 0xffffffffL));
    case BYTE:
      return getByte();
    case SHORT:
      return getShort();
    case INT:
      return (float) getInt();
    case LONG:
      return (float) getLong();
    case DOUBLE:
      return (float) getDouble();
    case DECIMAL:
      return ((BigDecimal) objValue).floatValue();
    default:
      throw new TypeException("Expected a numeric type, found: " + valueType);
    }
  }

  @Override
  public double getDouble() {
    switch(valueType) {
    case DOUBLE:
      return Double.longBitsToDouble(jsonValue);
    case FLOAT:
      return getFloat();
    case BYTE:
      return getByte();
    case SHORT:
      return getShort();
    case INT:
      return getInt();
    case LONG:
      return getLong();
    case DECIMAL:
      return ((BigDecimal) objValue).doubleValue();
    default:
      throw new TypeException("Expected a numeric type, found: " + valueType);
    }
  }

  @Override
  public BigDecimal getDecimal() {
    switch(valueType) {
    case DECIMAL:
      return (BigDecimal) objValue;
    case DOUBLE:
      return new BigDecimal(getDouble());
    case FLOAT:
      return new BigDecimal(getFloat());
    case BYTE:
      return new BigDecimal(getByte());
    case SHORT:
      return new BigDecimal(getShort());
    case INT:
      return new BigDecimal(getInt());
    case LONG:
      return new BigDecimal(getLong());
    default:
      throw new TypeException("Expected a numeric type, found: " + valueType);
    }
  }

  @Override
  public boolean getBoolean() {
    checkType(Type.BOOLEAN);
    return jsonValue != 0;
  }

  @Override
  public String getString() {
    checkType(Type.STRING);
    return (String) objValue;
  }

  @Override
  public OTimestamp getTimestamp() {
    checkType(Type.TIMESTAMP);
    if (objValue == null) {
      objValue = new OTimestamp(jsonValue);;
    }
    return (OTimestamp) objValue;
  }

  @Override
  public long getTimestampAsLong() {
    return jsonValue;
  }

  @Override
  public ODate getDate() {
    checkType(Type.DATE);
    if (objValue == null) {
      objValue = ODate.fromDaysSinceEpoch((int) jsonValue);
    }
    return (ODate) objValue;
  }

  @Override
  public int getDateAsInt() {
    return (int) jsonValue;
  }

  @Override
  public OTime getTime() {
    checkType(Type.TIME);
    if (objValue == null) {
      objValue = OTime.fromMillisOfDay((int) jsonValue);
    }
    return (OTime) objValue;
  }

  @Override
  public int getTimeAsInt() {
    return (int) jsonValue;
  }

  @Override
  public OInterval getInterval() {
    checkType(Type.INTERVAL);
    // on first access create the object and cache it
    // this is to avoid unnecessary object creation
    if (objValue == null) {
      OInterval t = new OInterval(jsonValue);
      objValue = t;
    }
    return (OInterval) objValue;
  }

  @Override
  public long getIntervalAsLong() {
    return jsonValue;
  }

  @Override
  public ByteBuffer getBinary() {
    checkType(Type.BINARY);
    return ((ByteBuffer) objValue).slice();
  }

  @Override
  public Map<String, Object> getMap() {
    checkType(Type.MAP);
    return (JsonDocument)this;
  }

  @Override
  public List<Object> getList() {
    checkType(Type.ARRAY);
    return (JsonList) this;
  }

  @Override
  public DocumentReader asReader() {
    return new JsonDOMDocumentReader(this);
  }

  /*
   * makes a shallow copy of the KeyValue It will copy the fields which are not
   * dependent on which tree this key value belongs to Caller needs to
   * recalculate the size of key value after inserting it into the new tree
   */
  JsonValue shallowCopy() {
    JsonValue newKeyValue = new JsonValue(valueType);
    newKeyValue.objValue = objValue;
    newKeyValue.jsonValue = jsonValue;
    return newKeyValue;
  }

  @Override
  public String toString() {
    return Values.asJsonString(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return valueType == Type.NULL;
    } else if (obj instanceof JsonValue) {
      JsonValue value = (JsonValue) obj;
      if (valueType != value.getType()) {
        return false;
      }
      switch (valueType) {
      case BOOLEAN:
      case BYTE:
      case SHORT:
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case DATE:
      case TIMESTAMP:
      case TIME:
      case INTERVAL:
        return jsonValue == value.jsonValue;
      case NULL:
        return ((objValue == null) && (value.objValue == null));
      case BINARY:
      case DECIMAL:
      case STRING:
      case MAP:
      case ARRAY:
        return objValue.equals(value.objValue);
      }
    } else if (obj instanceof String) {
      return objValue.equals(obj);
    } else if (obj instanceof Byte) {
      return obj.equals(getByte());
    } else if (obj instanceof Short) {
      return obj.equals(getShort());
    } else if (obj instanceof Boolean) {
      return obj.equals(getBoolean());
    } else if (obj instanceof Float) {
      return obj.equals(getFloat());
    } else if (obj instanceof Integer) {
      return obj.equals(getInt());
    } else if (obj instanceof Long) {
      return obj.equals(getLong());
    } else if (obj instanceof BigDecimal) {
      return obj.equals(getDecimal());
    } else if (obj instanceof Double) {
      return obj.equals(getDouble());
    } else if (obj instanceof ODate) {
      /* Internally we store Date, Time and Timestamp objects as long
       * values. Therefore, it is simpler to compare against that when
       * obj is of time Date, Time or Timestamp.
       * However, if the comparison is done with, for example, a date object
       * date as date.equals(getDate()), the comparison will not be equivalent
       * since the interval implementation in java is different. It may not
       * return same result.
       */
      long dateAsLong = ((ODate)obj).toDaysSinceEpoch();
      return dateAsLong == jsonValue;
    } else if (obj instanceof OTime) {
      long timeAsLong = ((OTime)obj).toTimeInMillis();
      return timeAsLong == jsonValue;
    } else if (obj instanceof OTimestamp) {
      long timestampAsLong = ((OTimestamp)obj).getMillis();
      return getTimestampAsLong() == timestampAsLong;
    } else if (obj instanceof OInterval) {
      return obj.equals(getInterval());
    } else if (obj instanceof ByteBuffer) {
      return obj.equals(getBinary());
    } else if (obj instanceof Map) {
      return objValue.equals(obj);
    } else if (obj instanceof List) {
      return objValue.equals(obj);
    } else if (obj instanceof Value) {
      return equals(((Value) obj).getObject());
    }

    return false;
  }

}
