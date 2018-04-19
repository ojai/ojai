/**
 * Copyright (c) 2018 MapR, Inc.
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

import static org.junit.Assert.fail;

import org.junit.Test;
import org.ojai.JsonString;
import org.ojai.json.JsonOptions;

public class TestJsonString extends BaseTest {

  @Test
  public void testDefaultJsonString() {
    JsonString derived = new JsonString() {};

    try {
      derived.asJsonString();
      fail();
    } catch (UnsupportedOperationException e) {}

    try {
      derived.asJsonString(JsonOptions.DEFAULT);
      fail();
    } catch (UnsupportedOperationException e) {}
  }

}
