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
package org.argonaut.json;

import java.util.Queue;

import org.argonaut.RecordReader;
import org.argonaut.Value;
import org.argonaut.RecordReader.EventType;
import org.argonaut.annotation.API;

@API.Public
public class Events {

  public static class TypeValuePair {
    public EventType eventType;
    public Value value;
    public TypeValuePair(EventType eventType) {
      this(eventType, null);
    }
    public TypeValuePair(EventType eventType, Value value) {
      this.eventType = eventType;
      this.value = value;
    }
  }

  public static interface Delegate {
    public boolean process(RecordReader reader,
        EventType event, Queue<TypeValuePair> eventQueue);
    public boolean bor(RecordReader reader, Queue<TypeValuePair> eventQueue);
    public boolean eor(RecordReader reader, Queue<TypeValuePair> eventQueue);
  }

  public static abstract class BaseDelegate implements Delegate {
    @Override
    public boolean process(RecordReader reader, EventType event,
        Queue<TypeValuePair> eventQueue) {
      return false;
    }

    @Override
    public boolean bor(RecordReader reader,
        Queue<TypeValuePair> eventQueue) {
      return false;
    }

    @Override
    public boolean eor(RecordReader reader,
        Queue<TypeValuePair> eventQueue) {
      return false;
    }
  }

}
