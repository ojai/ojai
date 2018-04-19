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
package org.ojai.tests;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import org.junit.Test;
import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.json.Json;
import org.ojai.store.ValueBuilder;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class TestValueBuilder extends BaseTest {

  @Test
  public void testDBValueBuilder() {
    Value value = null;
    ValueBuilder valueBuilder = Json.getValueBuilder();

    value = valueBuilder.newNullValue();
    assertEquals(Type.NULL, value.getType());

    boolean bool = true;
    value = valueBuilder.newValue(bool);
    assertEquals(Type.BOOLEAN, value.getType());
    assertEquals(bool, value.getBoolean());

    String string = "string";
    value = valueBuilder.newValue(string);
    assertEquals(Type.STRING, value.getType());
    assertEquals(string, value.getString());

    value = valueBuilder.newValue(Byte.MAX_VALUE);
    assertEquals(Type.BYTE, value.getType());
    assertEquals(Byte.MAX_VALUE, value.getByte());

    value = valueBuilder.newValue(Short.MAX_VALUE);
    assertEquals(Type.SHORT, value.getType());
    assertEquals(Short.MAX_VALUE, value.getShort());

    value = valueBuilder.newValue(Integer.MAX_VALUE);
    assertEquals(Type.INT, value.getType());
    assertEquals(Integer.MAX_VALUE, value.getInt());

    value = valueBuilder.newValue(Long.MAX_VALUE);
    assertEquals(Type.LONG, value.getType());
    assertEquals(Long.MAX_VALUE, value.getLong());

    value = valueBuilder.newValue(Float.MAX_VALUE);
    assertEquals(Type.FLOAT, value.getType());
    assertEquals(Float.MAX_VALUE, value.getFloat(), 0);

    value = valueBuilder.newValue(Double.MAX_VALUE);
    assertEquals(Type.DOUBLE, value.getType());
    assertEquals(Double.MAX_VALUE, value.getDouble(), 0);

    value = valueBuilder.newValue(BigDecimal.ONE);
    assertEquals(Type.DECIMAL, value.getType());
    assertEquals(BigDecimal.ONE, value.getDecimal());

    ODate date = ODate.fromDaysSinceEpoch(1);
    value = valueBuilder.newValue(date);
    assertEquals(Type.DATE, value.getType());
    assertEquals(date, value.getDate());

    OTime time = OTime.fromMillisOfDay(0);
    value = valueBuilder.newValue(time);
    assertEquals(Type.TIME, value.getType());
    assertEquals(time, value.getTime());

    OTimestamp timestamp = OTimestamp.parse("2015-06-29T12:33:22.000Z");
    value = valueBuilder.newValue(timestamp);
    assertEquals(Type.TIMESTAMP, value.getType());
    assertEquals(timestamp, value.getTimestamp());

    OInterval interval = new OInterval(0);
    value = valueBuilder.newValue(interval);
    assertEquals(Type.INTERVAL, value.getType());
    assertEquals(interval, value.getInterval());

    byte[] byteArray = new byte[] {0};
    value = valueBuilder.newValue(byteArray);
    assertEquals(Type.BINARY, value.getType());
    assertEquals(ByteBuffer.wrap(byteArray), value.getBinary());

    ByteBuffer byteBuff = ByteBuffer.allocate(0);
    value = valueBuilder.newValue(byteBuff);
    assertEquals(Type.BINARY, value.getType());
    assertEquals(byteBuff, value.getBinary());

    ImmutableMap<String, Object> map = ImmutableMap.of("key", 25);
    value = valueBuilder.newValue(map);
    assertEquals(Type.MAP, value.getType());
    assertEquals(map, value.getMap());

    ImmutableList<Object> list = ImmutableList.of(1, "abc", 2.5, true);
    value = valueBuilder.newValue(list);
    assertEquals(Type.ARRAY, value.getType());
    assertEquals(list, value.getList());
  }

}
