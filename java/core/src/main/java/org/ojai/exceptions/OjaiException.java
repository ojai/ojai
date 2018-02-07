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
package org.ojai.exceptions;

import org.ojai.annotation.API;

/**
 * The parent class of all exceptions thrown from this library.
 */
@API.Public
public class OjaiException extends RuntimeException {

  private static final long serialVersionUID = 0xd5abadb72d9d62edL;

  public OjaiException() {
    super();
  }

  public OjaiException(String message, Throwable cause) {
    super(message, cause);
  }

  public OjaiException(String message) {
    super(message);
  }

  public OjaiException(Throwable cause) {
    super(cause);
  }

}
