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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.github.marsbits.restfbmessenger.DefaultMessenger;
import com.github.marsbits.restfbmessenger.Messenger;
import com.github.marsbits.restfbmessenger.webhook.AbstractCallbackHandler;
import com.github.marsbits.restfbmessenger.webhook.CallbackHandler;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

/**
 * Tests for {@link MessengerAutoConfiguration}.
 *
 * @author Marcel Overdijk
 */
public class MessengerAutoConfigurationTests {

    private AnnotationConfigWebApplicationContext context;

    @Before
    public void setUp() {
        this.context = new AnnotationConfigWebApplicationContext();
    }

    @Test
    public void testDefaultAutoConfiguration() {
        load(new Class[] {CallbackHandlerConfig.class, MessengerAutoConfiguration.class},
                "restfbmessenger.verify-token: the_verify_token",
                "restfbmessenger.access-token: the_access_token",
                "restfbmessenger.app-secret: the_app_secret");

        Messenger messenger = this.context.getBean(Messenger.class);
        DefaultMessenger defaultMessenger = (DefaultMessenger) messenger;
        FacebookClient facebookClient =
                (FacebookClient) getFieldValue(defaultMessenger, "facebookClient");
        DefaultFacebookClient defaultFacebookClient = (DefaultFacebookClient) facebookClient;
        CallbackHandler callbackHandler =
                (CallbackHandler) getFieldValue(defaultMessenger, "callbackHandler");

        assertThat((String) getFieldValue(defaultMessenger, "verifyToken"), is("the_verify_token"));
        assertThat((String) getFieldValue(defaultMessenger, "appSecret"), is("the_app_secret"));
        assertThat((String) getFieldValue(defaultFacebookClient, "accessToken"),
                is("the_access_token"));
        assertThat((String) getFieldValue(defaultFacebookClient, "appSecret"),
                is("the_app_secret"));
        assertThat(callbackHandler, is(notNullValue()));
        assertThat(this.context.getBean(ServletRegistrationBean.class).getUrlMappings(),
                hasItem("/webhook/*"));
    }

    @Test
    public void testDisabled() {
        load(new Class[] {MessengerAutoConfiguration.class},
                "restfbmessenger.enabled: false");

        assertThat(this.context.getBeansOfType(Messenger.class).size(), is(0));
        assertThat(this.context.getBeansOfType(ServletRegistrationBean.class).size(), is(0));
    }

    @Test
    public void testNoCallbackHandler() {
        load(new Class[] {MessengerAutoConfiguration.class},
                "restfbmessenger.verify-token: the_verify_token",
                "restfbmessenger.access-token: the_access_token",
                "restfbmessenger.app-secret: the_app_secret");

        Messenger messenger = this.context.getBean(Messenger.class);
        DefaultMessenger defaultMessenger = (DefaultMessenger) messenger;
        CallbackHandler callbackHandler =
                (CallbackHandler) getFieldValue(defaultMessenger, "callbackHandler");

        assertThat(callbackHandler, is(nullValue()));
    }

    @Test
    public void testNoAppSecret() {
        load(new Class[] {CallbackHandlerConfig.class, MessengerAutoConfiguration.class},
                "restfbmessenger.verify-token: the_verify_token",
                "restfbmessenger.access-token: the_access_token");

        Messenger messenger = this.context.getBean(Messenger.class);
        DefaultMessenger defaultMessenger = (DefaultMessenger) messenger;
        FacebookClient facebookClient =
                (FacebookClient) getFieldValue(defaultMessenger, "facebookClient");
        DefaultFacebookClient defaultFacebookClient = (DefaultFacebookClient) facebookClient;

        assertThat(getFieldValue(defaultMessenger, "appSecret"), is(nullValue()));
        assertThat(getFieldValue(defaultFacebookClient, "appSecret"), is(nullValue()));
    }

    @Test
    public void testWebhookDisabled() {
        load(new Class[] {CallbackHandlerConfig.class, MessengerAutoConfiguration.class},
                "restfbmessenger.verify-token: the_verify_token",
                "restfbmessenger.access-token: the_access_token",
                "restfbmessenger.app-secret: the_app_secret",
                "restfbmessenger.webhook.enabled: false");

        assertThat(this.context.getBean(Messenger.class), is(notNullValue()));
        assertThat(this.context.getBeansOfType(ServletRegistrationBean.class).size(), is(0));
    }

    @Test
    public void testCustomWebhookPath() {
        load(new Class[] {CallbackHandlerConfig.class, MessengerAutoConfiguration.class},
                "restfbmessenger.verify-token: the_verify_token",
                "restfbmessenger.access-token: the_access_token",
                "restfbmessenger.app-secret: the_app_secret",
                "restfbmessenger.webhook.path: /my-webhook");

        assertThat(this.context.getBean(ServletRegistrationBean.class).getUrlMappings(),
                hasItem("/my-webhook/*"));
    }

    private void load(Class<?>[] configs, String... environment) {
        this.context.register(configs);
        EnvironmentTestUtils.addEnvironment(this.context, environment);
        this.context.refresh();
    }

    @Configuration
    protected static class CallbackHandlerConfig {

        @Bean
        public CallbackHandler callbackHandler() {
            return new AbstractCallbackHandler() {};
        }
    }

    private Object getFieldValue(Object object, String name) {
        Field field = ReflectionUtils.findField(object.getClass(), name);
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, object);
    }
}
