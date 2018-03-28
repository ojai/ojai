/**
 * Copyright (c) 2018 MapR, Inc.
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

@API.Public
public abstract class BaseFieldProjector implements JsonString {
  private static final FieldSegment DOCUMENT_ROOT = new NameSegment(DOCUMENT_KEY, null, false);

  protected final ProjectionTree rootSegment;

  protected ProjectionTree currentSegment;

  protected ProjectionTree lastSegment;

  protected MutableFieldSegment readerFieldSegment;

  protected int level;

  /**
   * This flag indicates that the current Field should be projected because its FieldPath
   * is a "suffix" of the FieldPath of one of the projected fields.
   * <p/>
   * For example, is "a.b.c" is is one of the projected FieldPath then, this is set for
   * "a", "a.b", and "a.b.c" but not for "a.b.c.d".
   */
  protected boolean includeField;

  /**
   * This flag indicates that the current Field should be projected because its FieldPath
   * is a descendant of one of the projected fields.
   */
  protected boolean includeAllChildren;

  protected BaseFieldProjector(@NonNullable String... includedPaths) {
    this(Fields.toFieldPathArray(
        Preconditions.checkNotNull(includedPaths)));
  }

  protected BaseFieldProjector(@NonNullable FieldPath... includedPaths) {
    this(Arrays.asList(
        Preconditions.checkNotNull(includedPaths)));
  }

  protected BaseFieldProjector(@NonNullable Collection<FieldPath> includedPaths) {
    rootSegment = new ProjectionTree(DOCUMENT_ROOT, null);
    for (FieldPath includedPath : Preconditions.checkNotNull(includedPaths)) {
      rootSegment.addOrGetChild(includedPath.getRootSegment());
    }
    finishConstruction();
  }

  /**
   * Creates a lightweight clone of this FieldProjector with shared ProjectionTree.
   */
  public abstract BaseFieldProjector cloneWithSharedProjectionTree();

  protected BaseFieldProjector(final ProjectionTree rootSegment) {
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
    currentSegment = rootSegment;
    lastSegment = rootSegment;
    includeField = false;
    includeAllChildren = false;
    readerFieldSegment = new MutableFieldSegment();
  }

  protected boolean shouldEmitEvent() {
    return includeField || includeAllChildren;
  }

  /**
   * This is the main algorithm that determine if the current {@link DocumentReader}
   * node should be included or excluded based on the set of projected fields.
   */
  protected abstract void moveTo(EventType event);

  /**
   * Resets the state of this projection tree to the root of the Document.
   * @return {@code this} for chaining.
   */
  protected abstract BaseFieldProjector reset(DocumentReader reader);

  /**
   * Resets the state of this projection tree to the root of the Document
   * when not using with a DocumentReader
   * @return {@code this} for chaining;
   */
  protected abstract BaseFieldProjector reset();

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
