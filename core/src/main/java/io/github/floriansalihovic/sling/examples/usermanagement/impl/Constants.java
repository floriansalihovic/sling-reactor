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

/**
 * Basic class storing constant values.
 */
public class Constants {

    /**
     * The value of the GET method name when declared in sling servlets.
     */
    public final static String GET_METHOD = "GET";

    /**
     * The value of a sling folder resource type.
     */
    public static final String SLING_FOLDER_RESOURCE_TYPE = "sling:Folder";

    /**
     * The value of the all selector.
     *
     * @see io.github.floriansalihovic.sling.examples.usermanagement.impl.CompleteAccessServlet
     */
    public static final String ALL_SELECTOR = "all";

    /**
     * The value of the readonly selector.
     *
     * @see io.github.floriansalihovic.sling.examples.usermanagement.impl.ReadOnlyServlet
     */
    public static final String READ_ONLY = "readonly";

    /**
     * The value of the remove selector.
     *
     * @see io.github.floriansalihovic.sling.examples.usermanagement.impl.RemoveAclsServlet
     */
    public static final String REMOVE = "remove";
}
