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

import static org.ojai.util.Constants.MILLISECONDSPERDAY;

import java.io.Serializable;

import org.ojai.annotation.API;

/**
 * An immutable class which encapsulates a time interval.
 */
@API.Public
public final class OInterval implements Serializable {

  private static final long serialVersionUID = 0x228372f2047c1511L;

  private static final double APPROX_DAYS_IN_YEAR = ((365 * 4) + 1)/4.0;

  private static final double APPROX_DAYS_IN_MONTH = APPROX_DAYS_IN_YEAR/12;

  private final int years;

  private final int months;

  private final int days;

  private final int seconds;

  private final int milliseconds;

  private final long timeDuration;

  public OInterval(final long milliseconds) {
    this.timeDuration = milliseconds;
    this.milliseconds = (int) (milliseconds % 1000);
    this.seconds      = (int) (milliseconds % MILLISECONDSPERDAY) / 1000;
    this.days         = (int) (milliseconds / MILLISECONDSPERDAY);
    this.months       = 0;
    this.years        = 0;
  }

  public OInterval(String iso8601DurationPattern) {
    // FIXME: parse the string as per ISO 8601 duration and time stamps format
    this(0, 0, 0, 0, 0);
  }

  public OInterval(final int years, final int months, final int days,
      final int seconds, final int milliseconds) {
    this.years = years;
    this.months = months;
    this.days = days;
    this.seconds = seconds;
    this.milliseconds = milliseconds;

    long totalDays = (long) ((years * APPROX_DAYS_IN_YEAR) + (months * APPROX_DAYS_IN_MONTH) + days);
    this.timeDuration = MILLISECONDSPERDAY * totalDays + seconds * 1000 + milliseconds;
  }

  /**
   * @return The years value of this {@code Interval} as an
   *         {@code int} or 0 if not set.
   */
  public int getYears() {
    return years;
  }

  /**
   * @return The months value of this {@code Interval} as an
   *         {@code int} or 0 if not set.
   */
  public int getMonths() {
    return months;
  }

  /**
   * @return The days value of this {@code Interval} as an
   *         {@code int} or 0 if not set.
   */
  public int getDays() {
    return days;
  }

  /**
   * @return The seconds value of this {@code Interval} as an
   *         {@code int} or 0 if not set.
   */
  public int getSeconds() {
    return seconds;
  }

  /**
   * @return The milliseconds value of this {@code Interval} as
   *         an {@code int} or 0 if not set.
   */
  public int getMilliseconds() {
    return milliseconds;
  }

  /**
   * Returns the value of this duration in milliseconds computed from
   * individual fields. This value will be approximate if months or years
   * field of this interval is non-zero.
   *
   * @return The length of the duration in milliseconds.
   */
  public long getTimeInMillis() {
    return timeDuration;
  }

}
