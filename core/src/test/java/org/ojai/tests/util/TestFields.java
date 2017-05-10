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
package org.ojai.tests.util;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.ojai.FieldPath;
import org.ojai.util.Fields;

public class TestFields {

  @Test
  public void testToFieldPathArray_NullArray() {
    assertArrayEquals(null, Fields.toFieldPathArray((String[])null));
  }

  @Test
  public void testToFieldPathArray_EmptyArray() {
    assertArrayEquals(new FieldPath[0], Fields.toFieldPathArray(new String[0]));
  }

  @Test
  public void testToFieldPathArray_SingleFieldPath() {
    assertArrayEquals(new FieldPath[] {FieldPath.parseFrom("a")},
        Fields.toFieldPathArray("a"));
  }

  @Test
  public void testToFieldPathArray_MultiFieldPath() {
    assertArrayEquals(new FieldPath[] {FieldPath.parseFrom("a"), FieldPath.parseFrom("b")},
        Fields.toFieldPathArray("a", "b"));
  }

}
