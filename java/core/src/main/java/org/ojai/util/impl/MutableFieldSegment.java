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

import org.ojai.DocumentReader;
import org.ojai.FieldSegment;
import org.ojai.annotation.API;

/**
 * Used as the key in the {@link ProjectionTree} map. This is mutable so that a single
 * instance of this object can be used for all nodes in the ProjectionTree.
 */
@API.Internal
public class MutableFieldSegment implements Comparable<MutableFieldSegment> {

  private boolean isNamed;
  private int fieldIndex;
  private String fieldName;

  public MutableFieldSegment() {
  }

  MutableFieldSegment(FieldSegment fieldSegment) {
    isNamed = fieldSegment.isNamed();
    if (isNamed) {
      fieldName = fieldSegment.getNameSegment().getName();
    } else {
      fieldIndex = fieldSegment.getIndexSegment().getIndex();
    }
  }

  public MutableFieldSegment fromReaderField(DocumentReader reader) {
    isNamed = reader.inMap();
    if (isNamed) {
      fieldName = reader.getFieldName();
    } else {
      fieldIndex = reader.getArrayIndex();
    }
    return this;
  }

  public boolean isNamed() {
    return this.isNamed;
  }

  public boolean hasIndex() {
    return !this.isNamed;
  }

  public int getIndex() {
    return this.fieldIndex;
  }

  @Override
  public int compareTo(MutableFieldSegment that) {
    if (this.isNamed == that.isNamed) {
      if (isNamed) {
        return (fieldName == null ? (that.fieldName == null ? 0 : -1) : fieldName.compareTo(that.fieldName));
      } else {
        return (fieldIndex - that.fieldIndex);
      }
    } else {
      return isNamed ? 1 : -1;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fieldIndex;
    result = prime * result + (isNamed ? 1231 : 1237);
    result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;

    MutableFieldSegment other = (MutableFieldSegment) obj;
    if (isNamed == other.isNamed) {
      return isNamed ? fieldName.equals(other.fieldName) : fieldIndex == other.fieldIndex;
    }
    return false;
  }

  @Override
  public String toString() {
    return isNamed ? '`' + fieldName + '`' : "[" + fieldIndex + "]";
  }

}
