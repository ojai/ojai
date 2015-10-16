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
package org.ojai.json.impl;

import static org.ojai.util.Constants.MILLISECONDSPERDAY;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.ojai.DocumentReader;
import org.ojai.annotation.API;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.TypeException;
import org.ojai.types.Interval;
import org.ojai.util.Types;
import org.ojai.util.Values;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

@API.Internal
public class JsonStreamDocumentReader implements DocumentReader {

  private final JsonDocumentStream documentStream;
  private JsonToken currentToken; /* current token read from stream */
  private JsonToken nextToken; /* next token from stream */
  private int mapLevel;
  private boolean eor;

  protected long currentLongValue = 0;
  protected Object currentObjValue = null;
  protected double currentDoubleValue = 0;
  protected EventType currentEventType;

  /* flag used to lookup next token to determine extended types */
  private boolean lookupToken = false;
  /*
   * isExtendedType is used to track if we are parsing extended type from stream.
   * If we do, we need to ignore the END_OBJECT token.
   */
  private boolean isExtendedType = false;

  JsonStreamDocumentReader(JsonDocumentStream stream) {
    documentStream = stream;
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
          isExtendedType = true;

          // determine extended type
          switch(field_name) {
          case Types.TAG_LONG:
            return EventType.LONG;
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
    case VALUE_NUMBER_FLOAT:
      return EventType.DOUBLE;
    case FIELD_NAME:
      return EventType.FIELD_NAME;
    default:
      throw new DecodingException("Encountered unexpected token of type: " + currentToken);
    }
  }

  protected void cacheCurrentValue() throws IOException {
    switch (currentEventType) {
    case BOOLEAN:
      currentObjValue = isEventBoolean() ? getParser().getBooleanValue() : Boolean.valueOf(getValueAsString());
      break;
    case STRING:
    case FIELD_NAME:
      currentObjValue = getParser().getText();
      break;
    case BYTE:
      currentLongValue = (getParser().getLongValue() & 0xff);
      break;
    case SHORT:
      currentLongValue = (getParser().getLongValue() & 0xffff);
      break;
    case INT:
      currentLongValue = (getParser().getLongValue() & 0xffffffff);
      break;
    case LONG:
      currentLongValue = getParser().getLongValue();
      break;
    case FLOAT:
    case DOUBLE:
      currentDoubleValue = getParser().getDoubleValue();
      break;
    case DECIMAL:
      currentObjValue = Values.parseBigDecimal(getParser().getText());
      break;
    case DATE:
      currentObjValue = Values.parseDate(getParser().getText());
      break;
    case TIME:
      currentObjValue = Values.parseTime(getParser().getText());
      break;
    case TIMESTAMP:
      currentObjValue = Values.parseTimestamp(getParser().getText());
      break;
    case INTERVAL:
      currentLongValue = getParser().getLongValue();
      break;
    case BINARY:
      currentObjValue = ByteBuffer.wrap(getParser().getBinaryValue());
      break;
    default:
      // ARRAY, MAP and NULL need not be cached
      break;
    }
  }

  private void checkEventType(EventType eventType) throws TypeException {
    if (currentEventType != eventType) {
      throw new TypeException(String.format(
          "Event type mismatch, expected %s, got %s", eventType, currentEventType));
    }
  }

  private void checkNumericEventType() throws TypeException {
    switch (currentEventType) {
    case BYTE:
    case SHORT:
    case INT:
    case LONG:
    case FLOAT:
    case DOUBLE:
      return;
    default:
      break;
    }
    throw new TypeException("Event type mismatch. Expected numeric type, found "
        + currentEventType);
  }

  protected JsonParser getParser() {
    return documentStream.getParser();
  }

  protected boolean isEventBoolean() {
    return getParser().getCurrentToken().isBoolean();
  }

  protected boolean isEventNumeric() {
    return getParser().getCurrentToken().isNumeric();
  }

  protected String getValueAsString() {
    try {
      return getParser().getText();
    } catch (JsonParseException e) {
      throw new IllegalStateException(e);
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  /**
   * @return {@code true} if the {@code DocumentReader} has read past the end
   *         of the current document or end of the underlying stream has been
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
        et = currentEventType = eventType();
        cacheCurrentValue();
      }

      if (et == EventType.START_MAP) {
        mapLevel++;
      } else if (et == EventType.END_MAP) {
        if (!isExtendedType) {
          mapLevel--;
        } else {
          isExtendedType = false;
          return next();
        }
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
   * Forward the stream to end of the current document
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

  @Override
  public String getFieldName() {
    checkEventType(EventType.FIELD_NAME);
    return (String) currentObjValue;
  }


  @Override
  public byte getByte() {
    checkNumericEventType();
    return (byte) currentLongValue;
  }

  @Override
  public short getShort() {
    checkNumericEventType();
    return (short) currentLongValue;
  }

  @Override
  public int getInt() {
    checkNumericEventType();
    return (int) currentLongValue;
  }

  @Override
  public long getLong() {
    checkEventType(EventType.LONG);
    return currentLongValue;
  }

  @Override
  public float getFloat() {
    checkEventType(EventType.FLOAT);
    return (float) currentDoubleValue;
  }

  @Override
  public double getDouble() {
    checkEventType(EventType.DOUBLE);
    return currentDoubleValue;
  }

  @Override
  public BigDecimal getDecimal() {
    checkEventType(EventType.DECIMAL);
    return (BigDecimal) currentObjValue;
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
    checkEventType(EventType.BOOLEAN);
    return (boolean) currentObjValue;
  }

  @Override
  public String getString() {
    checkEventType(EventType.STRING);
    return (String) currentObjValue;
  }

  @Override
  public long getTimestampLong() {
    checkEventType(EventType.TIMESTAMP);
    return ((Timestamp) currentObjValue).getTime();
  }

  @Override
  public Timestamp getTimestamp() {
    checkEventType(EventType.TIMESTAMP);
    return (Timestamp) currentObjValue;
  }

  @Override
  public int getDateInt() {
    checkEventType(EventType.DATE);
    return (int) JsonUtils.dateToNumDays((Date) currentObjValue);
  }

  @Override
  public Date getDate() {
    checkEventType(EventType.DATE);
    return (Date) currentObjValue;
  }

  @Override
  public int getTimeInt() {
    checkEventType(EventType.TIME);
    return (int) (((Time) currentObjValue).getTime() % MILLISECONDSPERDAY);
  }

  @Override
  public Time getTime() {
    checkEventType(EventType.TIME);
    return (Time) currentObjValue;
  }

  @Override
  public Interval getInterval() {
    checkEventType(EventType.INTERVAL);
    return new Interval(currentLongValue);
  }

  @Override
  public int getIntervalDays() {
    checkEventType(EventType.INTERVAL);
    return (int) (currentLongValue / MILLISECONDSPERDAY);
  }

  @Override
  public long getIntervalMillis() {
    checkEventType(EventType.INTERVAL);
    return currentLongValue;
  }

  @Override
  public ByteBuffer getBinary() {
    checkEventType(EventType.BINARY);
    return (ByteBuffer) currentObjValue;
  }

}
