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

import java.io.Closeable;
import java.util.Map;

import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.OjaiException;

/**
 * A logical connection to an OJAI data source. This could be a connection to
 * a database server, a distributed cluster or a set of distributed clusters.
 */
public interface Connection extends Closeable {
  /**
   * Returns a handle to an OJAI DocumentStore specified by the given name
   * or path.
   *
   * @param storeName name or path of an OJAI data source table/store.
   */
  public DocumentStore getStore(String storeName) throws OjaiException;

  /**
   * <p>Creates and returns a new, empty instance of an OJAI Document.</p>
   * This is an alias to getDriver().newDocument().
   */
  public Document newDocument();

  /**
   * <p>Returns a new instance of OJAI Document parsed from the specified JSON string.</p>
   * This is an alias to getDriver().newDocument().
   */
  public Document newDocument(String jsonString) throws DecodingException;

  /**
   * <p>Returns a new instance of Document constructed from the specified Map.</p>
   * This is an alias to getDriver().newDocument().
   */
  public Document newDocument(Map<String, Object> map) throws DecodingException;

  /**
   * <p>Returns a new instance of Document built from the specified Java bean.</p>
   * This is an alias to getDriver().newDocument().
   */
  public Document newDocument(Object bean) throws DecodingException;

  /**
   * <p>Returns a new DocumentBuilder object.</p>
   * This is an alias to getDriver().newDocumentBuilder().
   */
  public DocumentBuilder newDocumentBuilder();

  /**
   * <p>Creates and returns a new DocumentMutation object.<p>
   * This is an alias to getDriver().newMutation().
   */
  public DocumentMutation newMutation();

  /**
   * <p>Creates and returns a new DocumentMutation object decoded from the supplied JSON String.</p>
   * This is an alias to getDriver().newMutation().
   */
  public DocumentMutation newMutation(String mutationJson);

  /**
   * <p>Creates and returns a new QueryCondition object.</p>
   * This is an alias to getDriver().newCondition().
   */
  public QueryCondition newCondition();

  /**
   * <p>Creates and returns a new QueryCondition object decoded from the supplied JSON String.</p>
   * This is an alias to getDriver().newCondition().
   */
  public QueryCondition newCondition(String conditionJson);

  /**
   * <p>Creates and returns a new Query object.</p>
   * This is an alias to getDriver().newQuery().
   */
  public Query newQuery();

  /**
   * <p>Creates and returns a new Query object decoded from the supplied JSON String.</p>
   * This is an alias to getDriver().newQuery().
   */
  public Query newQuery(String queryJson);

  /**
   * Returns the OJAI Driver instance associated with this connection.
   */
  public Driver getDriver();

  /**
   * <p>Closes this connection and releases any system resources associated
   * with it. If the connection is already closed then invoking this
   * method has no effect.</p>
   *
   * <p>This method does not throw any exception.</p>
   */
  public void close();

}