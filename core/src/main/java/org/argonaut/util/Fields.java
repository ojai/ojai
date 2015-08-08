/**
 * Copyright (c) 2014 MapR, Inc.
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
package org.argonaut.util;

import org.argonaut.annotation.API;

@API.Public
public class Fields {

  public static String quoteFieldName(String fieldName) {
    if (fieldName.charAt(0)  == '`' && fieldName.charAt(fieldName.length()-1)  == '`') {
      return fieldName; // already quoted
    } else {
      for (int i = 0; i < fieldName.length(); i++) {
        char ch = fieldName.charAt(i);
        if ("`-/ \t\n\r\f".indexOf(ch) != -1) {
          return '`' + fieldName + '`';
        }
      }
    }
    return fieldName; // no need to quote
  }

  public static String unquoteFieldName(String fieldName) {
    if (fieldName.charAt(0)  == '`' && fieldName.charAt(fieldName.length()-1)  == '`') {
      return fieldName.substring(1, fieldName.length()-1); // quoted
    }
    return fieldName; // not quoted
  }

}
