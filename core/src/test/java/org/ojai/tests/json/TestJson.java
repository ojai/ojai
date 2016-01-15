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
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.regex.Pattern;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.json.Json;
import org.ojai.json.JsonOptions;
import org.ojai.tests.BaseTest;

public class TestJson extends BaseTest {

  @Test
  public void testJson_NewDocument() {
    Document doc = Json.newDocument("{}");
    assertTrue(doc.size() == 0);

    doc = Json.newDocument(
        "{\n" +
        "  \"business_id\": \"vcNAWiLM4dR7D2nwwJ7nCA\",\n" +
        "  \"full_address\": \"4840 E Indian School Rd\\nSte 101\\nPhoenix, AZ 85018\",\n" +
        "  \"hours\": {\n" +
        "    \"Tuesday\": {\n" +
        "      \"close\": \"17:00\",\n" +
        "      \"open\": \"08:00\"\n" +
        "    },\n" +
        "    \"Friday\": {\n" +
        "      \"close\": \"17:00\",\n" +
        "      \"open\": \"08:00\"\n" +
        "    },\n" +
        "    \"Monday\": {\n" +
        "      \"close\": \"17:00\",\n" +
        "      \"open\": \"08:00\"\n" +
        "    },\n" +
        "    \"Wednesday\": {\n" +
        "      \"close\": \"17:00\",\n" +
        "      \"open\": \"08:00\"\n" +
        "    },\n" +
        "    \"Thursday\": {\n" +
        "      \"close\": \"17:00\",\n" +
        "      \"open\": \"08:00\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"open\": true,\n" +
        "  \"categories\": [\"Doctors\",\n" +
        "  \"Health & Medical\"],\n" +
        "  \"city\": \"Phoenix\",\n" +
        "  \"review_count\": 7,\n" +
        "  \"name\": \"Eric Goldberg, MD\",\n" +
        "  \"neighborhoods\": [],\n" +
        "  \"longitude\": -111,\n" +
        "  \"state\": \"AZ\",\n" +
        "  \"stars\": 3.5,\n" +
        "  \"latitude\": 33.499313000000001,\n" +
        "  \"attributes\": {\n" +
        "    \"By Appointment Only\": true\n" +
        "  },\n" +
        "  \"type\": \"business\"\n" +
        "}");
    assertEquals(15, doc.size());
    assertEquals("08:00", doc.getString("hours.Wednesday.open"));
    assertEquals("Health & Medical", doc.getString("categories[1]"));

    String jsonString = Json.toJsonString(doc.asReader());
    Pattern p1 = Pattern.compile(".*\"review_count\".*:.*7,.*", Pattern.DOTALL);
    Pattern p2 = Pattern.compile(".*\"longitude\".*:.*-111,.*", Pattern.DOTALL);
    assertTrue(p1.matcher(jsonString).matches());
    assertTrue(p2.matcher(jsonString).matches());
  }

