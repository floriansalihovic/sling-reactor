/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. * See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. * The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. * See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.floriansalihovic.sling.examples.usermanagement.impl;

import io.github.floriansalihovic.sling.examples.usermanagement.*;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.*;
import org.apache.sling.api.servlets.*;

/**
 * Abstract servlet which is the base class for the servlets providing entry points for acl manipulation.
 *
 * @author Florian Salihovic<florian.salihovic@me.com>
 * @version 1.0.0
 */
@Component(componentAbstract = true)
abstract class AbstractAclServlet extends SlingSafeMethodsServlet {

    /**
     * The reference to the {@link io.github.floriansalihovic.sling.examples.usermanagement.SimpleAccessManagerService}.
     */
    @Reference
    protected SimpleAccessManagerService userManagerService;

    /**
     * Reads the principal name from the request's suffix.
     *
     * @param request
     *         The request to get the suffix from.
     *
     * @return A principal name.
     */
    protected String getPrincipalName(final SlingHttpServletRequest request) {
        final String suffix = request.getRequestPathInfo().getSuffix();

        if (null == suffix || suffix.length() == 0) {
            throw new IllegalArgumentException("Suffix must not be null or empty and a valid principal name.");
        }

        if (suffix.startsWith("/")) {
            return suffix.substring(1);
        }

        return suffix;
    }
}
