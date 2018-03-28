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
package org.ojai.util.impl;

import java.util.TreeMap;

import org.ojai.FieldSegment;
import org.ojai.FieldSegment.IndexSegment;
import org.ojai.JsonString;
import org.ojai.annotation.API;
import org.ojai.json.JsonOptions;

import com.google.common.collect.Maps;

@API.Internal
public final class ProjectionTree implements JsonString {
  private static final MutableFieldSegment ENTIRE_ARRAY_SEGMENT =
		  new MutableFieldSegment(new IndexSegment(-1 /*index*/, null/*child*/));

  private final FieldSegment fieldSegment;
  private final ProjectionTree parent;

  TreeMap<MutableFieldSegment, ProjectionTree> children;
  private boolean isLeafSegment;
  private boolean isSingleValueArray;

  public ProjectionTree(final FieldSegment fieldSegment, final ProjectionTree parent) {
    this.parent = parent;
    this.fieldSegment = fieldSegment;
    this.isLeafSegment = false;
    this.isSingleValueArray = false;
  }


  /**
   * Check if curFieldSeg  is index segment with index -1; if so,
   * build an exhaustive tree where all permutations of field with []
   * at this level and lower levels in the fieldpath are added to the
   * targetSubTree.
   *
   * Example 1: For an incoming fieldpath a[].b:
   * the tree will hold the following fields:
   * a[].b
   * a.b
   *
   * Example 2: For a fieldpath a[][].b[].c
   * the tree will hold  the following fields:
   * a[][].b[].c
   * a[][].b.c
   * a[].b[].c
   * a[].b.c
   */
  private void handleEntireArrayProjection(FieldSegment curFieldSeg) {
    IndexSegment indexSeg = curFieldSeg.getChild().getIndexSegment();

    //Check if it is not a terminal [-1] and add grandChild to subtree
    boolean isTerminalIndexSeg = true;
    final FieldSegment grandChild = indexSeg.getChild();

    if (grandChild == null) {
      this.setIsSingleValueArray();
    } else if (grandChild.isIndexed()) {
      final IndexSegment grandChildIndexSeg = (IndexSegment) grandChild;
      if (grandChildIndexSeg.getIndex() == -1) {
        //Since grandChild is also IndexSegment with [-1],
        //child can't be terminal index segment with [-1]
        isTerminalIndexSeg = false;
      }
    }
    if (isTerminalIndexSeg && grandChild != null) {
      addOrGetChild(grandChild);
    }
  }

  public void addOrGetChild(final FieldSegment child) {
    final MutableFieldSegment segment = new MutableFieldSegment(child);
    ProjectionTree subTree = getChildren().get(segment);
    if (subTree == null) {
      subTree = new ProjectionTree(child, this);
      children.put(segment, subTree);
    }

    if (child.isLeaf()) {
      /*
       * Trim the branch in this projection tree if a suffix of an existing field path is encountered
       * i.e. ("a.b.c", "a.b") => ("a.b").
       */
      subTree.setLeafSegment();
    } else if (!subTree.isLeafSegment()) { // do not descent if a suffix field path already exists.
      // if child IndexSegment is [-1]
      final FieldSegment childSegment = child.getChild();
      if (childSegment.isIndexed()
          && childSegment.getIndexSegment().getIndex() == -1) {
        subTree.handleEntireArrayProjection(child);
      }

      subTree.addOrGetChild(child.getChild());
    }
  }

  private TreeMap<MutableFieldSegment, ProjectionTree> getChildren() {
    if (children == null) {
      children = Maps.newTreeMap();
    }
    return children;
  }

  private void setIsSingleValueArray() {
    this.isSingleValueArray = true;
  }

  /**
   * Checks if given segment is an array of single-values.
   * Examples:
   * In "a.b[]", 'b' is an array of single-values
   * In "a.b[][]", 'b[]' is an array of single-values; 'b' is an array
   * In "a[].b", 'a' is NOT an array of single-values; 'a' is an array of map(s)
   * with 'b' as one of its keys.
   */
  public boolean isSingleValueArray() {
    return this.isSingleValueArray;
  }

  @Override
  public String toString() {
    return asJsonString();
  }

  @Override
  public String asJsonString() {
    return "{\"fieldSegment\": " + fieldSegment.asJsonString()
            + ", \"isLeafSegment\": " + isLeafSegment
            + ", \"children\": " + (children == null ? "[]" : children.values())
            + "}";
  }

  @Override
  public String asJsonString(JsonOptions options) {
    return asJsonString();
  }

  private void setLeafSegment() {
    children = null;
    isLeafSegment = true;
  }

  public boolean isLeafSegment() {
    return isLeafSegment;
  }

  public ProjectionTree findChild(MutableFieldSegment fieldSegment) {
    if (children == null) {
      return null;
    }

    ProjectionTree childSegTree = null;
    //When looking for an 'index segment' look for Index Segment with -1 (entire array)
    //If it qualifies
    if (fieldSegment.hasIndex()) {
      childSegTree = children.get(ProjectionTree.ENTIRE_ARRAY_SEGMENT);
    }

    return (childSegTree != null ? childSegTree : children.get(fieldSegment));
  }

  public ProjectionTree getParent() {
    return parent;
  }

}
