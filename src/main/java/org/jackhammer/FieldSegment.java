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
package org.jackhammer;

public abstract class FieldSegment implements Comparable<FieldSegment> {

  FieldSegment child;

  int hash;

  public abstract FieldSegment cloneWithNewChild(FieldSegment segment);
  public abstract int segmentCompareTo(FieldSegment o);

  @Override
  public abstract FieldSegment clone();

  public static final class ArraySegment extends FieldSegment {
    private final int index;

    public ArraySegment(String numberAsText, FieldSegment child) {
      this(numberAsText == null ? -1 : Integer.parseInt(numberAsText), child);
    }

    public ArraySegment(int index, FieldSegment child) {
      this.child = child;
      this.index = index;
      assert index >= -1;
    }

    public ArraySegment(FieldSegment child) {
      this.child = child;
      this.index = -1;
    }

    public boolean hasIndex() {
      return index != -1;
    }

    public ArraySegment(int index) {
      if (index < 0 ) {
        throw new IllegalArgumentException();
      }
      this.index = index;
    }

    public int getIndex() {
      return index;
    }

    @Override
    public boolean isArray() {
      return true;
    }

    @Override
    public boolean isNamed() {
      return false;
    }

    @Override
    public ArraySegment getArraySegment() {
      return this;
    }

    @Override
    public int segmentHashCode() {
      return index;
    }

    @Override
    public int segmentCompareTo(FieldSegment other) {
      if (other instanceof ArraySegment) {
        ArraySegment that = (ArraySegment)other;
        return this.index - that.index;
      }
      return other == null? 1 : -1;
    }

    @Override
    public boolean segmentEquals(FieldSegment obj) {
      if (this == obj) {
        return true;
      } else if (obj == null) {
        return false;
      } else if (obj instanceof ArraySegment) {
        return index == ((ArraySegment)obj).getIndex();
      }
      return false;
    }

    @Override
    public FieldSegment clone() {
      FieldSegment seg = index < 0 ? new ArraySegment(null) : new ArraySegment(index);
      if (child != null) {
        seg.setChild(child.clone());
      }
      return seg;
    }

    @Override
    public ArraySegment cloneWithNewChild(FieldSegment newChild) {
      ArraySegment seg = index < 0 ? new ArraySegment(null) : new ArraySegment(index);
      if (child != null) {
        seg.setChild(child.cloneWithNewChild(newChild));
      } else {
        seg.setChild(newChild);
      }
      return seg;
    }

    @Override
    protected StringBuilder writeSegment(StringBuilder sb, boolean escape) {
      sb.append('[');
      if (index >= 0) {
        sb.append(index);
      }
      sb.append(']');
      return sb;
    }

  }

  public static final class NameSegment extends FieldSegment {
    private final String name;

    public NameSegment(CharSequence n, FieldSegment child) {
      this.child = child;
      this.name = n.toString();
    }

    public NameSegment(CharSequence n) {
      this.name = n.toString();
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean isArray() {
      return false;
    }

    @Override
    public boolean isNamed() {
      return true;
    }

    @Override
    public int segmentCompareTo(FieldSegment o) {
      if (o instanceof NameSegment) {
        NameSegment that = (NameSegment)o;
        return this.name.compareTo(that.name);
      }
      return +1;
    }

    @Override
    public NameSegment getNameSegment() {
      return this;
    }

    @Override
    public int segmentHashCode() {
      return ((name == null) ? 0 : name.toLowerCase().hashCode());
    }

    @Override
    public boolean segmentEquals(FieldSegment obj) {
      if (this == obj) {
        return true;
      } else if (obj == null) {
        return false;
      } else if (getClass() != obj.getClass()) {
        return false;
      }

      NameSegment other = (NameSegment) obj;
      if (name == null) {
        return other.name == null;
      }
      return name.equalsIgnoreCase(other.name);
    }

    @Override
    public NameSegment clone() {
      NameSegment s = new NameSegment(this.name);
      if (child != null) {
        s.setChild(child.clone());
      }
      return s;
    }

    @Override
    public NameSegment cloneWithNewChild(FieldSegment newChild) {
      NameSegment s = new NameSegment(this.name);
      if (child != null) {
        s.setChild(child.cloneWithNewChild(newChild));
      } else {
        s.setChild(newChild);
      }
      return s;
    }

    @Override
    protected StringBuilder writeSegment(StringBuilder sb, boolean escape) {
      if (escape) sb.append('`');
      sb.append(name);
      if (escape) sb.append('`');
      return sb;
    }

  }

