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

import org.jackhammer.RecordReader;
import org.jackhammer.RecordReader.EventType;
import org.jackhammer.json.JsonRecordStream;
import org.jackhammer.tests.BaseTest;
import org.junit.Test;

public class TestJsonRecordReader extends BaseTest {

  @Test
  public void testAll() throws IOException {

    try (InputStream testJson = getJsonStream("test.json");
         JsonRecordStream stream = new JsonRecordStream(testJson);) {
      RecordReader r = stream.recordReaders().iterator().next();
      EventType et = r.next();
      while (et != null) {
        if (et == EventType.BYTE) {
          byte b = r.getByte();
          assertEquals((byte) 127, b);
        } else if (et == EventType.SHORT) {
          short s = r.getShort();
          assertEquals((short) 32767, s);
        } else if (et == EventType.INT) {
          int myint = r.getInt();
          assertEquals(2147483647, myint);
        } else if (et == EventType.BOOLEAN) {
          boolean b = r.getBoolean();
          assertEquals(true, b);
        } else if (et == EventType.STRING) {
          String s = r.getString();
          assertEquals("eureka", s);
        }
        else if (et == EventType.LONG) {
          long l = r.getLong();
          assertEquals(123456789, l);
        } else if (et == EventType.INTERVAL) {
          int days = r.getIntervalDays();
          assertEquals(2, days);
        }
        else if (et == EventType.FLOAT) {
          r.getFloat();
        } else if (et == EventType.START_ARRAY) {
          et = r.next();
          while (et != EventType.END_ARRAY) {
            if (et == EventType.LONG) {
              assertEquals(42, r.getLong());
            } else if (et == EventType.STRING) {
              assertEquals("open sesame", r.getString());
            } else if (et == EventType.DOUBLE) {
              assertEquals(3.14, r.getDouble(), 0.0);
            } else {

            }
            et = r.next();
          }
        } else {

        }
        et = r.next();
      }
    }
  }

}
