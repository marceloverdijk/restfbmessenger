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

import com.github.marsbits.restfbmessenger.send.DefaultSendOperations;
import com.github.marsbits.restfbmessenger.send.SendOperations;
import com.github.marsbits.restfbmessenger.webhook.CallbackHandler;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;
import com.restfb.types.send.CallToAction;
import com.restfb.types.send.DomainActionTypeEnum;
import com.restfb.types.send.Greeting;
import com.restfb.types.send.Message;
import com.restfb.types.send.SendResponse;
import com.restfb.types.send.SettingTypeEnum;
import com.restfb.types.send.ThreadStateEnum;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.util.EncodingUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;

/**
 * Default implementation of the {@link Messenger} interface.
 *
 * @author Marcel Overdijk
 * @since 1.0.0
 */
public class DefaultMessenger implements Messenger {

    private static final Logger logger = Logger.getLogger(DefaultMessenger.class.getName());

    public static final Version DEFAULT_API_VERSION = Version.VERSION_2_8;

    public static final String THREAD_SETTINGS_PATH = "me/thread_settings";

    public static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    public static final String SIGNATURE_PREFIX = "sha1=";

    public static final String OBJECT_PAGE_VALUE = "page";

    public static final String USER_FIELDS_PARAM_NAME = "fields";
    public static final String USER_FIELDS_DEFAULT_VALUE = "first_name,last_name,profile_pic,locale,timezone,gender";

    public static final String SETTING_TYPE_PARAM_NAME = "setting_type";
    public static final String GREETING_PARAM_NAME = "greeting";
    public static final String THREAD_STATE_PARAM_NAME = "thread_state";
    public static final String CALL_TO_ACTIONS_PARAM_NAME = "call_to_actions";
    public static final String ACCOUNT_LINKING_URL_PARAM_NAME = "account_linking_url";
    public static final String WHITELISTED_DOMAINS_PARAM_NAME = "whitelisted_domains";
    public static final String DOMAIN_ACTION_TYPE_PARAM_NAME = "domain_action_type";

    protected String verifyToken;
    protected String appSecret;

    protected FacebookClient facebookClient;

    protected SendOperations sendOperations;
    protected CallbackHandler callbackHandler;

    /**
     * Creates a {@code DefaultMessenger} instance. If the app secret is not provided ({@code null} the callback signature verification will
     * be disabled.
     *
     * @param verifyToken     the verify token
     * @param accessToken     the access token
     * @param appSecret       the app secret
     * @param callbackHandler the callback handler
     */
    public DefaultMessenger(String verifyToken, String accessToken, String appSecret, CallbackHandler callbackHandler) {
        this(verifyToken, accessToken, appSecret, callbackHandler, DEFAULT_API_VERSION);
    }

    /**
     * Creates a {@code DefaultMessenger} instance.
     *
     * If the app secret is not provided ({@code null} the callback signature verification will be disabled.
     *
     * @param verifyToken     the verify token
     * @param accessToken     the access token
     * @param appSecret       the app secret
     * @param callbackHandler the callback handler
     * @param apiVersion      the api version
     */
    public DefaultMessenger(String verifyToken, String accessToken, String appSecret, CallbackHandler callbackHandler, Version apiVersion) {
        this(verifyToken, appSecret, callbackHandler, new DefaultFacebookClient(accessToken, appSecret, apiVersion));
    }

    /**
     * Creates a {@code DefaultMessenger} instance.
     *
     * If the app secret is not provided ({@code null} the callback signature verification will be disabled.
     *
     * The access token and api version need to be configured on the provided facebook client.
     *
     * @param verifyToken     the verify token
     * @param appSecret       the app secret
     * @param callbackHandler the callback handler
     * @param facebookClient  the facebook client
     */
    public DefaultMessenger(String verifyToken, String appSecret, CallbackHandler callbackHandler, FacebookClient facebookClient) {
        this.verifyToken = verifyToken;
        this.appSecret = appSecret;
        this.callbackHandler = callbackHandler;
        this.facebookClient = facebookClient;
        this.sendOperations = new DefaultSendOperations(facebookClient);
        if (appSecret == null) {
            if (logger.isLoggable(WARNING)) {
                logger.warning("App secret not configured; webhook signature will not be verified");
            }
        }
        if (callbackHandler == null) {
            if (logger.isLoggable(WARNING)) {
                logger.warning("Webhook handler not configured; webhook callbacks will not be handled");
            }
        }
    }

    @Override
    public boolean verifyToken(String token) {
        return token != null && token.equals(verifyToken);
    }

    @Override
    public void handleCallback(String payload, String signature) {
        if (callbackHandler == null) {
            if (logger.isLoggable(FINE)) {
                logger.fine("Webhook received but no webhook handler configured");
            }
        } else {
            if (logger.isLoggable(FINE)) {
                logger.fine(format("Handling webhook for payload: %s, signature: %s", payload, signature));
            }
            if (appSecret != null) {
                if (!verifySignature(payload, signature)) {
                    if (logger.isLoggable(FINE)) {
                        logger.fine("Invalid signature received; webhook handler not invoked");
                    }
                    return;
                }
            }
            WebhookObject webhookObject = facebookClient.getJsonMapper().toJavaObject(payload, WebhookObject.class);
            if (!OBJECT_PAGE_VALUE.equals(webhookObject.getObject())) {
                if (logger.isLoggable(FINE)) {
                    logger.fine(format("Ignoring webhook object: %s; webhook handler not invoked", webhookObject.getObject()));
                }
                return;
            }
            callbackHandler.onCallback(this, webhookObject);
        }
    }


