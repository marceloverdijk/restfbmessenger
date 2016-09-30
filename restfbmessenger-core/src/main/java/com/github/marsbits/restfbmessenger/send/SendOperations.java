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

import java.util.List;

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
import com.restfb.types.send.airline.AirlineBoardingPassTemplatePayload;
import com.restfb.types.send.airline.AirlineCheckinTemplatePayload;
import com.restfb.types.send.airline.AirlineItineraryTemplatePayload;
import com.restfb.types.send.airline.AirlineUpdateTemplatePayload;

/**
 * Interface specifying the operations for the Send API.
 *
 * @author Marcel Overdijk
 * @since 1.0.0
 */
public interface SendOperations {

    /**
     * Sends the given sender action to the user.
     *
     * @param recipient the recipient
     * @param senderAction the sender action
     * @return the {@code SendResponse}
     */
    SendResponse senderAction(IdMessageRecipient recipient, SenderActionEnum senderAction);

    /**
     * Sends the given sender action to the user.
     *
     * @param recipient the recipient
     * @param senderAction the sender action
     * @return the {@code SendResponse}
     */
    SendResponse senderAction(PhoneMessageRecipient recipient, SenderActionEnum senderAction);

    /**
     * Sends a read receipt to the user.
     *
     * @param recipient the recipient
     * @return the {@code SendResponse}
     */
    SendResponse markSeen(IdMessageRecipient recipient);

    /**
     * Sends a read receipt to the user.
     *
     * @param recipient the recipient
     * @return the {@code SendResponse}
     */
    SendResponse markSeen(PhoneMessageRecipient recipient);

    /**
     * Sends a typing indicator on to the user.
     *
     * @param recipient the recipient
     * @return the {@code SendResponse}
     */
    SendResponse typingOn(IdMessageRecipient recipient);

    /**
     * Sends a typing indicator on to the user.
     *
     * @param recipient the recipient
     * @return the {@code SendResponse}
     */
    SendResponse typingOn(PhoneMessageRecipient recipient);

    /**
     * Sends a typing indicator off to the user.
     *
     * @param recipient the recipient
     * @return the {@code SendResponse}
     */
    SendResponse typingOff(IdMessageRecipient recipient);

    /**
     * Sends a typing indicator off to the user.
     *
     * @param recipient the recipient
     * @return the {@code SendResponse}
     */
    SendResponse typingOff(PhoneMessageRecipient recipient);

    /**
     * Sends the given message to the user.
     *
     * @param recipient the recipient
     * @param message the message
     * @return the {@code SendResponse}
     */
    SendResponse message(IdMessageRecipient recipient, Message message);

    /**
     * Sends the given message to the user.
     *
     * @param recipient the recipient
     * @param message the message
     * @return the {@code SendResponse}
     */
    SendResponse message(PhoneMessageRecipient recipient, Message message);

    /**
     * Sends the given text message to the user.
     *
     * @param recipient the recipient
     * @param text the text message
     * @return the {@code SendResponse}
     */
    SendResponse textMessage(IdMessageRecipient recipient, String text);

    /**
     * Sends the given text message to the user.
     *
     * @param recipient the recipient
     * @param text the text message
     * @return the {@code SendResponse}
     */
    SendResponse textMessage(PhoneMessageRecipient recipient, String text);

    /**
     * Sends the given attachment to the user.
     *
     * @param recipient the recipient
     * @param type the type of the attachment
     * @param url the url of the attachment
     * @return the {@code SendResponse}
     */
    SendResponse attachment(IdMessageRecipient recipient, MediaAttachment.Type type, String url);

    /**
     * Sends the given attachment to the user.
     *
     * @param recipient the recipient
     * @param type the type of the attachment
     * @param url the url of the attachment
     * @return the {@code SendResponse}
     */
    SendResponse attachment(PhoneMessageRecipient recipient, MediaAttachment.Type type, String url);

    /**
     * Sends the given image to the user.
     *
     * @param recipient the recipient
     * @param url the url of the image
     * @return the {@code SendResponse}
     */
    SendResponse imageAttachment(IdMessageRecipient recipient, String url);

