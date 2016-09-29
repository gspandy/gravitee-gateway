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

import io.gravitee.gateway.api.ExecutionContext;
import io.gravitee.gateway.api.Request;
import io.gravitee.gateway.api.Response;
import io.gravitee.gateway.policy.*;
import io.gravitee.gateway.policy.impl.PolicyImpl;
import io.gravitee.policy.api.PolicyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A policy resolver based on the plan identified by the consumer api-key.
 *
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class PlanPolicyChainResolver extends AbstractPolicyChainResolver {

    @Autowired
    private PolicyManager policyManager;

    @Autowired
    private PolicyFactory policyFactory;

    @Autowired
    private PolicyConfigurationFactory policyConfigurationFactory;

    @Override
    protected List<Policy> calculate(StreamType streamType, Request request, Response response, ExecutionContext executionContext) {
        if (streamType == StreamType.ON_REQUEST) {
            // The plan identifier has been loaded while validating API Key
            String planId = (String) executionContext.getAttribute(ExecutionContext.ATTR_PLAN);
            List<io.gravitee.definition.model.Policy> policies = api.getPlan(planId).getPolicies();

            return policies.stream()
                    .map((Function<io.gravitee.definition.model.Policy, Policy>) policy -> {
                        PolicyMetadata policyMetadata = policyManager.get(policy.getName());
                        if (policyMetadata == null) {
                            LOGGER.error("Policy {} can't be found in registry. Unable to apply it for request {}",
                                    policy.getName(), request.id());
                        } else {
                            PolicyConfiguration policyConfiguration = policyConfigurationFactory.create(
                                    policyMetadata.configuration(), policy.getConfiguration());

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

        return Collections.emptyList();
    }
}
