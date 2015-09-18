/**
 * Copyright (c) 2014 MapR, Inc.
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
package org.ojai.json.tests.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.ojai.json.mapreduce.JSONMapReduceUtil;

public class TestJsonMapperReducer extends Configured implements Tool {

  public int run(String[] args) throws Exception {
    Configuration conf = getConf();
    Job job = Job.getInstance(conf);
    job.setJarByClass(getClass());
    job.setJobName(getClass().getSimpleName());
    job.setOutputKeyClass(LongWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    JSONMapReduceUtil.initMapperJob(TestInputMapper.class, job);
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    JSONMapReduceUtil.initReducerJob(TestInputReducer.class, job);

    return job.waitForCompletion(true) ? 0 : 1;

  }

  public static void main(String[] args) throws Exception {
    int rc = ToolRunner.run(new TestJsonMapperReducer(), args);
    System.exit(rc);
  }

}