    /**
     * Sends the given image to the user.
     *
     * @param recipient the recipient
     * @param url the url of the image
     * @return the {@code SendResponse}
     */
    SendResponse imageAttachment(PhoneMessageRecipient recipient, String url);

    /**
     * Sends the given audio to the user.
     *
     * @param recipient the recipient
     * @param url the url of the audio
     * @return the {@code SendResponse}
     */
    SendResponse audioAttachment(IdMessageRecipient recipient, String url);

    /**
     * Sends the given audio to the user.
     *
     * @param recipient the recipient
     * @param url the url of the audio
     * @return the {@code SendResponse}
     */
    SendResponse audioAttachment(PhoneMessageRecipient recipient, String url);

    /**
     * Sends the given video to the user.
     *
     * @param recipient the recipient
     * @param url the url of the video
     * @return the {@code SendResponse}
     */
    SendResponse videoAttachment(IdMessageRecipient recipient, String url);

    /**
     * Sends the given video to the user.
     *
     * @param recipient the recipient
     * @param url the url of the video
     * @return the {@code SendResponse}
     */
    SendResponse videoAttachment(PhoneMessageRecipient recipient, String url);

    /**
     * Sends the given file to the user.
     *
     * @param recipient the recipient
     * @param url the url of the file
     * @return the {@code SendResponse}
     */
    SendResponse fileAttachment(IdMessageRecipient recipient, String url);

    /**
     * Sends the given file to the user.
     *
     * @param recipient the recipient
     * @param url the url of the file
     * @return the {@code SendResponse}
     */
    SendResponse fileAttachment(PhoneMessageRecipient recipient, String url);

    /**
     * Sends the given text message and quick replies to the user.
     *
     * @param recipient the recipient
     * @param text the text message
     * @param quickReplies the quick replies
     * @return the {@code SendResponse}
     */
    SendResponse quickReplies(IdMessageRecipient recipient, String text,
            List<QuickReply> quickReplies);

    /**
     * Sends the given text message and quick replies to the user.
     *
     * @param recipient the recipient
     * @param text the text message
     * @param quickReplies the quick replies
     * @return the {@code SendResponse}
     */
    SendResponse quickReplies(PhoneMessageRecipient recipient, String text,
            List<QuickReply> quickReplies);

    /**
     * Sends the given media attachment and quick replies to the user.
     *
     * @param recipient the recipient
     * @param attachment the media attachment
     * @param quickReplies the quick replies
     * @return the {@code SendResponse}
     */
    SendResponse quickReplies(IdMessageRecipient recipient, MediaAttachment attachment,
            List<QuickReply> quickReplies);

    /**
     * Sends the given media attachment and quick replies to the user.
     *
     * @param recipient the recipient
     * @param attachment the media attachment
     * @param quickReplies the quick replies
     * @return the {@code SendResponse}
     */
    SendResponse quickReplies(PhoneMessageRecipient recipient, MediaAttachment attachment,
            List<QuickReply> quickReplies);

    /**
     * Sends the given template attachment and quick replies to the user.
     *
     * @param recipient the recipient
     * @param attachment the template attachment
     * @param quickReplies the quick replies
     * @return the {@code SendResponse}
     */
    SendResponse quickReplies(IdMessageRecipient recipient, TemplateAttachment attachment,
            List<QuickReply> quickReplies);

    /**
     * Sends the given template attachment and quick replies to the user.
     *
     * @param recipient the recipient
     * @param attachment the template attachment
     * @param quickReplies the quick replies
     * @return the {@code SendResponse}
     */
    SendResponse quickReplies(PhoneMessageRecipient recipient, TemplateAttachment attachment,
            List<QuickReply> quickReplies);

    /**
     * Sends the given generic template to the user.
     *
     * @param recipient the recipient
     * @param genericTemplate the generic template
     * @return the {@code SendResponse}
     */
    SendResponse genericTemplate(IdMessageRecipient recipient,
            GenericTemplatePayload genericTemplate);

