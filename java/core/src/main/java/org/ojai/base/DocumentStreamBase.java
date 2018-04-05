/**
 * Copyright (c) 2018 MapR, Inc.
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
package org.ojai.base;

import java.util.Iterator;

import org.ojai.Document;
import org.ojai.DocumentListener;
import org.ojai.DocumentReader;
import org.ojai.DocumentStream;
import org.ojai.exceptions.OjaiException;

/**
 * Abstract implementation of DocumentStream.
 */
public abstract class DocumentStreamBase implements DocumentStream {
  /**
   * This is the only method deriving class MUST implement.
   */
  abstract protected Iterator<Document> iteratorDerived();

  /**
   * Indicates that one of documentReaders(), iterator(), or streamTo() has been called.
   */
  protected boolean isUsed;

  /**
   * Indicates that the stream has been closed.
   */
  private boolean isClosed;

  /**
   * Perform whatever steps the derived class requires for closing.
   */
  protected void closeDerived() throws OjaiException {}

  /**
   * Checks to see if the stream is open or not.
   *
   * @throws IllegalStateException if the stream has been closed
   */
  protected void checkOpen() {
    if (isClosed) {
      throw new IllegalStateException("DocumentStream is already closed.");
    }
  }

  /**
   * Checks to see that the state is valid (open, not canceled or closed) for requesting
   * data.
   *
   * @throws IllegalStateException if the stream is closed or if one of the data retrieval
   *   methods (documentReaders(), iterator(), or streamTo()) has been used once already
   */
  protected void checkAndSetInUse() {
    checkOpen();
    if (isUsed) {
      throw new IllegalStateException("DocumentStream is already in use.");
    }
    isUsed = true;
  }

  /**
   * @return true if the stream has been closed
   */
  protected boolean isClosed() {
    return isClosed;
  }

  @Override
  public final Iterator<Document> iterator() {
    checkAndSetInUse();
    return iteratorDerived();
  }

  /**
   * Closes this {@link DocumentStream}.<p/>
   * If a derived class requires any closing steps, it should override {@link #closeDerived()}.
   */
  @Override
  public final void close() throws OjaiException {
    if (isClosed) {
      return;
    }
    closeDerived();
    isClosed = true;
  }

  protected Iterable<DocumentReader> documentReadersDerived() {
    return new ReaderIterable(iteratorDerived());
  }

  @Override
  public final Iterable<DocumentReader> documentReaders() {
    checkAndSetInUse();
    return documentReadersDerived();
  }

  @Override
  public void streamTo(DocumentListener docListener) {
    try {
      for(Document doc : this) {
        docListener.documentArrived(doc);
      }
      docListener.eos();
    } catch(Exception ex) {
      docListener.failed(ex);
    }
  }

  /**
   * Trivial implementation of DocumentReader Iterator
   */
  private static class ReaderIterator implements Iterator<DocumentReader> {
    private final Iterator<Document> docIter;

    public ReaderIterator(Iterator<Document> docIter) {
      this.docIter = docIter;
    }

    @Override
    public boolean hasNext() {
      return docIter.hasNext();
    }

    @Override
    public DocumentReader next() {
      return docIter.next().asReader();
    }
  }

  /**
   * Trivial implementation of DocumentReader Iterable
   */
  private class ReaderIterable implements Iterable<DocumentReader> {
    private final Iterator<Document> docIter;

    public ReaderIterable(Iterator<Document> docIter) {
      this.docIter = docIter;
    }

    @Override
    public Iterator<DocumentReader> iterator() {
      return new ReaderIterator(docIter);
    }
  }

}
