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
package io.gravitee.gateway.http.core.invoker;

import io.gravitee.common.http.HttpMethod;
import io.gravitee.gateway.api.*;
import io.gravitee.gateway.api.handler.Handler;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 * @author GraviteeSource Team
 */
public class DefaultHttpInvoker extends AbstractHttpInvoker {

    @Override
    protected ClientRequest invoke0(String host, int port, HttpMethod method, String requestUri, Request serverRequest,
                                    ExecutionContext executionContext, Handler<ClientResponse> response) {
        return httpClient.request(
                host, port, method, requestUri, serverRequest, response);
    }
}
