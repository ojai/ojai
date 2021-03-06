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

/**
 * An operation was requested without sufficient authentication.
 */
public class AuthenticationException extends SecurityException {
  private static final long serialVersionUID = 0x67fc5aeaa264e09L;

  public AuthenticationException() {
  }

  public AuthenticationException(String s, Throwable t) {
    super(s, t);
  }

  public AuthenticationException(String s) {
    super(s);
  }

  public AuthenticationException(Throwable t) {
    super(t);
  }

}
