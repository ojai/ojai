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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.ojai.Document;
import org.ojai.FieldPath;
import org.ojai.JsonString;
import org.ojai.Value;
import org.ojai.Value.Type;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.types.ODate;
import org.ojai.types.OInterval;
import org.ojai.types.OTime;
import org.ojai.types.OTimestamp;

/**
 * The DocumentMutation interface defines the APIs to perform mutation of a
 * Document already stored in a DocumentStore.
 * <br/><br/>
 * Please see the following notes regarding the behavior of the API types in
 * this Interface.
 *
 * <h3>{@code set()}</h3>
 * These APIs validate the type of existing value at the specified FieldPath
 * before applying the mutation. If the field does not exist in the corresponding
 * Document in the DocumentStore, it is created. If the field exists but is not
 * of the same type as the type of new value, then the entire mutation fails.
 *
 * <h3>{@code setOrReplace()}</h3>
 * These are performant APIs that do not require or perform a read-modify-write
 * operation on the document store.<br/><br/>
 * If a segment in the specified FieldPath doesn't exist, it is created. For example:
 * <blockquote>{@code setOrReplace("a.b.c", (int) 10)}</blockquote>
 * In this example, if the Document stored in the DocumentStore has an empty MAP
 * field {@code "a"}, then a setOrReplace of {@code "a.b.c"} will create a field
 * {@code "b"} of type MAP under {@code "a"}. It will also create an field named
 * {@code "c"} of type INTEGER under {@code "b"} and set its value to 10.<br/><br/>
 *
 * If any segment specified in the FieldPath is of a different type than the
 * existing field segment on the document store, it will be deleted and replaced by
 * a new segment. For example:
 * <blockquote>{@code setOrReplace("a.b.c", (int) 10)}<br/></blockquote>
 * If the Document stored in the DocumentStore has a field "a" of type array.
 * This operation will delete "a", create new field "a" of type map, add a MAP
 * field "b" under "a" and finally create an INTEGER field "c" with value 10 under
 * "b".<br/><br/>
 * <b/>Warning:</b> These are potentially destructive operations since they do
 * not validate existence or type of any field segment in the specified FieldPath.
 *
 * <h3>{@code append()}</h3>
 * These operations perform read-modify-write on the document store and will fail if
 * type of any of the intermediate fields segment in the specified FieldPath
 * does not match the type of the corresponding field in the document stored
 * on document store. For example, an append operation on field {@code "a.b.c"} will
 * fail if, on the document store, the field {@code "a"} itself is an ARRAY or INTEGER.
 *
 * <h3>{@code merge()}</h3>
 * If the specified field is of a type other than MAP, then the operation will fail.
 * If the field doesn't exist in the Document on the document store, then this operation will
 * create a new field at the given path. This new field will be of the MAP type and
 * its value will be as specified in the parameter.
 *
 * This operation will fail if any type of intermediate field segment specified
 * in the FieldPath doesn't match the type of the corresponding field in the
 * document stored on the document store. For example, a merge operation on field {@code "a.b.c"}
 * will fail if, on the document store, the field {@code "a"} itself is an ARRAY or INTEGER.
 *
 * <h3>{@code increment()}</h3>
 * If the FieldPath specified for the incremental change doesn't exist in the
 * corresponding Document in the DocumentStore then this operation will create
 * a new field at the given path. This new field will be of same type as the
 * value specified in the parameter.
 * <br/><br/>
 * This operation will fail if the type of any intermediate fields specified
 * in the FieldPath doesn't match the type of the corresponding field in the
 * document stored on the document store. For example, an operation on field "a.b.c" will fail
 * if, on the document store, document a itself is an array or integer.
 * <br/><br/>
 * An increment operation can be applied on any of the numeric types such as byte,
 * short, int, long, float, double, or decimal. The operation will fail if the
 * increment is applied to a field that is of a non-numeric type.
 *
 * The increment operation won't change the type of the existing value stored in
 * the given field for the row. The resultant value of the field will be
 * truncated based on the original type of the field.
 *
 * For example, field 'score' is of type int and contains 60. The increment
 * '5.675', a double, is applied. The resultant value of the field will be 65
 * (65.675 will be truncated to 65).
 */
