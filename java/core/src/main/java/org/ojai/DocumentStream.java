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
package org.ojai;

import java.util.Iterator;

import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.exceptions.OjaiException;

/**
 * A stream of documents.
 *
 * Implements Iterable<Document> but only one call is allows to iterator()
 * or readerIterator(). Only one of these iterators can be retrieved
 * from the stream.
 */
@API.Public
@API.NotThreadSafe
public interface DocumentStream extends AutoCloseable, Iterable<Document> {

  /**
   * Streams all the documents in this {@code DocumentStream} to the specified
   * listener.
   *
   * @param listener a {@code DocumentListener} which is notified of
   *        {@code Document}s as they arrive
   * @throws StreamInUseException if an iterator is already retrieved from this
   *         {@code DocumentStream}
   */
   void streamTo(@NonNullable DocumentListener listener);

  /**
   * Returns an iterator over a set of {@code Document}.
   * @throws StreamInUseException if an iterator is already retrieved from this
   *         {@code DocumentStream}
   */
  Iterator<Document> iterator();

  /**
   * Returns an {@code Iterable} over a set of {@code DocumentReader}.
   * @throws StreamInUseException if an iterator is already retrieved from this
   *         {@code DocumentStream}
   */
  Iterable<DocumentReader> documentReaders();

  /**
   * Overridden to remove checked exception
   */
  void close() throws OjaiException;

}
