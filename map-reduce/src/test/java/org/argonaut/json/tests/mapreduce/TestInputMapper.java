package org.argonaut.json.tests.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.argonaut.json.impl.JsonRecord;

public class TestInputMapper extends
Mapper<LongWritable, JsonRecord, LongWritable, JsonRecord> {

  @Override
  public void map(LongWritable key, JsonRecord value, Context context)
      throws IOException, InterruptedException {
    LongWritable l = new LongWritable(1);
    context.write(l, value);
  }

}
