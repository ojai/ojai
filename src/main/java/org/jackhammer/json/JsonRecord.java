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
package org.jackhammer.json;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jackhammer.FieldPath;
import org.jackhammer.FieldSegment;
import org.jackhammer.Record;
import org.jackhammer.RecordReader;
import org.jackhammer.Value;
import org.jackhammer.types.Interval;

public class JsonRecord extends JsonValue implements Record, Map<String, Object> {

  private LinkedHashMap<String, JsonValue> map;
  private JsonRecordReader jsonRecordReader;

  public JsonRecord() {
    this(null);
  }

  JsonRecord(JsonRecordReader reader) {
    jsonRecordReader = reader;
    valueType = Type.MAP;
  }

  JsonRecord createOrInsert(Iterator<FieldSegment> iter, JsonValue newKeyValue) {
    FieldSegment field;
    field = iter.next();
    String key = field.getNameSegment().getName();
    JsonValue oldKeyValue = getRootMap().get(key);
    /*
     * If this is the last element in the path then just
     * overwrite the previous value for the same key with
     * new value
     */
    if (field.isLastPath()) {
      newKeyValue.setKey(key);
      getRootMap().put(key, newKeyValue);
      return this;
    }

    if (field.isMap()) {
      /*
       * if the new value for the same field is not
       * a map then delete the existing value and write new
       */
      JsonRecord newRecord;
      if ((oldKeyValue == null) || (oldKeyValue.getType() != Type.MAP)) {
        newRecord = new JsonRecord();
        newRecord.createOrInsert(iter, newKeyValue);
        getRootMap().put(key, newRecord);
        return this;
      }

      // Inserting into an existing child of map type
      newRecord = (JsonRecord) oldKeyValue;
      return newRecord.createOrInsert(iter, newKeyValue);
    }

    JsonList newList;
    if ((oldKeyValue == null) || (oldKeyValue.getType() != Type.ARRAY)) {
      System.out.println("New array to be introduced");
      newList = new JsonList();
      newList.createOrInsert(iter, newKeyValue);
      newList.setKey(key);
      getRootMap().put(key, newList);
      return this;
    }

    newList = (JsonList)oldKeyValue;
    newList.createOrInsert(iter, newKeyValue);
    return this;

  }

  private JsonRecord setCommon(FieldPath fieldPath, JsonValue value) {
    Iterator<FieldSegment> iter = fieldPath.iterator();
    createOrInsert(iter, value);
    return this;
  }

  @Override
  public Record set(FieldPath fieldPath, String value) {
    return setCommon(fieldPath, new JsonValue(value, Type.STRING));
  }

  @Override
  public Record set(FieldPath fieldPath, boolean value) {
    return setCommon(fieldPath, new JsonValue(value, Type.BOOLEAN));
  }

  @Override
  public Record set(FieldPath fieldPath, byte value) {
    return setCommon(fieldPath, new JsonValue(value, Type.BYTE));
  }

  @Override
  public Record set(FieldPath fieldPath, short value) {

    return setCommon(fieldPath, new JsonValue(value, Type.SHORT));
  }

  @Override
  public Record set(FieldPath fieldPath, int value) {

    return setCommon(fieldPath, new JsonValue(value, Type.INT));
  }

  @Override
  public Record set(FieldPath fieldPath, long value) {

    return setCommon(fieldPath, new JsonValue(value, Type.LONG));
  }

