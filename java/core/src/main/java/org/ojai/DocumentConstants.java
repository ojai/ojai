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
package org.ojai;

public class DocumentConstants {

  /**
   * Name of the key used to refer the entire Document.
   */
  public static final String DOCUMENT_KEY = "$$document";

  /**
   * FieldPath of the key used to refer the entire Document.
   */
  public static final FieldPath DOCUMENT_FIELD = FieldPath.parseFrom(DOCUMENT_KEY);

  /**
   * Name of the key used to refer the id of a Document.
   */
  public static final String ID_KEY = "_id";

  /**
   * FieldPath of the key used to refer the id of a Document.
   */
  public static final FieldPath ID_FIELD = FieldPath.parseFrom(ID_KEY);

}
