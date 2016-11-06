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

package com.github.marsbits.restfbmessenger;

import com.github.marsbits.restfbmessenger.webhook.CallbackHandler;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.types.User;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.util.EncodingUtils;

import org.junit.Before;
import org.junit.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.github.marsbits.restfbmessenger.DefaultMessenger.HMAC_SHA1_ALGORITHM;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.OBJECT_PAGE_VALUE;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.SIGNATURE_PREFIX;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.USER_FIELDS_DEFAULT_VALUE;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.USER_FIELDS_PARAM_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DefaultMessenger}.
 *
 * @author Marcel Overdijk
 */
public class DefaultMessengerTests {

    private String verifyToken = "verify token";
    private String appSecret = "app secret";
    private String payload = "the payload";

    private DefaultMessenger messenger;

    private CallbackHandler callbackHandler;
    private FacebookClient facebookClient;
    private JsonMapper jsonMapper;

    @Before
    public void setUp() {
        this.callbackHandler = mock(CallbackHandler.class);
        this.facebookClient = mock(FacebookClient.class);
        this.jsonMapper = mock(JsonMapper.class);
        this.messenger = new DefaultMessenger(verifyToken, appSecret, callbackHandler, facebookClient);
        when(facebookClient.getJsonMapper()).thenReturn(jsonMapper);
    }

    @Test
    public void testVerifyTokenReturnsTrueWhenVerifyTokenIsValid() {
        assertThat(messenger.verifyToken(verifyToken), is(true));
    }

    @Test
    public void testVerifyTokenReturnsFalseWhenVerifyTokenIsInvalid() {
        assertThat(messenger.verifyToken("invalid verify token"), is(false));
    }

    @Test
    public void testHandleCallbackHandler() throws Exception {
        String signature = generateSignature(payload, appSecret);
        WebhookObject webhookObject = new WebhookObject();
        webhookObject.setObject(OBJECT_PAGE_VALUE);
        when(jsonMapper.toJavaObject(payload, WebhookObject.class)).thenReturn(webhookObject);
        messenger.handleCallback(payload, signature);
        verify(callbackHandler).onCallback(messenger, webhookObject);
    }

    @Test
    public void testHandleCallbackHandlerIgnoresInvalidSignatureWhenAppSecretIsNull()
            throws Exception {
        messenger = new DefaultMessenger(verifyToken, null, callbackHandler, facebookClient);
        when(facebookClient.getJsonMapper()).thenReturn(jsonMapper);
        String signature = "invalid signtaure";
        WebhookObject webhookObject = new WebhookObject();
        webhookObject.setObject(OBJECT_PAGE_VALUE);
        when(jsonMapper.toJavaObject(payload, WebhookObject.class)).thenReturn(webhookObject);
        messenger.handleCallback(payload, signature);
        verify(callbackHandler).onCallback(messenger, webhookObject);
    }

    @Test
    public void testHandleCallbackHandlerDoesNotInvokeCallbackHandlerWhenCallbackHandlerIsNull()
            throws Exception {
        messenger = new DefaultMessenger(verifyToken, appSecret, null, facebookClient);
        String signature = generateSignature(payload, appSecret);
        messenger.handleCallback(payload, signature);
        verify(jsonMapper, never()).toJavaObject(any(String.class), any(Class.class));
        verify(callbackHandler, never()).onCallback(any(Messenger.class), any(WebhookObject.class));
    }

    @Test
    public void testHandleCallbackHandlerDoesNotInvokeCallbackHandlerWhenWebhookObjectIsNotPage()
            throws Exception {
        String signature = generateSignature(payload, appSecret);
        WebhookObject webhookObject = new WebhookObject();
        webhookObject.setObject("not page");
        when(jsonMapper.toJavaObject(payload, WebhookObject.class)).thenReturn(webhookObject);
        messenger.handleCallback(payload, signature);
        verify(callbackHandler, never()).onCallback(any(Messenger.class), any(WebhookObject.class));
    }

    @Test
    public void testHandleCallbackHandlerDoesNotMapPayloedWhenHandlerWhenSignatureIsNotValid()
            throws Exception {
        String signature = "invalid signature";
        messenger.handleCallback(payload, signature);
        verify(jsonMapper, never()).toJavaObject(any(String.class), any(Class.class));
        verify(callbackHandler, never()).onCallback(any(Messenger.class), any(WebhookObject.class));
    }

    @Test
    public void testGetUserProfile() {
        String userId = "user id";
        User user = new User();
        user.setFirstName("first name");
        user.setLastName("last name");
        User.Picture picture = new User.Picture();
        picture.setUrl("url");
        user.setPicture(picture);
        user.setLocale("locale");
        user.setTimezone(1.00);
        user.setGender("gender");
        when(facebookClient.fetchObject(userId, User.class,
                Parameter.with(USER_FIELDS_PARAM_NAME, USER_FIELDS_DEFAULT_VALUE)))
                .thenReturn(user);
        assertThat(messenger.getUserProfile(userId), is(user));
    }

    @Test
    public void testGetUserProfileWithSpecificFieldsAsVarargs() {
        String userId = "user id";
        User user = new User();
        user.setFirstName("first name");
        user.setLastName("last name");
        when(facebookClient.fetchObject(userId, User.class,
                Parameter.with(USER_FIELDS_PARAM_NAME, "first_name,last_name")))
                .thenReturn(user);
        assertThat(messenger.getUserProfile(userId, "first_name", "last_name"), is(user));
    }

    @Test
    public void testGetUserProfileWithSpecificFieldsAsString() {
        String userId = "user id";
        User user = new User();
        user.setFirstName("first name");
        user.setLastName("last name");
        when(facebookClient.fetchObject(userId, User.class,
                Parameter.with(USER_FIELDS_PARAM_NAME, "first_name,last_name")))
                .thenReturn(user);
        assertThat(messenger.getUserProfile(userId, "first_name,last_name"), is(user));
    }

    @Test
    public void testSendOperationsNotNull() throws Exception {
        assertThat(messenger.send(), is(notNullValue()));
    }

    @Test
    public void testVerifySignatureReturnsTrueWhenVerifyTokenIsValid() throws Exception {
        String payload = "the payload";
        String signature = generateSignature(payload, appSecret);
        assertThat(messenger.verifySignature(payload, signature), is(true));
    }

    @Test
    public void testVerifySignatureReturnsFalseWhenVerifyTokenIsInvalid() throws Exception {
        String payload = "the payload";
        String signature = SIGNATURE_PREFIX + "invalid signature";
        assertThat(messenger.verifySignature(payload, signature), is(false));
    }

    @Test
    public void testVerifySignatureReturnsTrueWhenVerifyTokenIsInvalid() throws Exception {
        String payload = "the payload";
        String signature = SIGNATURE_PREFIX + "invalid signature";
        assertThat(messenger.verifySignature(payload, signature), is(false));
    }

    private String generateSignature(String payload, String appSecret) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(appSecret.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] hmac = mac.doFinal(payload.getBytes());
        return SIGNATURE_PREFIX + new String(EncodingUtils.encodeHex(hmac));
    }
}
