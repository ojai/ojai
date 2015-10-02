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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.DocumentReader;
import org.ojai.DocumentStream;
import org.ojai.json.Json;
import org.ojai.tests.BaseTest;
import org.ojai.util.Values;

import javax.print.Doc;

public class TestJsonUtil extends BaseTest {

  @Test
  public void testJsonSerialization() throws Exception {
    try (InputStream in = getJsonStream("multidocument.json");
        DocumentStream<Document> stream = Json.newDocumentStream(in)) {
      for (DocumentReader reader : stream.documentReaders()) {
        DocumentBuilder writer = Json.newDocumentBuilder();
        Json.writeReaderToBuilder(reader, writer);
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


  @Test
  public void testToMap() {
    Document d = Json.newDocument();
    d.set("string", "hello");
    d.set("integer", 123);
    d.set("map.key", "the_key");
    d.set("map.value", "the_value");
    d.set("list", Arrays.asList(0,1,2,3));

    Map map = d.toMap();

    assertEquals(d.getString("string") , map.get("string"));
    assertEquals(d.getInt("integer") , map.get("integer"));
    assertEquals(123, map.get("integer"));
    assertEquals("the_key", ((Map)map.get("map")).get("key") );
    assertEquals("the_value", ((Map)map.get("map")).get("value") );
    assertEquals(4, ((List)map.get("list")).size());
    assertEquals(3, ((List)map.get("list")).get(3));

  }

}
