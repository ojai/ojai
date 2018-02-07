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

/**
 * This exception is thrown if a Store operation fails if a Document already
 * existed when none was expected for the operation to succeed.
 */
public class DocumentExistsException extends StoreException {
  private static final long serialVersionUID = 8790183596738651462L;

  /** Default constructor. */
  public DocumentExistsException() {
    super();
  }

  /**
   * Constructor.
   * @param s Message.
   */
  public DocumentExistsException(String s) {
    super(s);
  }

  /**
   * Constructor taking another exception.
   * @param t exception to grab data from
   */
  public DocumentExistsException(Throwable t) {
    super(t);
  }

  /**
   * Constructor taking a message and another exception.
   * @param s message
   * @param t exception to grab data from
   */
  public DocumentExistsException(String s, Throwable t) {
    super(s, t);
  }

}
