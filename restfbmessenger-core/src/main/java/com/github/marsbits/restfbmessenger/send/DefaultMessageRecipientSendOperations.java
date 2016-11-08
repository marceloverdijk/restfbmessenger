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
import com.restfb.types.send.airline.AirlineBoardingPassTemplatePayload;
import com.restfb.types.send.airline.AirlineCheckinTemplatePayload;
import com.restfb.types.send.airline.AirlineItineraryTemplatePayload;
import com.restfb.types.send.airline.AirlineUpdateTemplatePayload;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of the {@link MessageRecipientSendOperations} interface.
 *
 * @author Marcel Overdijk
 * @since 1.1.0
 */
public class DefaultMessageRecipientSendOperations implements MessageRecipientSendOperations {

    private SendOperations sendOperations;
    private MessageRecipient recipient;
    private NotificationTypeEnum notificationType;

    /**
     * Creates a {@code DefaultMessageRecipientSendOperations} instance for the given {@link MessageRecipient}.
     *
     * @param sendOperations the send operations, not null
     * @param recipient the recipient, not null
     * @param notificationType the push notification type
     */
    public DefaultMessageRecipientSendOperations(SendOperations sendOperations, MessageRecipient recipient,
            NotificationTypeEnum notificationType) {
        this.sendOperations = requireNonNull(sendOperations, "'sendOperations' must not be null");
        this.recipient = requireNonNull(recipient, "'recipient' must not be null");
        this.notificationType = notificationType;
    }


    @Override
    public SendResponse senderAction(SenderActionEnum senderAction) throws FacebookException {
        return senderAction(senderAction, notificationType);
    }

    @Override
    public SendResponse senderAction(SenderActionEnum senderAction, NotificationTypeEnum notificationType)
            throws FacebookException {
        return sendOperations.senderAction(recipient, senderAction, notificationType);
    }

    @Override
    public SendResponse markSeen() throws FacebookException {
        return markSeen(notificationType);
    }

    @Override
    public SendResponse markSeen(NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.markSeen(recipient, notificationType);
    }

    @Override
    public SendResponse typingOn() throws FacebookException {
        return typingOn(notificationType);
    }

    @Override
    public SendResponse typingOn(NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.typingOn(recipient, notificationType);
    }

    @Override
    public SendResponse typingOff() throws FacebookException {
        return typingOff(notificationType);
    }

    @Override
    public SendResponse typingOff(NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.typingOff(recipient, notificationType);
    }

    @Override
    public SendResponse message(Message message) throws FacebookException {
        return message(message, notificationType);
    }

    @Override
    public SendResponse message(Message message, NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.message(recipient, message, notificationType);
    }

    @Override
    public SendResponse textMessage(String text) throws FacebookException {
        return textMessage(text, notificationType);
    }

    @Override
    public SendResponse textMessage(String text, NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.textMessage(recipient, text, notificationType);
    }

    @Override
    public SendResponse attachment(MediaAttachment.Type type, String url) throws FacebookException {
        return attachment(type, url, notificationType);
    }

    @Override
    public SendResponse attachment(MediaAttachment.Type type, String url, NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.attachment(recipient, type, url, notificationType);
    }

    @Override
    public SendResponse imageAttachment(String url) throws FacebookException {
        return imageAttachment(url, notificationType);
    }

    @Override
    public SendResponse imageAttachment(String url, NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.imageAttachment(recipient, url, notificationType);
    }

    @Override
    public SendResponse audioAttachment(String url) throws FacebookException {
        return audioAttachment(url, notificationType);
    }

    @Override
    public SendResponse audioAttachment(String url, NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.audioAttachment(recipient, url, notificationType);
    }

    @Override
    public SendResponse videoAttachment(String url) throws FacebookException {
        return videoAttachment(url, notificationType);
    }

    @Override
    public SendResponse videoAttachment(String url, NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.videoAttachment(recipient, url, notificationType);
    }

    @Override
    public SendResponse fileAttachment(String url) throws FacebookException {
        return fileAttachment(url, notificationType);
    }

    @Override
    public SendResponse fileAttachment(String url, NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.fileAttachment(recipient, url, notificationType);
    }

    @Override
    public SendResponse quickReplies(String text, List<QuickReply> quickReplies) throws FacebookException {
        return quickReplies(text, quickReplies, notificationType);
    }

    @Override
    public SendResponse quickReplies(String text, List<QuickReply> quickReplies, NotificationTypeEnum notificationType)
            throws FacebookException {
        return sendOperations.quickReplies(recipient, text, quickReplies, notificationType);
    }

