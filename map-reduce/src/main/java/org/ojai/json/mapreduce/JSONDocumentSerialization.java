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
package org.ojai.json.mapreduce;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.serializer.Deserializer;
import org.apache.hadoop.io.serializer.Serialization;
import org.apache.hadoop.io.serializer.Serializer;
import org.ojai.Document;
import org.ojai.DocumentReader;
import org.ojai.DocumentStream;
import org.ojai.json.Json;
import org.ojai.json.impl.JsonDocument;

public class JSONDocumentSerialization extends Configured implements
Serialization<JsonDocument> {

  @Override
  public boolean accept(Class<?> arg0) {
    return JsonDocument.class.isAssignableFrom(arg0);
  }

  @Override
  public Deserializer<JsonDocument> getDeserializer(Class<JsonDocument> arg0) {
    return new JsonDocumentDeserializer();
  }

  @Override
  public Serializer<JsonDocument> getSerializer(Class<JsonDocument> arg0) {
    return new JsonDocumentSerializer();
  }

  private static class JsonDocumentDeserializer implements
  Deserializer<JsonDocument> {

    private DocumentStream<Document> stream;
    private Iterator<Document> iter;

    @Override
    public void close() throws IOException {
      try {
        stream.close();
      } catch (Exception e) {
        throw new IOException(e);
      }
    }

    @Override
    public JsonDocument deserialize(JsonDocument arg0) throws IOException {
      if (iter.hasNext()) {
        return (JsonDocument) iter.next();
      }
      return null;
    }

    @Override
    public void open(InputStream in) throws IOException {
      stream = Json.newDocumentStream(in);
      iter = stream.iterator();
    }

  }

  private static class JsonDocumentSerializer implements Serializer<JsonDocument> {

    private OutputStream out;
    private JSONFileRecordWriter writer = null;

    @Override
    public void close() throws IOException {
      out.close();
    }

    @Override
    public void open(OutputStream arg0) throws IOException {
      out = arg0;
    }

    @Override
    public void serialize(JsonDocument arg0) throws IOException {
      writer = new JSONFileRecordWriter(out);
      if (writer == null) {
        throw new IOException(
            "Output stream is not available for serialization.");
      }

      DocumentReader reader = arg0.asReader();
      Json.writeReaderToBuilder(reader, writer);
    }

  }

}
