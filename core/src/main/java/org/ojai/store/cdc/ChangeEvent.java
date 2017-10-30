/**
 * Copyright (c) 2016 MapR, Inc.
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
package org.ojai.store.cdc;

import org.ojai.Value.Type;
import org.ojai.annotation.API;

/**
 * This enumeration identifies the change event associated
 * with the current change node.
 */
@API.Public
public enum ChangeEvent {

  /**
   * The reader arrived at a field which does not have an event or 
   * event information is not available.
   */
  NULL(1),

  /**
   * The reader arrived at a scalar field or a field that was deleted.
   */
  NODE(2),

  /**
   * The reader arrived at beginning of a {@link Type#MAP MAP}.
   */
  START_MAP(3),

  /**
   * The reader reached the end of a {@link Type#MAP MAP}.
   */
  END_MAP(4),

  /**
   * The reader arrived at beginning of an {@link Type#ARRAY ARRAY}.
   */
  START_ARRAY(5),

  /**
   * The reader reached the end of an {@link Type#ARRAY ARRAY}.
   */
  END_ARRAY(6);

  private final byte code;

  private ChangeEvent(int code) {
    this.code = (byte) code;
  }

  /**
   * Returns the numerical code of the enumeration.
   */
  public byte getCode() {
    return code;
  }

}
