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
import org.ojai.Document;
import org.ojai.json.Json;
import org.ojai.json.impl.JsonDocumentStream;

public class JSONFileRecordReader extends RecordReader<LongWritable, Document> {

  private FSDataInputStream inputStream;
  private JsonDocumentStream documentStream;
  private Iterator<Document> it;
  private long documentCount;
  private LongWritable key = null;
  private Document document;
  private long currentPos;
  private long start;
  private long end;
  private long blockLength;

  @Override
  public void close() throws IOException {
    try {
      documentStream.close();
    } catch (Exception e) {
      throw new IOException(
          "Error closing document Stream in JsonFileRecordReader");
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


    /*
     * if this block is not the first block check if it falls on document
     * boundary. If not, skip bytes to start to the next document boundary.
     */
    start = split.getStart();
    blockLength = split.getLength();
    long skipBytes = 0;

    if (start != 0) {
      /*
       * not the first block check if it starts on a document boundary
       */
      skipBytes = bytesToSkip(start, blockLength);
      currentPos = start - 1 + skipBytes;
      inputStream.seek(currentPos);
    }

    /* Initialize a stream reader so that it can read multiple documents from */
    /* the file */

    documentStream = (JsonDocumentStream)Json.newDocumentStream(inputStream);
    it = documentStream.iterator();

  }

  @Override
  public boolean nextKeyValue() throws IOException, InterruptedException {
    boolean hasNextKeyVal = false;

    long thisPos = documentStream.getInputStreamPosition();
    if (thisPos >= (start + blockLength)) {
      return false;
    }

    if (it.hasNext()) {
      key.set(documentCount);
      document = it.next();
      documentCount++;
      hasNextKeyVal = true;
    }

    return hasNextKeyVal;
  }

}
