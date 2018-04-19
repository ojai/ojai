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
package org.ojai.tests.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentListener;
import org.ojai.DocumentStream;
import org.ojai.tests.BaseTest;
import org.ojai.util.EmptyDocumentStream;

public class TestEmptyDocumentStream extends BaseTest {

  @Test
  public void test_EmptyDocumentStream() {
    // Document iterator
    final DocumentStream stream1 = new EmptyDocumentStream();
    assertFalse(stream1.iterator().hasNext());
    stream1.close();

    // DocumentReader iterator
    final DocumentStream stream2 = new EmptyDocumentStream();
    assertFalse(stream2.documentReaders().iterator().hasNext());
    stream2.close();

    // streamTo
    final AtomicReference<Boolean> eosCalled = new AtomicReference<Boolean>(false);
    final AtomicReference<Boolean> failedCalled = new AtomicReference<Boolean>(false);
    final AtomicReference<Boolean> documentArrivedCalled = new AtomicReference<Boolean>(false);
    final DocumentStream stream3 = new EmptyDocumentStream();
    stream3.streamTo(new DocumentListener() {
      @Override
      public void eos() {
        eosCalled.set(true);
      }

      @Override
      public void failed(Exception e) {
        failedCalled.set(true);
      }

      @Override
      public boolean documentArrived(Document document) {
        documentArrivedCalled.set(true);
        return false;
      }
    });
    assertTrue(eosCalled.get());
    assertFalse(failedCalled.get());
    assertFalse(documentArrivedCalled.get());
    stream3.close();
  }

}
