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
import com.restfb.types.send.NotificationTypeEnum;
import com.restfb.types.send.PhoneMessageRecipient;
import com.restfb.types.send.QuickReply;
import com.restfb.types.send.ReceiptTemplatePayload;
import com.restfb.types.send.SenderActionEnum;
import com.restfb.types.send.TemplateAttachment;
import com.restfb.types.send.airline.AirlineBoardingPassTemplatePayload;
import com.restfb.types.send.airline.AirlineCheckinTemplatePayload;
import com.restfb.types.send.airline.AirlineItineraryTemplatePayload;
import com.restfb.types.send.airline.AirlineUpdateTemplatePayload;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link DefaultMessageRecipientSendOperations}.
 *
 * @author Marcel Overdijk
 */
@SuppressWarnings("Duplicates")
public class DefaultMessageRecipientSendOperationsTests extends AbstractSendOperationsTests {

    private DefaultMessageRecipientSendOperations send;

    private SendOperations sendOperations;

    @Before
    public void setUp() {
        this.sendOperations = mock(SendOperations.class);
        this.idMessageRecipient = new IdMessageRecipient("1");
        this.phoneMessageRecipient = new PhoneMessageRecipient("+1(212)555-2368");
    }

    @Test
    public void testSenderActionWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(senderAction);
        verify(sendOperations).senderAction(idMessageRecipient, senderAction, null);
    }

    @Test
    public void testSenderActionWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(senderAction, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).senderAction(idMessageRecipient, senderAction, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testSenderActionWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(senderAction);
        verify(sendOperations).senderAction(phoneMessageRecipient, senderAction, null);
    }

    @Test
    public void testSenderActionWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(senderAction, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).senderAction(phoneMessageRecipient, senderAction, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testMarkSeenWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        send.markSeen();
        verify(sendOperations).markSeen(idMessageRecipient, null);
    }

    @Test
    public void testMarkSeenWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        send.markSeen(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).markSeen(idMessageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testMarkSeenWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        send.markSeen();
        verify(sendOperations).markSeen(phoneMessageRecipient, null);
    }

    @Test
    public void testMarkSeenWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        send.markSeen(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).markSeen(phoneMessageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTypingOnWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        send.typingOn();
        verify(sendOperations).typingOn(idMessageRecipient, null);
    }

    @Test
    public void testTypingOnWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        send.typingOn(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).typingOn(idMessageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTypingOnWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        send.typingOn();
        verify(sendOperations).typingOn(phoneMessageRecipient, null);
    }

    @Test
    public void testTypingOnWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        send.typingOn(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).typingOn(phoneMessageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTypingOffWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        send.typingOff();
        verify(sendOperations).typingOff(idMessageRecipient, null);
    }

    @Test
    public void testTypingOffWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        send.typingOff(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).typingOff(idMessageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTypingOffWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        send.typingOff();
        verify(sendOperations).typingOff(phoneMessageRecipient, null);
    }

    @Test
    public void testTypingOffWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        send.typingOff(NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).typingOff(phoneMessageRecipient, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testMessageWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        Message message = new Message("Hello!");
        send.message(message);
        verify(sendOperations).message(idMessageRecipient, message, null);
    }

    @Test
    public void testMessageWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        Message message = new Message("Hello!");
        send.message(message, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).message(idMessageRecipient, message, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testMessageWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        Message message = new Message("Hello!");
        send.message(message);
        verify(sendOperations).message(phoneMessageRecipient, message, null);
    }

    @Test
    public void testMessageWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        Message message = new Message("Hello!");
        send.message(message, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).message(phoneMessageRecipient, message, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTextMessageWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String text = "Hello!";
        send.textMessage(text);
        verify(sendOperations).textMessage(idMessageRecipient, text, null);
    }

    @Test
    public void testTextMessageWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String text = "Hello!";
        send.textMessage(text, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).textMessage(idMessageRecipient, text, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testTextMessageWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String text = "Hello!";
        send.textMessage(text);
        verify(sendOperations).textMessage(phoneMessageRecipient, text, null);
    }

    @Test
    public void testTextMessageWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String text = "Hello!";
        send.textMessage(text, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).textMessage(phoneMessageRecipient, text, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAttachmentWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(type, url);
        verify(sendOperations).attachment(idMessageRecipient, type, url, null);
    }

    @Test
    public void testAttachmentWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(type, url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).attachment(idMessageRecipient, type, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAttachmentWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(type, url);
        verify(sendOperations).attachment(phoneMessageRecipient, type, url, null);
    }

    @Test
    public void testAttachmentWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(type, url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).attachment(phoneMessageRecipient, type, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testImageAttachmentWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String url = "http://localhost";
        send.imageAttachment(url);
        verify(sendOperations).imageAttachment(idMessageRecipient, url, null);
    }

    @Test
    public void testImageAttachmentWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String url = "http://localhost";
        send.imageAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).imageAttachment(idMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testImageAttachmentWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String url = "http://localhost";
        send.imageAttachment(url);
        verify(sendOperations).imageAttachment(phoneMessageRecipient, url, null);
    }

    @Test
    public void testImageAttachmentWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String url = "http://localhost";
        send.imageAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).imageAttachment(phoneMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAudioAttachmentWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String url = "http://localhost";
        send.audioAttachment(url);
        verify(sendOperations).audioAttachment(idMessageRecipient, url, null);
    }

    @Test
    public void testAudioAttachmentWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String url = "http://localhost";
        send.audioAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).audioAttachment(idMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAudioAttachmentWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String url = "http://localhost";
        send.audioAttachment(url);
        verify(sendOperations).audioAttachment(phoneMessageRecipient, url, null);
    }

    @Test
    public void testAudioAttachmentWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String url = "http://localhost";
        send.audioAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).audioAttachment(phoneMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testVideoAttachmentWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String url = "http://localhost";
        send.videoAttachment(url);
        verify(sendOperations).videoAttachment(idMessageRecipient, url, null);
    }

    @Test
    public void testVideoAttachmentWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String url = "http://localhost";
        send.videoAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).videoAttachment(idMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testVideoAttachmentWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String url = "http://localhost";
        send.videoAttachment(url);
        verify(sendOperations).videoAttachment(phoneMessageRecipient, url, null);
    }

    @Test
    public void testVideoAttachmentWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String url = "http://localhost";
        send.videoAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).videoAttachment(phoneMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testFileAttachmentWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String url = "http://localhost";
        send.fileAttachment(url);
        verify(sendOperations).fileAttachment(idMessageRecipient, url, null);
    }

    @Test
    public void testFileAttachmentWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String url = "http://localhost";
        send.fileAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).fileAttachment(idMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testFileAttachmentWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String url = "http://localhost";
        send.fileAttachment(url);
        verify(sendOperations).fileAttachment(phoneMessageRecipient, url, null);
    }

    @Test
    public void testFileAttachmentWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String url = "http://localhost";
        send.fileAttachment(url, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).fileAttachment(phoneMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithTextMessageAndIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(text, quickReplies);
        verify(sendOperations).quickReplies(idMessageRecipient, text, quickReplies, null);
    }

    @Test
    public void testQuickRepliesWithTextMessageAndIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(text, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(idMessageRecipient, text, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithTextMessageAndPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(text, quickReplies);
        verify(sendOperations).quickReplies(phoneMessageRecipient, text, quickReplies, null);
    }

    @Test
    public void testQuickRepliesWithTextMessageAndPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(text, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(phoneMessageRecipient, text, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        MediaAttachment attachment = new MediaAttachment(type, url);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies);
        verify(sendOperations).quickReplies(idMessageRecipient, attachment, quickReplies, null);
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        MediaAttachment attachment = new MediaAttachment(type, url);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(idMessageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        MediaAttachment attachment = new MediaAttachment(type, url);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies);
        verify(sendOperations).quickReplies(phoneMessageRecipient, attachment, quickReplies, null);
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        MediaAttachment attachment = new MediaAttachment(type, url);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(phoneMessageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies);
        verify(sendOperations).quickReplies(idMessageRecipient, attachment, quickReplies, null);
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(idMessageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies);
        verify(sendOperations).quickReplies(phoneMessageRecipient, attachment, quickReplies, null);
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).quickReplies(phoneMessageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testButtonTemplateWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(buttonTemplate);
        verify(sendOperations).buttonTemplate(idMessageRecipient, buttonTemplate, null);
    }

    @Test
    public void testButtonTemplateWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(buttonTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).buttonTemplate(idMessageRecipient, buttonTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testButtonTemplateWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(buttonTemplate);
        verify(sendOperations).buttonTemplate(phoneMessageRecipient, buttonTemplate, null);
    }

    @Test
    public void testButtonTemplateWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(buttonTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).buttonTemplate(phoneMessageRecipient, buttonTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testGenericTemplateWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(genericTemplate);
        verify(sendOperations).genericTemplate(idMessageRecipient, genericTemplate, null);
    }

    @Test
    public void testGenericTemplateWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(genericTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).genericTemplate(idMessageRecipient, genericTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testGenericTemplateWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(genericTemplate);
        verify(sendOperations).genericTemplate(phoneMessageRecipient, genericTemplate, null);
    }

    @Test
    public void testGenericTemplateWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(genericTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).genericTemplate(phoneMessageRecipient, genericTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testListTemplateWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(listTemplate);
        verify(sendOperations).listTemplate(idMessageRecipient, listTemplate, null);
    }

    @Test
    public void testListTemplateWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(listTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).listTemplate(idMessageRecipient, listTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testListTemplateWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(listTemplate);
        verify(sendOperations).listTemplate(phoneMessageRecipient, listTemplate, null);
    }

    @Test
    public void testListTemplateWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(listTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).listTemplate(phoneMessageRecipient, listTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testReceiptTemplateWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(receiptTemplate);
        verify(sendOperations).receiptTemplate(idMessageRecipient, receiptTemplate, null);
    }

    @Test
    public void testReceiptTemplateWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(receiptTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).receiptTemplate(idMessageRecipient, receiptTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testReceiptTemplateWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(receiptTemplate);
        verify(sendOperations).receiptTemplate(phoneMessageRecipient, receiptTemplate, null);
    }

    @Test
    public void testReceiptTemplateWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(receiptTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).receiptTemplate(phoneMessageRecipient, receiptTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineItineraryTemplateWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(airlineItineraryTemplate);
        verify(sendOperations).airlineItineraryTemplate(idMessageRecipient, airlineItineraryTemplate, null);
    }

    @Test
    public void testAirlineItineraryTemplateWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineItineraryTemplate(idMessageRecipient, airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineItineraryTemplateWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(airlineItineraryTemplate);
        verify(sendOperations).airlineItineraryTemplate(phoneMessageRecipient, airlineItineraryTemplate, null);
    }

    @Test
    public void testAirlineItineraryTemplateWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineItineraryTemplate(phoneMessageRecipient, airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineCheckinTemplateWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(airlineCheckinTemplate);
        verify(sendOperations).airlineCheckinTemplate(idMessageRecipient, airlineCheckinTemplate, null);
    }

    @Test
    public void testAirlineCheckinTemplateWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineCheckinTemplate(idMessageRecipient, airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineCheckinTemplateWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(airlineCheckinTemplate);
        verify(sendOperations).airlineCheckinTemplate(phoneMessageRecipient, airlineCheckinTemplate, null);
    }

    @Test
    public void testAirlineCheckinTemplateWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineCheckinTemplate(phoneMessageRecipient, airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineBoardingPassTemplateWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(airlineBoardingPassTemplate);
        verify(sendOperations).airlineBoardingPassTemplate(idMessageRecipient, airlineBoardingPassTemplate, null);
    }

    @Test
    public void testAirlineBoardingPassTemplateWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineBoardingPassTemplate(idMessageRecipient, airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineBoardingPassTemplateWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(airlineBoardingPassTemplate);
        verify(sendOperations).airlineBoardingPassTemplate(phoneMessageRecipient, airlineBoardingPassTemplate, null);
    }

    @Test
    public void testAirlineBoardingPassTemplateWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations)
                .airlineBoardingPassTemplate(phoneMessageRecipient, airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineUpdateTemplateWithIdMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(airlineUpdateTemplate);
        verify(sendOperations).airlineUpdateTemplate(idMessageRecipient, airlineUpdateTemplate, null);
    }

    @Test
    public void testAirlineUpdateTemplateWithIdMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, idMessageRecipient);
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineUpdateTemplate(idMessageRecipient, airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
    }

    @Test
    public void testAirlineUpdateTemplateWithPhoneMessageRecipient() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(airlineUpdateTemplate);
        verify(sendOperations).airlineUpdateTemplate(phoneMessageRecipient, airlineUpdateTemplate, null);
    }

    @Test
    public void testAirlineUpdateTemplateWithPhoneMessageRecipientAndNotificationType() {
        send = new DefaultMessageRecipientSendOperations(sendOperations, phoneMessageRecipient);
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
        verify(sendOperations).airlineUpdateTemplate(phoneMessageRecipient, airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
    }
}
