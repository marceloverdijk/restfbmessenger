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

package com.github.marsbits.restfbmessenger.sample;

import com.github.marsbits.restfbmessenger.DefaultMessenger;
import com.github.marsbits.restfbmessenger.Messenger;
import com.github.marsbits.restfbmessenger.MessengerProvider;
import com.github.marsbits.restfbmessenger.webhook.CallbackHandler;

/**
 * The Echo {@code MessengerProvider}.
 *
 * @author Marcel Overdijk
 */
public class EchoMessengerProvider implements MessengerProvider {

    @Override
    public Messenger getMessenger() {
        String verifyToken = System.getProperty("restfbmessenger.verify_token");
        String accessToken = System.getProperty("restfbmessenger.access_token");
        String appSecret = System.getProperty("restfbmessenger.app_secret");
        CallbackHandler callbackHandler = new EchoCallbackHandler();
        return new DefaultMessenger(verifyToken, accessToken, appSecret, callbackHandler);
    }
}
