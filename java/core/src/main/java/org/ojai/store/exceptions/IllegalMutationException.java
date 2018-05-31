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
package org.ojai.store.exceptions;

import org.ojai.store.DocumentMutation;
import org.ojai.store.DocumentStore;

/**
 * Thrown when a {@link DocumentStore#update()} requests a mutation which could structurally alter a
 * Document without such intention.<p/>
 * For example, if {@link DocumentMutation#set()} is used to set a field to a scalar value but is
 * currently a Map or array, the mutation is rejected.<p/>
 * Use {@link DocumentMutation#setOrReplace()} instead to force such mutation.
 * 
 * @since 3.0
 */
public class IllegalMutationException extends StoreException {
  private static final long serialVersionUID = 0xdc6ebd19a0b5bb45L;

  public IllegalMutationException() {
  }

  public IllegalMutationException(String s) {
    super(s);
  }

  public IllegalMutationException(Throwable t) {
    super(t);
  }

  public IllegalMutationException(String s, Throwable t) {
    super(s, t);
  }

}
