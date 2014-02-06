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

import io.github.floriansalihovic.sling.examples.usermanagement.SimpleAccessManagerService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.security.principal.*;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.base.util.AccessControlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the SimpleAccessManagerService. <p>It requires a ResourceResolver with the necessary privileges to
 * modify user and group nodes.</p>
 * <p/>
 * Some links: http://wiki.apache.org/jackrabbit/UserManagement http://sling.apache.org/site/managing-permissions-jackrabbitaccessmanager.html
 * http://sling.apache.org/documentation/bundles/managing-permissions-jackrabbit-accessmanager.html
 *
 * @author florian.salihovic@me.com
 * @version 1.0.0
 * @see io.github.floriansalihovic.sling.examples.usermanagement.SimpleAccessManagerService
 * @see org.apache.sling.api.resource.ResourceResolver
 */
@Service
@Component
public class SimpleAccessMangerServiceImpl implements SimpleAccessManagerService {

    private final static List<String> DENIABLE_PRIVILEGES = new ArrayList<String>();

    static {
        DENIABLE_PRIVILEGES.add(Privilege.JCR_READ);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_MODIFY_PROPERTIES);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_ADD_CHILD_NODES);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_REMOVE_NODE);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_REMOVE_CHILD_NODES);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_WRITE);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_READ_ACCESS_CONTROL);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_MODIFY_ACCESS_CONTROL);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_LOCK_MANAGEMENT);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_VERSION_MANAGEMENT);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_NODE_TYPE_MANAGEMENT);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_RETENTION_MANAGEMENT);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_LIFECYCLE_MANAGEMENT);
        DENIABLE_PRIVILEGES.add(Privilege.JCR_ALL);
    }

    /**
     * The logger.
     */
    final Logger logger = LoggerFactory.getLogger(SimpleAccessMangerServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(ResourceResolver resourceResolver,
                           String name) {

        try {
            final Session session = resourceResolver.adaptTo(Session.class);
            // the user manager used for user creation
            final UserManager userManager = AccessControlUtil.getUserManager(session);
            // if it was not set to true, the session had to be used to save the newly created content.
            userManager.autoSave(true);
            return userManager.createUser(name, name);
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Group createGroup(ResourceResolver resourceResolver,
                             String name) {

        try {
            final Session session = resourceResolver.adaptTo(Session.class);
            // the user manager used for user creation
            final UserManager userManager = AccessControlUtil.getUserManager(session);
            // if it was not set to true, the session had to be used to save the newly created content.
            userManager.autoSave(true);
            return userManager.createGroup(userManager.getAuthorizable("administrators").getPrincipal(), name);
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public StringBuilder print(ResourceResolver resourceResolver,
                               Resource resource) {

        final Session session = resourceResolver.adaptTo(Session.class);
        final String path = resource.getPath();
        final StringBuilder stringBuilder = new StringBuilder("policies:\n");
        final AccessControlManager accessControlManager;
        try {
            accessControlManager = AccessControlUtil.getAccessControlManager(session);
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            stringBuilder.setLength(0);
            return stringBuilder.append(e.getMessage());
        }

        try {
            final AccessControlPolicy[] policies = accessControlManager.getPolicies(path);

            for (AccessControlPolicy acp : policies) {
                printAccessControlLists(acp, stringBuilder);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        stringBuilder.append("effectivePolicies:\n");

        try {
            final AccessControlPolicy[] effectivePolicies = accessControlManager.getEffectivePolicies(path);

            for (AccessControlPolicy acp : effectivePolicies) {
                printAccessControlLists(acp, stringBuilder);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        stringBuilder.append("applicablePolicies:\n");

        try {
            final AccessControlPolicyIterator policies = accessControlManager.getApplicablePolicies(path);

            if (policies.getSize() > 0) {
                while (policies.hasNext()) {
                    printAccessControlLists(policies.nextAccessControlPolicy(), stringBuilder);
                }
            } else {
                stringBuilder.append(0);
                stringBuilder.append("\n");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return stringBuilder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean restrict(ResourceResolver resourceResolver,
                            Resource resource,
                            String name,
                            final boolean isAllow) {

        final Session session = resourceResolver.adaptTo(Session.class);
        final String path = resource.getPath();

        try {
            final UserManager userManager = AccessControlUtil.getUserManager(session);

            final Authorizable authorizable = userManager.getAuthorizable(name);
            final AccessControlManager accessControlManager = AccessControlUtil.getAccessControlManager(session);
            final AccessControlPolicyIterator iterator = accessControlManager.getApplicablePolicies(path);

            while (iterator.hasNext()) {
                AccessControlPolicy applicablePolicy = iterator.nextAccessControlPolicy();
                // the user's control list may grow a lot, so this specific use case the implementation will delete
                // all existing entries and add the allowed or denied properties to always understand in which state
                // we're in. The second aspect to consider is having acl inheritance. If path /a/b/c is restricted for
                // a user, path /a/b/c/d will inherit all entries.
                // it's also important to consider that "denys" should be preferred over "allow"

                // other policies may be in use as well.

                final Privilege[] denyPrivileges = getDeniedPrivilegesForReadOnlyUsers(accessControlManager);
                final Privilege[] allowPrivileges = getPrivilegesFromNames(accessControlManager,
                        Arrays.asList(Privilege.JCR_READ));
                if (applicablePolicy instanceof AccessControlList) {
                    final AccessControlList accessControlList = (AccessControlList) applicablePolicy;
                    final AccessControlEntry[] accessControlEntries = accessControlList.getAccessControlEntries();

                    for (AccessControlEntry entry : accessControlEntries) {
                        final String principalName = entry.getPrincipal().getName();
                        final Privilege[] privileges = entry.getPrivileges();

                        if (privileges.length > 0) {
                            for (Privilege privilege : privileges) {
                                logger.info("\n\tapplicable ace principal {}, privilege {}", principalName, privilege.getName());
                            }
                        } else {
                            logger.debug("\n\tno privileges ");
                        }
                    }
                    if (isAllow) {
                        accessControlList.addAccessControlEntry(authorizable.getPrincipal(), new Privilege[]{accessControlManager.privilegeFromName(Privilege.JCR_READ)});
                        accessControlManager.setPolicy(path, accessControlList);
                        session.save();
                    }

                    // badly documented
                    // for jcr based implementations see http://wiki.apache.org/jackrabbit/AccessControl
                    if (null != authorizable) {
                        final Principal principal = authorizable.getPrincipal();
                        if (isAllow) {
                            final List<Privilege> all = new ArrayList<Privilege>();
                            all.addAll(Arrays.asList(denyPrivileges));
                            all.addAll(Arrays.asList(allowPrivileges));
                            AccessControlUtil.addEntry(accessControlList, principal, all.toArray(new Privilege[all.size()]), true);
                        } else {
                            AccessControlUtil.addEntry(accessControlList, principal, denyPrivileges, false);
                            AccessControlUtil.addEntry(accessControlList, principal, allowPrivileges, true);
                        }
                        accessControlManager.setPolicy(path, accessControlList);
                        session.save();
                    }
                }
            }
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
        }

        return true;
    }

    /**
     * Returns an array of javax.jcr.security.Privilege which restrict access to a resource/node/path.
     *
     * @param accessControlManager
     *         The AccessControlManager providing a factory method for creating Privileges.
     *
     * @return The array of privileges being created.
     */
    private Privilege[] getDeniedPrivilegesForReadOnlyUsers(final AccessControlManager accessControlManager) {

        return getPrivilegesFromNames(accessControlManager, DENIABLE_PRIVILEGES);
    }

    /**
     * Convenience method for creating javax.jcr.security.Privilege instances by name.
     *
     * @param accessControlManager
     *         The AccessControlManager providing a factory method for creating Privileges.
     * @param names
     *         The names of the Privileges being created. Please ensure using the Privilege class's constant fields.
     *
     * @return the created Privileges.
     *
     * @see javax.jcr.security.AccessControlManager
     * @see javax.jcr.security.Privilege
     */
    private Privilege[] getPrivilegesFromNames(final AccessControlManager accessControlManager,
                                               final List<String> names) {
        final int size = names.size();
        final Privilege[] privileges = new Privilege[size];

        for (int i = 0; i < size; i++) {
            try {
                privileges[i] = accessControlManager.privilegeFromName(names.get(i));
            } catch (RepositoryException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return privileges;
    }

    @Override
    public boolean resourceIsReadOnlyFor(final ResourceResolver resourceResolver,
                                         final Resource resource,
                                         final String principalName) {
        final Session session = resourceResolver.adaptTo(Session.class);
        final String resourcePath = resource.getPath();

        try {
            final AccessControlManager accessControlManager = AccessControlUtil.getAccessControlManager(session);
            final PrincipalManager principalManager = AccessControlUtil.getPrincipalManager(session);
            final Principal principal = principalManager.getPrincipal(principalName);

            final AccessControlPolicy[] policies = accessControlManager.getPolicies(resourcePath);

            for (AccessControlPolicy policy : policies) {
                if (policy instanceof AccessControlList) {
                    final AccessControlList list = (AccessControlList) policy;
                    for (AccessControlEntry entry : list.getAccessControlEntries()) {
                        if (entry.getPrincipal().getName().equals(principalName)) {
                            list.removeAccessControlEntry(entry);
                        }
                    }

                    list.addAccessControlEntry(principal,
                            new Privilege[]{accessControlManager.privilegeFromName(Privilege.JCR_READ)});
                    accessControlManager.setPolicy(resourcePath, policy);
                }
            }
            session.save();
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    @Override
    public boolean removePoliciesFor(final ResourceResolver resourceResolver,
                                     final Resource resource) {

        final Session session = resourceResolver.adaptTo(Session.class);
        final String resourcePath = resource.getPath();

        try {
            final AccessControlManager accessControlManager = AccessControlUtil.getAccessControlManager(session);
            final AccessControlPolicy[] effectivePolicies = accessControlManager.getPolicies(resourcePath);
            for (AccessControlPolicy policy : effectivePolicies) {
                if (policy instanceof AccessControlList) {
                    AccessControlList list = (AccessControlList) policy;
                    for (AccessControlEntry entry : list.getAccessControlEntries()) {
                        list.removeAccessControlEntry(entry);
                    }
                    accessControlManager.setPolicy(resourcePath, policy);
                }
            }
            session.save();
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    @Override
    public boolean resourceAccessibleAndModifiableFor(final ResourceResolver resourceResolver,
                                                      final Resource resource,
                                                      final String principalName) {

        final Session session = resourceResolver.adaptTo(Session.class);
        final String resourcePath = resource.getPath();

        try {
            final AccessControlManager accessControlManager = AccessControlUtil.getAccessControlManager(session);
            final AccessControlPolicy[] effectivePolicies = accessControlManager.getPolicies(resourcePath);
            for (AccessControlPolicy policy : effectivePolicies) {
                if (policy instanceof AccessControlList) {
                    AccessControlList list = (AccessControlList) policy;
                    for (AccessControlEntry entry : list.getAccessControlEntries()) {
                        if (entry.getPrincipal().getName().equals(principalName)) {
                            list.removeAccessControlEntry(entry);
                        }
                    }
                    accessControlManager.setPolicy(resourcePath, policy);
                }
            }
            session.save();
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    private StringBuilder printAccessControlLists(final AccessControlPolicy acp,
                                                  final StringBuilder stringBuilder)
            throws RepositoryException {

        if (acp instanceof AccessControlList) {
            AccessControlList acl = (AccessControlList) acp;
            final AccessControlEntry[] accessControlEntries = acl.getAccessControlEntries();
            stringBuilder.append("\taccessControlEntries:").append(accessControlEntries.length).append("\n");
            if (accessControlEntries.length > 0) {
                for (AccessControlEntry ace : accessControlEntries) {
                    for (Privilege privilege : ace.getPrivileges()) {
                        stringBuilder.append("\t\teffective ace for principal:").append(ace.getPrincipal().getName())
                                .append(", privilege ").append(privilege.getName()).append(", isAllow ")
                                .append(AccessControlUtil.isAllow(ace)).append("\n");
                    }
                }
            } else {
                logger.debug("\n\tno aces.");
            }
        }
        return stringBuilder;
    }
}
