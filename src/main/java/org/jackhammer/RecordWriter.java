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

import org.jackhammer.types.Interval;

public interface RecordWriter {

  /* Map Methods */
  RecordWriter put(String field, boolean value);
  RecordWriter put(String field, String value);
  RecordWriter put(String field, byte value);
  RecordWriter put(String field, short value);
  RecordWriter put(String field, int value);
  RecordWriter put(String field, long value);
  RecordWriter put(String field, float value);
  RecordWriter put(String field, double value);
  RecordWriter put(String field, Date value);
  RecordWriter put(String field, BigDecimal value);
  RecordWriter put(String field, byte[] value);
  RecordWriter put(String field, byte[] value, int off, int len);
  RecordWriter put(String field, ByteBuffer value);
  RecordWriter put(String field, Interval value);
  RecordWriter putNewMap(String field);
  RecordWriter putNewArray(String field);

  RecordWriter putNull(String field);

  RecordWriter put(String field, Value value);
  RecordWriter put(String field, Record value);

  /* Advanced Map Methods */
  RecordWriter putDecimal(String field, int unscaledValue, int scale);
  RecordWriter putDecimal(String field, long unscaledValue, int scale);
  RecordWriter putDecimal(String field, double unscaledValue, int scale);
  RecordWriter putDecimal(String field, float unscaledValue, int scale);
  RecordWriter putDecimal(String field, byte[] unscaledValue, int scale);

  RecordWriter putDate(String field, int days);
  RecordWriter putTime(String field, int millis);
  RecordWriter putTimestamp(String field, long timeMillis);
  RecordWriter putInterval(String field, long durationInMs);
  RecordWriter putInterval(String field, int months, int days, int milliseconds);

  /* Array Methods */
  RecordWriter add(boolean value);
  RecordWriter add(String value);
  RecordWriter add(byte value);
  RecordWriter add(short value);
  RecordWriter add(int value);
  RecordWriter add(long value);
  RecordWriter add(float value);
  RecordWriter add(double value);
  RecordWriter add(BigDecimal value);

  RecordWriter add(Time value);
  RecordWriter add(Date value);
  RecordWriter add(Timestamp value);
  RecordWriter add(Interval value);

  RecordWriter add(byte[] value);
  RecordWriter add(byte[] value, int off, int len);
  RecordWriter add(ByteBuffer value);

  RecordWriter addNull();

  RecordWriter add(Value value);
  RecordWriter add(Record value);

  /* Advanced Array Methods */
  RecordWriter addNewArray();
  RecordWriter addNewMap();

  RecordWriter addDecimal(int unscaledValue, int scale);
  RecordWriter addDecimal(long unscaledValue, int scale);
  RecordWriter addDecimal(double unscaledValue, int scale);
  RecordWriter addDecimal(float unscaledValue, int scale);
  RecordWriter addDecimal(byte[] unscaledValue, int scale);

  RecordWriter addDate(int days);
  RecordWriter addTime(int millis);
  RecordWriter addTimestamp(long timeMillis);
  RecordWriter addInterval(long durationInMs);
  RecordWriter addInterval(int months, int days, int milliseconds);

  /* Lifecycle methods */
  RecordWriter endArray();
  RecordWriter endMap();

  Record build();
}
