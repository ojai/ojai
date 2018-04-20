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
package org.ojai.store;

import java.math.BigDecimal;

import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.store.exceptions.DocumentExistsException;
import org.ojai.store.exceptions.DocumentNotFoundException;
import org.ojai.store.exceptions.MultiOpException;
import org.ojai.store.exceptions.StoreException;

@API.Public
@API.NotThreadSafe
public interface DocumentStore extends AutoCloseable {

  /**
   * Returns {@code true} if this Document store does not support any write
   * operations like insert/update/delete, etc.
   */
  public boolean isReadOnly();

  /**
   * Flushes any buffered writes operations for this DocumentStore.
   *
   * @throws StoreException if the flush failed or if the flush of any
   *         buffered operation resulted in an error
   */
  public void flush() throws StoreException;

  /**
   * Begins tracking the write operations performed through this instance of {@link DocumentStore}.
   *
   * @see #endTrackingWrites()
   * @see #clearTrackedWrites()
   * @see Query#waitForTrackedWrites(String)
   *
   * @throws IllegalStateException if a beginTrackingWrites() was already called
   *         and a corresponding endTrackingWrites()/clearTrackedWrites() wasn't
   */
  public void beginTrackingWrites() throws StoreException;

  /**
   * Begins tracking the write operations performed through this instance of {@link DocumentStore}.
   *
   * @param previousWritesContext previously tracked writes that were retrieved from this
   *        {@link DocumentStore}, or from other {@link DocumentStore} instances. The tracking
   *        begins by using this context as the base state
   *
   * @see #endTrackingWrites()
   * @see #clearTrackedWrites()
   * @see Query#waitForTrackedWrites(String)
   *
   * @throws NullPointerException if previousWrites is {@code null}
   * @throws IllegalStateException if a beginTrackingWrites() was already called
   *         and a corresponding endTrackingWrites()/clearTrackedWrites() wasn't
   * @throws IllegalArgumentException if the specified argument can not be parsed
   */
  public void beginTrackingWrites(@NonNullable String previousWritesContext) throws StoreException;

  /**
   * Flushes any buffered writes operations for this {@link DocumentStore} and returns a
   * writesContext which can be used to ensure that such writes are visible to ensuing queries.
   * <p/>
   * The write-context is cleared and tracking is stopped.
   * <p/>
   * This call does not isolate the writes originating from this instance of DocumentStore
   * from other instances and as a side-effect other writes issued to the same document-store
   * through other DocumentStore instances could get flushed.
   *
   * @see #beginTrackingWrites()
   * @see #clearTrackedWrites()
   * @see Query#waitForTrackedWrites(String)
   *
   * @return an encoded string representing the write-context of all writes issued,
   *         since {@link #beginTrackingWrites()} until now, through this instance of
   *         {@link DocumentStore}
   *
   * @throws StoreException if the flush failed or if the flush of any
   *         buffered operation resulted in an error.
   * @throws IllegalStateException if a corresponding {@link #beginTrackingWrites()} was not
   *         called before calling this method
   */
  public String endTrackingWrites() throws StoreException;

  /**
   * Stops the writes tracking and clears any state on this {@link DocumentStore} instance.
   * <p/>
   * This API should be called to stop tracking the writes-context in case where
   * {@link #beginTrackingWrites()} was previously called but a commit context is not needed
   * anymore, for example in case of an error in any of the mutation.
   *
   * @throws IllegalStateException if a corresponding {@link #beginTrackingWrites()} was not
   *         called before calling this method
   */
  public void clearTrackedWrites() throws StoreException;

  /**
   * Returns the Document with the given `_id` or {@code null} if the document with that `_id`
   * doesn't exist in this DocumentStore.
   *
   * @param _id document id
   *
   * @return an OJAI Document with the specified id or {@code null} if one does not exist in
   *         this DocumentStore
   *
   * @throws StoreException
   */
  public Document findById(String _id) throws StoreException;

  /**
   * Returns the Document with the given `_id` or {@code null} if the document with that `_id`
   * doesn't exist in this DocumentStore.
   *
   * @param _id Document _id
   *
   * @return an OJAI Document with the specified id or {@code null} if one does not exist in
   *         this DocumentStore
   *
   * @throws StoreException
   */
  public Document findById(Value _id) throws StoreException;

  /**
   * Returns the Document with the given `_id` or {@code null} if the document with that `_id`
   * doesn't exist in this DocumentStore. The returned Document will include only the specified
   * fields.
   *
   * @param _id Document _id
   * @param fieldPaths Array of field paths that should be returned
   *
   * @return an OJAI Document with the specified id or {@code null} if one does not exist in
   *         this DocumentStore
   *
   * @throws StoreException
   */
  public Document findById(String _id, String...fieldPaths) throws StoreException;

