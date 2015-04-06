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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.jackhammer.Record;
import org.jackhammer.RecordReader;
import org.jackhammer.RecordReader.EventType;
import org.jackhammer.exceptions.DecodingException;
import org.jackhammer.util.Fields;

public class JsonRecordIterator implements Iterator<Record> {
  Iterator<RecordReader> it = null;
  JsonRecord record;
  RecordReader reader;
  private boolean hasNextRecord;

  public JsonRecordIterator(JsonRecordStream s) {
    it = s.recordReaders().iterator();
  }

  @Override
  public boolean hasNext() {
    hasNextRecord = false;
    if (it.hasNext()) {
      reader = it.next();
      if (reader != null) {
        hasNextRecord = true;
      }
    }
    return hasNextRecord;
  }

  @Override
  public Record next() {
    if (hasNextRecord) {
      return getRecordFromStreamReader();
    }
    throw new NoSuchElementException();
  }

  private Record getRecordFromStreamReader() {
    Stack<JsonValue> containerStack = new Stack<JsonValue>();
    EventType event;
    JsonRecord lastRecord = null;
    String currentFieldName = null;
    JsonValue currentContainer = null;

    while ((event = reader.next()) != null) {
      switch(event) {
      case START_MAP:
        JsonValue newRec = new JsonRecord();
        if (currentContainer != null) {
          appendTo(currentContainer, currentFieldName, newRec);
        }
        currentContainer = newRec;
        containerStack.push(currentContainer);
        break;
      case END_MAP:
        if (!containerStack.empty() && containerStack.peek() instanceof JsonRecord) {
          lastRecord = (JsonRecord) containerStack.pop();
          if (!containerStack.empty()) {
            currentContainer = containerStack.peek();
          }
        } else {
          throw new DecodingException("Unable to decode record stream: " + containerStack.toString());
        }
        break;
      case START_ARRAY:
        JsonValue newList = new JsonList();
        if (currentContainer != null) {
          appendTo(currentContainer, currentFieldName, newList);
        }
        currentContainer = newList;
        containerStack.push(currentContainer);
        break;
      case END_ARRAY:
        if (!containerStack.empty() && containerStack.peek() instanceof JsonList) {
          containerStack.pop();
          if (!containerStack.empty()) {
            currentContainer = containerStack.peek();
          }
        } else {
          throw new DecodingException("Unable to decode record stream: " + containerStack.toString());
        }
        break;
      case FIELD_NAME:
        currentFieldName = Fields.escapeFieldName(reader.getFieldName());
        break;
      case NULL:
        appendTo(currentContainer, currentFieldName, JsonValue.NULLKEYVALUE);
        break;
      case BOOLEAN:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getBoolean()));
        break;
      case BINARY:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getBinary()));
        break;
      case BYTE:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getByte()));
        break;
      case SHORT:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getShort()));
        break;
      case INT:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getInt()));
        break;
      case LONG:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getLong()));
        break;
      case FLOAT:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getFloat()));
        break;
      case DOUBLE:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getDouble()));
        break;
      case DECIMAL:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getDecimal()));
        break;
      case STRING:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getString()));
        break;
      case DATE:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getDate()));
        break;
      case TIME:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getTime()));
        break;
      case TIMESTAMP:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getTimestamp()));
        break;
      case INTERVAL:
        appendTo(currentContainer, currentFieldName, JsonValueBuilder.initFrom(reader.getInterval()));
        break;
       default:
         throw new DecodingException("Unknown token type " + event + " from stream reader");
      }
    }
    if (!containerStack.empty()) {
      //this means we did not got the end of the record.
      throw new DecodingException("Error processing record");
    }
    return lastRecord;
  }

  private void appendTo(JsonValue currentContainer, String fieldName,
      JsonValue value) {
    if (currentContainer instanceof JsonRecord) {
      ((JsonRecord)currentContainer).getRootMap().put(fieldName, value);
    } else if (currentContainer instanceof JsonList) {
      ((JsonList)currentContainer).getRootList().add(value);
    } else {
      throw new DecodingException("Unable to decode record stream: " + currentContainer.toString());
    }
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
