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
package org.ojai.store.exceptions;

import org.ojai.exceptions.OjaiException;
import org.ojai.store.DocumentStore;

/**
 * This class is the base class of all exceptions thrown from the {@link DocumentStore}.
 */
public class StoreException extends OjaiException {
  private static final long serialVersionUID = -6009041115768531693L;

  /** Default constructor. */
  public StoreException() {
    super();
  }

  /**
   * Constructor.
   * @param s message
   */
  public StoreException(String s) {
    super(s);
  }

  /**
   * Constructor taking another exception.
   * @param t exception to grab data from
   */
  public StoreException(Throwable t) {
    super(t);
  }

  /**
   * Constructor taking a message and another exception.
   * @param s message
   * @param t exception to grab data from
   */
  public StoreException(String s, Throwable t) {
    super(s, t);
  }

}
