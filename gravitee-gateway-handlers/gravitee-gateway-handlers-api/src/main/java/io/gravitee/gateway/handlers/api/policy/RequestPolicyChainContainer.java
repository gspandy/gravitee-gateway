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
import io.gravitee.gateway.api.handler.Handler;
import io.gravitee.gateway.policy.StreamType;
import io.gravitee.gateway.policy.impl.PolicyChain;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class RequestPolicyChainContainer {

    private final Iterator<PolicyChainResolver> iterator;
    private Handler<PolicyChainResult> resultHandler;
    private PolicyChain lastPolicyChain;

    public RequestPolicyChainContainer(List<PolicyChainResolver> policyChainResolvers) {
        Objects.requireNonNull(policyChainResolvers, "Policy chain must not be null");

        this.iterator = policyChainResolvers.iterator();
    }

    public void execute(Request request, Response response, ExecutionContext executionContext) {
        if (iterator.hasNext()) {
            PolicyChainResolver policyChainResolver = iterator.next();
            PolicyChain policyChain = policyChainResolver.resolve(StreamType.ON_REQUEST, request, response, executionContext);
            lastPolicyChain = policyChain;
            policyChain.setResultHandler(policyResult -> {
                if (!policyResult.isFailure()) {
                    execute(request, response, executionContext);
                } else {
                    resultHandler.handle(new PolicyChainResult(lastPolicyChain, policyResult));
                }
            });
            policyChain.doNext(request, response);
        } else {
            resultHandler.handle(new PolicyChainResult(lastPolicyChain, null));
        }
    }

    public void setResultHandler(Handler<PolicyChainResult> resultHandler) {
        this.resultHandler = resultHandler;
    }
}
