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
import org.ojai.JsonString;
import org.ojai.annotation.API;
import org.ojai.json.JsonOptions;

import com.google.common.collect.Maps;

@API.Internal
public final class ProjectionTree implements JsonString {
  private final FieldSegment fieldSegment;
  private final ProjectionTree parent;

  TreeMap<MutableFieldSegment, ProjectionTree> children;
  private boolean isLeafSegment;

  public ProjectionTree(final FieldSegment fieldSegment, final ProjectionTree parent) {
    this.parent = parent;
    this.fieldSegment = fieldSegment;
    this.isLeafSegment = false;
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
      subTree.addOrGetChild(child.getChild());
    }
  }

  private TreeMap<MutableFieldSegment, ProjectionTree> getChildren() {
    if (children == null) {
      children = Maps.newTreeMap();
    }
    return children;
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
    return children == null ? null : children.get(fieldSegment);
  }

  public ProjectionTree getParent() {
    return parent;
  }

}