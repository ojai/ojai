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
import java.util.ArrayList;
import java.util.List;

import org.jackhammer.RecordReader.EventType;
import org.jackhammer.json.JsonDOMRecordReader;
import org.jackhammer.json.JsonRecord;
import org.jackhammer.tests.BaseTest;
import org.junit.Test;

public class TestJsonDOMRecordReader extends BaseTest {

  @Test
  public void testDOMRecordReader() throws IOException {
    JsonRecord rec = new JsonRecord();
    rec.set("map.num1", (byte)127);
    rec.set("map.name.first", "John");
    rec.set("map.name.last", "Doe");
    rec.set("map.address.street.no", 350);
    rec.set("map.address.street.name", "Elan village");
    rec.set("map.address.zip", (long)95134);
    List<Object> values = new ArrayList<Object>();
    values.add("Field1");
    values.add(new Short((short)500));
    values.add(new Double(5555.5555));
    rec.set("map.list", values);
    boolean isArray = false;

    JsonDOMRecordReader r = new JsonDOMRecordReader(rec);
    EventType et;
    int numtokens = 0;
    String fieldName = null;
    while ((et = r.next()) != null) {
      if (et == EventType.FIELD_NAME) {
        fieldName = r.getFieldName();
      }
      if (isArray && et == EventType.SHORT) {
        assertEquals("list", fieldName);
        assertEquals((short)500, r.getShort());
      }
      if (et == EventType.BYTE) {
        assertEquals("num1", fieldName);
        assertEquals((byte)127, r.getByte());
      }
      if (et == EventType.INT) {
        assertEquals("no", fieldName);
        assertEquals(350, r.getInt());
      }

      if (et == EventType.LONG) {
        assertEquals("zip", fieldName);
        assertEquals(95134, r.getLong());
      }

      if (et == EventType.START_ARRAY) {
        isArray = true;
      }

      if (et == EventType.END_ARRAY) {
        isArray = false;
      }

      numtokens++;
    }

  }
}
