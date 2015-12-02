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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.ojai.Document;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;

@API.Internal
public class JsonDocumentIterator implements Iterator<Document> {
  Iterator<DocumentReader> it = null;
  JsonDocument document;
  DocumentReader reader;
  private boolean done = false;

  public JsonDocumentIterator(JsonDocumentStream s) {
    it = s.documentReaders().iterator();
  }

  @Override
  public boolean hasNext() {
    if (done) {
      return false;
    }
    if (reader == null) {
      if (it.hasNext()) {
        reader = it.next();
      } else {
        done = true;
      }
    }
    return reader != null ;
  }

  @Override
  public Document next() {
    if (!hasNext()) {
      throw new NoSuchElementException("next() called after hasNext() returned false");
    }
    Document rec = getDocumentFromStreamReader();
    reader = null;
    return rec;
  }

  private Document getDocumentFromStreamReader() {
    Stack<JsonValue> containerStack = new Stack<JsonValue>();
    EventType event;
    JsonDocument lastDocument = null;
    JsonValue currentContainer = null;

    while ((event = reader.next()) != null) {
      switch(event) {
      case START_MAP:
        JsonValue newRec = new JsonDocument();
        if (currentContainer != null) {
          appendTo(currentContainer, newRec);
        }
        currentContainer = newRec;
        containerStack.push(currentContainer);
        break;
      case END_MAP:
        if (!containerStack.empty() && containerStack.peek() instanceof JsonDocument) {
          lastDocument = (JsonDocument) containerStack.pop();
          if (!containerStack.empty()) {
            currentContainer = containerStack.peek();
          }
        } else {
          throw new DecodingException("Unable to decode document stream: " + containerStack.toString());
        }
        break;
      case START_ARRAY:
        JsonValue newList = new JsonList();
        if (currentContainer != null) {
          appendTo(currentContainer, newList);
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
          throw new DecodingException("Unable to decode document stream: " + containerStack.toString());
        }
        break;
      case NULL:
        appendTo(currentContainer, JsonValue.NULLKEYVALUE);
        break;
      case BOOLEAN:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getBoolean()));
        break;
      case BINARY:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getBinary()));
        break;
      case BYTE:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getByte()));
        break;
      case SHORT:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getShort()));
        break;
      case INT:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getInt()));
        break;
      case LONG:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getLong()));
        break;
      case FLOAT:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getFloat()));
        break;
      case DOUBLE:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getDouble()));
        break;
      case DECIMAL:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getDecimal()));
        break;
      case STRING:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getString()));
        break;
      case DATE:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getDate()));
        break;
      case TIME:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getTime()));
        break;
      case TIMESTAMP:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getTimestamp()));
        break;
      case INTERVAL:
        appendTo(currentContainer, JsonValueBuilder.initFrom(reader.getInterval()));
        break;
      default:
        throw new DecodingException("Unknown token type " + event + " from stream reader");
      }
    }
    if (!containerStack.empty()) {
      //this means we did not got the end of the document.
      throw new DecodingException("Error processing document");
    }
    return lastDocument;
  }

  private void appendTo(JsonValue currentContainer, JsonValue value) {
    if (currentContainer instanceof JsonDocument) {
      value.setKey(reader.getFieldName());
      ((JsonDocument)currentContainer).getRootMap().put(reader.getFieldName(), value);
    } else if (currentContainer instanceof JsonList) {
      ((JsonList)currentContainer).add(reader.getArrayIndex(), value);
    } else {
      throw new DecodingException("Unable to decode document stream: " + currentContainer.toString());
    }
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
