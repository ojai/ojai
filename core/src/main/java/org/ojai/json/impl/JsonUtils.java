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

import java.io.UnsupportedEncodingException;

import org.ojai.DocumentBuilder;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;

@API.Internal
public class JsonUtils {

  public static byte[] getBytes(String string) {
    try {
      return string.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {}
    return null;
  }

  public static void addToMap(DocumentReader r, DocumentBuilder w) {
    EventType e;
    while((e = r.next()) != null) {
      switch (e) {
      case NULL:
        w.putNull(r.getFieldName());
        break;
      case BOOLEAN:
        w.put(r.getFieldName(), r.getBoolean());
        break;
      case STRING:
        w.put(r.getFieldName(), r.getString());
        break;
      case BYTE:
        w.put(r.getFieldName(), r.getByte());
        break;
      case SHORT:
        w.put(r.getFieldName(), r.getShort());
        break;
      case INT:
        w.put(r.getFieldName(), r.getInt());
        break;
      case LONG:
        w.put(r.getFieldName(), r.getLong());
        break;
      case FLOAT:
        w.put(r.getFieldName(), r.getFloat());
        break;
      case DOUBLE:
        w.put(r.getFieldName(), r.getDouble());
        break;
      case DECIMAL:
        w.put(r.getFieldName(), r.getDecimal());
        break;
      case DATE:
        w.put(r.getFieldName(), r.getDate());
        break;
      case TIME:
        w.put(r.getFieldName(), r.getTime());
        break;
      case TIMESTAMP:
        w.put(r.getFieldName(), r.getTimestamp());
        break;
      case INTERVAL:
        w.put(r.getFieldName(), r.getInterval());
        break;
      case BINARY:
        w.put(r.getFieldName(), r.getBinary());
        break;
      case START_ARRAY:
        w.putNewArray(r.getFieldName());
        addToArray(r, w);
        break;
      case END_ARRAY:
        w.endArray();
        break;
      case START_MAP:
        if (r.getFieldName() != null) {
          w.putNewMap(r.getFieldName());
        } else {
          //start of the document
          w.addNewMap();
        }
        addToMap(r, w);
        break;
      case END_MAP:
        w.endMap();
        return;
      default:
        throw new DecodingException("Unknown event type: " + e);
      }
    }
  }

  public static void addToArray(DocumentReader r, DocumentBuilder w) {
    EventType e;
    while((e = r.next()) != null) {
      if (e == EventType.END_ARRAY) {
        w.endArray();
        break;
      } else {
        addReaderEvent(e, r, w.setArrayIndex(r.getArrayIndex()));
      }
    }
  }

  public static void addReaderEvent(EventType e, DocumentReader r, DocumentBuilder w) {
    switch (e) {
    case NULL:
      w.addNull();
      break;
    case BOOLEAN:
      w.add(r.getBoolean());
      break;
    case STRING:
      w.add(r.getString());
      break;
    case BYTE:
      w.add(r.getByte());
      break;
    case SHORT:
      w.add(r.getShort());
      break;
    case INT:
      w.add(r.getInt());
      break;
    case LONG:
      w.add(r.getLong());
      break;
    case FLOAT:
      w.add(r.getFloat());
      break;
    case DOUBLE:
      w.add(r.getDouble());
      break;
    case DECIMAL:
      w.add(r.getDecimal());
      break;
    case DATE:
      w.add(r.getDate());
      break;
    case TIME:
      w.add(r.getTime());
      break;
    case TIMESTAMP:
      w.add(r.getTimestamp());
      break;
    case INTERVAL:
      w.add(r.getInterval());
      break;
    case BINARY:
      w.add(r.getBinary());
      break;
    case START_MAP:
      w.addNewMap();
      addToMap(r, w);
      break;
    case END_MAP:
      w.endMap();
      break;
    case START_ARRAY:
      w.addNewArray();
      addToArray(r, w);
      break;
    default:
      throw new DecodingException("Unexpected event type: " + e);
    }
  }

}
