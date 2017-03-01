/**
 * Copyright (c) 2016 MapR, Inc.
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
package org.ojai.store.cdps;

import java.util.Iterator;

import org.ojai.Document;
import org.ojai.FieldPath;
import org.ojai.KeyValue;
import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.store.DocumentStore;

/**
 * This interface encapsulates an atomic set of change operations that
 * were performed to a single {@link Document} in the source {@link DocumentStore}.
 */
@API.Public
public interface ChangeDataRecord extends Iterable<KeyValue<FieldPath, ChangeNode>> {

  /**
   * Returns the "_id" field of the {@link Document} associated with this
   * {@code ChangeData} notification.
   */
  Value getId();

  /**
   * Returns the {@link ChangeDataRecordType} associated with this change
   * data record.
   */
  ChangeDataRecordType getType();

  /**
   * Returns the timestamp of this change data record as number of
   * milliseconds since Unix epoch.<br/>
   * This is set to -1 if ChangeDataRecordType is {@code RECORD_UPDATE}.
   */
  long getOpTimestamp();

  /**
   * Returns the server timestamp of this change data record as number of
   * milliseconds since Unix epoch.<br/>
   * This is set to -1 if ChangeDataRecordType is {@code RECORD_UPDATE}.
   */
  long getServerTimestamp();

  /**
   * Returns an iterator over a set of {@link ChangeNode}s which are part
   * of this change data record.
   */
  @Override
  Iterator<KeyValue<FieldPath, ChangeNode>> iterator();

  /**
   * Returns a new {@link ChangeDataReader} for this change data record.
   */
  ChangeDataReader getReader();

}
