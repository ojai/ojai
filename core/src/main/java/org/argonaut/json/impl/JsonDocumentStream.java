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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.argonaut.FieldPath;
import org.argonaut.Document;
import org.argonaut.DocumentListener;
import org.argonaut.DocumentReader;
import org.argonaut.DocumentStream;
import org.argonaut.Value.Type;
import org.argonaut.annotation.API;
import org.argonaut.exceptions.DecodingException;
import org.argonaut.exceptions.StreamInUseException;
import org.argonaut.json.Events;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

@API.Internal
public class JsonDocumentStream implements DocumentStream<Document> {

  private final InputStream inputStream;
  private JsonParser jsonParser;

  private final boolean readStarted;
  private volatile boolean iteratorOpened;

  private final Map<FieldPath, Type> fieldPathTypeMap;
  private final Events.Delegate eventDelegate;

  static DocumentStream<Document> newDocumentStream(FileSystem fs,
      Path path, Map<FieldPath, Type> map, Events.Delegate delegate)
          throws IllegalArgumentException, IOException {
    final InputStream in = fs.open(path);
    return new JsonDocumentStream(in, map, delegate) {
      @Override
      public void close() throws IOException {
        try {
          super.close();
        } finally {
          in.close();
        }
      }
    };
  }

  public static DocumentStream<Document> newDocumentStream(FileSystem fs,
      String path, Map<FieldPath, Type> map, Events.Delegate delegate)
          throws IllegalArgumentException, IOException {
    return newDocumentStream(fs, new Path(path), map, delegate);
  }

  public JsonDocumentStream(InputStream in,
      Map<FieldPath, Type> fieldPathTypeMap, Events.Delegate eventDelegate) {
    inputStream = in;
    readStarted = false;
    iteratorOpened = false;
    this.eventDelegate = eventDelegate;
    this.fieldPathTypeMap = fieldPathTypeMap;
    try {
      JsonFactory jFactory = new JsonFactory();
      /* setting explicitly AUTO_CLOSE_SOURCE = false to ensure that
       * jsonParser.close() do not close the underlying inputstream.
       * It has to be closed by the owner of the stream.
       */
      jFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
      jsonParser = jFactory.createParser(inputStream);
    } catch (IOException e) {
      throw new DecodingException(e);
    }
  }

  @Override
  public Iterable<DocumentReader> documentReaders() {
    checkStateForIteration();
    iteratorOpened = true;
    return new JsonDocumentReaderIterable(this);
  }


  @Override
  public synchronized Iterator<Document> iterator() {
    checkStateForIteration();
    return new JsonDocumentIterator(this);
  }

  @Override
  public void streamTo(DocumentListener l) {
    try {
      for (Document document : this) {
        l.documentArrived(document);
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

  Events.Delegate getEventDelegate() {
    return eventDelegate;
  }

  private void checkStateForIteration() {
    if (readStarted) {
      throw new StreamInUseException("Can not create iterator after reading from the stream has started.");
    } else if (iteratorOpened) {
      throw new StreamInUseException("An iterator has already been opened on this document stream.");
    }
  }

}
