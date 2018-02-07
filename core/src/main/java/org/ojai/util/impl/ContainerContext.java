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
package org.ojai.util.impl;

import org.ojai.annotation.API;
import org.ojai.Value.Type;

@API.Internal
public class ContainerContext {
  private final String fieldName;
  private final Type type;
  private int index;

  public static final ContainerContext NULL = new ContainerContext(null);

  public ContainerContext(Type t) {
    this(t, null);
  }

  public ContainerContext(Type t, String n) {
    type = t;
    fieldName = n;
    index = -1;
  }

  public Type getType() {
    return type;
  }

  public ContainerContext setIndex(int newIndex) {
    if (type != Type.ARRAY) {
      throw new IllegalStateException("setIndex() called on a map");
    }
    index = newIndex;
    return this;
  }

  public int getIndex() {
    if (type != Type.ARRAY) {
      throw new IllegalStateException("getIndex() called on a map");
    }
    return index;
  }

  public void incrementIndex() {
    if (type != Type.ARRAY) {
      throw new IllegalStateException("incrementIndex() called on a map");
    }
    index++;
  }

  @Override
  public String toString() {
    return "[" + type + ", " + index + ", " + fieldName + "]@" + hashCode();
  }

  public String getFieldName() {
    return fieldName;
  }

}
