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
package org.ojai.store;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.ojai.Document;
import org.ojai.FieldPath;
import org.ojai.Value;
import org.ojai.annotation.API;
import org.ojai.types.Interval;

/**
 * APIs to help perform modification of a Document.
 */
@API.Public
public interface DocumentMutation extends Iterable<MutationOp> {

  /**
   * Empties this Mutation object.
   */
  public DocumentMutation empty();

  /**
   * Sets the value of given field to the specified value. If the value of
   * given field name doesn't exist in the existing document on the server
   * then new field will be created.
   *
   * If the value for the given field name exists in the existing document
   * on server and its of the same type as the new value then existing value
   * will be replaced by new value.
   *
   * If the value of given field name exists and it is not same type as the
   * type of newly set value then mutation will fail.
   *
   * @param path path of the field that needs to be updated
   * @return this Mutation object
   */
  public DocumentMutation setNull(String path);

  public DocumentMutation setNull(FieldPath path);

  public DocumentMutation set(String path, Value v);

  public DocumentMutation set(FieldPath path, Value v);

  public DocumentMutation set(String path, boolean b);

  public DocumentMutation set(FieldPath path, boolean b);

  public DocumentMutation set(String path, short s);

  public DocumentMutation set(FieldPath path, short s);

  public DocumentMutation set(String path, byte b);

  public DocumentMutation set(FieldPath path, byte b);

  public DocumentMutation set(String path, int i);

  public DocumentMutation set(FieldPath path, int i);

  public DocumentMutation set(String path, long l);

  public DocumentMutation set(FieldPath path, long l);

  public DocumentMutation set(String path, float f);

  public DocumentMutation set(FieldPath path, float f);

  public DocumentMutation set(String path, double d);

  public DocumentMutation set(FieldPath path, double d);

  public DocumentMutation set(String path, String value);

  public DocumentMutation set(FieldPath path, String value);

  public DocumentMutation set(String path, BigDecimal bd);

  public DocumentMutation set(FieldPath path, BigDecimal bd);

  public DocumentMutation set(String path, Time t);

  public DocumentMutation set(FieldPath path, Time t);

  public DocumentMutation set(String path, Timestamp t);

  public DocumentMutation set(FieldPath path, Timestamp t);

  public DocumentMutation set(String path, Date d);

  public DocumentMutation set(FieldPath path, Date d);

  public DocumentMutation set(String path, List<? extends Object> value);

  public DocumentMutation set(FieldPath path, List<? extends Object> value);

  public DocumentMutation set(String path, Interval intv);

  public DocumentMutation set(FieldPath path, Interval intv);

  public DocumentMutation set(String path, ByteBuffer bb);

  public DocumentMutation set(FieldPath path, ByteBuffer bb);

  public DocumentMutation set(String path, Map<String, ? extends Object> value);

  public DocumentMutation set(FieldPath path, Map<String, ? extends Object> value);

  public DocumentMutation set(String path, Document value);

  public DocumentMutation set(FieldPath path, Document value);


  /**
   * Sets or replaces the value of a given field with the new value given in the API.
   * This is a fast API that doesn't require a read on the server to validate
   * the path or type.
   *
   * If any element in the specified field path doesn't exist then that element
   * will be created on the server.
   * Example : Field is "a.b.c" and value is int 10.
   * Suppose the server has only the field "a" of type map, then a setOrReplace of "a.b.c"
   * will create element "b" of type map inside "a". It will also create an element
   * named "c" of type integer inside map "b".
   *
   * If any element specified in the field path is of a different type
   * than the existing field element on the server, it will be deleted and replaced by
   * a new element.
   * Example : Field is "a.b.c" and value is int 10.
   * Suppose on the server, there is a field "a" of type array. This operation
   * will delete "a", create new element "a" of type map, and insert "b.c" in it.
   *
   * @param path path of the field that needs to be updated
   * @return this Mutation object
   */
  public DocumentMutation setOrReplace(String path, Value v);

