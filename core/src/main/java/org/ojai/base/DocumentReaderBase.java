/**
 * Copyright (c) 2017 MapR, Inc.
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
package org.ojai.base;

import org.ojai.DocumentReader;
import org.ojai.annotation.API;

/**
 * A template abstract class that implements the {@link DocumentReader} interface.
 */
@API.Public
public abstract class DocumentReaderBase implements DocumentReader {

  /**
   * This is a trivial implementation of {@link #skipChildren()} API.
   * <p/>
   * An optimized implementation could simply skip over the sub-tree by advancing the
   * reader buffer to the end of current map or array.
   */
  @Override
  public DocumentReader skipChildren() {
    EventType currentEvent = getCurrentEvent();
    if (currentEvent == null) {
      return this;
    }

    switch (currentEvent) {
    case START_MAP:
    case START_ARRAY:
      int levelCount = 1;
      EventType nextEvt = null;
      while (levelCount > 0
          && ((nextEvt = next()) != null)) {
        switch (nextEvt) {
        case START_MAP:
        case START_ARRAY:
          levelCount++;
          break;
        case END_MAP:
        case END_ARRAY:
          levelCount--;
          break;
        default:
          break;
        }
      }
      break;
    default:
      // no-op for all other event
      break;
    }
    return this;
  }

}
