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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.ojai.FieldPath;
import org.ojai.FieldSegment;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;
import org.ojai.types.ODate;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.util.Types;
import org.ojai.util.Values;

@API.Internal
public class TypeMappedJsonDocumentReader extends JsonStreamDocumentReader {

  private final Map<FieldPath, Type> typeMap;
  private final Stack<Object> fieldSegmentStack = new Stack<Object>();

  private FieldPath currentFieldPath = null;
  private String currentFieldName;

  TypeMappedJsonDocumentReader(JsonDocumentStream stream, Map<FieldPath, Type> hashMap) {
    super(stream);
    if (hashMap == null) {
      throw new IllegalArgumentException("A FieldPath => Type map must be provided.");
    }
    for (Entry<FieldPath, Type> entry : hashMap.entrySet()) {
      for (FieldSegment seg : entry.getKey()) {
        if (seg.isArray()) {
          throw new IllegalArgumentException("A FieldPath with indexed segment is not supported.");
        }
      }
      if (!entry.getValue().isScalar()) {
        throw new IllegalArgumentException("A mapping to a container type (ARRAY, MAP) is not supported.");
      }
    }
    this.typeMap = hashMap;
  }

  @Override
  public EventType next() {
    EventType et = super.next();
    if (et != null) {
      switch (et) {
      case END_MAP:
        if (!fieldSegmentStack.isEmpty()) {
          fieldSegmentStack.pop();
        }
        break;
      default:
        if (inMap()) {
          currentFieldName = getFieldName();
          calculateCurrentFieldPath();
          if (et == EventType.START_MAP) {
            if (currentFieldName != null) {
              fieldSegmentStack.push(currentFieldName);
            }
          } else {
            Type mappedType = typeMap.get(currentFieldPath);
            if (mappedType != null) {
              Type currentFieldType = mappedType;
              et = Types.getEventTypeForType(currentFieldType);
              if (et != getCurrentEventType()) {
                updateCurrentValue(et);
                setCurrentEventType(et);
              }
            }
          }
        }
        break;
      }
    }
    return et;
  }

  private void updateCurrentValue(EventType evt) {
    EventType oldEvt = getCurrentEventType();
    switch (evt) {
    case BOOLEAN:
      setCurrentObj(convertValueToBoolean(oldEvt, evt));
      break;
    case STRING:
      setCurrentObj(isExtended()
          ? convertValueToString(oldEvt, evt) : getValueAsString());
      break;
    case BYTE:
      setCurrentLongValue(convertValueToLong(oldEvt, evt) & 0xff);
      break;
    case SHORT:
      setCurrentLongValue(convertValueToLong(oldEvt, evt) & 0xffff);
      break;
    case INT:
      setCurrentLongValue(convertValueToLong(oldEvt, evt) & 0xffffffff);
      break;
    case LONG:
    case INTERVAL:
      setCurrentLongValue(convertValueToLong(oldEvt, evt));
      break;
    case FLOAT:
    case DOUBLE:
      setCurrentDoubleValue(convertValueToDouble(oldEvt, evt));
      break;
    case DECIMAL:
      setCurrentObj(convertValueToDecimal(oldEvt, evt));
      break;
    case DATE:
      setCurrentObj(convertValueToDate(oldEvt, evt));
      break;
    case TIME:
      setCurrentObj(convertValueToTime(oldEvt, evt));
      break;
    case TIMESTAMP:
      setCurrentObj(convertValueToTimestamp(oldEvt, evt));
      break;
    case BINARY:
      setCurrentObj(convertValueToBinary(oldEvt, evt));
      break;
    default:
      // ARRAY, MAP and NULL need not be cached
      break;
    }
  }

  private ByteBuffer convertValueToBinary(EventType oldEvt, EventType evt) {
    switch (oldEvt) {
    case STRING:
      return Values.parseBinary((String)getCurrentObj());
    default:
      throw unsupportedConversion(oldEvt, evt);
    }
  }

