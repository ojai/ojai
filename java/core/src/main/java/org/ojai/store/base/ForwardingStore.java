/**
 * Copyright (c) 2018 MapR, Inc.
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
package org.ojai.store.base;

import java.math.BigDecimal;

import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.store.DocumentMutation;
import org.ojai.store.DocumentStore;
import org.ojai.store.Query;
import org.ojai.store.QueryCondition;
import org.ojai.store.QueryResult;
import org.ojai.store.exceptions.MultiOpException;
import org.ojai.store.exceptions.StoreException;

import com.google.common.base.Preconditions;

/**
 * An adapter implementation of {@linkplain DocumentStore} interface which forwards the
 * individual API calls to the wrapped Store object.
 */
@API.Public
@SuppressWarnings("deprecation")
public class ForwardingStore implements DocumentStore {

  private final DocumentStore store;

  public ForwardingStore(final DocumentStore store) {
    this.store = Preconditions.checkNotNull(store);
  }

  @Override
  public void close() throws StoreException {
    store.close();
  }

  @Override
  public boolean isReadOnly() {
    return store.isReadOnly();
  }

  @Override
  public void beginTrackingWrites() throws StoreException {
    store.beginTrackingWrites();
  }

  @Override
  public void beginTrackingWrites(final String previousWritesContext) throws StoreException {
    store.beginTrackingWrites(previousWritesContext);
  }

  @Override
  public String endTrackingWrites() throws StoreException {
    return store.endTrackingWrites();
  }

  @Override
  public void clearTrackedWrites() throws StoreException {
    store.clearTrackedWrites();
  }

  @Override
  public Document findById(final Value _id, final String... fieldPaths) throws StoreException {
    return store.findById(_id, fieldPaths);
  }

  @Override
  public Document findById(final Value _id, final FieldPath... fieldPaths) throws StoreException {
    return store.findById(_id, fieldPaths);
  }

  @Override
  public Document findById(final Value _id, final QueryCondition condition) throws StoreException {
    return store.findById(_id, condition);
  }

  @Override
  public Document findById(final Value _id, final QueryCondition condition, final String... fieldPaths)
      throws StoreException {
    return store.findById(_id, condition, fieldPaths);
  }

  @Override
  public Document findById(final Value _id, final QueryCondition condition, final FieldPath... fieldPaths)
      throws StoreException {
    return store.findById(_id, condition, fieldPaths);
  }

  @Override
  public QueryResult find(final Query query) throws StoreException {
    return store.find(query);
  }

  @Override
  public DocumentStream findQuery(final Query query) throws StoreException {
    return store.find(query);
  }

  @Override
  public DocumentStream findQuery(final String queryJSON) throws StoreException {
    return store.findQuery(queryJSON);
  }

  @Override
  public void insertOrReplace(final Value _id, final Document doc) throws StoreException {
    store.insertOrReplace(_id, doc);
  }

  @Override
  public void update(final Value _id, final DocumentMutation mutation) throws StoreException {
    store.update(_id, mutation);
  }

  @Override
  public void delete(final Value _id) throws StoreException {
    store.delete(_id);
  }

  @Override
  public void insert(final Value _id, final Document doc) throws StoreException {
    store.insert(_id, doc);
  }

  @Override
  public void replace(final Value _id, final Document doc) throws StoreException {
    store.replace(_id, doc);
  }

  @Override
  public void increment(final Value _id, final String field, final byte inc) throws StoreException {
    store.increment(_id, field, inc);
  }

  @Override
  public void increment(final Value _id, final String field, final short inc) throws StoreException {
    store.increment(_id, field, inc);
  }

  @Override
  public void increment(final Value _id, final String field, final int inc) throws StoreException {
    store.increment(_id, field, inc);
  }

  @Override
  public void increment(final Value _id, final String field, final long inc) throws StoreException {
    store.increment(_id, field, inc);
  }

