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
import java.util.Map;

import org.jackhammer.FieldPath;
import org.jackhammer.RecordReader;
import org.jackhammer.Value.Type;
import org.jackhammer.json.JsonRecordStream;
import org.jackhammer.json.JsonUtils;
import org.jackhammer.tests.BaseTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class TestJsonRecordStream extends BaseTest {
  private static Logger logger = LoggerFactory.getLogger(TestJsonRecordStream.class);

  @Test
  public void testFetchAndParseJsonRecordStream() throws IOException {
    try (InputStream in = getJsonStream("business.json");
        JsonRecordStream stream = new JsonRecordStream(in)) {

      int recordCount = 0;
      for (RecordReader reader : stream.recordReaders()) {
        recordCount++;
        logger.debug(JsonUtils.serializeToJsonString(reader, false));
      }
      assertEquals(5, recordCount);
    }
  }

  @Test
  public void testFetchAndParseTypeMappedJsonRecordStream() throws IOException {
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
        JsonRecordStream stream = new JsonRecordStream(in, fieldPathTypeMap)) {

      int recordCount = 0;
      for (RecordReader reader : stream.recordReaders()) {
        recordCount++;
        logger.debug(JsonUtils.serializeToJsonString(reader, false));
      }
      assertEquals(5, recordCount);
    }
  }

  @Test
  public void testFetchAndParsePartiallyJsonRecordStream() throws IOException {
    try (InputStream in = getJsonStream("business.json");
        JsonRecordStream stream = new JsonRecordStream(in)) {

      int recordCount = 0;
      for (RecordReader reader : stream.recordReaders()) {
        recordCount++;
        logger.debug("First event in the RecordReader: " + reader.next());
      }
      assertEquals(5, recordCount);
    }
  }

}
