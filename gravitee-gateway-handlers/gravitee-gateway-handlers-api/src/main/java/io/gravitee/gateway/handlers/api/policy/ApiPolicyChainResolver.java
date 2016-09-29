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
package io.gravitee.gateway.handlers.api.policy;

import io.gravitee.definition.model.Path;
import io.gravitee.definition.model.Rule;
import io.gravitee.gateway.api.ExecutionContext;
import io.gravitee.gateway.api.Request;
import io.gravitee.gateway.api.Response;
import io.gravitee.gateway.handlers.api.PathResolver;
import io.gravitee.gateway.policy.*;
import io.gravitee.gateway.policy.impl.PolicyImpl;
import io.gravitee.policy.api.PolicyConfiguration;
import io.gravitee.policy.api.annotations.OnRequest;
import io.gravitee.policy.api.annotations.OnRequestContent;
import io.gravitee.policy.api.annotations.OnResponse;
import io.gravitee.policy.api.annotations.OnResponseContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A policy chain resolver based on the policy configuration from the API.
 * This policy configuration is done by path / method.
 *
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class ApiPolicyChainResolver extends AbstractPolicyChainResolver {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiPolicyChainResolver.class);

    @Autowired
    private PathResolver pathResolver;

    @Autowired
    private PolicyManager policyManager;

    @Autowired
    private PolicyFactory policyFactory;

    @Autowired
    private PolicyConfigurationFactory policyConfigurationFactory;

    @Override
    protected List<Policy> calculate(StreamType streamType, Request request, Response response, ExecutionContext executionContext) {
        // Resolve the "configured" path according to the inbound request
        Path path = pathResolver.resolve(request);
        executionContext.setAttribute(ExecutionContext.ATTR_RESOLVED_PATH, path.getPath());

        return path.getRules()
                .stream()
                .filter(rule -> rule.isEnabled() && rule.getMethods().contains(request.method()))
                .map((Function<Rule, Policy>) rule -> {
                    PolicyMetadata policyMetadata = policyManager.get(rule.getPolicy().getName());
                    if (policyMetadata == null) {
                        LOGGER.error("Policy {} can't be found in registry. Unable to apply it for request {}",
                                rule.getPolicy().getName(), request.id());
                    } else if (
                            ((streamType == StreamType.ON_REQUEST &&
                                    (policyMetadata.method(OnRequest.class) != null || policyMetadata.method(OnRequestContent.class) != null)) ||
                                    (streamType == StreamType.ON_RESPONSE && (
                                            policyMetadata.method(OnResponse.class) != null || policyMetadata.method(OnResponseContent.class) != null)))) {

                        PolicyConfiguration policyConfiguration = policyConfigurationFactory.create(
                                policyMetadata.configuration(), rule.getPolicy().getConfiguration());

                        // TODO: this should be done only if policy is injectable
                        Map<Class<?>, Object> injectables = new HashMap<>(2);
                        injectables.put(policyMetadata.configuration(), policyConfiguration);
                        if (policyMetadata.context() != null) {
                            injectables.put(policyMetadata.context().getClass(), policyMetadata.context());
                        }

                        Object policyInst = policyFactory.create(policyMetadata, injectables);

                        if (policyInst != null) {
                            LOGGER.debug("Policy {} has been added to the chain for request {}", policyMetadata.id(), request.id());
                            return PolicyImpl
                                    .target(policyInst)
                                    .definition(policyMetadata)
                                    .build();
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
