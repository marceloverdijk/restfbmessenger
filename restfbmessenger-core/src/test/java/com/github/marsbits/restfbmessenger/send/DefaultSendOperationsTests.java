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
import com.restfb.types.send.Bubble;
import com.restfb.types.send.ButtonTemplatePayload;
import com.restfb.types.send.CallButton;
import com.restfb.types.send.GenericTemplatePayload;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.Message;
import com.restfb.types.send.PhoneMessageRecipient;
import com.restfb.types.send.PostbackButton;
import com.restfb.types.send.QuickReply;
import com.restfb.types.send.ReceiptSummary;
import com.restfb.types.send.ReceiptTemplatePayload;
import com.restfb.types.send.SendResponse;
import com.restfb.types.send.SenderActionEnum;
import com.restfb.types.send.TemplateAttachment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.MESSAGES_PATH;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.MESSAGE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.RECIPIENT_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.SENDER_ACTION_PARAM_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link DefaultSendOperations}.
 *
 * @author Marcel Overdijk
 */
@SuppressWarnings("Duplicates")
public class DefaultSendOperationsTests {

    private DefaultSendOperations send;
    private FacebookClient facebookClient;

    private IdMessageRecipient idMessageRecipient;
    private PhoneMessageRecipient phoneMessageRecipient;

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
    public void testSenderActionWithPhoneMessageRecipient() {
        SenderActionEnum senderAction = SenderActionEnum.mark_seen;
        send.senderAction(phoneMessageRecipient, senderAction);
        verifySend(phoneMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, senderAction));
    }

    @Test
    public void testMarkSeenWithIdMessageRecipient() {
        send.markSeen(idMessageRecipient);
        verifySend(idMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.mark_seen));
    }

    @Test
    public void testMarkSeenWithPhoneMessageRecipient() {
        send.markSeen(phoneMessageRecipient);
        verifySend(phoneMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.mark_seen));
    }

    @Test
    public void testTypingOnWithIdMessageRecipient() {
        send.typingOn(idMessageRecipient);
        verifySend(idMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_on));
    }

    @Test
    public void testTypingOnWithPhoneMessageRecipient() {
        send.typingOn(phoneMessageRecipient);
        verifySend(phoneMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_on));
    }

    @Test
    public void testTypingOffWithIdMessageRecipient() {
        send.typingOff(idMessageRecipient);
        verifySend(idMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_off));
    }

    @Test
    public void testTypingOffWithPhoneMessageRecipient() {
        send.typingOff(phoneMessageRecipient);
        verifySend(phoneMessageRecipient, Parameter.with(SENDER_ACTION_PARAM_NAME, SenderActionEnum.typing_off));
    }

    @Test
    public void testMessageWithIdMessageRecipient() {
        Message message = new Message("Hello!");
        send.message(idMessageRecipient, message);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testMessageWithPhoneMessageRecipient() {
        Message message = new Message("Hello!");
        send.message(phoneMessageRecipient, message);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWithIdMessageRecipient() {
        String text = "Hello!";
        send.textMessage(idMessageRecipient, text);
        Message message = new Message(text);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testTextMessageWithPhoneMessageRecipient() {
        String text = "Hello!";
        send.textMessage(phoneMessageRecipient, text);
        Message message = new Message(text);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testAttachmentWithPhoneMessageRecipient() {
        MediaAttachment.Type type = MediaAttachment.Type.IMAGE;
        String url = "http://localhost";
        send.attachment(phoneMessageRecipient, type, url);
        MediaAttachment attachment = new MediaAttachment(type, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testImageAttachmentWithPhoneMessageRecipient() {
        String url = "http://localhost";
        send.imageAttachment(phoneMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.IMAGE, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testAudioAttachmentWithPhoneMessageRecipient() {
        String url = "http://localhost";
        send.audioAttachment(phoneMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.AUDIO, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testVideoAttachmentWithPhoneMessageRecipient() {
        String url = "http://localhost";
        send.videoAttachment(phoneMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.VIDEO, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testFileAttachmentWithPhoneMessageRecipient() {
        String url = "http://localhost";
        send.fileAttachment(phoneMessageRecipient, url);
        MediaAttachment attachment = new MediaAttachment(MediaAttachment.Type.FILE, url);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testQuickRepliesWithTextMessageAndPhoneMessageRecipient() {
        String text = "Hello!";
        List<QuickReply> quickReplies = createQuickReplies();
        send.quickReplies(phoneMessageRecipient, text, quickReplies);
        Message message = new Message(text);
        message.addQuickReplies(quickReplies);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testGenericTemplateWithIdMessageRecipient() {
        GenericTemplatePayload genericTemplate = createGenericTemplate();
        send.genericTemplate(idMessageRecipient, genericTemplate);
        TemplateAttachment attachment = new TemplateAttachment(genericTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testButtonTemplateWithIdMessageRecipient() {
        ButtonTemplatePayload buttonTemplate = createButtonTemplate();
        send.buttonTemplate(idMessageRecipient, buttonTemplate);
        TemplateAttachment attachment = new TemplateAttachment(buttonTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testReceiptTemplateWithIdMessageRecipient() {
        ReceiptTemplatePayload receiptTemplate = createReceiptTemplate();
        send.receiptTemplate(idMessageRecipient, receiptTemplate);
        TemplateAttachment attachment = new TemplateAttachment(receiptTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testAirlineItineraryTemplateWithIdMessageRecipient() {
        AirlineItineraryTemplatePayload airlineItineraryTemplate = createAirlineItineraryTemplate();
        send.airlineItineraryTemplate(idMessageRecipient, airlineItineraryTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineItineraryTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testAirlineCheckinTemplateWithIdMessageRecipient() {
        AirlineCheckinTemplatePayload airlineCheckinTemplate = createAirlineCheckinTemplate();
        send.airlineCheckinTemplate(idMessageRecipient, airlineCheckinTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineCheckinTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testAirlineBoardingPassTemplateWithIdMessageRecipient() {
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                createAirlineBoardingPassTemplate();
        send.airlineBoardingPassTemplate(idMessageRecipient, airlineBoardingPassTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineBoardingPassTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
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
    public void testAirlineUpdateTemplateWithIdMessageRecipient() {
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(idMessageRecipient, airlineUpdateTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineUpdateTemplate);
        Message message = new Message(attachment);
        verifySend(idMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    @Test
    public void testAirlineUpdateTemplateWithPhoneMessageRecipient() {
        AirlineUpdateTemplatePayload airlineUpdateTemplate = createAirlineUpdateTemplate();
        send.airlineUpdateTemplate(phoneMessageRecipient, airlineUpdateTemplate);
        TemplateAttachment attachment = new TemplateAttachment(airlineUpdateTemplate);
        Message message = new Message(attachment);
        verifySend(phoneMessageRecipient, Parameter.with(MESSAGE_PARAM_NAME, message));
    }

    public void verifySend(IdMessageRecipient recipient, Parameter... parameters) {
        verifySend(Parameter.with(RECIPIENT_PARAM_NAME, recipient), parameters);
    }

    public void verifySend(PhoneMessageRecipient recipient, Parameter... parameters) {
        verifySend(Parameter.with(RECIPIENT_PARAM_NAME, recipient), parameters);
    }

    public void verifySend(Parameter recipient, Parameter... parameters) {
        List<Parameter> params = new ArrayList<>();
        params.add(recipient);
        params.addAll(Arrays.asList(parameters));
        verify(facebookClient).publish(MESSAGES_PATH, SendResponse.class, params.toArray(new Parameter[params.size()]));
    }

    private List<QuickReply> createQuickReplies() {
        List<QuickReply> quickReplies = Arrays.asList(
                new QuickReply("title 1", "payload 1"),
                new QuickReply("title 2", "payload 2"),
                new QuickReply());
        return quickReplies;
    }

    private GenericTemplatePayload createGenericTemplate() {
        GenericTemplatePayload genericTemplate = new GenericTemplatePayload();
        genericTemplate.addBubble(new Bubble("bubble 1"));
        genericTemplate.addBubble(new Bubble("bubble 2"));
        return genericTemplate;
    }

    private ButtonTemplatePayload createButtonTemplate() {
        ButtonTemplatePayload buttonTemplate = new ButtonTemplatePayload("body text");
        buttonTemplate.addButton(new CallButton("title", "phonenumber"));
        buttonTemplate.addButton(new PostbackButton("title", "postback"));
        return buttonTemplate;
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
