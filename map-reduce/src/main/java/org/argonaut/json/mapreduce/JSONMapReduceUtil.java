package org.argonaut.json.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.argonaut.json.impl.JsonRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONMapReduceUtil {
  final Logger LOG = LoggerFactory.getLogger(JSONMapReduceUtil.class);

  public static void initMapperJob(Class<? extends Mapper> mapper, Job job) {
    Configuration conf = job.getConfiguration();
    conf.setStrings("io.serializations", conf.get("io.serializations"),
        JSONRecordSerialization.class.getName());
    if (mapper != null) {
      job.setMapperClass(mapper);
    }
    job.setInputFormatClass(JSONFileInputFormat.class);
    job.setMapOutputValueClass(JsonRecord.class);

  }

  public static void initReducerJob(Class<? extends Reducer> reducer, Job job) {
    Configuration conf = job.getConfiguration();
    if (reducer != null) {
      job.setReducerClass(reducer);
    }
    conf.setStrings("io.serializations", conf.get("io.serializations"),
        JSONRecordSerialization.class.getName());
    job.setOutputFormatClass(JSONFileOutputFormat.class);
    job.setOutputValueClass(JsonRecord.class);

  }
}
