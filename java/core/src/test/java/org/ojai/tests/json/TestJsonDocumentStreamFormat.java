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
package org.ojai.tests.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Iterator;

import org.junit.Test;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.DocumentStream;
import org.ojai.json.Json;
import org.ojai.tests.BaseTest;

//unit test for parsing JSON text in array format
public class TestJsonDocumentStreamFormat extends BaseTest {

  @Test
  public void testFetchAndParseJsonDocumentStream() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/manydocs.json");
        DocumentStream stream = Json.newDocumentStream(in)) {

      int documentCount = 0;
      for (DocumentReader reader : stream.documentReaders()) {
        documentCount++;
        if (documentCount == 1) {
          validateDocumentReaderOne(reader);
        } else if (documentCount == 2) {
          validateDocumentReaderTwo(reader);
        } else {
          validateDocumentReaderThree(reader);
        }
      }
      assertEquals(3, documentCount);
    }
  }

  @Test
  public void testEmptyJsonFile() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/nodocs.json");
         DocumentStream stream = Json.newDocumentStream(in)) {
      int documentCount = getDocumentCount(stream.documentReaders());
      assertEquals(0, documentCount);
    }
  }

  @Test
  public void testEmptyJsonFileInArrayFormat() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/emptyjsonfileinarrayformat.json");
         DocumentStream stream = Json.newDocumentStream(in)) {
      int documentCount = getDocumentCount(stream.documentReaders());
      assertEquals(0, documentCount);
    }
  }

  @Test
  public void testParseStreamWithManyArrays() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/manyarray.json");
        DocumentStream stream = Json.newDocumentStream(in)) {

      int documentCount = 0;
      for (DocumentReader reader : stream.documentReaders()) {
        documentCount++;
        if (documentCount == 1) {
          validateDocumentReaderOne(reader);
        }
        if (documentCount == 2) {
          validateDocumentReaderTwo(reader);
        }
        if (documentCount == 3){
          validateDocumentReaderThree(reader);
        }
        if (documentCount == 5) {
          validateDocumentReaderFive(reader);
        }
      }
      assertEquals(5, documentCount);
    }
  }

  @Test
  public void testParseStreamWithManyMiscDocs() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/manymiscdocs.json");
        DocumentStream stream = Json.newDocumentStream(in)) {

      int documentCount = 0;
      for (DocumentReader reader : stream.documentReaders()) {
        documentCount++;
        if (documentCount == 1) {
          validateDocumentReaderOne(reader);
        }
        if (documentCount == 2) {
          validateDocumentReaderTwo(reader);
        }
        if (documentCount == 3){
          validateDocumentReaderThree(reader);
        }
        if (documentCount == 5) {
          validateMapDocBetweenArrays(reader);
        }
        if (documentCount == 6) {
          validateDocumentReaderFive(reader);
        }
      }
      assertEquals(7, documentCount);
    }
  }

  @Test
  public void testHybridFormat() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/hybridFormat.json");
        DocumentStream stream = Json.newDocumentStream(in)) {
      int documentCount = getDocumentCount(stream.documentReaders());
      assertEquals(6, documentCount);
    }
  }

  private void validateDocumentReaderOne(DocumentReader r) {
    assertEquals(EventType.START_MAP, r.next());
    assertEquals(EventType.STRING, r.next());
    assertEquals("1", r.getString());
    assertEquals(EventType.START_ARRAY, r.next());
    assertEquals("info", r.getFieldName());
    assertEquals(EventType.START_MAP, r.next());
    assertTrue(!r.inMap());
    assertEquals(EventType.START_MAP, r.next());
    assertEquals("name", r.getFieldName());
    assertEquals(EventType.STRING, r.next());
    assertEquals("John", r.getString());
    assertEquals(EventType.STRING, r.next());
    assertEquals("Doe", r.getString());
    assertEquals("last", r.getFieldName());
    assertEquals(EventType.END_MAP, r.next());
    assertTrue(r.inMap());
    assertEquals(EventType.END_MAP, r.next());
    assertEquals(EventType.START_MAP, r.next());
    assertEquals(EventType.DOUBLE, r.next());
    assertEquals(EventType.END_MAP, r.next());
    assertEquals(EventType.START_MAP, r.next());
    assertEquals(EventType.DOUBLE, r.next());
    assertEquals(EventType.END_MAP, r.next());
    assertEquals(EventType.END_ARRAY, r.next());
    assertEquals(EventType.END_MAP, r.next());
    assertNull(r.next());
  }

  private void validateDocumentReaderTwo(DocumentReader r) {
    assertEquals(EventType.START_MAP, r.next());
    assertEquals(EventType.STRING, r.next());
    assertEquals("2", r.getString());
    assertEquals(EventType.START_MAP, r.next());
    assertEquals("name", r.getFieldName());
    assertEquals(EventType.STRING, r.next());
    assertEquals(EventType.STRING, r.next());
    assertEquals(EventType.END_MAP, r.next());
    assertEquals(EventType.DOUBLE, r.next());
    assertEquals(EventType.END_MAP, r.next());
    assertNull(r.next());
  }

  private void validateDocumentReaderThree(DocumentReader r) {
    assertEquals(EventType.START_MAP, r.next());
    assertEquals(EventType.STRING, r.next());
    assertEquals("3", r.getString());
    assertEquals(EventType.START_ARRAY, r.next());
    assertEquals("name", r.getFieldName());
    assertEquals(EventType.STRING, r.next());
    assertEquals(EventType.STRING, r.next());
    assertEquals(EventType.STRING, r.next());
    assertTrue(!r.inMap());
    assertEquals(EventType.STRING, r.next());
    assertEquals(EventType.START_MAP, r.next());
    assertEquals(EventType.DOUBLE, r.next());
    assertEquals("age", r.getFieldName());
    assertEquals(EventType.END_MAP, r.next());
    assertEquals(EventType.END_ARRAY, r.next());
    assertEquals(EventType.END_MAP, r.next());
    assertNull(r.next());
  }

  private void validateDocumentReaderFive(DocumentReader r) {
    assertEquals(EventType.START_MAP, r.next());
    assertEquals(EventType.STRING, r.next());
    assertEquals("5", r.getString());
    assertEquals(EventType.START_MAP, r.next());
    assertEquals("name", r.getFieldName());
    assertEquals(EventType.STRING, r.next());
    assertEquals("first", r.getFieldName());
    assertTrue(r.inMap());
    assertEquals("Jack", r.getString());
    assertEquals(EventType.STRING, r.next());
    assertEquals("Jones", r.getString());
    assertEquals(EventType.END_MAP, r.next());
    assertEquals(EventType.DOUBLE, r.next());
    assertEquals("age", r.getFieldName());
    assertEquals(55.0, r.getDouble(), 0.0);
    assertEquals(EventType.END_MAP, r.next());
    assertNull(r.next());
  }

  private void validateMapDocBetweenArrays(DocumentReader r) {
    assertEquals(EventType.START_MAP, r.next());
    assertEquals(EventType.DOUBLE, r.next());
    assertEquals("a", r.getFieldName());
    assertEquals(123, r.getDouble(), 0.0);
    assertEquals(EventType.START_ARRAY, r.next());
    assertEquals("b", r.getFieldName());
    assertEquals(EventType.DOUBLE, r.next());
    assertEquals(EventType.DOUBLE, r.next());
    assertEquals(2, r.getDouble(), 0.0);
    assertTrue(!r.inMap());
    assertEquals(EventType.START_MAP, r.next());
    assertEquals(EventType.STRING, r.next());
    assertEquals("y1", r.getFieldName());
    assertTrue(r.inMap());
    assertEquals("pqr", r.getString());
    assertEquals(EventType.END_MAP, r.next());
    assertEquals(EventType.END_ARRAY, r.next());
    assertEquals(EventType.DOUBLE, r.next());
    assertEquals(1.1, r.getDouble(), 0.0);
    assertEquals(EventType.END_MAP, r.next());
    assertNull(r.next());
  }

  private int getDocumentCount(Iterable<DocumentReader> documentReaders) {
    int documentCount = 0;
    for(Iterator<DocumentReader> itr = documentReaders.iterator(); itr.hasNext(); itr.next()) {
      documentCount++;
    }
    return documentCount;
  }

}
