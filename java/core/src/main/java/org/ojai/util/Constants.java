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

import java.util.TimeZone;

import org.ojai.annotation.API;

@API.Public
public class Constants {

  /**
   * A long value equal to the time zone offset from the GMT in milliseconds.
   */
  public static final long TIMEZONE_OFFSET = TimeZone.getDefault().getRawOffset();

  /**
   * A long value equal to the number of milliseconds in a second.
   */
  public final static int MILLISECONDSPERSECOND = 1000;

  /**
   * A long value equal to the number of milliseconds in a minute.
   */
  public final static int MILLISECONDSPERMINUTE = 60 * MILLISECONDSPERSECOND;

  /**
   * A long value equal to the number of milliseconds in an hour.
   */
  public final static int MILLISECONDSPERHOUR = 60 * MILLISECONDSPERMINUTE;

  /**
   * A long value equal to the number of milliseconds in a day.
   */
  public final static long MILLISECONDSPERDAY = 24 * MILLISECONDSPERHOUR;

}
