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
package org.ojai.tests.beans.model;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.ojai.json.impl.JsonUtils;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

import com.google.common.collect.ImmutableList;

public class BeanWithAllTypes {

  private Object nullT;
  private boolean booleanT;
  private String stringT;
  private byte byteT;
  private short shortT;
  private int intT;
  private long longT;
  private float floatT;
  private double doubleT;
  private BigDecimal bigDecimalT;
  private ODate dateT;
  private OTime timeT;
  private OTimestamp timestampT;
  private OInterval intervalT;
  private ByteBuffer byteBufferT;
  private int[] intArrayT;
  private List<? extends Object> objectListT;
  private ChildObject childObjectT;

  public BeanWithAllTypes() {
  }

  public BeanWithAllTypes init() {
    nullT = null;
    booleanT = true;
    stringT = "sample string";
    byteT = Byte.MAX_VALUE;
    shortT = Short.MAX_VALUE;
    intT = Integer.MAX_VALUE;
    longT = Long.MAX_VALUE;
    floatT = Float.MAX_VALUE;
    doubleT = Double.MAX_VALUE;
    bigDecimalT = new BigDecimal("12345678901234567890.123456789");
    dateT = ODate.parse("2015-12-31");
    timeT = OTime.parse("23:59:59");
    timestampT = OTimestamp.parse("2015-06-29T12:33:22.000Z");
    intervalT = new OInterval(1, 6, 25, 32569, 265);
    byteBufferT = ByteBuffer.wrap(JsonUtils.getBytes("sample binary data"));
    intArrayT = new int[] {0, 1, 1, 2, 3, 5, 8, 13, 21, 34};
    objectListT = ImmutableList.of(1, 23.5, 12.0f, "hello", new ChildObject());
    childObjectT = new ChildObject();
    return this;
  }

  public boolean isBooleanT() {
    return booleanT;
  }

  public void setBooleanT(boolean booleanT) {
    this.booleanT = booleanT;
  }

  public String getStringT() {
    return stringT;
  }

  public void setStringT(String stringT) {
    this.stringT = stringT;
  }

  public byte getByteT() {
    return byteT;
  }

  public void setByteT(byte byteT) {
    this.byteT = byteT;
  }

  public short getShortT() {
    return shortT;
  }

  public void setShortT(short shortT) {
    this.shortT = shortT;
  }

  public int getIntT() {
    return intT;
  }

  public void setIntT(int intT) {
    this.intT = intT;
  }

  public long getLongT() {
    return longT;
  }

  public void setLongT(long longT) {
    this.longT = longT;
  }

  public float getFloatT() {
    return floatT;
  }

  public void setFloatT(float floatT) {
    this.floatT = floatT;
  }

  public double getDoubleT() {
    return doubleT;
  }

  public void setDoubleT(double doubleT) {
    this.doubleT = doubleT;
  }

  public BigDecimal getBigDecimalT() {
    return bigDecimalT;
  }

  public void setBigDecimalT(BigDecimal bigDecimalT) {
    this.bigDecimalT = bigDecimalT;
  }

  public ODate getDateT() {
    return dateT;
  }

  public void setDateT(ODate dateT) {
    this.dateT = dateT;
  }

  public OTime getTimeT() {
    return timeT;
  }

  public void setTimeT(OTime timeT) {
    this.timeT = timeT;
  }

  public OTimestamp getTimestampT() {
    return timestampT;
  }

  public void setTimestampT(OTimestamp timestampT) {
    this.timestampT = timestampT;
  }

  public OInterval getIntervalT() {
    return intervalT;
  }

  public void setIntervalT(OInterval intervalT) {
    this.intervalT = intervalT;
  }

  public ByteBuffer getByteBufferT() {
    return byteBufferT;
  }

  public void setByteBufferT(ByteBuffer byteBufferT) {
    this.byteBufferT = byteBufferT;
  }

  public int[] getIntArrayT() {
    return intArrayT;
  }

  public void setIntArrayT(int[] intArrayT) {
    this.intArrayT = intArrayT;
  }

  public List<? extends Object> getObjectListT() {
    return objectListT;
  }

  public void setObjectListT(List<? extends Object> objectListT) {
    this.objectListT = objectListT;
  }

  public ChildObject getChildObjectT() {
    return childObjectT;
  }

  public void setChildObjectT(ChildObject childObjectT) {
    this.childObjectT = childObjectT;
  }

  public Object getNullT() {
    return nullT;
  }

  public void setNullT(Object nullT) {
    this.nullT = nullT;
  }

  static class ChildObject {
    private int[] intArrayT = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34};

    public int[] getIntArrayT() {
      return intArrayT;
    }

    public void setIntArrayT(int[] intArrayT) {
      this.intArrayT = intArrayT;
    }

    @Override
    public String toString() {
      return "{intArrayT: " + Arrays.toString(intArrayT) + "}";
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(intArrayT);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ChildObject other = (ChildObject) obj;
      if (!Arrays.equals(intArrayT, other.intArrayT))
        return false;
      return true;
    }
  }

  @Override
  public String toString() {
    return "{nullT: " + nullT + ", booleanT: " + booleanT + ", stringT: " + stringT
        + ", byteT: " + byteT + ", shortT: " + shortT + ", intT: " + intT + ", longT: " + longT
        + ", floatT: " + floatT + ", doubleT: " + doubleT + ", bigDecimalT: " + bigDecimalT
        + ", dateT: " + dateT + ", timeT: " + timeT + ", timestampT: " + timestampT + ", intervalT: "
        + intervalT + ", byteBufferT: " + byteBufferT + ", intArrayT: " + Arrays.toString(intArrayT)
        + ", objectListT: " + objectListT + ", childObjectT: " + childObjectT + "}";
  }

}
