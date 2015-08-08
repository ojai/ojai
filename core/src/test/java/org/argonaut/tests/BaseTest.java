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
package org.argonaut.tests;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

public class BaseTest {

  @Rule public TestName TEST_NAME = new TestName();
  @Before
  public void printID() throws Exception {
    System.out.printf("Running %s#%s\n", getClass().getName(), TEST_NAME.getMethodName());
  }

  public InputStream getJsonStream(String resourceName) {
    return getClass().getClassLoader().getResourceAsStream(resourceName);
  }

}
