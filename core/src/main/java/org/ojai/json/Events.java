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
package org.ojai.json;

import java.util.Queue;

import org.ojai.DocumentReader;
import org.ojai.Value;
import org.ojai.DocumentReader.EventType;
import org.ojai.annotation.API;

@API.Public
public class Events {

  public static class EventDescriptor {
    private final EventType eventType;
    private Value value;
    private String fieldName;
    private int index;

    public EventDescriptor(EventType eventType) {
      this.index = -1;
      this.fieldName = null;
      this.eventType = eventType;
    }

    public EventType getEventType() {
      return eventType;
    }

    public Value getValue() {
      return value;
    }

    public EventDescriptor setValue(Value value) {
      this.value = value;
      return this;
    }

    public String getFieldName() {
      return fieldName;
    }

    public EventDescriptor setFieldName(String fieldName) {
      this.fieldName = fieldName;
      return this;
    }

    public int getIndex() {
      return index;
    }

    public EventDescriptor setIndex(int index) {
      this.index = index;
      return this;
    }
  }

  public static interface Delegate {
    public boolean process(DocumentReader reader,
        EventType event, Queue<EventDescriptor> eventQueue);
    public boolean bor(DocumentReader reader, Queue<EventDescriptor> eventQueue);
    public boolean eor(DocumentReader reader, Queue<EventDescriptor> eventQueue);
  }

  public static abstract class BaseDelegate implements Delegate {
    @Override
    public boolean process(DocumentReader reader, EventType event,
        Queue<EventDescriptor> eventQueue) {
      return false;
    }

    @Override
    public boolean bor(DocumentReader reader,
        Queue<EventDescriptor> eventQueue) {
      return false;
    }

    @Override
    public boolean eor(DocumentReader reader,
        Queue<EventDescriptor> eventQueue) {
      return false;
    }
  }

}
