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

/**
 * This class encapsulates various options to configure Json serializer
 * for Documents.<br/><br/>
 * Currently, it supports the following options.
 * <ul>
 *  <li><b>Pretty Print</b>: off by default.</li>
 *  <li><b>With Tags</b>: on by default.</li>
 * </ul>
 */
public class JsonOptions {

  public static final JsonOptions DEFAULT = new JsonOptions();

  private boolean pretty = false;
  private boolean withTags = true;

  public JsonOptions() { }

  public boolean isPretty() {
    return pretty;
  }

  public JsonOptions setPretty(boolean pretty) {
    checkMutationOfDefault();
    this.pretty = pretty;
    return this;
  }

  public JsonOptions pretty() {
    checkMutationOfDefault();
    this.pretty = true;
    return this;
  }

  public JsonOptions compact() {
    checkMutationOfDefault();
    this.pretty = false;
    return this;
  }

  public boolean isWithTags() {
    return withTags;
  }

  public JsonOptions setWithTags(boolean withTags) {
    checkMutationOfDefault();
    this.withTags = withTags;
    return this;
  }

  public JsonOptions withTags() {
    checkMutationOfDefault();
    this.withTags = true;
    return this;
  }

  public JsonOptions withoutTags() {
    checkMutationOfDefault();
    this.withTags = false;
    return this;
  }

  private void checkMutationOfDefault() {
    if (this == DEFAULT) {
      throw new UnsupportedOperationException("Can not modify default options.");
    }
  }

  @Override
  public String toString() {
    return "{\"pretty\":" + pretty + ", \"withTags\":" + withTags + "}";
  }

}
