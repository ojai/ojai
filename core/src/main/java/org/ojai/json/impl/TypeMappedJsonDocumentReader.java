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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.ojai.FieldPath;
import org.ojai.FieldSegment;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.util.Types;

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
      case START_MAP:
        if (currentFieldName != null) {
          fieldSegmentStack.push(currentFieldName);
        }
        break;
      case END_MAP:
        if (!fieldSegmentStack.isEmpty()) {
          fieldSegmentStack.pop();
        }
        break;
      case START_ARRAY:
      case END_ARRAY:
        break;
      default:
        if (inMap()) {
          currentFieldName = getFieldName();
          calculateCurrentFieldPath();
        }
        Type mappedType = typeMap.get(currentFieldPath);
        if (mappedType != null) {
          Type currentFieldType = mappedType;
          et = Types.getEventTypeForType(currentFieldType);
          setCurrentEventType(et);
          cacheCurrentValue();
        }
        break;
      }
    }
    return et;
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
