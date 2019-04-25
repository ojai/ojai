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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.ojai.Document;
import org.ojai.DocumentListener;
import org.ojai.DocumentReader;
import org.ojai.DocumentStream;
import org.ojai.FieldPath;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.OjaiException;
import org.ojai.exceptions.StreamInUseException;
import org.ojai.json.Events;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

@API.Internal
public class JsonDocumentStream implements DocumentStream {

  private final InputStream inputStream;
  private JsonParser jsonParser;

  private final boolean readStarted;
  private volatile boolean iteratorOpened;

  private final Map<FieldPath, Type> fieldPathTypeMap;
  private final Events.Delegate eventDelegate;

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

  /**
   * Returns the current position in the underlying InputStream
   */
  public long getInputStreamPosition() {
    return jsonParser.getTokenLocation().getByteOffset();
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
  public void streamTo(DocumentListener listener) {
    Exception failure = null;
    try {
      for (Document doc : this) {
        if (!listener.documentArrived(doc)) {
          break;
        }
      }
    } catch (Exception e) {
      failure = e;
    } finally {
      try {
        close();
      } catch (Exception e) {
        if (failure == null) {
          failure = e;
        }
      }
    }

    if (failure == null) {
      listener.eos();
    } else {
      listener.failed(failure);
    }
  }

  @Override
  public void close() {
    try {
      jsonParser.close();
    } catch (IOException e) {
      throw new OjaiException(e);
    }
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
