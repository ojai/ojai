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
import org.ojai.base.DocumentStreamBase;

/**
 * A DocumentStream that returns no documents.
 */
public class EmptyDocumentStream extends DocumentStreamBase {

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

  @Override
  protected Iterator<Document> iteratorDerived() {
    return new EmptyIterator<>();
  }

}
