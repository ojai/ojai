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
package org.ojai.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import org.ojai.DocumentReader;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

import com.google.common.base.Preconditions;

/**
 * An implementation of {@link DocumentReader} interface which can be used to project
 * a sub-set of fields
 */
@API.Public
public class DocumentReaderWithProjection implements DocumentReader {

  private final DocumentReader reader;

  private final FieldProjector projector;

  public DocumentReaderWithProjection(
      @NonNullable final DocumentReader reader, @NonNullable final FieldProjector projection) {
    this.reader = Preconditions.checkNotNull(reader);
    this.projector = Preconditions.checkNotNull(projection).reset(reader);
  }

  @Override
  public EventType next() {
    do {
      EventType event = reader.next();
      if (event == null) {
        return null; // end of document
      } else if (event == EventType.START_MAP
          && reader.inMap()
          && reader.getFieldName() == null) {
        return event; // beginning of the document
      }

      // move the projection tree to this event
      projector.moveTo(event);

      if (projector.shouldEmitEvent()) {
        // if projection allows, emit the current event
        return event;
      } else {
        // otherwise, skip the entire sub-tree
        reader.skipChildren();
      }
    } while (true);
  }

  @Override
  public DocumentReader skipChildren() {
    reader.skipChildren();
    return this;
  }

  @Override
  public boolean inMap() {
    return reader.inMap();
  }

  @Override
  public String getFieldName() {
    return reader.getFieldName();
  }

  @Override
  public int getArrayIndex() {
    return reader.getArrayIndex();
  }

  @Override
  public byte getByte() {
    return reader.getByte();
  }

  @Override
  public short getShort() {
    return reader.getShort();
  }

  @Override
  public int getInt() {
    return reader.getInt();
  }

  @Override
  public long getLong() {
    return reader.getLong();
  }

  @Override
  public float getFloat() {
    return reader.getFloat();
  }

  @Override
  public double getDouble() {
    return reader.getDouble();
  }

  @Override
  public BigDecimal getDecimal() {
    return reader.getDecimal();
  }

  @Override
  public int getDecimalPrecision() {
    return reader.getDecimalPrecision();
  }

  @Override
  public int getDecimalScale() {
    return reader.getDecimalScale();
  }

  @Override
  public int getDecimalValueAsInt() {
    return reader.getDecimalValueAsInt();
  }

  @Override
  public long getDecimalValueAsLong() {
    return reader.getDecimalValueAsLong();
  }

  @Override
  public ByteBuffer getDecimalValueAsBytes() {
    return reader.getDecimalValueAsBytes();
  }

  @Override
  public boolean getBoolean() {
    return reader.getBoolean();
  }

  @Override
  public String getString() {
    return reader.getString();
  }

  @Override
  public long getTimestampLong() {
    return reader.getTimestampLong();
  }

  @Override
  public OTimestamp getTimestamp() {
    return reader.getTimestamp();
  }

  @Override
  public int getDateInt() {
    return reader.getDateInt();
  }

  @Override
  public ODate getDate() {
    return reader.getDate();
  }

  @Override
  public int getTimeInt() {
    return reader.getTimeInt();
  }

  @Override
  public OTime getTime() {
    return reader.getTime();
  }

  @Override
  public OInterval getInterval() {
    return reader.getInterval();
  }

  @Override
  public int getIntervalDays() {
    return reader.getIntervalDays();
  }

  @Override
  public long getIntervalMillis() {
    return reader.getIntervalMillis();
  }

  @Override
  public ByteBuffer getBinary() {
    return reader.getBinary();
  }

  @Override
  public EventType getCurrentEvent() {
    return reader.getCurrentEvent();
  }

}
