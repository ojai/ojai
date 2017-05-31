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

import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

import org.ojai.Document;
import org.ojai.annotation.API;
import org.ojai.annotation.API.NonNullable;
import org.ojai.exceptions.OjaiException;
import org.ojai.store.exceptions.StoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * A factory class to manage OJAI Drivers.
 * This is the entry point for any OJAI implementation.
 */
@API.Public
@API.Factory
public final class DriverManager {

  public static final String OJAI_PROTOCOL_NAME = "ojai:";

  /**
   * Discover and load the OJAI Driver implementation which supported the
   * specified URL.
   *
   * @param url a URL of the form "ojai:<driver_name>"
   *
   * @throws NullPointerException if the specified URL is {@code null}
   * @throws IllegalArgumentException if the specified URL does does not begin with "ojai:"
   * @throws OjaiException if no registered driver for found for the specified URL.
   */
  public static Driver getDriver(@NonNullable String url) throws OjaiException {
    Preconditions.checkNotNull(url);
    Preconditions.checkArgument(url.startsWith(OJAI_PROTOCOL_NAME));
    for (Driver driver : ojaiDrivers) {
      if (driver.accepts(url)) {
        return driver;
      }
    }
    throw new OjaiException(String.format("No registered driver found for url: '%s'", url));
  }

  /**
   * Establishes and returns a Connection to an OJAI data source.
   * The returned Connection is thread-safe.
   *
   * @param url a URL of the form "ojai:<driver_name>:[<connection_properties>]"
   *
   * @throws NullPointerException if the specified URL is {@code null}
   * @throws IllegalArgumentException if the specified URL does does not begin with "ojai:"
   * @throws OjaiException if no registered driver for found for the specified URL.
   * @throws StoreException if connection to the data-source failed.
   */
  public static Connection getConnection(@NonNullable String url) throws OjaiException {
    return getConnection(url, null);
  }

  /**
   * Establishes and returns a Connection to an OJAI data source.
   * The returned Connection is thread-safe.
   *
   * @param url a URL of the form "ojai:<driver_name>:[<connection_properties>]"
   * @param options an OJAI Document of arbitrary, implementation specific settings
   *
   * @throws NullPointerException if the specified URL is {@code null}
   * @throws IllegalArgumentException if the specified URL does does not begin with "ojai:"
   * @throws OjaiException if no registered driver for found for the specified URL.
   * @throws StoreException if connection to the data-source failed.
   */
  public static Connection getConnection(@NonNullable String url, @NonNullable Document options) throws OjaiException {
    Preconditions.checkNotNull(url);
    Preconditions.checkArgument(url.startsWith(OJAI_PROTOCOL_NAME));
    for (Driver driver : ojaiDrivers) {
      if (driver.accepts(url)) {
        logger.debug("URL '{}' was accepted by driver '{}'.", url, driver.getName());
        return driver.connect(url, options);
      }
    }
    throw new OjaiException(String.format("No registered driver found for url: '%s'", url));
  }

  /**
   * Register the specified Driver with this DriverManager.
   * <p/>
   * All OJAI Driver should register themselves with DriverManager during their
   * class initialization using this method.
   */
  @API.Internal
  public static synchronized void registerDriver(@NonNullable Driver driver) {
    Preconditions.checkNotNull(driver);
    ojaiDrivers.addIfAbsent(driver);
  }

  private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
  private static final CopyOnWriteArrayList<Driver> ojaiDrivers = new CopyOnWriteArrayList<Driver>();

  static {
    loadOjaiDrivers();
  }

  private static void loadOjaiDrivers() {
    ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
    for (Driver driver : loadedDrivers) {
      logger.debug("Loaded driver '{}'.", driver.getClass().getName());
    }
  }

}
