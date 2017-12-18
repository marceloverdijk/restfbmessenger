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

package com.github.marsbits.restfbmessenger;

import com.github.marsbits.restfbmessenger.webhook.CallbackHandler;
import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.ProfilePictureSource;
import com.restfb.types.User;
import com.restfb.types.send.CallToAction;
import com.restfb.types.send.DomainActionTypeEnum;
import com.restfb.types.send.Greeting;
import com.restfb.types.send.Message;
import com.restfb.types.send.PageMessageTag;
import com.restfb.types.send.SendResponse;
import com.restfb.types.send.SettingTypeEnum;
import com.restfb.types.send.ThreadStateEnum;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.util.EncodingUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.github.marsbits.restfbmessenger.DefaultMessenger.ACCOUNT_LINKING_URL_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.CALL_TO_ACTIONS_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.DOMAIN_ACTION_TYPE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.GREETING_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.HMAC_SHA1_ALGORITHM;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.OBJECT_PAGE_VALUE;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.PAGE_MESSAGE_TAGS_PATH;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.SETTING_TYPE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.SIGNATURE_PREFIX;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.THREAD_SETTINGS_PATH;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.THREAD_STATE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.USER_FIELDS_DEFAULT_VALUE;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.USER_FIELDS_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.DefaultMessenger.WHITELISTED_DOMAINS_PARAM_NAME;
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
    private FacebookOAuthException facebookOAuthException;
    private JsonMapper jsonMapper;

    @Before
    public void setUp() {
        this.callbackHandler = mock(CallbackHandler.class);
        this.facebookClient = mock(FacebookClient.class);
        this.facebookOAuthException = mock(FacebookOAuthException.class);
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
    public void testHandleCallbackHandlerIgnoresInvalidSignatureWhenAppSecretIsNull() throws Exception {
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
    public void testHandleCallbackHandlerDoesNotInvokeCallbackHandlerWhenCallbackHandlerIsNull() throws Exception {
        messenger = new DefaultMessenger(verifyToken, appSecret, null, facebookClient);
        String signature = generateSignature(payload, appSecret);
        messenger.handleCallback(payload, signature);
        verify(jsonMapper, never()).toJavaObject(any(String.class), any(Class.class));
        verify(callbackHandler, never()).onCallback(any(Messenger.class), any(WebhookObject.class));
    }

    @Test
    public void testHandleCallbackHandlerDoesNotInvokeCallbackHandlerWhenWebhookObjectIsNotPage() throws Exception {
        String signature = generateSignature(payload, appSecret);
        WebhookObject webhookObject = new WebhookObject();
        webhookObject.setObject("not page");
        when(jsonMapper.toJavaObject(payload, WebhookObject.class)).thenReturn(webhookObject);
        messenger.handleCallback(payload, signature);
        verify(callbackHandler, never()).onCallback(any(Messenger.class), any(WebhookObject.class));
    }

    @Test
    public void testHandleCallbackHandlerDoesNotMapPayloedWhenHandlerWhenSignatureIsNotValid() throws Exception {
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
        ProfilePictureSource picture = new ProfilePictureSource();
        picture.setUrl("url");
        user.setPicture(picture);
        user.setLocale("locale");
        user.setTimezone(1.00);
        user.setGender("gender");
        when(facebookClient.fetchObject(userId, User.class, Parameter.with(USER_FIELDS_PARAM_NAME, USER_FIELDS_DEFAULT_VALUE)))
                .thenReturn(user);
        assertThat(messenger.getUserProfile(userId), is(user));
    }

    @Test
    public void testGetUserProfileWithSpecificFieldsAsVarargs() {
        String userId = "user id";
        User user = new User();
        user.setFirstName("first name");
        user.setLastName("last name");
        when(facebookClient.fetchObject(userId, User.class, Parameter.with(USER_FIELDS_PARAM_NAME, "first_name,last_name")))
                .thenReturn(user);
        assertThat(messenger.getUserProfile(userId, "first_name", "last_name"), is(user));
    }

    @Test
    public void testGetUserProfileWithSpecificFieldsAsString() {
        String userId = "user id";
        User user = new User();
        user.setFirstName("first name");
        user.setLastName("last name");
        when(facebookClient.fetchObject(userId, User.class, Parameter.with(USER_FIELDS_PARAM_NAME, "first_name,last_name")))
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

    @Test
    public void testSetGreetingAsText() {
        String greeting = "greeting";
        messenger.setGreeting(greeting);
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.greeting),
                Parameter.with(GREETING_PARAM_NAME, new Greeting(greeting)));
    }

    @Test(expected = FacebookException.class)
    public void testSetGreetingAsTextThrowsFacebookExceptionWhenRequestFails() {
        String greeting = "greeting";
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.greeting),
                Parameter.with(GREETING_PARAM_NAME, new Greeting(greeting))))
                .thenThrow(facebookOAuthException);
        messenger.setGreeting(greeting);
    }

    @Test
    public void testSetGreeting() {
        Greeting greeting = new Greeting("greeting");
        messenger.setGreeting(greeting);
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.greeting),
                Parameter.with(GREETING_PARAM_NAME, greeting));
    }

    @Test(expected = FacebookException.class)
    public void testSetGreetingThrowsFacebookExceptionWhenRequestFails() {
        Greeting greeting = new Greeting("greeting");
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.greeting),
                Parameter.with(GREETING_PARAM_NAME, greeting)))
                .thenThrow(facebookOAuthException);
        messenger.setGreeting(greeting);
    }

    @Test
    public void testRemoveGreeting() {
        messenger.removeGreeting();
        verify(facebookClient).deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.greeting));
    }

    @Test(expected = FacebookException.class)
    public void testRemoveGreetingThrowsFacebookExceptionWhenRequestFails() {
        when(facebookClient.deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.greeting)))
                .thenThrow(facebookOAuthException);
        messenger.removeGreeting();
    }

    @Test
    public void testSetGetStartedButton() {
        CallToAction callToAction = new CallToAction("get started");
        List<CallToAction> callToActions = Arrays.asList(callToAction);
        messenger.setGetStartedButton(callToAction);
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.new_thread),
                Parameter.with(CALL_TO_ACTIONS_PARAM_NAME, callToActions));
    }

    @Test(expected = FacebookException.class)
    public void testSetGetStartedButtonThrowsFacebookExceptionWhenRequestFails() {
        CallToAction callToAction = new CallToAction("get started");
        List<CallToAction> callToActions = Arrays.asList(callToAction);
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.new_thread),
                Parameter.with(CALL_TO_ACTIONS_PARAM_NAME, callToActions)))
                .thenThrow(facebookOAuthException);
        messenger.setGetStartedButton(callToAction);
    }

    @Test
    public void testSetGetStartedButtonWithPayload() {
        CallToAction callToAction = new CallToAction("get started");
        List<CallToAction> callToActions = Arrays.asList(callToAction);
        messenger.setGetStartedButton("get started");
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.new_thread),
                Parameter.with(CALL_TO_ACTIONS_PARAM_NAME, callToActions));
    }

    @Test(expected = FacebookException.class)
    public void testSetGetStartedButtonWithPayloadThrowsFacebookExceptionWhenRequestFails() {
        CallToAction callToAction = new CallToAction("get started");
        List<CallToAction> callToActions = Arrays.asList(callToAction);
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.new_thread),
                Parameter.with(CALL_TO_ACTIONS_PARAM_NAME, callToActions)))
                .thenThrow(facebookOAuthException);
        messenger.setGetStartedButton("get started");
    }

    @Test
    public void testRemoveGetStartedButton() {
        messenger.removeGetStartedButton();
        verify(facebookClient).deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.new_thread));
    }

    @Test(expected = FacebookException.class)
    public void testRemoveGetStartedButtonThrowsFacebookExceptionWhenRequestFails() {
        when(facebookClient.deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.new_thread)))
                .thenThrow(facebookOAuthException);
        messenger.removeGetStartedButton();
    }

    @Test
    public void testSetPersistentMenu() {
        List<CallToAction> callToActions = Arrays.asList(
                new CallToAction("call to action 1"),
                new CallToAction("call to action 2")
        );
        messenger.setPersistentMenu(callToActions);
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.existing_thread),
                Parameter.with(CALL_TO_ACTIONS_PARAM_NAME, callToActions));
    }

    @Test(expected = FacebookException.class)
    public void testSetPersistentMenuThrowsFacebookExceptionWhenRequestFails() {
        List<CallToAction> callToActions = Arrays.asList(
                new CallToAction("call to action 1"),
                new CallToAction("call to action 2")
        );
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.existing_thread),
                Parameter.with(CALL_TO_ACTIONS_PARAM_NAME, callToActions)))
                .thenThrow(facebookOAuthException);
        messenger.setPersistentMenu(callToActions);
    }

    @Test
    public void testRemovePersistentMenu() {
        messenger.removePersistentMenu();
        verify(facebookClient).deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.existing_thread));
    }

    @Test(expected = FacebookException.class)
    public void testRemovePersistentMenuThrowsFacebookExceptionWhenRequestFails() {
        when(facebookClient.deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.existing_thread)))
                .thenThrow(facebookOAuthException);
        messenger.removePersistentMenu();
    }

    @Test
    public void testSetAccountLinkingUrl() {
        String url = "account linking url";
        messenger.setAccountLinkingUrl(url);
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.account_linking),
                Parameter.with(ACCOUNT_LINKING_URL_PARAM_NAME, url));
    }

    @Test(expected = FacebookException.class)
    public void testSetAccountLinkingUrlThrowsFacebookExceptionWhenRequestFails() {
        String url = "account linking url";
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.account_linking),
                Parameter.with(ACCOUNT_LINKING_URL_PARAM_NAME, url)))
                .thenThrow(facebookOAuthException);
        messenger.setAccountLinkingUrl(url);
    }

    @Test
    public void testRemoveAccountLinkingUrl() {
        messenger.removeAccountLinkingUrl();
        verify(facebookClient).deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.account_linking));
    }

    @Test(expected = FacebookException.class)
    public void testRemoveAccountLinkingUrlThrowsFacebookExceptionWhenRequestFails() {
        when(facebookClient.deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.account_linking)))
                .thenThrow(facebookOAuthException);
        messenger.removeAccountLinkingUrl();
    }

    @Test
    public void testAddDomainWhitelisting() {
        String url = "domain url";
        List<String> urls = Arrays.asList(url);
        messenger.addDomainWhitelisting(url);
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.add));
    }

    @Test(expected = FacebookException.class)
    public void testAddDomainWhitelistingThrowsFacebookExceptionWhenRequestFails() {
        String url = "domain url";
        List<String> urls = Arrays.asList(url);
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.add)))
                .thenThrow(facebookOAuthException);
        messenger.addDomainWhitelisting(url);
    }

    @Test
    public void testAddMultipleDomainWhitelisting() {
        List<String> urls = Arrays.asList("domain url 1", "domain url 2");
        messenger.addDomainWhitelisting(urls);
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.add));
    }

    @Test(expected = FacebookException.class)
    public void testAddMultipleDomainWhitelistingThrowsFacebookExceptionWhenRequestFails() {
        List<String> urls = Arrays.asList("domain url 1", "domain url 2");
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.add)))
                .thenThrow(facebookOAuthException);
        messenger.addDomainWhitelisting(urls);
    }

    @Test
    public void testRemoveDomainWhitelisting() {
        String url = "domain url";
        List<String> urls = Arrays.asList(url);
        messenger.removeDomainWhitelisting(url);
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.remove));
    }

    @Test(expected = FacebookException.class)
    public void testRemoveDomainWhitelistingThrowsFacebookExceptionWhenRequestFails() {
        String url = "domain url";
        List<String> urls = Arrays.asList(url);
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.remove)))
                .thenThrow(facebookOAuthException);
        messenger.removeDomainWhitelisting(url);
    }

    @Test
    public void testRemoveMultipleDomainWhitelisting() {
        List<String> urls = Arrays.asList("domain url 1", "domain url 2");
        messenger.removeDomainWhitelisting(urls);
        verify(facebookClient).publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.remove));
    }

    @Test(expected = FacebookException.class)
    public void testRemoveMultipleDomainWhitelistingThrowsFacebookExceptionWhenRequestFails() {
        List<String> urls = Arrays.asList("domain url 1", "domain url 2");
        when(facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.remove)))
                .thenThrow(facebookOAuthException);
        messenger.removeDomainWhitelisting(urls);
    }

    @Test
    public void testGetMessageTags() {
        Connection<PageMessageTag> connection = mock(Connection.class);
        when(connection.getData()).thenReturn(new ArrayList<PageMessageTag>());
        when(facebookClient.fetchConnection(PAGE_MESSAGE_TAGS_PATH, PageMessageTag.class)).thenReturn(connection);
        messenger.getMessageTags();
        verify(facebookClient).fetchConnection(PAGE_MESSAGE_TAGS_PATH, PageMessageTag.class);
    }

    @Test(expected = FacebookException.class)
    public void testGetMessageTagsThrowsFacebookExceptionWhenRequestFails() {
        when(facebookClient.fetchConnection(PAGE_MESSAGE_TAGS_PATH, PageMessageTag.class)).thenThrow(facebookOAuthException);
        messenger.getMessageTags();
    }

    private String generateSignature(String payload, String appSecret) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(appSecret.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] hmac = mac.doFinal(payload.getBytes());
        return SIGNATURE_PREFIX + new String(EncodingUtils.encodeHex(hmac));
    }
}
