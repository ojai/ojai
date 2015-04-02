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
package org.jackhammer.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.jackhammer.FieldPath;
import org.jackhammer.Record;
import org.jackhammer.RecordListener;
import org.jackhammer.RecordReader;
import org.jackhammer.RecordStream;
import org.jackhammer.Value.Type;
import org.jackhammer.exceptions.DecodingException;
import org.jackhammer.exceptions.StreamInUseException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

public class JsonRecordStream implements RecordStream<Record> {

  private InputStream inputStream;
  private JsonParser jsonParser;

  private boolean readStarted;
  private volatile boolean iteratorOpened;
  private Map<FieldPath, Type> fieldPathTypeMap;

  public JsonRecordStream(InputStream in) {
    this(in, null);
  }

  public JsonRecordStream(InputStream in, Map<FieldPath, Type> fieldType) {
    inputStream = in;
    readStarted = false;
    iteratorOpened = false;
    this.fieldPathTypeMap = fieldType;
    try {
      JsonFactory jFactory = new JsonFactory();
      jsonParser = jFactory.createParser(inputStream);
    } catch (IOException e) {
      throw new DecodingException(e);
    }
  }

  @Override
  public Iterable<RecordReader> recordReaders() {
    checkStateForIteration();
    iteratorOpened = true;
    return new JsonRecordStreamReaders(this);
  }

  @Override
  public synchronized Iterator<Record> iterator() {
    checkStateForIteration();
    /*
     * TODO: Implement a Iterator returning instance of JsonRecord
     */
    return new Iterator<Record>() {

      @Override
      public void remove() {
        // TODO Auto-generated method stub
      }

      @Override
      public Record next() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public boolean hasNext() {
        // TODO Auto-generated method stub
        return false;
      }
    };
  }

  @Override
  public void streamTo(RecordListener l) {
    try {
      for (Record record : this) {
        l.recordArrived(record);
      }
    } catch (Exception e) {
      try {
        close();
      } catch (IOException e1) {}
      l.failed(e);
    }
  }

  @Override
  public void close() throws IOException {
    jsonParser.close();
  }

  JsonParser getParser() {
    return jsonParser;
  }

  Map<FieldPath, Type> getFieldPathTypeMap() {
    return fieldPathTypeMap;
  }

  private void checkStateForIteration() {
    if (readStarted) {
      throw new StreamInUseException("Can not create iterator after reading from the stream has started.");
    } else if (iteratorOpened) {
      throw new StreamInUseException("An iterator has already been opened on this record stream.");
    }
  }

}
