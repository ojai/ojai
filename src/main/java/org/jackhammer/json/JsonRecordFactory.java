package org.jackhammer.json;

import org.jackhammer.Record;
import org.jackhammer.Record.Builder;
import org.jackhammer.RecordFactory;

public class JsonRecordFactory implements RecordFactory {

  @Override
  public Record newRecord() {
    // TODO Auto-generated method stub
    return new JsonRecord();
  }

  @Override
  public Builder newRecordBuilder() {
    // TODO Auto-generated method stub
    return new JsonRecordBuilder();
  }

}
