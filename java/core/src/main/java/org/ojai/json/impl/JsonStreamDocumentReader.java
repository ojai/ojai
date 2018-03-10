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
import static org.ojai.util.Types.TAG_BINARY;
import static org.ojai.util.Types.TAG_BYTE;
import static org.ojai.util.Types.TAG_DATE;
import static org.ojai.util.Types.TAG_DECIMAL;
import static org.ojai.util.Types.TAG_FLOAT;
import static org.ojai.util.Types.TAG_INT;
import static org.ojai.util.Types.TAG_INTERVAL;
import static org.ojai.util.Types.TAG_LONG;
import static org.ojai.util.Types.TAG_SHORT;
import static org.ojai.util.Types.TAG_TIME;
import static org.ojai.util.Types.TAG_TIMESTAMP;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Stack;

import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.base.DocumentReaderBase;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.TypeException;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;
import org.ojai.util.Values;
import org.ojai.util.impl.ContainerContext;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

@API.Internal
public class JsonStreamDocumentReader extends DocumentReaderBase {

  private final JsonDocumentStream documentStream;
  private int mapLevel;
  private boolean eor;

  private long currentLongValue = 0;
  private Object currentObjValue = null;
  private double currentDoubleValue = 0;
  private EventType currentEvent;

  private String fieldName;
  private LinkedList<JsonToken> cachedTokens;
  private Stack<ContainerContext> containerStack;
  private ContainerContext currentContainer;
  private boolean isExtended;

  JsonStreamDocumentReader(JsonDocumentStream stream) {
    containerStack = new Stack<ContainerContext>();
    cachedTokens = new LinkedList<JsonToken>();
    documentStream = stream;
    mapLevel = 0;
    eor = false;
  }

  /*
   * Get next token which should be a field name. If it's one of the extended
   * type, find the type and the value. Verify that the map ends with a single
   * field and consume the following END_OBJECT token.
   */
  private EventType parseMap() {
    // we just entered a Map, look ahead to see if
    // the field name matches an extended type
    JsonToken nextToken = peekToken();
    if (nextToken == JsonToken.END_OBJECT) {
      return EventType.START_MAP; // empty Map
    } else if (nextToken != JsonToken.FIELD_NAME) {
      throw new DecodingException("Encountered " + nextToken
          + " while looking for a field name.");
    }

    String field_name = getCurrentName();
    if (field_name.startsWith("$")) {
      // determine extended type
      isExtended = true;
      switch(field_name) {
      case TAG_BYTE:
        setCurrentEventType(EventType.BYTE);
        break;
      case TAG_SHORT:
        setCurrentEventType(EventType.SHORT);
        break;
      case TAG_INT:
        setCurrentEventType(EventType.INT);
        break;
      case TAG_LONG:
        setCurrentEventType(EventType.LONG);
        break;
      case TAG_FLOAT:
        setCurrentEventType(EventType.FLOAT);
        break;
      case TAG_DECIMAL:
        setCurrentEventType(EventType.DECIMAL);
        break;
      case TAG_DATE:
        setCurrentEventType(EventType.DATE);
        break;
      case TAG_TIME:
        setCurrentEventType(EventType.TIME);
        break;
      case TAG_TIMESTAMP:
        setCurrentEventType(EventType.TIMESTAMP);
        break;
      case TAG_INTERVAL:
        setCurrentEventType(EventType.INTERVAL);
        break;
      case TAG_BINARY:
        setCurrentEventType(EventType.BINARY);
        break;
      default:
        // regular map, return without consuming the field name.
        isExtended = false;
        return EventType.START_MAP;
      }

      // At this point, we have determined that the current map is
      // one of the extended type, so let's consume the field name
      nextToken();
      // and move forward so that the parser is at field value,
      nextToken = nextToken();
      // then cache the current value
      cacheCurrentValue();
      // finally, consume the END_OBJECT tag
      nextToken = nextToken();
      if (nextToken != JsonToken.END_OBJECT) {
        throw new DecodingException("Encountered " + nextToken
            + " while looking for end object token.");
      }
      return currentEvent;
    } else {
      return EventType.START_MAP;
    }
  }

