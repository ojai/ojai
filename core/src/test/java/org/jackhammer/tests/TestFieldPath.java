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
    assertTrue(fp.getRootSegment().isLeaf());
    assertEquals("`a`", fp.asPathString(true));
    assertEquals("a", fp.asPathString());
  }

  @Test
  public void testSimplePathDoubleSegment() {
    FieldPath fp = FieldPath.parseFrom("a.`b`");
    assertTrue(fp.getRootSegment().isMap());
    assertTrue(fp.getRootSegment().getChild().isLeaf());
    assertEquals("`a`.`b`", fp.asPathString(true));
    assertEquals("a.`b`", fp.asPathString());
  }

  @Test
  public void testSimplePathWithArrays() {
    FieldPath fp = FieldPath.parseFrom("a.b[3].c");
    assertTrue(fp.getRootSegment().isMap());
    assertTrue(fp.getRootSegment().getChild().isArray());
    assertTrue(fp.getRootSegment().getChild().getChild().isIndexed());
    assertTrue(fp.getRootSegment().getChild().getChild().isMap());
    assertTrue(fp.getRootSegment().getChild().getChild().getChild().isLeaf());
    assertEquals("`a`.`b`[3].`c`", fp.asPathString(true));
    assertEquals("a.b[3].c", fp.asPathString());
  }

  @Test
  public void testSimplePathWithArraysEmptyIndex() {
    FieldPath fp = FieldPath.parseFrom("a.b[].c");
    assertTrue(fp.getRootSegment().isMap());
    assertTrue(fp.getRootSegment().getChild().isArray());
    assertTrue(fp.getRootSegment().getChild().getChild().isIndexed());
    assertTrue(fp.getRootSegment().getChild().getChild().isMap());
    assertTrue(fp.getRootSegment().getChild().getChild().getChild().isLeaf());
    assertEquals("`a`.`b`[].`c`", fp.asPathString(true));
    assertEquals("a.b[].c", fp.asPathString());
  }

  @Test
  public void testEscapedPathSingleSegment() {
    FieldPath fp = FieldPath.parseFrom("`a`");
    assertTrue(fp.getRootSegment().isLeaf());
    assertEquals("`a`", fp.asPathString());
    assertEquals("`a`", fp.asPathString(false));
  }

  @Test
  public void testEscapedPathDoubleSegment() {
    FieldPath fp = FieldPath.parseFrom("`a.b`");
    assertTrue(fp.getRootSegment().isLeaf());
    assertEquals("`a.b`", fp.asPathString());
    assertEquals("`a.b`", fp.asPathString(false));
  }

  @Test
  public void testEscapedPathWithArrays() {
    FieldPath fp = FieldPath.parseFrom("a.`b[3].c`");
    assertTrue(fp.getRootSegment().isMap());
    assertTrue(fp.getRootSegment().getChild().isLeaf());
    assertEquals("`a`.`b[3].c`", fp.asPathString(true));
    assertEquals("a.`b[3].c`", fp.asPathString());
  }

  @Test
  public void testEscapedPathWithArraysEmptyIndex() {
    FieldPath fp = FieldPath.parseFrom("`a.b`[].c");
    assertTrue(fp.getRootSegment().isArray());
    assertTrue(fp.getRootSegment().getChild().isIndexed());
    assertTrue(fp.getRootSegment().getChild().isMap());
    assertTrue(fp.getRootSegment().getChild().getChild().isLeaf());
    assertEquals("`a.b`[].`c`", fp.asPathString(true));
    assertEquals("`a.b`[].c", fp.asPathString());
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
    assertEquals(4, itr.next().getIndexSegment().getIndex());
    assertEquals("x", itr.next().getNameSegment().getName());
    assertFalse(itr.hasNext());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionWhenBadIndex() {
    FieldPath.parseFrom("a.b.c[a]");
  }

  @Test
  public void testSortOrder() {
    FieldPath fp0 = FieldPath.parseFrom("a[0]");
    FieldPath fp1 = FieldPath.parseFrom("a[1]");
    FieldPath fp2 = FieldPath.parseFrom("a.b");
    FieldPath fp3 = FieldPath.parseFrom("a.b.c");
    FieldPath fp4 = FieldPath.parseFrom("a.c");
    FieldPath fp5 = FieldPath.parseFrom("a.c[4]");

    assertTrue(fp0.compareTo(fp1) < 0);
    assertTrue(fp1.compareTo(fp2) < 0);
    assertTrue(fp2.compareTo(fp3) < 0);
    assertTrue(fp3.compareTo(fp4) < 0);
    assertTrue(fp4.compareTo(fp5) < 0);
  }

}
