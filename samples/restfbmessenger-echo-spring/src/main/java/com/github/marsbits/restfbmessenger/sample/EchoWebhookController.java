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

package com.github.marsbits.restfbmessenger.sample;

import static com.github.marsbits.restfbmessenger.Messenger.*;
import static java.lang.String.format;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.marsbits.restfbmessenger.Messenger;

/**
 * The Echo webhook controller.
 *
 * @author Marcel Overdijk
 */
@Controller
@RequestMapping("/webhook")
public class EchoWebhookController {

    private static final Logger logger = Logger.getLogger(EchoWebhookController.class.getName());

    @Autowired
    private Messenger messenger;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> get(
            @RequestParam(HUB_MODE_PARAM_NAME) String mode,
            @RequestParam(HUB_VERIFY_TOKEN_PARAM_NAME) String verifyToken,
            @RequestParam(HUB_CHALLENGE_PARAM_NAME) String challenge) {
        logger.info("Validating webhook...");
        if (HUB_MODE_SUBSCRIBE_VALUE.equals(mode) && messenger.verifyToken(verifyToken)) {
            logger.info("Validating webhook succeeded");
            return new ResponseEntity<>(challenge, HttpStatus.OK);
        } else {
            logger.warning("Validating webhook failed");
            return new ResponseEntity<>("Failed validation. Make sure the validation tokens match.",
                    HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public void post(
            @RequestHeader(HUB_SIGNATURE_HEADER_NAME) String signature,
            @RequestBody String payload) {
        logger.info("Webhook received");
        try {
            messenger.handleCallback(payload, signature);
        } catch (Exception e) {
            logger.severe(format("Exception handling webhook: %s", e.getMessage()));
            throw e;
        }
    }
}
