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
package org.ojai.store;

import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.annotation.API.NonNullable;

public class MutationOp {

  public enum Type {
    SET,
    SET_OR_REPLACE,
    DELETE,
    INCREMENT,
    APPEND,
    MERGE
  }

  private Type type;
  private FieldPath fieldPath;
  private Value opValue;

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public FieldPath getFieldPath() {
    return fieldPath;
  }

  public void setFieldPath(@NonNullable FieldPath fieldPath) {
    this.fieldPath = fieldPath;
  }

  public Value getOpValue() {
    return opValue;
  }

  public void setOpValue(@NonNullable Value opValue) {
    this.opValue = opValue;
  }

}
