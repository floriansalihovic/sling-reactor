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
 * Simple service which allows to create users and provides a minimal set of methods to restrict the access of
 * resources.
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
     * Prints the current access control policies of a resource.
     *
     * @param resourceResolver
     *         The ResourceResolver used for resource resolution and adaption.
     * @param resource
     *         The resource to be modified.
     *
     * @return A string representation of the current access control policies of a resource.
     */
    String print(ResourceResolver resourceResolver,
                 Resource resource);

    /**
     * Modifies the resource so that it is accessible and modifiable for the given user or group.
     *
     * @param resourceResolver
     *         The ResourceResolver used for resource resolution and adaption. It has to provides administrator level
     *         rights.
     * @param resource
     *         The resource to be modified.
     * @param principalName
     *         The name of the principal who gains complete access to the resource.
     *
     * @return {@code true} if the change of access rights was successful, {@code false} else.
     */
    boolean resourceAccessibleAndModifiableFor(final ResourceResolver resourceResolver,
                                               final Resource resource,
                                               final String principalName);

    /**
     * Modifies the resource so that it is accessible and modifiable for the given user or group.
     *
     * @param resourceResolver
     *         The ResourceResolver used for resource resolution and adaption. It has to provides administrator level
     *         rights.
     * @param resource
     *         The resource to be modified.
     * @param principalName
     *         The name of the principal who gains read only access to the resource.
     *
     * @return {@code true} if the change of access rights was successful, {@code false} else.
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
     * @return {@code true} if the change of access rights was successful, {@code false} else.
     */
    boolean removePoliciesForResource(final ResourceResolver resourceResolver,
                                      final Resource resource);
}
