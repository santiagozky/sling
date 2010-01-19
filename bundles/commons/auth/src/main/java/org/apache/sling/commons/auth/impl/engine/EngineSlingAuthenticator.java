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
package org.apache.sling.commons.auth.impl.engine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.commons.auth.NoAuthenticationHandlerException;
import org.apache.sling.engine.auth.Authenticator;

/**
 * The <code>EngineSlingAuthenticator</code> class is a simple proxy service
 * providing the old Sling Engine {@link Authenticator} service calling into the
 * new standalone Apache Sling
 * {@link org.apache.sling.commons.auth.Authenticator} service.
 *
 * @scr.component metatype="no"
 * @scr.service interface="org.apache.sling.engine.auth.Authenticator"
 * @scr.property name="service.description"
 *               value="Apache Sling Request Authenticator (Legacy Bridge)"
 * @scr.property name="service.vendor" value="The Apache Software Foundation"
 */
@SuppressWarnings("deprecation")
public class EngineSlingAuthenticator implements Authenticator {

    /**
     * @scr.reference
     */
    private org.apache.sling.commons.auth.Authenticator slingAuthenticator;

    public void login(HttpServletRequest request, HttpServletResponse response) {
        try {
            slingAuthenticator.login(request, response);
        } catch (NoAuthenticationHandlerException nahe) {
            final org.apache.sling.engine.auth.NoAuthenticationHandlerException wrapped = new org.apache.sling.engine.auth.NoAuthenticationHandlerException();
            wrapped.initCause(nahe);
            throw wrapped;
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        slingAuthenticator.logout(request, response);
    }

}