  /**
   * Returns the Document with the given `_id` or {@code null} if the document with that `_id`
   * doesn't exist in this DocumentStore. The returned Document will include only the specified
   * fields.
   *
   * @param _id Document _id
   * @param fieldPaths Array of field paths that should be returned
   *
   * @return an OJAI Document with the specified id or {@code null} if one does not exist in
   *         this DocumentStore
   *
   * @throws StoreException
   */
  public Document findById(String _id, FieldPath...fieldPaths) throws StoreException;

  /**
   * Returns the Document with the given `_id` or {@code null} if the document with that `_id`
   * doesn't exist in this DocumentStore. The returned Document will include only the specified
   * fields.
   *
   * @param _id Document _id
   * @param fieldPaths Array of of field paths that should be returned
   *
   * @return an OJAI Document with the specified id or {@code null} if one does not exist in
   *         this DocumentStore
   *
   * @throws StoreException
   */
  public Document findById(Value _id, String...fieldPaths) throws StoreException;

  /**
   * Returns the Document with the given `_id` or {@code null} if the document with that `_id`
   * doesn't exist in this DocumentStore. The returned Document will include only the specified
   * fields.
   *
   * @param _id Document _id
   * @param fieldPaths Array of of field paths that should be returned
   *
   * @return an OJAI Document with the specified id or {@code null} if one does not exist in
   *         this DocumentStore
   *
   * @throws StoreException
   */
  public Document findById(Value _id, FieldPath...fieldPaths) throws StoreException;

  /**
   * Returns the Document with the given `_id` if it matches the specified condition. If a Document
   * with the specified `_id` does not exist in this DocumentStore or does not satisfy the specified
   * condition, {@code null} is returned.
   *
   * @param _id document id
   * @param condition query condition to test the document
   *
   * @return an OJAI Document with the specified _id
   *
   * @throws StoreException
   */
  public Document findById(String _id, QueryCondition condition) throws StoreException;

  /**
   * Returns the Document with the given `_id` if it matches the specified condition. If a Document
   * with the specified `_id` does not exist in this DocumentStore or does not satisfy the specified
   * condition, {@code null} is returned.
   *
   * @param _id document id
   * @param condition query condition to test the document
   *
   * @return an OJAI Document with the specified _id
   *
   * @throws StoreException
   */
  public Document findById(Value _id, QueryCondition condition) throws StoreException;

  /**
   * Returns the Document with the given `_id` if it matches the specified condition. If a Document
   * with the specified `_id` does not exist in this DocumentStore or does not satisfy the specified
   * condition, {@code null} is returned. The returned Document will include only the specified fields.
   *
   * @param _id document id
   * @param fieldPaths list of fields that should be returned in the read document
   * @param condition query condition to test the document
   *
   * @return an OJAI Document with the specified _id
   *
   * @throws StoreException
   */
  public Document findById(String _id, QueryCondition condition, String...fieldPaths) throws StoreException;

  /**
   * Returns the Document with the given `_id` if it matches the specified condition. If a Document
   * with the specified `_id` does not exist in this DocumentStore or does not satisfy the specified
   * condition, {@code null} is returned. The returned Document will include only the specified fields.
   *
   * @param _id document id
   * @param fieldPaths list of fields that should be returned in the read document
   * @param condition query condition to test the document
   *
   * @return an OJAI Document with the specified _id
   *
   * @throws StoreException
   */
  public Document findById(String _id, QueryCondition condition, FieldPath...fieldPaths) throws StoreException;

  /**
   * Returns the Document with the given `_id` if it matches the specified condition. If a Document
   * with the specified `_id` does not exist in this DocumentStore or does not satisfy the specified
   * condition, {@code null} is returned. The returned Document will include only the specified fields.
   *
   * @param _id document id
   * @param fieldPaths list of fields that should be returned in the read document
   * @param condition query condition to test the document
   *
   * @return an OJAI Document with the specified _id
   *
   * @throws StoreException
   */
  public Document findById(Value _id, QueryCondition condition, String...fieldPaths) throws StoreException;

