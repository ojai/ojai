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

package org.jackhammer.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.jackhammer.RecordReader;
import org.jackhammer.exceptions.DecodingException;
import org.jackhammer.exceptions.TypeException;
import org.jackhammer.types.Interval;
import org.jackhammer.util.Types;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonRecordReader implements RecordReader {

  private JsonRecordStream recordStream;
  private JsonToken currentToken; /* current token read from stream */
  private JsonToken nextToken; /* next token from stream */
  private EventType currentEventType; /* used for caching current event type */
  private int mapLevel;
  private boolean eor;

  /* flag used to lookup next token to determine extended types */
  private boolean lookupToken = false;

  JsonRecordReader(JsonRecordStream stream) {
    recordStream = stream;
    currentToken = null;
    nextToken = null;
    mapLevel = 0;
    eor = false;
  }

  /*
   * Get next token. If it's FIELD_NAME then if it starts with '$' then it's
   * extended type. Find the type and return it before advancing to next value
   * token.
   */
  private EventType nextType() {
    try {
      nextToken = getParser().nextToken();
      lookupToken = true;

      if (nextToken == JsonToken.FIELD_NAME) {
        String field_name = getParser().getCurrentName();

        if (field_name.startsWith("$")) {
          currentToken = getParser().nextToken();
          lookupToken = false;

          // determine extended type
          switch(field_name) {
          case Types.TAG_BYTE:
            return EventType.BYTE;
          case Types.TAG_SHORT:
            return EventType.SHORT;
          case Types.TAG_INT:
            return EventType.INT;
          case Types.TAG_FLOAT:
            return EventType.FLOAT;
          case Types.TAG_DECIMAL:
            return EventType.DECIMAL;
          case Types.TAG_DATE:
            return EventType.DATE;
          case Types.TAG_TIME:
            return EventType.TIME;
          case Types.TAG_TIMESTAMP:
            return EventType.TIMESTAMP;
          case Types.TAG_INTERVAL:
            return EventType.INTERVAL;
          case Types.TAG_BINARY:
            return EventType.BINARY;
          default:
            break;
          }
        }
      }
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
    return EventType.START_MAP;
  }

  private JsonParser getParser() {
    return recordStream.getParser();
  }

  /* returns the eventType associated with the currentToken */
  private EventType eventType() {
    switch (currentToken) {
    case START_OBJECT:
      return nextType();
    case START_ARRAY:
      return EventType.START_ARRAY;
    case VALUE_NULL:
      return EventType.NULL;
    case VALUE_TRUE:
    case VALUE_FALSE:
      return EventType.BOOLEAN;
    case VALUE_STRING:
      return EventType.STRING;
    case END_OBJECT:
      return EventType.END_MAP;
    case END_ARRAY:
      return EventType.END_ARRAY;
    case VALUE_NUMBER_INT:
      return EventType.LONG;
    case VALUE_NUMBER_FLOAT:
      return EventType.DOUBLE;
    case FIELD_NAME:
      return EventType.FIELD_NAME;
    default:
      return null;
    }
  }

  /**
   * @return {@code true} if the {@code RecordReader} has read past the end
   *         of the current record or end of the underlying stream has been
   *         reached.
   */
  boolean eor() {
    if (eor) {
      return true;
    }
    try {
      nextToken = getParser().nextToken();
      lookupToken = true;
      return nextToken == null;
    } catch (IOException e) {
      throw new DecodingException(e);
    }
  }

  @Override
  public EventType next() {
    if (eor) {
      return null;
    }

    EventType et = null;
    try {
      if (lookupToken) {
        currentToken = nextToken;
        lookupToken = false;
      } else {
        currentToken = getParser().nextToken();
      }

      if (currentToken != null) {
        /* cache current event type */
        currentEventType = eventType();
        et = currentEventType;
      }

      if (et == EventType.START_MAP) {
        mapLevel++;
      } else if (et == EventType.END_MAP) {
        mapLevel--;
      }
      if (mapLevel == 0) {
        eor = true;
      }
    } catch (JsonParseException jp) {
      throw new IllegalStateException(jp);
    } catch (IOException e) {
      throw new DecodingException(e);
    }

    return et;
  }

  /**
   * Forward the stream to end of the current record
   */
  void readFully() {
    if (eor) {
      return;
    }
    /**
     * FIXME: Currently this method just consume the remaining tokens from
     *        the parser stream and discard them. Instead we need to cache
     *        the token and return them in next() and getXXXX() calls.
     */
    try {
      JsonToken token;
      while((token = getParser().nextToken()) != null) {
        switch (token) {
        case START_OBJECT:
          mapLevel++;
          break;
        case END_OBJECT:
          mapLevel--;
          break;
        default:
        }

        if (mapLevel == 0) {
          break;
        }
      }
      eor = true;
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new DecodingException(e);
    }
  }

  private void CheckEventType(EventType eventType) throws TypeException {
    if (currentEventType != eventType) {
      throw new TypeException("Event type mismatch");
    }
  }

  @Override
  public String getFieldName() {
    CheckEventType(EventType.FIELD_NAME);
    try {
      return getParser().getCurrentName();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public byte getByte() {
    CheckEventType(EventType.BYTE);
    try {
      return (byte) (getParser().getLongValue() & 0xff);
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public short getShort() {
    CheckEventType(EventType.SHORT);
    try {
      return (short) (getParser().getLongValue() & 0xffff);
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public int getInt() {
    CheckEventType(EventType.INT);
    try {
      return (int) (getParser().getLongValue() & 0xffffffff);
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public long getLong() {
    CheckEventType(EventType.LONG);
    try {
      return getParser().getLongValue();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public float getFloat() {
    CheckEventType(EventType.FLOAT);
    try {
      return getParser().getFloatValue();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public double getDouble() {
    CheckEventType(EventType.DOUBLE);
    try {
      return getParser().getDoubleValue();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public BigDecimal getDecimal() {
    CheckEventType(EventType.DECIMAL);
    try {
      return getParser().getDecimalValue();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public int getDecimalPrecision() {
    BigDecimal decimal = getDecimal();
    if (decimal != null) {
      return decimal.precision();
    }
    return 0;
  }

  @Override
  public int getDecimalScale() {
    BigDecimal decimal = getDecimal();
    if (decimal != null) {
      return decimal.scale();
    }
    return 0;
  }

  @Override
  public int getDecimalValueAsInt() {
    BigDecimal decimal = getDecimal();
    if (decimal != null) {
      return decimal.intValueExact();
    }
    return 0;
  }

  @Override
  public long getDecimalValueAsLong() {
    BigDecimal decimal = getDecimal();
    if (decimal != null) {
      return decimal.longValueExact();
    }
    return 0;
  }

  @Override
  public ByteBuffer getDecimalValueAsBytes() {
    BigDecimal decimal = getDecimal();
    if (decimal != null) {
      BigInteger decimalInteger = decimal.unscaledValue();
      byte[] bytearray = decimalInteger.toByteArray();
      return ByteBuffer.wrap(bytearray);
    }
    return null;
  }

  @Override
  public boolean getBoolean() {
    CheckEventType(EventType.BOOLEAN);
    try {
      return getParser().getBooleanValue();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public String getString() {
    CheckEventType(EventType.STRING);
    try {
      return getParser().getText();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public long getTimeStamp() {
    CheckEventType(EventType.TIMESTAMP);
    try {
      return getParser().getLongValue();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public Timestamp getTimestamp() {
    CheckEventType(EventType.TIMESTAMP);
    try {
      long l = getParser().getLongValue();
      return new Timestamp(l);
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public int getDateInt() {
    CheckEventType(EventType.DATE);
    try {
      return getParser().getIntValue();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public Date getDate() {
    CheckEventType(EventType.DATE);
    try {
      return new Date(getParser().getLongValue());
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public int getTimeInt() {
    CheckEventType(EventType.TIME);
    try {
      return getParser().getIntValue();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public Time getTime() {
    CheckEventType(EventType.TIME);
    try {
      return new Time(getParser().getLongValue());
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public Interval getInterval() {
    CheckEventType(EventType.INTERVAL);
    try {
      return new Interval(getParser().getLongValue());
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public int getIntervalMillis() {
    CheckEventType(EventType.INTERVAL);
    try {
      return (int) (getParser().getLongValue() & 0xffffffff);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public ByteBuffer getBinary() {
    CheckEventType(EventType.BINARY);
    try {
      return ByteBuffer.wrap(getParser().getBinaryValue());
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  @Override
  public int getIntervalDays() {
    CheckEventType(EventType.INTERVAL);
    try {
      Interval i = new Interval(getParser().getLongValue());
      return i.getDays();
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

}
