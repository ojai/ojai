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
package org.argonaut.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.argonaut.FieldPath;
import org.argonaut.Document;
import org.argonaut.DocumentReader;
import org.argonaut.DocumentStream;
import org.argonaut.DocumentBuilder;
import org.argonaut.DocumentReader.EventType;
import org.argonaut.Value.Type;
import org.argonaut.annotation.API;
import org.argonaut.exceptions.DecodingException;
import org.argonaut.json.impl.JsonDocument;
import org.argonaut.json.impl.JsonDocumentStream;
import org.argonaut.json.impl.JsonDocumentBuilder;
import org.argonaut.json.impl.JsonUtils;

import com.google.common.base.Preconditions;

/**
 * This class serves as factory for JSON implementation
 * of all Argonaut interfaces.
 */
@API.Public
public final class Json {

  public static Document newDocument() {
    return new JsonDocument();
  }

  public static Document newDocument(String jsonString)
      throws DecodingException {
    try {
      InputStream jsonStream =
          new ByteArrayInputStream(JsonUtils.getBytes(jsonString));
      return newDocumentStream(jsonStream).iterator().next();
    } catch (DecodingException e) {
      throw e;
    } catch (Exception e) {
      throw new DecodingException(e);
    }
  }

  public static DocumentReader newDocumentReader(String jsonString)
      throws DecodingException {
    try {
      InputStream jsonStream =
          new ByteArrayInputStream(JsonUtils.getBytes(jsonString));
      return newDocumentStream(jsonStream).documentReaders().iterator().next();
    } catch (DecodingException e) {
      throw e;
    } catch (Exception e) {
      throw new DecodingException(e);
    }
  }

  public static DocumentBuilder newDocumentBuilder() {
    return new JsonDocumentBuilder();
  }

  public static DocumentBuilder newDocumentBuilder(JsonOptions options) {
    return new JsonDocumentBuilder().setJsonOptions(options);
  }

  public static DocumentStream<Document> newDocumentStream(InputStream in) {
    return new JsonDocumentStream(in, null, null);
  }

  public static DocumentStream<Document> newDocumentStream(
      InputStream in, Map<FieldPath, Type> fieldPathTypeMap) {
    return new JsonDocumentStream(in, fieldPathTypeMap, null);
  }

  public static DocumentStream<Document> newDocumentStream(
      InputStream in, Events.Delegate eventDelegate) {
    return new JsonDocumentStream(in, null, eventDelegate);
  }

  public static DocumentStream<Document> newDocumentStream(
      FileSystem fs, String path)
          throws DecodingException, IOException {
    return JsonDocumentStream.newDocumentStream(fs, path, null, null);
  }

  public static DocumentStream<Document> newDocumentStream(
      FileSystem fs, String path, Map<FieldPath, Type> fieldPathTypeMap)
          throws DecodingException, IOException {
    return JsonDocumentStream.newDocumentStream(fs, path, fieldPathTypeMap, null);
  }

  public static DocumentStream<Document> newDocumentStream(
      FileSystem fs, String path, Events.Delegate eventDelegate)
          throws DecodingException, IOException {
    return JsonDocumentStream.newDocumentStream(fs, path, null, eventDelegate);
  }

  public static String toJsonString(Document d) {
    return Json.toJsonString(d.asReader(), JsonOptions.DEFAULT);
  }

  public static String toJsonString(Document d, JsonOptions options) {
    return Json.toJsonString(d.asReader(), options);
  }

  public static String toJsonString(DocumentReader r) {
    return toJsonString(r, JsonOptions.DEFAULT);
  }

  public static String toJsonString(DocumentReader r, JsonOptions options) {
    EventType e = r.next();
    Preconditions.checkState((e == EventType.START_MAP),
        "Expected %s, found %s", EventType.START_MAP, e);
    JsonDocumentBuilder builder = new JsonDocumentBuilder()
        .setJsonOptions(options)
        .addNewMap();
    JsonUtils.addToMap(r, builder);
    return builder.asUTF8String();
  }

  public static void writeReaderToStream(DocumentReader r, DocumentBuilder w) {
    JsonUtils.addToMap(r, w);
  }

}
