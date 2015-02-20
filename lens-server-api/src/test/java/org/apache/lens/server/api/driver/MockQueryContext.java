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
package org.apache.lens.server.api.driver;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.lens.api.LensConf;
import org.apache.lens.api.LensException;
import org.apache.lens.server.api.query.PreparedQueryContext;
import org.apache.lens.server.api.query.QueryContext;
import parquet.Preconditions;

import java.util.Map;

public class MockQueryContext extends QueryContext {

  public static class Builder {

    private PreparedQueryContext preparedQueryContext;
    private String query;
    private String user;
    private LensConf qconf;
    private Configuration conf;
    private Map<LensDriver, String> driverQueries;
    private String lensSessionId;
    private LensDriver selectedDriver;

    public Builder query(String query) {
      this.query = query;
      return this;
    }

    public Builder prepared(PreparedQueryContext preparedQueryContext) {
      this.preparedQueryContext = preparedQueryContext;
      return this;
    }

    public Builder user(String user) {
      this.user = user;
      return this;
    }

    public Builder lensConf(LensConf qconf) {
      this.qconf = qconf;
      return this;
    }

    public Builder conf(Configuration qconf) {
      this.conf = qconf;
      return this;
    }

    public Builder driverQueries(Map<LensDriver, String> driverQueries) {
      this.driverQueries = driverQueries;
      return this;
    }

    public Builder session(String sessionId) {
      this.lensSessionId = sessionId;
      return this;
    }

    public Builder selectedDriver(LensDriver selectedDriver) {
      this.selectedDriver = selectedDriver;
      return this;
    }

    public MockQueryContext build() throws LensException {
      Preconditions.checkArgument(StringUtils.isNotBlank(query),
                                  "Query should not be null");
      Preconditions.checkNotNull(conf, "Configuration should not be null");
      Preconditions.checkNotNull(driverQueries, "Driver Queries should not be null");
      MockQueryContext ctx = new MockQueryContext(query, user, qconf, conf, driverQueries);
      if (selectedDriver != null) {
        ctx.setSelectedDriver(selectedDriver);
      }
      if (lensSessionId != null) {
        ctx.setLensSessionIdentifier(lensSessionId);
      }
      return ctx;
    }

    public MockQueryContext buildPrepared() throws LensException {
      Preconditions.checkNotNull(query,
                                 "Query should not be null");
      Preconditions.checkNotNull(conf, "Configuration should not be null");
      MockQueryContext ctx = new MockQueryContext(preparedQueryContext, user, qconf, conf);
      if (selectedDriver != null) {
        ctx.setSelectedDriver(selectedDriver);
      }
      if (lensSessionId != null) {
        ctx.setLensSessionIdentifier(lensSessionId);
      }
      return ctx;
    }

  }

  protected MockQueryContext(final String query, final String user, final LensConf qconf,
                             final Configuration conf, final Map<LensDriver, String> driverQueries) throws
    LensException {
    super(query, user, qconf, conf, driverQueries.keySet());
    getDriverContext().setDriverConf(conf);
    setDriverQueries(driverQueries);
  }

  protected MockQueryContext(final PreparedQueryContext query, final String user, final LensConf qconf,
                             final Configuration conf) throws
    LensException {
    super(query, user, qconf, conf);
  }

  public Builder builder() {
    return new Builder();
  }
}