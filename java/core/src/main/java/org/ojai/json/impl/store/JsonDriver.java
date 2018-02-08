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
package org.ojai.json.impl.store;

import java.util.Map;

import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.OjaiException;
import org.ojai.json.Json;
import org.ojai.json.JsonConsts;
import org.ojai.json.impl.JsonValueBuilder;
import org.ojai.store.Connection;
import org.ojai.store.DocumentMutation;
import org.ojai.store.Driver;
import org.ojai.store.DriverManager;
import org.ojai.store.Query;
import org.ojai.store.QueryCondition;
import org.ojai.store.ValueBuilder;

import com.google.common.base.Preconditions;

public class JsonDriver implements Driver {
  private final static JsonDriver DRIVER_INSTANCE = new JsonDriver();

  static {
    DriverManager.registerDriver(DRIVER_INSTANCE);
  }

  @Override
  public Document newDocument() {
    return Json.newDocument();
  }

  @Override
  public Document newDocument(String documentJson) throws DecodingException {
    return Json.newDocument(documentJson);
  }

  @Override
  public Document newDocument(Map<String, Object> map) throws DecodingException {
    return Json.newDocument(map);
  }

  @Override
  public Document newDocument(Object bean) throws DecodingException {
    return Json.newDocument(bean);
  }

  @Override
  public DocumentBuilder newDocumentBuilder() {
    return Json.newDocumentBuilder();
  }

  @Override
  public DocumentMutation newMutation() {
    throw new UnsupportedOperationException();
  }

  @Override
  public QueryCondition newCondition() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Query newQuery() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Query newQuery(String queryJson) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean accepts(String url) {
    Preconditions.checkNotNull(url);
    return url.startsWith(JsonConsts.BASE_URL);
  }

  @Override
  public Connection connect(String url, Document options) throws OjaiException {
    Preconditions.checkArgument(accepts(url));
    return new JsonConnection(this, url, options);
  }

  @Override
  public String getName() {
    return JsonConsts.DRIVER_NAME;
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    return true;
  }

  @Override
  public ValueBuilder getValueBuilder() {
    return JsonValueBuilder.INSTANCE;
  }

}
