package org.jackhammer.tests.json;

import java.io.IOException;
import java.io.InputStream;

import org.jackhammer.RecordReader;
import org.jackhammer.json.JsonRecordStream;
import org.jackhammer.json.JsonRecordWriter;
import org.jackhammer.json.JsonUtils;
import org.jackhammer.tests.BaseTest;
import org.junit.Test;

public class TestJsonUtil extends BaseTest {

  @Test
  public void testJsonSerialization() throws IOException {
    try (InputStream in = getJsonStream("multirecord.json");
        JsonRecordStream stream = new JsonRecordStream(in)) {
      for (RecordReader reader : stream.recordReaders()) {
        JsonRecordWriter writer = new JsonRecordWriter();
        JsonUtils.writeToStreamFromReader(reader, writer);
        writer.build();
        System.out.println(writer.asUTF8String());
      }
    }
  }

}
