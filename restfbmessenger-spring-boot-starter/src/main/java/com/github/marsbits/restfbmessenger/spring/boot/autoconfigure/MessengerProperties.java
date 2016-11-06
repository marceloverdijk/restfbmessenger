/*
 * Copyright 2015-2016 the original author or authors.
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

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * MessengerConfig properties for Messenger.
 *
 * @author Marcel Overdijk
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "restfbmessenger")
public class MessengerProperties {

    /**
     * Enable Messenger for the application.
     */
    private boolean enabled = true;

    /**
     * The verify token.
     */
    private String verifyToken;

    /**
     * The access token.
     */
    private String accessToken;

    /**
     * The app secret.
     */
    private String appSecret;

    /**
     * The Graph API version.
     */
    private String apiVersion = DefaultMessenger.DEFAULT_API_VERSION.getUrlElement();

    @Valid
    private Webhook webhook = new Webhook();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String version) {
        this.apiVersion = apiVersion;
    }

    public Webhook getWebhook() {
        return webhook;
    }

    public void setWebhook(Webhook webhook) {
        this.webhook = webhook;
    }

    public static class Webhook {

        /**
         * Enable Webhook servlet.
         */
        private boolean enabled = true;

        /**
         * The path of the webhook when enabled.
         */
        @NotNull
        @Pattern(regexp = "/[^?#]*", message = "Path must start with /")
        private String path = "/webhook";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
