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
package org.ojai.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.DocumentStream;
import org.ojai.FieldPath;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.beans.BeanCodec;
import org.ojai.exceptions.DecodingException;
import org.ojai.json.impl.JsonDocument;
import org.ojai.json.impl.JsonDocumentBuilder;
import org.ojai.json.impl.JsonDocumentStream;
import org.ojai.json.impl.JsonUtils;
import org.ojai.json.impl.JsonValueBuilder;
import org.ojai.store.ValueBuilder;
import org.ojai.util.Documents;

import com.google.common.base.Preconditions;

/**
 * This class serves as a factory for a JSON implementation
 * of all OJAI interfaces.
 */
@API.Public
@API.Factory
public final class Json {

  /**
   * Returns a new, empty Document.
   */
  public static Document newDocument() {
    return new JsonDocument();
  }

  /**
   * Returns a ValueBuilder object.
   */
  public static ValueBuilder getValueBuilder() {
    return JsonValueBuilder.INSTANCE;
  }

  /**
   * Returns a Document built from the specified JSON string.
   */
  public static Document newDocument(@NonNullable String jsonString)
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

  /**
   * Returns a new instance of a Document built from the specified Java bean.
   */
  public static Document newDocument(@NonNullable Object bean) throws DecodingException {
    Preconditions.checkNotNull(bean);
    return BeanCodec.decode(newDocumentBuilder(), bean);
  }

  /**
   * Returns a new instance of a Document built from the specified Map.
   */
  public static <T extends Object> Document newDocument(@NonNullable Map<String, T> map)
      throws DecodingException {
    return (Document) JsonValueBuilder.initFrom(map);
  }

  /**
   * Returns a new instance of the JSON DocumentReader.
   */
  public static DocumentReader newDocumentReader(@NonNullable String jsonString)
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

  /**
   * Returns a new instance of JSON DocumentBuilder.
   */
  public static DocumentBuilder newDocumentBuilder() {
    return new JsonDocumentBuilder();
  }

  /**
   * Returns a new instance of JSON DocumentBuilder with the specified JsonOptions.
   */
  public static DocumentBuilder newDocumentBuilder(@NonNullable JsonOptions options) {
    return new JsonDocumentBuilder().setJsonOptions(options);
  }

  /**
   * Returns a new instance of JSON DocumentStream from the specified InputStream.
   */
  public static DocumentStream newDocumentStream(@NonNullable InputStream in) {
    return new JsonDocumentStream(in, null, null);
  }

  /**
   * Returns a new instance of s JSON DocumentStream from the specified InputStream
   * using the FieldPath => Type mapping to decode the JSON tokens from the stream.
   */
  public static DocumentStream newDocumentStream(
      @NonNullable InputStream in, @NonNullable Map<FieldPath, Type> fieldPathTypeMap) {
    return new JsonDocumentStream(in, fieldPathTypeMap, null);
  }

  public static DocumentStream newDocumentStream(
      @NonNullable InputStream in, @NonNullable Events.Delegate eventDelegate) {
    return new JsonDocumentStream(in, null, eventDelegate);
  }

  public static DocumentStream newDocumentStream(
      @NonNullable FileSystem fs, @NonNullable String path)
          throws DecodingException, IOException {
    return JsonDocumentStream.newDocumentStream(fs, path, null, null);
  }

  public static DocumentStream newDocumentStream(
      @NonNullable FileSystem fs, @NonNullable String path, @NonNullable Map<FieldPath, Type> fieldPathTypeMap)
          throws DecodingException, IOException {
    return JsonDocumentStream.newDocumentStream(fs, path, fieldPathTypeMap, null);
  }

  public static DocumentStream newDocumentStream(
      @NonNullable FileSystem fs, @NonNullable String path, @NonNullable Events.Delegate eventDelegate)
          throws DecodingException, IOException {
    return JsonDocumentStream.newDocumentStream(fs, path, null, eventDelegate);
  }

  public static <T> T encode(@NonNullable String jsonString, @NonNullable Class<T> beanClass) {
    return BeanCodec.encode(newDocumentReader(jsonString), beanClass);
  }

  public static String toJsonString(@NonNullable Document d) {
    return Json.toJsonString(d.asReader(), JsonOptions.DEFAULT);
  }

  public static String toJsonString(@NonNullable Document d, @NonNullable JsonOptions options) {
    return Json.toJsonString(d.asReader(), options);
  }

  public static String toJsonString(@NonNullable DocumentReader r) {
    return toJsonString(r, JsonOptions.DEFAULT);
  }

  public static String toJsonString(@NonNullable DocumentReader r, @NonNullable JsonOptions options) {
    JsonDocumentBuilder builder = new JsonDocumentBuilder().setJsonOptions(options);
    EventType e = r.next();
    switch (e) {
    case START_MAP:
      builder.addNewMap();
      JsonUtils.addToMap(r, builder);
      break;
    case START_ARRAY:
      builder.setCheckContext(false).addNewArray().setCheckContext(true);
      JsonUtils.addToArray(r, builder);
      break;
    default:
      JsonUtils.addReaderEvent(e, r, builder.setCheckContext(false));
      break;
    }
    return builder.asUTF8String();
  }

  /**
   * @deprecated Use {@link Documents#writeReaderToBuilder(DocumentReader, DocumentBuilder)} 
   */
  public static void writeReaderToBuilder(@NonNullable DocumentReader r, @NonNullable DocumentBuilder w) {
    Documents.writeReaderToBuilder(r, w);
  }

}
