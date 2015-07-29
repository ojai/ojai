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
package org.jackhammer.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.jackhammer.FieldPath;
import org.jackhammer.Record;
import org.jackhammer.RecordReader;
import org.jackhammer.RecordReader.EventType;
import org.jackhammer.RecordStream;
import org.jackhammer.RecordWriter;
import org.jackhammer.Value.Type;
import org.jackhammer.annotation.API;

/**
 * This class serves as factory for JSON implementation
 * of all Argonaut interfaces.
 */
@API.Public
public final class Json {

  public static Record newRecord() {
    return new JsonRecord();
  }

  public static RecordWriter newRecordWriter() {
    return new JsonRecordWriter();
  }

  public static RecordStream<Record> newRecordStream(InputStream in) {
    return new JsonRecordStream(in, null, null);
  }

  public static RecordStream<Record> newRecordStream(
      InputStream in, Map<FieldPath, Type> fieldPathTypeMap) {
    return new JsonRecordStream(in, fieldPathTypeMap, null);
  }

  public static RecordStream<Record> newRecordStream(
      InputStream in, Events.Delegate eventDelegate) {
    return new JsonRecordStream(in, null, eventDelegate);
  }

  public static RecordStream<Record> newRecordStream(FileSystem fs, String path)
      throws IllegalArgumentException, IOException {
    return JsonRecordStream.newRecordStream(fs, path, null, null);
  }

  public static RecordStream<Record> newRecordStream(
      FileSystem fs, String path, Map<FieldPath, Type> fieldPathTypeMap)
          throws IllegalArgumentException, IOException {
    return JsonRecordStream.newRecordStream(fs, path, fieldPathTypeMap, null);
  }

  public static RecordStream<Record> newRecordStream(
      FileSystem fs, String path, Events.Delegate eventDelegate)
          throws IllegalArgumentException, IOException {
    return JsonRecordStream.newRecordStream(fs, path, null, eventDelegate);
  }

  public static String toJsonString(RecordReader r) {
    return Json.toJsonString(r, true);
  }

  public static String toJsonString(RecordReader r, boolean pretty) {
    EventType e = r.next();
    assert e == EventType.START_MAP;

    JsonRecordWriter w = new JsonRecordWriter();
    w.addNewMap();
    w.enablePrettyPrinting(pretty);
    JsonUtils.addToMap(r, w);
    return w.asUTF8String();
  }

  public static void writeReaderToStream(RecordReader r, RecordWriter w) {
    JsonUtils.addToMap(r, w);
  }

}