  @Test
  public void testJson_AsJsonStringComplex() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/complex.json");
         DocumentStream<Document> stream = Json.newDocumentStream(in)) {
      Document doc = stream.iterator().next();
      String s = Json.toJsonString(doc);
      assertEquals("{\"first\":\"Sam\",\"last\":\"LNU\",\"age\":23,\"sex\":\"F\",\"salary\":315000,"
          + "\"active\":true,\"interests\":[\"Reading\",\"Hiking\",{\"passive\":[\"sleeping\","
          + "\"dreaming\"]}],\"favorites\":{\"color\":\"Red\",\"sport\":\"Cricket\","
          + "\"food\":\"Nasi Goreng\"},\"skills\":[{\"category\":\"Economics\","
          + "\"tests\":[{\"name\":\"ECO101\",\"score\":90},{\"name\":\"ECO212\",\"score\":96},"
          + "[\"ECO152\",87,\"ECO162\",91]]},{\"category\":\"Computer Science\","
          + "\"tests\":[{\"name\":\"CS404\",\"score\":99},{\"name\":\"CS301\",\"score\":93}]}]}", s);

      s = Json.toJsonString(doc, new JsonOptions().pretty());
      assertEquals("{\n" +
          "  \"first\" : \"Sam\",\n" +
          "  \"last\" : \"LNU\",\n" +
          "  \"age\" : 23,\n" +
          "  \"sex\" : \"F\",\n" +
          "  \"salary\" : 315000,\n" +
          "  \"active\" : true,\n" +
          "  \"interests\" : [ \"Reading\", \"Hiking\", {\n" +
          "    \"passive\" : [ \"sleeping\", \"dreaming\" ]\n" +
          "  } ],\n" +
          "  \"favorites\" : {\n" +
          "    \"color\" : \"Red\",\n" +
          "    \"sport\" : \"Cricket\",\n" +
          "    \"food\" : \"Nasi Goreng\"\n" +
          "  },\n" +
          "  \"skills\" : [ {\n" +
          "    \"category\" : \"Economics\",\n" +
          "    \"tests\" : [ {\n" +
          "      \"name\" : \"ECO101\",\n" +
          "      \"score\" : 90\n" +
          "    }, {\n" +
          "      \"name\" : \"ECO212\",\n" +
          "      \"score\" : 96\n" +
          "    }, [ \"ECO152\", 87, \"ECO162\", 91 ] ]\n" +
          "  }, {\n" +
          "    \"category\" : \"Computer Science\",\n" +
          "    \"tests\" : [ {\n" +
          "      \"name\" : \"CS404\",\n" +
          "      \"score\" : 99\n" +
          "    }, {\n" +
          "      \"name\" : \"CS301\",\n" +
          "      \"score\" : 93\n" +
          "    } ]\n" +
          "  } ]\n" +
          "}", s);
    }
  }

  @Test
  public void testJson_AsJsonStringIncludingTags() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/test.json");
         DocumentStream<Document> stream = Json.newDocumentStream(in)) {
      Document doc = stream.iterator().next();

      String s = Json.toJsonString(doc, JsonOptions.WITH_TAGS);
      assertEquals("{\"map\":{\"null\":null,\"boolean\":true,\"string\":\"eureka\","
          + "\"byte\":{\"$numberLong\":127},\"short\":{\"$numberLong\":32767},"
          + "\"int\":{\"$numberLong\":2147483647},\"long\":{\"$numberLong\":9223372036854775807},"
          + "\"float\":3.4028235,\"double\":1.7976931348623157E308,"
          + "\"decimal\":{\"$decimal\":\"123456789012345678901234567890123456789012345678901.23456789\"},"
          + "\"date\":{\"$dateDay\":\"2012-10-20\"},\"time\":{\"$time\":\"07:42:46.123\"},"
          + "\"timestamp\":{\"$date\":\"2012-10-20T14:42:46.123Z\"},\"interval\":{\"$interval\":172800000},"
          + "\"binary\":{\"$binary\":\"YWJjZA==\"},\"array\":[42,\"open sesame\",3.14,{\"$dateDay\":\"2015-01-21\"}]}}", s);

      s = Json.toJsonString(doc, new JsonOptions().withoutTags());
      assertEquals("{\"map\":{\"null\":null,\"boolean\":true,\"string\":\"eureka\",\"byte\":127,\"short\":32767,"
          + "\"int\":2147483647,\"long\":9223372036854775807,\"float\":3.4028235,\"double\":1.7976931348623157E308,"
          + "\"decimal\":123456789012345678901234567890123456789012345678901.23456789,\"date\":\"2012-10-20\","
          + "\"time\":\"07:42:46.123\",\"timestamp\":\"2012-10-20T14:42:46.123Z\",\"interval\":172800000,"
          + "\"binary\":\"YWJjZA==\",\"array\":[42,\"open sesame\",3.14,\"2015-01-21\"]}}", s);

      s = Json.toJsonString(doc, new JsonOptions().pretty().withoutTags());
      assertEquals("{\n" +
          "  \"map\" : {\n" +
          "    \"null\" : null,\n" +
          "    \"boolean\" : true,\n" +
          "    \"string\" : \"eureka\",\n" +
          "    \"byte\" : 127,\n" +
          "    \"short\" : 32767,\n" +
          "    \"int\" : 2147483647,\n" +
          "    \"long\" : 9223372036854775807,\n" +
          "    \"float\" : 3.4028235,\n" +
          "    \"double\" : 1.7976931348623157E308,\n" +
          "    \"decimal\" : 123456789012345678901234567890123456789012345678901.23456789,\n" +
          "    \"date\" : \"2012-10-20\",\n" +
          "    \"time\" : \"07:42:46.123\",\n" +
          "    \"timestamp\" : \"2012-10-20T14:42:46.123Z\",\n" +
          "    \"interval\" : 172800000,\n" +
          "    \"binary\" : \"YWJjZA==\",\n" +
          "    \"array\" : [ 42, \"open sesame\", 3.14, \"2015-01-21\" ]\n" +
          "  }\n" +
          "}", s);

      s = Json.toJsonString(doc, new JsonOptions().pretty().withTags());
      assertEquals("{\n" +
          "  \"map\" : {\n" +
          "    \"null\" : null,\n" +
          "    \"boolean\" : true,\n" +
          "    \"string\" : \"eureka\",\n" +
          "    \"byte\" : {\n" +
          "      \"$numberLong\" : 127\n" +
          "    },\n" +
          "    \"short\" : {\n" +
          "      \"$numberLong\" : 32767\n" +
          "    },\n" +
          "    \"int\" : {\n" +
          "      \"$numberLong\" : 2147483647\n" +
          "    },\n" +
          "    \"long\" : {\n" +
          "      \"$numberLong\" : 9223372036854775807\n" +
          "    },\n" +
          "    \"float\" : 3.4028235,\n" +
          "    \"double\" : 1.7976931348623157E308,\n" +
          "    \"decimal\" : {\n" +
          "      \"$decimal\" : \"123456789012345678901234567890123456789012345678901.23456789\"\n" +
          "    },\n" +
          "    \"date\" : {\n" +
          "      \"$dateDay\" : \"2012-10-20\"\n" +
          "    },\n" +
          "    \"time\" : {\n" +
          "      \"$time\" : \"07:42:46.123\"\n" +
          "    },\n" +
          "    \"timestamp\" : {\n" +
          "      \"$date\" : \"2012-10-20T14:42:46.123Z\"\n" +
          "    },\n" +
          "    \"interval\" : {\n" +
          "      \"$interval\" : 172800000\n" +
          "    },\n" +
          "    \"binary\" : {\n" +
          "      \"$binary\" : \"YWJjZA==\"\n" +
          "    },\n" +
          "    \"array\" : [ 42, \"open sesame\", 3.14, {\n" +
          "      \"$dateDay\" : \"2015-01-21\"\n" +
          "    } ]\n" +
          "  }\n" +
          "}", s);
    }
  }

  @Test
  public void testJson_AsJsonStringNonDocument() throws Exception {
    try (InputStream in = getJsonStream("org/ojai/test/data/test.json");
         DocumentStream<Document> stream = Json.newDocumentStream(in)) {
      Document doc = stream.iterator().next();

      assertEquals("null",
          Json.toJsonString(doc.getValue("map.null").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("true",
          Json.toJsonString(doc.getValue("map.boolean").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("\"eureka\"",
          Json.toJsonString(doc.getValue("map.string").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("{\"$numberLong\":127}",
          Json.toJsonString(doc.getValue("map.byte").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("{\"$numberLong\":32767}",
          Json.toJsonString(doc.getValue("map.short").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("{\"$numberLong\":2147483647}",
          Json.toJsonString(doc.getValue("map.int").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("{\"$numberLong\":9223372036854775807}",
          Json.toJsonString(doc.getValue("map.long").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("3.4028235",
          Json.toJsonString(doc.getValue("map.float").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("1.7976931348623157E308",
          Json.toJsonString(doc.getValue("map.double").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("{\"$decimal\":\"123456789012345678901234567890123456789012345678901.23456789\"}",
          Json.toJsonString(doc.getValue("map.decimal").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("{\"$dateDay\":\"2012-10-20\"}",
          Json.toJsonString(doc.getValue("map.date").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("{\"$time\":\"07:42:46.123\"}",
          Json.toJsonString(doc.getValue("map.time").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("{\"$date\":\"2012-10-20T14:42:46.123Z\"}",
          Json.toJsonString(doc.getValue("map.timestamp").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("{\"$binary\":\"YWJjZA==\"}",
          Json.toJsonString(doc.getValue("map.binary").asReader(), JsonOptions.WITH_TAGS));

      assertEquals("[42,\"open sesame\",3.14,{\"$dateDay\":\"2015-01-21\"}]",
          Json.toJsonString(doc.getValue("map.array").asReader(), JsonOptions.WITH_TAGS));
    }
  }

}
