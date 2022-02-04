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
package org.ojai.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ojai.FieldPath.EMPTY;

import java.util.Iterator;

import org.junit.Test;
import org.ojai.FieldPath;
import org.ojai.FieldSegment;

public class TestFieldPath extends BaseTest {

  @Test
  public void testEmptyFieldPath() {
    FieldSegment root = EMPTY.getRootSegment();
    assertNotNull(root);
    assertTrue(root.isLastPath());
    assertTrue(root.isLeaf());
    assertEquals("", EMPTY.asPathString());

    FieldPath fp = FieldPath.parseFrom("``");
    assertSame(fp, EMPTY);

    fp = FieldPath.parseFrom("\"\"");
    assertSame(fp, EMPTY);

    fp = FieldPath.parseFrom("");
    assertSame(fp, EMPTY);
  }

  @Test
  public void testPathWithUnderscore() {
    FieldPath fp = FieldPath.parseFrom("work_phone");
    assertTrue(fp.getRootSegment().isLeaf());
    assertEquals("\"work_phone\"", fp.asPathString(true));
    assertEquals("work_phone", fp.asPathString());
    assertEquals("\"work_phone\"", fp.asJsonString());
  }

  @Test
  public void testPathWithHyphen() {
    FieldPath fp = FieldPath.parseFrom("work-phone");
    assertTrue(fp.getRootSegment().isLeaf());
    assertEquals("\"work-phone\"", fp.asPathString(true));
    assertEquals("work-phone", fp.asPathString());
    assertEquals("\"work-phone\"", fp.asJsonString());
  }

  @Test
  public void testPathWithSpace() {
    FieldPath fp = FieldPath.parseFrom("work phone.cell phone");
    assertFalse(fp.getRootSegment().isLeaf());
    assertTrue(fp.getRootSegment().getChild().isLeaf());
    assertEquals("\"work phone\".\"cell phone\"", fp.asPathString(true));
    assertEquals("work phone.cell phone", fp.asPathString());
    assertEquals("\"work phone.cell phone\"", fp.asJsonString());

    fp = FieldPath.parseFrom("a[ ]");
    assertEquals("\"a\"[]", fp.asPathString(true));
    assertEquals("a[]", fp.asPathString());
    assertEquals("\"a[]\"", fp.asJsonString());
  }

  @Test
  public void testSimplePathSingleSegment() {
    FieldPath fp = FieldPath.parseFrom("a");
    assertTrue(fp.getRootSegment().isLeaf());
    assertEquals("\"a\"", fp.asPathString(true));
    assertEquals("a", fp.asPathString());
    assertEquals("\"a\"", fp.asJsonString());
  }

  @Test
  public void testQuotedPath() {
    FieldPath fp = FieldPath.parseFrom("\"the quick.brown fox\"");
    assertTrue(fp.getRootSegment().isLastPath());
    assertEquals("\"the quick.brown fox\"", fp.asPathString(true));
    assertEquals("\"the quick.brown fox\"", fp.asPathString());
  }

  @Test
  public void testQuotedEscapedPath() {
    FieldPath fp = FieldPath.parseFrom("\"the\\\"quick.brown\\\\fox\"");
    assertTrue(fp.getRootSegment().isLastPath());
    assertEquals("\"the\\\"quick.brown\\\\fox\"", fp.asPathString(true));
    assertEquals("\"the\\\"quick.brown\\\\fox\"", fp.asPathString(false));
    assertEquals("\"the\\\"quick.brown\\\\fox\"", fp.asPathString());
    assertEquals("\"\\\"the\\\\\"quick.brown\\\\fox\\\"\"", fp.asJsonString());
  }

  @Test
  public void testSimplePathDoubleSegment() {
    FieldPath fp = FieldPath.parseFrom("a.\"b\"");
    assertTrue(fp.getRootSegment().isMap());
    assertTrue(fp.getRootSegment().getChild().isLeaf());
    assertEquals("\"a\".\"b\"", fp.asPathString(true));
    assertEquals("a.\"b\"", fp.asPathString());
    assertEquals("\"a.\\\"b\\\"\"", fp.asJsonString());
  }