  /**
   * Returns the Document with the given `_id` if it matches the specified condition. If a Document
   * with the specified `_id` does not exist in this DocumentStore or does not satisfy the specified
   * condition, {@code null} is returned. The returned Document will include only the specified fields.
   *
   * @param _id document id
   * @param fieldPaths list of fields that should be returned in the read document
   * @param condition query condition to test the document
   *
   * @return an OJAI Document with the specified _id
   *
   * @throws StoreException
   */
  public Document findById(Value _id, QueryCondition condition, FieldPath...fieldPaths) throws StoreException;

  /**
   * <p>Executes the specified query on the DocumentStore and return a QueryResult.
   * <p>The returned QueryResult must be closed after retrieving the documents.
   *
   * @return a QueryResult that can be used to retrieve the documents in the result
   *
   * @throws StoreException
   */
  public QueryResult find(@NonNullable Query query) throws StoreException;

  /**
   * <p>Executes a query to return all Documents in the DocumentStore.
   * <p>The returned DocumentStream must be closed after retrieving the documents.
   *
   * @return a DocumentStream of all documents in the this DocumentStore
   * @deprecated use {@link #find(Query)}
   *
   * @throws StoreException
   */
  public DocumentStream find() throws StoreException;

  /**
   * <p>Executes the specified query on the DocumentStore and return a DocumentStream of the result.
   * <p>The returned DocumentStream must be closed after retrieving the documents.
   *
   * @return a DocumentStream that can be used to retrieve the documents in the result
   * @deprecated use {@link #find(Query)}
   *
   * @throws StoreException
   */
  public DocumentStream findQuery(@NonNullable Query query) throws StoreException;

  /**
   * <p>Executes the specified query on the DocumentStore and return a DocumentStream of the result.
   * <p>The returned DocumentStream must be closed after retrieving the documents.
   *
   * @param queryJSON a Json string representation of OJAI Query
   * @return a DocumentStream that can be used to retrieve the documents in the result
   * @deprecated use {@link #find(Query)}
   *
   * @throws StoreException
   */
  public DocumentStream findQuery(@NonNullable String queryJSON) throws StoreException;

  /**
   * <p>Executes a query to return all Documents in the DocumentStore.
   * <p>Each Document will contain only those field paths that are specified in the
   * argument. If no fields are specified then it returns a full document.
   *
   * @param fieldPaths list of fields that should be returned in the read document
   * @return a DocumentStream that can be used to retrieve the documents in the result
   * @deprecated use {@link #find(Query)}
   *
   * @throws StoreException
   */
  public DocumentStream find(@NonNullable String... fieldPaths) throws StoreException;

  /**
   * <p>Executes a query to return all Documents in the DocumentStore.
   * <p>Each Document will contain only those field paths that are specified in the
   * argument. If no fields are specified then it returns a full document.
   *
   * @param fieldPaths list of fields that should be returned in the read document
   * @return a DocumentStream that can be used to retrieve the documents in the result
   * @deprecated use {@link #find(Query)}
   *
   * @throws StoreException
   */
  public DocumentStream find(@NonNullable FieldPath... fieldPaths) throws StoreException;

  /**
   * Returns a DocumentStream with all the documents in the DocumentStore that
   * satisfies the QueryCondition.
   *
   * @param condition the QueryCondition to match the documents
   * @return a DocumentStream that can be used to retrieve the documents in the result
   * @deprecated use {@link #find(Query)}
   *
   * @throws StoreException
   */
  public DocumentStream find(@NonNullable QueryCondition condition) throws StoreException;

  /**
   * <p>Executes a query on the DocumentStore and return a DocumentStream of the Document
   * matching the specified QueryCondition.
   * <p>Each Document will contain only those field paths that are specified in the
   * argument. If no fields are specified then it returns a full document.
   *
   * @param condition the QueryCondition to match the documents
   * @param fieldPaths list of fields that should be returned in the read document
   * @return a DocumentStream that can be used to retrieve the documents in the result
   * @deprecated use {@link #find(Query)}
   *
   * @throws StoreException
   */
  public DocumentStream find(@NonNullable QueryCondition condition, @NonNullable String...fieldPaths)
      throws StoreException;

  /**
   * <p>Executes a query on the DocumentStore and return a DocumentStream of the Document
   * matching the specified QueryCondition.
   * <p>Each Document will contain only those field paths that are specified in the
   * argument. If no fields are specified then it returns a full document.
   *
   * @param condition the QueryCondition to match the documents
   * @param fieldPaths list of fields that should be returned in the read document
   * @return a DocumentStream that can be used to retrieve the documents in the result
   * @deprecated use {@link #find(Query)}
   *
   * @throws StoreException
   */
  public DocumentStream find(@NonNullable QueryCondition condition, @NonNullable FieldPath... fieldPaths)
      throws StoreException;