  public DocumentMutation setOrReplace(FieldPath path, Value v);

  public DocumentMutation setOrReplaceNull(String path);

  public DocumentMutation setOrReplaceNull(FieldPath path);

  public DocumentMutation setOrReplace(String path, boolean b);

  public DocumentMutation setOrReplace(FieldPath path, boolean b);

  public DocumentMutation setOrReplace(String path, short s);

  public DocumentMutation setOrReplace(FieldPath path, short s);

  public DocumentMutation setOrReplace(String path, byte b);

  public DocumentMutation setOrReplace(FieldPath path, byte b);

  public DocumentMutation setOrReplace(String path, int i);

  public DocumentMutation setOrReplace(FieldPath path, int i);

  public DocumentMutation setOrReplace(String path, long l);

  public DocumentMutation setOrReplace(FieldPath path, long l);

  public DocumentMutation setOrReplace(String path, float f);

  public DocumentMutation setOrReplace(FieldPath path, float f);

  public DocumentMutation setOrReplace(String path, double d);

  public DocumentMutation setOrReplace(FieldPath path, double d);

  public DocumentMutation setOrReplace(String path, String value);

  public DocumentMutation setOrReplace(FieldPath path, String value);

  public DocumentMutation setOrReplace(String path, BigDecimal bd);

  public DocumentMutation setOrReplace(FieldPath path, BigDecimal bd);

  public DocumentMutation setOrReplace(String path, Time t);

  public DocumentMutation setOrReplace(FieldPath path, Time t);

  public DocumentMutation setOrReplace(String path, Timestamp t);

  public DocumentMutation setOrReplace(FieldPath path, Timestamp t);

  public DocumentMutation setOrReplace(String path, Date d);

  public DocumentMutation setOrReplace(FieldPath path, Date d);

  public DocumentMutation setOrReplace(String path, List<? extends Object> value);

  public DocumentMutation setOrReplace(FieldPath path, List<? extends Object> value);

  public DocumentMutation setOrReplace(String path, Interval intv);

  public DocumentMutation setOrReplace(FieldPath path, Interval intv);

  public DocumentMutation setOrReplace(String path, ByteBuffer bb);

  public DocumentMutation setOrReplace(FieldPath path, ByteBuffer bb);

  public DocumentMutation setOrReplace(String path, Map<String, ? extends Object> value);

  public DocumentMutation setOrReplace(FieldPath path,
      Map<String, ? extends Object> value);

  public DocumentMutation setOrReplace(String path, Document value);

  public DocumentMutation setOrReplace(FieldPath path, Document value);

  /**
   * Appends elements to an existing array. If the field doesn't exist on server,
   * it will be created and this operation proceeds. If the field already exists,
   * this operation will validate that the existing field is of the array type.
   * If it is of a different type, then this operation will fail.
   */
  public DocumentMutation append(String path, List<? extends Object> value);

  public DocumentMutation append(FieldPath path, List<? extends Object> value);

  /**
   * Appends elements to an existing string. This operation will first
   * validate on the server that the field specified by the given path
   * exists and is of the string type. If the field doesn't exist or is
   * of a different type, then the operation will fail.
   */
  public DocumentMutation append(String path, String value);

  public DocumentMutation append(FieldPath path, String value);

  /**
   * Appends a given byte array to the existing binary data stored on the server
   * on the given path. This operation will fail if the given field on the server is not of the
   * BINARY type. If the field path specified for append doesn't
   * exist on a server record, then this operation will create a new element at the
   * append path. This new element will be of BINARY type and its value will be
   * as specified in the parameter.
   *
   * This operation will fail if any type of the intermediate path elements specified
   * in the append field path doesn't match the type of the corresponding field in the
   * record stored on server. For example, an operation on field "a.b.c" will fail if,
   * on the server, the record a itself is an array or integer.
   *
   * This operation will perform read-modify-write on the server.
   *
   * @param path field path
   * @param value byte array value
   * @param offset offset in byte array
   * @param len length in byte array
   * @return this Mutation Object
   */
  public DocumentMutation append(String path, byte[] value, int offset, int len);

