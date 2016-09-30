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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.send.ButtonTemplatePayload;
import com.restfb.types.send.GenericTemplatePayload;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.Message;
import com.restfb.types.send.PhoneMessageRecipient;
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

/**
 * Default implementation of the {@link SendOperations} interface.
 *
 * @author Marcel Overdijk
 * @since 1.0.0
 */
public class DefaultSendOperations implements SendOperations {

    public static final String MESSAGES_PATH = "me/messages";

    public static final String RECIPIENT_PARAM_NAME = "recipient";
    public static final String SENDER_ACTION_PARAM_NAME = "sender_action";
    public static final String MESSAGE_PARAM_NAME = "message";

    protected FacebookClient facebookClient;

    public DefaultSendOperations(FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    @Override
    public SendResponse senderAction(IdMessageRecipient recipient, SenderActionEnum senderAction) {
        return send(recipient, Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction.name()));
    }

    @Override
    public SendResponse senderAction(PhoneMessageRecipient recipient,
            SenderActionEnum senderAction) {
        return send(recipient, Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction.name()));
    }

    @Override
    public SendResponse markSeen(IdMessageRecipient recipient) {
        return senderAction(recipient, SenderActionEnum.mark_seen);
    }

    @Override
    public SendResponse markSeen(PhoneMessageRecipient recipient) {
        return senderAction(recipient, SenderActionEnum.mark_seen);
    }

    @Override
    public SendResponse typingOn(IdMessageRecipient recipient) {
        return senderAction(recipient, SenderActionEnum.typing_on);
    }

    @Override
    public SendResponse typingOn(PhoneMessageRecipient recipient) {
        return senderAction(recipient, SenderActionEnum.typing_on);
    }

    @Override
    public SendResponse typingOff(IdMessageRecipient recipient) {
        return senderAction(recipient, SenderActionEnum.typing_off);
    }

    @Override
    public SendResponse typingOff(PhoneMessageRecipient recipient) {
        return senderAction(recipient, SenderActionEnum.typing_off);
    }

    @Override
    public SendResponse message(IdMessageRecipient recipient, Message message) {
        return send(recipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Override
    public SendResponse message(PhoneMessageRecipient recipient, Message message) {
        return send(recipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Override
    public SendResponse textMessage(IdMessageRecipient recipient, String text) {
        Message message = new Message(text);
        return message(recipient, message);
    }

    @Override
    public SendResponse textMessage(PhoneMessageRecipient recipient, String text) {
        Message message = new Message(text);
        return message(recipient, message);
    }

    @Override
    public SendResponse attachment(IdMessageRecipient recipient, MediaAttachment.Type type,
            String url) {
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        return message(recipient, message);
    }

    @Override
    public SendResponse attachment(PhoneMessageRecipient recipient, MediaAttachment.Type type,
            String url) {
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        return message(recipient, message);
    }

    @Override
    public SendResponse imageAttachment(IdMessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.IMAGE, url);
    }

    @Override
    public SendResponse imageAttachment(PhoneMessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.IMAGE, url);
    }

    @Override
    public SendResponse audioAttachment(IdMessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.AUDIO, url);
    }

    @Override
    public SendResponse audioAttachment(PhoneMessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.AUDIO, url);
    }

    @Override
    public SendResponse videoAttachment(IdMessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.VIDEO, url);
    }

    @Override
    public SendResponse videoAttachment(PhoneMessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.VIDEO, url);
    }

    @Override
    public SendResponse fileAttachment(IdMessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.FILE, url);
    }

    @Override
    public SendResponse fileAttachment(PhoneMessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.FILE, url);
    }

    @Override
    public SendResponse quickReplies(IdMessageRecipient recipient, String text,
            List<QuickReply> quickReplies) {
        Message message = new Message(text);
        addQuickReplies(message, quickReplies);
        return message(recipient, message);
    }

    @Override
    public SendResponse quickReplies(PhoneMessageRecipient recipient, String text,
            List<QuickReply> quickReplies) {
        Message message = new Message(text);
        addQuickReplies(message, quickReplies);
        return message(recipient, message);
    }

    @Override
    public SendResponse quickReplies(IdMessageRecipient recipient, MediaAttachment attachment,
            List<QuickReply> quickReplies) {
        Message message = new Message(attachment);
        addQuickReplies(message, quickReplies);
        return message(recipient, message);
    }

    @Override
    public SendResponse quickReplies(PhoneMessageRecipient recipient, MediaAttachment attachment,
            List<QuickReply> quickReplies) {
        Message message = new Message(attachment);
        addQuickReplies(message, quickReplies);
        return message(recipient, message);
    }

    @Override
    public SendResponse quickReplies(IdMessageRecipient recipient, TemplateAttachment attachment,
            List<QuickReply> quickReplies) {
        Message message = new Message(attachment);
        addQuickReplies(message, quickReplies);
        return message(recipient, message);
    }

    @Override
    public SendResponse quickReplies(PhoneMessageRecipient recipient, TemplateAttachment attachment,
            List<QuickReply> quickReplies) {
        Message message = new Message(attachment);
        addQuickReplies(message, quickReplies);
        return message(recipient, message);
    }

    @Override
    public SendResponse genericTemplate(IdMessageRecipient recipient,
            GenericTemplatePayload genericTemplate) {
        return template(recipient, genericTemplate);
    }

    @Override
    public SendResponse genericTemplate(PhoneMessageRecipient recipient,
            GenericTemplatePayload genericTemplate) {
        return template(recipient, genericTemplate);
    }

    @Override
    public SendResponse buttonTemplate(IdMessageRecipient recipient,
            ButtonTemplatePayload buttonTemplate) {
        return template(recipient, buttonTemplate);
    }

    @Override
    public SendResponse buttonTemplate(PhoneMessageRecipient recipient,
            ButtonTemplatePayload buttonTemplate) {
        return template(recipient, buttonTemplate);
    }

    @Override
    public SendResponse receiptTemplate(IdMessageRecipient recipient,
            ReceiptTemplatePayload receiptTemplate) {
        return template(recipient, receiptTemplate);
    }

    @Override
    public SendResponse receiptTemplate(PhoneMessageRecipient recipient,
            ReceiptTemplatePayload receiptTemplate) {
        return template(recipient, receiptTemplate);
    }

    @Override
    public SendResponse airlineItineraryTemplate(IdMessageRecipient recipient,
            AirlineItineraryTemplatePayload airlineItineraryTemplate) {
        return template(recipient, airlineItineraryTemplate);
    }

    @Override
    public SendResponse airlineItineraryTemplate(PhoneMessageRecipient recipient,
            AirlineItineraryTemplatePayload airlineItineraryTemplate) {
        return template(recipient, airlineItineraryTemplate);
    }

    @Override
    public SendResponse airlineCheckinTemplate(IdMessageRecipient recipient,
            AirlineCheckinTemplatePayload airlineCheckinTemplate) {
        return template(recipient, airlineCheckinTemplate);
    }

    @Override
    public SendResponse airlineCheckinTemplate(PhoneMessageRecipient recipient,
            AirlineCheckinTemplatePayload airlineCheckinTemplate) {
        return template(recipient, airlineCheckinTemplate);
    }

    @Override
    public SendResponse airlineBoardingPassTemplate(IdMessageRecipient recipient,
            AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate) {
        return template(recipient, airlineBoardingPassTemplate);
    }

    @Override
    public SendResponse airlineBoardingPassTemplate(PhoneMessageRecipient recipient,
            AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate) {
        return template(recipient, airlineBoardingPassTemplate);
    }

    @Override
    public SendResponse airlineUpdateTemplate(IdMessageRecipient recipient,
            AirlineUpdateTemplatePayload airlineUpdateTemplate) {
        return template(recipient, airlineUpdateTemplate);
    }

    @Override
    public SendResponse airlineUpdateTemplate(PhoneMessageRecipient recipient,
            AirlineUpdateTemplatePayload airlineUpdateTemplate) {
        return template(recipient, airlineUpdateTemplate);
    }

    protected SendResponse template(IdMessageRecipient recipient, TemplatePayload template) {
        TemplateAttachment attachment = new TemplateAttachment(template);
        Message message = new Message(attachment);
        return message(recipient, message);
    }

    protected SendResponse template(PhoneMessageRecipient recipient, TemplatePayload template) {
        TemplateAttachment attachment = new TemplateAttachment(template);
        Message message = new Message(attachment);
        return message(recipient, message);
    }

    protected SendResponse send(IdMessageRecipient recipient, Parameter... parameters) {
        List<Parameter> params = new ArrayList<>();
        params.add(Parameter.with(RECIPIENT_PARAM_NAME, recipient));
        params.addAll(Arrays.asList(parameters));
        return send(params.toArray(new Parameter[params.size()]));
    }

    protected SendResponse send(PhoneMessageRecipient recipient, Parameter... parameters) {
        List<Parameter> params = new ArrayList<>();
        params.add(Parameter.with(RECIPIENT_PARAM_NAME, recipient));
        params.addAll(Arrays.asList(parameters));
        return send(params.toArray(new Parameter[params.size()]));
    }

    protected SendResponse send(Parameter... parameters) {
        return send(SendResponse.class, parameters);
    }

    protected <T> T send(Class<T> objectType, Parameter... parameters) {
        return facebookClient.publish(MESSAGES_PATH, objectType, parameters);
    }

    // https://github.com/restfb/restfb/issues/581
    private void addQuickReplies(Message message, List<QuickReply> quickReplies) {
        if (quickReplies != null) {
            for (QuickReply quickReply : quickReplies) {
                message.addQuickReply(quickReply);
            }
        }
    }
}
