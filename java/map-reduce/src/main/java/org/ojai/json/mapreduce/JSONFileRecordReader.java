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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.Decompressor;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.ojai.Document;
import org.ojai.json.Json;
import org.ojai.json.impl.JsonDocumentStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONFileRecordReader extends RecordReader<LongWritable, Document> {

  private static Logger LOG = LoggerFactory.getLogger(JSONFileRecordReader.class);
  private InputStream inputStream;
  private JsonDocumentStream documentStream;
  private Iterator<Document> it;
  private long documentCount;
  private LongWritable key = null;
  private Document document;
  private long currentPos;
  private long start;
  private long end;
  private Decompressor decompressor;

  @Override
  public void close() throws IOException {
    try {
      documentStream.close();
    } catch (Exception e) {
      throw new IOException(
          "Error closing document Stream in JsonFileRecordReader");
    }
    try {
      if (inputStream != null) {
        inputStream.close();
      }
    } finally {
      if (decompressor != null) {
        CodecPool.returnDecompressor(decompressor);
        decompressor = null;
      }
    }
  }

  @Override
  public LongWritable getCurrentKey() throws IOException, InterruptedException {
    return key;
  }

  @Override
  public Document getCurrentValue() throws IOException, InterruptedException {
    return document;
  }

  @Override
  public float getProgress() throws IOException, InterruptedException {
    if (start == end) {
      return 0.0F;
    }
    return Math.min(1.0F, (float)(currentPos - start) / (float)(end - start));
  }


  @Override
  public void initialize(InputSplit arg0, TaskAttemptContext taskContext)
      throws IOException, InterruptedException {

    documentStream = null;
    it = null;
    documentCount = 0;
    key = new LongWritable();
    document = null;
    currentPos = 0;

    /* get the split */
    FileSplit split = (FileSplit) arg0;

    /* get configuration object */
    Configuration job = taskContext.getConfiguration();

    /* initialize file /input stream */
    final Path path = split.getPath();
    FileSystem fs = path.getFileSystem(job);
    inputStream = fs.open(path);

    CompressionCodec codec = new CompressionCodecFactory(job).getCodec(path);

    if (codec != null) {
      decompressor = CodecPool.getDecompressor(codec);
      inputStream = codec.createInputStream(inputStream, decompressor);
    }

    start = split.getStart();
    end = start + split.getLength();

    /* Initialize a stream reader so that it can read multiple documents from */
    /* the file */

    documentStream = (JsonDocumentStream)Json.newDocumentStream(inputStream);
    it = documentStream.iterator();

  }

  @Override
  public boolean nextKeyValue() throws IOException, InterruptedException {
    boolean hasNextKeyVal = false;

    if (it.hasNext()) {
      key.set(documentCount);
      document = it.next();
      documentCount++;
      hasNextKeyVal = true;
      currentPos = documentStream.getInputStreamPosition();
    }

    return hasNextKeyVal;
  }

}
