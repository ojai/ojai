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
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.jackhammer.types.Interval;

public interface Record extends Iterable<Map.Entry<String, Value>> {

  Record set(String fieldPath, String value);
  Record set(FieldPath fieldPath, String value);

  Record set(String fieldPath, boolean value);
  Record set(FieldPath fieldPath, boolean value);

  Record set(String fieldPath, byte value);
  Record set(FieldPath fieldPath, byte value);

  Record set(String fieldPath, short value);
  Record set(FieldPath fieldPath, short value);

  Record set(String fieldPath, int value);
  Record set(FieldPath fieldPath, int value);

  Record set(String fieldPath, long value);
  Record set(FieldPath fieldPath, long value);

  Record set(String fieldPath, float value);
  Record set(FieldPath fieldPath, float value);

  Record set(String fieldPath, double value);
  Record set(FieldPath fieldPath, double value);

  Record set(String fieldPath, Time value);
  Record set(FieldPath fieldPath, Time value);

  Record set(String fieldPath, Date value);
  Record set(FieldPath fieldPath, Date value);

  Record set(String fieldPath, Timestamp value);
  Record set(FieldPath fieldPath, Timestamp value);

  Record set(String fieldPath, BigDecimal value);
  Record set(FieldPath fieldPath, BigDecimal value);

  Record set(String fieldPath, byte[] value);
  Record set(FieldPath fieldPath, byte[] value);

  Record set(String fieldPath, byte[] value, int off, int len);
  Record set(FieldPath fieldPath, byte[] value, int off, int len);

  Record set(String fieldPath, ByteBuffer value);
  Record set(FieldPath fieldPath, ByteBuffer value);

  Record set(String fieldPath, Interval value);
  Record set(FieldPath fieldPath, Interval value);

  Record set(String fieldPath, Map<String, Object> value);
  Record set(FieldPath fieldPath, Map<String, Object> value);

  Record set(String fieldPath, Record value);
  Record set(FieldPath fieldPath, Record value);

  Record set(String fieldPath, Value value);
  Record set(FieldPath fieldPath, Value value);

  Record set(String fieldPath, List<Object> value);
  Record set(FieldPath fieldPath, List<Object> value);

  Record setArray(String fieldPath, byte[] values);
  Record setArray(FieldPath fieldPath, byte[] values);

  Record setArray(String fieldPath, short[] values);
  Record setArray(FieldPath fieldPath, short[] values);

  Record setArray(String fieldPath, int[] values);
  Record setArray(FieldPath fieldPath, int[] values);

  Record setArray(String fieldPath, long[] values);
  Record setArray(FieldPath fieldPath, long[] values);

  Record setArray(String fieldPath, float[] values);
  Record setArray(FieldPath fieldPath, float[] values);

  Record setArray(String fieldPath, double[] values);
  Record setArray(FieldPath fieldPath, double[] values);

  Record setArray(String fieldPath, String[] values);
  Record setArray(FieldPath fieldPath, String[] values);

  Record setArray(String fieldPath, Object... values);
  Record setArray(FieldPath fieldPath, Object... values);

  Record setNull(String fieldPath);
  Record setNull(FieldPath fieldPath);

  Record delete(String fieldPath);
  Record delete(FieldPath fieldPath);

  String getString(String fieldPath);
  String getString(FieldPath fieldPath);

  boolean getBoolean(String fieldPath);
  boolean getBoolean(FieldPath fieldPath);

  byte getByte(String fieldPath);
  byte getByte(FieldPath fieldPath);

  short getShort(String fieldPath);
  short getShort(FieldPath fieldPath);

  int getInt(String fieldPath);
  int getInt(FieldPath fieldPath);

  long getLong(String fieldPath);
  long getLong(FieldPath fieldPath);

  float getFloat(String fieldPath);
  float getFloat(FieldPath fieldPath);

  double getDouble(String fieldPath);
  double getDouble(FieldPath fieldPath);

  Time getTime(String fieldPath);
  Time getTime(FieldPath fieldPath);

  Date getDate(String fieldPath);
  Date getDate(FieldPath fieldPath);

  Timestamp getTimestamp(String fieldPath);
  Timestamp getTimestamp(FieldPath fieldPath);

  BigDecimal getDecimal(String fieldPath);
  BigDecimal getDecimal(FieldPath fieldPath);

  ByteBuffer getBinary(String fieldPath);
  ByteBuffer getBinary(FieldPath fieldPath);

  Interval getInterval(String fieldPath);
  Interval getInterval(FieldPath fieldPath);

  Value getValue(String fieldPath);
  Value getValue(FieldPath fieldPath);

  Map<String, Object> getMap(String fieldPath);
  Map<String, Object> getMap(FieldPath fieldPath);

  List<Object> getList(String fieldPath);
  List<Object> getList(FieldPath fieldPath);

  /**
   * @return A new {@link RecordReader} over the current <code>Record</code>.
   */
  RecordReader asReader();

  RecordReader asReader(String fieldPath);
  RecordReader asReader(FieldPath fieldPath);

}
