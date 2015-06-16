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
package org.jackhammer.types;

import java.io.Serializable;

import org.jackhammer.annotation.API;
import org.jackhammer.util.Constants;

/**
 * An lightweight, immutable class which encapsulates a time interval.
 */
@API.Public
public class Interval implements Serializable, Constants {

  private static final long serialVersionUID = 0x228372f2047c1511L;

  private final long _millis;

  public Interval(final long millis) {
      _millis = millis;
  }

  /**
   * @return The interval duration in milliseconds.
   */
  public long getInterval() {
    return _millis;
  }

  /*
   * @return Number of days in the interval.
   */
  public int getDays() {
    return (int)(_millis/MILLISECONDSPERDAY);
  }

}
