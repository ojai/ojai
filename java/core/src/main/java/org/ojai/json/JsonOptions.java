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
package org.ojai.json;

import org.ojai.exceptions.OjaiException;

/**
 * <p>This class encapsulates various options to configure a JSON serializer
 * for Documents.</p>
 * Currently, it supports the following options.
 * <ul>
 *  <li><b>Pretty Print</b>: off by default.</li>
 *  <li><b>With Tags</b>: on by default.</li>
 * </ul>
 */
public class JsonOptions implements Cloneable {

  public static final JsonOptions DEFAULT = new JsonOptions();

  public static final JsonOptions WITH_TAGS = new JsonOptions().withTags();

  private boolean pretty = false;
  private boolean withTags = false;

  public JsonOptions() { }

  public boolean isPretty() {
    return pretty;
  }

  public JsonOptions setPretty(boolean pretty) {
    checkMutationOfConstants();
    this.pretty = pretty;
    return this;
  }

  public JsonOptions pretty() {
    checkMutationOfConstants();
    this.pretty = true;
    return this;
  }

  public JsonOptions compact() {
    checkMutationOfConstants();
    this.pretty = false;
    return this;
  }

  public boolean isWithTags() {
    return withTags;
  }

  public JsonOptions setWithTags(boolean withTags) {
    checkMutationOfConstants();
    this.withTags = withTags;
    return this;
  }

  public JsonOptions withTags() {
    checkMutationOfConstants();
    this.withTags = true;
    return this;
  }

  public JsonOptions withoutTags() {
    checkMutationOfConstants();
    this.withTags = false;
    return this;
  }

  private void checkMutationOfConstants() {
    if (this == DEFAULT
        || this == WITH_TAGS) {
      throw new UnsupportedOperationException("Can not modify constants options.");
    }
  }

  @Override
  public String toString() {
    return "{\"pretty\":" + pretty + ", \"withTags\":" + withTags + "}";
  }

  @Override
  public JsonOptions clone()  {
    try {
      return (JsonOptions) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new OjaiException(e); // Unlikely to ever get here
    }
  }

}
