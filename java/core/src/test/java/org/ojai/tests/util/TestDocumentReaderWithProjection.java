/**
 * Copyright (c) 2017 MapR, Inc.
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

import static org.junit.Assert.assertNull;

import java.io.InputStream;

import org.junit.Test;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.DocumentStream;
import org.ojai.json.Json;
import org.ojai.tests.BaseTest;
import org.ojai.util.DocumentReaderWithProjection;
import org.ojai.util.FieldProjector;

public class TestDocumentReaderWithProjection extends BaseTest {

  /*
   * Test Document
   *
   * {
   *   "a": 1,
   *   "b": 2,
   *   "c": {
   *     "d": 3,
   *     "e": 4
   *   },
   *   "f": {
   *     "g": {
   *       "h": ["h1", [{"h2": [5, {"h3": {"h4": 6}}]}]],
   *       "i": {"i1": {"i2": {"i3": {"i4": {"i5": [], "i6": {}}}}}}
   *     },
   *     "j": 7
   *   },
   *   "k": {
   *     "l": {
   *       "l1": {
   *         "l2": 8
   *       },
   *       "l3": 9
   *     }
   *   },
   *   "m": {
   *     "n": {
   *       "n1": {
   *         "n2": 10
   *       }
   *     }
   *   },
   *   "o": {},
   *   "p": [],
   *   "q": [[]],
   *   "r": [[], {}],
   *   "s": [{}, []],
   *   "t": {"u": {}},
   *   "v": {"w": []},
   *   "x": {"y": [{}, null]},
   *   "z": null
   * }
   *
   */


  @Test
  public void testEmptyMissingField() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
        DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("a.a1", "b.b1");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }

  @Test
  public void testProjectScalarFieldAsContainer() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
        DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("c.c1", "f.g.h[2]");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);

      assertMapEvent(r, EventType.START_MAP, "c");
      assertMapEvent(r, EventType.END_MAP, "c");

      assertMapEvent(r, EventType.START_MAP, "f");
      assertMapEvent(r, EventType.START_MAP, "g");
      assertMapEvent(r, EventType.START_ARRAY, "h");
      assertMapEvent(r, EventType.END_ARRAY, "h");
      assertMapEvent(r, EventType.END_MAP, "g");
      assertMapEvent(r, EventType.END_MAP, "f");

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }

  @Test
  public void testEmptyContainers() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
        DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("o", "p", "q[0]", "r[0]", "r[1]", "s[0]", "s[1]",
                                                    "t.u", "v.w", "x.y[0]", "x.y[1]", "z");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);

      assertMapEvent(r, EventType.START_MAP, "o");
      assertMapEvent(r, EventType.END_MAP, "o");

      assertMapEvent(r, EventType.START_ARRAY, "p");
      assertMapEvent(r, EventType.END_ARRAY, "p");

      assertMapEvent(r, EventType.START_ARRAY, "q");
      assertArrayEvent(r, EventType.START_ARRAY, 0);
      assertArrayEvent(r, EventType.END_ARRAY, 0);
      assertMapEvent(r, EventType.END_ARRAY, "q");

      assertMapEvent(r, EventType.START_ARRAY, "r");
      assertArrayEvent(r, EventType.START_ARRAY, 0);
      assertArrayEvent(r, EventType.END_ARRAY, 0);
      assertArrayEvent(r, EventType.START_MAP, 1);
      assertArrayEvent(r, EventType.END_MAP, 1);
      assertMapEvent(r, EventType.END_ARRAY, "r");

      assertMapEvent(r, EventType.START_ARRAY, "s");
      assertArrayEvent(r, EventType.START_MAP, 0);
      assertArrayEvent(r, EventType.END_MAP, 0);
      assertArrayEvent(r, EventType.START_ARRAY, 1);
      assertArrayEvent(r, EventType.END_ARRAY, 1);
      assertMapEvent(r, EventType.END_ARRAY, "s");

      assertMapEvent(r, EventType.START_MAP, "t");
      assertMapEvent(r, EventType.START_MAP, "u");
      assertMapEvent(r, EventType.END_MAP, "u");
      assertMapEvent(r, EventType.END_MAP, "t");

      assertMapEvent(r, EventType.START_MAP, "v");
      assertMapEvent(r, EventType.START_ARRAY, "w");
      assertMapEvent(r, EventType.END_ARRAY, "w");
      assertMapEvent(r, EventType.END_MAP, "v");

      assertMapEvent(r, EventType.START_MAP, "x");
      assertMapEvent(r, EventType.START_ARRAY, "y");
      assertArrayEvent(r, EventType.START_MAP, 0);
      assertArrayEvent(r, EventType.END_MAP, 0);
      assertArrayEvent(r, EventType.NULL, 1);
      assertMapEvent(r, EventType.END_ARRAY, "y");
      assertMapEvent(r, EventType.END_MAP, "x");

      assertMapEvent(r, EventType.NULL, "z");

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }


  @Test
  public void testScalars() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
         DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("a", "b", "c.d", "f.g.h[1][0].h2[0]", "k.l.l3");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);

      assertMapEvent(r, EventType.DOUBLE, "a");

      assertMapEvent(r, EventType.DOUBLE, "b");

      assertMapEvent(r, EventType.START_MAP, "c");
      assertMapEvent(r, EventType.DOUBLE, "d");
      assertMapEvent(r, EventType.END_MAP, "c");

      assertMapEvent(r, EventType.START_MAP, "f");
      assertMapEvent(r, EventType.START_MAP, "g");
      assertMapEvent(r, EventType.START_ARRAY, "h");
      assertArrayEvent(r, EventType.START_ARRAY, 1);
      assertArrayEvent(r, EventType.START_MAP, 0);
      assertMapEvent(r, EventType.START_ARRAY, "h2");
      assertArrayEvent(r, EventType.DOUBLE, 0);
      assertMapEvent(r, EventType.END_ARRAY, "h2");
      assertArrayEvent(r, EventType.END_MAP, 0);
      assertArrayEvent(r, EventType.END_ARRAY, 1);
      assertMapEvent(r, EventType.END_ARRAY, "h");
      assertMapEvent(r, EventType.END_MAP, "g");
      assertMapEvent(r, EventType.END_MAP, "f");

      assertMapEvent(r, EventType.START_MAP, "k");
      assertMapEvent(r, EventType.START_MAP, "l");
      assertMapEvent(r, EventType.DOUBLE, "l3");
      assertMapEvent(r, EventType.END_MAP, "l");
      assertMapEvent(r, EventType.END_MAP, "k");

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }

  @Test
  public void testProjectEmptyMap() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
        DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("f.g.i.i1.i2.i3.i4.i6");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);

      assertMapEvent(r, EventType.START_MAP, "f");
      assertMapEvent(r, EventType.START_MAP, "g");
      assertMapEvent(r, EventType.START_MAP, "i");
      assertMapEvent(r, EventType.START_MAP, "i1");
      assertMapEvent(r, EventType.START_MAP, "i2");
      assertMapEvent(r, EventType.START_MAP, "i3");
      assertMapEvent(r, EventType.START_MAP, "i4");
      assertMapEvent(r, EventType.START_MAP, "i6");
      // {}
      assertMapEvent(r, EventType.END_MAP, "i6");
      assertMapEvent(r, EventType.END_MAP, "i4");
      assertMapEvent(r, EventType.END_MAP, "i3");
      assertMapEvent(r, EventType.END_MAP, "i2");
      assertMapEvent(r, EventType.END_MAP, "i1");
      assertMapEvent(r, EventType.END_MAP, "i");
      assertMapEvent(r, EventType.END_MAP, "g");
      assertMapEvent(r, EventType.END_MAP, "f");

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }

  @Test
  public void testProjectEmptyArray() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
        DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("f.g.i.i1.i2.i3.i4.i5");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);
      assertMapEvent(r, EventType.START_MAP, "f");
      assertMapEvent(r, EventType.START_MAP, "g");
      assertMapEvent(r, EventType.START_MAP, "i");
      assertMapEvent(r, EventType.START_MAP, "i1");
      assertMapEvent(r, EventType.START_MAP, "i2");
      assertMapEvent(r, EventType.START_MAP, "i3");
      assertMapEvent(r, EventType.START_MAP, "i4");
      assertMapEvent(r, EventType.START_ARRAY, "i5");
      // []
      assertMapEvent(r, EventType.END_ARRAY, "i5");
      assertMapEvent(r, EventType.END_MAP, "i4");
      assertMapEvent(r, EventType.END_MAP, "i3");
      assertMapEvent(r, EventType.END_MAP, "i2");
      assertMapEvent(r, EventType.END_MAP, "i1");
      assertMapEvent(r, EventType.END_MAP, "i");
      assertMapEvent(r, EventType.END_MAP, "g");
      assertMapEvent(r, EventType.END_MAP, "f");

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }

  @Test
  public void testProjectEmptyMapAndArray() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
        DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("f.g.i.i1.i2.i3.i4.i5", "f.g.i.i1.i2.i3.i4.i6");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);

      assertMapEvent(r, EventType.START_MAP, "f");
      assertMapEvent(r, EventType.START_MAP, "g");
      assertMapEvent(r, EventType.START_MAP, "i");
      assertMapEvent(r, EventType.START_MAP, "i1");
      assertMapEvent(r, EventType.START_MAP, "i2");
      assertMapEvent(r, EventType.START_MAP, "i3");
      assertMapEvent(r, EventType.START_MAP, "i4");
      assertMapEvent(r, EventType.START_ARRAY, "i5");
      // []
      assertMapEvent(r, EventType.END_ARRAY, "i5");
      assertMapEvent(r, EventType.START_MAP, "i6");
      // {}
      assertMapEvent(r, EventType.END_MAP, "i6");
      assertMapEvent(r, EventType.END_MAP, "i4");
      assertMapEvent(r, EventType.END_MAP, "i3");
      assertMapEvent(r, EventType.END_MAP, "i2");
      assertMapEvent(r, EventType.END_MAP, "i1");
      assertMapEvent(r, EventType.END_MAP, "i");
      assertMapEvent(r, EventType.END_MAP, "g");
      assertMapEvent(r, EventType.END_MAP, "f");

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }

  @Test
  public void testSimpleMaps() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
         DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("a", "f.j", "k.l.l1", "m");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);

      assertMapEvent(r, EventType.DOUBLE, "a");

      assertMapEvent(r, EventType.START_MAP, "f");
      assertMapEvent(r, EventType.DOUBLE, "j");
      assertMapEvent(r, EventType.END_MAP, "f");

      assertMapEvent(r, EventType.START_MAP, "k");
      assertMapEvent(r, EventType.START_MAP, "l");
      assertMapEvent(r, EventType.START_MAP, "l1");
      assertMapEvent(r, EventType.DOUBLE, "l2");
      assertMapEvent(r, EventType.END_MAP, "l1");
      assertMapEvent(r, EventType.END_MAP, "l");
      assertMapEvent(r, EventType.END_MAP, "k");

      assertMapEvent(r, EventType.START_MAP, "m");
      assertMapEvent(r, EventType.START_MAP, "n");
      assertMapEvent(r, EventType.START_MAP, "n1");
      assertMapEvent(r, EventType.DOUBLE, "n2");
      assertMapEvent(r, EventType.END_MAP, "n1");
      assertMapEvent(r, EventType.END_MAP, "n");
      assertMapEvent(r, EventType.END_MAP, "m");

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }

  @Test
  public void testProjectParentAndChild() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
        DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("k", "k.l.l1");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);

      assertMapEvent(r, EventType.START_MAP, "k");
      assertMapEvent(r, EventType.START_MAP, "l");
      assertMapEvent(r, EventType.START_MAP, "l1");
      assertMapEvent(r, EventType.DOUBLE, "l2");
      assertMapEvent(r, EventType.END_MAP, "l1");
      assertMapEvent(r, EventType.DOUBLE, "l3");
      assertMapEvent(r, EventType.END_MAP, "l");
      assertMapEvent(r, EventType.END_MAP, "k");

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }

  @Test
  public void testProjectChildParentAndChild() throws Exception {
    try (InputStream testJson = getJsonStream("org/ojai/test/data/test1.json");
        DocumentStream stream = Json.newDocumentStream(testJson);) {
      DocumentReader reader = stream.iterator().next().asReader();
      FieldProjector projector = new FieldProjector("k.l.l1", "k", "k.l.l1");

      DocumentReaderWithProjection r = new DocumentReaderWithProjection(reader, projector);

      assertMapEvent(r, EventType.START_MAP, null);

      assertMapEvent(r, EventType.START_MAP, "k");
      assertMapEvent(r, EventType.START_MAP, "l");
      assertMapEvent(r, EventType.START_MAP, "l1");
      assertMapEvent(r, EventType.DOUBLE, "l2");
      assertMapEvent(r, EventType.END_MAP, "l1");
      assertMapEvent(r, EventType.DOUBLE, "l3");
      assertMapEvent(r, EventType.END_MAP, "l");
      assertMapEvent(r, EventType.END_MAP, "k");

      assertMapEvent(r, EventType.END_MAP, null);

      assertNull(r.next());
    }
  }

}
