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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A policy resolver based on the policy configuration from the API.
 * This policy configuration is done by path / method.
 *
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class ApiKeyPolicyChainResolver extends AbstractPolicyChainResolver {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiKeyPolicyChainResolver.class);

    private final static String API_KEY_POLICY = "api-key";

    @Autowired
    private PolicyManager policyManager;

    @Autowired
    private PolicyFactory policyFactory;

    @Autowired
    private PolicyConfigurationFactory policyConfigurationFactory;

    @Override
    protected List<Policy> calculate(StreamType streamType, Request request, Response response, ExecutionContext executionContext) {
        if (streamType == StreamType.ON_REQUEST) {
            PolicyMetadata policyMetadata = policyManager.get(API_KEY_POLICY);
            if (policyMetadata == null) {
                LOGGER.error("Policy {} can't be found in registry. Unable to apply it for request {}",
                        API_KEY_POLICY, request.id());
            } else {
                PolicyConfiguration policyConfiguration = policyConfigurationFactory.create(
                        policyMetadata.configuration(), "{}");

                // TODO: this should be done only if policy is injectable
                Map<Class<?>, Object> injectables = new HashMap<>(1);
                injectables.put(policyMetadata.configuration(), policyConfiguration);

                Object policyInst = policyFactory.create(policyMetadata, injectables);

                if (policyInst != null) {
                    LOGGER.debug("Policy {} has been added to the api-key chain for request {}", policyMetadata.id(), request.id());
                    return Collections.singletonList(
                            PolicyImpl
                                    .target(policyInst)
                                    .definition(policyMetadata)
                                    .build());
                }
            }
        }

        return Collections.emptyList();
    }
}
