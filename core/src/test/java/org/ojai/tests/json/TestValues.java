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

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.Value;
import org.ojai.json.Json;
import org.ojai.json.impl.JsonValueBuilder;
import org.ojai.tests.BaseTest;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class TestValues extends BaseTest {

  @Test
  public void testEmptyByteBufferSerialization() throws Exception {
    ByteBuffer hbb = ByteBuffer.allocate(0);
    Value hbbValue = JsonValueBuilder.initFrom(hbb);
    assertEquals("{\"$binary\":\"\"}", hbbValue.toString());

    ByteBuffer dbb = ByteBuffer.allocateDirect(0);
    Value dbbValue = JsonValueBuilder.initFrom(dbb);
    assertEquals("{\"$binary\":\"\"}", dbbValue.toString());
  }

  @Test
  public void testAsJsonString() throws IOException {
    URL url = Resources.getResource("org/ojai/test/data/test0.json");
    String content = Resources.toString(url, Charsets.UTF_8);
    Document document = Json.newDocument(content);

    assertEquals("true", document.getValue("boolean").asJsonString());
    assertEquals("\"eureka\"", document.getValue("string").asJsonString());
    assertEquals("{\"$numberLong\":127}", document.getValue("byte").asJsonString());
    assertEquals("{\"$numberLong\":32767}", document.getValue("short").asJsonString());
    assertEquals("{\"$numberLong\":2147483647}", document.getValue("int").asJsonString());
    assertEquals("{\"$numberLong\":9223372036854775807}", document.getValue("long").asJsonString());
    assertEquals("3.4028235", document.getValue("float").asJsonString());
    assertEquals("1.7976931348623157E308", document.getValue("double").asJsonString());
    assertEquals("\"123456789012345678901234567890123456789012345678901.23456789\"", document.getValue("decimal").asJsonString());
    assertEquals("{\"$dateDay\":\"2012-10-20\"}", document.getValue("date").asJsonString());
    assertEquals("{\"$time\":\"07:42:46.123\"}", document.getValue("time").asJsonString());
    assertEquals("{\"$date\":\"2012-10-20T14:42:46.123Z\"}", document.getValue("timestamp").asJsonString());
    assertEquals("172800000", document.getValue("interval").asJsonString());
    assertEquals("{\"$binary\":\"YWJjZA==\"}", document.getValue("binary").asJsonString());
  }

}
