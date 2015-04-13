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

import org.jackhammer.util.Fields;

public abstract class FieldSegment implements Comparable<FieldSegment> {

  protected enum Type {
    MAP,
    ARRAY,
    LEAF
  }

  final protected FieldSegment child;
  final protected Type type;

  protected int hash;

  protected FieldSegment(FieldSegment child) {
    this.child = child;
    if (child == null) {
      type = Type.LEAF;
    } else {
      type = child.isIndexed() ? Type.ARRAY : Type.MAP;
    }
  }

  /**
   * @return <code>true</code> if the current segment has a child and
   * <code>child.isNamed()</code> is <code>true</code>.
   */
  public boolean isMap() {
    return type == Type.MAP;
  }

  /**
   * @return <code>true</code> if the current segment has a child and
   * <code>child.isIndexed()</code> is <code>true</code>.
   */
  public boolean isArray() {
    return type == Type.ARRAY;
  }

  /**
   * @return <code>true</code> if the current segment has no child.
   */
  public boolean isLeaf() {
    return type == Type.LEAF;
  }

  /**
   * @return <code>true</code> if the current segment is identified by an index.
   */
  public abstract boolean isIndexed();

  /**
   * @return <code>true</code> if the current segment is identified by a name.
   */
  public abstract boolean isNamed();

  public abstract FieldSegment cloneWithNewChild(FieldSegment segment);

  public abstract int segmentCompareTo(FieldSegment o);

  @Override
  public abstract FieldSegment clone();

  public static final class IndexSegment extends FieldSegment {
    private final int index;

    public IndexSegment(int index) {
      this(index, null);
    }

    public IndexSegment(String numberAsText, FieldSegment child) {
      this(numberAsText == null ? -1 : Integer.parseInt(numberAsText), child);
    }

    public IndexSegment(FieldSegment child) {
      this(-1, child);
    }

    public IndexSegment(int index, FieldSegment child) {
      super(child);
      if (index < -1) {
        throw new IllegalArgumentException();
      }
      this.index = index;
    }

    public boolean hasIndex() {
      return index != -1;
    }

    public int getIndex() {
      return index;
    }

    @Override
    public boolean isIndexed() {
      return true;
    }

    @Override
    public boolean isNamed() {
      return false;
    }

    @Override
    public IndexSegment getIndexSegment() {
      return this;
    }

    @Override
    public int segmentHashCode() {
      return index;
    }

    @Override
    public int segmentCompareTo(FieldSegment other) {
      if (other instanceof IndexSegment) {
        IndexSegment that = (IndexSegment)other;
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
      } else if (obj instanceof IndexSegment) {
        return index == ((IndexSegment)obj).getIndex();
      }
      return false;
    }

    @Override
    public FieldSegment clone() {
      FieldSegment clonedChild = (child != null) ? child.clone() : null;
      return (index < 0) ? new IndexSegment(clonedChild) : new IndexSegment(index, clonedChild);
    }

    @Override
    public IndexSegment cloneWithNewChild(FieldSegment newChild) {
      FieldSegment clonedChild = (child != null) ? child.cloneWithNewChild(newChild) : newChild;
      return (index < 0) ? new IndexSegment(clonedChild) : new IndexSegment(index, clonedChild);
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
    private final boolean quoted;

    NameSegment(String n) {
      super(null);
      this.name = Fields.unquoteFieldName(n);
      this.quoted = (this.name != n);
    }

    public NameSegment(String n, FieldSegment child, boolean quoted) {
      super(child);
      this.name = n;
      this.quoted = quoted;
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean isIndexed() {
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
      return new NameSegment(this.name, (child != null ? child.clone() : null), this.quoted);
    }

    @Override
    public NameSegment cloneWithNewChild(FieldSegment newChild) {
      return new NameSegment(this.name,
          (child != null ? child.cloneWithNewChild(newChild) : newChild), this.quoted);
    }

    @Override
    protected StringBuilder writeSegment(StringBuilder sb, boolean escape) {
      if (escape || quoted) sb.append('`');
      sb.append(name);
      if (escape || quoted) sb.append('`');
      return sb;
    }

  }

  public NameSegment getNameSegment() {
    throw new UnsupportedOperationException();
  }

  public IndexSegment getIndexSegment() {
    throw new UnsupportedOperationException();
  }

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
   * [a] . contains([a.b.c]) returns true
   * [a.b.c] . contains([a]) returns true
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
    if (otherSeg.isIndexed() || this.isIndexed()) {
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
