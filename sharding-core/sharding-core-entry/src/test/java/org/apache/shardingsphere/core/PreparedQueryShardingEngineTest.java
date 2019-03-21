/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core;

import lombok.SneakyThrows;
import org.apache.shardingsphere.core.constant.DatabaseType;
import org.apache.shardingsphere.core.metadata.ShardingMetaData;
import org.apache.shardingsphere.core.parse.cache.ParsingResultCache;
import org.apache.shardingsphere.core.route.PreparedStatementRoutingEngine;
import org.apache.shardingsphere.core.rule.ShardingRule;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class PreparedQueryShardingEngineTest extends BaseShardingEngineTest {
    
    @Mock
    private PreparedStatementRoutingEngine routingEngine;
    
    private PreparedQueryShardingEngine shardingEngine;
    
    public PreparedQueryShardingEngineTest() {
        super("SELECT ?", Collections.<Object>singletonList(1));
    }
    
    @Before
    public void setUp() {
        shardingEngine = new PreparedQueryShardingEngine(getSql(), mock(ShardingRule.class), getShardingProperties(), mock(ShardingMetaData.class), DatabaseType.MySQL, new ParsingResultCache());
        setRoutingEngine();
    }
    
    @SneakyThrows
    private void setRoutingEngine() {
        Field field = PreparedQueryShardingEngine.class.getDeclaredField("routingEngine");
        field.setAccessible(true);
        field.set(shardingEngine, routingEngine);
    }
    
    protected void assertShard() {
        when(routingEngine.route(getParameters())).thenReturn(createSQLRouteResult());
        assertSQLRouteResult(shardingEngine.shard(getSql(), getParameters()));
    }
}