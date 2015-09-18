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
package org.ojai.beans;

import static org.ojai.beans.jackson.JacksonHelper.MAPPER;

import java.io.IOException;

import org.ojai.Document;
import org.ojai.DocumentBuilder;
import org.ojai.DocumentReader;
import org.ojai.annotation.API;
import org.ojai.beans.jackson.DocumentGenerator;
import org.ojai.beans.jackson.DocumentParser;
import org.ojai.exceptions.DecodingException;
import org.ojai.exceptions.EncodingException;

@API.Public
public class BeanCodec {

  public static Document decode(DocumentBuilder db, Object bean)
      throws DecodingException {
    if (bean == null) return null;

    DocumentGenerator gen = new DocumentGenerator(db);
    try {
      MAPPER.writeValue(gen, bean);
      return gen.getDocument();
    } catch (Exception e) {
      throw new DecodingException("Failed to convert the java bean to Document", e);
    }
  }

  public static <T> T encode(DocumentReader dr, Class<T> beanClass)
      throws EncodingException {
    if (dr == null) return null;

    try {
      return MAPPER.readValue(new DocumentParser(dr), beanClass);
    } catch (IOException e) {
      throw new EncodingException("Failed to create java bean from Document", e);
    }
  }

}
