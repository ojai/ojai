/* Copyright (c) 2015 & onwards. MapR Tech, Inc., All rights reserved */
package org.ojai.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class API {
  /**
   * Annotate a class or method as a public API.
   * Intended for use by any project or application.
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Public {};

  /**
   * Annotate a class or method, even if marked as public
   * with Java keyword, as internal. Intended for use only
   * within library itself.
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Internal {};

}
