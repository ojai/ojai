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
import java.util.Map;
import java.util.NoSuchElementException;

import org.ojai.DocumentReader;
import org.ojai.FieldPath;
import org.ojai.Value.Type;
import org.ojai.json.Events.Delegate;

class JsonDocumentReaderIterable implements Iterable<DocumentReader> {

  final private JsonDocumentStream jsonDocumentStream;
  final private Map<FieldPath, Type> fieldPathTypeMap;
  final private Delegate eventDelegate;

  JsonDocumentReaderIterable(JsonDocumentStream documentStream) {
    this.jsonDocumentStream = documentStream;
    fieldPathTypeMap = documentStream.getFieldPathTypeMap();
    eventDelegate = documentStream.getEventDelegate();
  }

  @Override
  public Iterator<DocumentReader> iterator() {
    return new Iterator<DocumentReader>() {
      private JsonStreamDocumentReader lastReader;
      private JsonStreamDocumentReader currentReader;
      private boolean eos = false;

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

      @Override
      public DocumentReader next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        lastReader = currentReader;
        currentReader = null;
        return lastReader;
      }

      @Override
      public boolean hasNext() {
        if (lastReader != null) {
          // If a document was previously returned
          // ensure that its reader has consumed
          // its data from the underlying stream
          lastReader.readFully();
          lastReader = null;
        }
        if (!eos && currentReader == null) {
          if (eventDelegate != null) {
            currentReader = new DelegatingJsonDocumentReader(jsonDocumentStream, eventDelegate);
          } else if (fieldPathTypeMap != null) {
            currentReader = new TypeMappedJsonDocumentReader(jsonDocumentStream, fieldPathTypeMap);
          } else {
            currentReader = new JsonStreamDocumentReader(jsonDocumentStream);
          }
          eos = currentReader.eor();
        }
        return !eos;
      }
    };
  }

}
