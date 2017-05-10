/**
 * Copyright (c) 2016 MapR, Inc.
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

import java.util.Properties;

import org.ojai.exceptions.OjaiException;

/**
 * A factory class to manage OJAI Drivers.
 * This is the entry point for any OJAI implementation. 
 */
public final class DriverManager {
  /**
   * Discover and load the OJAI Driver implementation which supported the
   * specified URL.
   *
   * @param url a URL of the form "ojai:<driver_name>"
   */
  public static Driver getDriver(String url) throws OjaiException {
    //TODO: implement
    return null;
  }

  /**
   * Establishes and returns a Connection to an OJAI data source.
   * The returned Connection is thread-safe.
   *
   * @param url a URL of the form "ojai:<driver_name>:[<connection_properties>]"
   */
  public static Connection getConnection(String url) throws OjaiException {
    //TODO: implement
    return null;
  }

  /**
   * Establishes and returns a Connection to an OJAI data source.
   * The returned Connection is thread-safe.
   *
   * @param url a URL of the form "ojai:<driver_name>:[<connection_properties>]"
   * @param options a list of arbitrary, implementation specific string
   *        key-value pairs
   */
  public static Connection getConnection(String url, Properties options) throws OjaiException {
    //TODO: implement
    return null;
  }

}
