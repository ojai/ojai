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
package org.ojai.util;

import static org.ojai.DocumentConstants.DOCUMENT_KEY;

import java.util.Arrays;
import java.util.Collection;

import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.FieldPath;
import org.ojai.FieldSegment;
import org.ojai.FieldSegment.NameSegment;
import org.ojai.JsonString;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.json.JsonOptions;
import org.ojai.util.impl.ProjectionTree;
import org.ojai.util.impl.MutableFieldSegment;

import com.google.common.base.Preconditions;

/**
 * A helper class, which maintains a projection tree built form the list of user specified {@link FieldPath}s
 * and in association with a {@link DocumentReader}, determines which fields form the document reader are
 * emitted to the applications.
 * <p/>
 * A single instance of ProjectionTree can not be, concurrently, used with multiple {@link DocumentReader}s.
 * Use {@link #cloneWithSharedProjectionTree()} in such cases.
 */
@API.Public
public final class FieldProjector implements JsonString {

  private static final FieldSegment DOCUMENT_ROOT = new NameSegment(DOCUMENT_KEY, null, false);

  private final ProjectionTree rootSegment;

  private DocumentReader reader;

  private ProjectionTree currentSegment;

  private ProjectionTree lastSegment;

  private MutableFieldSegment readerFieldSegment;

  private int level;

  /**
   * This flag indicates that the current Field should be projected because its FieldPath
   * is a "suffix" of the FieldPath of one of the projected fields.
   * <p/>
   * For example, is "a.b.c" is is one of the projected FieldPath then, this is set for
   * "a", "a.b", and "a.b.c" but not for "a.b.c.d".
   */
  private boolean includeField;

  /**
   * This flag indicates that the current Field should be projected because its FieldPath
   * is a descendant of one of the projected fields.
   */
  private boolean includeAllChildren;

  public FieldProjector(@NonNullable String... includedPaths) {
    this(Fields.toFieldPathArray(
        Preconditions.checkNotNull(includedPaths)));
  }

  public FieldProjector(@NonNullable FieldPath... includedPaths) {
    this(Arrays.asList(
        Preconditions.checkNotNull(includedPaths)));
  }

  public FieldProjector(@NonNullable Collection<FieldPath> includedPaths) {
    rootSegment = new ProjectionTree(DOCUMENT_ROOT, null);
    for (FieldPath includedPath : Preconditions.checkNotNull(includedPaths)) {
      rootSegment.addOrGetChild(includedPath.getRootSegment());
    }
    finishConstruction();
  }

  /**
   * Creates a lightweight clone of this FieldProjector with shared ProjectionTree.
   */
  public FieldProjector cloneWithSharedProjectionTree() {
    return new FieldProjector(rootSegment);
  }

  private FieldProjector(final ProjectionTree rootSegment) {
    this.rootSegment = rootSegment;
    finishConstruction();
  }

  /**
   * Sets/resets the state variable to their state at the construction time.
   */
  private void finishConstruction() {
    if (rootSegment == null) {
      throw new AssertionError("`rootSegment` needs to be set before calling this method");
    }
    level = 0;
    reader = null;
    currentSegment = rootSegment;
    lastSegment = rootSegment;
    includeField = false;
    includeAllChildren = false;
    readerFieldSegment = new MutableFieldSegment();
  }

  boolean shouldEmitEvent() {
    return includeField || includeAllChildren;
  }

  /**
   * This is the main algorithm that determine if the current {@link DocumentReader}
   * node should be included or excluded based on the set of projected fields.
   */
  void moveTo(EventType event) {
    ProjectionTree childSegment = null;
    if (currentSegment != null) { // skipped in short-circuit mode
        if (event == EventType.END_MAP || event == EventType.END_ARRAY) {
          /*
           * END_XXXX events from the reader are always emitted because if the corresponding
           * START_XXXX was not projected, the entire sub-tree was skipped outside of this loop.
           */
          includeField = true;
        } else {
          /*
           * Morph the field segment cursor to the current reader field
           */
          readerFieldSegment.fromReaderField(reader);
          /*
           * and find the corresponding field segment in the projection tree
           */
          childSegment = currentSegment.findChild(readerFieldSegment);
          /*
           * If a matching field segment if found, the current reader field is included
           */
          includeField = (childSegment != null)
              /*
               * however, if the current reader field is a scalar type, the corresponding
               * field segment in the projection tree must be a leaf node.
               */
              && (event == EventType.START_MAP || event == EventType.START_ARRAY || childSegment.isLeafSegment());
        }
    }

    switch (event) {
    case START_MAP:
    case START_ARRAY:
      if (shouldEmitEvent()) {
        if (currentSegment != null) { // skipped in short-circuit mode
          currentSegment = childSegment;
          if (childSegment != null) {
            lastSegment = childSegment;
            includeAllChildren = childSegment.isLeafSegment();
            if (includeAllChildren) {
              /*
               * If the current field segment in the projection tree is a leaf node,
               * ALL children of the current DocumentReader field are included.
               *
               * At this point in the tree, we descent into the projection tree using
               * a level counter, which is incremented/decremented as we traversed
               * through the containers of the sub-tree.
               */
              level = 1;
              /*
               * Setting `currentSegment` to null allows us to short-circuit this
               * decision tree for all fields which are descendant of one of the projected fields
               * i.e. if the projected field path is "a.b.c", then testing the inclusion of
               * "a.b.c.d", "a.b.c.d.e", "a.b.c.e.f", etc happens very quickly
               */
              currentSegment = null;
            }
          }
        } else {
          level++;
        }
      }
      break;
    case END_MAP:
    case END_ARRAY:
      if (shouldEmitEvent()) {
        if (currentSegment == null) {
          level--;
          if (level == 0) {
            /*
             * While traversing up the sub-tree of a projected field, when we get back to the
             * last segment of the projected field, we get out of the short-circuit mode.
             */
            currentSegment = lastSegment.getParent();
            includeAllChildren = false;
          }
        } else { // skipped in short-circuit mode
          currentSegment = currentSegment.getParent();
        }
      }
      break;
    default: // scalar events
      break;
    }
  }

  /**
   * Resets the state of this projection tree to the root of the Document.
   * @return {@code this} for chaining.
   */
  FieldProjector reset(DocumentReader reader) {
    this.reader = reader;
    this.currentSegment = rootSegment;
    return this;
  }

  @Override
  public String toString() {
    return asJsonString();
  }

  @Override
  public String asJsonString() {
    return rootSegment.toString();
  }

  @Override
  public String asJsonString(JsonOptions options) {
    return asJsonString();
  }

}
