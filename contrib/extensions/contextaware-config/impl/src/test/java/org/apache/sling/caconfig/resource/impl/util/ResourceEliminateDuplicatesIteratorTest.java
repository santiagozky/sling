/*
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
package org.apache.sling.caconfig.resource.impl.util;

import static org.apache.sling.caconfig.resource.impl.util.ContextResourceTestUtil.toContextResourceIterator;
import static org.apache.sling.caconfig.resource.impl.util.ContextResourceTestUtil.toResourceIterator;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.hamcrest.ResourceIteratorMatchers;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class ResourceEliminateDuplicatesIteratorTest {
    
    @Rule
    public SlingContext context = new SlingContext();

    @SuppressWarnings("unchecked")
    @Test
    public void testIterator() {
        context.build()
            .resource("/conf/a")
            .resource("/conf/a/b")
            .resource("/conf/a/b/c");
        
        List<Resource> list = ImmutableList.of(
                context.resourceResolver().getResource("/conf/a"), 
                context.resourceResolver().getResource("/conf/a/b"),
                context.resourceResolver().getResource("/conf/a"),
                context.resourceResolver().getResource("/conf/a/b/c"));
        
        Iterator<Resource> result = toResourceIterator(new ResourceEliminateDuplicatesIterator(toContextResourceIterator(list.iterator())));
        assertThat(result, ResourceIteratorMatchers.paths(
                "/conf/a",
                "/conf/a/b",
                "/conf/a/b/c"));
    }

}
