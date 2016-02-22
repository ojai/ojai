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
package org.ojai.tests.beans;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.ojai.Document;
import org.ojai.Value.Type;
import org.ojai.json.Json;
import org.ojai.tests.beans.model.BeanWithAllTypes;
import org.ojai.tests.beans.model.User;

public class TestJavaBeans {

  @Test
  public void testJavaBeansWithAllTypes() {
    BeanWithAllTypes bean = new BeanWithAllTypes().init();
    Document doc = Json.newDocument(bean);
    BeanWithAllTypes newBean = doc.toJavaBean(BeanWithAllTypes.class);

    assertEquals(Type.NULL, doc.getValue("nullT").getType());
    assertEquals(bean.getNullT(), newBean.getNullT());

    assertEquals(bean.isBooleanT(), doc.getBoolean("booleanT"));
    assertEquals(bean.isBooleanT(), newBean.isBooleanT());

    assertEquals(bean.getStringT(), doc.getString("stringT"));
    assertEquals(bean.getStringT(), newBean.getStringT());

    assertEquals(bean.getByteT(), doc.getByte("byteT"));
    assertEquals(bean.getByteT(), newBean.getByteT());

    assertEquals(bean.getShortT(), doc.getShort("shortT"));
    assertEquals(bean.getShortT(), newBean.getShortT());

    assertEquals(bean.getIntT(), doc.getInt("intT"));
    assertEquals(bean.getIntT(), newBean.getIntT());

    assertEquals(bean.getLongT(), doc.getLong("longT"));
    assertEquals(bean.getLongT(), newBean.getLongT());

    assertEquals(bean.getFloatT(), doc.getDouble("floatT"), 0);
    assertEquals(bean.getFloatT(), newBean.getFloatT(), 0);

    assertEquals(bean.getDoubleT(), doc.getDouble("doubleT"), 0);
    assertEquals(bean.getDoubleT(), newBean.getDoubleT(), 0);

    assertEquals(bean.getBigDecimalT(), doc.getDecimal("bigDecimalT"));
    assertEquals(bean.getBigDecimalT(), newBean.getBigDecimalT());

    assertEquals(bean.getDateT(), doc.getDate("dateT"));
    assertEquals(bean.getDateT(), newBean.getDateT());

    assertEquals(bean.getTimeT().toString(), doc.getTime("timeT").toString());
    assertEquals(bean.getTimeT().toString(), newBean.getTimeT().toString());

    assertEquals(bean.getTimestampT(), doc.getTimestamp("timestampT"));
    assertEquals(bean.getTimestampT(), newBean.getTimestampT());

    assertEquals(bean.getIntervalT(), doc.getInterval("intervalT"));
    assertEquals(bean.getIntervalT(), newBean.getIntervalT());

    assertEquals(bean.getByteBufferT(), doc.getBinary("byteBufferT"));
    assertEquals(bean.getByteBufferT(), newBean.getByteBufferT());

    assertArrayEquals(bean.getIntArrayT(), newBean.getIntArrayT());

    assertEquals(bean.getChildObjectT(), newBean.getChildObjectT());

    assertEquals(bean.getBigDecimalT(), doc.getDecimal("bigDecimalT"));
    assertEquals(bean.getBigDecimalT(), newBean.getBigDecimalT());
  }

  @Test
  public void testMultiLevelJavaBeans() {
    String jsonString = "{" +
            " \"_id\" : \"1001\"," +
            " \"first_name\" : \"John\"," +
            " \"last_name\" : \"Doe\"," +
            " \"active\" : false," +
            " \"age\" : 35," +
            " \"interests\" : [\"sports\",\"computers\"]," +
            " \"account\" : 1234.4321," +
            " \"address\" : {" +
            "     \"street\" : \"1015 15th av\"," +
            "     \"city\" : \"SFO\"," +
            "     \"zip\" : 94065" +
            "    }" +
            "}";

    Document doc = Json.newDocument(jsonString);
    User user = doc.toJavaBean(User.class);
    Document userDoc = Json.newDocument(user);

    assertNotEquals(doc, userDoc);

    assertEquals(doc.getString("first_name"), userDoc.getString("first_name"));
    assertEquals(user.getFirstName(), userDoc.getString("first_name"));

    assertEquals(doc.getString("last_name"), userDoc.getString("last_name"));
    assertEquals(user.getLastName(), userDoc.getString("last_name"));

    assertEquals(doc.getDouble("age"), userDoc.getInt("age"), 0.0);
    assertEquals(user.getAge(), userDoc.getInt("age"), 0.0);

    assertEquals(doc.getString("address.city"), userDoc.getString("address.city"));
    assertEquals(user.getAddress().getTown(), userDoc.getString("address.city"));

    Document addressDoc = Json.newDocument(user.getAddress());
    assertEquals(addressDoc, userDoc.getMap("address"));
  }

}
