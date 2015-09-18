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
package org.ojai;

import org.ojai.annotation.API;

@API.Public
public interface DocumentListener {

  /**
   * Called when a Document from the {@link DocumentStream} is available for
   * consumption.
   *
   * @param document The available <code>Document</code>.
   *
   * @return The implementation should return <code>false</code> to stop
   *         listening for more documents at which point stream is closed.
   */
  boolean documentArrived(Document document);

  /**
   * Called when an <code>Exception</code> occurs while retrieving a Document.
   * The {@code DocumentListener} will be closed and no new document will be
   * returned.
   *
   * @param e The exception which describes the failure.
   */
  void failed(Exception e);

  /**
   * Called when the end of the document stream is reached. The stream is
   * is already closed at this point.
   */
  void eos();

}