  /**
   * Reads and caches the current Value from the stream.
   * The parser must be positioned at the value and the
   * current {@link EventType} must be set by calling
   * {@link #setCurrentEventType(EventType)}.
   */
  protected void cacheCurrentValue() {
    try {
      switch (currentEvent) {
      case BOOLEAN:
        currentObjValue = isEventBoolean() ? getParser().getBooleanValue()
            : Boolean.valueOf(getValueAsString());
        break;
      case STRING:
        currentObjValue = getParser().getText();
        break;
      case BYTE:
        currentLongValue = getValueAsLong() & 0xff;
        break;
      case SHORT:
        currentLongValue = getValueAsLong() & 0xffff;
        break;
      case INT:
        currentLongValue = getValueAsLong() & 0xffffffff;
        break;
      case LONG:
        currentLongValue = getValueAsLong();
        break;
      case FLOAT:
      case DOUBLE:
        currentDoubleValue = getValueAsDouble();
        break;
      case DECIMAL:
        currentObjValue = Values.parseBigDecimal(getParser().getText());
        break;
      case DATE:
        currentObjValue = ODate.parse(getParser().getText());
        break;
      case TIME:
        currentObjValue = OTime.parse(getParser().getText());
        break;
      case TIMESTAMP:
        currentObjValue = OTimestamp.parse(getParser().getText());
        break;
      case INTERVAL:
        currentLongValue = getValueAsLong();
        break;
      case BINARY:
        currentObjValue = ByteBuffer.wrap(getParser().getBinaryValue());
        break;
      default:
        // ARRAY, MAP and NULL need not be cached
        break;
      }
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  private void checkEventType(EventType eventType) throws TypeException {
    if (currentEvent != eventType) {
      throw new TypeException(String.format(
          "Event type mismatch, expected %s, got %s", eventType, currentEvent));
    }
  }

  private void checkNumericEventType() throws TypeException {
    switch (currentEvent) {
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
        + currentEvent);
  }

  private JsonParser getParser() {
    return documentStream.getParser();
  }

  private boolean isEventNumeric() {
    return getParser().getCurrentToken().isNumeric();
  }

  private boolean isEventBoolean() {
    return getParser().getCurrentToken().isBoolean();
  }

  private long getValueAsLong() throws NumberFormatException, IOException {
    return isEventNumeric() ? getParser().getLongValue()
        : Long.valueOf(getValueAsString());
  }

  private double getValueAsDouble() throws NumberFormatException, IOException {
    return isEventNumeric() ? getParser().getDoubleValue()
        : Double.valueOf(getValueAsString());
  }

  protected String getValueAsString() {
    try {
      return getParser().getText();
    } catch (IOException e) {
      throw new DecodingException(e);
    }
  }

  /**
   * @return {@code true} if the {@code DocumentReader} has read past the end
   *         of the current document or end of the underlying stream has been
   *         reached.
   */
  protected boolean eor() {
    return eor || !hasMoreTokens();
  }

  @Override
  public EventType next() {
    isExtended = false;
    if (eor()) {
      return null;
    }

    JsonToken currentToken = nextToken();
    if (currentToken == JsonToken.FIELD_NAME) {
      fieldName = getCurrentName();
      currentToken = nextToken();
    }
    updateCurrentContainer();

    switch (currentToken) {
    case START_OBJECT:
      setCurrentEventType(parseMap());
      if (currentEvent == EventType.START_MAP) {
        containerStack.push(new ContainerContext(Type.MAP, fieldName));
      }
      break;
    case END_OBJECT:
      setCurrentEventType(EventType.END_MAP);
      ContainerContext lastContainer = containerStack.pop();
      if (lastContainer.getType() == Type.MAP) {
        fieldName = lastContainer.getFieldName();
      }
      updateCurrentContainer();
      break;
    case START_ARRAY:
      setCurrentEventType(EventType.START_ARRAY);
      if (!inMap()) {
        currentContainer.incrementIndex();
      }
      containerStack.push(new ContainerContext(Type.ARRAY));
      break;
    case END_ARRAY:
      setCurrentEventType(EventType.END_ARRAY);
      containerStack.pop();
      updateCurrentContainer();
      break;
    case VALUE_NULL:
      setCurrentEventType(EventType.NULL).cacheCurrentValue();
      break;
    case VALUE_TRUE:
    case VALUE_FALSE:
      setCurrentEventType(EventType.BOOLEAN).cacheCurrentValue();
      break;
    case VALUE_STRING:
      setCurrentEventType(EventType.STRING).cacheCurrentValue();
      break;
    case VALUE_NUMBER_INT:
    case VALUE_NUMBER_FLOAT:
      setCurrentEventType(EventType.DOUBLE).cacheCurrentValue();
      break;
    default:
      throw new DecodingException(
          "Encountered unexpected token of type: " + currentToken);
    }

    if (!inMap()
        && currentEvent != EventType.END_MAP
        && currentEvent != EventType.START_ARRAY
        && currentEvent != EventType.END_ARRAY) {
      // if traversing an array, increment the index
      currentContainer.incrementIndex();
    }

    if (currentEvent == EventType.START_MAP) {
      mapLevel++;
    } else if (currentEvent == EventType.END_MAP) {
      mapLevel--;
    }
    if (mapLevel == 0) {
      eor = true;
    }

    return currentEvent;
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
    JsonToken token;
    while((token = nextToken()) != null) {
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
  }

  @Override
  public boolean inMap() {
    return currentContainer == null
        || currentContainer.getType() == Type.MAP;
  }

  @Override
  public String getFieldName() {
    if (!inMap()) {
      throw new IllegalStateException("Not traversing a map!");
    }
    return fieldName;
  }

  @Override
  public int getArrayIndex() {
    if (inMap()) {
      throw new IllegalStateException("Not traversing an array!");
    }
    return currentContainer.getIndex();
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
    return ((OTimestamp) currentObjValue).getMillis();
  }

  @Override
  public OTimestamp getTimestamp() {
    checkEventType(EventType.TIMESTAMP);
    return (OTimestamp) currentObjValue;
  }

  @Override
  public int getDateInt() {
    checkEventType(EventType.DATE);
    return ((ODate) currentObjValue).toDaysSinceEpoch();
  }

  @Override
  public ODate getDate() {
    checkEventType(EventType.DATE);
    return (ODate) currentObjValue;
  }

  @Override
  public int getTimeInt() {
    checkEventType(EventType.TIME);
    return ((OTime) currentObjValue).toTimeInMillis();
  }

  @Override
  public OTime getTime() {
    checkEventType(EventType.TIME);
    return (OTime) currentObjValue;
  }

  @Override
  public OInterval getInterval() {
    checkEventType(EventType.INTERVAL);
    return new OInterval(currentLongValue);
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

  private void updateCurrentContainer() {
    currentContainer = containerStack.isEmpty() ? null : containerStack.peek();
  }

  protected JsonToken peekToken() {
    if (hasMoreTokens()) {
      return cachedTokens.peek();
    }
    throw new DecodingException("No more Json tokens.");
  }

  protected JsonToken nextToken() {
    if (hasMoreTokens()) {
      return cachedTokens.remove();
    }
    throw new DecodingException("No more Json tokens.");
  }

  protected boolean hasMoreTokens() {
    try {
      if (!cachedTokens.isEmpty()) return true;
      JsonToken token = getParser().nextToken();
      if (token == null) return false;
      /*
       * if the data is in array of documents format, the first token encountered will be
       * START_ARRAY and the last token will be a STOP_ARRAY. If we are not using array format,
       * we will encounter START_MAP.
       */
      if (mapLevel == 0) {
        if (token == JsonToken.START_ARRAY) {
          token = getParser().nextToken();
        }
        if (token == JsonToken.END_ARRAY) {
          token = getParser().nextToken();
          /*
           * If we encounter a new token after end of an array of document, we will expect
           * this to be either a START_MAP (start of a new document) or START_ARRAY (new array
           * of documents). If it's a START_MAP, continue by caching the token. If it's
           * START_ARRAY, we need to get the next token from stream.
           */
          if (token == JsonToken.START_ARRAY) {
            token = getParser().nextToken();
          }
        }
        /* stream is exhausted */
        if (token == null) return false;
      }
      cachedTokens.add(token);
      return true;
    } catch (IOException e) {
      throw new DecodingException(e);
    }
  }

  protected String getCurrentName() {
    try {
      return getParser().getCurrentName();
    } catch (IOException ie) {
      throw new DecodingException(ie);
    }
  }

  protected Object getCurrentObj() {
    return currentObjValue;
  }

  protected JsonStreamDocumentReader setArrayIndex(int index) {
    if (inMap()) {
      throw new IllegalStateException("Not traversing an array!");
    }
    currentContainer.setIndex(index);
    return this;
  }

  protected JsonStreamDocumentReader setFieldName(String fieldName) {
    if (!inMap()) {
      throw new IllegalStateException("Not traversing a map!");
    }
    this.fieldName = fieldName;
    return this;
  }

  protected JsonStreamDocumentReader setCurrentObj(Object obj) {
    this.currentObjValue = obj;
    return this;
  }

  protected EventType getCurrentEventType() {
    return currentEvent;
  }

  protected JsonStreamDocumentReader setCurrentEventType(EventType eventType) {
    this.currentEvent = eventType;
    return this;
  }

  protected long getCurrentLongValue() {
    return currentLongValue;
  }

  protected JsonStreamDocumentReader setCurrentLongValue(long value) {
    this.currentLongValue = value;
    return this;
  }

  protected double getCurrentDoubleValue() {
    return currentDoubleValue;
  }

  protected JsonStreamDocumentReader setCurrentDoubleValue(double value) {
    this.currentDoubleValue = value;
    return this;
  }

  protected boolean isExtended() {
    return isExtended;
  }

  @Override
  public EventType getCurrentEvent() {
    return currentEvent;
  }

}
