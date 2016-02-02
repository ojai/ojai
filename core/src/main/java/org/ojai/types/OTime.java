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

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.ParseException;

/**
 * An immutable class which encapsulates an OJAI {@link Type#TIME TIME} type.
 */
@API.Public
public final class OTime implements Comparable<OTime>, Serializable {

  private static final long serialVersionUID = 0xaffa9a5dfe3ff863L;
  private static final DateTime EPOCH_DATE = new LocalDate(1970, 1, 1).toDateTimeAtStartOfDay();

  private transient volatile LocalTime time; // for lazy initialization

  /** milliseconds from midnight */
  private final int millisOfDay;

  /**
   * Parses and return an instance of {@code OTime} from the specified string.
   * @param timeStr  the string to parse
   * @exception ParseException if the beginning of the specified string
   *            cannot be parsed.
   */
  public static OTime parse(String timeStr) {
    try {
      return new OTime(LocalTime.parse(timeStr));
    } catch (IllegalArgumentException e) {
      throw new ParseException(e);
    }
  }

  /**
   * Return an {@code OTime} instance from the specified milliseconds since
   * midnight.
   *
   * @param millisOfDay the number of milliseconds since midnight
   */
  public static OTime fromMillisOfDay(int millisOfDay) {
    return new OTime(millisOfDay);
  }

  /**
   * Constructs an {@code OTime} instance from the milliseconds value since the
   * Unix epoch. The TIME value is set to the corresponding time in the default
   * time zone.
   *
   * @param epoh  the milliseconds from 1970-01-01T00:00:00.000 UTC
   */
  public OTime(long epoh) {
    this(new LocalTime(epoh));
  }

  /**
   * Constructs an {@code OTime} instance set to the specified hour, minute and
   * seconds.
   *
   * @param hourOfDay the hour of the day, from 0 to 23
   * @param minutes   the minute of the hour, from 0 to 59
   * @param seconds   the second of the minute, from 0 to 59
   */
  public OTime(int hourOfDay, int minutes, int seconds) {
    this(hourOfDay, minutes, seconds, 0);
  }

  /**
   * Constructs an {@code OTime} instance set to the specified hour, minute,
   * seconds and milliseconds.
   *
   * @param hourOfDay the hour of the day, from 0 to 23
   * @param minutes   the minute of the hour, from 0 to 59
   * @param seconds   the second of the minute, from 0 to 59
   * @param ms        the millisecond of the second, from 0 to 999
   */
  public OTime(int hourOfDay, int minutes, int seconds, int ms) {
    this(new LocalTime(hourOfDay, minutes, seconds, ms));
  }

  /**
   * Constructs an {@code OTime} instance from a {@code java.util.Date) using
   * exactly the same field values. The TIME value is set to the corresponding
   * time in the default time zone.
   *
   * @param date  the Date to extract fields from
   */
  public OTime(Date date) {
    this(LocalTime.fromDateFields(date));
  }

  /*
   * Private constructors.
   */
  private OTime(LocalTime localTime) {
    time = localTime;
    millisOfDay = localTime.getMillisOfDay();
  }
  private OTime(int millisOfDay) {
    this.millisOfDay = millisOfDay;
  }

  /**
   * @return The hour part of this {@code OTime} as an {@code int}.
   */
  public int getHour() {
    return getTime().getHourOfDay();
  }

  /**
   * @return The minute part of this {@code OTime} as an {@code int}.
   */
  public int getMinute() {
    return getTime().getMinuteOfHour();
  }

  /**
   * @return The second part of this {@code OTime} as an {@code int}.
   */
  public int getSecond() {
    return getTime().getSecondOfMinute();
  }

  /**
   * @return The millisecond part of this {@code OTime} as an {@code int}.
   */
  public int getMilliSecond() {
    return getTime().getMillisOfSecond();
  }

  /**
   * Get this TIME as a {@link java.util.Date} in the default timezone.
   * The year, month and date fields of the {@code Date} object is set to Unix
   * epoch date (1970-01-01).
   * 
   * @return a Date initialized with this TIME
   */
  public Date toDate() {
    return getTime().toDateTime(EPOCH_DATE).toDate();
  }

  /**
   * @return The total number milliseconds since the midnight for this TIME in UTC.
   */
  public int toTimeInMillis() {
    return millisOfDay;
  }

  /**
   * @return A string representation of this TIME in "HH:mm" format
   */
  public String toShortTimeStr() {
    return toString("HH:mm");
  }

  /**
   * Returns a string representation of this TIME in "HH:mm.ss.SSS" format.
   */
  public String toFullTimeStr() {
    return toString();
  }

  /**
   * Returns a string representation of this TIME in "HH:mm.ss[.SSS]" format.
   * The milliseconds are omitted if zero.
   */
  public String toTimeStr() {
    return (millisOfDay % 1000 == 0)
        ? toString("HH:mm:ss") : toString("HH:mm:ss.SSS");
  }

  /**
   * Returns a string representation of this TIME using the specified format pattern.
   *
   * @param pattern  the pattern specification
   */
  public String toString(String pattern) {
    return getTime().toString(pattern);
  }

  @Override
  public String toString() {
    return getTime().toString();
  }

  @Override
  public int hashCode() {
    return millisOfDay;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OTime other = (OTime) obj;
    if (millisOfDay != other.millisOfDay)
      return false;
    return true;
  }

  @Override
  public int compareTo(OTime o) {
    return millisOfDay - o.millisOfDay;
  }

  private LocalTime getTime() {
    if (time == null) {
      synchronized (this) {
        if (time == null) {
          time = LocalTime.fromMillisOfDay(millisOfDay);
        }
      }
    }
    return time;
  }

}
