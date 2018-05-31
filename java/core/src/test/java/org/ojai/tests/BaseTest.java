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
package org.ojai.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.ojai.DocumentReader;
import org.ojai.DocumentReader.EventType;
import org.ojai.annotation.API;

@API.Public
public class BaseTest {

  @Rule public TestName TEST_NAME = new TestName();
  @Before
  public void printID() throws Exception {
    System.out.printf("Running %s#%s\n", getClass().getName(), TEST_NAME.getMethodName());
  }

  public InputStream getJsonStream(String resourceName) {
    return getClass().getClassLoader().getResourceAsStream(resourceName);
  }

  public static void assertArrayEvent(DocumentReader r, EventType expectedEt, int expectedArrayIndex) {
    EventType et = null;
    assertNotNull((et = r.next()));
    assertTrue(!r.inMap());
    assertEquals(expectedEt, et);
    assertEquals(expectedArrayIndex, r.getArrayIndex());
  }

  public static void assertMapEvent(DocumentReader r, EventType expectedEt, String expectedFieldName) {
    EventType et = null;
    assertNotNull((et = r.next()));
    assertTrue(r.inMap());
    assertEquals(expectedEt, et);
    assertEquals(expectedFieldName, r.getFieldName());
  }

  @FunctionalInterface
  public interface OperationWithException {
    public void apply();
  }

  protected void expectException(
      final Class<? extends Throwable> expectedException, final OperationWithException function) {
    boolean exception = true;
    try {
      function.apply();
      exception = false;
    } catch (final Throwable t) {
      if (!t.getClass().isAssignableFrom(expectedException)) {
        t.printStackTrace();
        fail("An unexpected exception was thrown.");
      }
    } finally {
      if (!exception) {
        fail("Expected the test to throw: " + expectedException.getTypeName());
      }
    }
    
  }

}
