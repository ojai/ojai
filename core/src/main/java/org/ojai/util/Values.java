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
package org.ojai.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.exceptions.TypeException;
import org.ojai.json.Json;
import org.ojai.json.JsonOptions;

import com.google.common.annotations.VisibleForTesting;

/**
 * A helper class that provides convenience methods
 * to operate on a {@code Value}.
 */
@API.Public
public class Values {
  private static TimeZone LOCAL = TimeZone.getDefault();

  private static final String DATE_FORMATTER_STR = "yyyy-MM-dd";
  private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER = new ThreadLocal<SimpleDateFormat>() {
    @Override protected SimpleDateFormat initialValue() {
        return new SimpleDateFormat(DATE_FORMATTER_STR);
    }
  };

  private static final String SHORT_DATE_FORMATTER_STR = "yyyy-MM";
  private static final int shortDateStringLen = SHORT_DATE_FORMATTER_STR.length();
  private static final ThreadLocal<SimpleDateFormat> SHORT_DATE_FORMATTER = new ThreadLocal<SimpleDateFormat>() {
    @Override protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat(SHORT_DATE_FORMATTER_STR);
    }
  };

  private static final String SHORT_TIME_FORMATTER_STR = "HH:mm";
  private static final int shortTimeStringLen = SHORT_TIME_FORMATTER_STR.length();
  private static final ThreadLocal<SimpleDateFormat> SHORT_TIME_FORMATTER = new ThreadLocal<SimpleDateFormat>() {
    @Override protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat(SHORT_TIME_FORMATTER_STR);
    }
  };

  private static final String TIME_FORMATTER_STR = "HH:mm:ss";
  private static final int timeStringLen = TIME_FORMATTER_STR.length();
  private static final ThreadLocal<SimpleDateFormat> TIME_FORMATTER = new ThreadLocal<SimpleDateFormat>() {
    @Override protected SimpleDateFormat initialValue() {
        return new SimpleDateFormat(TIME_FORMATTER_STR);
    }
  };

  private static final String FULL_TIME_FORMATTER_STR = "HH:mm:ss.SSS";
  private static final ThreadLocal<SimpleDateFormat> FULL_TIME_FORMATTER = new ThreadLocal<SimpleDateFormat>() {
    @Override protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat(FULL_TIME_FORMATTER_STR);
    }
  };

  private static final String TIMESTAMP_FORMATTER_STR = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  private static final ThreadLocal<SimpleDateFormat> TIMESTAMP_FORMATTER = new ThreadLocal<SimpleDateFormat>() {
    @Override protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat(TIMESTAMP_FORMATTER_STR);
    }
  };

  /**
   * @return The specified value as a <code>byte</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static byte asByte(Value value) {
    return asNumber(value).byteValue();
  }

  /**
   * @return The specified value as a <code>short</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static short asShort(Value value) {
    return asNumber(value).shortValue();
  }

  /**
   * @return The specified value as an <code>int</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static int asInt(Value value) {
    return asNumber(value).intValue();
  }

  /**
   * @return The specified value as a <code>long</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static long asLong(Value value) {
    return asNumber(value).longValue();
  }

  /**
   * @return The specified value as a <code>float</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static float asFloat(Value value) {
    return asNumber(value).floatValue();
  }

  /**
   * @return The specified value as a <code>double</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static double asDouble(Value value) {
    return asNumber(value).doubleValue();
  }

  /**
   * @return The specified value as a <code>BigDecimal</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException uf the specified value is
   * not one of the number types
   */
  public static BigDecimal asDecimal(Value value) {
    Number val = asNumber(value);
    return (val instanceof BigDecimal)
        ? (BigDecimal) val
            : ((val instanceof Long)
                ? new BigDecimal(val.longValue())
            : new BigDecimal(val.doubleValue()));
  }

  /**
   * @return The specified value as a <code>Number</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException if the specified value is
   * not one of the number types
   */
  public static Number asNumber(Value value) {
    switch (value.getType()) {
    case BYTE:
      return value.getByte();
    case SHORT:
      return value.getShort();
    case INT:
      return value.getInt();
    case LONG:
      return value.getLong();
    case FLOAT:
      return value.getFloat();
    case DOUBLE:
      return value.getDouble();
    case DECIMAL:
      return value.getDecimal();
    default:
      throw new TypeException(value.getType() + " can not be converted to a Number.");
    }
  }

  /*
   * Converts a string to BigDecimal object.
   */
  public static BigDecimal parseBigDecimal(String s) {
    return new BigDecimal(s);
  }

  /**
   * Converts a string in ISO-8601 date format to a <code>Date</code> value.
   * This method assumes that the specified date string is in GMT, unlike
   * {@link Date#valueOf(String)}, which uses the current timezone.
   *
   * @param date a <code>String</code> object representing a date in
   *        in the format "yyyy-mm-dd"
   * @return A <code>java.sql.Date</code> object representing the
   *         given date
   * @throws IllegalArgumentException if the date given is not in the
   *         ISO-8601 date format (yyyy-mm-dd)
   */
  public static Date parseDate(String date) {
    try {
      if (date.length() > shortDateStringLen) {
        return new Date(getDateFormatter().parse(date).getTime());
      } else {
        return new Date(getShortDateFormatter().parse(date).getTime());
      }
    } catch (ParseException e) {
      throw new IllegalArgumentException("Can not parse the provided date: " + date, e);
    }
  }

  /**
   * Returns the JDBC string representation ("yyyy-mm-dd") of the specified Date.
   * @param date the {@code Date} value to stringify.
   * @return The formatted date string.
   */
  public static String toDateStr(Date date) {
    return getDateFormatter().format(date);
  }

  /**
   * Converts a string in ISO-8601 time format to a <code>Time</code> value.
   * This assumes that the specified time string is in GMT, unlike
   * {@link Time#valueOf(String)}, which uses current timezone.
   *
   * @param time a <code>String</code> object representing a time in
   *        in the format "HH:mm", "HH:mm:ss" or "HH:mm:ss.SSS"
   * @return A <code>java.sql.Time</code> object representing the
   *         given time
   * @throws IllegalArgumentException if the time given is not in the
   *         ISO-8601 date format "HH:mm:ss" or "HH:mm:ss.SSS"
   */
  public static Time parseTime(String time) {
    try {
      if (time.length() > timeStringLen) {
        return new Time(getFullTimeFormatter().parse(time).getTime());
      } else if (time.length() > shortTimeStringLen) {
        return new Time(getTimeFormatter().parse(time).getTime());
      } else {
        return new Time(getShortTimeFormatter().parse(time).getTime());
      }
    } catch (ParseException e) {
      throw new IllegalArgumentException("Can not parse the provided time: " + time, e);
    }
  }

  /**
   * Returns the JDBC string representation ("HH:mm:ss") of the specified Time.
   * @param time the {@code Time} value to stringify
   * @return The formatted time string
   */
  public static String toTimeStr(Time time) {
    return getTimeFormatter().format(time);
  }

  /**
   * Returns the JDBC string representation ("HH:mm") of the specified Time.
   * @param time the {@code Time} value to stringify
   * @return The formatted time string
   */
  public static String toShortTimeStr(Time time) {
    return getShortTimeFormatter().format(time);
  }

  /**
   * Returns the JDBC string representation ("HH:mm") of the specified Time.
   * @param time the {@code Time} value to stringify
   * @return The formatted time string
   */
  public static String toFullTimeStr(Time time) {
    return getFullTimeFormatter().format(time);
  }

  /**
   * Converts a string in ISO-8601 time format to a <code>Time</code> value.
   * This assumes that the specified time string is in GMT, unlike
   * {@link Timestamp#valueOf(String)}, which uses current timezone.
   *
   * @param timestampe a <code>String</code> object representing a timestamp in
   *        in the format "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
   * @return A <code>java.sql.Timestampe</code> object representing the
   *         given timestamp
   * @throws IllegalArgumentException if the time given is not in the
   *         ISO-8601 timestamp format "yyyy-MM-dd'T'HH:mm:ss.SSSXXX".
   */
  public static Timestamp parseTimestamp(String timestampe) {
    try {
      return new Timestamp(getTimestampFormatter().parse(timestampe).getTime());
    } catch (ParseException e) {
      throw new IllegalArgumentException("Can not parse the provided timestampe: " + timestampe, e);
    }
  }

  public static String toTimestampString(Timestamp timeStamp) {
    return getTimestampFormatter().format(timeStamp);
  }

  /**
   * Converts a {@code Value} to its extended JSON representation.<br/><br/>
   * The 7 intrinsic types, &nbsp;{@code null, boolean, string, long,
   * double, array, and map }, are represented in regular JSON. The
   * extended types are converted to a singleton map with the type tag name
   * as the key and the value of the given {@code Value} as its value.
   * The following sample illustrates the string representation of the
   * various types.
   *
   * <pre>
   * {
   *   "map": {
   *     "null": null,
   *     "boolean" : true,
   *     "string": "eureka",
   *     "byte" : {"$numberLong": 127},
   *     "short": {"$numberLong": 32767},
   *     "int": {"$numberLong": 2147483647},
   *     "long": {"$numberLong":9223372036854775807},
   *     "float" : 3.4028235E38,
   *     "double" : 1.7976931348623157e308,
   *     "decimal": {"$decimal": "12345678901234567890189012345678901.23456789"},
   *     "date": {"$dateDay": "&lt;yyyy-mm-dd&gt;"},
   *     "time" : {"$time" : "&lt;HH:mm:ss[.sss]&gt;"},
   *     "timestamp" : {"$date" : "&lt;yyyy-MM-ddTHH:mm:ss.SSSXXX&gt;"},
   *     "interval" : {"$interval" : &lt;number_of_millisecods&gt;},
   *     "binary" : {"$binary" : "&lt;base64_encoded_binary_value&gt;"},
   *     "array" : [42, "open sesame", 3.14, {"$dateDay": "2015-01-21"}]
   *   }
   * }
   * </pre>
   *
   * @param value a <code>Value</code> that should be converted to JSON string
   * @return The extended JSON representation of the given value
   */
  public static String asJsonString(Value value) {
    return Json.toJsonString(value.asReader(), JsonOptions.WITH_TAGS);
  }

  @VisibleForTesting
  public static void setTimeZone (TimeZone tz) {
    LOCAL = (TimeZone) tz.clone();
  }

  private static DateFormat getShortDateFormatter() {
    final SimpleDateFormat df = SHORT_DATE_FORMATTER.get();
    df.setTimeZone(LOCAL);
    return df;
  }

  private static DateFormat getDateFormatter() {
    final SimpleDateFormat f = DATE_FORMATTER.get();
    f.setTimeZone(LOCAL);
    return f;
  }

  private static DateFormat getShortTimeFormatter() {
    final SimpleDateFormat f = SHORT_TIME_FORMATTER.get();
    f.setTimeZone(LOCAL);
    return f;
  }

  private static DateFormat getTimeFormatter() {
    final SimpleDateFormat f = TIME_FORMATTER.get();
    f.setTimeZone(LOCAL);
    return f;
  }

  private static DateFormat getFullTimeFormatter() {
    final SimpleDateFormat f = FULL_TIME_FORMATTER.get();
    f.setTimeZone(LOCAL);
    return f;
  }

  private static DateFormat getTimestampFormatter() {
    final SimpleDateFormat f = TIMESTAMP_FORMATTER.get();
    f.setTimeZone(LOCAL);
    return f;
  }

}
