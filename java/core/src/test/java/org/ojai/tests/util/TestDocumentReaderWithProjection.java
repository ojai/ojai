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
import org.ojai.Document;
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

  @Test
  public void testProjectEntireArrayOnSubdocument() {
    Document doc = Json.newDocument()
                       .set("a.b", "ab")
                       .set("a.c.d", "acd")
                       .set("a.x", "ax")
                       .setArray("a.d", new Object[] {"ad1","ad2","ad3"});

    DocumentReader docReader = doc.asReader();
    String[] paths = {"a[].b","a.c[]","a.d[2]","a[].x[]"};
    FieldProjector fieldProjector = new FieldProjector(paths);

    DocumentReaderWithProjection r = new DocumentReaderWithProjection(docReader, fieldProjector);

    assertMapEvent(r, EventType.START_MAP, null);
      assertMapEvent(r, EventType.START_MAP, "a");
        assertMapEvent(r, EventType.STRING, "b");

        assertMapEvent(r, EventType.START_MAP, "c");
          assertMapEvent(r, EventType.STRING, "d");
        assertMapEvent(r, EventType.END_MAP, "c");

        assertMapEvent(r, EventType.STRING, "x");

        assertMapEvent(r, EventType.START_ARRAY, "d");
          assertArrayEvent(r, EventType.STRING, 2);
        assertMapEvent(r, EventType.END_ARRAY, "d");

      assertMapEvent(r, EventType.END_MAP, "a");
    assertMapEvent(r, EventType.END_MAP, null);
    assertNull(r.next());
  }

  @Test
  public void testProjectEntireArrayOnArray() {
    Document subDoc1 = Json.newDocument()
                           .set("d", "cd1");
    Document subDoc2 = Json.newDocument()
                           .set("d1", "cd2");
    Document subDoc3 = Json.newDocument()
                           .set("d", "cd3");

    Document doc = Json.newDocument()
                       .setArray("a.b", new Object[] { "ab1", "ab2", "ab3"})
                       .setArray("a.c", new Object[] { subDoc1, subDoc2, subDoc3 })
                       .setArray("a.d", new Object[] {
                                        new Object [] { (long) 0xad1, (long) 0xad2 },
                                        new Object [] { (long) 0xad3, (long) 0xad4 }
                                        }
                        )
                       .set("a.e", "ae");

    DocumentReader docReader = doc.asReader();
    String[] paths = {"a[].b[]","a.c[].d","a.d[]"};
    FieldProjector fieldProjector = new FieldProjector(paths);

    DocumentReaderWithProjection r = new DocumentReaderWithProjection(docReader, fieldProjector);

    assertMapEvent(r, EventType.START_MAP, null);
      assertMapEvent(r, EventType.START_MAP, "a");

        assertMapEvent(r, EventType.START_ARRAY, "b");
          assertArrayEvent(r, EventType.STRING, 0);
          assertArrayEvent(r, EventType.STRING, 1);
          assertArrayEvent(r, EventType.STRING, 2);
        assertMapEvent(r, EventType.END_ARRAY, "b");

        assertMapEvent(r, EventType.START_ARRAY, "c");
          assertArrayEvent(r, EventType.START_MAP, 0);
            assertMapEvent(r, EventType.STRING, "d");
          assertArrayEvent(r, EventType.END_MAP, 0);

          assertArrayEvent(r, EventType.START_MAP, 1);
          assertArrayEvent(r, EventType.END_MAP, 1);

          assertArrayEvent(r, EventType.START_MAP, 2);
            assertMapEvent(r, EventType.STRING, "d");
          assertArrayEvent(r, EventType.END_MAP, 2);
        assertMapEvent(r, EventType.END_ARRAY, "c");

        assertMapEvent(r, EventType.START_ARRAY, "d");
          assertArrayEvent(r, EventType.START_ARRAY, 0);
            assertArrayEvent(r, EventType.LONG, 0);
            assertArrayEvent(r, EventType.LONG, 1);
          assertArrayEvent(r, EventType.END_ARRAY, 0);

          assertArrayEvent(r, EventType.START_ARRAY, 1);
            assertArrayEvent(r, EventType.LONG, 0);
            assertArrayEvent(r, EventType.LONG, 1);
          assertArrayEvent(r, EventType.END_ARRAY, 1);
        assertMapEvent(r, EventType.END_ARRAY, "d");

      assertMapEvent(r, EventType.END_MAP, "a");
    assertMapEvent(r, EventType.END_MAP, null);
  }

  @Test
  public void testProjecEntireArrayOfScalar() {
    Document doc = Json.newDocument()
                     .set("a.b", "ab1")
                     .setArray("a.c.e", new Object[] { "ace1", "ace2", "ace3"})
                     .setArray("a.c.d",
                                new Object[] {
                               new Object[] { "acd1", "acd2", "acd3" },
                               new Object[] { "acd4", "acd5", "acd6" }
                             }
                     )
                     .set("a.d", "ad");

    DocumentReader docReader = doc.asReader();
    String[] paths = {"a.b[]","a.c.d[0][]","a.d[][]"};
    FieldProjector fieldProjector = new FieldProjector(paths);

    DocumentReaderWithProjection r = new DocumentReaderWithProjection(docReader, fieldProjector);

    assertMapEvent(r, EventType.START_MAP, null);
    assertMapEvent(r, EventType.START_MAP, "a");

      assertMapEvent(r, EventType.STRING, "b");

      assertMapEvent(r, EventType.START_MAP, "c");
        assertMapEvent(r, EventType.START_ARRAY, "d");
          assertArrayEvent(r, EventType.START_ARRAY, 0);
            assertArrayEvent(r, EventType.STRING, 0);
            assertArrayEvent(r, EventType.STRING, 1);
            assertArrayEvent(r, EventType.STRING, 2);
          assertArrayEvent(r, EventType.END_ARRAY, 0);
        assertMapEvent(r, EventType.END_ARRAY, "d");
      assertMapEvent(r, EventType.END_MAP, "c");

    assertMapEvent(r, EventType.END_MAP, "a");
    assertMapEvent(r, EventType.END_MAP, null);
  }

  @Test
  public void testProjectionEntireArrayMixed() {
    Document doc = Json.newDocument()
                       .set("a.scalar1", "ab1")
                       .setArray("a.array", new Object[] { "ac1", "ac2", "ac3", "ac4"})
                       .setArray("a.arrayOfArray", new Object[] {
                               new Object[] { "aoa1", "aoa2" , "aoa3" },
                               new Object[] { "aoa4", "aoa5", "aoa6" }
                        })
                       .setArray("a.arrayOfMaps", new Object[] {
                               Json.newDocument().set("map1", "aom1")
                                                 .set("map2", "aom2")
                                                 .set("map3", "aom3"),
                               Json.newDocument().set("map4", "aom4")
                                                 .set("map5", "aom5")
                                                 .set("map6", "aom6")
                        })
                       .set("a.scalar2", (long) 0x12345)
                       .set("b.c", "bc1");

    DocumentReader docReader = doc.asReader();
    String[] paths = {"a.scalar1[]",
                      "a[].array[2]",
                      "a.arrayOfArray[][1]",
                      "a.arrayOfMaps[].map2",
                      "a[].scalar2",
                      "b[]"};

    FieldProjector fieldProjector = new FieldProjector(paths);

    DocumentReaderWithProjection r = new DocumentReaderWithProjection(docReader, fieldProjector);

    assertMapEvent(r, EventType.START_MAP, null);
      assertMapEvent(r, EventType.START_MAP, "a");

        assertMapEvent(r, EventType.STRING, "scalar1");

        assertMapEvent(r, EventType.START_ARRAY, "array");
          assertArrayEvent(r, EventType.STRING, 2);
        assertMapEvent(r, EventType.END_ARRAY, "array");

        assertMapEvent(r, EventType.START_ARRAY, "arrayOfArray");
          assertArrayEvent(r, EventType.START_ARRAY, 0);
            assertArrayEvent(r, EventType.STRING, 1);
          assertArrayEvent(r, EventType.END_ARRAY, 0);

          assertArrayEvent(r, EventType.START_ARRAY, 1);
            assertArrayEvent(r, EventType.STRING, 1);
          assertArrayEvent(r, EventType.END_ARRAY, 1);
        assertMapEvent(r, EventType.END_ARRAY, "arrayOfArray");

        assertMapEvent(r, EventType.START_ARRAY, "arrayOfMaps");
          assertArrayEvent(r, EventType.START_MAP, 0);
            assertMapEvent(r, EventType.STRING, "map2");
          assertArrayEvent(r, EventType.END_MAP, 0);

          assertArrayEvent(r, EventType.START_MAP, 1);
          assertArrayEvent(r, EventType.END_MAP, 1);
        assertMapEvent(r, EventType.END_ARRAY, "arrayOfMaps");

        assertMapEvent(r, EventType.LONG, "scalar2");
      assertMapEvent(r, EventType.END_MAP, "a");

      assertMapEvent(r, EventType.START_MAP, "b");
        assertMapEvent(r, EventType.STRING, "c");
      assertMapEvent(r, EventType.END_MAP, "b");
    assertMapEvent(r, EventType.END_MAP, null);
  }

}
