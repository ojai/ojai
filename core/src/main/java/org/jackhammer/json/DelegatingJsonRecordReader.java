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

import java.util.LinkedList;
import java.util.Queue;

import org.jackhammer.Value;
import org.jackhammer.json.Events.Delegate;
import org.jackhammer.json.Events.TypeValuePair;

/**
 * This implementation of {@code RecordReader} offers application a
 * chance to inspect the next event in Json stream and alter if required.
 */
public class DelegatingJsonRecordReader extends JsonStreamRecordReader {

  private final Queue<TypeValuePair> eventQueue = new LinkedList<Events.TypeValuePair>();
  private final Events.Delegate eventDelegate;
  private boolean startSeen = false;

  DelegatingJsonRecordReader(JsonRecordStream stream, Delegate eventDelegate) {
    super(stream);
    if (eventDelegate == null) {
      throw new NullPointerException("Event delegate must be provided.");
    }
    this.eventDelegate = eventDelegate;
  }

  @Override
  public EventType next() {
    /*
     * Be careful in returning from the middle of this function
     */
    EventType et = null;
    if (!eventQueue.isEmpty()) {
      et = updateCurrentValue(eventQueue.remove());
    } else {
      et = super.next();
      if (!startSeen && et == EventType.START_MAP) {
        startSeen = true;
        if (eventDelegate.bor(this, eventQueue)) {
          et = next();
        }
      } else if (et != null) {
        if (eventDelegate.process(this, et, eventQueue)) {
          et = next();
        }
      } else {
        if (eventDelegate.eor(this, eventQueue)) {
          et = next();
        }
      }
    }

    return et;
  }

  private EventType updateCurrentValue(TypeValuePair head) {
    currentEventType = head.eventType;
    Value value = head.value;

    switch (currentEventType) {
    case BOOLEAN:
      currentObjValue = value.getBoolean();
      break;
    case STRING:
    case FIELD_NAME:
      currentObjValue = value.getString();
      break;
    case BYTE:
      currentLongValue = value.getByte();
      break;
    case SHORT:
      currentLongValue = value.getShort();
      break;
    case INT:
      currentLongValue = value.getInt();
      break;
    case LONG:
      currentLongValue = value.getLong();
      break;
    case FLOAT:
      currentDoubleValue = value.getFloat();
      break;
    case DOUBLE:
      currentDoubleValue = value.getDouble();
      break;
    case DECIMAL:
      currentObjValue = value.getDecimal();
      break;
    case DATE:
      currentObjValue = value.getDate();
      break;
    case TIME:
      currentObjValue = value.getTime();
      break;
    case TIMESTAMP:
      currentObjValue = value.getTimestamp();
      break;
    case INTERVAL:
      currentLongValue = value.getIntervalAsLong();
      break;
    case BINARY:
      currentObjValue = value.getBinary();
      break;
    default:
      // ARRAY, MAP and NULL need not be cached
      break;
    }

    return currentEventType;
  }

}
