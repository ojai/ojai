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

import org.ojai.Document;

/**
 * This class is used to wrap a exception for stream processing that fails
 * midway. It tracks each Document that cannot be processed along with any
 * corresponding exception for that Document.
 */
public class FailedOp {

  Document document;
  Exception exception;

  FailedOp() {
    document = null;
    exception = null;
  }

  public FailedOp(Document doc, Exception e) {
    document = doc;
    exception = e;
  }

  public Document getDocument() {
    return document;
  }

  public Exception getException() {
    return exception;
  }

}
