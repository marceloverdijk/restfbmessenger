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
import com.restfb.exception.FacebookException;
import com.restfb.types.send.ButtonTemplatePayload;
import com.restfb.types.send.GenericTemplatePayload;
import com.restfb.types.send.ListTemplatePayload;
import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.Message;
import com.restfb.types.send.MessageRecipient;
import com.restfb.types.send.NotificationTypeEnum;
import com.restfb.types.send.QuickReply;
import com.restfb.types.send.ReceiptTemplatePayload;
import com.restfb.types.send.SendResponse;
import com.restfb.types.send.SenderActionEnum;
import com.restfb.types.send.TemplateAttachment;
import com.restfb.types.send.TemplatePayload;
import com.restfb.types.send.airline.AirlineBoardingPassTemplatePayload;
import com.restfb.types.send.airline.AirlineCheckinTemplatePayload;
import com.restfb.types.send.airline.AirlineItineraryTemplatePayload;
import com.restfb.types.send.airline.AirlineUpdateTemplatePayload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of the {@link SendOperations} interface.
 *
 * @author Marcel Overdijk
 * @since 1.0.0
 */
public class DefaultSendOperations implements SendOperations {

    public static final String MESSAGES_PATH = "me/messages";

    public static final String RECIPIENT_PARAM_NAME = "recipient";
    public static final String MESSAGE_PARAM_NAME = "message";
    public static final String SENDER_ACTION_PARAM_NAME = "sender_action";
    public static final String NOTIFICATION_TYPE_PARAM_NAME = "notification_type";
    public static final String TAG_PARAM_NAME = "tag";

    protected FacebookClient facebookClient;

    /**
     * Creates a {@code DefaultSendOperations} instance.
     *
     * @param facebookClient the facebook client, not null
     */
    public DefaultSendOperations(FacebookClient facebookClient) {
        this.facebookClient = requireNonNull(facebookClient, "'facebookClient' must not be null");
    }

    @Override
    public SendResponse senderAction(MessageRecipient recipient, SenderActionEnum senderAction) throws FacebookException {
        return senderAction(recipient, senderAction, null);
    }