  /**
   * Inserts or replaces a new document in this DocumentStore.
   * <br/><br/>
   * The specified Document must contain an {@code "_id"} field or the operation
   * will fail.
   * <br/><br/>
   * If the document with the given _id exists in the DocumentStore then that
   * document will be replaced by the specified document.
   *
   * @param doc the Document to be inserted or replaced in the DocumentStore
   * @throws StoreException
   */
  public void insertOrReplace(@NonNullable Document doc) throws StoreException;

  /**
   * Inserts or replaces a new document in this DocumentStore with the given _id.
   * <br/><br/>
   * The specified document should either not contain an {@code "_id"} field or
   * its value should be same as the specified _id or the operation will fail.
   * <br/><br/>
   * If the document with the given _id exists in the DocumentStore then that
   * document will be replaced by the specified document.
   *
   * @param doc the Document to be inserted or replaced in the DocumentStore
   * @param _id value to be used as the _id for this document
   * @throws StoreException
   */
  public void insertOrReplace(@NonNullable String _id, @NonNullable Document r) throws StoreException;

  /**
   * Inserts or replaces a new document in this DocumentStore with the given _id.
   * <br/><br/>
   * The specified document should either not contain an {@code "_id"} field or
   * its value should be same as the specified _id or the operation will fail.
   * <br/><br/>
   * If the document with the given _id exists in the DocumentStore then that
   * document will be replaced by the specified document.
   *
   * @param doc the Document to be inserted or replaced in the DocumentStore
   * @param _id value to be used as the _id for this document
   * @throws StoreException
   */
  public void insertOrReplace(@NonNullable Value _id, @NonNullable Document doc) throws StoreException;

  /**
   * Inserts or replaces a new document in this DocumentStore with the value of
   * the specified Field as the {@code _id}.
   * <br/><br/>
   * If the document with the given _id exists in the DocumentStore then that
   * document will be replaced by the specified document.
   *
   * @param doc the Document to be inserted or replaced in the DocumentStore
   * @param fieldAsKey document's field to be used as the key when an id is not
   *                   passed in and the document doesn't have an "_id" field or
   *                   a different field is desired to be used as _id
   * @throws StoreException
   */
  public void insertOrReplace(@NonNullable Document doc, @NonNullable FieldPath fieldAsKey) throws StoreException;

  /**
   * Inserts or replaces a new document in this DocumentStore with the value of
   * the specified Field as the {@code _id}.
   * <br/><br/>
   * If the document with the given _id exists in the DocumentStore then that
   * document will be replaced by the specified document.
   *
   * @param doc the Document to be inserted or replaced in the DocumentStore
   * @param fieldAsKey document's field to be used as the key when an id is not
   *                   passed in and the document doesn't have an "_id" field or
   *                   a different field is desired to be used as _id
   * @throws StoreException
   */
  public void insertOrReplace(@NonNullable Document doc, @NonNullable String fieldAsKey) throws StoreException;

  /**
   * Inserts all documents from the specified DocumentStream into this DocumentStore.
   * <br/><br/>
   * This is a synchronous API and it won't return until all the documents
   * in the DocumentStream are written to the DocumentStore or some error has
   * occurred while storing the documents. Each document read from the DocumentStream
   * must have a field "_id"; otherwise, the operation will fail.
   *
   * If there is an error in reading from the stream or in writing to the DocumentStore
   * then a MultiOpException will be thrown containing the list of documents that
   * failed to be stored in the DocumentStore. Reading from a stream stops on the
   * first read error. If only write errors occur, the iterator will stop and the
   * rest of the documents will remain un-consumed in the DocumentStream.
   *
   * @param stream the DocumentStream to read the documents from
   *
   * @throws MultiOpException which has a list of write-failed documents and
   *                          their errors
   */
  public void insertOrReplace(@NonNullable DocumentStream stream) throws MultiOpException;

  /**
   * Inserts all documents from the specified DocumentStream into this DocumentStore
   * using the field specified by parameter {@code fieldAsKey} as the "_id" field.
   * If an "_id" field is present in the documents, an exception will be thrown.
   * <br/><br/>
   * This is a synchronous API and it won't return until all the documents
   * in the DocumentStream are written to the DocumentStore or some error has
   * occurred while storing the documents. Each document read from the DocumentStream
   * must have a field "_id"; otherwise, the operation will fail.
   *
   * If there is an error in reading from the stream or in writing to the DocumentStore
   * then a MultiOpException will be thrown containing the list of documents that
   * failed to be stored in the DocumentStore. Reading from a stream stops on the
   * first read error. If only write errors occur, the iterator will stop and the
   * rest of the documents will remain un-consumed in the DocumentStream.
   *
   * @param stream the DocumentStream to read the documents from
   * @param fieldAsKey field from each document whose value is to be used as
   *                   the document key for insertion
   *
   * @throws MultiOpException which has a list of write-failed documents and
   *                          their errors
   */
  public void insertOrReplace(@NonNullable DocumentStream stream, @NonNullable FieldPath fieldAsKey)
      throws MultiOpException;

