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

package org.jackhammer.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;

import org.jackhammer.RecordReader;
import org.jackhammer.Value;
import org.jackhammer.Value.Type;
import org.jackhammer.exceptions.TypeException;
import org.jackhammer.types.Interval;
import org.jackhammer.util.Constants;
import org.jackhammer.util.Types;

class JsonDOMRecordReader implements RecordReader, Constants {

  class IteratorWithType {
    Iterator<JsonValue> iter;
    Type iterType;

    IteratorWithType(Iterator<?> i, Type t) {
      iter = (Iterator<JsonValue>)i;
      iterType = t;
    }
  }

  //define data structures
  private Stack<IteratorWithType> stateStack = null;
  private EventType event = null;
  private EventType nextEvent = null;
  private EventType currentEvent = null;
  private JsonValue jsonValue;


  JsonDOMRecordReader(Value value) {
    stateStack = new Stack<IteratorWithType>();
    /* analyze the type of value object and initialize events and stacks */
    Type t = value.getType();
    Iterator<?> iter;
    if (t == Type.MAP) {
      iter = ((JsonRecord)value).iterator();
      stateStack.push(new IteratorWithType(iter, t));
      event = EventType.START_MAP;
    } else if (t == Type.ARRAY) {
      iter = ((JsonList)value).iterator();
      stateStack.push(new IteratorWithType(iter, t));
      event = EventType.START_ARRAY;
    } else {
      jsonValue = (JsonValue)value;
      event = Types.getEventTypeForType(value.getType());
    }
  }

  /*private void setNextType(Value v) {
    nextEvent = Types.getEventTypeForType(v.getType());
  }*/

  /* Main method for traversing the DFS structure.
   * We use a stack to keep track of iterators at each level of
   * the graph model. Each call to iterator next() can give us either a map
   * or non-map. If it's a map, we need to return field name first followed
   * by the START_MAP token. If it's non-map and non-array, then we have
   * FIELD_NAME followed by value type. In case of array, we return FIELD_NAME
   * and START_ARRAY. Note that an array is returned as a single JsonValue
   * from iterator. Therefore, we should not traverse the iterator (using next())
   * once we have the array. We need to process array elements sequentially. Since
   * we have to determine two types of tokens when we process a node, we have two
   * variables et and nextEt.
   */
  private void ProcessNextNode() {
    if (stateStack.empty()) {
      /* We are done with the document */
      event = null;
      return;
    }
    IteratorWithType iterElem = stateStack.peek();
    Iterator<JsonValue> iter = iterElem.iter;
    if (iter.hasNext()) {
      Object o = iter.next();
      if (o instanceof Entry<?,?>) {
        Entry<String, JsonValue> kvpair = (Entry<String,JsonValue>)o;
        jsonValue = kvpair.getValue();
        jsonValue.setKey(kvpair.getKey());
        Type kvpairType = jsonValue.getType();
        if (kvpairType == Type.MAP) {
          event = EventType.FIELD_NAME;
          nextEvent = EventType.START_MAP;
          stateStack.push(new IteratorWithType(((JsonRecord)jsonValue).iterator(), kvpairType));
        } else if (kvpairType == Type.ARRAY) {
          event = EventType.FIELD_NAME;
          nextEvent = EventType.START_ARRAY;
          stateStack.push(new IteratorWithType(((JsonList)jsonValue).iterator(), kvpairType));
        } else {
          event = EventType.FIELD_NAME;
          nextEvent = Types.getEventTypeForType(jsonValue.getType());
        }
      } else {
        //inside array
        jsonValue = JsonValueBuilder.initFromObject(o);
        Type t = jsonValue.getType();
        if (t == Type.ARRAY) {
          event = EventType.START_ARRAY;
          stateStack.push(new IteratorWithType(((JsonList)jsonValue).iterator(), t));
        } else if (t == Type.MAP) {
          event = EventType.START_MAP;
          stateStack.push(new IteratorWithType(((JsonRecord)jsonValue).iterator(), t));
        } else {
          event = Types.getEventTypeForType(t);
        }
      }
    } else {
      iterElem = stateStack.pop();
      event = (iterElem.iterType == Type.MAP) ? EventType.END_MAP : EventType.END_ARRAY;
      return;
    }
  }


