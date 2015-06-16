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
package org.jackhammer.tests.json;

import java.io.InputStream;

import org.jackhammer.Record;
import org.jackhammer.RecordReader;
import org.jackhammer.RecordStream;
import org.jackhammer.RecordWriter;
import org.jackhammer.json.Json;
import org.jackhammer.tests.BaseTest;
import org.junit.Test;

public class TestJsonUtil extends BaseTest {

  @Test
  public void testJsonSerialization() throws Exception {
    try (InputStream in = getJsonStream("multirecord.json");
        RecordStream<Record> stream = Json.newRecordStream(in)) {
      for (RecordReader reader : stream.recordReaders()) {
        RecordWriter writer = Json.newRecordWriter();
        Json.writeReaderToStream(reader, writer);
        writer.build();
        System.out.println(writer);
      }
    }
  }

}
