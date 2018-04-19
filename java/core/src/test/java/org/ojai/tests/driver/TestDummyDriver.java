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
package org.ojai.tests.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ojai.json.JsonConsts;
import org.ojai.json.impl.store.JsonDriver;
import org.ojai.store.Driver;
import org.ojai.store.DriverManager;
import org.ojai.tests.BaseTest;

public class TestDummyDriver extends BaseTest {

  @Test
  public void testDummyDriver() {
    Driver driver = DriverManager.getDriver(JsonConsts.BASE_URL);
    assertNotNull(driver);
    assertTrue(driver instanceof JsonDriver);
    assertEquals(JsonConsts.DRIVER_NAME, driver.getName());
  }

}
