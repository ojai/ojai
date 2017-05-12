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
package org.ojai.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class API {
  /**
   * Annotate a class or method as a public API.
   * Intended for use by any project or application.
   *
   * @see Internal
   */
  @Documented
  @Retention(RetentionPolicy.CLASS)
  public @interface Public {};

  /**
   * Annotate a class or method, even if marked as public
   * with Java keyword, as internal. Intended for use only
   * within library itself.
   *
   * @see Public
   */
  @Documented
  @Retention(RetentionPolicy.CLASS)
  public @interface Internal {};

  /**
   * Annotate a class field, method parameter or return value as nullable, i.e. {@code null}
   * is an acceptable value for that parameter.
   *
   * @see NonNullable
   */
  @Documented
  @Retention(RetentionPolicy.CLASS)
  @Target(value = { ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
  public @interface Nullable {};

  /**
   * Annotate a class field, method parameter or return value as non-nullable, i.e. {@code null}
   * is NOT an acceptable value for that parameter.
   * <p/>
   * A {@link NullPointerException} will be thrown if {@code null} is passed for such a parameter.
   *
   * @see Nullable
   */
  @Documented
  @Retention(RetentionPolicy.CLASS)
  @Target(value = { ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
  public @interface NonNullable {};

  /**
   * Annotate a type to be thread-safe.
   *
   * @see NotThreadSafe
   */
  @Documented
  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.TYPE)
  public @interface ThreadSafe {};

  /**
   * Annotate a type to be not thread-safe.
   *
   * @see ThreadSafe
   */
  @Documented
  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.TYPE)
  public @interface NotThreadSafe {};

  /**
   * Annotate a type to be mutable after construction.
   *
   * @see Immutable
   * @see ImmutableOnBuild
   */
  @Documented
  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.TYPE)
  public @interface Mutable {};

  /**
   * Annotate a type to be immutable after construction.
   *
   * @see Mutable
   * @see ImmutableOnBuild
   */
  @Documented
  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.TYPE)
  public @interface Immutable {};
  
  /**
   * Annotate a type to be immutable after its {@code build()} method is called.
   *
   * @see Mutable
   * @see Immutable
   */
  @Documented
  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.TYPE)
  public @interface ImmutableOnBuild {};

}
