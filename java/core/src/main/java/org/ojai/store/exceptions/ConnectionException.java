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

import org.ojai.exceptions.OjaiException;
import org.ojai.store.Connection;

/**
 * This class is the base class of all exceptions thrown from the {@link Connection}.
 */
public class ConnectionException extends OjaiException {
  private static final long serialVersionUID = 0x94f211d05ddd9260L;

  /** Default constructor. */
  public ConnectionException() {
    super();
  }

  /**
   * Constructor.
   * @param s message
   */
  public ConnectionException(String s) {
    super(s);
  }

  /**
   * Constructor taking another exception.
   * @param t exception to grab data from
   */
  public ConnectionException(Throwable t) {
    super(t);
  }

  /**
   * Constructor taking a message and another exception.
   * @param s message
   * @param t exception to grab data from
   */
  public ConnectionException(String s, Throwable t) {
    super(s, t);
  }

}