  /**
   * Inserts all documents from the specified DocumentStream into this DocumentStore
   * using the field specified by parameter {@code fieldAsKey} as the "_id" field.
   * If an "_id" field is present in the documents, an exception will be thrown.
   * <br/><br/>
   * This is a synchronous API and it won't return until all the documents
   * in the DocumentStream are written to the DocumentStore or some error has
   * occurred while storing the documents. Each document read from the DocumentStream
   * must have a field "_id"; otherwise, the operation will fail.
   *
   * If there is an error in reading from the stream or in writing to the DocumentStore
   * then a MultiOpException will be thrown containing the list of documents that
   * failed to be stored in the DocumentStore. Reading from a stream stops on the
   * first read error. If only write errors occur, the iterator will stop and the
   * rest of the documents will remain un-consumed in the DocumentStream.
   *
   * @param stream the DocumentStream to read the documents from
   * @param fieldAsKey field from each document whose value is to be used as
   *                   the document key for insertion
   *
   * @throws MultiOpException which has a list of write-failed documents and
   *                          their errors
   */
  public void insertOrReplace(@NonNullable DocumentStream stream, @NonNullable String fieldAsKey)
      throws MultiOpException;

  /**
   * Applies a mutation on the document identified by the document id.<br/><br/>
   * All updates specified by the mutation object should be applied atomically,
   * and consistently meaning either all of the updates in mutation are applied
   * or none of them is applied and a partial update should not be visible to an
   * observer.
   *
   * @param _id document id
   * @param m a mutation object specifying the mutation operations on the document
   * @throws StoreException
   */
  public void update(@NonNullable String _id, @NonNullable DocumentMutation mutation) throws StoreException;

  /**
   * Applies a mutation on the document identified by the document id.<br/><br/>
   * All updates specified by the mutation object should be applied atomically,
   * and consistently meaning either all of the updates in mutation are applied
   * or none of them is applied and a partial update should not be visible to an
   * observer.
   *
   * @param _id document id
   * @param m a mutation object specifying the mutation operations on the document
   * @throws StoreException
   */
  public void update(@NonNullable Value _id, @NonNullable DocumentMutation mutation) throws StoreException;

  /**
   * Deletes a document with the given id. This operation is successful even
   * when the document with the given id doesn't exist.
   *
   * If the parameter {@code fieldAsKey} is provided, its value will be used as
   * the "_id" to delete the document.
   *
   * @param _id document id
   * @param doc JSON document to be deleted
   * @param fieldAsKey document's field to be used as the key when an id is not
   *                   passed in and a document doesn't have an "_id" field
   * @throws StoreException
   */
  public void delete(@NonNullable String _id) throws StoreException;
  public void delete(@NonNullable Value _id) throws StoreException;
  public void delete(@NonNullable Document doc) throws StoreException;

  public void delete(@NonNullable Document doc, @NonNullable FieldPath fieldAsKey) throws StoreException;
  public void delete(@NonNullable Document doc, @NonNullable String fieldAsKey) throws StoreException;

  /**
   * Deletes a set of documents from the DocumentStore represented by the DocumentStream.
   * This is a synchronous API and it won't return until all the documents
   * in the DocumentStream are written to the DocumentStore or some error occurs while
   * writing the documents. Each document read from the DocumentStream must have a
   * field "_id" of type BINARY or UTF8-string; otherwise the operation will fail.
   *
   * If there is an error in reading from the stream or in writing to
   * the DocumentStore then a MultiOpException will be thrown that contains a list of
   * documents that failed to write to the DocumentStore. Reading from a stream stops on
   * the first read error. If only write errors occur, the iterator will stop
   * and the current list of failed document is returned in a multi op exception.
   * The untouched documents will remain in the DocumentStream.
   *
   * @param stream DocumentStream
   * @param fieldAsKey a field from each document whose value is to be used as
   *                     the document key for deletion
   *
   * @throws MultiOpException which has a list of write-failed documents and
   *                          their errors
   */
  public void delete(@NonNullable DocumentStream stream) throws MultiOpException;
  public void delete(@NonNullable DocumentStream stream, @NonNullable FieldPath fieldAsKey)
      throws MultiOpException;
  public void delete(@NonNullable DocumentStream stream, @NonNullable String fieldAsKey)
      throws MultiOpException;

