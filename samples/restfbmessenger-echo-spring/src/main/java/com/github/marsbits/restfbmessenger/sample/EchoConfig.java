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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.marsbits.restfbmessenger.DefaultMessenger;
import com.github.marsbits.restfbmessenger.Messenger;

/**
 * The Echo config.
 *
 * @author Marcel Overdijk
 */
@Configuration
public class EchoConfig {

    @Autowired
    private EchoCallbackHandler echoCallbackHandler;

    @Bean
    public Messenger messenger() {
        String verifyToken = "the-verify-token";
        String accessToken = "the-access-token";
        String appSecret = "the-app-secret";
        return new DefaultMessenger(verifyToken, accessToken, appSecret, echoCallbackHandler);
    }
}
