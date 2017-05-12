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

import org.ojai.FieldPath;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.annotation.API.Nullable;
import org.ojai.json.impl.ConstantImpl;

@API.Public
public class Fields {

  public static final char SEGMENT_QUOTE_CHAR = '"';

  public static String quoteFieldName(@NonNullable String fieldName) {
    if (fieldName.length() == 0) {
      return ConstantImpl.EMPTYSTRING;
    }

    if (fieldName.charAt(0)  == SEGMENT_QUOTE_CHAR && fieldName.charAt(fieldName.length()-1)  == SEGMENT_QUOTE_CHAR) {
      return fieldName; // already quoted
    } else {
      for (int i = 0; i < fieldName.length(); i++) {
        char ch = fieldName.charAt(i);
        if ("`-/ \t\n\r\f".indexOf(ch) != -1) {
          return SEGMENT_QUOTE_CHAR + fieldName + SEGMENT_QUOTE_CHAR;
        }
      }
    }
    return fieldName; // no need to quote
  }

  public static String unquoteFieldName(@NonNullable String fieldName) {
    if (fieldName.length() == 0) {
      return ConstantImpl.EMPTYSTRING;
    }

    if (fieldName.charAt(0)  == SEGMENT_QUOTE_CHAR && fieldName.charAt(fieldName.length()-1)  == SEGMENT_QUOTE_CHAR) {
      return fieldName.substring(1, fieldName.length()-1); // quoted
    }
    return fieldName; // not quoted
  }

  private static final FieldPath[] EMPTRY_FIELDPATH_ARRAY = new FieldPath[0];
  public static FieldPath[] toFieldPathArray(@Nullable String... fieldPaths) {
    if (fieldPaths == null) {
      return null;
    } else if (fieldPaths.length == 0) {
      return EMPTRY_FIELDPATH_ARRAY;
    } else {
      final FieldPath[] fpArray = new FieldPath[fieldPaths.length];
      for (int i = 0; i < fieldPaths.length; i++) {
        fpArray[i] = FieldPath.parseFrom(fieldPaths[i]);
      }
      return fpArray;
    }
  }

}
