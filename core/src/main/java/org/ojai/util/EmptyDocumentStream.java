/**
 * Copyright (c) 2017 MapR, Inc.
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
package org.ojai.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.ojai.Document;
import org.ojai.DocumentListener;
import org.ojai.DocumentReader;
import org.ojai.DocumentStream;
import org.ojai.exceptions.OjaiException;
import org.ojai.json.impl.JsonDocument;

/**
 * A DocumentStream that returns no documents.
 */
public class EmptyDocumentStream implements DocumentStream {
  private boolean isUsed;
  private boolean isClosed;

  @Override
  public Iterator<Document> iterator() {
    checkState();
    return new EmptyIterator<Document>();
  }

  @Override
  public void streamTo(DocumentListener listener) {
    checkState();
    listener.eos();
  }

  @Override
  public Iterable<DocumentReader> documentReaders() {
    checkState();
    return new EmptyIterable<DocumentReader>();
  }

  @Override
  public void close() throws OjaiException {
    isClosed = true;
  }

  private void checkState() {
    checkOpen();
    if (isUsed) {
      throw new IllegalStateException("DocumentStream has already been used once");
    }
  }
  private void checkOpen() {
    if (isClosed) {
      throw new IllegalStateException("DocumentStream is already closed");
    }
  }

  /**
   * An empty Iterator that returns nothing.
   */
  private static class EmptyIterator<T> implements Iterator<T> {
    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public T next() {
      throw new NoSuchElementException();
    }
  }

  /**
   * An empty Iterable that returns nothing.
   */
  private static class EmptyIterable<T> implements Iterable<T> {
    @Override
    public Iterator<T> iterator() {
      return new EmptyIterator<T>();
    }
  }

  @Override
  public Document getQueryPlan() {
    return new JsonDocument();
  }
}