  private BigDecimal convertValueToDecimal(EventType oldEvt, EventType evt) {
    switch (oldEvt) {
    case BYTE: case SHORT: case INT: case LONG:
      return BigDecimal.valueOf(getCurrentLongValue());
    case FLOAT: case DOUBLE:
      return BigDecimal.valueOf(getCurrentDoubleValue());
    case STRING:
      return Values.parseBigDecimal((String)getCurrentObj());
    default:
      throw unsupportedConversion(oldEvt, evt);
    }
  }

  private ODate convertValueToDate(EventType oldEvt, EventType evt) {
    switch (oldEvt) {
    case BYTE: case SHORT: case INT: case LONG:
      return new ODate(getCurrentLongValue());
    case STRING:
      return ODate.parse((String)getCurrentObj());
    default:
      throw unsupportedConversion(oldEvt, evt);
    }
  }

  private OTime convertValueToTime(EventType oldEvt, EventType evt) {
    switch (oldEvt) {
    case BYTE: case SHORT: case INT: case LONG:
      return new OTime(getCurrentLongValue());
    case STRING:
      return OTime.parse((String)getCurrentObj());
    default:
      throw unsupportedConversion(oldEvt, evt);
    }
  }
  
  private OTimestamp convertValueToTimestamp(EventType oldEvt, EventType evt) {
    switch (oldEvt) {
    case BYTE: case SHORT: case INT: case LONG:
      return new OTimestamp(getCurrentLongValue());
    case STRING:
      return OTimestamp.parse((String)getCurrentObj());
    default:
      throw unsupportedConversion(oldEvt, evt);
    }
  }

  private Object convertValueToString(EventType oldEvt, EventType evt) {
    switch (oldEvt) {
    case BYTE: case SHORT: case INT: case LONG: case INTERVAL:
      return String.valueOf(getCurrentLongValue());
    case FLOAT: case DOUBLE:
      return String.valueOf(getCurrentDoubleValue());
    default:
      return String.valueOf(getCurrentObj());
    }
  }

  private Boolean convertValueToBoolean(EventType oldEvt, EventType evt) {
    switch (oldEvt) {
    case BYTE: case SHORT: case INT: case LONG:
      return getCurrentLongValue() != 0;
    case FLOAT: case DOUBLE:
      return getCurrentDoubleValue() != 0;
    case STRING:
      return Boolean.valueOf((String)getCurrentObj());
    default:
      throw unsupportedConversion(oldEvt, evt);
    }
  }

  private double convertValueToDouble(EventType oldEvt, EventType evt) {
    switch (getCurrentEventType()) {
    case BYTE: case SHORT: case INT: case LONG: case INTERVAL:
      return getCurrentLongValue();
    case FLOAT: case DOUBLE:
      return getCurrentDoubleValue();
    case STRING:
      return Double.valueOf((String)getCurrentObj());
    case DECIMAL:
      return ((BigDecimal)getCurrentObj()).doubleValue();
    default:
      throw unsupportedConversion(oldEvt, evt);
    }
  }

  private long convertValueToLong(EventType oldEvt, EventType evt) {
    switch (getCurrentEventType()) {
    case BYTE: case SHORT: case INT: case LONG: case INTERVAL:
      return getCurrentLongValue();
    case FLOAT: case DOUBLE:
      return (long) getCurrentDoubleValue();
    case STRING:
      return Long.valueOf((String)getCurrentObj());
    case DECIMAL:
      return ((BigDecimal)getCurrentObj()).longValue();
    default:
      throw unsupportedConversion(oldEvt, evt);
    }
  }

  private DecodingException unsupportedConversion(EventType oldEvt, EventType evt) {
    return new DecodingException(
        String.format("A value of type %s cannot be converted as %s.",
        Types.getTypeForEventType(oldEvt), Types.getTypeForEventType(evt)));
  }

  private void calculateCurrentFieldPath() {
    StringBuilder sb = new StringBuilder();
    for (Object field : fieldSegmentStack) {
      if (field instanceof Number) {
        sb.append('[').append(field).append(']');
      } else {
        sb.append('`').append(field).append('`').append('.');
      }
    }
    currentFieldPath = FieldPath.parseFrom(sb.append('`').append(currentFieldName).append('`').toString());
  }

}
