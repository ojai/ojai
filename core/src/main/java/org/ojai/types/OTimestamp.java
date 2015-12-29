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
package org.ojai.types;

import static org.joda.time.format.DateTimeFormat.forPattern;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.ParseException;

/**
 * An immutable class which encapsulates an OJAI {@link Type#TIMESTAMP TIMESTAMP} type.
 */
@API.Public
public final class OTimestamp implements Comparable<OTimestamp>, Serializable {

  private static final long serialVersionUID = 0x3800c3b7b7f0e008L;

  private static final DateTimeFormatter UTC_FORMATTER =
      forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZoneUTC();

  private static final DateTimeFormatter LOCAL_FORMATTER =
      forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").withZone(DateTimeZone.getDefault());

  private static final ISOChronology UTC_CHRONOLOGY = ISOChronology.getInstanceUTC();

  private volatile DateTime datetime; // for lazy initialization

  /** milliseconds since epoch */
  private final long millisSinceEpoch;

  /**
   * Parses and return an instance of {@code OTimestamp} from the specified string.
   * @param dateTimeStr  the string to parse
   * @exception ParseException if the beginning of the specified string
   *            cannot be parsed.
   */
  public static OTimestamp parse(String dateTimeStr) {
    try {
      return new OTimestamp(DateTime.parse(dateTimeStr));
    } catch (IllegalArgumentException e) {
      throw new ParseException(e);
    }
  }

  /**
   * Constructs an {@code OTimestamp} instance from the milliseconds value since the
   * Unix epoch.
   *
   * @param epoh  the milliseconds from 1970-01-01T00:00:00.000 UTC
   */
  public OTimestamp(long millisSinceEpoch) {
    this.millisSinceEpoch = millisSinceEpoch;
  }

  /**
   * Constructs an {@code OTimestamp} instance set to the specified fields in the
   * default time zone.
   *
   * @param year            the year
   * @param monthOfYear     the month of the year, from 1 to 12
   * @param dayOfMonth      the day of the month, from 1 to 31
   * @param hourOfDay       the hour of the day, from 0 to 23
   * @param minuteOfHour    the minute of the hour, from 0 to 59
   * @param secondOfMinute  the second of the minute, from 0 to 59
   * @param millisOfSecond  the millisecond of the second, from 0 to 999
   */
  public OTimestamp(
      int year,
      int monthOfYear,
      int dayOfMonth,
      int hourOfDay,
      int minuteOfHour,
      int secondOfMinute,
      int millisOfSecond) {
    this(new DateTime(year, monthOfYear, dayOfMonth, hourOfDay,
        minuteOfHour, secondOfMinute, millisOfSecond));
  }

  /**
   * Constructs an {@code OTimestamp} instance from a {@code java.util.Date) using
   * exactly the same field values.
   *
   * @param date  the Date to extract fields from
   */
  public OTimestamp(Date date) {
    this(new DateTime(date));
  }

  /*
   * Private constructors.
   */
  private OTimestamp(DateTime date_time) {
    datetime = date_time;
    millisSinceEpoch = date_time.getMillis();
  }

  /**
   * @return The UTC years value of this {@code Timestamp} as an {@code int}.
   */
  public int getYear() {
    return UTC_CHRONOLOGY.year().get(millisSinceEpoch);
  }

  /**
   * @return The UTC months value of this {@code Timestamp} as an {@code int}.
   */
  public int getMonth() {
    return UTC_CHRONOLOGY.monthOfYear().get(millisSinceEpoch);
  }

  /**
   * @return The UTC date of month value of this {@code Timestamp} as an {@code int}.
   */
  public int getDayOfMonth() {
    return UTC_CHRONOLOGY.dayOfMonth().get(millisSinceEpoch);
  }

  /**
   * @return The UTC hour part of this {@code Timestamp} as an {@code int}.
   */
  public int getHour() {
    return UTC_CHRONOLOGY.hourOfDay().get(millisSinceEpoch);
  }

  /**
   * @return The UTC minute part of this {@code Timestamp} as an {@code int}.
   */
  public int getMinute() {
    return UTC_CHRONOLOGY.minuteOfHour().get(millisSinceEpoch);
  }

  /**
   * @return The UTC second part of this {@code Timestamp} as an {@code int}.
   */
  public int getSecond() {
    return UTC_CHRONOLOGY.secondOfMinute().get(millisSinceEpoch);
  }

  /**
   * @return The UTC millisecond part of this {@code Timestamp} as an {@code int}.
   */
  public int getMilliSecond() {
    return UTC_CHRONOLOGY.millisOfSecond().get(millisSinceEpoch);
  }

  /**
   * @return The total number milliseconds since the Unix epoch.
   */
  public long getMillis() {
    return millisSinceEpoch;
  }

  /**
   * Get this timestamp as a {@link java.util.Date}. The {@code Date}
   * object created has exactly the same millisecond as this timestamp.
   * 
   * @return a Date initialized with this TIMESTAMP
   */
  public Date toDate() {
    return new Date(millisSinceEpoch);
  }

  /**
   * Returns the ISO8601 format timestamp string in UTC.
   */
  public String toUTCString() {
    return UTC_FORMATTER.print(getDateTime());
  }

  /**
   * Returns the ISO8601 format timestamp string in local time zone.
   */
  public String toLocalString() {
    return LOCAL_FORMATTER.print(getDateTime());
  }

  /**
   * Return the string representation the timestamp using the specified format
   * pattern.
   *
   * @param pattern  the pattern specification
   */
  public String toString(String pattern) {
    return getDateTime().toString(pattern);
  }

  @Override
  public String toString() {
    return toUTCString();
  }

  @Override
  public int hashCode() {
    return (int) (millisSinceEpoch ^ (millisSinceEpoch >>> 32));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OTimestamp other = (OTimestamp) obj;
    if (millisSinceEpoch != other.millisSinceEpoch)
      return false;
    return true;
  }

  @Override
  public int compareTo(OTimestamp o) {
    return Long.compare(millisSinceEpoch, o.millisSinceEpoch);
  }

  private DateTime getDateTime() {
    if (datetime == null) {
      synchronized (this) {
        if (datetime == null) {
          datetime = new DateTime(millisSinceEpoch, UTC_CHRONOLOGY);
        }
      }
    }
    return datetime;
  }

}
