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

import org.apache.felix.scr.annotations.sling.*;
import org.apache.sling.api.*;
import org.apache.sling.api.resource.*;

import javax.servlet.*;
import java.io.*;

/**
 * Servlet which provides access to the service which removes all access control policies for a resource.
 *
 * @see io.github.floriansalihovic.sling.examples.usermanagement.SimpleAccessManagerService
 */
@SlingServlet(methods = {Constants.GET_METHOD},
        resourceTypes = {Constants.SLING_FOLDER_RESOURCE_TYPE},
        selectors = {Constants.REMOVE})
public class RemoveAclsServlet extends AbstractAclServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws ServletException, IOException {
        final Resource resource = request.getResource();
        final ResourceResolver resourceResolver = request.getResourceResolver();

        userManagerService.removePoliciesForResource(resourceResolver, resource);
        response.getWriter().print(userManagerService.print(resourceResolver, resource));
    }
}
