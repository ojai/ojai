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

import org.joda.time.DateTime;
import org.joda.time.Interval;

interface StreamReader {

  enum EventType {
    NULL,
    BOOLEAN,
    STRING,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    DATE,
    TIME,
    DATETIME,
    DECIMAL,
    INTERVAL,
    BINARY,
    FIELD_NAME,
    START_MAP,
    END_MAP,
    START_ARRAY,
    END_ARRAY,
  }

  StreamReader.EventType next();

  byte getByte();
  short getShort();
  int getInt();
  long getLong();
  float getFloat();
  double getDouble();
  boolean getBoolean();
  long getTimeStamp();
  String getString();
  BigDecimal getDecimal();
  Date getDate();
  Time getTime();
  DateTime getDateTime();
  Interval getInterval();

  int getDateInt();
  int getTimeInt();
  long getDateTimeLong();

  int getIntervalMonths();
  int getIntervalDays();
  int getIntervalMillis();

  int getDecimalPrecision();
  int getDecimalScale();

  int getDecimalValueAsInt();
  long getDecimalValueAsLong();
  ByteBuffer getDecimalValueAsBytes();

  ByteBuffer getBinary();

}
