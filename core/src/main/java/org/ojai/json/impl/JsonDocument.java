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
package org.ojai.json.impl;

import static org.ojai.DocumentConstants.ID_FIELD;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ojai.Document;
import org.ojai.DocumentReader;
import org.ojai.FieldPath;
import org.ojai.FieldSegment;
import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.beans.BeanCodec;
import org.ojai.exceptions.DecodingException;
import org.ojai.json.Json;
import org.ojai.json.JsonOptions;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.util.MapEncoder;

@API.Internal
public class JsonDocument extends JsonValue implements Document, Map<String, Object> {

  private final JsonStreamDocumentReader jsonDocumentReader;

  public JsonDocument() {
    this(null);
  }

  @Override
  public JsonDocument setId(Value value) {
    set(ID_FIELD, value);
    return this;
  }

  @Override
  public Value getId() {
    return getValue(ID_FIELD);
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  JsonDocument(JsonStreamDocumentReader reader) {
    jsonDocumentReader = reader;
    valueType = Type.MAP;
  }

  JsonDocument createOrInsert(Iterator<FieldSegment> iter, JsonValue newKeyValue) {
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
      JsonDocument newDocument;
      if ((oldKeyValue == null) || (oldKeyValue.getType() != Type.MAP)) {
        newDocument = new JsonDocument();
        newDocument.createOrInsert(iter, newKeyValue);
        newDocument.setKey(key);
        getRootMap().put(key, newDocument);
        return this;
      }

      // Inserting into an existing child of map type
      newDocument = (JsonDocument) oldKeyValue;
      return newDocument.createOrInsert(iter, newKeyValue);
    }

    JsonList newList;
    if ((oldKeyValue == null) || (oldKeyValue.getType() != Type.ARRAY)) {
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


  private JsonDocument setCommon(FieldPath fieldPath, JsonValue value) {
    Iterator<FieldSegment> iter = fieldPath.iterator();
    createOrInsert(iter, value);
    return this;
  }

  @Override
  public Document set(FieldPath fieldPath, String value) {
    return setCommon(fieldPath, new JsonValue(value, Type.STRING));
  }

  @Override
  public Document set(FieldPath fieldPath, boolean value) {
    return setCommon(fieldPath, new JsonValue(value, Type.BOOLEAN));
  }

  @Override
  public Document set(FieldPath fieldPath, byte value) {
    return setCommon(fieldPath, new JsonValue(value, Type.BYTE));
  }

  @Override
  public Document set(FieldPath fieldPath, short value) {

    return setCommon(fieldPath, new JsonValue(value, Type.SHORT));
  }

  @Override
  public Document set(FieldPath fieldPath, int value) {

    return setCommon(fieldPath, new JsonValue(value, Type.INT));
  }

  @Override
  public Document set(FieldPath fieldPath, long value) {

    return setCommon(fieldPath, new JsonValue(value, Type.LONG));
  }

  @Override
  public Document set(FieldPath fieldPath, float value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(FieldPath fieldPath, double value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(FieldPath fieldPath, BigDecimal value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(String fieldPath, OTimestamp value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(FieldPath fieldPath, OTimestamp value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(String fieldPath, OInterval value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(FieldPath fieldPath, OInterval value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(FieldPath fieldPath, OTime value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(FieldPath fieldPath, ODate value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonDocument set(String field, Document value) {
    return setCommon(FieldPath.parseFrom(field), JsonValueBuilder.initFrom(value));
  }
  @Override
  public JsonDocument set(FieldPath field, Document value) {
    return setCommon(field, JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonDocument set(String field, Value value) {
    return setCommon(FieldPath.parseFrom(field), JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonDocument set(FieldPath field, Value value) {
    return setCommon(field, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document setNull(FieldPath fieldPath) {
    return setCommon(fieldPath, JsonValue.NULLKEYVALUE);
  }

  /*
   * deletes an element from the document. it returns the change in size
   */
  Document delete(Iterator<FieldSegment> iter) {
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
      return ((JsonDocument)kv).delete(iter);
    }

    if (kv.getType() != Type.ARRAY) {
      return null;
    }
    ((JsonList)kv).delete(iter);
    return this;
  }

  @Override
  public Document delete(String field) {
    delete(FieldPath.parseFrom(field));
    return this;
  }

  @Override
  public Document delete(FieldPath path) {
    delete(path.iterator());
    return this;
  }

  /* iterator over the Document object */
  class JsonDocumentIterator implements Iterator<java.util.Map.Entry<String, Value>> {

    Iterator<String> keyIter;
    JsonDocumentIterator() {
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
      return ((JsonDocument)kv).getKeyValueAt(iter);
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
  public JsonDocument empty() {
    getRootMap().clear();
    return this;
  }

  @Override
  public DocumentReader asReader() {
    if (jsonDocumentReader == null) {
      return new JsonDOMDocumentReader(this);
    }
    return jsonDocumentReader;
  }

  @Override
  public DocumentReader asReader(FieldPath fieldPath) {
    JsonValue val = getValue(fieldPath);
    return new JsonDOMDocumentReader(val);
  }

  @Override
  public DocumentReader asReader(String fieldPath) {
    return asReader(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public Iterator<java.util.Map.Entry<String, Value>> iterator() {
    return new JsonDocumentIterator();
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
  public Document set(String fieldPath, String value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(String fieldPath, boolean value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(String fieldPath, byte value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(String fieldPath, short value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(String fieldPath, int value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(String fieldPath, long value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(String fieldPath, float value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(String fieldPath, double value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(String fieldPath, BigDecimal value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(String fieldPath, OTime value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(String fieldPath, ODate value) {
    return setCommon(FieldPath.parseFrom(fieldPath), JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonDocument set(String field, byte[] value) {
    return setCommon(FieldPath.parseFrom(field),
        JsonValueBuilder.initFrom(ByteBuffer.wrap(value)));
  }

  @Override
  public JsonDocument set(FieldPath field, byte[] value) {
    return setCommon(field, JsonValueBuilder.initFrom(ByteBuffer.wrap(value)));
  }

  @Override
  public JsonDocument set(String field, byte[] value, int off, int len) {
    return setCommon(FieldPath.parseFrom(field),
        JsonValueBuilder.initFrom(ByteBuffer.wrap(value, off, len)));
  }

  @Override
  public JsonDocument set(FieldPath field, byte[] value, int off, int len) {
    return setCommon(field, JsonValueBuilder.initFrom(ByteBuffer.wrap(value, off, len)));
  }

  @Override
  public JsonDocument set(String field, ByteBuffer value) {
    return setCommon(FieldPath.parseFrom(field), JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonDocument set(FieldPath field, ByteBuffer value) {
    return setCommon(field, JsonValueBuilder.initFrom(value));
  }

  private Document setCommonFromObjectArray(FieldPath field, Object[] values) {
    return setCommon(field, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Document setArray(String fieldPath, byte[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Document setArray(FieldPath fieldPath, byte[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Document setArray(String fieldPath, short[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Document setArray(FieldPath fieldPath, short[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Document setArray(String fieldPath, int[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Document setArray(FieldPath fieldPath, int[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Document setArray(String fieldPath, long[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Document setArray(FieldPath fieldPath, long[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Document setArray(String fieldPath, float[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Document setArray(FieldPath fieldPath, float[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Document setArray(String fieldPath, double[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Document setArray(FieldPath fieldPath, double[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public Document setArray(String fieldPath, String[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Document setArray(FieldPath fieldPath, String[] values) {
    return setCommonFromObjectArray(fieldPath, values);
  }

  @Override
  public Document setArray(String fieldPath, Object... values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Document setArray(FieldPath fieldPath, Object... values) {
    return setCommonFromObjectArray(fieldPath, values);
  }

  @Override
  public Document setNull(String fieldPath) {
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
  public OTimestamp getTimestamp(String fieldPath) {
    return getTimestamp(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public OTimestamp getTimestamp(FieldPath fieldPath) {
    JsonValue v = getKeyValueAt(fieldPath.iterator());
    if (v != null) {
      return v.getTimestamp();
    }
    return null;
  }

  @Override
  public OInterval getInterval(String fieldPath) {
    return getInterval(FieldPath.parseFrom(fieldPath));
  }

  @Override
  public OInterval getInterval(FieldPath fieldPath) {
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
  public OTime getTime(String field) {
    return getTime(FieldPath.parseFrom(field));
  }

  @Override
  public OTime getTime(FieldPath field) {
    JsonValue v = getKeyValueAt(field.iterator());
    if (v != null) {
      return v.getTime();
    }
    return null;
  }

  @Override
  public ODate getDate(String field) {
    return getDate(FieldPath.parseFrom(field));
  }

  @Override
  public ODate getDate(FieldPath field) {
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
  public Document set(String fieldPath, Map<String, ? extends Object> value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(FieldPath fieldPath, Map<String, ? extends Object> value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public Document set(String fieldPath, List<? extends Object> value) {
    return set(FieldPath.parseFrom(fieldPath), value);
  }

  @Override
  public Document set(FieldPath fieldPath, List<? extends Object> value) {
    return setCommon(fieldPath, JsonValueBuilder.initFrom(value));
  }

  @Override
  public JsonDocument shallowCopy() {
    JsonDocument rec = new JsonDocument();
    rec.objValue = objValue;
    rec.jsonValue = jsonValue;
    return rec;
  }

  @SuppressWarnings("unchecked")
  Map<String, JsonValue> getRootMap() {
    if (objValue == null) {
      objValue = new LinkedHashMap<String, JsonValue>();
    }
    return (Map<String, JsonValue>)objValue;
  }

  @Override
  public Document setArray(String fieldPath, boolean[] values) {
    return setArray(FieldPath.parseFrom(fieldPath), values);
  }

  @Override
  public Document setArray(FieldPath fieldPath, boolean[] values) {
    return setCommon(fieldPath, JsonValueBuilder.initFromArray(values));
  }

  @Override
  public String toString() {
    return asJsonString();
  }

  @Override
  public String asJsonString() {
    return asJsonString(JsonOptions.DEFAULT);
  }

  @Override
  public String asJsonString(JsonOptions options) {
    return Json.toJsonString(this, options);
  }

  @Override
  public <T> T toJavaBean(Class<T> beanClass) throws DecodingException {
    return BeanCodec.encode(asReader(), beanClass);
  }

  @Override
  public Map<String, Object> asMap() throws DecodingException {
    return MapEncoder.encode(asReader());
  }
}