  @Override
  public Record set(FieldPath fieldPath, float value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(FieldPath fieldPath, double value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(FieldPath fieldPath, BigDecimal value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(String fieldPath, Timestamp value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(FieldPath fieldPath, Timestamp value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(String fieldPath, Interval value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(FieldPath fieldPath, Interval value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(FieldPath fieldPath, Time value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(FieldPath fieldPath, Date value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonRecord set(String field, Record value) {
    return setCommon(FieldPath.parseFrom(field), JsonValueBuilder.initFrom(value));
  }
  @Override
  public JsonRecord set(FieldPath field, Record value) {
    return setCommon(field, JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonRecord set(String field, Value value) {
    return setCommon(FieldPath.parseFrom(field), JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonRecord set(FieldPath field, Value value) {
    return setCommon(field, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record setNull(FieldPath fieldPath) {
    return setCommon(fieldPath, JsonValue.NULLKEYVALUE);
  }

  /*
   * deletes an element from the record. it returns the change in size
   */
 Record delete(Iterator<FieldSegment> iter) {
    FieldSegment field = iter.next();
    if (field == null) return null;

    String key = field.getNameSegment().getName();
    JsonValue kv = getRootMap().get(key);
    // if value doesn't exist in map then return null
    if (kv == null) {
      return null;
    }

    // if this is the last path then return the value at this key in map
    if (field.isLastPath()) {
      getRootMap().remove(kv.key);
      return null;
    }

    // this is intermediate path so based on the type return the hierarchical value
    if (field.isMap()) {
      if (kv.getType() != Type.MAP) {
        return null;
      }
      return ((JsonRecord)kv).delete(iter);
    }

    if (kv.getType() != Type.ARRAY) {
      return null;
    }
    ((JsonList)kv).delete(iter);
    return this;
  }

  @Override
  public Record delete(String field) {
    delete(FieldPath.parseFrom(field));
    return this;
  }

  @Override
  public Record delete(FieldPath path) {
    delete(path.iterator());
    return this;
  }

  /* iterator over the Record object */
  class JsonRecordIterator implements Iterator<java.util.Map.Entry<String, Value>> {

    Iterator<String> keyIter;
    JsonRecordIterator() {
      keyIter = getRootMap().keySet().iterator();
    }
    @Override
    public boolean hasNext() {
      return keyIter.hasNext();
    }

    @Override
    public java.util.Map.Entry<String, Value> next() {
      String key = keyIter.next();
      JsonValue kv = getRootMap().get(key);
      return new AbstractMap.SimpleImmutableEntry<String, Value>(key, kv);
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  public JsonValue getKeyValueAt(Iterator<FieldSegment> iter) {
    FieldSegment field = iter.next();
    if (field == null) return null;

    String key = field.getNameSegment().getName();
    JsonValue kv = getRootMap().get(key);
    if (kv == null) {
      return null;
    }

    // if this is the last path then return the value at this key in map
    if (field.isLastPath()) {
      return kv;
    }

    // this is intermediate path so based on the type return the hierarchical value
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

  @Override
  public String getString(FieldPath fieldPath) {
    JsonValue value = getKeyValueAt(fieldPath.iterator());
    if (value != null) {
      return value.getString();
    }
    return null;
  }

  @Override
  public boolean getBoolean(FieldPath fieldPath) {
    JsonValue value = getKeyValueAt(fieldPath.iterator());
    if (value != null) {
      return value.getBoolean();
    }
    return false;
  }

  @Override
  public Boolean getBooleanObj(FieldPath fieldPath) {
    return new Boolean(getBoolean(fieldPath));
  }

  @Override
  public byte getByte(FieldPath fieldPath) {
    JsonValue value = getKeyValueAt(fieldPath.iterator());
    if (value != null) {
      return value.getByte();
    }
    return 0;
  }

  @Override
  public Byte getByteObj(FieldPath fieldPath) {
    return new Byte(getByte(fieldPath));
  }

  @Override
  public short getShort(FieldPath fieldPath) {
    JsonValue value = getKeyValueAt(fieldPath.iterator());
    if (value != null) {
      return value.getShort();
    }
    return 0;
  }

  @Override
  public Short getShortObj(FieldPath fieldPath) {
    return new Short(getShort(fieldPath));
  }

  @Override
  public int getInt(FieldPath fieldPath) {
    JsonValue value = getKeyValueAt(fieldPath.iterator());
    if (value != null) {
      return value.getInt();
    }
    return 0;
  }

  @Override
  public Integer getIntObj(FieldPath fieldPath) {
    return new Integer(getInt(fieldPath));
  }

  @Override
  public long getLong(FieldPath fieldPath) {
    JsonValue value = getKeyValueAt(fieldPath.iterator());
    if (value != null) {
      return value.getLong();
    }
    return 0;
  }

  @Override
  public Long getLongObj(FieldPath fieldPath) {
    return new Long(getLong(fieldPath));
  }

  @Override
  public float getFloat(FieldPath fieldPath) {
    JsonValue value = getKeyValueAt(fieldPath.iterator());
    if (value != null) {
      return value.getFloat();
    }
    return 0;
  }

  @Override
  public Float getFloatObj(FieldPath fieldPath) {
    return new Float(getFloat(fieldPath));
  }

  @Override
  public double getDouble(FieldPath fieldPath) {
    JsonValue value = getKeyValueAt(fieldPath.iterator());
    if (value != null) {
      return value.getDouble();
    }
    return 0;
  }

  @Override
  public Double getDoubleObj(FieldPath fieldPath) {
    return new Double(getDouble(fieldPath));
  }

  @Override
  public BigDecimal getDecimal(FieldPath fieldPath) {
   JsonValue v = getKeyValueAt(fieldPath.iterator());
   if (v != null) {
     return v.getDecimal();
   }
   return null;
  }

  @Override
  public JsonValue getValue(FieldPath fieldPath) {
    return getKeyValueAt(fieldPath.iterator());
  }

  @Override
  public Map<String, Object> getMap(FieldPath fieldPath) {
    JsonValue v = getKeyValueAt(fieldPath.iterator());
    if (v != null) {
      return v.getMap();
    }
    return null;
  }

  @Override
  public List<Object> getList(String fieldPath) {
    return getList(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public List<Object> getList(FieldPath fieldPath) {
    JsonValue v = getKeyValueAt(fieldPath.iterator());
    if (v != null) {
      return v.getList();
    }
    return null;
  }

  @Override
  public RecordReader asReader() {
    if (jsonRecordReader == null) {
      // TODO build a RecordReader over the DOM map
    } else if (map != null) { // don't call getRootMap() 
      // TODO build a merged RecordReader with DOM
      // overlaying on top of the jsonRecordReader
    }
    return jsonRecordReader;
  }

  @Override
  public RecordReader asReader(FieldPath fieldPath) {
    /* TODO Here, we need to create a serialized version of the
     * DOM tree model which can be parsed as next token and put
     * it into a buffer.
     * @see org.jackhammer.Record#asReader(org.jackhammer.FieldPath)
     */
    return null;
  }

  @Override
  public RecordReader asReader(String fieldPath) {
    return asReader(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Iterator<java.util.Map.Entry<String, Value>> iterator() {
    return new JsonRecordIterator();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean containsKey(Object key) {
    return getRootMap().containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    JsonValue v = JsonValueBuilder.initFromObject(value);
    return getRootMap().containsValue(v);
  }

  @Override
  public Set<java.util.Map.Entry<String, Object>> entrySet() {
    /* make a copy of the string and the real object and return that */
    LinkedHashSet<Map.Entry<String, Object>> s = new LinkedHashSet<Map.Entry<String,Object>>();
    for (String k : getRootMap().keySet()) {

      Map.Entry<String, Object> newEntry =
          new AbstractMap.SimpleImmutableEntry<String, Object>(k, getRootMap().get(k).getObject());
      s.add(newEntry);
    }
    return s;
  }

  @Override
  public Object get(Object key) {
    JsonValue value = getRootMap().get(key);
    if (value != null) {
      return value.getObject();
    }
    return null;
  }

  @Override
  public boolean isEmpty() {
    return getRootMap().isEmpty();
  }

  @Override
  public Set<String> keySet() {
    return getRootMap().keySet();
  }

  @Override
  public Object put(String arg0, Object arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void putAll(Map<? extends String, ? extends Object> arg0) {
    throw new UnsupportedOperationException();

  }

  @Override
  public Object remove(Object arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size() {
    return getRootMap().size();
  }

  @Override
  public Collection<Object> values() {
    /* make a copy and return the real value object */
    ArrayList<Object> list = new ArrayList<Object>();
    for (JsonValue v : getRootMap().values()) {
      list.add(v.getObject());
    }
    return list;
  }

  @Override
  public Record set(String fieldPath, String value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(String fieldPath, boolean value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(String fieldPath, byte value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(String fieldPath, short value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(String fieldPath, int value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(String fieldPath, long value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(String fieldPath, float value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(String fieldPath, double value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(String fieldPath, BigDecimal value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(String fieldPath, Time value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(String fieldPath, Date value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonRecord set(String field, byte[] value) {
    return setCommon(FieldPath.parseFrom(field),
                     JsonValueBuilder.initFrom(ByteBuffer.wrap(value)));
  }

  @Override
  public JsonRecord set(FieldPath field, byte[] value) {
    return setCommon(field, JsonValueBuilder.initFrom(ByteBuffer.wrap(value)));
  }

  @Override
  public JsonRecord set(String field, byte[] value, int off, int len) {
    return setCommon(FieldPath.parseFrom(field),
                     JsonValueBuilder.initFrom(ByteBuffer.wrap(value, off, len)));
  }

  @Override
  public JsonRecord set(FieldPath field, byte[] value, int off, int len) {
    return setCommon(field, JsonValueBuilder.initFrom(ByteBuffer.wrap(value, off, len)));
  }

  @Override
  public JsonRecord set(String field, ByteBuffer value) {
    return setCommon(FieldPath.parseFrom(field), JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonRecord set(FieldPath field, ByteBuffer value) {
    return setCommon(field, JsonValueBuilder.initFrom(value));
  }

  private Record setCommonFromObjectArray(FieldPath field, Object[] values) {
    return setCommon(field, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Record setArray(String fieldPath, byte[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Record setArray(FieldPath fieldPath, byte[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Record setArray(String fieldPath, short[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Record setArray(FieldPath fieldPath, short[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Record setArray(String fieldPath, int[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Record setArray(FieldPath fieldPath, int[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Record setArray(String fieldPath, long[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Record setArray(FieldPath fieldPath, long[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Record setArray(String fieldPath, float[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Record setArray(FieldPath fieldPath, float[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Record setArray(String fieldPath, double[] values) {
    return setArray(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Record setArray(FieldPath fieldPath, double[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
   }

  @Override
  public Record setArray(String fieldPath, String[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Record setArray(FieldPath fieldPath, String[] values) {
    return setCommonFromObjectArray(fieldPath, values);
  }

  @Override
  public Record setArray(String fieldPath, Object... values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Record setArray(FieldPath fieldPath, Object... values) {
    return setCommonFromObjectArray(fieldPath, values);
  }

  @Override
  public Record setNull(String fieldPath) {
    return setNull(FieldPath.parseFrom(fieldPath));
  }


  @Override
  public String getString(String fieldPath) {
    return getString(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public boolean getBoolean(String fieldPath) {
    return getBoolean(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Boolean getBooleanObj(String fieldPath) {
    return new Boolean(getBoolean(fieldPath));
  }

  @Override
  public byte getByte(String fieldPath) {
    return getByte(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Byte getByteObj(String fieldPath) {
    return new Byte(getByte(fieldPath));
  }

  @Override
  public short getShort(String fieldPath) {
    return getShort(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Short getShortObj(String fieldPath) {
    return new Short(getShort(fieldPath));
  }

  @Override
  public int getInt(String fieldPath) {
    return getInt(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Integer getIntObj(String fieldPath) {
    return new Integer(getInt(fieldPath));
  }

  @Override
  public long getLong(String fieldPath) {
    return getLong(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Long getLongObj(String fieldPath) {
    return new Long(getLong(fieldPath));
  }

  @Override
  public float getFloat(String fieldPath) {
    return getFloat(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Float getFloatObj(String fieldPath) {
    return new Float(getFloat(fieldPath));
  }

  @Override
  public double getDouble(String fieldPath) {
    return getDouble(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Double getDoubleObj(String fieldPath) {
    return new Double(getDouble(fieldPath));
  }

  @Override
  public BigDecimal getDecimal(String fieldPath) {
    return getDecimal(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public ByteBuffer getBinary(String field) {
    return getBinary(FieldPath.parseFrom(field));
  }

  @Override
  public ByteBuffer getBinary(FieldPath field) {
    JsonValue v = getKeyValueAt(field.iterator());
    if (v != null) {
      return v.getBinary();
    }
    return null;
  }

  @Override
  public Timestamp getTimestamp(String fieldPath) {
    return getTimestamp(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Timestamp getTimestamp(FieldPath fieldPath) {
    JsonValue v = getKeyValueAt(fieldPath.iterator());
    if (v != null) {
      return v.getTimestamp();
    }
    return null;
  }

  @Override
  public Interval getInterval(String fieldPath) {
    return getInterval(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Interval getInterval(FieldPath fieldPath) {
    JsonValue v = getKeyValueAt(fieldPath.iterator());
    if (v != null) {
      return v.getInterval();
    }
    return null;
  }

  @Override
  public Map<String, Object> getMap(String fieldPath) {
    return getMap(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Time getTime(String field) {
    return getTime(FieldPath.parseFrom(field));
  }

  @Override
  public Time getTime(FieldPath field) {
    JsonValue v = getKeyValueAt(field.iterator());
    if (v != null) {
      return v.getTime();
    }
    return null;
  }

  @Override
  public Date getDate(String field) {
    return getDate(FieldPath.parseFrom(field));
  }

  @Override
  public Date getDate(FieldPath field) {
    JsonValue v = getKeyValueAt(field.iterator());
    if (v != null) {
      return v.getDate();
    }
    return null;
  }

  @Override
  public Value getValue(String fieldPath) {
    return getValue(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Record set(String fieldPath, Map<String, ? extends Object> value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(FieldPath fieldPath, Map<String, ? extends Object> value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Record set(String fieldPath, List<? extends Object> value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Record set(FieldPath fieldPath, List<? extends Object> value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonRecord shallowCopy() {
    JsonRecord rec = new JsonRecord();
    rec.map = map;
    rec.objValue = objValue;
    rec.jsonValue = jsonValue;
    return rec;
  }

  private Map<String, JsonValue> getRootMap() {
    if (map == null) {
      map = new LinkedHashMap<String, JsonValue>();
    }
    return map;
  }

}