  @Test
  public void testSimplePathWithArrays() {
    FieldPath fp = FieldPath.parseFrom("a.b[3].c");
    assertTrue(fp.getRootSegment().isMap());
    assertTrue(fp.getRootSegment().getChild().isArray());
    assertTrue(fp.getRootSegment().getChild().getChild().isIndexed());
    assertTrue(fp.getRootSegment().getChild().getChild().isMap());
    assertTrue(fp.getRootSegment().getChild().getChild().getChild().isLeaf());
    assertEquals("\"a\".\"b\"[3].\"c\"", fp.asPathString(true));
    assertEquals("a.b[3].c", fp.asPathString());
  }

  @Test
  public void testSimplePathWithNumericNameSegments() {
    FieldPath fp = FieldPath.parseFrom("1.23.4a");
    assertTrue(fp.getRootSegment().isMap());
    assertTrue(fp.getRootSegment().getChild().isMap());
    assertTrue(fp.getRootSegment().getChild().getChild().isNamed());
    assertTrue(fp.getRootSegment().getChild().getChild().isLeaf());
    assertEquals("\"1\".\"23\".\"4a\"", fp.asPathString(true));
    assertEquals("1.23.4a", fp.asPathString());
    assertEquals("\"1.23.4a\"", fp.asJsonString());
  }

  @Test
  public void testSimplePathWithArraysEmptyIndex() {
    FieldPath fp = FieldPath.parseFrom("a.b[].c");
    assertTrue(fp.getRootSegment().isMap());
    assertTrue(fp.getRootSegment().getChild().isArray());
    assertTrue(fp.getRootSegment().getChild().getChild().isIndexed());
    assertTrue(fp.getRootSegment().getChild().getChild().isMap());
    assertTrue(fp.getRootSegment().getChild().getChild().getChild().isLeaf());
    assertEquals("\"a\".\"b\"[].\"c\"", fp.asPathString(true));
    assertEquals("a.b[].c", fp.asPathString());
    assertEquals("\"a.b[].c\"", fp.asJsonString());
  }

  @Test
  public void testEscapedPathSingleSegment() {
    FieldPath fp = FieldPath.parseFrom("\"a\"");
    assertTrue(fp.getRootSegment().isLeaf());
    assertEquals("\"a\"", fp.asPathString());
    assertEquals("\"a\"", fp.asPathString(false));
    assertEquals("\"\\\"a\\\"\"", fp.asJsonString());
  }

  @Test
  public void testEscapedPathDoubleSegment() {
    FieldPath fp = FieldPath.parseFrom("\"a.b\"");
    assertTrue(fp.getRootSegment().isLeaf());
    assertEquals("\"a.b\"", fp.asPathString());
    assertEquals("\"a.b\"", fp.asPathString(false));
    assertEquals("\"\\\"a.b\\\"\"", fp.asJsonString());
  }

  @Test
  public void testEscapedPathWithArrays() {
    FieldPath fp = FieldPath.parseFrom("a.\"b[3].c\"");
    assertTrue(fp.getRootSegment().isMap());
    assertTrue(fp.getRootSegment().getChild().isLeaf());
    assertEquals("\"a\".\"b[3].c\"", fp.asPathString(true));
    assertEquals("a.\"b[3].c\"", fp.asPathString());
    assertEquals("\"a.\\\"b[3].c\\\"\"", fp.asJsonString());
  }

  @Test
  public void testEscapedPathWithArraysEmptyIndex() {
    FieldPath fp = FieldPath.parseFrom("\"a.b\"[].c");
    assertTrue(fp.getRootSegment().isArray());
    assertTrue(fp.getRootSegment().getChild().isIndexed());
    assertTrue(fp.getRootSegment().getChild().isMap());
    assertTrue(fp.getRootSegment().getChild().getChild().isLeaf());
    assertEquals("\"a.b\"[].\"c\"", fp.asPathString(true));
    assertEquals("\"a.b\"[].c", fp.asPathString());
    assertEquals("\"\\\"a.b\\\"[].c\"", fp.asJsonString());
  }

