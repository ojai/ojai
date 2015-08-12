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
package org.argonaut.json.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.argonaut.Document;

public class JSONFileInputFormat extends FileInputFormat<LongWritable, Document> {

  @Override
  public RecordReader<LongWritable, Document> createRecordReader(InputSplit arg0,
      TaskAttemptContext arg1) throws IOException, InterruptedException {

    return new JSONFileRecordReader();

  }

  @Override
  public boolean isSplitable(JobContext context, Path path) {

    /*
     * define a config parameter to determine if we want to make it not
     * splittable.
     */
    Configuration conf = context.getConfiguration();
    if (conf.get("jsonfileinputformat.nosplit") != null) {
      return false;
    }

    return true;
  }

}
