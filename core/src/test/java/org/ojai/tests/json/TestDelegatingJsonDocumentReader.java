/**
 * Copyright (c) 2016 MapR, Inc.
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
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.DocumentStream;
import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.json.Events;
import org.ojai.json.Events.BaseDelegate;
import org.ojai.json.Events.EventDescriptor;
import org.ojai.json.Json;
import org.ojai.json.impl.JsonValueBuilder;
import org.ojai.tests.BaseTest;

public class TestDelegatingJsonDocumentReader extends BaseTest {

  @Test
  public void testFieldInjection() throws IOException {
    final Events.Delegate EVENTDELEGATE = new BaseDelegate() {

      private int fieldCount = 0;

      @Override
      public boolean bor(DocumentReader reader, Queue<EventDescriptor> eventQueue) {
        eventQueue.offer(new EventDescriptor(EventType.INT)
            .setFieldName("_version")
            .setValue(JsonValueBuilder.initFrom(0)));
        return false;
      }

      @Override
      public boolean process(DocumentReader reader, EventType event,
          Queue<EventDescriptor> eventQueue) {
        if (event != EventType.END_ARRAY && event != EventType.END_MAP) {
          fieldCount++;
        }
        return false;
      }

      @Override
      public boolean eor(DocumentReader reader, Queue<EventDescriptor> eventQueue) {
        eventQueue.offer(new EventDescriptor(EventType.INT)
            .setFieldName("_fieldCount")
            .setValue(JsonValueBuilder.initFrom(fieldCount+1)));
        return false;
      }
    };

    try (InputStream in = getJsonStream("org/ojai/test/data/complex.json");
         DocumentStream stream = Json.newDocumentStream(in, EVENTDELEGATE)) {
      for (Document document : stream) {
        assertEquals(0, document.getInt("_version"));
        assertEquals(43, document.getInt("_fieldCount")); // 41 in JSON text, 2 injected
      }
    }

  }

  @Test
  public void testFieldTransformation() throws IOException {
    final Events.Delegate EVENTDELEGATE = new BaseDelegate() {
      @Override
      public boolean process(DocumentReader reader, EventType event,
          Queue<EventDescriptor> eventQueue) {
        if (reader.inMap()
            && event == EventType.DOUBLE
            && reader.getFieldName().equals("age")) {
          // replace a DOUBLE with a BYTE value 
          eventQueue.offer(new EventDescriptor(EventType.BYTE)
              .setFieldName("ageB")
              .setValue(JsonValueBuilder.initFrom((byte)reader.getDouble())));
          return true;
        }
        return false;
      }
    };

    try (InputStream in = getJsonStream("org/ojai/test/data/complex.json");
         DocumentStream stream = Json.newDocumentStream(in, EVENTDELEGATE)) {
      for (Document document : stream) {
        Value ageValue = document.getValue("age");
        assertNull(ageValue);
        ageValue = document.getValue("ageB");
        assertNotNull(ageValue);
        assertEquals(Type.BYTE, ageValue.getType());
        assertEquals(23, ageValue.getByte());
      }
    }

  }

}