  public NameSegment getNameSegment() {
    throw new UnsupportedOperationException();
  }

  public ArraySegment getArraySegment() {
    throw new UnsupportedOperationException();
  }

  public abstract boolean isArray();
  public abstract boolean isNamed();
  protected abstract StringBuilder writeSegment(StringBuilder sb, boolean escape);

  @Override
  public final String toString() {
    return asPathString(true);
  }

  public final String asPathString(boolean escape) {
    StringBuilder sb = new StringBuilder();
    FieldSegment seg = this;
    seg.writeSegment(sb, escape);
    while ((seg = seg.getChild()) != null) {
      if (seg.isNamed()) {
        sb.append('.');
      }
      seg.writeSegment(sb, escape);
    }
    return sb.toString();
  }

  public boolean isLastPath() {
    return child == null;
  }

  public FieldSegment getChild() {
    return child;
  }

  void setChild(FieldSegment child) {
    this.child = child;
  }

  protected abstract int segmentHashCode();
  protected abstract boolean segmentEquals(FieldSegment other);

  @Override
  public int hashCode() {
    int h = hash;
    if (h == 0) {
      h = segmentHashCode();
      h = 31*h + ((child == null) ? 0 : child.hashCode());
      hash = h;
    }
    return h;
  }

  @Override
  public int compareTo(FieldSegment other) {
    int cmp = segmentCompareTo(other);
    if (cmp != 0) {
      return cmp;
    } else if (child == null) {
      return (other.child == null) ? 0 : -1;
    } else {
      return child.compareTo(other.child);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    FieldSegment other = (FieldSegment) obj;
    if (!segmentEquals(other)) {
      return false;
    } else if (child == null) {
      return (other.child == null);
    } else {
      return child.equals(other.child);
    }
  }

  /**
   * Check if another path is contained in this one. This is useful for 2 cases. The first
   * is checking if the other is lower down in the tree, below this path. The other is if
   * a path is actually contained above the current one.
   *
   * Examples:
   * [a] . contains( [a.b.c] ) returns true
   * [a.b.c] . contains( [a] ) returns true
   *
   * This behavior is used for cases like scanning json in an event based fashion, when we arrive at
   * a node in a complex type, we will know the complete path back to the root. This method can
   * be used to determine if we need the data below. This is true in both the cases where the
   * column requested from the user is below the current node (in which case we may ignore other nodes
   * further down the tree, while keeping others). This is also the case if the requested path is further
   * up the tree, if we know we are at position a.b.c and a.b was a requested column, we need to scan
   * all of the data at and below the current a.b.c node.
   *
   * @param otherSeg - path segment to check if it is contained below this one.
   * @return - is this a match
   */
  public boolean contains(FieldSegment otherSeg) {
    if (this == otherSeg) {
      return true;
    }
    if (otherSeg == null) {
      return false;
    }
    // TODO - fix this in the future to match array segments are part of the path
    // the current behavior to always return true when we hit an array may be useful in some cases,
    // but we can get better performance in the JSON reader if we avoid reading unwanted elements in arrays
    if (otherSeg.isArray() || this.isArray()) {
      return true;
    }
    if (getClass() != otherSeg.getClass()) {
      return false;
    }

    if (!segmentEquals(otherSeg)) {
      return false;
    }
    else if (child == null || otherSeg.child == null) {
      return true;
    } else {
      return child.contains(otherSeg.child);
    }

  }

}
