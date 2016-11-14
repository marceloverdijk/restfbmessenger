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
import com.restfb.types.send.ListTemplatePayload;
import com.restfb.types.send.ListViewElement;
import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.MessageRecipient;
import com.restfb.types.send.PostbackButton;
import com.restfb.types.send.QuickReply;
import com.restfb.types.send.ReceiptSummary;
import com.restfb.types.send.ReceiptTemplatePayload;
import com.restfb.types.send.SendResponse;
import com.restfb.types.send.airline.AirlineBoardingPassTemplatePayload;
import com.restfb.types.send.airline.AirlineCheckinTemplatePayload;
import com.restfb.types.send.airline.AirlineItineraryTemplatePayload;
import com.restfb.types.send.airline.AirlineUpdateTemplatePayload;
import com.restfb.types.send.airline.BoardingPass;
import com.restfb.types.send.airline.FlightAirport;
import com.restfb.types.send.airline.FlightInfo;
import com.restfb.types.send.airline.FlightSchedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.MESSAGES_PATH;
import static com.github.marsbits.restfbmessenger.send.DefaultSendOperations.RECIPIENT_PARAM_NAME;
import static org.mockito.Mockito.verify;

/**
 * Abstract class containing helpers for Send API related tests.
 *
 * @author Marcel Overdijk
 */
public abstract class AbstractSendOperationsTests {

    protected FacebookClient facebookClient;

    protected void verifySend(MessageRecipient recipient, Parameter... parameters) {
        verifySend(Parameter.with(RECIPIENT_PARAM_NAME, recipient), parameters);
    }

    protected void verifySend(Parameter recipient, Parameter... parameters) {
        List<Parameter> params = new ArrayList<>();
        params.add(recipient);
        params.addAll(Arrays.asList(parameters));
        verify(facebookClient).publish(MESSAGES_PATH, SendResponse.class, params.toArray(new Parameter[params.size()]));
    }

    protected MediaAttachment createMediaAttachment() {
        return new MediaAttachment(MediaAttachment.Type.IMAGE, "http://localhost");
    }

    protected List<QuickReply> createQuickReplies() {
        List<QuickReply> quickReplies = Arrays.asList(
                new QuickReply("title 1", "payload 1"),
                new QuickReply("title 2", "payload 2"),
                new QuickReply());
        return quickReplies;
    }

    protected ButtonTemplatePayload createButtonTemplate() {
        ButtonTemplatePayload buttonTemplate = new ButtonTemplatePayload("body text");
        buttonTemplate.addButton(new CallButton("title", "phonenumber"));
        buttonTemplate.addButton(new PostbackButton("title", "postback"));
        return buttonTemplate;
    }

    protected GenericTemplatePayload createGenericTemplate() {
        GenericTemplatePayload genericTemplate = new GenericTemplatePayload();
        genericTemplate.addBubble(new Bubble("bubble 1"));
        genericTemplate.addBubble(new Bubble("bubble 2"));
        return genericTemplate;
    }

    protected ListTemplatePayload createListTemplate() {
        ListViewElement element1 = new ListViewElement("title 1");
        ListViewElement element2 = new ListViewElement("title 2");
        List<ListViewElement> listViewElements = Arrays.asList(element1, element2);
        ListTemplatePayload listTemplate = new ListTemplatePayload(listViewElements);
        return listTemplate;
    }

    protected ReceiptTemplatePayload createReceiptTemplate() {
        String recipientName = "recipient name";
        String orderNumber = "order number";
        String currency = "currency";
        String paymentMethod = "payment method";
        ReceiptSummary summary = new ReceiptSummary(100.00);
        ReceiptTemplatePayload receiptTemplate = new ReceiptTemplatePayload(recipientName, orderNumber, currency, paymentMethod, summary);
        return receiptTemplate;
    }

    protected AirlineItineraryTemplatePayload createAirlineItineraryTemplate() {
        String introMessage = "intro message";
        String locale = "locale";
        String pnrNumber = "pnr number";
        double totalPrice = 100.00;
        AirlineItineraryTemplatePayload airlineItineraryTemplate =
                new AirlineItineraryTemplatePayload(introMessage, locale, pnrNumber, totalPrice);
        return airlineItineraryTemplate;
    }

    protected AirlineCheckinTemplatePayload createAirlineCheckinTemplate() {
        String introMessage = "intro message";
        String locale = "locale";
        String pnrNumber = "pnr number";
        String checkinUrl = "checkin url";
        AirlineCheckinTemplatePayload airlineCheckinTemplate =
                new AirlineCheckinTemplatePayload(introMessage, locale, pnrNumber, checkinUrl);
        airlineCheckinTemplate.addFlightInfo(createFlightInfo());
        return airlineCheckinTemplate;
    }

    protected FlightInfo createFlightInfo() {
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

    protected AirlineBoardingPassTemplatePayload createAirlineBoardingPassTemplate() {
        String introMessage = "intro message";
        ;
        String locale = "locale";
        List<BoardingPass> boardingPassList = Arrays.asList(createBoardingPass());
        AirlineBoardingPassTemplatePayload airlineBoardingPassTemplate =
                new AirlineBoardingPassTemplatePayload(introMessage, locale, boardingPassList);
        return airlineBoardingPassTemplate;
    }

    protected BoardingPass createBoardingPass() {
        String passengerName = "passenger name";
        String pnrNumber = "pnr number";
        String logoImageUrl = "logo image url";
        String aboveBarCodeImageUrl = "above bar code image url";
        FlightInfo flightInfo = createFlightInfo();
        BoardingPass boardingPass = new BoardingPass(passengerName, pnrNumber, logoImageUrl, aboveBarCodeImageUrl, flightInfo);
        return boardingPass;
    }

    protected AirlineUpdateTemplatePayload createAirlineUpdateTemplate() {
        String locale = "locale";
        String pnrNumber = "pnr number";
        FlightInfo updateFlightInfo = createFlightInfo();
        AirlineUpdateTemplatePayload airlineUpdateTemplate = new AirlineUpdateTemplatePayload(locale, pnrNumber, updateFlightInfo);
        return airlineUpdateTemplate;
    }
}
