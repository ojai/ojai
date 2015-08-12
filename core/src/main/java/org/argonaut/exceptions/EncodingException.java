/**
 * Copyright (c) 2014 MapR, Inc.
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
package org.argonaut.exceptions;

import org.argonaut.annotation.API;

/**
 * Exception thrown by implementations if a failure occurs during
 * serialization of the document.
 */
@API.Public
public class EncodingException extends ArgonautException {

  private static final long serialVersionUID = 0x549350ba59b3b829L;

  public EncodingException() {
    super();
  }

  public EncodingException(String message, Throwable cause) {
    super(message, cause);
  }

  public EncodingException(String message) {
    super(message);
  }

  public EncodingException(Throwable cause) {
    super(cause);
  }

}
