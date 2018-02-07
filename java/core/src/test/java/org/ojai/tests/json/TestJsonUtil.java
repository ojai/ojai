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
package org.ojai.tests.json;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.DocumentReader;
import org.ojai.DocumentStream;
import org.ojai.json.Json;
import org.ojai.tests.BaseTest;
import org.ojai.types.ODate;
import org.ojai.util.Documents;
import org.ojai.util.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJsonUtil extends BaseTest {
  private static Logger logger = LoggerFactory
      .getLogger(TestJsonUtil.class);

  @Test
  public void testJsonSerialization() throws Exception {
    try (InputStream in = getJsonStream("multidocument.json");
         DocumentStream stream = Json.newDocumentStream(in)) {
      for (DocumentReader reader : stream.documentReaders()) {
        DocumentBuilder builder = Json.newDocumentBuilder();
        Documents.writeReaderToBuilder(reader, builder);
        logger.info(builder.toString());
      }
    }
  }

  @Test
  public void testValuesAsJsonString() {
    Document r = Json.newDocument();
    r.set("a", (long)1234);
    r.set("b", ODate.parse("2011-09-15"));

    assertEquals("{\"$numberLong\":1234}", Values.asJsonString(r.getValue("a")));
  }

}
