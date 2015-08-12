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
package org.argonaut.tests.json;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.argonaut.FieldPath;
import org.argonaut.Document;
import org.argonaut.DocumentReader;
import org.argonaut.DocumentStream;
import org.argonaut.DocumentReader.EventType;
import org.argonaut.Value.Type;
import org.argonaut.json.Json;
import org.argonaut.tests.BaseTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class TestJsonDocumentStream extends BaseTest {
  private static Logger logger = LoggerFactory
      .getLogger(TestJsonDocumentStream.class);

  @Test
  public void testFetchAndParseJsonDocumentStream() throws Exception {
    try (InputStream in = getJsonStream("business.json");
        DocumentStream<Document> stream = Json.newDocumentStream(in)) {

      int documentCount = 0;
      for (DocumentReader reader : stream.documentReaders()) {
        documentCount++;
        testDocumentReaderFromIterator(reader);
      }
      assertEquals(5, documentCount);
    }
  }

  private void testDocumentReaderFromIterator(DocumentReader reader) {
    EventType et;
    String name_field = null;
    String fieldName = null;
    while ((et = reader.next()) != null) {
      if (et == EventType.FIELD_NAME) {
        fieldName = reader.getFieldName();
      } else if (et == EventType.STRING) {
        if (fieldName.equals("name")) {
          name_field = reader.getString();
        }
      } else {
        if ((et == EventType.DOUBLE) && (fieldName.equals("stars"))
            && (name_field.equals("Culver's"))) {
          assertEquals(4.5, reader.getDouble(), 0.0);
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

    try (InputStream in = getJsonStream("business.json");
        DocumentStream<Document> stream = Json.newDocumentStream(in, fieldPathTypeMap)) {

      int documentCount = 0;
      for (DocumentReader reader : stream.documentReaders()) {
        documentCount++;
        logger.debug(Json.toJsonString(reader, false));
      }
      assertEquals(5, documentCount);
    }
  }

  @Test
  public void testFetchAndParsePartiallyJsonDocumentStream() throws Exception {
    try (InputStream in = getJsonStream("business.json");
        DocumentStream<Document> stream = Json.newDocumentStream(in)) {

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
    try (InputStream in = getJsonStream("multidocument.json");
        DocumentStream<Document> stream = Json.newDocumentStream(in)) {

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
    DocumentReader domReader = rec.asReader();
    EventType et;
    String id = null;
    String fieldName = null;
    boolean isArray = false;
    while ((et = domReader.next()) != null) {
      if (et == EventType.FIELD_NAME) {
        fieldName = domReader.getFieldName();
      } else if (et == EventType.START_ARRAY) {
        isArray = true;
      } else if (et == EventType.END_ARRAY) {
        isArray = false;
      } else if (et == EventType.STRING) {

        if (fieldName.equals("business_id")) {
          id = domReader.getString();
        }
        if ((fieldName.equals("street")) && (id.equals("id3"))) {
          assertEquals("Lint St", domReader.getString());
        }

        if ((isArray) && (fieldName.equals("first"))) {
          assertEquals("Jerry", domReader.getString());
        }

      } else {
        if ((et == EventType.LONG) && (id.equals("id2"))) {
          assertEquals(45, domReader.getLong());
        }
        if ((et == EventType.BOOLEAN) && id.equals("id1")) {
          assertEquals(true, domReader.getBoolean());
        }

      }
    }
  }

  @Test
  public void testDocumentIteratorNextMethod() throws Exception {
    try (InputStream in = getJsonStream("multidocument.json");
        DocumentStream<Document> stream = Json.newDocumentStream(in)) {

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

}
