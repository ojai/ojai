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
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Stack;

import org.ojai.DocumentReader;
import org.ojai.Value.Type;
import org.ojai.base.DocumentReaderBase;
import org.ojai.exceptions.TypeException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.util.Types;

class JsonDOMDocumentReader extends DocumentReaderBase {

  //define data structures
  private Stack<IteratorWithType> stateStack = null;
  private IteratorWithType currentItr = null;
  private EventType nextEvent = null;
  private EventType currentEvent = null;
  private JsonValue jsonValue;

  JsonDOMDocumentReader(JsonValue value) {
    stateStack = new Stack<IteratorWithType>();
    /* analyze the type of value object and initialize events and stacks */
    jsonValue = value;
    Type type = value.getType();
    nextEvent = Types.getEventTypeForType(type);
    if (!type.isScalar()) {
      stateStack.push(new IteratorWithType(value));
    }
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
  @SuppressWarnings("unchecked")
  private void processNextNode() {
    if (stateStack.empty()) {
      /* We are done with the document */
      nextEvent = null;
      return;
    }

    currentItr = stateStack.peek();
    if (currentItr.hasNext()) {
      Object o = currentItr.next();
      if (inMap()) {
        jsonValue = ((Entry<String, JsonValue>) o).getValue();
      } else { //inside array
        jsonValue = JsonValueBuilder.initFromObject(o);
      }
      nextEvent = Types.getEventTypeForType(jsonValue.getType());
      if (!jsonValue.getType().isScalar()) {
        stateStack.push(new IteratorWithType(jsonValue));
      }
    } else {
      IteratorWithType iter = stateStack.pop();
      jsonValue = iter.getValue();
      nextEvent = (iter.getType() == Type.MAP) ? EventType.END_MAP : EventType.END_ARRAY;
      currentItr = stateStack.isEmpty() ? null : stateStack.peek();
    }
  }

  @Override
  public EventType next() {
    currentEvent = null;
    if (nextEvent != null) {
      currentEvent = nextEvent;
      nextEvent = null;
    } else {
      processNextNode();
      currentEvent = nextEvent;
      nextEvent = null;
    }
    return currentEvent;
  }

  private void checkEventType(EventType event) throws TypeException {
    if (currentEvent != event) {
      throw new TypeException(String.format(
          "Event type mismatch. The operation requires %s, but found %s",
          event, currentEvent));
    }
  }

  @Override
  public boolean inMap() {
    return currentItr == null
        || currentItr.getType() == Type.MAP;
  }

  @Override
  public int getArrayIndex() {
    if (inMap()) {
      throw new IllegalStateException("Not traversing an array!");
    }
    return currentItr.previousIndex();
  }

  @Override
  public String getFieldName() {
    if (!inMap()) {
      throw new IllegalStateException("Not traversing a map!");
    }
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
  public OTimestamp getTimestamp() {
    checkEventType(EventType.TIMESTAMP);
    return jsonValue.getTimestamp();
  }

  @Override
  public int getDateInt() {
    checkEventType(EventType.DATE);
    return jsonValue.getDateAsInt();
  }

  @Override
  public ODate getDate() {
    checkEventType(EventType.DATE);
    return jsonValue.getDate();
  }

  @Override
  public int getTimeInt() {
    checkEventType(EventType.TIME);
    return jsonValue.getTimeAsInt();
  }

  @Override
  public OTime getTime() {
    checkEventType(EventType.TIME);
    return jsonValue.getTime();
  }

  @Override
  public OInterval getInterval() {
    checkEventType(EventType.INTERVAL);
    return jsonValue.getInterval();
  }

  @Override
  public int getIntervalDays() {
    return getInterval().getDays();
  }

  @Override
  public long getIntervalMillis() {
    return getInterval().getTimeInMillis();
  }

  @Override
  public ByteBuffer getBinary() {
    checkEventType(EventType.BINARY);
    return jsonValue.getBinary();
  }

  private class IteratorWithType implements ListIterator<Object> {
    final Iterator<?> i;
    final JsonValue value;

    IteratorWithType(JsonValue value) {
      this.value = value;
      this.i = (value.getType() == Type.MAP)
          ? ((JsonDocument) value).iterator()
          : ((JsonList) value).listIterator();
    }

    public JsonValue getValue() {
      return value;
    }

    @Override
    public boolean hasNext() {
      return i.hasNext();
    }

    @Override
    public Object next() {
      return i.next();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
      return (getType() == Type.ARRAY ? "ListIterator@" : "MapIterator@")
          + hashCode();
    }

    Type getType() {
      return value.getType();
    }

    @Override
    public boolean hasPrevious() {
      checkList();
      return ((ListIterator<?>) i).hasPrevious();
    }

    @Override
    public Object previous() {
      checkList();
      return ((ListIterator<?>) i).previous();
    }

    @Override
    public int nextIndex() {
      checkList();
      return ((ListIterator<?>) i).nextIndex();
    }

    @Override
    public int previousIndex() {
      checkList();
      return ((ListIterator<?>) i).previousIndex();
    }

    @Override
    public void set(Object e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(Object e) {
      throw new UnsupportedOperationException();
    }

    private void checkList() {
      if (getType() != Type.ARRAY) {
        throw new UnsupportedOperationException();
      }
    }
  }

  @Override
  protected EventType getCurrentEvent() {
    return currentEvent;
  }

}
