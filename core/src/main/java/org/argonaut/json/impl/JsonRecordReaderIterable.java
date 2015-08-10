/**
 * Copyright (c) 2014 MapR, Inc.
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
package org.argonaut.json.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.argonaut.FieldPath;
import org.argonaut.RecordReader;
import org.argonaut.Value.Type;
import org.argonaut.json.Events.Delegate;

class JsonRecordReaderIterable implements Iterable<RecordReader> {

  final private JsonRecordStream jsonRecordStream;
  final private Map<FieldPath, Type> fieldPathTypeMap;
  final private Delegate eventDelegate;

  JsonRecordReaderIterable(JsonRecordStream recordStream) {
    this.jsonRecordStream = recordStream;
    fieldPathTypeMap = recordStream.getFieldPathTypeMap();
    eventDelegate = recordStream.getEventDelegate();
  }

  @Override
  public Iterator<RecordReader> iterator() {
    return new Iterator<RecordReader>() {
      private JsonStreamRecordReader lastReader;
      private JsonStreamRecordReader currentReader;
      private boolean eos = false;

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

      @Override
      public RecordReader next() {
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
          // If a record was previously returned
          // ensure that its reader has consumed
          // its data from the underlying stream
          lastReader.readFully();
          lastReader = null;
        }
        if (!eos && currentReader == null) {
          if (eventDelegate != null) {
            currentReader = new DelegatingJsonRecordReader(jsonRecordStream, eventDelegate);
          } else if (fieldPathTypeMap != null) {
            currentReader = new TypeMappedJsonRecordReader(jsonRecordStream, fieldPathTypeMap);
          } else {
            currentReader = new JsonStreamRecordReader(jsonRecordStream);
          }
          eos = currentReader.eor();
        }
        return !eos;
      }
    };
  }

}
