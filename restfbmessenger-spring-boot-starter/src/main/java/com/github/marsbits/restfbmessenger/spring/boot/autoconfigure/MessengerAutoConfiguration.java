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

package com.github.marsbits.restfbmessenger.spring.boot.autoconfigure;

import com.github.marsbits.restfbmessenger.DefaultMessenger;
import com.github.marsbits.restfbmessenger.Messenger;
import com.github.marsbits.restfbmessenger.webhook.CallbackHandler;
import com.github.marsbits.restfbmessenger.webhook.WebhookServlet;
import com.restfb.Version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Messenger.
 *
 * @author Marcel Overdijk
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "restfbmessenger", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(MessengerProperties.class)
public class MessengerAutoConfiguration {

    @Configuration
    @ConditionalOnMissingBean(Messenger.class)
    protected static class MessengerConfiguration {

        @Autowired(required = false)
        private CallbackHandler callbackHandler;

        @Autowired
        private MessengerProperties properties;

        @Bean
        public Messenger messenger() {
            return new DefaultMessenger(properties.getVerifyToken(), properties.getAccessToken(),
                    properties.getAppSecret(), callbackHandler,
                    Version.getVersionFromString(properties.getApiVersion()));
        }
    }


    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(prefix = "restfbmessenger.webhook", name = "enabled",
            matchIfMissing = true)
    protected static class WebhookConfiguration {

        @Autowired
        private MessengerProperties properties;

        @Bean
        public ServletRegistrationBean webhookServlet(Messenger messenger) {
            String path = properties.getWebhook().getPath();
            String urlMapping = (path.endsWith("/") ? path + "*" : path + "/*");
            WebhookServlet servlet = new WebhookServlet(messenger);
            return new ServletRegistrationBean(servlet, urlMapping);
        }
    }
}
