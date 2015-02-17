package org.jackhammer.json;

import org.jackhammer.Record;
import org.jackhammer.Record.Builder;
import org.jackhammer.RecordFactory;

public class JsonRecordFactory implements RecordFactory {

  boolean singleLine = true;
  @Override
  public Builder newStreamBuilder() {
    return new JsonRecordBuilder(singleLine);
  }

  @Override
  public Record newRecord() {
    // TODO Auto-generated method stub
    return null;
  }

}
