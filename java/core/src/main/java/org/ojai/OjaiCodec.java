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
package org.ojai;

import org.ojai.Document;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.EncodingException;
import org.ojai.store.DocumentMutation;
import org.ojai.store.Query;
import org.ojai.store.QueryCondition;

/**
 * Implementation of this interface can be used to encode/decode OJAI Object to/from a target/source
 * format <b>&lt;T></b>
 *
 * @param <T> the encoded object type
 */
@API.Public
public interface OjaiCodec<T> {

  /**
   * Encodes an OJAI {@link Document} into an instance of &lt;T&gt;
   *
   * @param document OJAI Document to encode
   * @return an instance of &lt;T&gt;
   * @throws EncodingException
   */
  public T encodeDocument(@NonNullable Document document) throws EncodingException;

  /**
   * Decodes an instance of &lt;T&gt; into an OJAI {@link Document}
   *
   * @param encodedDocument an instance of &lt;T&gt; containing encoded OJAI Document
   * @return an OJAI Document
   * @throws DecodingException
   */
  public Document decodeDocument(@NonNullable T encodedDocument) throws DecodingException;

  /**
   * Encodes an OJAI {@link Query} into an instance of &lt;T&gt;
   *
   * @param query OJAI Query to encode
   * @return an instance of &lt;T&gt;
   * @throws EncodingException
   */
  public T encodeQuery(@NonNullable Query query) throws EncodingException;

  /**
   * Decodes an instance of &lt;T&gt; into an OJAI {@link Query}
   *
   * @param encodedQuery an instance of &lt;T&gt; containing encoded OJAI Query
   * @return an OJAI Query
   * @throws DecodingException
   */
  public Query decodeQuery(@NonNullable T encodedQuery) throws DecodingException;

  /**
   * Encodes an OJAI {@link QueryCondition} into an instance of &lt;T&gt;
   *
   * @param condition OJAI QueryCondition to encode
   * @return an instance of &lt;T&gt;
   * @throws EncodingException
   */
  public T encodeCondition(@NonNullable QueryCondition condition) throws EncodingException;

  /**
   * Decodes an instance of &lt;T&gt; into an OJAI {@link QueryCondition}
   *
   * @param encodedCondition an instance of &lt;T&gt; containing encoded OJAI QueryCondition
   * @return an OJAI {@link QueryCondition}
   * @throws DecodingException
   */
  public QueryCondition decodeCondition(@NonNullable T encodedCondition) throws DecodingException;
  
  /**
   * Encodes an OJAI {@link DocumentMutation} into an instance of &lt;T&gt;
   *
   * @param condition OJAI DocumentMutation to encode
   * @return an instance of &lt;T&gt;
   * @throws EncodingException
   */
  public T encodeMutation(@NonNullable DocumentMutation mutation) throws EncodingException;
  
  /**
   * Decodes an instance of &lt;T&gt; into an OJAI {@link DocumentMutation}
   *
   * @param encodedMutation an instance of &lt;T&gt; containing encoded OJAI DocumentMutation
   * @return an OJAI {@link DocumentMutation}
   * @throws DecodingException
   */
  public DocumentMutation decodeMutation(@NonNullable T encodedMutation) throws DecodingException;

}
