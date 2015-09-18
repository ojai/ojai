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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentReader;
import org.ojai.DocumentStream;
import org.ojai.DocumentReader.EventType;
import org.ojai.json.Json;
import org.ojai.tests.BaseTest;
import org.ojai.util.Values;

public class TestJsonDocumentReader extends BaseTest {

  @Test
  public void testAll() throws Exception {


    try (InputStream testJson = getJsonStream("test.json");
        DocumentStream<Document> stream = Json.newDocumentStream(testJson);) {
      EventType et = null;
      String fieldName = null;
      DocumentReader r = stream.documentReaders().iterator().next();
      while ((et = r.next()) != null) {
        if (et == EventType.FIELD_NAME) {
          fieldName = r.getFieldName();
        }
        else if (et == EventType.LONG) {
          if (fieldName.equals("byte")) {
            assertEquals((byte)127, r.getByte());
          }
          else if (fieldName.equals("short")) {
            assertEquals((short)32767, r.getShort());
          }
          else if (fieldName.equals("int")) {
            assertEquals(2147483647, r.getInt());
          }
          else if (fieldName.equals("long")) {
            long l = r.getLong();
            assertEquals(Long.valueOf("9223372036854775807").longValue(), l);
          }

        }
        else if (et == EventType.BOOLEAN) {
          boolean b = r.getBoolean();
          assertEquals(true, b);
        } else if (et == EventType.STRING) {
          String s = r.getString();
          assertEquals("eureka", s);
        }
        else if (et == EventType.INTERVAL) {
          int days = r.getIntervalDays();
          assertEquals(2, days);
        }
        else if (et == EventType.START_ARRAY) {
          et = r.next();
          assertNotNull(et);
          assertTrue(et == EventType.DOUBLE);
          assertEquals(42.0, r.getDouble(), 0.0);

          et = r.next();
          assertNotNull(et);
          assertTrue(et == EventType.STRING);
          assertEquals("open sesame", r.getString());

          et = r.next();
          assertNotNull(et);
          assertTrue(et == EventType.DOUBLE);
          assertEquals(3.14, r.getDouble(), 0.0001);

          et = r.next();
          assertNotNull(et);
          assertTrue(et == EventType.DATE);
          assertEquals(Values.parseDate("2015-01-21"), r.getDate());
        }
      }
    }
  }

}
