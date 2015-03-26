package org.jackhammer.tests;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

public class BaseTest {

  @Rule public TestName TEST_NAME = new TestName();
  @Before
  public void printID() throws Exception {
    System.out.printf("Running %s#%s\n", getClass().getName(), TEST_NAME.getMethodName());
  }

}