  /**
   * Inserts a document with the given id. This operation is successful only
   * when the document with the given id doesn't exist.
   *
   * "fieldAsKey", when provided, will also be stored as the "_id" field in the
   * written document for the document. If "_id" already existed in the document, then
   * an error will be thrown. When reading the document back from the DB, the
   * key will be returned back as usual as the "_id" field.
   *
   * Note that an insertOrReplace() operation would be more efficient than an
   * insert() call.
   *
   * @param doc JSON document as the new value for the given document
   * @param _id to be used as the key for the document
   * @param fieldAsKey document's field to be used as the key when the id is not
   *                     passed in and document doesn't have an "_id" field
   * @throws TableNotFoundException when a DocumentStore does not exist to add this document
   * @throws ReadOnlyException when a DocumentStore is not accepting writes
   * @throws OpNotPermittedException when the server returned EPERM
   * @throws DocumentExistsException when a document with id already exists in DocumentStore
   */
  public void insert(@NonNullable String _id, @NonNullable Document doc) throws StoreException;
  public void insert(@NonNullable Value _id, @NonNullable Document doc) throws StoreException;

  public void insert(@NonNullable Document doc) throws StoreException;
  public void insert(@NonNullable Document doc, @NonNullable FieldPath fieldAsKey) throws StoreException;
  public void insert(@NonNullable Document doc, @NonNullable String fieldAsKey) throws StoreException;

  /**
   * Inserts a set of documents represented by the DocumentStream into the DocumentStore.
   * This is a synchronous API that won't return until all the documents
   * in the DocumentStream are written to the DocumentStore or some error occurs while
   * writing the documents. Each document read from the DocumentStream must have a
   * field "_id" of type BINARY or UTF8-string; otherwise, the operation will
   * fail or it will be of the Document type.
   *
   * If a document with the given key exists on the server then it throws a document
   * exists exception, similar to the non-DocumentStream based insert() API.
   *
   * If there is an error in reading from the stream or in writing to
   * the DocumentStore then a MultiOpException will be thrown that contains a list of
   * documents that failed to write to the DocumentStore. Reading from a stream stops on
   * the first read error. If only write errors occur, the iterator will stop
   * and the current list of failed document is returned in a multi op exception.
   * The untouched documents will remain in the DocumentStream.
   *
   * @param stream DocumentStream
   * @param fieldAsKey a field from each document whose value is to be used as
   *                     the document key for deletion
   *
   * @throws MultiOpException which has a list of write-failed documents and
   *                          their errors
   */
  public void insert(@NonNullable DocumentStream stream) throws MultiOpException;
  public void insert(@NonNullable DocumentStream stream, @NonNullable FieldPath fieldAsKey)
      throws MultiOpException;
  public void insert(@NonNullable DocumentStream stream, @NonNullable String fieldAsKey)
      throws MultiOpException;

  /**
   * Replaces a document in the DocumentStore. The document id is either explicitly specified
   * as parameter "id" or it is implicitly specified as the field "_id" in the
   * passed document. If the document id is explicitly passed then the document should
   * not contain "_id" field or its value should be the same as the explicitly
   * specified id; otherwise, the operation will  fail.
   *
   * If the document with the given key does not exist on the server then it will
   * throw DocumentNotFoundException.
   *
   * "fieldAsKey", when provided, will also be stored as the "_id" field in the
   * written document for the document. If "_id" already existed in the document, then
   * an error will be thrown. When reading the document back from the DB, the
   * key would be returned back as usual as "_id" field.
   *
   * Note that an insertOrReplace() operation would be more efficient than an
   * replace() call.

   * @param doc JSON document as the new value for the given document
   * @param _id to be used as the key for the document
   * @param fieldAsKey document's field to be used as the key when an id is not
   *                     passed in and document doesn't have an "_id" field
   * @throws TableNotFoundException when a DocumentStore does not exist to which to add this document
   * @throws ReadOnlyException when a DocumentStore is not accepting writes
   * @throws OpNotPermittedException when the server returns EPERM
   * @throws DocumentNotFoundException when a document with the id does not exist in DocumentStore
   */
  public void replace(@NonNullable String _id, @NonNullable Document doc) throws StoreException;
  public void replace(@NonNullable Value _id, @NonNullable Document doc) throws StoreException;

