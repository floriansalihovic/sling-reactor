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
package io.github.floriansalihovic.sling.examples.usermanagement;

import org.apache.jackrabbit.api.security.user.*;
import org.apache.sling.api.resource.*;

/**
 * Simple service which allows to create users and provides a minimal set of methods to readAccessOnly the user's
 * privileges.
 *
 * @author florian.salihovic@me.com
 * @version 1.0.0
 */
public interface SimpleAccessManagerService {

    /**
     * Creates a new user.
     *
     * @param resourceResolver
     *         The ResourceResolver used for resource resolution and adaption.
     * @param name
     *         The name of the new user.
     *
     * @return The user, if it could be created, {@code null} otherwise.
     */
    User createUser(final ResourceResolver resourceResolver,
                    final String name);

    /**
     * Creates a new group.
     *
     * @param resourceResolver
     *         The ResourceResolver used for resource resolution and adaption.
     * @param name
     *         The name of the new user.
     *
     * @return The group, if it could be created, {@code null} otherwise.
     */
    Group createGroup(final ResourceResolver resourceResolver,
                      final String name);

    /**
     * Restricts the user to read access only.
     *
     * @param resourceResolver
     *         The ResourceResolver used for resource resolution and adaption.
     * @param resource
     *         The resource to modify the access methods for.
     * @param name
     *         The name of the user.
     * @param allow
     *         If {@code allow} is set to {@code true}, the user may have complete access to a resource, otherwise the
     *         user is restricted to read access.
     *
     * @return {@code true} if the access restriction was changed, {@code false} else.
     */
    boolean restrict(final ResourceResolver resourceResolver,
                     Resource resource,
                     final String name,
                     final boolean allow);

    StringBuilder print(ResourceResolver resourceResolver,
                        Resource resource);

    /**
     * @param resourceResolver
     * @param resource
     * @param principalName
     *
     * @return
     */
    boolean resourceAccessibleAndModifiableFor(final ResourceResolver resourceResolver,
                                               final Resource resource,
                                               final String principalName);

    /**
     * @param resourceResolver
     * @param resource
     * @param principalName
     *
     * @return
     */
    boolean resourceIsReadOnlyFor(final ResourceResolver resourceResolver,
                                  final Resource resource,
                                  final String principalName);

    /**
     * Removes all access control policy entries for the given resource.
     *
     * @param resourceResolver
     *         The current user's resource resolver.
     * @param resource
     *         The resource to be modified.
     *
     * @return
     */
    boolean removePoliciesFor(final ResourceResolver resourceResolver,
                              final Resource resource);
}
