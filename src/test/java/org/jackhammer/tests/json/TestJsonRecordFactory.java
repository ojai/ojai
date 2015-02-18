package org.jackhammer.tests.json;

import org.jackhammer.RecordWriter;
import org.jackhammer.json.JsonRecordFactory;
import org.junit.Ignore;
import org.junit.Test;

public class TestJsonRecordFactory {

  @Ignore @Test
  public void test() {
    RecordWriter builder = new JsonRecordFactory()
      .newRecordBuilder();
    builder.build();
  }

}
