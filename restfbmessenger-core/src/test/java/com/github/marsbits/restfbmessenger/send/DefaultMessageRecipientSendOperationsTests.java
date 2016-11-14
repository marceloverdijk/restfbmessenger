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

package com.github.marsbits.restfbmessenger.send;

import com.restfb.types.send.ButtonTemplatePayload;
import com.restfb.types.send.GenericTemplatePayload;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.ListTemplatePayload;
import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.Message;
import com.restfb.types.send.MessageRecipient;
import com.restfb.types.send.NotificationTypeEnum;
import com.restfb.types.send.PhoneMessageRecipient;
import com.restfb.types.send.QuickReply;
import com.restfb.types.send.ReceiptTemplatePayload;
import com.restfb.types.send.SenderActionEnum;
import com.restfb.types.send.TemplateAttachment;
import com.restfb.types.send.UserRefMessageRecipient;
import com.restfb.types.send.airline.AirlineBoardingPassTemplatePayload;
import com.restfb.types.send.airline.AirlineCheckinTemplatePayload;
import com.restfb.types.send.airline.AirlineItineraryTemplatePayload;
import com.restfb.types.send.airline.AirlineUpdateTemplatePayload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link DefaultMessageRecipientSendOperations}.
 *
 * @author Marcel Overdijk
 */
@RunWith(Parameterized.class)
public class DefaultMessageRecipientSendOperationsTests extends AbstractSendOperationsTests {

    @Parameterized.Parameters(name = "{0}")
    public static Object[] data() {
        return new Object[] {
                new IdMessageRecipient("1"),
                new PhoneMessageRecipient("+1(212)555-2368"),
                new UserRefMessageRecipient("UNIQUE_REF_PARAM")};
    }

    @Parameterized.Parameter(0)
    public MessageRecipient messageRecipient;

    private DefaultMessageRecipientSendOperations send;

    private SendOperations sendOperations;

    @Before
    public void setUp() {
        this.sendOperations = mock(SendOperations.class);
    }

