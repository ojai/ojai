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

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.exceptions.ParseException;

/**
 * An immutable class which encapsulates an OJAI {@link Type#DATE DATE} type.
 */
@API.Public
public final class ODate implements Comparable<ODate>, Serializable {

  private static final long serialVersionUID = 0xaffa9a5dfe3ff863L;

  private static final LocalDate EPOCH_DATE = new LocalDate(1970, 1, 1);
  private static final LocalTime START_OF_DAY = new LocalTime(0, 0, 0);

  private volatile LocalDate date; // for lazy initialization

  /** number of days since Unix epoch */
  private final int daysSinceEpoch;

  /**
   * Parses and return an instance of {@code ODate} from the specified string.
   * @param dateStr  the string to parse
   * @exception ParseException if the beginning of the specified string
   *            cannot be parsed.
   */
  public static ODate parse(String dateStr) {
    try {
      return new ODate(LocalDate.parse(dateStr));
    } catch (IllegalArgumentException e) {
      throw new ParseException(e);
    }
  }

  /**
   * Return an {@code ODate} instance from the specified number of days since epoch.
   *
   * @param millisOfDay the number of days since epoch
   */
  public static ODate fromDaysSinceEpoch(int daysSinceEpoch) {
    return new ODate(daysSinceEpoch);
  }

  /**
   * Constructs an {@code ODate} instance set to the specified year, month and
   * day of the month.
   *
   * @param year        the year
   * @param month       the month of the year, from 1 to 12
   * @param dayOfMonth  the day of the month, from 1 to 31
   */
  public ODate(int year, int month, int dayOfMonth) {
    this(new LocalDate(year, month, dayOfMonth));
  }

  /**
   * Constructs an {@code ODate} instance from the milliseconds value since the
   * Unix epoch. The DATE value is set to the calendar date in the default time
   * zone.
   *
   * @param epoh  the milliseconds from 1970-01-01T00:00:00.000 UTC
   */
  public ODate(long epoh) {
    this(new LocalDate(epoh));
  }

  /**
   * Constructs an {@code ODate} instance from a {@code java.util.Date) using
   * exactly the same field values. The DATE value is set to the calendar date
   * in the default time zone.
   *
   * @param date  the Date to extract fields from
   */
  public ODate(Date date) {
    this(LocalDate.fromDateFields(date));
  }

  /*
   * Private constructors.
   */
  private ODate(int daysSinceEpoch) {
    this.daysSinceEpoch = daysSinceEpoch;
  }
  private ODate(LocalDate localDate) {
    date = localDate;
    daysSinceEpoch = Days.daysBetween(EPOCH_DATE, date).getDays();
  }

  /**
   * @return The years value of this {@code Date} as an {@code int}.
   */
  public int getYear() {
    return getDate().getYear();
  }

  /**
   * @return The months value of this {@code Date} as an {@code int}.
   */
  public int getMonth() {
    return getDate().getMonthOfYear();
  }

  /**
   * @return The date of month value of this {@code Date} as an {@code int}.
   */
  public int getDayOfMonth() {
    return getDate().getDayOfMonth();
  }

  /**
   * @return The total number of days since the Unix epoch.
   */
  public int toDaysSinceEpoch() {
    return daysSinceEpoch;
  }

  /**
   * Get this DATE as a {@link java.util.Date} in the default timezone.
   * The time fields of the {@code Date} object is set to Unix 0.
   *
   * @return a Date initialized with this DATE
   */
  public Date toDate() {
    return getDate().toDateTime(START_OF_DAY).toDate();
  }

  /**
   * Returns a string representation of this date in ISO8601 format (yyyy-MM-dd).
   */
  public String toDateStr() {
    return toString("yyyy-MM-dd");
  }

  /**
   * Returns a string representation of this date using the specified format pattern.
   *
   * @param pattern  the pattern specification
   */
  public String toString(String pattern) {
    return getDate().toString(pattern);
  }

  @Override
  public String toString() {
    return toDateStr();
  }

  @Override
  public int hashCode() {
    return daysSinceEpoch;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ODate other = (ODate) obj;
    if (daysSinceEpoch != other.daysSinceEpoch)
      return false;
    return true;
  }

  @Override
  public int compareTo(ODate o) {
    return daysSinceEpoch - o.daysSinceEpoch;
  }

  private LocalDate getDate() {
    if (date == null) {
      synchronized (this) {
        if (date == null) {
          date = EPOCH_DATE.plusDays(daysSinceEpoch);
        }
      }
    }
    return date;
  }

}
