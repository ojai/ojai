/**
 * Copyright (c) 2018 MapR, Inc.
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
package org.ojai.tests.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.FieldPath;
import org.ojai.json.Json;
import org.ojai.util.Documents;

public class TestGetWithDefaults {

  private static final String docStrWithTags =
      "{\"map\":{"
          + "\"null\":null,"
          + "\"boolean\":true,"
          + "\"string\":\"eureka\","
          + "\"byte\":{\"$numberLong\":127},"
          + "\"short\":{\"$numberLong\":32767},"
          + "\"int\":{\"$numberLong\":2147483647},"
          + "\"long\":{\"$numberLong\":9223372036854775807},"
          + "\"float\":3.4028235,"
          + "\"double\":1.7976931348623157E308,"
          + "\"decimal\":{\"$decimal\":\"123456789012345678901234567890123456789012345678901.23456789\"},"
          + "\"date\":{\"$dateDay\":\"2012-10-20\"},"
          + "\"time\":{\"$time\":\"07:42:46.123\"},"
          + "\"timestamp\":{\"$date\":\"2012-10-20T14:42:46.123Z\"},"
          + "\"interval\":{\"$interval\":172800000},"
          + "\"binary\":{\"$binary\":\"YWJjZA==\"},"
          + "\"array\":[42,\"open sesame\",3.14,{\"$dateDay\":\"2015-01-21\"}]"
          + "}"
          + "}";

  private static final String MEH_STR = "meh";
  private static final FieldPath MEH_FP = FieldPath.parseFrom(MEH_STR);

  @Test
  public void testGetWithDefaults() {
    Document emptyDoc = Json.newDocument();
    Document doc = Json.newDocument(docStrWithTags);

    ByteBuffer binaryValue = ByteBuffer.allocate(0);
    assertEquals(binaryValue, Documents.getBinary(emptyDoc, MEH_STR, binaryValue));
    assertEquals(binaryValue, Documents.getBinary(emptyDoc, MEH_FP, binaryValue));
    assertNotNull(Documents.getBinary(doc, "map.binary", null));

    boolean booleanValue = true;
    assertEquals(booleanValue, Documents.getBoolean(emptyDoc, MEH_STR, booleanValue));
    assertEquals(booleanValue, Documents.getBoolean(emptyDoc, MEH_FP, booleanValue));
    assertTrue(Documents.getBoolean(doc, "map.boolean", false));

    byte byteValue = Byte.MAX_VALUE;
    assertEquals(byteValue, Documents.getByte(emptyDoc, MEH_STR, byteValue));
    assertEquals(byteValue, Documents.getByte(emptyDoc, MEH_FP, byteValue));
    assertEquals(byteValue, Documents.getByte(doc, "map.byte", (byte)0));

    short shortValue = Short.MAX_VALUE;
    assertEquals(shortValue, Documents.getShort(emptyDoc, MEH_STR, shortValue));
    assertEquals(shortValue, Documents.getShort(emptyDoc, MEH_FP, shortValue));
    assertEquals(shortValue, Documents.getShort(doc, "map.short", (short)0));

    int intValue = Integer.MAX_VALUE;
    assertEquals(intValue, Documents.getInt(emptyDoc, MEH_STR, intValue));
    assertEquals(intValue, Documents.getInt(emptyDoc, MEH_FP, intValue));
    assertEquals(intValue, Documents.getInt(doc, "map.int", 0));

    long longValue = Long.MAX_VALUE;
    assertEquals(longValue, Documents.getLong(emptyDoc, MEH_STR, longValue));
    assertEquals(longValue, Documents.getLong(emptyDoc, MEH_FP, longValue));
    assertEquals(longValue, Documents.getLong(doc, "map.long", 0L));

    float floatValue = 3.4028235f;
    assertEquals(floatValue, Documents.getFloat(emptyDoc, MEH_STR, floatValue), 0);
    assertEquals(floatValue, Documents.getFloat(emptyDoc, MEH_FP, floatValue), 0);
    assertEquals(floatValue, Documents.getFloat(doc, "map.float", 0L), 0);

    double doubleValue = 1.7976931348623157E308;
    assertEquals(doubleValue, Documents.getDouble(emptyDoc, MEH_STR, doubleValue), 0);
    assertEquals(doubleValue, Documents.getDouble(emptyDoc, MEH_FP, doubleValue), 0);
    assertEquals(doubleValue, Documents.getDouble(doc, "map.double", 0L), 0);

    BigDecimal decimalValue = new BigDecimal("123456789012345678901234567890123456789012345678901.23456789");
    assertEquals(decimalValue, Documents.getDecimal(emptyDoc, MEH_STR, decimalValue));
    assertEquals(decimalValue, Documents.getDecimal(emptyDoc, MEH_FP, decimalValue));
    assertEquals(decimalValue, Documents.getDecimal(doc, "map.decimal", new BigDecimal(0)));

    String stringValue = "eureka";
    assertEquals(stringValue, Documents.getString(emptyDoc, MEH_STR, stringValue));
    assertEquals(stringValue, Documents.getString(emptyDoc, MEH_FP, stringValue));
    assertEquals(stringValue, Documents.getString(doc, "map.string", null));
  }

}
