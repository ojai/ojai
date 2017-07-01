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

import org.ojai.annotation.API;

/**
 * This enumeration identifies the change event associated with the current change node.
 */
@API.Public
public enum ChangeOp {

  /**
   * The current change node does not have an operation, for example, the event of the node is an END_ARRAY
   * or END_MAP operation.
   */
  NULL(1),

  /**
   * The current field was set to the specified value.
   */
  SET(2),

  /**
   * The current (Map) field was merged with the specified value.
   */
  MERGE(3),

  /**
   * All values older than or equal to the delete operation timestamp
   * were deleted.
   */
  DELETE(4),

  /**
   * The operation deleted the current field, or a version thereof, whose
   * timestamp was same as that of the current change node.
   */
  DELETE_EXACT(5),

  /**
   * Add another version to the current field.
   */
  PUT(6);

  private final byte code;

  private ChangeOp(int code) {
    this.code = (byte) code;
  }

  public byte getCode() {
    return code;
  }

}