  public DocumentMutation append(FieldPath path, byte[] value, int offset, int len);

  public DocumentMutation append(String path, byte[] value);

  public DocumentMutation append(FieldPath path, byte[] value);

  public DocumentMutation append(String path, ByteBuffer value);

  public DocumentMutation append(FieldPath path, ByteBuffer value);

  /**
   * Merges the existing value on the server for the given field path with the new map
   * provided in the input. This operation will first validate that the entry with the
   * given field name exists on the server and is of the MAP type.
   *
   * If the entry is of a type other than MAP, then the operation will fail.
   * If the field path specified for merge doesn't
   * exist in the record on the server, then this operation will create a new element at the
   * given path. This new element will be of the MAP type and its value will be
   * as specified in the parameter.
   *
   * This operation will fail if any type of intermediate path elements specified
   * in the append field path doesn't match the type of the corresponding field in the
   * record stored on the server. For example, an operation on field "a.b.c" will fail if,
   * on the server, record a itself is an array or integer.
   *
   * @param path field path
   * @param value the value to be merged with existing value of a field on the server
   * @return this record mutation
   */
  public DocumentMutation merge(String path, Document value);

  public DocumentMutation merge(FieldPath path, Document value);

  public DocumentMutation merge(String path, Map<String, Object> value);

  public DocumentMutation merge(FieldPath path, Map<String, Object> value);

  /**
   * Atomically applies an increment to a given field (in dot separated notation)
   * of the given row id.
   *
   * If the field path specified for the incremental change doesn't
   * exist in the server record then this operation will create a new element at the
   * given path. This new element will be of same type as the value specified
   * in the parameter.
   *
   * This operation will fail if the type of any intermediate path elements specified
   * in the append field path doesn't match the type of the corresponding field in the
   * record stored on the server. For example, an operation on field "a.b.c" will fail
   * if, on the server, record a itself is an array or integer.
   *
   * If the field doesn't exist on
   * the server then it will be created with the type of given incremental value.
   * An increment operation can be applied on any of the numeric types
   * of a field, such as byte, short, int, long, float, double, or decimal.
   * The operation will fail if the increment is applied to a field
   * that is of a non-numeric type.
   *
   * The increment operation won't change the type of the existing value stored in
   * the given field for the row. The resultant value of the field will be
   * truncated based on the original type of the field.
   *
   * For example, field 'score' is of type int and contains 60. The increment
   * '5.675', a double, is applied. The resultant value of the field
   * will be 65 (65.675 will be truncated to 65).
   *
   * @param path field name in dot separated notation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(FieldPath path, byte inc);

  public DocumentMutation increment(String path, byte inc);

  public DocumentMutation increment(FieldPath path, short inc);

  public DocumentMutation increment(String path, short inc);

  public DocumentMutation increment(String path, int inc);

  public DocumentMutation increment(FieldPath path, int inc);

  public DocumentMutation increment(FieldPath path, long inc);

  public DocumentMutation increment(String path, long inc);

  public DocumentMutation increment(String path, float inc);

  public DocumentMutation increment(FieldPath path, float inc);

  public DocumentMutation increment(String path, double inc);

  public DocumentMutation increment(FieldPath path, double inc);

  public DocumentMutation increment(String path, BigDecimal inc);

  public DocumentMutation increment(FieldPath path, BigDecimal inc);

  /**
   * Deletes the field at the given path.
   * If the field does not exist, this method silently returns without doing anything.
   * For example, if a delete operation is attempted on a.b.c, and a.b is an array on the
   * server, then a.b.c will not be deleted.
   */
  public DocumentMutation delete(String path);

  public DocumentMutation delete(FieldPath path);

}
