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
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;

import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;

/**
 * Abstract implementation of the {@link CallbackHandler} interface.
 * <p>
 * This class processes the received webhook and delegates the actual callbacks to the appropriate methods like {@link #onMessage(Messenger,
 * MessagingItem)}, {@link #onPostback(Messenger, MessagingItem)} etc.
 * <p>
 * Custom classes that extend this {@code AbstractCallbackHandler} should override the callback methods that they want to listen and
 * interact to.
 * <p>
 * Special cases are the {@link #fallback(Messenger, MessagingItem)} and {@link #standbyFallback(Messenger, MessagingItem)} methods which
 * are called when the received callback type is unknown. This could potentially happen when Facebook introduces new callback types.
 *
 * @author Marcel Overdijk
 * @since 1.0.0
 */
public abstract class AbstractCallbackHandler implements CallbackHandler {

    private static final Logger logger = Logger.getLogger(AbstractCallbackHandler.class.getName());

    @Override
    public final void onCallback(Messenger messenger, WebhookObject webhookObject) {
        if (webhookObject != null) {
            // Process the entry list.
            for (WebhookEntry entry : webhookObject.getEntryList()) {

                if (logger.isLoggable(FINE)) {
                    logger.fine(format("Handling entry: %s", entry));
                }

                // Process the messaging items.
                if (entry.getMessaging() != null) {
                    for (MessagingItem messaging : entry.getMessaging()) {
                        if (logger.isLoggable(FINE)) {
                            logger.fine(format("Handling messaging item: %s", messaging));
                        }
                        if (messaging.isMessage()) {
                            if (messaging.getMessage().isEcho()) {
                                onMessageEcho(messenger, messaging);
                            } else {
                                onMessage(messenger, messaging);
                            }
                        } else if (messaging.isDelivery()) {
                            onMessageDelivered(messenger, messaging);
                        } else if (messaging.isRead()) {
                            onMessageRead(messenger, messaging);
                        } else if (messaging.isPostback()) {
                            onPostback(messenger, messaging);
                        } else if (messaging.isOptin()) {
                            onOptin(messenger, messaging);
                        } else if (messaging.isReferral()) {
                            onReferral(messenger, messaging);
                        } else if (messaging.isPayment()) {
                            onPayment(messenger, messaging);
                        } else if (messaging.isCheckoutUpdate()) {
                            onCheckoutUpdate(messenger, messaging);
                        } else if (messaging.isAccountLinking()) {
                            onAccountLinking(messenger, messaging);
                        } else if (messaging.isPolicyEnforcement()) {
                            onPolicyEnforcement(messenger, messaging);
                        } else if (messaging.isTakeThreadControl()) {
                            onTakeThreadControl(messenger, messaging);
                        } else if (messaging.isPassThreadControl()) {
                            onPassThreadControl(messenger, messaging);
                        } else if (messaging.getAppRoles() != null) {
                            onAppRoles(messenger, messaging);
                        } else {
                            if (logger.isLoggable(WARNING)) {
                                Class clazz = messaging.getItem() != null ? messaging.getItem().getClass() : null;
                                logger.warning(format("Unknown inner messaging item: %s", clazz));
                            }
                            fallback(messenger, messaging);
                        }
                    }
                }

                // Process the standby items.
                if (entry.getStandby() != null) {
                    for (MessagingItem standby : entry.getStandby()) {
                        if (logger.isLoggable(FINE)) {
                            logger.fine(format("Handling standby item: %s", standby));
                        }
                        if (standby.isMessage()) {
                            if (standby.getMessage().isEcho()) {
                                onStandbyMessageEcho(messenger, standby);
                            } else {
                                onStandbyMessage(messenger, standby);
                            }
                        } else if (standby.isDelivery()) {
                            onStandbyMessageDelivered(messenger, standby);
                        } else if (standby.isRead()) {
                            onStandbyMessageRead(messenger, standby);
                        } else {
                            if (logger.isLoggable(WARNING)) {
                                Class clazz = standby.getItem() != null ? standby.getItem().getClass() : null;
                                logger.warning(format("Unknown inner standby item: %s", clazz));
                            }
                            standbyFallback(messenger, standby);
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles a message callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the message data
     */
    public void onMessage(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a message delivered callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the message delivered data
     */
    public void onMessageDelivered(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a message read callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the message read data
     */
    public void onMessageRead(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a message echo callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the message echo data
     */
    public void onMessageEcho(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a postback callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the postback data
     */
    public void onPostback(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a optin callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the optin data
     */
    public void onOptin(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a referral callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the referral data
     */
    public void onReferral(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a payment callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the payment data
     */
    public void onPayment(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a checkout update callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the checkout update data
     */
    public void onCheckoutUpdate(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles an account linking callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the account linking data
     */
    public void onAccountLinking(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a policy enforcement callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the policy enforcement data
     */
    public void onPolicyEnforcement(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a take thread control callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the take thread control data
     */
    public void onTakeThreadControl(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a pass thread control callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the pass thread control data
     */
    public void onPassThreadControl(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles an app roles callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the app roles data
     */
    public void onAppRoles(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles fallback for unknown callback types.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the callback data
     */
    public void fallback(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a standby message callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the message data
     */
    public void onStandbyMessage(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a standby message delivered callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the message delivered data
     */
    public void onStandbyMessageDelivered(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a standby message read callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the message read data
     */
    public void onStandbyMessageRead(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles a standby message echo callback.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the message echo data
     */
    public void onStandbyMessageEcho(Messenger messenger, MessagingItem messaging) {
    }

    /**
     * Handles fallback for unknown standby callback types.
     *
     * @param messenger the {@code Messenger} instance that retrieved the callback
     * @param messaging the {@code MessagingItem} containing the callback data
     */
    public void standbyFallback(Messenger messenger, MessagingItem messaging) {
    }
}
