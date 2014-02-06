package io.github.floriansalihovic.sling.examples.usermanagement.impl;

import org.apache.felix.scr.annotations.sling.*;
import org.apache.sling.api.*;
import org.apache.sling.api.resource.*;

import javax.servlet.*;
import java.io.*;

/**
 * Created by florian on 05/02/14.
 */
@SlingServlet(methods = {"GET"}, resourceTypes = {"sling:Folder"}, selectors = {"remove"})
public class RemoveAclsServlet extends AbstractAclServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws ServletException, IOException {
        final Resource resource = request.getResource();
        final ResourceResolver resourceResolver = request.getResourceResolver();

        userManagerService.removePoliciesFor(resourceResolver, resource);

        // print the current access control policies for the resource
        response.getWriter().print(userManagerService.print(resourceResolver, resource));
    }
}
