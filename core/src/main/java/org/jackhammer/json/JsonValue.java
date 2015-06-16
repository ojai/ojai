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

import org.jackhammer.RecordReader;
import org.jackhammer.Value;
import org.jackhammer.annotation.API;
import org.jackhammer.exceptions.TypeException;
import org.jackhammer.types.Interval;
import org.jackhammer.util.Constants;

@API.Internal
public class JsonValue implements Value, Constants {

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

  public static final JsonValue NULLKEYVALUE = new JsonValue(Type.NULL);

  @Override
  public int getInt() {
    checkType(Type.INT);
    return (int) (jsonValue & 0xffffffff);
  }

  @Override
  public long getLong() {
    checkType(Type.LONG);
    return jsonValue;
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
    checkType(Type.BYTE);
    return (byte) (jsonValue & 0xff);
  }

  @Override
  public short getShort() {
    checkType(Type.SHORT);
    return (short) (jsonValue & 0xffff);
  }

  @Override
  public float getFloat() {
    checkType(Type.FLOAT);
    return Float.intBitsToFloat((int) (jsonValue & 0xffffffffL));
  }

  @Override
  public double getDouble() {
    checkType(Type.DOUBLE);
    return Double.longBitsToDouble(jsonValue);
  }

  @Override
  public BigDecimal getDecimal() {
    checkType(Type.DECIMAL);
    return (BigDecimal) objValue;
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
  public Timestamp getTimestamp() {
    checkType(Type.TIMESTAMP);
    if (objValue == null) {
      Timestamp timestamp = new Timestamp(jsonValue);
      objValue = timestamp;
    }
    return (Timestamp) objValue;
  }

  @Override
  public long getTimestampAsLong() {
    return jsonValue;
  }

  @Override
  public Date getDate() {
    checkType(Type.DATE);
    if (objValue == null) {
      Date date = new Date(jsonValue * MILLISECONDSPERDAY);
      objValue = date;
    }
    return (Date) objValue;
  }

  @Override
  public int getDateAsInt() {
    return (int) jsonValue;
  }

  @Override
  public Time getTime() {
    checkType(Type.TIME);
    if (objValue == null) {
      Time time = new Time(jsonValue);
      objValue = time;
    }
    return (Time) objValue;
  }

  @Override
  public int getTimeAsInt() {
    return (int) jsonValue;
  }

  @Override
  public Interval getInterval() {
    checkType(Type.INTERVAL);
    // on first access create the object and cache it
    // this is to avoid unnecessary object creation
    if (objValue == null) {
      Interval t = new Interval(jsonValue);
      objValue = t;
    }
    return (Interval) objValue;
  }

  @Override
  public long getIntervalAsLong() {
    return jsonValue;
  }

  @Override
  public ByteBuffer getBinary() {
    checkType(Type.BINARY);
    return (ByteBuffer) objValue;
  }

  @Override
  public Map<String, Object> getMap() {
    checkType(Type.MAP);
    return (JsonRecord)this;
  }

  @Override
  public List<Object> getList() {
    checkType(Type.ARRAY);
    return (JsonList) this;
  }

  @Override
  public RecordReader getStream() {
    return null;
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
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj instanceof JsonValue) {
      JsonValue value = (JsonValue) obj;
      if (valueType != value.getType())
        return false;

      switch (valueType) {
      case BOOLEAN:
      case BYTE:
      case SHORT:
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
        return jsonValue == value.jsonValue;

      case TIME:
      case TIMESTAMP:
      case DATE:
      case INTERVAL:
      case BINARY:
      case DECIMAL:
      case STRING:
      case NULL:
      case MAP:
      case ARRAY:
        return objValue.equals(value.objValue);
      }
    }
    return false;
  }

}
