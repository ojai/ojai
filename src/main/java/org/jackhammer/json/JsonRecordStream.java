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
import java.util.NoSuchElementException;

import org.jackhammer.Record;
import org.jackhammer.RecordListener;
import org.jackhammer.RecordStream;
import org.jackhammer.exceptions.DecodingException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

public class JsonRecordStream implements RecordStream<Record> {

  private InputStream inputStream;
  private JsonParser jsonParser;

  private boolean readStarted;
  private volatile boolean iteratorOpened;

  public JsonRecordStream(InputStream in) {
    inputStream = in;
    readStarted = false;
    iteratorOpened = false;
    try {
      JsonFactory jFactory = new JsonFactory();
      jsonParser = jFactory.createParser(inputStream);
    } catch (IOException e) {
      throw new DecodingException(e);
    }
  }

  @Override
  public void close() throws IOException {
    jsonParser.close();
  }

  @Override
  public synchronized Iterator<Record> iterator() {
    if (readStarted) {
      throw new IllegalStateException("Can not create iterator after reading from the stream has started.");
    } else if (iteratorOpened) {
      throw new IllegalStateException("An iterator has already been opened on this record stream.");
    }
    iteratorOpened = true;

    return new Iterator<Record>() {
      private JsonRecordReader lastReader;
      private JsonRecordReader currentReader;
      private boolean eos = false;

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Record next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        lastReader = currentReader;
        currentReader = null;
        return new JsonRecord(lastReader);
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
          currentReader = new JsonRecordReader(JsonRecordStream.this);
          eos = currentReader.eor();
        }
        return !eos;
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

  public JsonParser getParser() {
    return jsonParser;
  }

}
