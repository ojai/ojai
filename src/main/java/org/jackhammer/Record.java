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

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public interface Record extends Iterable<Map.Entry<String, Value>> {

  // set(String name, <Type> value)
  // setAt(String path, <Type> value)
  // setAt(FieldPath path, <Type> value)
  // get<Type>(String name)
  // get<Type>At(String path)
  // get<Type>At(FieldPath path)
  // delete(String name)
  // deleteAt(String path)
  // deleteAt(FieldPath path)

  /** no at **/
  Record set(String name, String value);
  Record set(String name, boolean value);
  Record set(String name, byte value);
  Record set(String name, short value);
  Record set(String name, int value);
  Record set(String name, long value);
  Record set(String name, float value);
  Record set(String name, double value);
  Record set(String name, Time value);
  Record set(String name, Date value);
  Record set(String name, DateTime value);
  Record set(String name, BigDecimal value);
  Record set(String name, byte[] value);
  Record set(String name, byte[] value,int off,int len);
  Record set(String name, ByteBuffer value);
  Record set(String name, Interval value);

  Record set(String name, Map<String, Object> value);
  Record set(String name, Record value);
  Record set(String name, Value value);
  Record set(String name, Object... value);
  Record set(String name, List<Object> value);

  /** at(String path) **/
  Record setAt(String path, String value);
  Record setAt(String path, boolean value);
  Record setAt(String path, byte value);
  Record setAt(String path, short value);
  Record setAt(String path, int value);
  Record setAt(String path, long value);
  Record setAt(String path, float value);
  Record setAt(String path, double value);
  Record setAt(String path, Time value);
  Record setAt(String path, Date value);
  Record setAt(String path, DateTime value);
  Record setAt(String path, BigDecimal value);
  Record setAt(String path, byte[] value);
  Record setAt(String path, byte[] value,int off,int len);
  Record setAt(String path, ByteBuffer value);
  Record setAt(String path, Interval value);

  Record setAt(String path, Map<String, Object> value);
  Record setAt(String path, Record value);
  Record setAt(String path, Value value);
  Record setAt(String path, Object... value);
  Record setAt(String path, List<Object> value);

  /** at(FieldPath path) **/
  Record setAt(FieldPath path, String value);
  Record setAt(FieldPath path, boolean value);
  Record setAt(FieldPath path, byte value);
  Record setAt(FieldPath path, short value);
  Record setAt(FieldPath path, int value);
  Record setAt(FieldPath path, long value);
  Record setAt(FieldPath path, float value);
  Record setAt(FieldPath path, double value);
  Record setAt(FieldPath path, Time value);
  Record setAt(FieldPath path, Date value);
  Record setAt(FieldPath path, DateTime value);
  Record setAt(FieldPath path, BigDecimal value);
  Record setAt(FieldPath path, byte[] value);
  Record setAt(FieldPath path, byte[] value,int off,int len);
  Record setAt(FieldPath path, ByteBuffer value);
  Record setAt(FieldPath path, Interval value);

  Record setAt(FieldPath path, Map<String, Object> value);
  Record setAt(FieldPath path, Record value);
  Record setAt(FieldPath path, Value value);
  Record setAt(FieldPath path, Object... value);
  Record setAt(FieldPath path, List<Object> value);

  /** no at **/
  String getString(String name);
  boolean getBoolean(String name);
  byte getByte(String name);
  short getShort(String name);
  int getInt(String name);
  long getLong(String name);
  float getFloat(String name);
  double getDouble(String name);
  Time getTime(String name);
  Date getDate(String name);
  DateTime getDateTime(String name);
  BigDecimal getDecimal(String name);
  ByteBuffer getBinary(String name);
  Interval getInterval(String name);
  void delete(String field);
  Value getValue(String name);

  /** at(String path) **/
  String getStringAt(String path);
  boolean getBooleanAt(String path);
  byte getByteAt(String path);
  short getShortAt(String path);
  int getIntAt(String path);
  long getLongAt(String path);
  float getFloatAt(String path);
  double getDoubleAt(String path);
  Time getTimeAt(String path);
  Date getDateAt(String path);
  DateTime getDateTimeAt(String path);
  BigDecimal getDecimalAt(String path);
  ByteBuffer getBinaryAt(String path);
  Interval getIntervalAt(String path);

  void deleteAt(String path);
  Value getValueAt(String path);

  /** at(FieldPath path) **/
  String getStringAt(FieldPath path);
  boolean getBooleanAt(FieldPath path);
  byte getByteAt(FieldPath path);
  short getShortAt(FieldPath path);
  int getIntAt(FieldPath path);
  long getLongAt(FieldPath path);
  float getFloatAt(FieldPath path);
  double getDoubleAt(FieldPath path);
  Time getTimeAt(FieldPath path);
  Date getDateAt(FieldPath path);
  DateTime getDateTimeAt(FieldPath path);
  BigDecimal getDecimalAt(FieldPath path);
  ByteBuffer getBinaryAt(FieldPath path);
  Interval getIntervalAt(FieldPath path);

  void deleteAt(FieldPath path);
  Value getValueAt(FieldPath path);

  /** Stream **/
  StreamReader asStream();
  StreamReader asStream(String name);
  StreamReader asStreamAt(String path);
  StreamReader asStreamAt(FieldPath path);

  public interface Builder {

    Builder addNewMap();

    /* Map Methods */
    Builder put(String field, boolean value);
    Builder put(String field, String value);
    Builder put(String field, byte value);
    Builder put(String field, short value);
    Builder put(String field, int value);
    Builder put(String field, long value);
    Builder put(String field, float value);
    Builder put(String field, double value);
    Builder put(String field, Date value);
    Builder put(String field, BigDecimal value);
    Builder put(String field, byte[] value);
    Builder put(String field, byte[] value, int off, int len);
    Builder put(String field, ByteBuffer value);
    Builder put(String field, Interval value);
    Builder putNewMap(String field);
    Builder putNewArray(String field);
    // questionable whether we should have this
    Builder putNull(String field);

    Builder put(String field, Value value);
    Builder put(String field, Record value);

    /* Advanced Map Methods */
    Builder putDecimal(String field, int unscaledValue, int scale);
    Builder putDecimal(String field, long unscaledValue, int scale);
    Builder putDecimal(String field, double unscaledValue, int scale);
    Builder putDecimal(String field, float unscaledValue, int scale);
    Builder putDecimal(String field, byte[] unscaledValue, int scale);
    Builder putDate(String field, int days);
    Builder putTime(String field, int millis);
    Builder putDateTime(String field, long timeMillis);
    Builder putInterval(int months, int days, int milliseconds);


    Builder addNewArray();

    /* Array Methods */
    Builder add( boolean value);
    Builder add( String value);
    Builder add( byte value);
    Builder add( short value);
    Builder add( int value);
    Builder add( long value);
    Builder add( float value);
    Builder add( double value);
    Builder add( Time value);
    Builder add( Date value);
    Builder add( DateTime value);
    Builder add( BigDecimal value);
    Builder add( byte[] value);
    Builder add( byte[] value, int off, int len);
    Builder add( ByteBuffer value);
    Builder add( Interval value);
    Builder addNull();

    Builder add(Value value);
    Builder add(Record value);

    /* Advanced Array Methods */
    Builder addDecimal( int unscaledValue, int scale);
    Builder addDecimal( long unscaledValue, int scale);
    Builder addDecimal( double unscaledValue, int scale);
    Builder addDecimal( float unscaledValue, int scale);
    Builder addDecimal( byte[] unscaledValue, int scale);
    Builder addDate( int days);
    Builder addTime( int millis);
    Builder addDateTime( long timeMillis);
    Builder addInterval(int months, int days, int milliseconds);


    /* Lifecycle methods */
    Builder endArray();
    Builder endMap();
    Record build();
  }

}