    @Override
    public SendResponse senderAction(MessageRecipient recipient, SenderActionEnum senderAction, NotificationTypeEnum notificationType)
            throws FacebookException {
        requireNonNull(recipient, "'recipient' must not be null");
        requireNonNull(senderAction, "'senderAction' must not be null");
        return send(recipient, notificationType, null, Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction.name()));
    }

    @Override
    public SendResponse markSeen(MessageRecipient recipient) throws FacebookException {
        return markSeen(recipient, null);
    }

    @Override
    public SendResponse markSeen(MessageRecipient recipient, NotificationTypeEnum notificationType) throws FacebookException {
        return senderAction(recipient, SenderActionEnum.mark_seen, notificationType);
    }

    @Override
    public SendResponse typingOn(MessageRecipient recipient) throws FacebookException {
        return typingOn(recipient, null);
    }

    @Override
    public SendResponse typingOn(MessageRecipient recipient, NotificationTypeEnum notificationType) throws FacebookException {
        return senderAction(recipient, SenderActionEnum.typing_on, notificationType);
    }

    @Override
    public SendResponse typingOff(MessageRecipient recipient) throws FacebookException {
        return typingOff(recipient, null);
    }

    @Override
    public SendResponse typingOff(MessageRecipient recipient, NotificationTypeEnum notificationType) throws FacebookException {
        return senderAction(recipient, SenderActionEnum.typing_off, notificationType);
    }

    @Override
    public SendResponse message(MessageRecipient recipient, Message message) throws FacebookException {
        return message(recipient, message, null, null);
    }

    @Override
    public SendResponse message(MessageRecipient recipient, Message message, NotificationTypeEnum notificationType)
            throws FacebookException {
        return message(recipient, message, notificationType, null);
    }

    @Override
    public SendResponse message(MessageRecipient recipient, Message message, NotificationTypeEnum notificationType, MessageTag tag)
            throws FacebookException {
        requireNonNull(recipient, "'recipient' must not be null");
        requireNonNull(message, "'message' must not be null");
        return send(recipient, notificationType, tag, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Override public SendResponse message(MessageRecipient recipient, Message message, MessageTag tag) throws FacebookException {
        return message(recipient, message, null, tag);
    }

    @Override
    public SendResponse textMessage(MessageRecipient recipient, String text) throws FacebookException {
        return textMessage(recipient, text, null, null);
    }

    @Override
    public SendResponse textMessage(MessageRecipient recipient, String text, NotificationTypeEnum notificationType)
            throws FacebookException {
        return textMessage(recipient, text, notificationType, null);
    }

    @Override
    public SendResponse textMessage(MessageRecipient recipient, String text, NotificationTypeEnum notificationType, MessageTag tag)
            throws FacebookException {
        requireNonNull(recipient, "'recipient' must not be null");
        requireNonNull(text, "'text' must not be null");
        Message message = new Message(text);
        return message(recipient, message, notificationType, tag);
    }

    @Override public SendResponse textMessage(MessageRecipient recipient, String text, MessageTag tag) throws FacebookException {
        return textMessage(recipient, text, null, tag);
    }

    @Override
    public SendResponse attachment(MessageRecipient recipient, MediaAttachment.Type type, String url) throws FacebookException {
        return attachment(recipient, type, url, null);
    }

    @Override
    public SendResponse attachment(MessageRecipient recipient, MediaAttachment.Type type, String url, NotificationTypeEnum notificationType)
            throws FacebookException {
        requireNonNull(recipient, "'recipient' must not be null");
        requireNonNull(type, "'type' must not be null");
        requireNonNull(url, "'url' must not be null");
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        return message(recipient, message, notificationType);
    }

    @Override
    public SendResponse imageAttachment(MessageRecipient recipient, String url) throws FacebookException {
        return imageAttachment(recipient, url, null);
    }

    @Override
    public SendResponse imageAttachment(MessageRecipient recipient, String url, NotificationTypeEnum notificationType)
            throws FacebookException {
        return attachment(recipient, MediaAttachment.Type.IMAGE, url, notificationType);
    }

    @Override
    public SendResponse audioAttachment(MessageRecipient recipient, String url) throws FacebookException {
        return audioAttachment(recipient, url, null);
    }

    @Override
    public SendResponse audioAttachment(MessageRecipient recipient, String url, NotificationTypeEnum notificationType)
            throws FacebookException {
        return attachment(recipient, MediaAttachment.Type.AUDIO, url, notificationType);
    }

    @Override
    public SendResponse videoAttachment(MessageRecipient recipient, String url) throws FacebookException {
        return videoAttachment(recipient, url, null);
    }

    @Override
    public SendResponse videoAttachment(MessageRecipient recipient, String url, NotificationTypeEnum notificationType)
            throws FacebookException {
        return attachment(recipient, MediaAttachment.Type.VIDEO, url, notificationType);
    }

    @Override
    public SendResponse fileAttachment(MessageRecipient recipient, String url) throws FacebookException {
        return fileAttachment(recipient, url, null);
    }

    @Override
    public SendResponse fileAttachment(MessageRecipient recipient, String url, NotificationTypeEnum notificationType)
            throws FacebookException {
        return attachment(recipient, MediaAttachment.Type.FILE, url, notificationType);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, String text, List<QuickReply> quickReplies) throws FacebookException {
        return quickReplies(recipient, text, quickReplies, null, null);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, String text, List<QuickReply> quickReplies,
            NotificationTypeEnum notificationType) throws FacebookException {
        return quickReplies(recipient, text, quickReplies, notificationType, null);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, String text, List<QuickReply> quickReplies,
            NotificationTypeEnum notificationType, MessageTag tag) throws FacebookException {
        requireNonNull(recipient, "'recipient' must not be null");
        requireNonNull(text, "'text' must not be null");
        requireNonNull(quickReplies, "'quickReplies' must not be null");
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        return message(recipient, message, notificationType, tag);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, String text, List<QuickReply> quickReplies, MessageTag tag)
            throws FacebookException {
        return quickReplies(recipient, text, quickReplies, null, tag);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, MediaAttachment attachment, List<QuickReply> quickReplies)
            throws FacebookException {
        return quickReplies(recipient, attachment, quickReplies, null);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, MediaAttachment attachment, List<QuickReply> quickReplies,
            NotificationTypeEnum notificationType) throws FacebookException {
        requireNonNull(recipient, "'recipient' must not be null");
        requireNonNull(attachment, "'attachment' must not be null");
        requireNonNull(quickReplies, "'quickReplies' must not be null");
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        return message(recipient, message, notificationType);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, TemplateAttachment attachment, List<QuickReply> quickReplies)
            throws FacebookException {
        return quickReplies(recipient, attachment, quickReplies, null);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, TemplateAttachment attachment, List<QuickReply> quickReplies,
            NotificationTypeEnum notificationType) throws FacebookException {
        requireNonNull(recipient, "'recipient' must not be null");
        requireNonNull(attachment, "'attachment' must not be null");
        requireNonNull(quickReplies, "'quickReplies' must not be null");
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        return message(recipient, message, notificationType);
    }

    @Override
    public SendResponse buttonTemplate(MessageRecipient recipient, ButtonTemplatePayload buttonTemplate) throws FacebookException {
        return buttonTemplate(recipient, buttonTemplate, null);
    }

    @Override
    public SendResponse buttonTemplate(MessageRecipient recipient, ButtonTemplatePayload buttonTemplate,
            NotificationTypeEnum notificationType) throws FacebookException {
        return template(recipient, buttonTemplate, notificationType, null);
    }

    @Override
    public SendResponse genericTemplate(MessageRecipient recipient, GenericTemplatePayload genericTemplate) throws FacebookException {
        return genericTemplate(recipient, genericTemplate, null, null);
    }

    @Override
    public SendResponse genericTemplate(MessageRecipient recipient, GenericTemplatePayload genericTemplate,
            NotificationTypeEnum notificationType) throws FacebookException {
        return genericTemplate(recipient, genericTemplate, notificationType, null);
    }

    @Override
    public SendResponse genericTemplate(MessageRecipient recipient, GenericTemplatePayload genericTemplate,
            NotificationTypeEnum notificationType, MessageTag tag) throws FacebookException {
        return template(recipient, genericTemplate, notificationType, tag);
    }

    @Override
    public SendResponse genericTemplate(MessageRecipient recipient, GenericTemplatePayload genericTemplate, MessageTag tag)
            throws FacebookException {
        return genericTemplate(recipient, genericTemplate, null, tag);
    }

    @Override
    public SendResponse listTemplate(MessageRecipient recipient, ListTemplatePayload listTemplate) throws FacebookException {
        return listTemplate(recipient, listTemplate, null);
    }

    @Override
    public SendResponse listTemplate(MessageRecipient recipient, ListTemplatePayload listTemplate, NotificationTypeEnum notificationType)
            throws FacebookException {
        return template(recipient, listTemplate, notificationType, null);
    }

    @Override
    public SendResponse receiptTemplate(MessageRecipient recipient, ReceiptTemplatePayload receiptTemplate) throws FacebookException {
        return receiptTemplate(recipient, receiptTemplate, null);
    }

    @Override
    public SendResponse receiptTemplate(MessageRecipient recipient, ReceiptTemplatePayload receiptTemplate,
            NotificationTypeEnum notificationType) throws FacebookException {
        return template(recipient, receiptTemplate, notificationType, null);
    }

    @Override
    public SendResponse airlineItineraryTemplate(MessageRecipient recipient, AirlineItineraryTemplatePayload airlineItineraryTemplate)
            throws FacebookException {
        return airlineItineraryTemplate(recipient, airlineItineraryTemplate, null);
    }

    @Override
    public SendResponse airlineItineraryTemplate(MessageRecipient recipient, AirlineItineraryTemplatePayload airlineItineraryTemplate,
            NotificationTypeEnum notificationType) throws FacebookException {
        return template(recipient, airlineItineraryTemplate, notificationType, null);
    }

    @Override
    public SendResponse airlineCheckinTemplate(MessageRecipient recipient, AirlineCheckinTemplatePayload airlineCheckinTemplate)
            throws FacebookException {
        return airlineCheckinTemplate(recipient, airlineCheckinTemplate, null);
    }

    @Override
    public SendResponse airlineCheckinTemplate(MessageRecipient recipient, AirlineCheckinTemplatePayload airlineCheckinTemplate,
            NotificationTypeEnum notificationType) throws FacebookException {
        return template(recipient, airlineCheckinTemplate, notificationType, null);
    }

    @Override
    public SendResponse airlineBoardingPassTemplate(MessageRecipient recipient,
            AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate) throws FacebookException {
        return airlineBoardingPassTemplate(recipient, airlineBoardingPassTemplate, null);
    }

    @Override
    public SendResponse airlineBoardingPassTemplate(MessageRecipient recipient,
            AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate, NotificationTypeEnum notificationType)
            throws FacebookException {
        return template(recipient, airlineBoardingPassTemplate, notificationType, null);
    }

    @Override
    public SendResponse airlineUpdateTemplate(MessageRecipient recipient, AirlineUpdateTemplatePayload airlineUpdateTemplate)
            throws FacebookException {
        return airlineUpdateTemplate(recipient, airlineUpdateTemplate, null);
    }

    @Override
    public SendResponse airlineUpdateTemplate(MessageRecipient recipient, AirlineUpdateTemplatePayload airlineUpdateTemplate,
            NotificationTypeEnum notificationType) throws FacebookException {
        return template(recipient, airlineUpdateTemplate, notificationType, null);
    }

    protected SendResponse template(MessageRecipient recipient, TemplatePayload template, NotificationTypeEnum notificationType,
            MessageTag tag) {
        requireNonNull(recipient, "'recipient' must not be null");
        requireNonNull(template, "'template' must not be null");
        TemplateAttachment attachment = new TemplateAttachment(template);
        Message message = new Message(attachment);
        return message(recipient, message, notificationType, tag);
    }

    protected SendResponse send(MessageRecipient recipient, NotificationTypeEnum notificationType, MessageTag tag,
            Parameter... parameters) {
        requireNonNull(recipient, "'recipient' must not be null");
        List<Parameter> params = new ArrayList<>();
        params.add(Parameter.with(RECIPIENT_PARAM_NAME, recipient));
        if (notificationType != null) {
            params.add(Parameter.with(NOTIFICATION_TYPE_PARAM_NAME, notificationType.name()));
        }
        if (tag != null) {
            params.add(Parameter.with(TAG_PARAM_NAME, tag.getTag()));
        }
        params.addAll(Arrays.asList(parameters));
        return send(params.toArray(new Parameter[params.size()]));
    }

    protected SendResponse send(Parameter... parameters) {
        return send(SendResponse.class, parameters);
    }

    protected <T> T send(Class<T> objectType, Parameter... parameters) {
        requireNonNull(objectType, "'objectType' must not be null");
        return facebookClient.publish(MESSAGES_PATH, objectType, parameters);
    }
}
