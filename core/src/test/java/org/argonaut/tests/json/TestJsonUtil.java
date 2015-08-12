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
package org.argonaut.tests.json;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.argonaut.Document;
import org.argonaut.DocumentBuilder;
import org.argonaut.DocumentReader;
import org.argonaut.DocumentStream;
import org.argonaut.json.Json;
import org.argonaut.tests.BaseTest;
import org.argonaut.util.Values;
import org.junit.Test;

public class TestJsonUtil extends BaseTest {

  @Test
  public void testJsonSerialization() throws Exception {
    try (InputStream in = getJsonStream("multidocument.json");
        DocumentStream<Document> stream = Json.newDocumentStream(in)) {
      for (DocumentReader reader : stream.documentReaders()) {
        DocumentBuilder writer = Json.newDocumentBuilder();
        Json.writeReaderToStream(reader, writer);
        System.out.println(writer);
      }
    }
  }

  @Test
  public void testValuesAsJsonString() {
    Document r = Json.newDocument();
    r.set("a", (long)1234);
    r.set("b", Values.parseDate("2011-09-15"));

    assertEquals("{\"$numberLong\":1234}", Values.asJsonString(r.getValue("a")));
  }

}
