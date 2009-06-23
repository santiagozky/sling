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
package org.apache.sling.osgi.installer.impl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Executes a list of OsgiController tasks in their own thread.
 */
class OsgiControllerTaskExecutor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    static int counter;

	/** Execute the given tasks in a new thread, return when done */
    List<OsgiControllerTask >execute(final List<OsgiControllerTask> tasks, final OsgiControllerTask.Context context) {
    	final List<OsgiControllerTask> remainingTasks = new LinkedList<OsgiControllerTask>();
		final String threadName = getClass().getSimpleName() + " #" + (++counter);
		final String oldThreadName = Thread.currentThread().getName();
		Thread.currentThread().setName(threadName);
		try {
		    log.info("Starting controller task executor.");
			while(!tasks.isEmpty()) {
				final OsgiControllerTask t = tasks.remove(0);
				try {
                    log.info("Executing task: " + t);
					t.execute(context);
					log.info("Task execution successful: " + t);
				} catch(MissingServiceException mse) {
					log.info("Task execution deferred due to " + mse + ", task=" + t);
					remainingTasks.add(t);
				} catch(Throwable e) {
					log.warn("Task execution failed: " + t, e);
                    remainingTasks.add(t);
				}
			}
            log.info("Finishing controller task executor.");
		} finally {
		    Thread.currentThread().setName(oldThreadName);
		}

		return remainingTasks;
	}
}
