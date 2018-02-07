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
package org.ojai.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.DocumentReader;
import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.annotation.API.Nullable;
import org.ojai.json.impl.JsonUtils;

import com.google.common.collect.Maps;

/**
 * This class contains utility methods for {@link Document} interface.
 */
@API.Public
public class Documents {

  /**
   * Compares two documents for equality.
   * @param d1 the first document to compare
   * @param d2 the second document to compare
   * @return {@code true} if both the documents are equal,
   *         {@code false} otherwise.
   */
  public static boolean equals(@Nullable Document d1, @Nullable Document d2) {
    if (d1 == d2) {
      return true; // both are null or same reference
    } else if (d1 == null || d2 == null
        || d1.size() != d2.size()) {
      return false;
    } else {
      Map<String, Value> keyValues = Maps.newTreeMap();
      Iterator<Entry<String, Value>> i = d2.iterator();
      while (i.hasNext()) {
        Entry<String, Value> e = i.next();
        keyValues.put(e.getKey(), e.getValue());
      }
      Iterator<Entry<String, Value>> j = d1.iterator();
      while (j.hasNext()) {
        Entry<String, Value> e = j.next();
        String k = e.getKey();
        Value v = keyValues.get(k);
        if (v == null || !e.getValue().equals(v)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * This method can be used to build a {@link Document} (via {@link DocumentBuilder}) from
   * a {@link DocumentReader} instance.
   *
   * @param reader instance of DocumentReader to read the fields from
   * @param builder instance of DocumentBuilder to write the field to
   */
  public static void writeReaderToBuilder(@NonNullable DocumentReader reader, @NonNullable DocumentBuilder builder) {
    JsonUtils.addToMap(reader, builder);
  }

}
