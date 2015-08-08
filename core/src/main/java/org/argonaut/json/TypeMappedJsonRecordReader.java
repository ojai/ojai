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
package org.argonaut.json;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import org.argonaut.FieldPath;
import org.argonaut.Value.Type;
import org.argonaut.annotation.API;
import org.argonaut.exceptions.DecodingException;
import org.argonaut.util.Types;

import com.fasterxml.jackson.core.JsonParseException;

@API.Internal
public class TypeMappedJsonRecordReader extends JsonStreamRecordReader {

  private final Map<FieldPath, Type> typeMap;
  private final Stack<Object> fieldSegmentStack = new Stack<Object>();

  private FieldPath currentFieldPath = null;
  private String currentFieldName;

  TypeMappedJsonRecordReader(JsonRecordStream stream, Map<FieldPath, Type> hashMap) {
    super(stream);
    if (hashMap == null) {
      throw new IllegalArgumentException("A FieldPath => Type map must be provided.");
    }
    this.typeMap = hashMap;
  }

  @Override
  public EventType next() {
    EventType et = super.next();
    switch (et) {
    case FIELD_NAME:
      currentFieldName = getFieldName();
      calculateCurrentFieldPath();
      break;
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
      Type mappedType = typeMap.get(currentFieldPath);
      if (mappedType != null) {
        Type currentFieldType = mappedType;
        et = currentEventType = Types.getEventTypeForType(currentFieldType);
        try {
          cacheCurrentValue();
        } catch (JsonParseException jp) {
          throw new IllegalStateException(jp);
        } catch (IOException e) {
          throw new DecodingException(e);
        }
      }
      break;
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
