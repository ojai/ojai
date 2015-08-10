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
import java.io.OutputStream;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.argonaut.Record;
import org.argonaut.RecordReader;
import org.argonaut.json.Json;
import org.argonaut.json.impl.JsonRecordWriter;

public class JSONFileOutputRecordWriter extends
RecordWriter<LongWritable, Record> {

  private JsonRecordWriter writer;
  private final OutputStream out;

  public JSONFileOutputRecordWriter(OutputStream fileOut) {
    out = fileOut;
  }

  @Override
  public void close(TaskAttemptContext arg0) throws IOException,
  InterruptedException {
    out.close();
  }

  @Override
  public void write(LongWritable arg0, Record record) throws IOException,
  InterruptedException {

    writer = (JsonRecordWriter) Json.newRecordWriter();
    RecordReader reader = record.asReader();
    Json.writeReaderToStream(reader, writer);
    byte[] bytes = writer.getOutputStream();
    out.write(bytes);
  }

}
