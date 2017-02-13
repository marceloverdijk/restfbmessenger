/*
 * Copyright 2015-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.marsbits.restfbmessenger.webhook;

import com.github.marsbits.restfbmessenger.Messenger;
import com.restfb.types.webhook.WebhookObject;

/**
 * Interface to be implemented by applications to handle webhook callbacks. <p> For most applications it will be more convenient to extend
 * the {@link AbstractCallbackHandler} instead.
 *
 * @author Marcel Overdijk
 * @see AbstractCallbackHandler
 * @since 1.0.0
 */
public interface CallbackHandler {

    /**
     * Handles a webhook callback.
     *
     * @param messenger     the {@code Messenger} instance that retrieved the callback
     * @param webhookObject the webhook object containing the callback request data
     */
    void onCallback(Messenger messenger, WebhookObject webhookObject);
}
