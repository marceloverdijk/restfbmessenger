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

package com.github.marsbits.restfbmessenger.send;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.send.Bubble;
import com.restfb.types.send.ButtonTemplatePayload;
import com.restfb.types.send.CallButton;
import com.restfb.types.send.GenericTemplatePayload;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.ListTemplatePayload;
import com.restfb.types.send.ListViewElement;
import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.Message;
import com.restfb.types.send.MessageRecipient;
import com.restfb.types.send.NotificationTypeEnum;
import com.restfb.types.send.PhoneMessageRecipient;
import com.restfb.types.send.PostbackButton;
import com.restfb.types.send.QuickReply;
import com.restfb.types.send.ReceiptSummary;
import com.restfb.types.send.ReceiptTemplatePayload;
import com.restfb.types.send.SendResponse;
import com.restfb.types.send.SenderActionEnum;
import com.restfb.types.send.TemplateAttachment;
import com.restfb.types.send.UserRefMessageRecipient;
import com.restfb.types.send.airline.AirlineBoardingPassTemplatePayload;
import com.restfb.types.send.airline.AirlineCheckinTemplatePayload;
import com.restfb.types.send.airline.AirlineItineraryTemplatePayload;
import com.restfb.types.send.airline.AirlineUpdateTemplatePayload;
import com.restfb.types.send.airline.BoardingPass;
import com.restfb.types.send.airline.FlightAirport;
import com.restfb.types.send.airline.FlightInfo;
import com.restfb.types.send.airline.FlightSchedule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.MESSAGES_PATH;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.MESSAGE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.NOTIFICATION_TYPE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.RECIPIENT_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.SENDER_ACTION_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.TAG_PARAM_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link DefaultSendOperations}.
 *
 * @author Marcel Overdijk
 */
@RunWith(Parameterized.class)
public class DefaultSendOperationsTests {

    @Parameterized.Parameters(name = "{0}")
    public static Object[] data() {
        return new Object[] {
                new IdMessageRecipient("1"),
                new PhoneMessageRecipient("+1(212)555-2368"),
                new UserRefMessageRecipient("UNIQUE_REF_PARAM")};
    }

    @Parameterized.Parameter(0)
    public MessageRecipient messageRecipient;

    private FacebookClient facebookClient;
    private DefaultSendOperations sendOperations;

    @Before
    public void setUp() {
        this.facebookClient = mock(FacebookClient.class);
        this.sendOperations = new DefaultSendOperations(facebookClient);
    }

