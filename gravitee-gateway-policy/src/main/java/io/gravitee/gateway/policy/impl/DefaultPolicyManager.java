/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.gateway.policy.impl;

import io.gravitee.common.component.AbstractLifecycleComponent;
import io.gravitee.definition.model.Policy;
import io.gravitee.gateway.policy.PolicyManager;
import io.gravitee.gateway.policy.PolicyMetadata;
import io.gravitee.gateway.reactor.Reactable;
import io.gravitee.gateway.reactor.handler.ReactorHandler;
import io.gravitee.gateway.resource.ResourceLifecycleManager;
import io.gravitee.plugin.core.api.PluginClassLoader;
import io.gravitee.plugin.policy.PolicyClassLoaderFactory;
import io.gravitee.plugin.policy.PolicyPlugin;
import io.gravitee.plugin.policy.PolicyPluginManager;
import io.gravitee.plugin.policy.internal.PolicyMethodResolver;
import io.gravitee.policy.api.PolicyConfiguration;
import io.gravitee.policy.api.PolicyContext;
import io.gravitee.resource.api.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class DefaultPolicyManager extends AbstractLifecycleComponent<PolicyManager>
        implements PolicyManager {

    private final Logger LOGGER = LoggerFactory.getLogger(DefaultPolicyManager.class);

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, RegisteredPolicy> policies = new HashMap<>();

    @Override
    protected void doStart() throws Exception {
        // Init required policies
        initialize();

        // Activate policy context
        policies.values()
                .stream()
                .filter(registeredPolicy -> registeredPolicy.metadata.context() != null)
                .forEach(registeredPolicy -> {
                    try {
                        LOGGER.info("Activating context for {} [{}]", registeredPolicy.metadata.id(),
                                registeredPolicy.metadata.context().getClass().getName());

                        registeredPolicy.metadata.context().onActivation();
                    } catch (Exception ex) {
                        LOGGER.error("Unable to activate policy context", ex);
                    }
                });
    }

    @Override
    protected void doStop() throws Exception {
        // Deactivate policy context
        policies.values()
                .stream()
                .filter(registeredPolicy -> registeredPolicy.metadata.context() != null)
                .forEach(registeredPolicy -> {
                    try {
                        LOGGER.info("De-activating context for {} [{}]", registeredPolicy.metadata.id(),
                                registeredPolicy.metadata.context().getClass().getName());
                        registeredPolicy.metadata.context().onDeactivation();
                    } catch (Exception ex) {
                        LOGGER.error("Unable to deactivate policy context", ex);
                    }
                });

        // Close policy classloaders
        policies.values().forEach(policy -> {
            ClassLoader policyClassLoader = policy.classLoader;
            if (policyClassLoader instanceof PluginClassLoader) {
                try {
                    ((PluginClassLoader)policyClassLoader).close();
                } catch (IOException e) {
                    LOGGER.error("Unable to close policy classloader for policy {}", policy.metadata.id());
                }
            }
        });

        // Be sure to remove all references to policies
        policies.clear();
    }

    private void initialize() {
        PolicyPluginManager ppm = applicationContext.getBean(PolicyPluginManager.class);
        PolicyClassLoaderFactory pclf = applicationContext.getBean(PolicyClassLoaderFactory.class);
        ReactorHandler rh = applicationContext.getBean(ReactorHandler.class);
        ResourceLifecycleManager rm = applicationContext.getBean(ResourceLifecycleManager.class);
        Reactable reactable = rh.reactable();

        Set<Policy> requiredPlugins = reactable.dependencies(Policy.class);

        requiredPlugins.forEach(policy -> {
            final PolicyPlugin policyPlugin = ppm.get(policy.getName());
            if (policyPlugin == null) {
                LOGGER.error("Policy [{}] can not be found in registry", policy.getName());
                throw new IllegalStateException("Policy ["+policy.getName()+"] can not be found in registry");
            }

            PluginClassLoader policyClassLoader = null;

            // Load dependant resources to enhance policy classloader
            Collection<? extends Resource> resources = rm.getResources();
            if (! resources.isEmpty()) {
                ClassLoader[] resourceClassLoaders = rm.getResources().stream().map(new Function<Resource, ClassLoader>() {
                    @Override
                    public ClassLoader apply(Resource resource) {
                        return resource.getClass().getClassLoader();
                    }
                }).toArray(ClassLoader[]::new);

                DelegatingClassLoader parentClassLoader = new DelegatingClassLoader(rh.classloader(), resourceClassLoaders);
                policyClassLoader = pclf.getOrCreateClassLoader(policyPlugin, parentClassLoader);
            } else {
                policyClassLoader = pclf.getOrCreateClassLoader(policyPlugin, rh.classloader());
            }

            LOGGER.debug("Loading policy {} for {}", policy.getName(), rh);

            DefaultPolicyMetadata policyMetadata = new DefaultPolicyMetadata(policyPlugin.id());

            try {
                // Prepare metadata
                policyMetadata.setPolicy(ClassUtils.forName(policyPlugin.policy().getName(), policyClassLoader));
                policyMetadata.setMethods(new PolicyMethodResolver().resolve(policyMetadata.policy()));

                if (policyPlugin.configuration() != null) {
                    policyMetadata.setConfiguration((Class<? extends PolicyConfiguration>) ClassUtils.forName(policyPlugin.configuration().getName(), policyClassLoader));
                }

                // Prepare context if defined
                if (policyPlugin.context() != null) {
                    Class<? extends PolicyContext> policyContextClass = (Class<? extends PolicyContext>)ClassUtils.forName(policyPlugin.context().getName(), policyClassLoader);
                    // Create policy context instance and initialize context provider (if used)
                    PolicyContext context = new PolicyContextFactory(reactable).create(policyContextClass);
                    policyMetadata.setContext(context);
                }

                RegisteredPolicy registeredPolicy = new RegisteredPolicy();
                registeredPolicy.classLoader = policyClassLoader;
                registeredPolicy.metadata = policyMetadata;

                policies.put(policy.getName(), registeredPolicy);
            } catch (Exception ex) {
                LOGGER.error("Unable to load policy metadata", ex);

                if (policyClassLoader != null) {
                    try {
                        policyClassLoader.close();
                    } catch (IOException ioe) {
                        LOGGER.error("Unable to close classloader for policy", ioe);
                    }
                }
            }
        });
    }

    @Override
    public PolicyMetadata get(String policy) {
        RegisteredPolicy registeredPolicy = policies.get(policy);
        return (registeredPolicy != null) ? registeredPolicy.metadata : null;
    }

    private static class RegisteredPolicy {
        PolicyMetadata metadata;
        ClassLoader classLoader;
    }
}
