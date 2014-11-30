/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.lens.server.api.query;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.lens.api.LensConf;
import org.apache.lens.api.LensException;
import org.apache.lens.server.api.driver.DriverQueryPlan;
import org.apache.lens.server.api.driver.LensDriver;

import java.io.Serializable;
import java.util.Collection;

public abstract class AbstractQueryContext implements Serializable {
  /** The Constant LOG */
  public static final Log LOG = LogFactory.getLog(AbstractQueryContext.class);

  /** The user query. */
  @Getter
  protected String userQuery;

  /** The merged Query conf. */
  @Getter @Setter
  transient protected Configuration conf;

  /** The query conf. */
  @Getter
  protected LensConf qconf;

  /** The driver ctx */
  @Getter
  @Setter
  protected DriverSelectorQueryContext driverContext;

  protected AbstractQueryContext(final String query, final LensConf qconf, final Configuration conf) {
    userQuery = query;
    this.qconf = qconf;
    this.conf = conf;
  }

  protected AbstractQueryContext(final String query, final LensConf qconf, final Configuration conf, final
    Collection<LensDriver> drivers) {
    driverContext = new DriverSelectorQueryContext(query, conf, drivers);
    userQuery = query;
    this.qconf = qconf;
    this.conf = conf;
  }

  /** Wrapper method for convenience on driver context
   *
   * @return the selected driver's query
   */
  public String getSelectedDriverQuery() {
    if(driverContext != null) {
      return driverContext.getSelectedDriverQuery();
    }
    return null;
  }

  /** Wrapper method for convenience on driver context
   *
   * @return the selected driver
   */
  public LensDriver getSelectedDriver() {
    if(driverContext != null) {
      return driverContext.getSelectedDriver();
    }
    return null;
  }

  /** Wrapper method for convenience on driver context
   *
   * @return the selected driver
   */
  public DriverQueryPlan getSelectedDriverQueryPlan() throws LensException {
    if(driverContext != null) {
      return driverContext.getSelectedDriverQueryPlan();
    }
    return null;
  }
}
