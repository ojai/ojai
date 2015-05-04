package org.jackhammer.json.mapreduce;

import java.io.OutputStream;

import org.jackhammer.json.JsonRecordWriter;

class JSONFileRecordWriter extends JsonRecordWriter {
  JSONFileRecordWriter(OutputStream out) {
    super(out);
  }
}
