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
import com.restfb.types.webhook.messaging.AppRoles;
import com.restfb.types.webhook.messaging.CheckoutUpdateItem;
import com.restfb.types.webhook.messaging.DeliveryItem;
import com.restfb.types.webhook.messaging.MessageItem;
import com.restfb.types.webhook.messaging.MessagingItem;
import com.restfb.types.webhook.messaging.OptinItem;
import com.restfb.types.webhook.messaging.PassThreadControlItem;
import com.restfb.types.webhook.messaging.PaymentItem;
import com.restfb.types.webhook.messaging.PolicyEnforcementItem;
import com.restfb.types.webhook.messaging.PostbackItem;
import com.restfb.types.webhook.messaging.ReadItem;
import com.restfb.types.webhook.messaging.ReferralItem;
import com.restfb.types.webhook.messaging.TakeThreadControlItem;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onMessage(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnMessageDelivered() {
        DeliveryItem delivery = new DeliveryItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setDelivery(delivery);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onMessageDelivered(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnMessageRead() {
        ReadItem read = new ReadItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setRead(read);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
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
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onMessageEcho(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnPostback() {
        PostbackItem postback = new PostbackItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setPostback(postback);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onPostback(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnOptin() {
        OptinItem optin = new OptinItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setOptin(optin);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onOptin(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnReferral() {
        ReferralItem referral = new ReferralItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setReferral(referral);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onReferral(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnPayment() {
        PaymentItem payment = new PaymentItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setPayment(payment);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onPayment(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnCheckoutUpdate() {
        CheckoutUpdateItem checkoutUpdate = new CheckoutUpdateItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setCheckoutUpdate(checkoutUpdate);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onCheckoutUpdate(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnAccountLinking() {
        AccountLinkingItem accountLinking = new AccountLinkingItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setAccountLinking(accountLinking);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onAccountLinking(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnPolicyEnforcement() {
        PolicyEnforcementItem policyEnforcement = new PolicyEnforcementItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setPolicyEnforcement(policyEnforcement);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onPolicyEnforcement(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnTakeThreadControl() {
        TakeThreadControlItem takeThreadControl = new TakeThreadControlItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setTakeThreadControl(takeThreadControl);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onTakeThreadControl(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnPassThreadControl() {
        PassThreadControlItem passThreadControl = new PassThreadControlItem();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setPassThreadControl(passThreadControl);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onPassThreadControl(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnAppRoles() {
        AppRoles appRoles = new AppRoles();
        MessagingItem messagingItem = new MessagingItem();
        messagingItem.setAppRoles(appRoles);
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onAppRoles(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testFallback() {
        MessagingItem messagingItem = new MessagingItem();
        WebhookObject webhookObject = createWebhookObjectWithMessagingItem(messagingItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).fallback(messenger, messagingItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnStandbyMessage() {
        MessageItem message = new MessageItem();
        MessagingItem standbyItem = new MessagingItem();
        standbyItem.setMessage(message);
        WebhookObject webhookObject = createWebhookObjectWithStandbyItem(standbyItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onStandbyMessage(messenger, standbyItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnStandbyMessageDelivered() {
        DeliveryItem delivery = new DeliveryItem();
        MessagingItem standbyItem = new MessagingItem();
        standbyItem.setDelivery(delivery);
        WebhookObject webhookObject = createWebhookObjectWithStandbyItem(standbyItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onStandbyMessageDelivered(messenger, standbyItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnStandbyMessageRead() {
        ReadItem read = new ReadItem();
        MessagingItem standbyItem = new MessagingItem();
        standbyItem.setRead(read);
        WebhookObject webhookObject = createWebhookObjectWithStandbyItem(standbyItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onStandbyMessageRead(messenger, standbyItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testOnStandbyMessageEcho() {
        MessageItem message = new MessageItem();
        message.setEcho(true);
        MessagingItem standbyItem = new MessagingItem();
        standbyItem.setMessage(message);
        WebhookObject webhookObject = createWebhookObjectWithStandbyItem(standbyItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onStandbyMessageEcho(messenger, standbyItem);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    @Test
    public void testStandbyFallback() {
        MessagingItem standbyItem = new MessagingItem();
        WebhookObject webhookObject = createWebhookObjectWithStandbyItem(standbyItem);
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).standbyFallback(messenger, standbyItem);
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
        MessageItem standby1 = new MessageItem();
        MessagingItem standbyItem1 = new MessagingItem();
        standbyItem1.setMessage(standby1);
        MessageItem standby2 = new MessageItem();
        MessagingItem standbyItem2 = new MessagingItem();
        standbyItem2.setMessage(standby2);
        MessagingItem standbyItem3 = new MessagingItem();
        WebhookObject webhookObject = createWebhookObject(
                Arrays.asList(messagingItem1, messagingItem2, messagingItem3, messagingItem4),
                Arrays.asList(standbyItem1, standbyItem2, standbyItem3));
        spyCallbackHandler.onCallback(messenger, webhookObject);
        verify(spyCallbackHandler, times(1)).onMessage(messenger, messagingItem1);
        verify(spyCallbackHandler, times(1)).onMessage(messenger, messagingItem2);
        verify(spyCallbackHandler, times(1)).onPostback(messenger, messagingItem3);
        verify(spyCallbackHandler, times(1)).fallback(messenger, messagingItem4);
        verify(spyCallbackHandler, times(1)).onStandbyMessage(messenger, standbyItem1);
        verify(spyCallbackHandler, times(1)).onStandbyMessage(messenger, standbyItem2);
        verify(spyCallbackHandler, times(1)).standbyFallback(messenger, standbyItem3);
        verifyNoMoreInteractions(spyCallbackHandler);
    }

    private WebhookObject createWebhookObjectWithMessagingItem(MessagingItem messagingItem) {
        return createWebhookObject(Arrays.asList(messagingItem), null);
    }

    private WebhookObject createWebhookObjectWithStandbyItem(MessagingItem standbyItem) {
        return createWebhookObject(null, Arrays.asList(standbyItem));
    }

    private WebhookObject createWebhookObject(List<MessagingItem> messagingItems, List<MessagingItem> standbyItems) {
        return createWebhookObject("page", messagingItems, standbyItems);
    }

    private WebhookObject createWebhookObject(String object, List<MessagingItem> messagingItems, List<MessagingItem> standbyItems) {
        WebhookObject webhookObject = new WebhookObject();
        webhookObject.setObject("page");
        WebhookEntry entry = new WebhookEntry();
        entry.setMessaging(messagingItems);
        entry.setStandby(standbyItems);
        webhookObject.setEntryList(Arrays.asList(entry));
        return webhookObject;
    }
}