  public void replace(@NonNullable Document doc) throws StoreException;
  public void replace(@NonNullable Document doc, @NonNullable FieldPath fieldAsKey) throws StoreException;
  public void replace(@NonNullable Document doc, @NonNullable String fieldAsKey) throws StoreException;

  /**
   * Replaces a set of documents represented by the DocumentStream into the DocumentStore.
   * This is a synchronous API and it won't return until all the documents
   * in the DocumentStream are written to the DocumentStore or some error occurs while
   * writing the documents. Each document read from the DocumentStream must have a
   * field "_id" of type BINARY or UTF8-string; otherwise, the operation will
   * fail or it will be of Document type.
   *
   * If the document with the given key does not exist on the server then it throws,
   * a document not exists exception, similar to the non-DocumentStream based
   * replace() API.
   *
   * If there is an error in reading from the stream or in writing to
   * the DocumentStore then a MultiOpException will be thrown that contains a list of
   * documents that failed to write to the DocumentStore. Reading from a stream stops on
   * the first read error. If only write errors occur, the iterator will stop
   * and the current list of failed document is returned in a multi op exception.
   * The untouched documents will remain in the DocumentStream.
   *
   * @param stream a DocumentStream to read the documents from
   * @param fieldAsKey field from each document whose value is to be used as
   *                     the document key for deletion
   *
   * @throws MultiOpException which has list of write-failed documents and
   *                          their errors
   */
  public void replace(@NonNullable DocumentStream stream) throws MultiOpException;
  public void replace(@NonNullable DocumentStream stream, @NonNullable FieldPath fieldAsKey)
      throws MultiOpException;
  public void replace(@NonNullable DocumentStream stream, @NonNullable String fieldAsKey)
      throws MultiOpException;

  /**
   * Atomically applies an increment to a given field (in dot separated notation)
   * of the given document id. If the field doesn't exist on the server
   * then it will be created with the type of the incremental value.
   * The increment operation can be applied on any of the numeric
   * types, such as byte, short, int, long, float, double, or decimal,
   * of a field. The operation will fail if the increment is applied to a
   * field that is of a non-numeric type.
   *
   * If an id doesn't exist, it gets created (similar to the insertOrReplace
   * behavior). And it is created, with the value of 'inc' parameter. The same
   * logic applies to intermittent paths in the path: they get created top to
   * bottom.
   *
   * If the type is different than the field in the operation, it fails.
   *
   * The increment operation won't change the type of existing value stored in
   * the given field for the document. The resultant value of the field will be
   * truncated based on the original type of the field.
   *
   * For example, if a field 'score' is of type int and contains 60 and an
   * increment of double '5.675' is applied, then the resultant value of the
   * field will be 65 (65.675 will be truncated to 65).
   *
   * If the type to which the increment is applied is a byte, short, or int,
   * then it needs to use long as the operation.
   *
   * @param _id document id
   * @param field the field name in dot separated notation
   * @param inc increment to apply to a field. Can be positive or negative
   * @throws StoreException
   */
  public void increment(@NonNullable String _id,
      @NonNullable String field, byte inc) throws StoreException;

  public void increment(@NonNullable String _id,
      @NonNullable String field, short inc) throws StoreException;

  public void increment(@NonNullable String _id,
      @NonNullable String field, int inc) throws StoreException;

  public void increment(@NonNullable String _id,
      @NonNullable String field, long inc) throws StoreException;

  public void increment(@NonNullable String _id,
      @NonNullable String field, float inc) throws StoreException;

  public void increment(@NonNullable String _id,
      @NonNullable String field, double inc) throws StoreException;

  public void increment(@NonNullable String _id,
      @NonNullable String field, @NonNullable BigDecimal inc) throws StoreException;

  public void increment(@NonNullable Value _id,
      @NonNullable String field, byte inc) throws StoreException;

  public void increment(@NonNullable Value _id,
      @NonNullable String field, short inc) throws StoreException;

  public void increment(@NonNullable Value _id,
      @NonNullable String field, int inc) throws StoreException;

  public void increment(@NonNullable Value _id,
      @NonNullable String field, long inc) throws StoreException;

  public void increment(@NonNullable Value _id,
      @NonNullable String field, float inc) throws StoreException;

  public void increment(@NonNullable Value _id,
      @NonNullable String field, double inc) throws StoreException;

  public void increment(@NonNullable Value _id,
      @NonNullable String field, @NonNullable BigDecimal inc) throws StoreException;