  @Override
  public EventType next() {
    currentEvent = null;
    if (event != null) {
      currentEvent = event;
      event = null;
    } else if (nextEvent != null) {
      currentEvent = nextEvent;
      nextEvent = null;
    } else {
      ProcessNextNode();
      currentEvent = event;
      event = null;
    }
    return currentEvent;
  }


  private void checkEventType(EventType event) throws TypeException {
    if (currentEvent != event) {
      throw new TypeException("Event type mismatch.");
    }
  }

  @Override
  public String getFieldName() {
    checkEventType(EventType.FIELD_NAME);
    return jsonValue.getKey();
  }

  @Override
  public byte getByte() {
    checkEventType(EventType.BYTE);
    return jsonValue.getByte();
  }

  @Override
  public short getShort() {
    checkEventType(EventType.SHORT);
    return jsonValue.getShort();
  }

  @Override
  public int getInt() {
    checkEventType(EventType.INT);
    return jsonValue.getInt();

  }

  @Override
  public long getLong() {
    checkEventType(EventType.LONG);
    return jsonValue.getLong();
  }

  @Override
  public float getFloat() {
    checkEventType(EventType.FLOAT);
    return jsonValue.getFloat();
  }

  @Override
  public double getDouble() {
    checkEventType(EventType.DOUBLE);
    return jsonValue.getDouble();
  }

  @Override
  public BigDecimal getDecimal() {
    checkEventType(EventType.DECIMAL);
    return jsonValue.getDecimal();
  }

  @Override
  public int getDecimalPrecision() {
    BigDecimal d = getDecimal();
    if (d != null) {
      return d.precision();
    }
    return 0;
  }

  @Override
  public int getDecimalScale() {
    BigDecimal d = getDecimal();
    if (d != null) {
      return d.scale();
    }
    return 0;
  }

  @Override
  public int getDecimalValueAsInt() {
    BigDecimal d = getDecimal();
    if (d != null) {
      return d.intValueExact();
    }
    return 0;
  }

  @Override
  public long getDecimalValueAsLong() {
    BigDecimal d = getDecimal();
    if (d != null) {
      return d.longValueExact();
    }
    return 0;
  }

  @Override
  public ByteBuffer getDecimalValueAsBytes() {
    BigDecimal decimal = getDecimal();
    if (decimal != null) {
      BigInteger decimalInteger = decimal.unscaledValue();
      byte[] bytearray = decimalInteger.toByteArray();
      return ByteBuffer.wrap(bytearray);
    }
    return null;
  }

  @Override
  public boolean getBoolean() {
    checkEventType(EventType.BOOLEAN);
    return jsonValue.getBoolean();
  }

  @Override
  public String getString() {
    checkEventType(EventType.STRING);
    return jsonValue.getString();
  }

  @Override
  public long getTimestampLong() {
    checkEventType(EventType.TIMESTAMP);
    return jsonValue.getTimestampAsLong();
  }

  @Override
  public Timestamp getTimestamp() {
    checkEventType(EventType.TIMESTAMP);
    return jsonValue.getTimestamp();
  }

  @Override
  public int getDateInt() {
    checkEventType(EventType.DATE);
    return jsonValue.getDateAsInt();
  }

  @Override
  public Date getDate() {
    checkEventType(EventType.DATE);
    return jsonValue.getDate();
  }

  @Override
  public int getTimeInt() {
    checkEventType(EventType.TIME);
    return jsonValue.getTimeAsInt();
  }

  @Override
  public Time getTime() {
    checkEventType(EventType.TIME);
    return jsonValue.getTime();
  }

  @Override
  public Interval getInterval() {
    checkEventType(EventType.INTERVAL);
    return jsonValue.getInterval();
  }

  @Override
  public int getIntervalDays() {
    return getInterval().getDays();
  }

  @Override
  public long getIntervalMillis() {
    return getInterval().getInterval();
  }

  @Override
  public ByteBuffer getBinary() {
    checkEventType(EventType.BINARY);
    return jsonValue.getBinary();
  }


}
