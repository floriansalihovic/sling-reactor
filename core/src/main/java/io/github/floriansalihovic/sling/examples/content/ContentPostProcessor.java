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
package io.github.floriansalihovic.sling.examples.content;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.SlingPostProcessor;

import javax.jcr.Node;
import java.util.List;

/**
 * Example implementation of a custom Sling post processor. It is only invoked, when the SlingPostServlet is invoked to
 * create ot manipulate content.
 * <p/>
 * The current example is not bound to a resource type so it won"t be updated.
 *
 * @author florian.salihovic@me.com
 * @version 1.0.0
 */
@Component
@Service
public class ContentPostProcessor implements SlingPostProcessor {

    @Override
    public void process(SlingHttpServletRequest request,
                        List<Modification> changes) throws Exception {
        final Resource resource = request.getResource();
        final Node node = resource.adaptTo(Node.class); // todo adaptation to PersistableValueMap has to be considered.
        if (null != node && node.getPath().startsWith("/content")) {
            node.getProperty("jcr:mixinTypes").setValue("rep:AccessControllable");
        }
    }
}
