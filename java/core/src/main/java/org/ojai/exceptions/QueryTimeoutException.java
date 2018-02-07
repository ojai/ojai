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
import org.ojai.store.Query;

/**
 * Exception thrown in the event of {@link Query} timeout.
 */
@API.Public
public class QueryTimeoutException extends OjaiException {

  private static final long serialVersionUID = 0x1488ede66742c6e8L;

  public QueryTimeoutException() {
    super();
  }

  public QueryTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }

  public QueryTimeoutException(String message) {
    super(message);
  }

  public QueryTimeoutException(Throwable cause) {
    super(cause);
  }

}
