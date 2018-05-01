/**
 * Copyright (c) 2018 MapR, Inc.
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
package org.ojai;

import org.ojai.exceptions.ReadOnlyObjectException;

/**
 * A container of OJAI objects. Not all implementing classes are required to implement all methods.
 * @since 3.0
 */
public interface Container {

  /**
   * @return the number of children in this container.
   */
  public default int size() {
    throw new UnsupportedOperationException(this.getClass().getTypeName());
  }

  /**
   * @return {@code true} if this container is empty, i.e. {@link #size()} == 0.
   *
   */
  public default boolean isEmpty() {
    throw new UnsupportedOperationException(this.getClass().getTypeName());
  }

  /**
   * @returns {@code true} if this container can not be modified.
   */
  public default boolean isReadOnly() {
    throw new UnsupportedOperationException(this.getClass().getTypeName());
  }

  /**
   * Empties this container, removing all children.
   *
   * @throws ReadOnlyObjectException if this container is read-only.
   * @return {@code this} for chaining.
   */
  public default Container empty() {
    throw new UnsupportedOperationException(this.getClass().getTypeName());
  }

}
