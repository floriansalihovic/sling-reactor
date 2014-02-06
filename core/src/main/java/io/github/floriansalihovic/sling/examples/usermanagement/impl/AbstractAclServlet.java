package io.github.floriansalihovic.sling.examples.usermanagement.impl;

import io.github.floriansalihovic.sling.examples.usermanagement.*;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.*;
import org.apache.sling.api.servlets.*;

/**
 * Created by florian on 05/02/14.
 */
@Component(componentAbstract = true)
abstract class AbstractAclServlet extends SlingSafeMethodsServlet {

    /**
     * The reference to the {@link io.github.floriansalihovic.sling.examples.usermanagement.SimpleAccessManagerService}
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
            throw new IllegalArgumentException("Suffix must not be null or empty.");
        }

        if (suffix.startsWith("/")) {
            return suffix.substring(1);
        }

        return suffix;
    }
}
