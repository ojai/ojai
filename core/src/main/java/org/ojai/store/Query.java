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
package org.ojai.store;

import org.ojai.Document;
import org.ojai.FieldPath;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.exceptions.OjaiException;

/**
 * OJAI interface which lets user build an OJAI Query that can be executed
 * on an OJAI DocumentStore.
 */
@API.Public
public interface Query {

  /**
   * Set a named query option. A query option can be used to provide hints to query execution engine.
   * However, under stable conditions, a query option can not alter the result of the query.
   * <p/>
   * Refer to OJAI Driver's documentation for the list of supported options.
   *
   * @return {@code this} for chained invocation.
   */
  public Query setOption(String optionName, Object value) throws IllegalArgumentException;

  /**
   * Returns the value of the named query option.
   *
   * @return value of the named query option, {@code null} if no such option exist.
   */
  public Object getOption(String optionName);

  /**
   * Set multiple query options for this Query.
   *
   * @return {@code this} for chained invocation.
   */
  public Query setOptions(Document options) throws IllegalArgumentException;

  /**
   * Set the commit-context for this query.
   * <p/>
   * A commit-context allows this query to "see" all the writes that happened before
   * the commit-context was retrieved from a {@link DocumentStore}.
   *
   * @see DocumentStore#beginCommitContext()
   * @see DocumentStore#beginCommitContext(String)
   * @see DocumentStore#commitAndGetContext(String)
   *
   * @throws NullPointerException if the commit context is {@code null}.
   * @throws IllegalArgumentException if the specified commit-context can not be parsed.
   *
   * @return {@code this} for chained invocation.
   */
  public Query setCommitContext(@NonNullable String commitContext) throws IllegalArgumentException;

  /**
   * Add the list of field paths to the list of projected fields.
   * If not specified, the entire Document will be returned.
   * <p/>
   * Multiple invocation will append new fields to the list.
   *
   * @return {@code this} for chained invocation.
   */
  public Query select(String... fieldPaths) throws IllegalArgumentException;

  /**
   * Add the list of field paths to the list of projected fields.
   * If not specified, the entire Document will be returned.
   * <p/>
   * Multiple invocation will append new fields to the list.
   *
   * @return {@code this} for chained invocation.
   */
  public Query select(FieldPath... fieldPaths) throws IllegalArgumentException;

  /**
   * Sets the filtering condition for the query.
   * <p/>
   * Multiple invocation will 'AND' the individual conditions.
   *
   * @return {@code this} for chained invocation.
   */
  public Query where(String conditionJson) throws OjaiException, IllegalArgumentException;

  /**
   * Sets the filtering condition for the query.
   * <p/>
   * Multiple invocation will 'AND' the individual conditions.
   *
   * @return {@code this} for chained invocation.
   */
  public Query where(QueryCondition condition) throws OjaiException, IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the ascending order of specified field paths.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   *
   * @return {@code this} for chained invocation.
   */
  public Query orderBy(String... fieldPaths) throws IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the ascending order of specified field paths.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   *
   * @return {@code this} for chained invocation.
   */
  public Query orderBy(FieldPath... fieldPaths) throws IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the specified field and order.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   * @throws IllegalArgumentException if the supplied field path can not be parsed or
   *         order is neither "ASC" or "DESC", ignoring case.
   *
   * @return {@code this} for chained invocation.
   */
  public Query orderBy(String field, String order) throws IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the specified field and order.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   *
   * @return {@code this} for chained invocation.
   */
  public Query orderBy(String field, SortOrder order) throws IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the specified field and order.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   *
   * @return {@code this} for chained invocation.
   */
  public Query orderBy(FieldPath field, SortOrder order) throws IllegalArgumentException;

  /**
   * {@code Zero} (0) based index which specifies number of Documents to skip before
   * returning any result. Negative values are not permitted.
   * <p/>
   * Multiple invocation will overwrite the previous value.
   *
   * @return {@code this} for chained invocation.
   */
  public Query offset(long offset) throws IllegalArgumentException;

  /**
   * Restricts the maximum number of documents returned from this query
   * to the specified value. Negative values are not permitted.
   * <p/>
   * Multiple invocation will overwrite the previous value.
   *
   * @return {@code this} for chained invocation.
   */
  public Query limit(long limit) throws IllegalArgumentException;

}