  @Test
  public void testPathWithIlleagalArrayIndex() {
    char[] illegalArrayIndices = {0, 5, 16, 'a', '\u4251'};
    for (char ch : illegalArrayIndices) {
      try {
        FieldPath.parseFrom("a[" + ch + "]");
        fail("Parsing should fail for character with code " + (int) ch);
      } catch (IllegalArgumentException e) { }
    }
  }

  @Test
  public void testCaching() {
    FieldPath fp1 = FieldPath.parseFrom("a.b.c[4]");
    FieldPath fp2 = FieldPath.parseFrom("a.b.c[4]");
    assertSame(fp1, fp2);
  }

  @Test
  public void testCanonicalForm() {
    FieldPath fp1 = FieldPath.parseFrom("a.b.\"c\"[4]");
    FieldPath fp2 = FieldPath.parseFrom("a.\"b\".c[4]");
    assertEquals(fp1, fp2);
  }

  @Test
  public void testSegmentIterator() {
    FieldPath fp = FieldPath.parseFrom("a.b.\"c\"[4].x");
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

  @Test
  public void testCloneWithNewParent() {
    FieldPath fp1 = FieldPath.parseFrom("a.b.c");
    FieldPath fp2 = fp1.cloneWithNewParent("v");
    assertEquals("v.a.b.c", fp2.asPathString());
    assertEquals(fp1.getRootSegment(), fp2.getRootSegment().getChild());
  }

  @Test
  public void testCloneAfterAncestor() {
    FieldPath parent = FieldPath.parseFrom("a.b.c");
    FieldPath child = FieldPath.parseFrom("a.b.c.d");

    FieldPath progeny = child.cloneAfterAncestor(parent);
    assertEquals("d", progeny.asPathString());
    assertEquals(EMPTY, child.cloneAfterAncestor(child));
    assertNull(child.cloneAfterAncestor(FieldPath.parseFrom("a.b.d")));

    parent = FieldPath.parseFrom("a.b[2]");
    child = FieldPath.parseFrom("a.b[2].c");
    progeny = child.cloneAfterAncestor(parent);
    assertEquals("c", progeny.asPathString());
    assertEquals("\"c\"", progeny.asJsonString());
  }

  @Test
  public void testFieldPathParentChild() {
    FieldPath parent = FieldPath.parseFrom("a.b.c");
    FieldPath child = FieldPath.parseFrom("a.b.c.d");
    assertTrue(parent.isAtOrAbove(child));
    assertFalse(parent.isAtOrBelow(child));
    assertTrue(child.isAtOrBelow(parent));
    assertFalse(child.isAtOrAbove(parent));

    parent = FieldPath.parseFrom("a.b[2]");
    child = FieldPath.parseFrom("a.b[2].c");
    assertTrue(parent.isAtOrAbove(child));
    assertFalse(parent.isAtOrBelow(child));
    assertTrue(child.isAtOrBelow(parent));
    assertFalse(child.isAtOrAbove(parent));
  }

  private static String hexDump(String binStr) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < binStr.length(); i++) {
      char ch = binStr.charAt(i);
      if (ch < ' ') {
        sb.append(String.format("\\x%02x", (int)ch));
      } else {
        sb.append(ch);
      }
    }
    return sb.toString();
  }

  @Test
  public void testFieldPathInvalidSequences() {
    String[] invalidQuotes = {"`", "\""};
    for (String invalidQuote : invalidQuotes) {
      try {
        FieldPath.parseFrom(invalidQuote);
        fail("FieldPath parsing should have failed for sequence: " + hexDump(invalidQuote));
      } catch (IllegalArgumentException e) {}
    }

    String[] invalidSequences = {"\\", "\b", "\f", "\n", "\r", "\t"};
    for (String invalidSequence : invalidSequences) {
      try {
        FieldPath.parseFrom(invalidSequence);
        fail("FieldPath parsing should have failed for sequence: " + hexDump(invalidSequence));
      } catch (IllegalArgumentException e) {}

      // quoted with double-quote
      String quotedInvalidSequence = "\"" + invalidSequence + "\"";
      try {
        FieldPath.parseFrom(quotedInvalidSequence);
        fail("FieldPath parsing should have failed for sequence: " + hexDump(quotedInvalidSequence));
      } catch (IllegalArgumentException e) {}

      // quoted with back-tick
      quotedInvalidSequence = "`" + invalidSequence + "`";
      try {
        FieldPath.parseFrom(quotedInvalidSequence);
        fail("FieldPath parsing should have failed for sequence: " + hexDump(quotedInvalidSequence));
      } catch (IllegalArgumentException e) {}
    }
  }

  @Test
  public void testFieldPathEscapedInvalidSequences() {
    String[] validSequences = {"\\`", "\\\"", "\\\\", "\\.", "\\[", "\\]"};
    String sequence = null;
    try {
      for (String validSequence : validSequences) {
        sequence = validSequence;
        FieldPath.parseFrom(validSequence);

        // quoted with double-quote
        sequence = "\"" + validSequence + "\"";
        FieldPath.parseFrom(sequence);

        // quoted with back-tick
        sequence = "`" + validSequence + "`";
        FieldPath.parseFrom(sequence);
      }
    } catch (IllegalArgumentException e) {
      fail("FieldPath parsing failed for sequence: " + hexDump(sequence));
    }
  }

  @Test
  public void testFieldPathUnicodeSequence() {
    // Java literal string "\u0041.\u0042", parsed by FieldPath grammar
    FieldPath fp = FieldPath.parseFrom("\\u0041.\\u0042");
    Iterator<FieldSegment> segItr = fp.iterator();

    assertTrue(segItr.hasNext());
    FieldSegment seg = segItr.next();
    assertTrue(seg.isNamed());
    assertEquals("A", seg.getNameSegment().getName());

    assertTrue(segItr.hasNext());
    seg = segItr.next();
    assertTrue(seg.isNamed());
    assertEquals("B", seg.getNameSegment().getName());

    // Unicode sequence parsed by Java literal String grammar
    fp = FieldPath.parseFrom("\u0041.\u0042");
    segItr = fp.iterator();

    assertTrue(segItr.hasNext());
    seg = segItr.next();
    assertTrue(seg.isNamed());
    assertEquals("A", seg.getNameSegment().getName());

    assertTrue(segItr.hasNext());
    seg = segItr.next();
    assertTrue(seg.isNamed());
    assertEquals("B", seg.getNameSegment().getName());
  }

  @Test
  public void testFieldPathEscapedWithoutQuote() {
    FieldPath fp = FieldPath.parseFrom("a\\.b\\[4\\]");
    Iterator<FieldSegment> segItr = fp.iterator();

    assertTrue(segItr.hasNext());
    FieldSegment seg = segItr.next();
    assertTrue(seg.isNamed());
    assertEquals("a.b[4]", seg.getNameSegment().getName());
    assertFalse(segItr.hasNext());

    assertEquals("a\\.b\\[4\\]", fp.asPathString());
    assertEquals("a\\.b\\[4\\]", fp.toString());
    assertEquals("a\\.b\\[4\\]", fp.asPathString(false));
    assertEquals("\"a.b[4]\"", fp.asPathString(true));
    assertEquals("\"a\\.b\\[4\\]\"", fp.asJsonString());
  }

  @Test
  public void testFieldPathEscapedControlCharsMedley() {
    FieldPath fp = FieldPath.parseFrom("`a\\\"`" // quoted with back-tick
        + "."
        + "\"\\u000F\\`\\\"\\b\\f\\n\\r\\t\\\\\\//000C\"" // quoted with double-quote
        + "."
        + "c\\u0034 \\`p"); // unquoted
    Iterator<FieldSegment> segItr = fp.iterator();

    assertTrue(segItr.hasNext());
    FieldSegment seg = segItr.next();
    assertTrue(seg.isNamed());
    assertEquals("a\"", seg.getNameSegment().getName());

    assertTrue(segItr.hasNext());
    seg = segItr.next();
    assertTrue(seg.isNamed());
    assertEquals("\u000F`\"\b\f\n\r\t\\//000C", seg.getNameSegment().getName());

    assertTrue(segItr.hasNext());
    seg = segItr.next();
    assertTrue(seg.isNamed());
    assertEquals("c4 `p", seg.getNameSegment().getName());

    assertFalse(segItr.hasNext());
  }

}
