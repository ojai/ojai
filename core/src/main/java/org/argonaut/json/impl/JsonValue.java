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
package org.argonaut.json.impl;

import static org.argonaut.util.Constants.MILLISECONDSPERDAY;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.argonaut.DocumentReader;
import org.argonaut.Value;
import org.argonaut.annotation.API;
import org.argonaut.exceptions.TypeException;
import org.argonaut.types.Interval;

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

  private void checkNumericType() throws TypeException {

    switch(valueType) {
    case BYTE:
    case SHORT:
    case INT:
    case LONG:
      return;
    default:
      throw new TypeException("Value type is wrong");
    }
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
    checkNumericType();
    return (int) (jsonValue & 0xffffffff);
  }

  @Override
  public long getLong() {
    checkNumericType();
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
    checkNumericType();
    return (byte) (jsonValue & 0xff);
  }

  @Override
  public short getShort() {
    checkNumericType();
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
    return (JsonDocument)this;
  }

  @Override
  public List<Object> getList() {
    checkType(Type.ARRAY);
    return (JsonList) this;
  }

  @Override
  public DocumentReader getStream() {
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
    }
    if (obj instanceof String) {
      return objValue.equals(obj);
    }
    if (obj instanceof Byte) {
      return obj.equals(getByte());
    }

    if (obj instanceof Short) {
      return obj.equals(getShort());
    }

    if (obj instanceof Boolean) {
      return obj.equals(getBoolean());
    }

    if (obj instanceof Float) {
      return obj.equals(getFloat());
    }

    if (obj instanceof Integer) {
      return obj.equals(getInt());
    }

    if (obj instanceof BigDecimal) {
      return obj.equals(getDecimal());
    }

    if (obj instanceof Double) {
      return obj.equals(getDouble());
    }

    /* Internally we store Date, Time and Timestamp objects as long
     * values. Therefore, it is simpler to compare against that when
     * obj is of time Date, Time or Timestamp.
     * However, if the comparison is done with, for example, a date object
     * date as date.equals(getDate()), the comparison will not be equivalent
     * since the interval implementation in java is different. It may not
     * return same result.
     */
    if (obj instanceof Date) {
      long dateAsLong = ((Date)obj).getTime()/MILLISECONDSPERDAY;
      return dateAsLong == jsonValue;
    }

    if (obj instanceof Time) {
      long timeAsLong = ((Time)obj).getTime() % MILLISECONDSPERDAY;
      return timeAsLong == jsonValue;
    }

    if (obj instanceof Timestamp) {
      long timestampAsLong = ((Timestamp)obj).getTime();
      return getTimestampAsLong() == timestampAsLong;
    }

    if (obj instanceof Interval) {
      return obj.equals(getInterval());
    }

    if (obj instanceof ByteBuffer) {
      return obj.equals(getBinary());
    }

    if (obj instanceof Map) {
      return obj.equals(getMap());
    }

    if (obj instanceof List) {
      return obj.equals(getList());
    }


    return false;
  }

}