    @Test
    public void testSenderAction() {
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        sendOperations.senderAction(messageRecipient, senderAction);
        verifySend(messageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction));
    }

    @Test
    public void testSenderActionWithNotificationType() {
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        sendOperations.senderAction(messageRecipient, senderAction, NotificationTypeEnum.NO_PUSH);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction));
    }

    @Test
    public void testMarkSeen() {
        sendOperations.markSeen(messageRecipient);
        verifySend(messageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.mark_seen));
    }

    @Test
    public void testMarkSeenWithNotificationType() {
        sendOperations.markSeen(messageRecipient, NotificationTypeEnum.NO_PUSH);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.mark_seen));
    }

    @Test
    public void testTypingOn() {
        sendOperations.typingOn(messageRecipient);
        verifySend(messageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_on));
    }

    @Test
    public void testTypingOnWithNotificationType() {
        sendOperations.typingOn(messageRecipient, NotificationTypeEnum.NO_PUSH);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_on));
    }

    @Test
    public void testTypingOff() {
        sendOperations.typingOff(messageRecipient);
        verifySend(messageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_off));
    }

    @Test
    public void testTypingOffWithNotificationType() {
        sendOperations.typingOff(messageRecipient, NotificationTypeEnum.NO_PUSH);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_off));
    }

    @Test
    public void testMessage() {
        Message message = new Message("Hello!");
        sendOperations.message(messageRecipient, message);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testMessageWithNotificationType() {
        Message message = new Message("Hello!");
        sendOperations.message(messageRecipient, message, NotificationTypeEnum.NO_PUSH);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testMessageWithNotificationTypeAndMessageTag() {
        Message message = new Message("Hello!");
        sendOperations.message(messageRecipient, message, NotificationTypeEnum.NO_PUSH, MessageTag.ISSUE_RESOLUTION);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(TAG_PARAM_NAME, MessageTag.ISSUE_RESOLUTION.getTag()),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testMessageWithMessageTag() {
        Message message = new Message("Hello!");
        sendOperations.message(messageRecipient, message, MessageTag.ISSUE_RESOLUTION);
        verifySend(messageRecipient,
                Parameter.with(TAG_PARAM_NAME, MessageTag.ISSUE_RESOLUTION.getTag()),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testMessageWithCustomMessageTag() {
        Message message = new Message("Hello!");
        sendOperations.message(messageRecipient, message, new MessageTag("CUSTOM"));
        verifySend(messageRecipient,
                Parameter.with(TAG_PARAM_NAME, "CUSTOM"),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessage() {
        String text = "Hello!";
        sendOperations.textMessage(messageRecipient, text);
        Message message = new Message(text);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWithNotificationType() {
        String text = "Hello!";
        sendOperations.textMessage(messageRecipient, text, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(text);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWithNotificationTypeAndMessageTag() {
        String text = "Hello!";
        sendOperations.textMessage(messageRecipient, text, NotificationTypeEnum.NO_PUSH, MessageTag.ISSUE_RESOLUTION);
        Message message = new Message(text);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(TAG_PARAM_NAME, MessageTag.ISSUE_RESOLUTION.getTag()),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWitMessageTag() {
        String text = "Hello!";
        sendOperations.textMessage(messageRecipient, text, MessageTag.ISSUE_RESOLUTION);
        Message message = new Message(text);
        verifySend(messageRecipient,
                Parameter.with(TAG_PARAM_NAME, MessageTag.ISSUE_RESOLUTION.getTag()),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWitCustomMessageTag() {
        String text = "Hello!";
        sendOperations.textMessage(messageRecipient, text, new MessageTag("CUSTOM"));
        Message message = new Message(text);
        verifySend(messageRecipient,
                Parameter.with(TAG_PARAM_NAME, "CUSTOM"),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAttachment() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        sendOperations.attachment(messageRecipient, MediaAttachment.Type.IMAGE, url);
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAttachmentWithNotificationType() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        sendOperations.attachment(messageRecipient, MediaAttachment.Type.IMAGE, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testImageAttachment() {
        String url = "http://localhost";
        sendOperations.imageAttachment(messageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.IMAGE, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testImageAttachmentWithNotificationType() {
        String url = "http://localhost";
        sendOperations.imageAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.IMAGE, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAudioAttachment() {
        String url = "http://localhost";
        sendOperations.audioAttachment(messageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.AUDIO, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAudioAttachmentWithNotificationType() {
        String url = "http://localhost";
        sendOperations.audioAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.AUDIO, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testVideoAttachment() {
        String url = "http://localhost";
        sendOperations.videoAttachment(messageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.VIDEO, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testVideoAttachmentWithNotificationType() {
        String url = "http://localhost";
        sendOperations.videoAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.VIDEO, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testFileAttachment() {
        String url = "http://localhost";
        sendOperations.fileAttachment(messageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.FILE, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testFileAttachmentWithNotificationType() {
        String url = "http://localhost";
        sendOperations.fileAttachment(messageRecipient, url, NotificationTypeEnum.NO_PUSH);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.FILE, url);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTextMessage() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        sendOperations.quickReplies(messageRecipient, text, quickReplies);
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTextMessageAndNotificationType() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        sendOperations.quickReplies(messageRecipient, text, quickReplies, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTextMessageAndNotificationTypeAndMessageTag() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        sendOperations.quickReplies(messageRecipient, text, quickReplies, NotificationTypeEnum.NO_PUSH, MessageTag.ISSUE_RESOLUTION);
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(TAG_PARAM_NAME, MessageTag.ISSUE_RESOLUTION.getTag()),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTextMessageAndMessageTag() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        sendOperations.quickReplies(messageRecipient, text, quickReplies, MessageTag.ISSUE_RESOLUTION);
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(messageRecipient,
                Parameter.with(TAG_PARAM_NAME, MessageTag.ISSUE_RESOLUTION.getTag()),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTextMessageAndCustomMessageTag() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        sendOperations.quickReplies(messageRecipient, text, quickReplies, new MessageTag("CUSTOM"));
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(messageRecipient,
                Parameter.with(TAG_PARAM_NAME, "CUSTOM"),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithMediaAttachment() {
        MediaAttachment attachment = createMediaAttachment();
        List<QuickReply> quickReplies = createQuickReplies();
        sendOperations.quickReplies(messageRecipient, attachment, quickReplies);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithMediaAttachmentAndNotificationType() {
        MediaAttachment attachment = createMediaAttachment();
        List<QuickReply> quickReplies = createQuickReplies();
        sendOperations.quickReplies(messageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTemplateAttachment() {
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        sendOperations.quickReplies(messageRecipient, attachment, quickReplies);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testQuickRepliesWithTemplateAttachmentAndNotificationType() {
        ButtonTemplatePayload payload = new ButtonTemplatePayload("body text");
        TemplateAttachment attachment = new TemplateAttachment(payload);
        List<QuickReply> quickReplies = createQuickReplies();
        sendOperations.quickReplies(messageRecipient, attachment, quickReplies, NotificationTypeEnum.NO_PUSH);
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testButtonTemplate() {
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        sendOperations.buttonTemplate(messageRecipient, buttonTemplate);
        TemplateAttachment attachment = new TemplateAttachment(buttonTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testButtonTemplateWithNotificationType() {
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        sendOperations.buttonTemplate(messageRecipient, buttonTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(buttonTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testGenericTemplate() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        sendOperations.genericTemplate(messageRecipient, genericTemplate);
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testGenericTemplateWithNotificationType() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        sendOperations.genericTemplate(messageRecipient, genericTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testGenericTemplateWithNotificationTypeAndMessageTag() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        sendOperations.genericTemplate(messageRecipient, genericTemplate, NotificationTypeEnum.NO_PUSH, MessageTag.ISSUE_RESOLUTION);
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(TAG_PARAM_NAME, MessageTag.ISSUE_RESOLUTION.getTag()),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testGenericTemplateWithMessageTag() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        sendOperations.genericTemplate(messageRecipient, genericTemplate, MessageTag.ISSUE_RESOLUTION);
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(TAG_PARAM_NAME, MessageTag.ISSUE_RESOLUTION.getTag()),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testGenericTemplateWithCustomMessageTag() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        sendOperations.genericTemplate(messageRecipient, genericTemplate, new MessageTag("CUSTOM"));
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(TAG_PARAM_NAME, "CUSTOM"),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testListTemplate() {
        ListTemplatePayload listTemplate = createListTemplate();
        sendOperations.listTemplate(messageRecipient, listTemplate);
        TemplateAttachment attachment = new TemplateAttachment(listTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testListTemplateWithNotificationType() {
        ListTemplatePayload listTemplate = createListTemplate();
        sendOperations.listTemplate(messageRecipient, listTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(listTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testReceiptTemplate() {
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        sendOperations.receiptTemplate(messageRecipient, receiptTemplate);
        TemplateAttachment attachment = new TemplateAttachment(receiptTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testReceiptTemplateWithNotificationType() {
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        sendOperations.receiptTemplate(messageRecipient, receiptTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(receiptTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineItineraryTemplate() {
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        sendOperations.airlineItineraryTemplate(messageRecipient, airlineItineraryTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineItineraryTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineItineraryTemplateWithNotificationType() {
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        sendOperations.airlineItineraryTemplate(messageRecipient, airlineItineraryTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineItineraryTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineCheckinTemplate() {
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        sendOperations.airlineCheckinTemplate(messageRecipient, airlineCheckinTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineCheckinTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineCheckinTemplateWithNotificationType() {
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        sendOperations.airlineCheckinTemplate(messageRecipient, airlineCheckinTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineCheckinTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineBoardingPassTemplate() {
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        sendOperations.airlineBoardingPassTemplate(messageRecipient, airlineBoardingPassTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineBoardingPassTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineBoardingPassTemplateWithNotificationType() {
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        sendOperations.airlineBoardingPassTemplate(messageRecipient, airlineBoardingPassTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineBoardingPassTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineUpdateTemplate() {
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        sendOperations.airlineUpdateTemplate(messageRecipient, airlineUpdateTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineUpdateTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineUpdateTemplateWithNotificationType() {
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        sendOperations.airlineUpdateTemplate(messageRecipient, airlineUpdateTemplate, NotificationTypeEnum.NO_PUSH);
        TemplateAttachment attachment = new TemplateAttachment(airlineUpdateTemplate);
        Message message = new Message(attachment);
        verifySend(messageRecipient,
                Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, NotificationTypeEnum.NO_PUSH),
                Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    private void verifySend(MessageRecipient recipient, Parameter... parameters) {
        verifySend(Parameter.with(RECIPIENT_PARAM_NAME, recipient), parameters);
    }

    private void verifySend(Parameter recipient, Parameter... parameters) {
        List<Parameter> params = new ArrayList<>();
        params.add(recipient);
        params.addAll(Arrays.asList(parameters));
        verify(facebookClient).publish(MESSAGES_PATH, SendResponse.class, params.toArray(new Parameter[params.size()]));
    }

    private MediaAttachment createMediaAttachment() {
        return new MediaAttachment(MediaAttachment.Type.IMAGE, "http://localhost");
    }

    private List<QuickReply> createQuickReplies() {
        List<QuickReply> quickReplies = Arrays.asList(
                new QuickReply("title 1", "payload 1"),
                new QuickReply("title 2", "payload 2"),
                new QuickReply());
        return quickReplies;
    }

    private ButtonTemplatePayload createButtonTemplate() {
        ButtonTemplatePayload buttonTemplate = new ButtonTemplatePayload("body text");
        buttonTemplate.addButton(new CallButton("title", "phonenumber"));
        buttonTemplate.addButton(new PostbackButton("title", "postback"));
        return buttonTemplate;
    }

    private GenericTemplatePayload createGenericTemplate() {
        GenericTemplatePayload genericTemplate = new GenericTemplatePayload();
        genericTemplate.addBubble(new Bubble("bubble 1"));
        genericTemplate.addBubble(new Bubble("bubble 2"));
        return genericTemplate;
    }

    private ListTemplatePayload createListTemplate() {
        ListViewElement element1 = new ListViewElement("title 1");
        ListViewElement element2 = new ListViewElement("title 2");
        List<ListViewElement> listViewElements = Arrays.asList(element1, element2);
        ListTemplatePayload listTemplate = new ListTemplatePayload(listViewElements);
        return listTemplate;
    }

    private ReceiptTemplatePayload createReceiptTemplate() {
        String recipientName = "recipient name";
        String orderNumber = "order number";
        String currency = "currency";
        String paymentMethod = "payment method";
        ReceiptSummary summary = new ReceiptSummary(100.00);
        ReceiptTemplatePayload receiptTemplate = new ReceiptTemplatePayload(recipientName, orderNumber, currency, paymentMethod, summary);
        return receiptTemplate;
    }

    private AirlineItineraryTemplatePayload createAirlineItineraryTemplate() {
        String introMessage = "intro message";
        String locale = "locale";
        String pnrNumber = "pnr number";
        double totalPrice = 100.00;
        AirlineItineraryTemplatePayload airlineItineraryTemplate =
                new AirlineItineraryTemplatePayload(introMessage, locale, pnrNumber, totalPrice);
        return airlineItineraryTemplate;
    }

    private AirlineCheckinTemplatePayload createAirlineCheckinTemplate() {
        String introMessage = "intro message";
        String locale = "locale";
        String pnrNumber = "pnr number";
        String checkinUrl = "checkin url";
        AirlineCheckinTemplatePayload airlineCheckinTemplate =
                new AirlineCheckinTemplatePayload(introMessage, locale, pnrNumber, checkinUrl);
        airlineCheckinTemplate.addFlightInfo(createFlightInfo());
        return airlineCheckinTemplate;
    }

    private FlightInfo createFlightInfo() {
        String connectionId = "connection id";
        String segmentId = "segment id";
        String flightNumber = "flight number";
        FlightAirport departureAirport = new FlightAirport("departure airport code", "departure city");
        FlightAirport arrivalAirport = new FlightAirport("arrival airport code", "arrival city");
        Date departureDate = new Date(0);
        Date arrivalDate = new Date(3600000);
        FlightSchedule flightSchedule = new FlightSchedule(departureDate, arrivalDate);
        String travelClass = "travel class";
        FlightInfo flightInfo =
                new FlightInfo(connectionId, segmentId, flightNumber, departureAirport, arrivalAirport, flightSchedule, travelClass);
        return flightInfo;
    }

    private AirlineBoardingPassTemplatePayload createAirlineBoardingPassTemplate() {
        String introMessage = "intro message";
        ;
        String locale = "locale";
        List<BoardingPass> boardingPassList = Arrays.asList(createBoardingPass());
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                new AirlineBoardingPassTemplatePayload(introMessage, locale, boardingPassList);
        return airlineBoardingPassTemplate;
    }

    private BoardingPass createBoardingPass() {
        String passengerName = "passenger name";
        String pnrNumber = "pnr number";
        String logoImageUrl = "logo image url";
        String aboveBarCodeImageUrl = "above bar code image url";
        FlightInfo flightInfo = createFlightInfo();
        BoardingPass boardingPass = new BoardingPass(passengerName, pnrNumber, logoImageUrl, aboveBarCodeImageUrl, flightInfo);
        return boardingPass;
    }

    private AirlineUpdateTemplatePayload createAirlineUpdateTemplate() {
        String locale = "locale";
        String pnrNumber = "pnr number";
        FlightInfo updateFlightInfo = createFlightInfo();
        AirlineUpdateTemplatePayload airlineUpdateTemplate = new AirlineUpdateTemplatePayload(locale, pnrNumber, updateFlightInfo);
        return airlineUpdateTemplate;
    }
}