  /**
   * Atomically evaluates the condition on a given document and if the
   * condition holds true for the document then a mutation is applied on the document.
   *
   * If an id doesn't exist, the function returns false (no exception is thrown).
   * If the mutation operation fails, it throws exception.
   *
   * @deprecated Use {@link #checkAndUpdate(String, QueryCondition, DocumentMutation)}
   *
   * @param _id document id
   * @param condition the condition to evaluate on the document
   * @param m mutation to apply on the document
   * @return True if the condition is true for the document; otherwise, false
   * @throws StoreException if the condition passes but the mutate fails
   */
  @Deprecated
  public boolean checkAndMutate(@NonNullable String _id,
      @NonNullable QueryCondition condition, @NonNullable DocumentMutation mutation) throws StoreException;

  /**
   * Atomically evaluates the condition on a given document and if the
   * condition holds true for the document then a mutation is applied on the document.
   *
   * If an id doesn't exist, the function returns false (no exception is thrown).
   * If the mutation operation fails, it throws exception.
   *
   * @deprecated Use {@link #checkAndUpdate(Value, QueryCondition, DocumentMutation)}
   *
   * @param _id document id
   * @param condition the condition to evaluate on the document
   * @param m mutation to apply on the document
   * @return True if the condition is true for the document; otherwise, false
   * @throws StoreException if the condition passes but the mutate fails
   */
  @Deprecated
  public boolean checkAndMutate(@NonNullable Value _id, @NonNullable QueryCondition condition,
      @NonNullable DocumentMutation mutation) throws StoreException;

  /**
   * Atomically evaluates the condition on a given document and if the
   * condition holds true for the document then a mutation is applied on the document.
   *
   * If an id doesn't exist, the function returns false (no exception is thrown).
   * If the mutation operation fails, it throws exception.
   *
   * @param _id document id
   * @param condition the condition to evaluate on the document
   * @param m mutation to apply on the document
   * @return True if the condition is true for the document; otherwise, false
   * @throws StoreException if the condition passes but the mutate fails
   */
  default public boolean checkAndUpdate(@NonNullable String _id,
      @NonNullable QueryCondition condition, @NonNullable DocumentMutation mutation) throws StoreException {
    return checkAndMutate(_id, condition, mutation);
  }

  /**
   * Atomically evaluates the condition on a given document and if the
   * condition holds true for the document then a mutation is applied on the document.
   *
   * If an id doesn't exist, the function returns false (no exception is thrown).
   * If the mutation operation fails, it throws exception.
   *
   * @param _id document id
   * @param condition the condition to evaluate on the document
   * @param m mutation to apply on the document
   * @return True if the condition is true for the document; otherwise, false
   * @throws StoreException if the condition passes but the mutate fails
   */
  default public boolean checkAndUpdate(@NonNullable Value _id, @NonNullable QueryCondition condition,
      @NonNullable DocumentMutation mutation) throws StoreException {
    return checkAndMutate(_id, condition, mutation);
  }

  /**
   * Atomically evaluates the condition on given document and if the
   * condition holds true for the document then it is atomically deleted.
   *
   * If id doesn't exist, returns false (no exception is thrown).
   * If deletion operation fails, it throws exception.
   *
   * @param _id document id
   * @param condition condition to evaluate on the document
   * @return True if the condition is valid for the document, otherwise false.
   * @throws StoreException if the condition passes but the delete fails
   */
  public boolean checkAndDelete(@NonNullable String _id, @NonNullable
      QueryCondition condition) throws StoreException;
  public boolean checkAndDelete(@NonNullable Value _id, @NonNullable
      QueryCondition condition) throws StoreException;

  /**
   * Atomically evaluates the condition on the given document and if the
   * condition holds true for the document then it atomically replaces the document
   * with the given document.
   *
   * If the id doesn't exist, the function returns false (no exception is thrown).
   * If the replace operation fails, it throws an exception.
   *
   * @param _id document id
   * @param condition the condition to evaluate on the document
   * @param doc document to replace
   * @return True if the condition is true for the document otherwise false
   * @throws StoreException if the condition passes but the replace fails
   */
  public boolean checkAndReplace(@NonNullable String _id,
      @NonNullable QueryCondition condition, @NonNullable Document doc) throws StoreException;
  public boolean checkAndReplace(@NonNullable Value _id,
      @NonNullable QueryCondition condition, @NonNullable Document doc) throws StoreException;

  /**
   * Override {@link AutoCloseable#close()} to avoid declaring a checked exception.
   */
  void close() throws StoreException;

}
