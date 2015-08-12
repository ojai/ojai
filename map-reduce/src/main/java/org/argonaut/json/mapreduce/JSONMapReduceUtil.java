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
package org.argonaut.json.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.argonaut.json.impl.JsonDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONMapReduceUtil {
  final Logger LOG = LoggerFactory.getLogger(JSONMapReduceUtil.class);

  @SuppressWarnings("rawtypes")
  public static void initMapperJob(Class<? extends Mapper> mapper, Job job) {
    Configuration conf = job.getConfiguration();
    conf.setStrings("io.serializations", conf.get("io.serializations"),
        JSONDocumentSerialization.class.getName());
    if (mapper != null) {
      job.setMapperClass(mapper);
    }
    job.setInputFormatClass(JSONFileInputFormat.class);
    job.setMapOutputValueClass(JsonDocument.class);

  }

  @SuppressWarnings("rawtypes")
  public static void initReducerJob(Class<? extends Reducer> reducer, Job job) {
    Configuration conf = job.getConfiguration();
    if (reducer != null) {
      job.setReducerClass(reducer);
    }
    conf.setStrings("io.serializations", conf.get("io.serializations"),
        JSONDocumentSerialization.class.getName());
    job.setOutputFormatClass(JSONFileOutputFormat.class);
    job.setOutputValueClass(JsonDocument.class);

  }

}
