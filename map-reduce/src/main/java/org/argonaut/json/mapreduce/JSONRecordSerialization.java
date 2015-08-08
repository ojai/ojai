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

package org.argonaut.json.mapreduce;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.serializer.Deserializer;
import org.apache.hadoop.io.serializer.Serialization;
import org.apache.hadoop.io.serializer.Serializer;
import org.argonaut.Record;
import org.argonaut.RecordReader;
import org.argonaut.RecordStream;
import org.argonaut.json.Json;
import org.argonaut.json.JsonRecord;

public class JSONRecordSerialization extends Configured implements
Serialization<JsonRecord> {

  @Override
  public boolean accept(Class<?> arg0) {
    return JsonRecord.class.isAssignableFrom(arg0);
  }

  @Override
  public Deserializer<JsonRecord> getDeserializer(Class<JsonRecord> arg0) {
    return new JsonRecordDeserializer();
  }

  @Override
  public Serializer<JsonRecord> getSerializer(Class<JsonRecord> arg0) {
    return new JsonRecordSerializer();
  }

  private static class JsonRecordDeserializer implements
  Deserializer<JsonRecord> {

    private RecordStream<Record> stream;
    private Iterator<Record> iter;

    @Override
    public void close() throws IOException {
      try {
        stream.close();
      } catch (Exception e) {
        throw new IOException(e);
      }
    }

    @Override
    public JsonRecord deserialize(JsonRecord arg0) throws IOException {
      if (iter.hasNext()) {
        return (JsonRecord) iter.next();
      }
      return null;
    }

    @Override
    public void open(InputStream in) throws IOException {
      stream = Json.newRecordStream(in);
      iter = stream.iterator();
    }

  }

  private static class JsonRecordSerializer implements Serializer<JsonRecord> {

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
    public void serialize(JsonRecord arg0) throws IOException {
      writer = new JSONFileRecordWriter(out);
      if (writer == null) {
        throw new IOException(
            "Output stream is not available for serialization.");
      }

      RecordReader reader = arg0.asReader();
      Json.writeReaderToStream(reader, writer);
    }

  }

}
