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
/*
 * 
 */
package org.apache.lens.server.query;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.lens.api.LensException;
import org.apache.lens.server.api.LensConfConstants;
import org.apache.lens.server.api.query.QueryRewriter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;

/**
 * Test Phase1 query rewrites from UserDSL to CubeQL
 */
public class TestPhase1Rewrite {

  @Test
  public void testUserQueryRewrite() throws LensException {
    HiveConf conf = new HiveConf();
    conf.set(LensConfConstants.QUERY_PHASE1_REWRITERS, "test1,test2");
    conf.set(LensConfConstants.getRewriterImplConfKey("test1"), DummyPhase1QueryRewriter1.class.getCanonicalName());
    conf.set(LensConfConstants.getRewriterImplConfKey("test2"), DummyPhase1QueryRewriter1.class.getCanonicalName());
    final Collection<QueryRewriter> queryRewriters = RewriteUtil.getQueryRewriter(conf);
    final String query = "cube select name from table";

    Assert.assertTrue(queryRewriters.iterator().hasNext());
    Assert.assertEquals(queryRewriters.iterator().next().rewrite(query, conf), "rewriter1: " + query);

    Assert.assertTrue(queryRewriters.iterator().hasNext());
    Assert.assertEquals(queryRewriters.iterator().next().rewrite(query, conf), "rewriter2: " +  query);

    Assert.assertEquals(conf.get("phase1.rewriter.conf1"), "value");
    Assert.assertEquals(conf.get("phase1.rewriter.conf2"), "value");
  }
}
