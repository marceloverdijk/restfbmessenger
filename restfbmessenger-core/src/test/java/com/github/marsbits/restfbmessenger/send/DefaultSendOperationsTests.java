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

import com.restfb.FacebookClient;
import com.restfb.Parameter;
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

import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.MESSAGE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.NOTIFICATION_TYPE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.SENDER_ACTION_PARAM_NAME;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DefaultSendOperations}.
 *
 * @author Marcel Overdijk
 */
@SuppressWarnings("Duplicates")
public class DefaultSendOperationsTests extends AbstractSendOperationsTests {

    private DefaultSendOperations send;

    @Before
    public void setUp() {
        this.facebookClient = mock(FacebookClient.class);
        this.send = new DefaultSendOperations(facebookClient);
        this.idMessageRecipient = new IdMessageRecipient("1");
        this.phoneMessageRecipient = new PhoneMessageRecipient("+1(212)555-2368");
    }

    @Test
    public void testSenderActionWithIdMessageRecipient() {
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(idMessageRecipient, senderAction);
        verifySend(idMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction));
    }

    @Test
    public void testSenderActionWithIdMessageRecipientAndNotificationType() {
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(idMessageRecipient, senderAction, NotificationTypeEnum.NO_PUSH);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction));
    }

    @Test
    public void testSenderActionWithPhoneMessageRecipient() {
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(phoneMessageRecipient, senderAction);
        verifySend(phoneMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction));
    }

    @Test
    public void testSenderActionWithPhoneMessageRecipientAndNotificationType() {
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(phoneMessageRecipient, senderAction, NotificationTypeEnum.NO_PUSH);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction));
    }

    @Test
    public void testMarkSeenWithIdMessageRecipient() {
        send.markSeen(idMessageRecipient);
        verifySend(idMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.mark_seen));
    }

    @Test
    public void testMarkSeenWithIdMessageRecipientAndNotificationType() {
        send.markSeen(idMessageRecipient, NotificationTypeEnum.NO_PUSH);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.mark_seen));
    }

    @Test
    public void testMarkSeenWithPhoneMessageRecipient() {
        send.markSeen(phoneMessageRecipient);
        verifySend(phoneMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.mark_seen));
    }

    @Test
    public void testMarkSeenWithPhoneMessageRecipientAndNotificationType() {
        send.markSeen(phoneMessageRecipient, NotificationTypeEnum.NO_PUSH);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.mark_seen));
    }

    @Test
    public void testTypingOnWithIdMessageRecipient() {
        send.typingOn(idMessageRecipient);
        verifySend(idMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_on));
    }

    @Test
    public void testTypingOnWithIdMessageRecipientAndNotificationType() {
        send.typingOn(idMessageRecipient, NotificationTypeEnum.NO_PUSH);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_on));
    }

    @Test
    public void testTypingOnWithPhoneMessageRecipient() {
        send.typingOn(phoneMessageRecipient);
        verifySend(phoneMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_on));
    }

    @Test
    public void testTypingOnWithPhoneMessageRecipientAndNotificationType() {
        send.typingOn(phoneMessageRecipient, NotificationTypeEnum.NO_PUSH);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_on));
    }

    @Test
    public void testTypingOffWithIdMessageRecipient() {
        send.typingOff(idMessageRecipient);
        verifySend(idMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_off));
    }

    @Test
    public void testTypingOffWithIdMessageRecipientAndNotificationType() {
        send.typingOff(idMessageRecipient, NotificationTypeEnum.NO_PUSH);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_off));
    }

    @Test
    public void testTypingOffWithPhoneMessageRecipient() {
        send.typingOff(phoneMessageRecipient);
        verifySend(phoneMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_off));
    }

    @Test
    public void testTypingOffWithPhoneMessageRecipientAndNotificationType() {
        send.typingOff(phoneMessageRecipient, NotificationTypeEnum.NO_PUSH);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_off));
    }

    @Test
    public void testMessageWithIdMessageRecipient() {
        Message message = new Message("Hello!");
        send.message(idMessageRecipient, message);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testMessageWithIdMessageRecipientAndNotificationType() {
        Message message = new Message("Hello!");
        send.message(idMessageRecipient, message, NotificationTypeEnum.NO_PUSH);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testMessageWithPhoneMessageRecipient() {
        Message message = new Message("Hello!");
        send.message(phoneMessageRecipient, message);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testMessageWithPhoneMessageRecipientAndNotificationType() {
        Message message = new Message("Hello!");
        send.message(phoneMessageRecipient, message, NotificationTypeEnum.NO_PUSH);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWithIdMessageRecipient() {
        String text = "Hello!";
        send.textMessage(idMessageRecipient, text);
        Message message = new Message(text);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWithIdMessageRecipientAndNotificationType() {
        String text = "Hello!";
        send.textMessage(idMessageRecipient, text, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(text);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWithPhoneMessageRecipient() {
        String text = "Hello!";
        send.textMessage(phoneMessageRecipient, text);
        Message message = new Message(text);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWithPhoneMessageRecipientAndNotificationType() {
        String text = "Hello!";
        send.textMessage(phoneMessageRecipient, text, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(text);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAttachmentWithIdMessageRecipient() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(idMessageRecipient, MediaAttachment.Type.IMAGE, url);
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAttachmentWithIdMessageRecipientAndNotificationType() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(idMessageRecipient, MediaAttachment.Type.IMAGE, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAttachmentWithPhoneMessageRecipient() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(phoneMessageRecipient, type, url);
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAttachmentWithPhoneMessageRecipientAndNotificationType() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(phoneMessageRecipient, type, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testImageAttachmentWithIdMessageRecipient() {
        String url = "http://localhost";
        send.imageAttachment(idMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.IMAGE, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testImageAttachmentWithIdMessageRecipientAndNotificationType() {
        String url = "http://localhost";
        send.imageAttachment(idMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.IMAGE, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testImageAttachmentWithPhoneMessageRecipient() {
        String url = "http://localhost";
        send.imageAttachment(phoneMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.IMAGE, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testImageAttachmentWithPhoneMessageRecipientAndNotificationType() {
        String url = "http://localhost";
        send.imageAttachment(phoneMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.IMAGE, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAudioAttachmentWithIdMessageRecipient() {
        String url = "http://localhost";
        send.audioAttachment(idMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.AUDIO, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAudioAttachmentWithIdMessageRecipientAndNotificationType() {
        String url = "http://localhost";
        send.audioAttachment(idMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.AUDIO, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAudioAttachmentWithPhoneMessageRecipient() {
        String url = "http://localhost";
        send.audioAttachment(phoneMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.AUDIO, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAudioAttachmentWithPhoneMessageRecipientAndNotificationType() {
        String url = "http://localhost";
        send.audioAttachment(phoneMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.AUDIO, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testVideoAttachmentWithIdMessageRecipient() {
        String url = "http://localhost";
        send.videoAttachment(idMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.VIDEO, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testVideoAttachmentWithIdMessageRecipientAndNotificationType() {
        String url = "http://localhost";
        send.videoAttachment(idMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.VIDEO, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testVideoAttachmentWithPhoneMessageRecipient() {
        String url = "http://localhost";
        send.videoAttachment(phoneMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.VIDEO, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testVideoAttachmentWithPhoneMessageRecipientAndNotificationType() {
        String url = "http://localhost";
        send.videoAttachment(phoneMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.VIDEO, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testFileAttachmentWithIdMessageRecipient() {
        String url = "http://localhost";
        send.fileAttachment(idMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.FILE, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testFileAttachmentWithIdMessageRecipientAndNotificationType() {
        String url = "http://localhost";
        send.fileAttachment(idMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.FILE, url);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testFileAttachmentWithPhoneMessageRecipient() {
        String url = "http://localhost";
        send.fileAttachment(phoneMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.FILE, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testFileAttachmentWithPhoneMessageRecipientAndNotificationType() {
        String url = "http://localhost";
        send.fileAttachment(phoneMessageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.FILE, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTextMessageAndIdMessageRecipient() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(idMessageRecipient, text, quickReplies);
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTextMessageAndIdMessageRecipientAndNotificationType() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(idMessageRecipient, text, quickReplies, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTextMessageAndPhoneMessageRecipient() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(phoneMessageRecipient, text, quickReplies);
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTextMessageAndPhoneMessageRecipientAndNotificationType() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(phoneMessageRecipient, text, quickReplies, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndIdMessageRecipient() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        MediaAttachment attachment = new MediaAttachment(type, url);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(idMessageRecipient, attachment, quickReplies);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndIdMessageRecipientAndNotificationType() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        MediaAttachment attachment = new MediaAttachment(type, url);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(idMessageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndPhoneMessageRecipient() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        MediaAttachment attachment = new MediaAttachment(type, url);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(phoneMessageRecipient, attachment, quickReplies);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndPhoneMessageRecipientAndNotificationType() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        MediaAttachment attachment = new MediaAttachment(type, url);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(phoneMessageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndIdMessageRecipient() {
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(idMessageRecipient, attachment, quickReplies);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndIdMessageRecipientAndNotificationType() {
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(idMessageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndPhoneMessageRecipient() {
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(phoneMessageRecipient, attachment, quickReplies);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndPhoneMessageRecipientAndNotificationType() {
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(phoneMessageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testButtonTemplateWithIdMessageRecipient() {
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(idMessageRecipient, buttonTemplate);
        TemplateAttachment attachment = new TemplateAttachment(buttonTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testButtonTemplateWithIdMessageRecipientAndNotificationType() {
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(idMessageRecipient, buttonTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(buttonTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testButtonTemplateWithPhoneMessageRecipient() {
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(phoneMessageRecipient, buttonTemplate);
        TemplateAttachment attachment = new TemplateAttachment(buttonTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testButtonTemplateWithPhoneMessageRecipientAndNotificationType() {
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(phoneMessageRecipient, buttonTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(buttonTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testGenericTemplateWithIdMessageRecipient() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(idMessageRecipient, genericTemplate);
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testGenericTemplateWithIdMessageRecipientAndNotificationType() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(idMessageRecipient, genericTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testGenericTemplateWithPhoneMessageRecipient() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(phoneMessageRecipient, genericTemplate);
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testGenericTemplateWithPhoneMessageRecipientAndNotificationType() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(phoneMessageRecipient, genericTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testListTemplateWithIdMessageRecipient() {
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(idMessageRecipient, listTemplate);
        TemplateAttachment attachment = new TemplateAttachment(listTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testListTemplateWithIdMessageRecipientAndNotificationType() {
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(idMessageRecipient, listTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(listTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testListTemplateWithPhoneMessageRecipient() {
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(phoneMessageRecipient, listTemplate);
        TemplateAttachment attachment = new TemplateAttachment(listTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testListTemplateWithPhoneMessageRecipientAndNotificationType() {
        ListTemplatePayload listTemplate = createListTemplate();
        send.listTemplate(phoneMessageRecipient, listTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(listTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testReceiptTemplateWithIdMessageRecipient() {
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(idMessageRecipient, receiptTemplate);
        TemplateAttachment attachment = new TemplateAttachment(receiptTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testReceiptTemplateWithIdMessageRecipientAndNotificationType() {
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(idMessageRecipient, receiptTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(receiptTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testReceiptTemplateWithPhoneMessageRecipient() {
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(phoneMessageRecipient, receiptTemplate);
        TemplateAttachment attachment = new TemplateAttachment(receiptTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testReceiptTemplateWithPhoneMessageRecipientAndNotificationType() {
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(phoneMessageRecipient, receiptTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(receiptTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineItineraryTemplateWithIdMessageRecipient() {
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(idMessageRecipient, airlineItineraryTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineItineraryTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineItineraryTemplateWithIdMessageRecipientAndNotificationType() {
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(idMessageRecipient, airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineItineraryTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineItineraryTemplateWithPhoneMessageRecipient() {
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(phoneMessageRecipient, airlineItineraryTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineItineraryTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineItineraryTemplateWithPhoneMessageRecipientAndNotificationType() {
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(phoneMessageRecipient, airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineItineraryTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineCheckinTemplateWithIdMessageRecipient() {
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(idMessageRecipient, airlineCheckinTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineCheckinTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineCheckinTemplateWithIdMessageRecipientAndNotificationType() {
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(idMessageRecipient, airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineCheckinTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineCheckinTemplateWithPhoneMessageRecipient() {
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(phoneMessageRecipient, airlineCheckinTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineCheckinTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineCheckinTemplateWithPhoneMessageRecipientAndNotificationType() {
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(phoneMessageRecipient, airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineCheckinTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineBoardingPassTemplateWithIdMessageRecipient() {
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(idMessageRecipient, airlineBoardingPassTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineBoardingPassTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineBoardingPassTemplateWithIdMessageRecipientAndNotificationType() {
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(idMessageRecipient, airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineBoardingPassTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineBoardingPassTemplateWithPhoneMessageRecipient() {
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(phoneMessageRecipient, airlineBoardingPassTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineBoardingPassTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineBoardingPassTemplateWithPhoneMessageRecipientAndNotificationType() {
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(phoneMessageRecipient, airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineBoardingPassTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineUpdateTemplateWithIdMessageRecipient() {
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(idMessageRecipient, airlineUpdateTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineUpdateTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineUpdateTemplateWithIdMessageRecipientAndNotificationType() {
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(idMessageRecipient, airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineUpdateTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineUpdateTemplateWithPhoneMessageRecipient() {
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(phoneMessageRecipient, airlineUpdateTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineUpdateTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineUpdateTemplateWithPhoneMessageRecipientAndNotificationType() {
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(phoneMessageRecipient, airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineUpdateTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }
}
