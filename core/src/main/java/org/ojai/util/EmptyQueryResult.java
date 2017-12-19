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
package org.ojai.util;

import org.ojai.Document;
import org.ojai.json.impl.JsonDocument;
import org.ojai.store.QueryResult;

/**
 * A QueryResult that returns no documents.
 */
public class EmptyQueryResult extends EmptyDocumentStream implements QueryResult {


  @Override
  public Document getQueryPlan() {
    return new JsonDocument();
  }

}
