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

import java.util.Iterator;

import org.ojai.Buildable;
import org.ojai.Container;
import org.ojai.Document;
import org.ojai.DocumentListener;
import org.ojai.DocumentStream;
import org.ojai.FieldPath;
import org.ojai.JsonString;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.annotation.API.Nullable;
import org.ojai.exceptions.OjaiException;
import org.ojai.exceptions.QueryTimeoutException;

/**
 * OJAI interface which lets users build an OJAI Query that can be executed
 * on an OJAI DocumentStore.
 */
@API.Public
@API.ImmutableOnBuild
public interface Query extends Buildable, Container, JsonString {

  /**
   * OJAI field name for the {@link #select(FieldPath...)} operator.
   */
  public static final String SELECT   = "$select";

  /**
   * OJAI field name for the {@link #orderBy(FieldPath...)} operator
   */
  public static final String ORDERBY  = "$orderby";

  /**
   * OJAI field name for the {@link #where(QueryCondition)} operator
   */
  public static final String WHERE    = "$where";

  /**
   * OJAI field name for the {@link #limit(long)} operator
   */
  public static final String LIMIT    = "$limit";

  /**
   * OJAI field name for the {@link #offset(long)} operator
   */
  public static final String OFFSET   = "$offset";

  /**
   * Sets a named query option. A query option can be used to provide hints to query execution engine.
   * However, under stable conditions, a query option can not alter the result of the query.
   * <p/>
   * Refer to OJAI Driver's documentation for the list of supported options.
   *
   * @return {@code this} for chained invocation
   */
  public Query setOption(@NonNullable String optionName, @NonNullable Object value) throws IllegalArgumentException;

  /**
   * Returns the value of the named query option.
   *
   * @return value of the named query option; {@code null} if no such option exist
   */
  public Object getOption(@NonNullable String optionName);

  /**
   * Sets multiple query options for this Query.
   *
   * @return {@code this} for chained invocation
   */
  public Query setOptions(@Nullable Document options) throws IllegalArgumentException;

  /**
   * Sets a duration after which the query will fails with {@link QueryTimeoutException}.
   * <p/>
   * A query timeout occurs when the specified time has passed since a query response was requested,
   * either by calling {@link Iterator#next()} on iterator of query's {@link DocumentStream},
   * or between successive callback of {@link DocumentListener#documentArrived(Document)}.
   *
   * @return {@code this} for chained invocation
   *
   * @throws IllegalArgumentException If the timeout value is negative
   */
  public Query setTimeout(long timeoutInMilliseconds) throws IllegalArgumentException;

  /**
   * Sets the writes-context for this query.
   * <p/>
   * A writes-context allows this query to "see" all the writes that happened inside
   * the writes-context of a {@link DocumentStore}.
   *
   * @see DocumentStore#beginTrackingWrites()
   * @see DocumentStore#beginTrackingWrites(String)
   * @see DocumentStore#endTrackingWrites()
   *
   * @throws NullPointerException if the commit context is {@code null}
   * @throws IllegalArgumentException if the specified writes-context can not be parsed
   *
   * @return {@code this} for chained invocation
   */
  public Query waitForTrackedWrites(@NonNullable String writesContext) throws IllegalArgumentException;

  /**
   * Adds the list of field paths to the list of projected fields.
   * If not specified, the entire Document will be returned.
   * <p/>
   * Multiple invocation will append new fields to the list.
   *
   * @return {@code this} for chained invocation
   */
  public Query select(@NonNullable String... fieldPaths) throws IllegalArgumentException;

  /**
   * Adds the list of field paths to the list of projected fields.
   * If not specified, the entire Document will be returned.
   * <p/>
   * Multiple invocation will append new fields to the list.
   *
   * @return {@code this} for chained invocation
   */
  public Query select(@NonNullable FieldPath... fieldPaths) throws IllegalArgumentException;

  /**
   * Sets the filtering condition for the query.
   * <p/>
   * Multiple invocation will 'AND' the individual conditions.
   *
   * @return {@code this} for chained invocation
   */
  public Query where(@NonNullable String conditionJson) throws OjaiException, IllegalArgumentException;

  /**
   * Sets the filtering condition for the query.
   * <p/>
   * Multiple invocation will 'AND' the individual conditions.
   *
   * @return {@code this} for chained invocation
   */
  public Query where(@NonNullable QueryCondition condition) throws OjaiException, IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the ascending order of specified field paths.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once
   *
   * @return {@code this} for chained invocation
   */
  public Query orderBy(@NonNullable String... fieldPaths) throws IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the ascending order of specified field paths.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once
   *
   * @return {@code this} for chained invocation
   */
  public Query orderBy(@NonNullable FieldPath... fieldPaths) throws IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the specified field and order.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once
   * @throws IllegalArgumentException if the supplied field path can not be parsed or
   *         order is neither "ASC" or "DESC", ignoring case
   *
   * @return {@code this} for chained invocation
   */
  public Query orderBy(@NonNullable String field, @NonNullable String order) throws IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the specified field and order.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once
   *
   * @return {@code this} for chained invocation
   */
  public Query orderBy(@NonNullable String field, @NonNullable SortOrder order) throws IllegalArgumentException;

  /**
   * Sets the sort ordering of the returned Documents to the specified field and order.
   * <p/>
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once
   *
   * @return {@code this} for chained invocation
   */
  public Query orderBy(@NonNullable FieldPath field, @NonNullable SortOrder order) throws IllegalArgumentException;

  /**
   * {@code Zero} (0) based index which specifies number of Documents to skip before
   * returning any result. Negative values are not permitted.
   * <p/>
   * Multiple invocation will overwrite the previous value.
   *
   * @return {@code this} for chained invocation
   */
  public Query offset(long offset) throws IllegalArgumentException;

  /**
   * Restricts the maximum number of documents returned from this query
   * to the specified value. Negative values are not permitted.
   * <p/>
   * Multiple invocation will overwrite the previous value.
   *
   * @return {@code this} for chained invocation
   */
  public Query limit(long limit) throws IllegalArgumentException;

  /**
   * Builds this Query object and make it immutable.
   *
   * @return {@code this} for chaining
   */
  @Override
  public Query build();

}
