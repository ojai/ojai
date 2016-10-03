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
import org.ojai.exceptions.OjaiException;

/**
 * OJAI interface which lets user build an OJAI Query that can be executed
 * on an OJAI DocumentStore.
 */
public interface Query {

  /**
   * <p>Set a named query option. A query option can be used to provide hints to query execution engine.
   * However, under stable conditions, a query option can not alter the result of the query</p>
   * <p>Refer to OJAI Driver's documentation for the list of supported options.</p>
   */
  public Query setOption(String optionName, Object value) throws IllegalArgumentException;

  /**
   * Returns the value of the named query option.
   *
   * @return value of the named query option, {@code null} if no such option exist.
   */
  public Object getOption(String optionName);

  /**
   * <p>Set multiple query options.
   * If not specified, the entire Document will be returned.</p>
   *
   * Multiple invocation will append new fields to the list.
   */
  public Query setOptions(Document options) throws IllegalArgumentException;

  /**
   * <p>Add the list of field paths to the list of projected fields.
   * If not specified, the entire Document will be returned.</p>
   *
   * Multiple invocation will append new fields to the list.
   */
  public Query select(String... fieldPaths) throws IllegalArgumentException;

  /**
   * <p>Add the list of field paths to the list of projected fields.
   * If not specified, the entire Document will be returned.</p>
   *
   * Multiple invocation will append new fields to the list.
   */
  public Query select(FieldPath... fieldPaths) throws IllegalArgumentException;

  /**
   * <p>Sets the filtering condition for the query.</p>
   *
   * Multiple invocation will 'AND' the individual conditions.
   */
  public Query where(String conditionJson) throws OjaiException, IllegalArgumentException;

  /**
   * <p>Sets the filtering condition for the query.</p>
   *
   * Multiple invocation will 'AND' the individual conditions.
   */
  public Query where(QueryCondition condition) throws OjaiException, IllegalArgumentException;

  /**
   * <p>Sets the sort ordering of the returned Documents to the ascending order of specified field paths.</p>
   *
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   */
  public Query orderBy(String... fieldPaths) throws IllegalArgumentException;

  /**
   * <p>Sets the sort ordering of the returned Documents to the ascending order of specified field paths.</p>
   *
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   */
  public Query orderBy(FieldPath... fieldPaths) throws IllegalArgumentException;

  /**
   * <p>Sets the sort ordering of the returned Documents to the specified field and order.</p>
   *
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   * @throws IllegalArgumentException if the supplied field path can not be parsed or
   *         order is neither "ASC" or "DESC", ignoring case.
   */
  public Query orderBy(String field, String order) throws IllegalArgumentException;

  /**
   * <p>Sets the sort ordering of the returned Documents to the specified field and order.</p>
   *
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   */
  public Query orderBy(String field, SortOrder order) throws IllegalArgumentException;

  /**
   * <p>Sets the sort ordering of the returned Documents to the specified field and order.</p>
   *
   * Multiple invocation will append the field to the sort list.
   *
   * @throws IllegalArgumentException if the same field is specified more than once.
   */
  public Query orderBy(FieldPath field, SortOrder order) throws IllegalArgumentException;

  /**
   * <p>{@code Zero} (0) based index which specifies number of Documents to skip before
   * returning any result. Negative values are not permitted.</p>
   *
   * Multiple invocation will overwrite the previous value.
   */
  public Query offset(long offset) throws IllegalArgumentException;

  /**
   * <p>Restricts the maximum number of documents returned from this query
   * to the specified value. Negative values are not permitted.</p>
   *
   * Multiple invocation will overwrite the previous value.
   */
  public Query limit(long limit) throws IllegalArgumentException;

}