@API.Public
@API.NotThreadSafe
public interface DocumentMutation extends Iterable<MutationOp>, JsonString {

  /**
   * OJAI field name for the {@link DocumentMutation} object.
   */
  public static final String MUTATION   = "$mutation";

  /**
   * Empties this DocumentMutation object.
   */
  public DocumentMutation empty();

  /**
   * Sets the field at the given FieldPath to {@link Type#NULL NULL} Value.
   *
   * @param path path of the field that needs to be updated
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setNull(@NonNullable String path);

  /**
   * Sets the field at the given FieldPath to {@link Type#NULL NULL} Value.
   *
   * @param path path of the field that needs to be updated
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setNull(@NonNullable FieldPath path);

  /**
   * Sets the field at the given FieldPath to the specified value.
   *
   * @param path path of the field that needs to be updated
   * @param value the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable Value value);

  /**
   * Sets the field at the given FieldPath to the specified value.
   *
   * @param path path of the field that needs to be updated
   * @param value the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable Value value);

  /**
   * Sets the field at the given FieldPath to the specified {@code boolean} value.
   *
   * @param path path of the field that needs to be updated
   * @param b the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, boolean b);

  /**
   * Sets the field at the given FieldPath to the specified {@code boolean} value.
   *
   * @param path path of the field that needs to be updated
   * @param b the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, boolean b);

  /**
   * Sets the field at the given FieldPath to the specified {@code byte} value.
   *
   * @param path path of the field that needs to be updated
   * @param b the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, byte b);

  /**
   * Sets the field at the given FieldPath to the specified {@code byte} value.
   *
   * @param path path of the field that needs to be updated
   * @param b the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, byte b);

  /**
   * Sets the field at the given FieldPath to the specified {@code short} value.
   *
   * @param path path of the field that needs to be updated
   * @param s the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, short s);

  /**
   * Sets the field at the given FieldPath to the specified {@code short} value.
   *
   * @param path path of the field that needs to be updated
   * @param s the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, short s);

  /**
   * Sets the field at the given FieldPath to the specified {@code int} value.
   *
   * @param path path of the field that needs to be updated
   * @param i the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, int i);

  /**
   * Sets the field at the given FieldPath to the specified {@code int} value.
   *
   * @param path path of the field that needs to be updated
   * @param i the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, int i);

  /**
   * Sets the field at the given FieldPath to the specified {@code long} value.
   *
   * @param path path of the field that needs to be updated
   * @param l the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, long l);

  /**
   * Sets the field at the given FieldPath to the specified {@code long} value.
   *
   * @param path path of the field that needs to be updated
   * @param l the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, long l);

  /**
   * Sets the field at the given FieldPath to the specified {@code float} value.
   *
   * @param path path of the field that needs to be updated
   * @param f the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, float f);

  /**
   * Sets the field at the given FieldPath to the specified {@code float} value.
   *
   * @param path path of the field that needs to be updated
   * @param f the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, float f);

  /**
   * Sets the field at the given FieldPath to the specified {@code double} value.
   *
   * @param path path of the field that needs to be updated
   * @param d the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, double d);

  /**
   * Sets the field at the given FieldPath to the specified {@code double} value.
   *
   * @param path path of the field that needs to be updated
   * @param d the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, double d);

  /**
   * Sets the field at the given FieldPath to the specified {@code String} value.
   *
   * @param path path of the field that needs to be updated
   * @param value the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable String value);

  /**
   * Sets the field at the given FieldPath to the specified {@code String} value.
   *
   * @param path path of the field that needs to be updated
   * @param value the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable String value);

  /**
   * Sets the field at the given FieldPath to the specified {@code BigDecimal} value.
   *
   * @param path path of the field that needs to be updated
   * @param bd the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable BigDecimal bd);

  /**
   * Sets the field at the given FieldPath to the specified {@code BigDecimal} value.
   *
   * @param path path of the field that needs to be updated
   * @param bd the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable BigDecimal bd);

  /**
   * Sets the field at the given FieldPath to the specified {@code Date} value.
   *
   * @param path path of the field that needs to be updated
   * @param d the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable ODate d);

  /**
   * Sets the field at the given FieldPath to the specified {@code Date} value.
   *
   * @param path path of the field that needs to be updated
   * @param d the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable ODate d);

  /**
   * Sets the field at the given FieldPath to the specified {@code Time} value.
   *
   * @param path path of the field that needs to be updated
   * @param t the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable OTime t);

  /**
   * Sets the field at the given FieldPath to the specified {@code Time} value.
   *
   * @param path path of the field that needs to be updated
   * @param t the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable OTime t);

  /**
   * Sets the field at the given FieldPath to the specified {@code Timestamp} value.
   *
   * @param path path of the field that needs to be updated
   * @param ts the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable OTimestamp ts);

  /**
   * Sets the field at the given FieldPath to the specified {@code Timestamp} value.
   *
   * @param path path of the field that needs to be updated
   * @param ts the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable OTimestamp ts);

  /**
   * Sets the field at the given FieldPath to the specified {@code Interval} value.
   *
   * @param path path of the field that needs to be updated
   * @param intv the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable OInterval intv);

  /**
   * Sets the field at the given FieldPath to the specified {@code Interval} value.
   *
   * @param path path of the field that needs to be updated
   * @param intv the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable OInterval intv);

  /**
   * Sets the field at the given FieldPath to the specified {@code ByteBuffer}.
   *
   * @param path path of the field that needs to be updated
   * @param bb the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable ByteBuffer bb);

  /**
   * Sets the field at the given FieldPath to the specified {@code ByteBuffer}.
   *
   * @param path path of the field that needs to be updated
   * @param bb the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable ByteBuffer bb);

  /**
   * Sets the field at the given FieldPath to the specified {@code List}.
   *
   * @param path path of the field that needs to be updated
   * @param list the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable List<? extends Object> list);

  /**
   * Sets the field at the given FieldPath to the specified {@code List}.
   *
   * @param path path of the field that needs to be updated
   * @param list the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable List<? extends Object> list);

  /**
   * Sets the field at the given FieldPath to the specified {@code Map}.
   *
   * @param path path of the field that needs to be updated
   * @param map the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable Map<String, ? extends Object> map);

  /**
   * Sets the field at the given FieldPath to the specified {@code Map}.
   *
   * @param path path of the field that needs to be updated
   * @param map the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable Map<String, ? extends Object> map);

  /**
   * Sets the field at the given FieldPath to the specified {@code Document}.
   *
   * @param path path of the field that needs to be updated
   * @param doc the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable String path, @NonNullable Document doc);

  /**
   * Sets the field at the given FieldPath to the specified {@code Document}.
   *
   * @param path path of the field that needs to be updated
   * @param doc the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation set(@NonNullable FieldPath path, @NonNullable Document doc);

  /**
   * Sets or replaces the field at the given FieldPath to {@link Type#NULL NULL} Value.
   *
   * @param path FieldPath in the document that needs to be updated
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplaceNull(@NonNullable String path);

  /**
   * Sets or replaces the field at the given FieldPath to {@link Type#NULL NULL} Value.
   *
   * @param path FieldPath in the document that needs to be updated
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplaceNull(@NonNullable FieldPath path);

  /**
   * Sets or replaces the field at the given FieldPath to the new value.
   *
   * @param path FieldPath in the document that needs to be updated
   * @param value the new value to set or replace at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable Value value);

  /**
   * Sets or replaces the field at the given FieldPath to the new value.
   *
   * @param path FieldPath in the document that needs to be updated
   * @param value the new value to set or replace at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable Value value);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code boolean} value.
   *
   * @param path path of the field that needs to be updated
   * @param b the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, boolean b);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code boolean} value.
   *
   * @param path path of the field that needs to be updated
   * @param b the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, boolean b);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code byte} value.
   *
   * @param path path of the field that needs to be updated
   * @param b the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, byte b);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code byte} value.
   *
   * @param path path of the field that needs to be updated
   * @param b the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, byte b);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code short} value.
   *
   * @param path path of the field that needs to be updated
   * @param s the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, short s);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code short} value.
   *
   * @param path path of the field that needs to be updated
   * @param s the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, short s);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code int} value.
   *
   * @param path path of the field that needs to be updated
   * @param i the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, int i);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code int} value.
   *
   * @param path path of the field that needs to be updated
   * @param i the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, int i);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code long} value.
   *
   * @param path path of the field that needs to be updated
   * @param l the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, long l);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code long} value.
   *
   * @param path path of the field that needs to be updated
   * @param l the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, long l);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code float} value.
   *
   * @param path path of the field that needs to be updated
   * @param f the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, float f);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code float} value.
   *
   * @param path path of the field that needs to be updated
   * @param f the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, float f);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code double} value.
   *
   * @param path path of the field that needs to be updated
   * @param d the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, double d);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code double} value.
   *
   * @param path path of the field that needs to be updated
   * @param d the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, double d);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code String} value.
   *
   * @param path path of the field that needs to be updated
   * @param string the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable String string);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code String} value.
   *
   * @param path path of the field that needs to be updated
   * @param string the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable String string);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code BigDecimal} value.
   *
   * @param path path of the field that needs to be updated
   * @param bd the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable BigDecimal bd);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code BigDecimal} value.
   *
   * @param path path of the field that needs to be updated
   * @param bd the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable BigDecimal bd);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Date} value.
   *
   * @param path path of the field that needs to be updated
   * @param d the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable ODate d);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Date} value.
   *
   * @param path path of the field that needs to be updated
   * @param d the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable ODate d);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Time} value.
   *
   * @param path path of the field that needs to be updated
   * @param t the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable OTime t);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Time} value.
   *
   * @param path path of the field that needs to be updated
   * @param t the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable OTime t);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Timestamp} value.
   *
   * @param path path of the field that needs to be updated
   * @param ts the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable OTimestamp ts);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Timestamp} value.
   *
   * @param path path of the field that needs to be updated
   * @param ts the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable OTimestamp ts);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Interval}.
   *
   * @param path path of the field that needs to be updated
   * @param intv the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable OInterval intv);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Interval}.
   *
   * @param path path of the field that needs to be updated
   * @param intv the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable OInterval intv);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code ByteBuffer}.
   *
   * @param path path of the field that needs to be updated
   * @param bb the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable ByteBuffer bb);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code ByteBuffer}.
   *
   * @param path path of the field that needs to be updated
   * @param bb the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable ByteBuffer bb);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code List}.
   *
   * @param path path of the field that needs to be updated
   * @param list the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable List<? extends Object> list);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code List}.
   *
   * @param path path of the field that needs to be updated
   * @param list the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable List<? extends Object> list);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Map}.
   *
   * @param path path of the field that needs to be updated
   * @param map the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable Map<String, ? extends Object> map);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Map}.
   *
   * @param path path of the field that needs to be updated
   * @param map the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable Map<String, ? extends Object> map);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Document}.
   *
   * @param path path of the field that needs to be updated
   * @param doc the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable String path, @NonNullable Document doc);

  /**
   * Sets or replaces the field at the given FieldPath to the specified
   * {@code Document}.
   *
   * @param path path of the field that needs to be updated
   * @param doc the new value to set at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation setOrReplace(@NonNullable FieldPath path, @NonNullable Document doc);

  /**
   * Appends elements of the given list to an existing ARRAY at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the specified List. If the field already exists, but is not of ARRAY type,
   * then this operation will fail.
   *
   * @param path path of the field that needs to be appended
   * @param list the List to append at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable String path, @NonNullable List<? extends Object> list);

  /**
   * Appends elements of the given list to an existing ARRAY at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the specified List. If the field already exists, but is not of ARRAY type,
   * then this operation will fail.
   *
   * @param path path of the field that needs to be appended
   * @param list the List to append at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable FieldPath path, @NonNullable List<? extends Object> list);

  /**
   * Appends the given string to an existing STRING at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the specified String. If the field already exists, but is not of STRING type,
   * then this operation will fail.
   *
   * @param path path of the field that needs to be appended
   * @param string the String to append at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable String path, @NonNullable String string);

  /**
   * Appends the given string to an existing STRING at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the specified String. If the field already exists, but is not of STRING type,
   * then this operation will fail.
   *
   * @param path path of the field that needs to be appended
   * @param string the String to append at the FieldPath
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable FieldPath path, @NonNullable String string);

  /**
   * Appends the given byte array to an existing BINARY value at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the BINARY value specified by the given byte array. If the field already exists,
   * but is not of BINARY type, then this operation will fail.
   *
   * @param path the FieldPath to apply this append operation
   * @param value the byte array to append
   * @param offset offset in byte array
   * @param len length in byte array
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable String path, @NonNullable byte[] value, int offset, int len);

  /**
   * Appends the given byte array to an existing BINARY value at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the BINARY value specified by the given byte array. If the field already exists,
   * but is not of BINARY type, then this operation will fail.
   *
   * @param path the FieldPath to apply this append operation
   * @param value the byte array to append
   * @param offset offset in byte array
   * @param len length in byte array
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable FieldPath path, @NonNullable byte[] value, int offset, int len);

  /**
   * Appends the given byte array to an existing BINARY value at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the BINARY value specified by the given byte array. If the field already exists,
   * but is not of BINARY type, then this operation will fail.
   *
   * @param path the FieldPath to apply this append operation
   * @param value the byte array to append
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable String path, @NonNullable byte[] value);

  /**
   * Appends the given byte array to an existing BINARY value at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the BINARY value specified by the given byte array. If the field already exists,
   * but is not of BINARY type, then this operation will fail.
   *
   * @param path the FieldPath to apply this append operation
   * @param value the byte array to append
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable FieldPath path, @NonNullable byte[] value);

  /**
   * Appends the given ByteBuffer to an existing BINARY value at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the BINARY value specified by the given ByteBuffer. If the field already exists,
   * but is not of BINARY type, then this operation will fail.
   *
   * @param path the FieldPath to apply this append operation
   * @param value the ByteBuffer to append
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable String path, @NonNullable ByteBuffer value);

  /**
   * Appends the given ByteBuffer to an existing BINARY value at the given FieldPath.
   * <br/><br/>
   * If the field doesn't exist on document store, it will be created and will be set to
   * the BINARY value specified by the given ByteBuffer. If the field already exists,
   * but is not of BINARY type, then this operation will fail.
   *
   * @param path the FieldPath to apply this append operation
   * @param value the ByteBuffer to append
   * @return {@code this} for chained invocation
   */
  public DocumentMutation append(@NonNullable FieldPath path, @NonNullable ByteBuffer value);

