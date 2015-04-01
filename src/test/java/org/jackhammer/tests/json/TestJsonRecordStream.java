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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.jackhammer.Record;
import org.jackhammer.RecordReader;
import org.jackhammer.json.JsonRecordStream;
import org.jackhammer.json.JsonUtils;
import org.jackhammer.tests.BaseTest;
import org.junit.Test;

public class TestJsonRecordStream extends BaseTest {

  @Test
  public void testFetchAndParseJsonRecordStream() throws IOException {
    try (InputStream in = getJsonStream("business.json");
        JsonRecordStream stream = new JsonRecordStream(in)) {

      int recordCount = 0;
      for (Record record : stream) {
        recordCount++;
        RecordReader r = record.asReader();
        System.out.println(JsonUtils.serializeToJsonString(r, false));
      }
      assertEquals(5, recordCount);
    }
  }

  @Test
  public void testFetchAndParsePartiallyJsonRecordStream() throws IOException {
    try (InputStream in = getJsonStream("business.json");
        JsonRecordStream stream = new JsonRecordStream(in)) {

      int recordCount = 0;
      for (Record record : stream) {
        recordCount++;
        RecordReader r = record.asReader();
        System.out.println("First event in the RecordReader: " + r.next());
      }
      assertEquals(5, recordCount);
    }
  }

}
