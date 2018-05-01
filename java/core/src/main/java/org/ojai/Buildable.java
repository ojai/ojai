/**
 * Copyright (c) 2017 MapR, Inc.
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

import org.ojai.annotation.API;
import org.ojai.exceptions.OjaiException;

@API.ImmutableOnBuild
public interface Buildable {

  /**
   * Builds this object and makes it immutable.
   *
   * @return {@code this} for chaining
   */
  public Buildable build() throws OjaiException;

  /**
   * @return {@code true} if this object is built.
   */
  default public boolean isBuilt() {
    throw new UnsupportedOperationException(this.getClass().getTypeName());
  }

}