  @Override
  public void increment(final Value _id, final String field, final float inc) throws StoreException {
    store.increment(_id, field, inc);
  }

  @Override
  public void increment(final Value _id, final String field, final double inc) throws StoreException {
    store.increment(_id, field, inc);
  }

  @Override
  public void increment(final Value _id, final String field, final BigDecimal inc) throws StoreException {
    store.increment(_id, field, inc);
  }

  @Override
  public boolean checkAndMutate(final Value _id, final QueryCondition condition, final DocumentMutation mutation)
      throws StoreException {
    return store.checkAndUpdate(_id, condition, mutation);
  }

  @Override
  public boolean checkAndDelete(final Value _id, final QueryCondition condition) throws StoreException {
    return store.checkAndDelete(_id, condition);
  }

  @Override
  public boolean checkAndReplace(final Value _id, final QueryCondition condition, final Document doc)
      throws StoreException {
    return store.checkAndReplace(_id, condition, doc);
  }

  @Override
  public void flush() throws StoreException {
    store.flush();
  }

  @Override
  public Document findById(final String id) throws StoreException {
    return store.findById(id);
  }

  @Override
  public Document findById(final Value id) throws StoreException {
    return store.findById(id);
  }

  @Override
  public Document findById(final String id, final String... paths) throws StoreException {
    return store.findById(id, paths);
  }

  @Override
  public Document findById(final String id, final FieldPath... paths) throws StoreException {
    return store.findById(id, paths);
  }

  @Override
  public Document findById(final String id, final QueryCondition c) throws StoreException {
    return store.findById(id, c);
  }

  @Override
  public Document findById(final String id, final QueryCondition c, final String... paths) throws StoreException {
    return store.findById(id, c, paths);
  }

  @Override
  public Document findById(final String id, final QueryCondition c, final FieldPath... paths) throws StoreException {
    return store.findById(id, c, paths);
  }

  @Override
  public DocumentStream find() throws StoreException {
    return store.find();
  }

  @Override
  public DocumentStream find(final String... paths) throws StoreException {
    return store.find(paths);
  }

  @Override
  public DocumentStream find(final FieldPath... paths) throws StoreException {
    return store.find(paths);
  }

  @Override
  public DocumentStream find(final QueryCondition c) throws StoreException {
    return store.find(c);
  }

  @Override
  public DocumentStream find(final QueryCondition c, final String... paths) throws StoreException {
    return store.find(c, paths);
  }

  @Override
  public DocumentStream find(final QueryCondition c, final FieldPath... paths) throws StoreException {
    return store.find(c, paths);
  }

  @Override
  public void insertOrReplace(final Document r) throws StoreException {
    store.insertOrReplace(r);
  }

  @Override
  public void insertOrReplace(final String id, final Document r) throws StoreException {
    store.insertOrReplace(id, r);
  }

  @Override
  public void insertOrReplace(final Document r, final FieldPath fieldAsKey) throws StoreException {
    store.insertOrReplace(r, fieldAsKey);
  }

  @Override
  public void insertOrReplace(final Document r, final String fieldAsKey) throws StoreException {
    store.insertOrReplace(r, fieldAsKey);
  }

  @Override
  public void insertOrReplace(final DocumentStream rs) throws MultiOpException {
    store.insertOrReplace(rs);
  }

  @Override
  public void insertOrReplace(final DocumentStream rs, final FieldPath fieldAsKey) throws MultiOpException {
    store.insertOrReplace(rs, fieldAsKey);
  }

  @Override
  public void insertOrReplace(final DocumentStream rs, final String fieldAsKey) throws MultiOpException {
    store.insertOrReplace(rs, fieldAsKey);
  }

  @Override
  public void update(final String id, final DocumentMutation m) throws StoreException {
    store.update(id, m);
  }

  @Override
  public void delete(final String id) throws StoreException {
    store.delete(id);
  }

  @Override
  public void delete(final Document r) throws StoreException {
    store.delete(r);
  }

