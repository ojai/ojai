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

import org.jackhammer.Record;
import org.jackhammer.RecordReader;
import org.jackhammer.Value;
import org.jackhammer.Value.Type;
import org.jackhammer.exceptions.TypeException;
import org.jackhammer.types.Interval;
import org.jackhammer.util.Constants;
import org.jackhammer.util.Types;

public class JsonDOMRecordReader implements RecordReader, Constants {


  private JsonRecord record;
  private Iterator<Entry<String, Value>> it ;
  Stack<Iterator<Entry<String, Value>>> cursorStack;
  Entry<String, Value> nextkv ;
  JsonValue jsonValue;
  EventType et;
  EventType nextEt;
  EventType currentEventType;
  String key;
  private boolean isArray;
  private int arrayIndex;
  private JsonList jsonArray;

  public JsonDOMRecordReader(Record rec) {
    record = (JsonRecord)rec;
    it = record.iterator();
    cursorStack = new Stack<Iterator<Entry<String, Value>>>();
    cursorStack.push(it);
    nextkv = null;
    et = EventType.START_MAP;
    nextEt = null;
    isArray = false;
  }

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
    if (isArray) {
      jsonValue = jsonArray.getJsonValueAt(arrayIndex);
      if (jsonValue == null) {
        isArray = false;
        et = EventType.END_ARRAY;
      }else {
        setNextType();
        et = nextEt;
        nextEt = null;
        arrayIndex++;
      }
      return;

    }
    if (cursorStack.empty()) {
      return;
    }
    it = cursorStack.peek();

    /* checks if we are done traversing a map or
     * if we are not inside an array.
     */
    if (!it.hasNext() && (!isArray)) {
      cursorStack.pop();
      et = EventType.END_MAP;
      return ;
    }

    //else we can extract next element from iterator sitting
    //on top of the stack
    nextkv = it.next();
    Type t = nextkv.getValue().getType();
    if (t == Type.MAP) {
      et = EventType.FIELD_NAME;
      nextEt = EventType.START_MAP;
      JsonRecord r = (JsonRecord)nextkv.getValue();
      cursorStack.push(r.iterator());
    } else if (t == Type.ARRAY) {
      et = EventType.FIELD_NAME;
      nextEt = EventType.START_ARRAY;
      isArray = true;
      arrayIndex = 0;
      jsonArray = (JsonList)nextkv.getValue();
    }
    else {
      et = EventType.FIELD_NAME;
      jsonValue = (JsonValue)nextkv.getValue();
      setNextType();
    }
  }


  @Override
  public EventType next() {
    currentEventType = null;
    if (et != null) {
      currentEventType = et;
      et = null;
    } else if (nextEt != null) {
      currentEventType = nextEt;
      nextEt = null;
    } else {
      ProcessNextNode();
      currentEventType = et;
      et = null;
    }
    return currentEventType;
  }


  private void setNextType() {
    nextEt = Types.getEventTypeForType(jsonValue.getType());
  }



  private void checkEventType(EventType event) throws TypeException {
    if (currentEventType != event) {
      throw new TypeException("Event type mismatch.");
    }
  }

  @Override
  public String getFieldName() {
    checkEventType(EventType.FIELD_NAME);
    return nextkv.getKey();
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
    return nextkv.getValue().getDouble();
  }

  @Override
  public BigDecimal getDecimal() {
    checkEventType(EventType.DECIMAL);
    return nextkv.getValue().getDecimal();
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
    return nextkv.getValue().getBoolean();
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