    @Override
    public SendResponse quickReplies(MediaAttachment attachment, List<QuickReply> quickReplies) throws FacebookException {
        return quickReplies(attachment, quickReplies, notificationType);
    }

    @Override
    public SendResponse quickReplies(MediaAttachment attachment, List<QuickReply> quickReplies, NotificationTypeEnum notificationType)
            throws FacebookException {
        return sendOperations.quickReplies(recipient, attachment, quickReplies, notificationType);
    }

    @Override
    public SendResponse quickReplies(TemplateAttachment attachment, List<QuickReply> quickReplies) throws FacebookException {
        return quickReplies(attachment, quickReplies, notificationType);
    }

    @Override
    public SendResponse quickReplies(TemplateAttachment attachment, List<QuickReply> quickReplies, NotificationTypeEnum notificationType)
            throws FacebookException {
        return sendOperations.quickReplies(recipient, attachment, quickReplies, notificationType);
    }

    @Override
    public SendResponse buttonTemplate(ButtonTemplatePayload buttonTemplate) throws FacebookException {
        return buttonTemplate(buttonTemplate, notificationType);
    }

    @Override
    public SendResponse buttonTemplate(ButtonTemplatePayload buttonTemplate, NotificationTypeEnum notificationType)
            throws FacebookException {
        return sendOperations.buttonTemplate(recipient, buttonTemplate, notificationType);
    }

    @Override
    public SendResponse genericTemplate(GenericTemplatePayload genericTemplate) throws FacebookException {
        return genericTemplate(genericTemplate, notificationType);
    }

    @Override
    public SendResponse genericTemplate(GenericTemplatePayload genericTemplate, NotificationTypeEnum notificationType)
            throws FacebookException {
        return sendOperations.genericTemplate(recipient, genericTemplate, notificationType);
    }

    @Override
    public SendResponse listTemplate(ListTemplatePayload listTemplate) throws FacebookException {
        return listTemplate(listTemplate, notificationType);
    }

    @Override
    public SendResponse listTemplate(ListTemplatePayload listTemplate, NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.listTemplate(recipient, listTemplate, notificationType);
    }

    @Override
    public SendResponse receiptTemplate(ReceiptTemplatePayload receiptTemplate) throws FacebookException {
        return receiptTemplate(receiptTemplate, notificationType);
    }

    @Override
    public SendResponse receiptTemplate(ReceiptTemplatePayload receiptTemplate, NotificationTypeEnum notificationType)
            throws FacebookException {
        return sendOperations.receiptTemplate(recipient, receiptTemplate, notificationType);
    }

    @Override
    public SendResponse airlineItineraryTemplate(AirlineItineraryTemplatePayload airlineItineraryTemplate) throws FacebookException {
        return airlineItineraryTemplate(airlineItineraryTemplate, notificationType);
    }

    @Override
    public SendResponse airlineItineraryTemplate(AirlineItineraryTemplatePayload airlineItineraryTemplate,
            NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.airlineItineraryTemplate(recipient, airlineItineraryTemplate, notificationType);
    }

    @Override
    public SendResponse airlineCheckinTemplate(AirlineCheckinTemplatePayload airlineCheckinTemplate) throws FacebookException {
        return airlineCheckinTemplate(airlineCheckinTemplate, notificationType);
    }

    @Override
    public SendResponse airlineCheckinTemplate(AirlineCheckinTemplatePayload airlineCheckinTemplate, NotificationTypeEnum notificationType)
            throws FacebookException {
        return sendOperations.airlineCheckinTemplate(recipient, airlineCheckinTemplate, notificationType);
    }

    @Override
    public SendResponse airlineBoardingPassTemplate(AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate)
            throws FacebookException {
        return airlineBoardingPassTemplate(airlineBoardingPassTemplate, notificationType);
    }

    @Override
    public SendResponse airlineBoardingPassTemplate(AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate,
            NotificationTypeEnum notificationType) throws FacebookException {
        return sendOperations.airlineBoardingPassTemplate(recipient, airlineBoardingPassTemplate, notificationType);
    }

    @Override
    public SendResponse airlineUpdateTemplate(AirlineUpdateTemplatePayload airlineUpdateTemplate) throws FacebookException {
        return airlineUpdateTemplate(airlineUpdateTemplate, notificationType);
    }

    @Override
    public SendResponse airlineUpdateTemplate(AirlineUpdateTemplatePayload airlineUpdateTemplate, NotificationTypeEnum notificationType)
            throws FacebookException {
        return sendOperations.airlineUpdateTemplate(recipient, airlineUpdateTemplate, notificationType);
    }
}