    /**
     * Sends the given generic template to the user.
     *
     * @param recipient the recipient
     * @param genericTemplate the generic template
     * @return the {@code SendResponse}
     */
    SendResponse genericTemplate(PhoneMessageRecipient recipient,
            GenericTemplatePayload genericTemplate);

    /**
     * Sends the given button template to the user.
     *
     * @param recipient the recipient
     * @param buttonTemplate the button template
     * @return the {@code SendResponse}
     */
    SendResponse buttonTemplate(IdMessageRecipient recipient, ButtonTemplatePayload buttonTemplate);

    /**
     * Sends the given button template to the user.
     *
     * @param recipient the recipient
     * @param buttonTemplate the button template
     * @return the {@code SendResponse}
     */
    SendResponse buttonTemplate(PhoneMessageRecipient recipient,
            ButtonTemplatePayload buttonTemplate);

    /**
     * Sends the given receipt template to the user.
     *
     * @param recipient the recipient
     * @param receiptTemplate the receipt template
     * @return the {@code SendResponse}
     */
    SendResponse receiptTemplate(IdMessageRecipient recipient,
            ReceiptTemplatePayload receiptTemplate);

    /**
     * Sends the given receipt template to the user.
     *
     * @param recipient the recipient
     * @param receiptTemplate the receipt template
     * @return the {@code SendResponse}
     */
    SendResponse receiptTemplate(PhoneMessageRecipient recipient,
            ReceiptTemplatePayload receiptTemplate);

    /**
     * Sends the given airline itinerary template to the user.
     *
     * @param recipient the recipient
     * @param airlineItineraryTemplate airline itinerary template
     * @return the {@code SendResponse}
     */
    SendResponse airlineItineraryTemplate(IdMessageRecipient recipient,
            AirlineItineraryTemplatePayload airlineItineraryTemplate);

    /**
     * Sends the given airline itinerary template to the user.
     *
     * @param recipient the recipient
     * @param airlineItineraryTemplate airline itinerary template
     * @return the {@code SendResponse}
     */
    SendResponse airlineItineraryTemplate(PhoneMessageRecipient recipient,
            AirlineItineraryTemplatePayload airlineItineraryTemplate);

    /**
     * Sends the given airline checkin template to the user.
     *
     * @param recipient the recipient
     * @param airlineCheckinTemplate airline checkin template
     * @return the {@code SendResponse}
     */
    SendResponse airlineCheckinTemplate(IdMessageRecipient recipient,
            AirlineCheckinTemplatePayload airlineCheckinTemplate);

    /**
     * Sends the given airline checkin template to the user.
     *
     * @param recipient the recipient
     * @param airlineCheckinTemplate airline checkin template
     * @return the {@code SendResponse}
     */
    SendResponse airlineCheckinTemplate(PhoneMessageRecipient recipient,
            AirlineCheckinTemplatePayload airlineCheckinTemplate);

    /**
     * Sends the given airline boarding pass template to the user.
     *
     * @param recipient the recipient
     * @param airlineBoardingPassTemplate airline boarding pass template
     * @return the {@code SendResponse}
     */
    SendResponse airlineBoardingPassTemplate(IdMessageRecipient recipient,
            AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate);

    /**
     * Sends the given airline boarding pass template to the user.
     *
     * @param recipient the recipient
     * @param airlineBoardingPassTemplate airline boarding pass template
     * @return the {@code SendResponse}
     */
    SendResponse airlineBoardingPassTemplate(PhoneMessageRecipient recipient,
            AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate);

    /**
     * Sends the given airline update template to the user.
     *
     * @param recipient the recipient
     * @param airlineUpdateTemplate airline update template
     * @return the {@code SendResponse}
     */
    SendResponse airlineUpdateTemplate(IdMessageRecipient recipient,
            AirlineUpdateTemplatePayload airlineUpdateTemplate);

    /**
     * Sends the given airline update template to the user.
     *
     * @param recipient the recipient
     * @param airlineUpdateTemplate airline update template
     * @return the {@code SendResponse}
     */
    SendResponse airlineUpdateTemplate(PhoneMessageRecipient recipient,
            AirlineUpdateTemplatePayload airlineUpdateTemplate);
}
