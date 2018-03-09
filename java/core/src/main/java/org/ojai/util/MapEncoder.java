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
package org.ojai.util;

import java.util.Map;

import org.ojai.DocumentReader;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.exceptions.EncodingException;
import org.ojai.exceptions.TypeException;

/**
 * @deprecated Use methods from {@link DocumentReaders} class.  
 *
 */
@API.Internal
@Deprecated
public class MapEncoder {

  /**
   * Encodes values from the specified DocumentReader into a Java Map. 
   * 
   * @param reader
   * @return a Map view of the Document
   * @throws EncodingException
   * @throws TypeException
   */
  public static Map<String, Object> encode(@NonNullable DocumentReader reader)
      throws EncodingException, TypeException {
    return DocumentReaders.encode(reader);
  }

}