  @Override
  public void delete(final Document r, final FieldPath fieldAsKey) throws StoreException {
    store.delete(r, fieldAsKey);
  }

  @Override
  public void delete(final Document r, final String fieldAsKey) throws StoreException {
    store.delete(r, fieldAsKey);
  }

  @Override
  public void delete(final DocumentStream rs) throws MultiOpException {
    store.delete(rs);
  }

  @Override
  public void delete(final DocumentStream rs, final FieldPath fieldAsKey) throws MultiOpException {
    store.delete(rs, fieldAsKey);
  }

  @Override
  public void delete(final DocumentStream rs, final String fieldAsKey) throws MultiOpException {
    store.delete(rs, fieldAsKey);
  }

  @Override
  public void insert(final String id, final Document r) throws StoreException {
    store.insert(id, r);
  }

  @Override
  public void insert(final Document r) throws StoreException {
    store.insert(r);
  }

  @Override
  public void insert(final Document r, final FieldPath fieldAsKey) throws StoreException {
    store.insert(r, fieldAsKey);
  }

  @Override
  public void insert(final Document r, final String fieldAsKey) throws StoreException {
    store.insert(r, fieldAsKey);
  }

  @Override
  public void insert(final DocumentStream rs) throws MultiOpException {
    store.insert(rs);
  }

  @Override
  public void insert(final DocumentStream rs, final FieldPath fieldAsKey) throws MultiOpException {
    store.insert(rs, fieldAsKey);
  }

  @Override
  public void insert(final DocumentStream rs, final String fieldAsKey) throws MultiOpException {
    store.insert(rs, fieldAsKey);
  }

  @Override
  public void replace(final String id, final Document r) throws StoreException {
    store.replace(id, r);
  }

  @Override
  public void replace(final Document r) throws StoreException {
    store.replace(r);
  }

  @Override
  public void replace(final Document r, final FieldPath fieldAsKey) throws StoreException {
    store.replace(r, fieldAsKey);

  }

  @Override
  public void replace(final Document r, final String fieldAsKey) throws StoreException {
    store.replace(r, fieldAsKey);
  }

  @Override
  public void replace(final DocumentStream rs) throws MultiOpException {
    store.replace(rs);
  }

  @Override
  public void replace(final DocumentStream rs, final FieldPath fieldAsKey) throws MultiOpException {
    store.replace(rs, fieldAsKey);

  }

  @Override
  public void replace(final DocumentStream rs, final String fieldAsKey) throws MultiOpException {
    store.replace(rs, fieldAsKey);
  }

  @Override
  public void increment(final String id, final String field, final byte inc) throws StoreException {
    store.increment(id, field, inc);
  }

  @Override
  public void increment(final String id, final String field, final short inc) throws StoreException {
    store.increment(id, field, inc);
  }

  @Override
  public void increment(final String id, final String field, final int inc) throws StoreException {
    store.increment(id, field, inc);
  }

  @Override
  public void increment(final String id, final String field, final long inc) throws StoreException {
    store.increment(id, field, inc);
  }

  @Override
  public void increment(final String id, final String field, final float inc) throws StoreException {
    store.increment(id, field, inc);
  }

  @Override
  public void increment(final String id, final String field, final double inc) throws StoreException {
    store.increment(id, field, inc);
  }

  @Override
  public void increment(final String id, final String field, final BigDecimal inc) throws StoreException {
    store.increment(id, field, inc);
  }

  @Override
  public boolean checkAndMutate(final String id, final QueryCondition condition, final DocumentMutation m)
      throws StoreException {
    return store.checkAndMutate(id, condition, m);
  }

  @Override
  public boolean checkAndDelete(final String id, final QueryCondition condition) throws StoreException {
    return store.checkAndDelete(id, condition);
  }

  @Override
  public boolean checkAndReplace(final String id, final QueryCondition condition, final Document r)
      throws StoreException {
    return store.checkAndReplace(id, condition, r);
  }

}
