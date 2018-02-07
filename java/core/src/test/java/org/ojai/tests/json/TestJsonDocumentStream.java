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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentListener;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.DocumentStream;
import org.ojai.FieldPath;
import org.ojai.Value.Type;
import org.ojai.json.Json;
import org.ojai.json.JsonOptions;
import org.ojai.tests.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class TestJsonDocumentStream extends BaseTest {
  private static Logger logger = LoggerFactory
      .getLogger(TestJsonDocumentStream.class);

  @Test
  public void testFetchAndParseJsonDocumentStream() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/business.json");
         DocumentStream stream = Json.newDocumentStream(in)) {

      int documentCount = 0;
      for (DocumentReader reader : stream.documentReaders()) {
        documentCount++;
        testDocumentReaderFromIterator(reader);
      }
      assertEquals(5, documentCount);
    }
  }

  private void testDocumentReaderFromIterator(DocumentReader r) {
    EventType et;
    String name_field = null;
    while ((et = r.next()) != null) {
      if (et == EventType.STRING && r.inMap()) {
        if (r.getFieldName().equals("name")) {
          name_field = r.getString();
        }
      } else {
        if ((et == EventType.DOUBLE) && (r.getFieldName().equals("stars"))
            && (name_field.equals("Culver's"))) {
          assertEquals(4.5, r.getDouble(), 0.0);
        }
      }
    }
  }

  @Test
  public void testFetchAndParseTypeMappedJsonDocumentStream() throws Exception {
    Map<FieldPath, Type> fieldPathTypeMap = Maps.newHashMap();
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Monday.open"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Monday.close"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Tuesday.open"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Tuesday.close"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Wednesday.open"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Wednesday.close"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Thursday.open"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Thursday.close"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Friday.open"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Friday.close"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Saturday.open"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Saturday.close"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Sunday.close"), Type.TIME);
    fieldPathTypeMap.put(FieldPath.parseFrom("hours.Sunday.close"), Type.TIME);

    try (InputStream in = getJsonStream("org/ojai/test/data/business.json");
         DocumentStream stream = Json.newDocumentStream(in, fieldPathTypeMap)) {

      int documentCount = 0;
      for (DocumentReader reader : stream.documentReaders()) {
        documentCount++;
        logger.debug(Json.toJsonString(reader, JsonOptions.WITH_TAGS));
      }
      assertEquals(5, documentCount);
    }
  }

  @Test
  public void testFetchAndParsePartiallyJsonDocumentStream() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/business.json");
         DocumentStream stream = Json.newDocumentStream(in)) {

      int documentCount = 0;
      for (DocumentReader reader : stream.documentReaders()) {
        documentCount++;
        logger.debug("First event in the DocumentReader: " + reader.next());
      }
      assertEquals(5, documentCount);
    }
  }

  @Test
  public void testDocumentIterator() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/multidocument.json");
         DocumentStream stream = Json.newDocumentStream(in)) {

      int documentCount = 0;
      Iterator<Document> it = stream.iterator();
      Document document;
      while (it.hasNext()) {
        document = it.next();
        testDocumentElements(document);
        documentCount++;
      }
      assertEquals(4, documentCount);
    }
  }

  private void testDocumentElements(Document rec) {
    DocumentReader r = rec.asReader();
    EventType et;
    String id = null;
    boolean isArray = false;
    while ((et = r.next()) != null) {
      if (et == EventType.START_ARRAY) {
        isArray = true;
      } else if (et == EventType.END_ARRAY) {
        isArray = false;
      } else if (et == EventType.STRING && r.inMap()) {

        if (r.getFieldName().equals("business_id")) {
          id = r.getString();
        }
        if ((r.getFieldName().equals("street")) && (id.equals("id3"))) {
          assertEquals("Lint St", r.getString());
        }

        if ((isArray) && (r.getFieldName().equals("first"))) {
          assertEquals("Jerry", r.getString());
        }

      } else {
        if ((et == EventType.LONG) && (id.equals("id2"))) {
          assertEquals(45, r.getLong());
        }
        if ((et == EventType.BOOLEAN) && id.equals("id1")) {
          assertEquals(true, r.getBoolean());
        }

      }
    }
  }

  @Test
  public void testDocumentIteratorNextMethod() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/multidocument.json");
         DocumentStream stream = Json.newDocumentStream(in)) {

      int documentCount = 0;
      Iterator<Document> it = stream.iterator();

      Document rec ;
      try {
        while ((rec = it.next()) != null) {
          documentCount++;
          if (documentCount == 1) {
            assertEquals("John", rec.getString("name.first"));
          }
        }
      } catch (Exception e) {
        assertEquals(4, documentCount);
      }
    }
  }

  @Test
  public void testDocumentListener() throws IOException {
    final Document status = Json.newDocument();

    try (InputStream in = getJsonStream("org/ojai/test/data/multidocument.json");
        DocumentStream stream = Json.newDocumentStream(in)) {
      stream.streamTo(new DocumentListener() {
        int documentProcessed = 0;
        @Override
        public boolean documentArrived(Document document) {
          documentProcessed++;
          status.set("documentArrived", true);
          logger.info("Document arrived: %s", document.asJsonString());
          if ("id3".equals(document.getString("business_id"))) {
            return false;
          } else {
            return true;
          }
        }

        @Override
        public void failed(Exception e) {
          status.set("failed", true);
        }

        @Override
        public void eos() {
          status.set("eos", true);
          assertEquals(3, documentProcessed);
        }
      });

      assertEquals(true, status.getBoolean("documentArrived"));
      assertNull(status.getBooleanObj("failed"));
      assertEquals(true, status.getBoolean("eos"));
    }
  }

  @Test
  public void testDocumentListenerError() throws IOException {
    final Document status = Json.newDocument();
    try (InputStream in = getJsonStream("org/ojai/test/data/multidocument.json");
        DocumentStream stream = Json.newDocumentStream(in)) {

      stream.iterator(); // open an iterator and ignore it

      stream.streamTo(new DocumentListener() {
        @Override
        public boolean documentArrived(Document document) {
          status.set("documentArrived", true);
          return false;
        }

        @Override
        public void failed(Exception e) {
          status.set("failed", true);
        }

        @Override
        public void eos() {
          status.set("eos", true);
        }
      });

      assertNull(status.getBooleanObj("documentArrived"));
      assertEquals(true, status.getBoolean("failed"));
      assertNull(status.getBooleanObj("eos"));
    }
  }

}
