package org.jackhammer.json;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;

import org.jackhammer.Record;
import org.jackhammer.Record.Builder;
import org.jackhammer.Value;
import org.joda.time.DateTime;
import org.joda.time.Interval;


public class JsonRecordBuilder implements Record.Builder {
  
  private static final char NEWLINE = '\n';
  private static final char MAP_CLOSE = '}';

  private final StringBuilder sb_;
  private final boolean singleLine_;

  private boolean built_;
  
  enum State {
    IN_MAP,
    IN_ARRAY,
    BUILT
  }

  public JsonRecordBuilder(boolean singleLine) {
    sb_ = new StringBuilder();
    singleLine_ = singleLine;
    built_ = false;
  }

  @Override
  public Builder addNewMap() {
    checkState();
    return this;
  }

  private void checkState() {
    if (built_) {
      throw new IllegalStateException("Can not modify the record as it is already built");
    }
  }

  @Override
  public Builder put(String field, boolean value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, String value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, byte value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, short value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, int value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, long value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, float value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, double value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, Date value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, BigDecimal value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, byte[] value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, byte[] value, int off, int len) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, ByteBuffer value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, Interval value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putNewMap(String field) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putNewArray(String field) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putNull(String field) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, Value value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder put(String field, Record value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putDecimal(String field, int unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putDecimal(String field, long unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putDecimal(String field, double unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putDecimal(String field, float unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putDecimal(String field, byte[] unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putDate(String field, int days) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putTime(String field, int millis) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putDateTime(String field, long timeMillis) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder putInterval(int months, int days, int milliseconds) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addNewArray() {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(boolean value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(String value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(byte value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(short value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(int value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(long value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(float value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(double value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(Time value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(Date value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(DateTime value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(BigDecimal value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(byte[] value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(byte[] value, int off, int len) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(ByteBuffer value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(Interval value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addNull() {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(Value value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder add(Record value) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addDecimal(int unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addDecimal(long unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addDecimal(double unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addDecimal(float unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addDecimal(byte[] unscaledValue, int scale) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addDate(int days) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addTime(int millis) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addDateTime(long timeMillis) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder addInterval(int months, int days, int milliseconds) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder endArray() {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Builder endMap() {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public Record build() {
    if (!singleLine_) {
      sb_.append(NEWLINE);
    }
    sb_.append(MAP_CLOSE);
    built_ = true;
    return new JsonRecord(sb_);
  }
}
