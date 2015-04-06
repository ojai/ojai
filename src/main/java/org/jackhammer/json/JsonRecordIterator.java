
package org.jackhammer.json;

import java.util.Iterator;
import java.util.Stack;

import org.jackhammer.Record;
import org.jackhammer.RecordReader;
import org.jackhammer.RecordReader.EventType;
import org.jackhammer.exceptions.DecodingException;

public class JsonRecordIterator implements Iterator<Record> {
  Iterator<RecordReader> it = null;
  JsonRecord record;
  RecordReader reader;
  private boolean hasNextRecord;

  public JsonRecordIterator(JsonRecordStream s) {
    it = s.recordReaders().iterator();
  }

  @Override
  public boolean hasNext() {
    hasNextRecord = false;
    if (it.hasNext()) {
      reader = it.next();
      if (reader != null) {
        hasNextRecord = true;
      }
    }
    return hasNextRecord;
  }

  @Override
  public Record next() {
    if (hasNextRecord) {
      return getRecordFromStreamReader();
    }
    return null;
  }

  private String getFieldPath(String fieldName, Stack<Object> fieldPathStack) {
    if (fieldPathStack.empty()) {
      return fieldName;
    }
    StringBuilder sb = new StringBuilder();
    for (Object o :fieldPathStack) {
      sb.append(o).append('.');
    }

    return sb.append(fieldName).toString();
  }

  private Record getRecordFromStreamReader() {
    Stack<Object> fieldPathStack = new Stack<Object>();
    JsonRecord newRecord = new JsonRecord();
    EventType event;
    String fieldName;
    String currentPath = null;
    while ((event = reader.next()) != null) {
      switch(event) {
      case START_MAP:
        if (currentPath != null) {
          fieldPathStack.push(currentPath);
        }
        break;
      case FIELD_NAME:
        fieldName = reader.getFieldName();
        currentPath = getFieldPath(fieldName, fieldPathStack);
        break;
      case END_MAP:
        if (!fieldPathStack.empty()) {
          fieldPathStack.pop();
        }
        break;
      case START_ARRAY:
      case END_ARRAY:
        //create a list of elements to be inserted as an array
        break;

      case NULL:
        newRecord.setNull(currentPath);
        break;
      case BOOLEAN:
        newRecord.set(currentPath, reader.getBoolean());
        break;
      case BINARY:
        newRecord.set(currentPath, reader.getBinary());
        break;
      case BYTE:
        newRecord.set(currentPath, reader.getByte());
        break;
      case SHORT:
        newRecord.set(currentPath, reader.getShort());
        break;
      case INT:
        newRecord.set(currentPath, reader.getInt());
        break;
      case LONG:
        newRecord.set(currentPath, reader.getLong());
        break;
      case FLOAT:
        newRecord.set(currentPath, reader.getFloat());
        break;
      case DOUBLE:
        newRecord.set(currentPath, reader.getDouble());
        break;
      case DECIMAL:
        newRecord.set(currentPath, reader.getDecimal());
        break;
      case STRING:
        newRecord.set(currentPath, reader.getString());
        break;
      case DATE:
        newRecord.set(currentPath, reader.getDate());
        break;
      case TIME:
        newRecord.set(currentPath, reader.getTime());
        break;
      case TIMESTAMP:
        newRecord.set(currentPath, reader.getTimestamp());
        break;
      case INTERVAL:
        newRecord.set(currentPath, reader.getInterval());
        break;
       default:
         throw new IllegalStateException("Unknown token type from stream reader");
      }
    }
    if (!fieldPathStack.empty()) {
      //this means we did not got the end of the record.
      throw new DecodingException("Error processing record");
    }
    return newRecord;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
