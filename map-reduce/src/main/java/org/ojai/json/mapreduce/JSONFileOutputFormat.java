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
package org.ojai.json.mapreduce;

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;
import org.ojai.Document;

public class JSONFileOutputFormat extends
FileOutputFormat<LongWritable, Document> {

  @Override
  public RecordWriter<LongWritable, Document> getRecordWriter(
      TaskAttemptContext job) throws IOException, InterruptedException {

    Configuration conf = job.getConfiguration();
    boolean isCompressed = getCompressOutput(job);
    CompressionCodec codec = null;
    String extension = "";
    if (isCompressed) {
      Class<? extends CompressionCodec> codecClass = getOutputCompressorClass(
          job, GzipCodec.class);
      codec = ReflectionUtils.newInstance(codecClass, conf);
      extension = codec.getDefaultExtension();
    }
    Path path = getDefaultWorkFile(job, extension);
    FileSystem fs = path.getFileSystem(conf);
    FSDataOutputStream out = fs.create(path, false);
    if (!isCompressed) {
      return new JSONFileOutputRecordWriter(out);
    } else {
      return new JSONFileOutputRecordWriter(new DataOutputStream(
          codec.createOutputStream(out)));
    }
  }

}
