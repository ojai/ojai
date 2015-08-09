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
import static org.junit.Assert.assertTrue;

import org.argonaut.Record;
import org.argonaut.json.Json;
import org.argonaut.tests.BaseTest;
import org.junit.Test;

public class TestJson extends BaseTest {

  @Test
  public void testJson_NewRecord() {
    Record rec = Json.newRecord("{}");
    assertTrue(rec.size() == 0);

    rec = Json.newRecord(
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
        "  \"longitude\": -111.98375799999999,\n" +
        "  \"state\": \"AZ\",\n" +
        "  \"stars\": 3.5,\n" +
        "  \"latitude\": 33.499313000000001,\n" +
        "  \"attributes\": {\n" +
        "    \"By Appointment Only\": true\n" +
        "  },\n" +
        "  \"type\": \"business\"\n" +
        "}");
    assertEquals(15, rec.size());
    assertEquals("08:00", rec.getString("hours.Wednesday.open"));
    assertEquals("Health & Medical", rec.getString("categories[1]"));
  }

}
