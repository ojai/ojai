package org.argonaut.json.mapreduce;

import java.io.OutputStream;

import org.argonaut.json.JsonRecordWriter;

class JSONFileRecordWriter extends JsonRecordWriter {
  JSONFileRecordWriter(OutputStream out) {
    super(out);
  }
}