    @Test
    public void testSenderAction() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(senderAction);
        verify(sendOperations).senderAction(messageRecipient, senderAction, null);
    }

    @Test
    public void testSenderActionWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(senderAction);
        verify(sendOperations).senderAction(messageRecipient, senderAction, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testSenderActionWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(senderAction, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).senderAction(messageRecipient, senderAction, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testSenderActionWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(senderAction, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).senderAction(messageRecipient, senderAction, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testMarkSeen() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        send.markSeen();
        verify(sendOperations).markSeen(messageRecipient, null);
    }

    @Test
    public void testMarkSeenWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        send.markSeen();
        verify(sendOperations).markSeen(messageRecipient, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testMarkSeenWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        send.markSeen(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).markSeen(messageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testMarkSeenWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        send.markSeen(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).markSeen(messageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTypingOn() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        send.typingOn();
        verify(sendOperations).typingOn(messageRecipient, null);
    }

    @Test
    public void testTypingOnWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        send.typingOn();
        verify(sendOperations).typingOn(messageRecipient, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testTypingOnWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        send.typingOn(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).typingOn(messageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTypingOnWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        send.typingOn(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).typingOn(messageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTypingOff() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        send.typingOff();
        verify(sendOperations).typingOff(messageRecipient, null);
    }

    @Test
    public void testTypingOffWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        send.typingOff();
        verify(sendOperations).typingOff(messageRecipient, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testTypingOffWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        send.typingOff(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).typingOff(messageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTypingOffWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        send.typingOff(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).typingOff(messageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testMessage() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        Message message = new Message("Hello!");
        send.message(message);
        verify(sendOperations).message(messageRecipient, message, null);
    }

    @Test
    public void testMessageWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        Message message = new Message("Hello!");
        send.message(message);
        verify(sendOperations).message(messageRecipient, message, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testMessageWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        Message message = new Message("Hello!");
        send.message(message, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).message(messageRecipient, message, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testMessageWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        Message message = new Message("Hello!");
        send.message(message, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).message(messageRecipient, message, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTextMessage() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String text = "Hello!";
        send.textMessage(text);
        verify(sendOperations).textMessage(messageRecipient, text, null);
    }

    @Test
    public void testTextMessageWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String text = "Hello!";
        send.textMessage(text);
        verify(sendOperations).textMessage(messageRecipient, text, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testTextMessageWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String text = "Hello!";
        send.textMessage(text, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).textMessage(messageRecipient, text, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTextMessageWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String text = "Hello!";
        send.textMessage(text, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).textMessage(messageRecipient, text, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAttachment() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(type, url);
        verify(sendOperations).attachment(messageRecipient, type, url, null);
    }

    @Test
    public void testAttachmentWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(type, url);
        verify(sendOperations).attachment(messageRecipient, type, url, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testAttachmentWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(type, url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).attachment(messageRecipient, type, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAttachmentWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(type, url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).attachment(messageRecipient, type, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testImageAttachment() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String url = "http://localhost";
        send.imageAttachment(url);
        verify(sendOperations).imageAttachment(messageRecipient, url, null);
    }

    @Test
    public void testImageAttachmentWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String url = "http://localhost";
        send.imageAttachment(url);
        verify(sendOperations).imageAttachment(messageRecipient, url, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testImageAttachmentWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String url = "http://localhost";
        send.imageAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).imageAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testImageAttachmentWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String url = "http://localhost";
        send.imageAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).imageAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAudioAttachment() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String url = "http://localhost";
        send.audioAttachment(url);
        verify(sendOperations).audioAttachment(messageRecipient, url, null);
    }

    @Test
    public void testAudioAttachmentWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String url = "http://localhost";
        send.audioAttachment(url);
        verify(sendOperations).audioAttachment(messageRecipient, url, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testAudioAttachmentWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String url = "http://localhost";
        send.audioAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).audioAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAudioAttachmentWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String url = "http://localhost";
        send.audioAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).audioAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testVideoAttachment() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String url = "http://localhost";
        send.videoAttachment(url);
        verify(sendOperations).videoAttachment(messageRecipient, url, null);
    }

    @Test
    public void testVideoAttachmentWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String url = "http://localhost";
        send.videoAttachment(url);
        verify(sendOperations).videoAttachment(messageRecipient, url, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testVideoAttachmentWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String url = "http://localhost";
        send.videoAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).videoAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testVideoAttachmentWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String url = "http://localhost";
        send.videoAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).videoAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testFileAttachment() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String url = "http://localhost";
        send.fileAttachment(url);
        verify(sendOperations).fileAttachment(messageRecipient, url, null);
    }

    @Test
    public void testFileAttachmentWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String url = "http://localhost";
        send.fileAttachment(url);
        verify(sendOperations).fileAttachment(messageRecipient, url, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testFileAttachmentWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String url = "http://localhost";
        send.fileAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).fileAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testFileAttachmentWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String url = "http://localhost";
        send.fileAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).fileAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithTextMessage() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(text, quickReplies);
        verify(sendOperations).quickReplies(messageRecipient, text, quickReplies, null);
    }

    @Test
    public void testQuickRepliesWithTextMessageAndImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(text, quickReplies);
        verify(sendOperations).quickReplies(messageRecipient, text, quickReplies, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testQuickRepliesWithTextMessageAndExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(text, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(messageRecipient, text, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithTextMessageAndExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(text, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(messageRecipient, text, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithMediaAttachment() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        MediaAttachment attachment = createMediaAttachment();
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies);
        verify(sendOperations).quickReplies(messageRecipient, attachment, quickReplies, null);
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        MediaAttachment attachment = createMediaAttachment();
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies);
        verify(sendOperations).quickReplies(messageRecipient, attachment, quickReplies, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        MediaAttachment attachment = createMediaAttachment();
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(messageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        MediaAttachment attachment = createMediaAttachment();
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(messageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithTemplateAttachment() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies);
        verify(sendOperations).quickReplies(messageRecipient, attachment, quickReplies, null);
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies);
        verify(sendOperations).quickReplies(messageRecipient, attachment, quickReplies, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(messageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(messageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testButtonTemplate() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(buttonTemplate);
        verify(sendOperations).buttonTemplate(messageRecipient, buttonTemplate, null);
    }

    @Test
    public void testButtonTemplateWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(buttonTemplate);
        verify(sendOperations).buttonTemplate(messageRecipient, buttonTemplate, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testButtonTemplateWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(buttonTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).buttonTemplate(messageRecipient, buttonTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testButtonTemplateWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(buttonTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).buttonTemplate(messageRecipient, buttonTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testGenericTemplate() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(genericTemplate);
        verify(sendOperations).genericTemplate(messageRecipient, genericTemplate, null);
    }

    @Test
    public void testGenericTemplateWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(genericTemplate);
        verify(sendOperations).genericTemplate(messageRecipient, genericTemplate, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testGenericTemplateWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(genericTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).genericTemplate(messageRecipient, genericTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testGenericTemplateWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(genericTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).genericTemplate(messageRecipient, genericTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testListTemplate() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(listTemplate);
        verify(sendOperations).listTemplate(messageRecipient, listTemplate, null);
    }

    @Test
    public void testListTemplateWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(listTemplate);
        verify(sendOperations).listTemplate(messageRecipient, listTemplate, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testListTemplateWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(listTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).listTemplate(messageRecipient, listTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testListTemplateWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(listTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).listTemplate(messageRecipient, listTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testReceiptTemplate() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(receiptTemplate);
        verify(sendOperations).receiptTemplate(messageRecipient, receiptTemplate, null);
    }

    @Test
    public void testReceiptTemplateWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(receiptTemplate);
        verify(sendOperations).receiptTemplate(messageRecipient, receiptTemplate, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testReceiptTemplateWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(receiptTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).receiptTemplate(messageRecipient, receiptTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testReceiptTemplateWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(receiptTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).receiptTemplate(messageRecipient, receiptTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineItineraryTemplate() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(airlineItineraryTemplate);
        verify(sendOperations).airlineItineraryTemplate(messageRecipient, airlineItineraryTemplate, null);
    }

    @Test
    public void testAirlineItineraryTemplateWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(airlineItineraryTemplate);
        verify(sendOperations).airlineItineraryTemplate(messageRecipient, airlineItineraryTemplate, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testAirlineItineraryTemplateWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineItineraryTemplate(messageRecipient, airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineItineraryTemplateWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineItineraryTemplate(messageRecipient, airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineCheckinTemplate() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(airlineCheckinTemplate);
        verify(sendOperations).airlineCheckinTemplate(messageRecipient, airlineCheckinTemplate, null);
    }

    @Test
    public void testAirlineCheckinTemplateWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(airlineCheckinTemplate);
        verify(sendOperations).airlineCheckinTemplate(messageRecipient, airlineCheckinTemplate, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testAirlineCheckinTemplateWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineCheckinTemplate(messageRecipient, airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineCheckinTemplateWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineCheckinTemplate(messageRecipient, airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineBoardingPassTemplate() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate = createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(airlineBoardingPassTemplate);
        verify(sendOperations).airlineBoardingPassTemplate(messageRecipient, airlineBoardingPassTemplate, null);
    }

    @Test
    public void testAirlineBoardingPassTemplateWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate = createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(airlineBoardingPassTemplate);
        verify(sendOperations).airlineBoardingPassTemplate(messageRecipient, airlineBoardingPassTemplate, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testAirlineBoardingPassTemplateWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineBoardingPassTemplate(messageRecipient, airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineBoardingPassTemplateWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate = createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineBoardingPassTemplate(messageRecipient, airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineUpdateTemplate() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(airlineUpdateTemplate);
        verify(sendOperations).airlineUpdateTemplate(messageRecipient, airlineUpdateTemplate, null);
    }

    @Test
    public void testAirlineUpdateTemplateWithImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(airlineUpdateTemplate);
        verify(sendOperations).airlineUpdateTemplate(messageRecipient, airlineUpdateTemplate, NotificationTypeEnum.SILENT_PUSH);
    }

    @Test
    public void testAirlineUpdateTemplateWithExplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient);
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineUpdateTemplate(messageRecipient, airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineUpdateTemplateWithExplicitNotificationTypeOverrulesImplicitNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, messageRecipient, NotificationTypeEnum.SILENT_PUSH);
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineUpdateTemplate(messageRecipient, airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
    }
}
