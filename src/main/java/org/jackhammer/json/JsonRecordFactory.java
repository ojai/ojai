package org.jackhammer.json;

import org.jackhammer.RecordWriter;
import org.jackhammer.Record;
import org.jackhammer.RecordFactory;

public class JsonRecordFactory implements RecordFactory {

  @Override
  public Record newRecord() {
    // TODO Auto-generated method stub
    return new JsonRecord();
  }

  @Override
  public RecordWriter newRecordBuilder() {
    // TODO Auto-generated method stub
    return new JsonRecordBuilder();
  }

}
