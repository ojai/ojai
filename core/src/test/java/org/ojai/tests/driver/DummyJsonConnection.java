/**
 * Copyright (c) 2017 MapR, Inc.
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
package org.ojai.tests.driver;

import java.util.Map;

import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.OjaiException;
import org.ojai.store.Connection;
import org.ojai.store.DocumentMutation;
import org.ojai.store.DocumentStore;
import org.ojai.store.Driver;
import org.ojai.store.Query;
import org.ojai.store.QueryCondition;

import com.google.common.base.Preconditions;

public class DummyJsonConnection implements Connection {
  private final DummyJsonDriver driver;
  private final String url;
  private final Document options;

  public DummyJsonConnection(DummyJsonDriver dummyJsonDriver, String connectionUrl, Document options) {
    Preconditions.checkNotNull(dummyJsonDriver);

    driver = dummyJsonDriver;
    this.options = options;
    url = connectionUrl;
  }

  @Override
  public DocumentStore getStore(String storeName) throws OjaiException {
    throw new UnsupportedOperationException();
  }

  @Override
  public DocumentStore getStore(String storeName, Document options) throws OjaiException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Document newDocument() {
    return driver.newDocument();
  }

  @Override
  public Document newDocument(String jsonString) throws DecodingException {
    return driver.newDocument(jsonString);
  }

  @Override
  public Document newDocument(Map<String, Object> map) throws DecodingException {
    return driver.newDocument(map);
  }

  @Override
  public Document newDocument(Object bean) throws DecodingException {
    return driver.newDocument(bean);
  }

  @Override
  public DocumentBuilder newDocumentBuilder() {
    return driver.newDocumentBuilder();
  }

  @Override
  public DocumentMutation newMutation() {
    return driver.newMutation();
  }

  @Override
  public QueryCondition newCondition() {
    return driver.newCondition();
  }

  @Override
  public Query newQuery() {
    return driver.newQuery();
  }

  @Override
  public Query newQuery(String queryJson) {
    return driver.newQuery(queryJson);
  }

  @Override
  public Driver getDriver() {
    return driver;
  }

  @Override
  public void close() {
  }

  public String getUrl() {
    return url;
  }

  public Document getOptions() {
    return options;
  }

}
