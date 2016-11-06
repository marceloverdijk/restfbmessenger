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
import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.Message;
import com.restfb.types.send.MessageRecipient;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public SendResponse senderAction(MessageRecipient recipient, SenderActionEnum senderAction) {
        return send(recipient, Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction.name()));
    }

    @Override
    public SendResponse senderAction(String recipient, SenderActionEnum senderAction) {
        return senderAction(toMessageRecipient(recipient), senderAction);
    }

    @Override
    public SendResponse markSeen(MessageRecipient recipient) {
        return senderAction(recipient, SenderActionEnum.mark_seen);
    }

    @Override
    public SendResponse markSeen(String recipient) {
        return markSeen(toMessageRecipient(recipient));
    }

    @Override
    public SendResponse typingOn(MessageRecipient recipient) {
        return senderAction(recipient, SenderActionEnum.typing_on);
    }

    @Override
    public SendResponse typingOn(String recipient) {
        return typingOn(toMessageRecipient(recipient));
    }

    @Override
    public SendResponse typingOff(MessageRecipient recipient) {
        return senderAction(recipient, SenderActionEnum.typing_off);
    }

    @Override
    public SendResponse typingOff(String recipient) {
        return typingOff(toMessageRecipient(recipient));
    }

    @Override
    public SendResponse message(MessageRecipient recipient, Message message) {
        return send(recipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Override
    public SendResponse message(String recipient, Message message) {
        return message(toMessageRecipient(recipient), message);
    }

    @Override
    public SendResponse textMessage(MessageRecipient recipient, String text) {
        Message message = new Message(text);
        return message(recipient, message);
    }

    @Override
    public SendResponse textMessage(String recipient, String text) {
        return textMessage(toMessageRecipient(recipient), text);
    }

    @Override
    public SendResponse attachment(MessageRecipient recipient, MediaAttachment.Type type, String url) {
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        return message(recipient, message);
    }

    @Override
    public SendResponse attachment(String recipient, MediaAttachment.Type type, String url) {
        return attachment(toMessageRecipient(recipient), type, url);
    }

    @Override
    public SendResponse imageAttachment(MessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.IMAGE, url);
    }

    @Override
    public SendResponse imageAttachment(String recipient, String url) {
        return imageAttachment(toMessageRecipient(recipient), url);
    }

    @Override
    public SendResponse audioAttachment(MessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.AUDIO, url);
    }

    @Override
    public SendResponse audioAttachment(String recipient, String url) {
        return audioAttachment(toMessageRecipient(recipient), url);
    }

    @Override
    public SendResponse videoAttachment(MessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.VIDEO, url);
    }

    @Override
    public SendResponse videoAttachment(String recipient, String url) {
        return videoAttachment(toMessageRecipient(recipient), url);
    }

    @Override
    public SendResponse fileAttachment(MessageRecipient recipient, String url) {
        return attachment(recipient, MediaAttachment.Type.FILE, url);
    }

    @Override
    public SendResponse fileAttachment(String recipient, String url) {
        return fileAttachment(toMessageRecipient(recipient), url);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, String text, List<QuickReply> quickReplies) {
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        return message(recipient, message);
    }

    @Override
    public SendResponse quickReplies(String recipient, String text, List<QuickReply> quickReplies) {
        return quickReplies(toMessageRecipient(recipient), text, quickReplies);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, MediaAttachment attachment, List<QuickReply> quickReplies) {
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        return message(recipient, message);
    }

    @Override
    public SendResponse quickReplies(String recipient, MediaAttachment attachment, List<QuickReply> quickReplies) {
        return quickReplies(toMessageRecipient(recipient), attachment, quickReplies);
    }

    @Override
    public SendResponse quickReplies(MessageRecipient recipient, TemplateAttachment attachment, List<QuickReply> quickReplies) {
        Message message = new Message(attachment);
        message.addQuickReplies(quickReplies);
        return message(recipient, message);
    }

    @Override
    public SendResponse quickReplies(String recipient, TemplateAttachment attachment, List<QuickReply> quickReplies) {
        return quickReplies(toMessageRecipient(recipient), attachment, quickReplies);
    }

    @Override
    public SendResponse genericTemplate(MessageRecipient recipient, GenericTemplatePayload genericTemplate) {
        return template(recipient, genericTemplate);
    }

    @Override
    public SendResponse genericTemplate(String recipient, GenericTemplatePayload genericTemplate) {
        return genericTemplate(toMessageRecipient(recipient), genericTemplate);
    }

    @Override
    public SendResponse buttonTemplate(MessageRecipient recipient, ButtonTemplatePayload buttonTemplate) {
        return template(recipient, buttonTemplate);
    }

    @Override
    public SendResponse buttonTemplate(String recipient, ButtonTemplatePayload buttonTemplate) {
        return buttonTemplate(toMessageRecipient(recipient), buttonTemplate);
    }

    @Override
    public SendResponse receiptTemplate(MessageRecipient recipient, ReceiptTemplatePayload receiptTemplate) {
        return template(recipient, receiptTemplate);
    }

    @Override
    public SendResponse receiptTemplate(String recipient, ReceiptTemplatePayload receiptTemplate) {
        return receiptTemplate(toMessageRecipient(recipient), receiptTemplate);
    }

    @Override
    public SendResponse airlineItineraryTemplate(MessageRecipient recipient, AirlineItineraryTemplatePayload airlineItineraryTemplate) {
        return template(recipient, airlineItineraryTemplate);
    }

    @Override
    public SendResponse airlineItineraryTemplate(String recipient, AirlineItineraryTemplatePayload airlineItineraryTemplate) {
        return airlineItineraryTemplate(toMessageRecipient(recipient), airlineItineraryTemplate);
    }

    @Override
    public SendResponse airlineCheckinTemplate(MessageRecipient recipient, AirlineCheckinTemplatePayload airlineCheckinTemplate) {
        return template(recipient, airlineCheckinTemplate);
    }

    @Override
    public SendResponse airlineCheckinTemplate(String recipient, AirlineCheckinTemplatePayload airlineCheckinTemplate) {
        return airlineCheckinTemplate(toMessageRecipient(recipient), airlineCheckinTemplate);
    }

    @Override
    public SendResponse airlineBoardingPassTemplate(MessageRecipient recipient,
            AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate) {
        return template(recipient, airlineBoardingPassTemplate);
    }

    @Override
    public SendResponse airlineBoardingPassTemplate(String recipient, AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate) {
        return airlineBoardingPassTemplate(toMessageRecipient(recipient), airlineBoardingPassTemplate);
    }

    @Override
    public SendResponse airlineUpdateTemplate(MessageRecipient recipient, AirlineUpdateTemplatePayload airlineUpdateTemplate) {
        return template(recipient, airlineUpdateTemplate);
    }

    @Override
    public SendResponse airlineUpdateTemplate(String recipient, AirlineUpdateTemplatePayload airlineUpdateTemplate) {
        return airlineUpdateTemplate(toMessageRecipient(recipient), airlineUpdateTemplate);
    }

    protected SendResponse template(MessageRecipient recipient, TemplatePayload template) {
        TemplateAttachment attachment = new TemplateAttachment(template);
        Message message = new Message(attachment);
        return message(recipient, message);
    }

    protected SendResponse send(MessageRecipient recipient, Parameter... parameters) {
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

    private MessageRecipient toMessageRecipient(String str) {
        MessageRecipient recipient = null;
        if (str != null && str.length() > 0) {
            if (str.startsWith("+")) {
                recipient = new PhoneMessageRecipient(str);
            } else {
                recipient = new IdMessageRecipient(str);
            }
        }
        return recipient;
    }
}
