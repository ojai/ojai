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
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.exceptions.TypeException;

import com.google.common.io.BaseEncoding;

/**
 * A helper class which provide convenience methods
 * to operate on a {@code Value}.
 */
@API.Public
public class Values {
  private static final TimeZone LOCAL = Calendar.getInstance().getTimeZone();

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
   * @return Returns the specified value as a <code>byte</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException If the specified value is
   * not one of the number types.
   */
  public static byte asByte(Value value) {
    return asNumber(value).byteValue();
  }

  /**
   * @return Returns the specified value as a <code>short</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException If the specified value is
   * not one of the number types.
   */
  public static short asShort(Value value) {
    return asNumber(value).shortValue();
  }

  /**
   * @return Returns the specified value as a <code>int</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException If the specified value is
   * not one of the number types.
   */
  public static int asInt(Value value) {
    return asNumber(value).intValue();
  }

  /**
   * @return Returns the specified value as a <code>long</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException If the specified value is
   * not one of the number types.
   */
  public static long asLong(Value value) {
    return asNumber(value).longValue();
  }

  /**
   * @return Returns the specified value as a <code>float</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException If the specified value is
   * not one of the number types.
   */
  public static float asFloat(Value value) {
    return asNumber(value).floatValue();
  }

  /**
   * @return Returns the specified value as a <code>double</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException If the specified value is
   * not one of the number types.
   */
  public static double asDouble(Value value) {
    return asNumber(value).doubleValue();
  }

  /**
   * @return Returns the specified value as a <code>BigDecimal</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException If the specified value is
   * not one of the number types.
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
   * @return Returns the specified value as a <code>Number</code>.
   * This may involve rounding or truncation.
   * @throws IllegalArgumentException If the specified value is
   * not one of the number types.
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
   * This assumes that the specified date string is in GMT. This is different
   * from {@link Date#valueOf(String)} which uses current timezone.
   *
   * @param date a <code>String</code> object representing a date in
   *        in the format "yyyy-mm-dd".
   * @return a <code>java.sql.Date</code> object representing the
   *         given date.
   * @throws IllegalArgumentException if the date given is not in the
   *         ISO-8601 date format (yyyy-mm-dd).
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
   * @param date The {@code Date} value to stringify.
   * @return The formatted date string.
   */
  public static String toDateStr(Date date) {
    return getDateFormatter().format(date);
  }

  /**
   * Converts a string in ISO-8601 time format to a <code>Time</code> value.
   * This assumes that the specified time string is in GMT. This is different
   * from {@link Time#valueOf(String)} which uses current timezone.
   *
   * @param time a <code>String</code> object representing a time in
   *        in the format "HH:mm", "HH:mm:ss" or "HH:mm:ss.SSS".
   * @return a <code>java.sql.Time</code> object representing the
   *         given time.
   * @throws IllegalArgumentException if the time given is not in the
   *         ISO-8601 date format "HH:mm:ss" or "HH:mm:ss.SSS".
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
   * @param time The {@code Time} value to stringify.
   * @return The formatted time string.
   */
  public static String toTimeStr(Time time) {
    return getTimeFormatter().format(time);
  }

  /**
   * Returns the JDBC string representation ("HH:mm") of the specified Time.
   * @param time The {@code Time} value to stringify.
   * @return The formatted time string.
   */
  public static String toShortTimeStr(Time time) {
    return getShortTimeFormatter().format(time);
  }

  /**
   * Returns the JDBC string representation ("HH:mm") of the specified Time.
   * @param time The {@code Time} value to stringify.
   * @return The formatted time string.
   */
  public static String toFullTimeStr(Time time) {
    return getFullTimeFormatter().format(time);
  }

  /**
   * Converts a string in ISO-8601 time format to a <code>Time</code> value.
   * This assumes that the specified time string is in GMT. This is different
   * from {@link Timestamp#valueOf(String)} which uses current timezone.
   *
   * @param timestampe a <code>String</code> object representing a timestamp in
   *        in the format "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
   * @return a <code>java.sql.Timestampe</code> object representing the
   *         given timestamp.
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
   * The 7 intrinsic types, i.e. &nbsp;{@code null, boolean, string, long,
   * double, array and map } are as their regular JSON representation. The
   * extended types are converted to a singleton map with the type tag name
   * as the key and the value of the given {@code Value} as its value.
   * Following is a sample illustration of the string representation of the
   * various types.
   *
   * <pre>
   * {
   *   "map": {
   *     "null": null,
   *     "boolean" : true,
   *     "string": "eureka",
   *     "byte" : {"$byte": 127},
   *     "short": {"$short": 32767},
   *     "int": {"$int": 2147483647},
   *     "long": 9223372036854775807,
   *     "float" : {"$float" : 3.4028235E38},
   *     "double" : 1.7976931348623157e308,
   *     "decimal": {"$decimal": "12345678901234567890189012345678901.23456789"},
   *     "date": {"$date": "&lt;yyyy-mm-dd&gt;"},
   *     "time" : {"$time" : "&lt;HH:mm:ss[.sss]&gt;"},
   *     "timestamp" : {"$timestamp" : "&lt;yyyy-MM-ddTHH:mm:ss.SSS+Z&gt;"},
   *     "interval" : {"$interval" : &lt;number_of_millisecods&gt;},
   *     "binary" : {"$binary" : "&lt;base64_encoded_binary_value&gt;"},
   *     "array" : [42, "open sesame", 3.14, {"$date": "2015-01-21"}]
   *   }
   * }
   * </pre>
   *
   * @param value A <code>Value</code> which should be converted to JSON string.
   * @return Extended JSON representation of the given value.
   */
  public static String asJsonString(Value value) {
    StringBuilder sb = new StringBuilder();
    if (Types.isExtendedType(value)) {
      sb.append('{').append('"').append(Types.getTypeTag(value)).append('"').append(':');
      switch (value.getType()) {
      case DATE:
        sb.append('"').append(getDateFormatter().format(value.getDate())).append('"');
        break;
      case TIME:
        int millis = value.getTimeAsInt();
        if (millis % 1000 == 0) {
          sb.append('"').append(getTimeFormatter().format(value.getTime())).append('"');
        } else {
          sb.append('"').append(getFullTimeFormatter().format(value.getTime())).append('"');
        }
        break;
      case TIMESTAMP:
        sb.append('"').append(getTimestampFormatter().format(value.getTimestamp())).append('"');
        break;
      case DECIMAL:
        sb.append('"').append(value.getObject()).append('"');
        break;
      case INTERVAL:
        sb.append(value.getIntervalAsLong());
        break;
      case BINARY:
        byte[] bytes;
        ByteBuffer bbuf = value.getBinary();
        if (bbuf.hasArray()) {
          bytes = bbuf.array();
        } else {
          bytes = new byte[bbuf.remaining()];
          bbuf.get(bytes);
        }
        sb.append('"').append(BaseEncoding.base64().encode(bytes)).append('"');
        break;
      default:
        sb.append(value.getObject());
      }
      sb.append('}');
    } else {
      switch (value.getType()) {
      case STRING:
        sb.append('"').append(value.getString().replaceAll("[\n\r]+", "\\\\n")).append('"');
        break;
      default:
        sb.append(value.getObject());
      }
    }
    return sb.toString();
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
    return f;
  }

}
