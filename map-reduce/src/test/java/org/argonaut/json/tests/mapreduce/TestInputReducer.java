package org.argonaut.json.tests.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.argonaut.json.JsonRecord;

public class TestInputReducer extends
Reducer<LongWritable, JsonRecord, LongWritable, JsonRecord> {

  protected void reduce(
      LongWritable val,
      JsonRecord value,
      Reducer<LongWritable, JsonRecord, LongWritable, JsonRecord>.Context context)
          throws IOException, InterruptedException {

    context.write(val, value);
  }

}
