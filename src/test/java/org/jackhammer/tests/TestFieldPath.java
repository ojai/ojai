/**
 * Copyright (c) 2014 MapR, Inc.
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
package org.jackhammer.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.jackhammer.FieldPath;
import org.jackhammer.FieldSegment;
import org.junit.Test;

public class TestFieldPath {

  @Test
  public void testSimplePathSingleSegment() {
    FieldPath fp = FieldPath.parseFrom("a");
    assertEquals("`a`", fp.asPathString());
    assertEquals("a", fp.asPathString(false));
  }

  @Test
  public void testSimplePathDoubleSegment() {
    FieldPath fp = FieldPath.parseFrom("a.b");
    assertEquals("`a`.`b`", fp.asPathString());
    assertEquals("a.b", fp.asPathString(false));
  }

  @Test
  public void testSimplePathWithArrays() {
    FieldPath fp = FieldPath.parseFrom("a.b[3].c");
    assertEquals("`a`.`b`[3].`c`", fp.asPathString());
    assertEquals("a.b[3].c", fp.asPathString(false));
  }

  @Test
  public void testSimplePathWithArraysEmptyIndex() {
    FieldPath fp = FieldPath.parseFrom("a.b[].c");
    assertEquals("`a`.`b`[].`c`", fp.asPathString());
    assertEquals("a.b[].c", fp.asPathString(false));
  }

  @Test
  public void testEscapedPathSingleSegment() {
    FieldPath fp = FieldPath.parseFrom("`a`");
    assertEquals("`a`", fp.asPathString());
    assertEquals("a", fp.asPathString(false));
  }

  @Test
  public void testEscapedPathDoubleSegment() {
    FieldPath fp = FieldPath.parseFrom("`a.b`");
    assertEquals("`a.b`", fp.asPathString());
    assertEquals("a.b", fp.asPathString(false));
  }

  @Test
  public void testEscapedPathWithArrays() {
    FieldPath fp = FieldPath.parseFrom("a.`b[3].c`");
    assertEquals("`a`.`b[3].c`", fp.asPathString());
    assertEquals("a.b[3].c", fp.asPathString(false));
  }

  @Test
  public void testEscapedPathWithArraysEmptyIndex() {
    FieldPath fp = FieldPath.parseFrom("`a.b`[].c");
    assertEquals("`a.b`[].`c`", fp.asPathString());
    assertEquals("a.b[].c", fp.asPathString(false));
  }

  @Test
  public void testCaching() {
    FieldPath fp1 = FieldPath.parseFrom("a.b.c[4]");
    FieldPath fp2 = FieldPath.parseFrom("a.b.c[4]");
    assertSame(fp1, fp2);
  }

  @Test
  public void testCanonicalForm() {
    FieldPath fp1 = FieldPath.parseFrom("a.b.`c`[4]");
    FieldPath fp2 = FieldPath.parseFrom("a.`b`.c[4]");
    assertEquals(fp1, fp2);
  }

  @Test
  public void testSegmentIterator() {
    FieldPath fp = FieldPath.parseFrom("a.b.`c`[4].x");
    Iterator<FieldSegment> itr = fp.iterator();
    assertTrue(itr.next().isNamed());
    assertEquals("b", itr.next().getNameSegment().getName());
    assertEquals("c", itr.next().getNameSegment().getName());
    assertEquals(4, itr.next().getArraySegment().getIndex());
    assertEquals("x", itr.next().getNameSegment().getName());
    assertFalse(itr.hasNext());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionWhenBadIndex() {
    FieldPath.parseFrom("a.b.c[a]");
  }

}