    protected boolean verifySignature(String payload, String signature) {
        if (signature == null || !signature.startsWith(SIGNATURE_PREFIX)) {
            if (logger.isLoggable(FINE)) {
                logger.fine(format("Invalid signature: %s", signature));
            }
            return false;
        }
        String signatureHash = signature.substring(SIGNATURE_PREFIX.length());
        String expectedHash = generateHmac(payload);
        return expectedHash.equals(signatureHash);
    }

    private String generateHmac(String payload) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(appSecret.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] hmac = mac.doFinal(payload.getBytes());
            return new String(EncodingUtils.encodeHex(hmac));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    format("%s algorithm not supported", HMAC_SHA1_ALGORITHM));
        } catch (InvalidKeyException e) {
            throw new IllegalStateException("Signing key is inappropriate");
        }
    }

    @Override
    public User getUserProfile(String userId) throws FacebookException {
        return getUserProfile(userId, USER_FIELDS_DEFAULT_VALUE);
    }

    @Override
    public User getUserProfile(String userId, String... fields) throws FacebookException {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (String field : fields) {
            sb.append(sep).append(field);
            sep = ",";
        }
        return getUserProfile(userId, sb.toString());
    }

    @Override
    public User getUserProfile(String userId, String fields) throws FacebookException {
        requireNonNull(userId, "'userId' must not be null");
        return facebookClient.fetchObject(userId, User.class, Parameter.with(USER_FIELDS_PARAM_NAME, fields));
    }

    @Override
    public SendOperations send() {
        return sendOperations;
    }

    @Override
    public void setGreeting(String greeting) throws FacebookException {
        requireNonNull(greeting, "'greeting' must not be null");
        setGreeting(new Greeting(greeting));
    }

    @Override
    public void setGreeting(Greeting greeting) throws FacebookException {
        requireNonNull(greeting, "'greeting' must not be null");
        facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.greeting),
                Parameter.with(GREETING_PARAM_NAME, greeting));
    }

    @Override
    public void removeGreeting() throws FacebookException {
        facebookClient.deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.greeting));
    }

    @Override
    public void setGetStartedButton(String payload) throws FacebookException {
        requireNonNull(payload, "'payload' must not be null");
        CallToAction callToAction = new CallToAction(payload);
        setGetStartedButton(callToAction);
    }

    @Override
    public void setGetStartedButton(CallToAction callToAction) throws FacebookException {
        requireNonNull(callToAction, "'callToAction' must not be null");
        List<CallToAction> callToActions = Arrays.asList(callToAction);
        facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.new_thread),
                Parameter.with(CALL_TO_ACTIONS_PARAM_NAME, callToActions));
    }

    @Override
    public void removeGetStartedButton() throws FacebookException {
        facebookClient.deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.new_thread));
    }

    @Override
    public void setPersistentMenu(List<CallToAction> callToActions) throws FacebookException {
        requireNonNull(callToActions, "'callToActions' must not be null");
        facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.existing_thread),
                Parameter.with(CALL_TO_ACTIONS_PARAM_NAME, callToActions));
    }

    @Override
    public void removePersistentMenu() throws FacebookException {
        facebookClient.deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.call_to_actions),
                Parameter.with(THREAD_STATE_PARAM_NAME, ThreadStateEnum.existing_thread));
    }

    @Override
    public void setAccountLinkingUrl(String url) throws FacebookException {
        requireNonNull(url, "'url' must not be null");
        facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.account_linking),
                Parameter.with(ACCOUNT_LINKING_URL_PARAM_NAME, url));
    }

    @Override
    public void removeAccountLinkingUrl() throws FacebookException {
        facebookClient.deleteObject(THREAD_SETTINGS_PATH,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.account_linking));
    }

    @Override
    public void addDomainWhitelisting(String url) throws FacebookException {
        requireNonNull(url, "'url' must not be null");
        addDomainWhitelisting(Arrays.asList(url));
    }

    @Override
    public void addDomainWhitelisting(List<String> urls) throws FacebookException {
        requireNonNull(urls, "'urls' must not be null");
        facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.add));
    }

    @Override
    public void removeDomainWhitelisting(String url) throws FacebookException {
        requireNonNull(url, "'url' must not be null");
        removeDomainWhitelisting(Arrays.asList(url));
    }

    @Override
    public void removeDomainWhitelisting(List<String> urls) throws FacebookException {
        requireNonNull(urls, "'urls' must not be null");
        facebookClient.publish(THREAD_SETTINGS_PATH, SendResponse.class,
                Parameter.with(SETTING_TYPE_PARAM_NAME, SettingTypeEnum.domain_whitelisting),
                Parameter.with(WHITELISTED_DOMAINS_PARAM_NAME, urls),
                Parameter.with(DOMAIN_ACTION_TYPE_PARAM_NAME, DomainActionTypeEnum.remove));
    }
}
