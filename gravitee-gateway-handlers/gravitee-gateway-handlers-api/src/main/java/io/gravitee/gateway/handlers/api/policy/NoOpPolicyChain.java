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
import io.gravitee.gateway.policy.Policy;
import io.gravitee.gateway.policy.impl.PolicyChain;

import java.util.Iterator;
import java.util.List;

/**
 * A simple no-op policy chain used to chain an empty policy collection.
 *
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
class NoOpPolicyChain extends PolicyChain {

    public NoOpPolicyChain(List<Policy> policies, ExecutionContext executionContext) {
        super(policies, executionContext);
    }

    @Override
    public void doNext(Request request, Response response) {
        resultHandler.handle(SUCCESS_POLICY_CHAIN);
    }

    @Override
    protected void execute(Policy policy, Object... args) throws Exception {
        // Nothing to do
    }

    @Override
    protected Iterator<Policy> iterator() {
        return null;
    }
}
