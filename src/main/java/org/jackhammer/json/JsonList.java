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
package org.jackhammer.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jackhammer.FieldSegment;

public class JsonList extends JsonValue implements List<Object> {

  List <JsonValue> list;
  public JsonList() {
    valueType = Type.ARRAY;
    list = new ArrayList<JsonValue>();
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return list.contains(JsonValueBuilder.initFromObject(o));
  }

  @Override
  public Iterator<Object> iterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object[] toArray() {
    List <Object> objs = new ArrayList<Object>(list.size());
    // Make a copy from the JsonValue to native objects
    for (JsonValue kv : list) {
      objs.add(kv.getObject());
    }
    return objs.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    //TODO: implement this
    return a;
  }

  @Override
  public boolean add(Object e) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (list.contains(JsonValueBuilder.initFromObject(o)) == false) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends Object> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(int index, Collection<? extends Object> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object get(int index) {
    JsonValue kv = getJsonValueAt(index);
    if (kv == null) {
      return null;
    }
    return kv.getObject();
  }

  @Override
  public Object set(int index, Object element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(int index, Object element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object remove(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int indexOf(Object o) {
    return list.indexOf(JsonValueBuilder.initFromObject(o));
  }

  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(JsonValueBuilder.initFromObject(o));
  }

  @Override
  public ListIterator<Object> listIterator() {
    return new JsonListIterator();
  }

  @Override
  public ListIterator<Object> listIterator(int index) {
    return new JsonListIterator(index);
  }

  @Override
  public List<Object> subList(int fromIndex, int toIndex) {
    List <Object> l = new ArrayList<Object>();
    for (JsonValue v : list.subList(fromIndex, toIndex)) {
      l.add(v.getObject());
    }
    return l;
  }

  JsonValue createOrInsert(Iterator<FieldSegment> iter, JsonValue inJsonValue) {
    /* if the field is null then get the next field from the iterator */
    FieldSegment field;
    field = iter.next();

    int index = field.getIndexSegment().getIndex();
    JsonValue oldJsonValue = getJsonValueAt(index);
    /*
     * If this is the last element in the path then just
     * overwrite the previous value for the same key with
     * new value
     */
    if (field.isLastPath()) {
      if (index >= list.size()) {
        list.add(inJsonValue);
      }else {
        list.set(index, inJsonValue);
      }
      return this;
    }

    if (field.isMap()) {
      /*
       * if the new value for the same field is not
       * a map then delete the existing value and write new
       */
      JsonRecord newRecord;
      if ((oldJsonValue == null) || (oldJsonValue.getType() != Type.MAP)) {
        newRecord = new JsonRecord();
        newRecord.createOrInsert(iter, inJsonValue);
        if (index >= list.size()) {
          list.add(inJsonValue);
        }else {
          list.set(index, inJsonValue);
        }
        return this;
      }
      newRecord = (JsonRecord) oldJsonValue;
      return newRecord.createOrInsert(iter, inJsonValue);
    }

    /* next field is an array element - like a[3][5] */
    JsonList newList;
    if (oldJsonValue == null || ((oldJsonValue.getType() != Type.ARRAY))) {
      newList = new JsonList();
      newList.createOrInsert(iter, inJsonValue);
      if (index >= list.size()) {
        list.add(newList);
      }else {
        list.set(index, newList);
      }
      return this;
    }
    // Existing element is already an array so insert the element inside it
    newList = (JsonList) oldJsonValue;
    return newList.createOrInsert(iter, inJsonValue);
  }

  void addToList(JsonValue keyValue) {
    list.add(keyValue);
  }

  void delete(Iterator<FieldSegment> iter) {
    FieldSegment field = iter.next();
    if (field == null) return ;

    int index = field.getIndexSegment().getIndex();
    JsonValue kv = getJsonValueAt(index);

    // if value doesn't exist in array then return null
    if (kv == null) {
      return;
    }

    // if this is the last path then return the value at this key in map
    if (field.isLastPath()) {
      list.remove(kv.key);

    }

    if (field.isMap()) {
      if (kv.getType() != Type.MAP) {
        return;
      }
      ((JsonRecord)kv).delete(iter);
    }

    if (kv.getType() != Type.ARRAY) {
      return;
    }
    JsonList l = (JsonList) kv;
    l.delete(iter);

  }

  JsonValue getJsonValueAt(int index) {
    return list.size() <= index ? null : list.get(index);
  }

  JsonValue getJsonValueAt(Iterator<FieldSegment> iter) {
    FieldSegment field = iter.next();
    if (field == null) return null;

    int index = field.getIndexSegment().getIndex();

    JsonValue kv = getJsonValueAt(index);
    // if value doesn't exist in map then return null
    if (kv == null) {
      return null;
    }

    // if this is the last path then return the value at this index
    if (field.isLastPath()) {
      return kv;
    }

    if (field.isMap()) {
      if (kv.getType() != Type.MAP) {
        return null;
      }
      return ((JsonRecord)kv).getKeyValueAt(iter);
    }
    if (kv.getType() != Type.ARRAY) {
      return null;
    }
    return ((JsonList)kv).getJsonValueAt(iter);
  }

  class JsonListIterator implements ListIterator<Object> {
    ListIterator<JsonValue> iter;

    public JsonListIterator(int index) {
      iter = list.listIterator(index);
    }

    public JsonListIterator() {
      iter = list.listIterator();
    }
    @Override
    public boolean hasNext() {
      return iter.hasNext();
    }

    @Override
    public Object next() {
      JsonValue kv = iter.next();
      if (kv != null) {
        return kv.getObject();
      }
      return null;
    }

    @Override
    public boolean hasPrevious() {
      return iter.hasPrevious();
    }

    @Override
    public Object previous() {
      JsonValue kv = iter.previous();
      if (kv != null) {
        return kv.getObject();
      }
      return null;
    }

    @Override
    public int nextIndex() {
      return iter.nextIndex();
    }

    @Override
    public int previousIndex() {
      return iter.previousIndex();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void set(Object e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(Object e) {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  JsonList shallowCopy() {
    JsonList rec = new JsonList();
    rec.list = list;
    rec.objValue = objValue;
    rec.jsonValue = jsonValue;
    return rec;
  }

}
