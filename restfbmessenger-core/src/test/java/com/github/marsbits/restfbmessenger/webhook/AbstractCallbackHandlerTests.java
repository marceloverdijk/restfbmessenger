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
import com.restfb.types.webhook.messaging.AccountLinkingItem;
import com.restfb.types.webhook.messaging.CheckoutUpdateItem;
import com.restfb.types.webhook.messaging.DeliveryItem;
import com.restfb.types.webhook.messaging.MessageItem;
import com.restfb.types.webhook.messaging.MessagingItem;
import com.restfb.types.webhook.messaging.OptinItem;
import com.restfb.types.webhook.messaging.PaymentItem;
import com.restfb.types.webhook.messaging.PostbackItem;
import com.restfb.types.webhook.messaging.ReadItem;
import com.restfb.types.webhook.messaging.ReferralItem;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests for {@link AbstractCallbackHandler}.
 *
 * @author Marcel Overdijk
 */
public class AbstractCallbackHandlerTests {

    private AbstractCallbackHandler callbackHandler;
    private AbstractCallbackHandler spyCallbackHandler;

    private Messenger messenger;

    @Before
    public void setUp() {
        this.callbackHandler = new AbstractCallbackHandler() {

        };
        this.spyCallbackHandler = spy(callbackHandler);
        this.messenger = mock(Messenger.class);
    }

    @Test
    public void testOnMessage() {
        MessageItem message = new MessageItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setMessage(message);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onMessage(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnMessageDelivered() {
        DeliveryItem delivery = new DeliveryItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setDelivery(delivery);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onMessageDelivered(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnMessageRead() {
        ReadItem read = new ReadItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setRead(read);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onMessageRead(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnMessageEcho() {
        MessageItem message = new MessageItem();
        message.setEcho(true);
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setMessage(message);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onMessageEcho(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnPostback() {
        PostbackItem postback = new PostbackItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setPostback(postback);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onPostback(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnOptin() {
        OptinItem optin = new OptinItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setOptin(optin);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onOptin(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnReferral() {
        ReferralItem referral = new ReferralItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setReferral(referral);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onReferral(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnPayment() {
        PaymentItem payment = new PaymentItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setPayment(payment);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onPayment(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnCheckoutUpdate() {
        CheckoutUpdateItem checkoutUpdate = new CheckoutUpdateItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setCheckoutUpdate(checkoutUpdate);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onCheckoutUpdate(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnAccountLinking() {
        AccountLinkingItem accountLinking = new AccountLinkingItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setAccountLinking(accountLinking);
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onAccountLinking(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testFallback() {
        MessagingItem messagingItem = new MessagingItem();
        WebhookObject webhookObject = createWebhookObject(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).fallback(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testMultipleMessagingItems() {
        MessageItem message1 = new MessageItem();
        MessagingItem messagingItem1 = new MessagingItem();
        messagingItem1.setMessage(message1);
        MessageItem message2 = new MessageItem();
        MessagingItem messagingItem2 = new MessagingItem();
        messagingItem2.setMessage(message2);
        PostbackItem postback = new PostbackItem();
        MessagingItem messagingItem3 = new MessagingItem();
        messagingItem3.setPostback(postback);
        MessagingItem messagingItem4 = new MessagingItem();
        WebhookObject webhookObject = createWebhookObject(messagingItem1, messagingItem2, messagingItem3, messagingItem4);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onMessage(messenger, messagingItem1);
        verify(spyCallbackHandler, times(1)).onMessage(messenger, messagingItem2);
        verify(spyCallbackHandler, times(1)).onPostback(messenger, messagingItem3);
        verify(spyCallbackHandler, times(1)).fallback(messenger, messagingItem4);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    private WebhookObject createWebhookObject(MessagingItem messagingItem) {
        return createWebhookObject(new MessagingItem[] {messagingItem});
    }

    private WebhookObject createWebhookObject(MessagingItem... messagingItems) {
        return createWebhookObject("page", messagingItems);
    }

    private WebhookObject createWebhookObject(String object, MessagingItem... messagingItems) {
        WebhookObject webhookObject = new WebhookObject();
        webhookObject.setObject(object);
        WebhookEntry entry = new WebhookEntry();
        entry.setMessaging(Arrays.asList(messagingItems));
        webhookObject.setEntryList(Arrays.asList(entry));
        return webhookObject;
    }
}
