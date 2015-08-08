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
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.argonaut.Record;
import org.argonaut.RecordStream;
import org.argonaut.json.Json;

public class JSONFileRecordReader extends RecordReader<LongWritable, Record> {

  private FSDataInputStream inputStream;
  private RecordStream<Record> recordStream;
  private Iterator<Record> it;
  private long recordCount;
  private LongWritable key = null;
  private Record record;
  private long currentPos;
  private long start;
  private long end;
  private long blockLength;

  @Override
  public void close() throws IOException {
    try {
      recordStream.close();
    } catch (Exception e) {
      throw new IOException(
          "Error closing record Stream in JsonFileRecordReader");
    }
    if (inputStream != null) {
      inputStream.close();
    }
  }

  @Override
  public LongWritable getCurrentKey() throws IOException, InterruptedException {
    return key;
  }

  @Override
  public Record getCurrentValue() throws IOException, InterruptedException {
    return record;
  }

  @Override
  public float getProgress() throws IOException, InterruptedException {
    if (start == end) {
      return 0.0F;
    }
    return Math.min(1.0F, (float)(currentPos - start) / (float)(end - start));
  }

  private long bytesToSkip(long start, long blockLength)
      throws IOException {
    long toSkip = 0;
    inputStream.seek(start - 1);

    //create InputStreamReader
    InputStreamReader in = new InputStreamReader(inputStream, "UTF-8");
    boolean gotStart = false;
    char curChar;
    while (toSkip <= blockLength) {
      curChar = (char)in.read();
      if (curChar == '}') {
        gotStart = true;
      }
      if (curChar == '{') {
        if (gotStart) {
          break;
        }
      }
      if (curChar == ',') {
        gotStart = false;
      }
      toSkip += 1;
    }

    return toSkip;
  }

  @Override
  public void initialize(InputSplit arg0, TaskAttemptContext taskContext)
      throws IOException, InterruptedException {

    recordStream = null;
    it = null;
    recordCount = 0;
    key = new LongWritable();
    record = null;
    currentPos = 0;

    /* get the split */
    FileSplit split = (FileSplit) arg0;

    /* get configuration object */
    Configuration job = taskContext.getConfiguration();

    /* initialize file /input stream */
    final Path path = split.getPath();
    FileSystem fs = path.getFileSystem(job);
    inputStream = fs.open(path);


    /*
     * if this block is not the first block check if it falls on record
     * boundary. If not, skip bytes to start to the next record boundary.
     */
    start = split.getStart();
    blockLength = split.getLength();
    long skipBytes = 0;

    if (start != 0) {
      /*
       * not the first block check if it starts on a record boundary
       */
      skipBytes = bytesToSkip(start, blockLength);
      currentPos = start - 1 + skipBytes;
      inputStream.seek(currentPos);
    }

    /* Initialize a stream reader so that it can read multiple records from */
    /* the file */

    recordStream = Json.newRecordStream(inputStream);
    it = recordStream.iterator();

  }

  @Override
  public boolean nextKeyValue() throws IOException, InterruptedException {
    boolean hasNextKeyVal = false;

    long thisPos = inputStream.getPos();
    if (thisPos >= (start + blockLength)) {
      return false;
    }

    if (it.hasNext()) {
      key.set(recordCount);
      record = it.next();
      recordCount++;
      hasNextKeyVal = true;
    }

    return hasNextKeyVal;
  }

}