  /**
   * Merges the existing MAP at the given FieldPath with the specified Document.
   * <br/><br/>
   * @param path the FieldPath to apply this merge operation
   * @param doc the document to be merged
   * @return {@code this} for chained invocation
   */
  public DocumentMutation merge(@NonNullable String path, @NonNullable Document doc);

  /**
   * Merges the existing MAP at the given FieldPath with the specified Document.
   * <br/><br/>
   * @param path the FieldPath to apply this merge operation
   * @param doc the document to be merged
   * @return {@code this} for chained invocation
   */
  public DocumentMutation merge(@NonNullable FieldPath path, @NonNullable Document doc);

  /**
   * Merges the existing MAP at the given FieldPath with the specified Map.
   * <br/><br/>
   * @param path the FieldPath to apply this merge operation
   * @param map the Map to be merged
   * @return {@code this} for chained invocation
   */
  public DocumentMutation merge(@NonNullable String path, @NonNullable Map<String, Object> map);

  /**
   * Merges the existing MAP at the given FieldPath with the specified Map.
   * <br/><br/>
   * @param path the FieldPath to apply this merge operation
   * @param map the Map to be merged
   * @return {@code this} for chained invocation
   */
  public DocumentMutation merge(@NonNullable FieldPath path, @NonNullable Map<String, Object> map);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable FieldPath path, byte inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable String path, byte inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable FieldPath path, short inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable String path, short inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable String path, int inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable FieldPath path, int inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable FieldPath path, long inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable String path, long inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable String path, float inc);

  /**
   * Atomically increment the field specified by the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable FieldPath path, float inc);

  public DocumentMutation increment(@NonNullable String path, double inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable FieldPath path, double inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable String path, @NonNullable BigDecimal inc);

  /**
   * Atomically increment the existing value at given the FieldPath by the given value.
   *
   * @param path the FieldPath to apply this increment operation
   * @param inc increment to apply to a field - can be positive or negative
   */
  public DocumentMutation increment(@NonNullable FieldPath path, @NonNullable BigDecimal inc);

  /**
   * Atomically decrements the given field (in dot separated notation)
   * of the given row id.
   *
   * If the field path specified for the decrement operation doesn't
   * exist in the document in document store, then this operation will create a new element at the
   * given path. This new element will be of same type as the value specified
   * in the parameter.
   *
   * This operation will fail if the type of any intermediate path elements specified
   * in the append field path doesn't match the type of the corresponding field in the
   * document stored on the document store. For example, an operation on field "a.b.c" will fail
   * if, on the document store, document a itself is an array or integer.
   *
   * If the field doesn't exist in
   * the document store then it will be created with the type of given decremental value.
   * A decrement operation can be applied on any of the numeric types
   * of a field, such as byte, short, int, long, float, double, or decimal.
   * The operation will fail if the decrement is applied to a field
   * that is of a non-numeric type.
   *
   * The decrement operation won't change the type of the existing value stored in
   * the given field for the row. The resultant value of the field will be
   * truncated based on the original type of the field.
   *
   * For example, field 'score' is of type int and contains 60. The decrement
   * '5.675', a double, is applied. The resultant value of the field
   * will be 54 (54.325 will be truncated to 54).
   *
   * @param path field name in dot separated notation
   * @param dec decrement to apply to a field - can be positive or negative
   * @throws IOException
   */

  public DocumentMutation decrement(@NonNullable FieldPath path, byte dec);

  public DocumentMutation decrement(@NonNullable String path, byte dec);

  public DocumentMutation decrement(@NonNullable FieldPath path, short dec);

  public DocumentMutation decrement(@NonNullable String path, short dec);

  public DocumentMutation decrement(@NonNullable String path, int dec);

  public DocumentMutation decrement(@NonNullable FieldPath path, int dec);

  public DocumentMutation decrement(@NonNullable FieldPath path, long dec);

  public DocumentMutation decrement(@NonNullable String path, long dec);

  public DocumentMutation decrement(@NonNullable String path, float dec);

  public DocumentMutation decrement(@NonNullable FieldPath path, float dec);

  public DocumentMutation decrement(@NonNullable String path, double dec);

  public DocumentMutation decrement(@NonNullable FieldPath path, double dec);

  public DocumentMutation decrement(@NonNullable String path, @NonNullable BigDecimal dec);

  public DocumentMutation decrement(@NonNullable FieldPath path, @NonNullable BigDecimal dec);

  /**
   * Deletes the field at the given path.
   * <br/><br/>
   * If the field does not exist, the mutation operation will silently succeed.
   * For example, if a delete operation is attempted on {@code "a.b.c"}, and the
   * field {@code "a.b"} is an array, then {@code "a.b.c"} will not be deleted.
   *
   * @param path the FieldPath to delete
   */
  public DocumentMutation delete(@NonNullable String path);

  /**
   * Deletes the field at the given path.
   * <br/><br/>
   * If the field does not exist, the mutation operation will silently succeed.
   * For example, if a delete operation is attempted on {@code "a.b.c"}, and the
   * field {@code "a.b"} is an array, then {@code "a.b.c"} will not be deleted.
   *
   * @param path the FieldPath to delete
   */
  public DocumentMutation delete(@NonNullable FieldPath path);

}
