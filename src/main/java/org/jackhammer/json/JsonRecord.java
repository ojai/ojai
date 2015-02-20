package org.jackhammer.json;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jackhammer.BaseRecord;
import org.jackhammer.FieldPath;
import org.jackhammer.Record;
import org.jackhammer.RecordReader;
import org.jackhammer.Value;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class JsonRecord extends BaseRecord {

  JsonRecord() {
  }

  @Override
  public Record set(FieldPath fieldPath, String value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, boolean value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, byte value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, short value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, int value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, long value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, float value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, double value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, Time value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, Date value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, DateTime value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, BigDecimal value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, byte[] value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, byte[] value, int off, int len) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, ByteBuffer value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, Interval value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, Map<String, Object> value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, Record value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, Value value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record set(FieldPath fieldPath, List<Object> value) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record setArray(FieldPath fieldPath, byte[] values) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record setArray(FieldPath fieldPath, short[] values) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record setArray(FieldPath fieldPath, int[] values) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record setArray(FieldPath fieldPath, long[] values) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record setArray(FieldPath fieldPath, float[] values) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record setArray(FieldPath fieldPath, double[] values) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record setArray(FieldPath fieldPath, String[] values) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record setArray(FieldPath fieldPath, Object... values) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record setNull(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Record delete(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getString(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean getBoolean(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public byte getByte(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public short getShort(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getInt(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long getLong(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getFloat(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getDouble(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Time getTime(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Date getDate(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DateTime getDateTime(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BigDecimal getDecimal(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ByteBuffer getBinary(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Interval getInterval(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Value getValue(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, Object> getMap(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Object> getList(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public RecordReader asReader() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public RecordReader asReader(FieldPath fieldPath) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterator<Entry<String, Value>> iterator() {
    // TODO Auto-generated method stub
    return null;
  }

}
