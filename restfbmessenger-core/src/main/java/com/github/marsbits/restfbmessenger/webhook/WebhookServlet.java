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

package com.github.marsbits.restfbmessenger.webhook;

import com.github.marsbits.restfbmessenger.Messenger;
import com.github.marsbits.restfbmessenger.MessengerProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.marsbits.restfbmessenger.Messenger.HUB_CHALLENGE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.Messenger.HUB_MODE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.Messenger.HUB_MODE_SUBSCRIBE_VALUE;
import static com.github.marsbits.restfbmessenger.Messenger.HUB_SIGNATURE_HEADER_NAME;
import static com.github.marsbits.restfbmessenger.Messenger.HUB_VERIFY_TOKEN_PARAM_NAME;
import static java.lang.String.format;

/**
 * Servlet that is responsible for receiving the Facebook Messenger webhook callbacks and delegating them to the configured {@link
 * Messenger} instance.
 *
 * @author Marcel Overdijk
 * @see Messenger#handleCallback(String, String)
 * @see CallbackHandler
 * @since 1.0.0
 */
public class WebhookServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(WebhookServlet.class.getName());

    /**
     * The {@code messengerProviderClass} servlet init parameter name.
     */
    public static final String MESSENGER_PROVIDER_CLASS_PARAM_NAME = "messengerProviderClass";

    private Messenger messenger;

    public WebhookServlet() {
    }

    public WebhookServlet(Messenger messenger) {
        this.messenger = messenger;
    }

    @Override
    public void init() throws ServletException {
        logger.info("Initializing webhook servlet...");
        if (messenger == null) {
            String providerClass = getServletConfig().getInitParameter(MESSENGER_PROVIDER_CLASS_PARAM_NAME);
            if (providerClass == null || providerClass.length() == 0) {
                logger.severe(format("Servlet init parameter not provided: %s", MESSENGER_PROVIDER_CLASS_PARAM_NAME));
            } else {
                try {
                    messenger = ((MessengerProvider) Class.forName(providerClass).newInstance()).getMessenger();
                } catch (ClassNotFoundException e) {
                    logger.severe("Messenger provider class not found: " + providerClass);
                } catch (InstantiationException e) {
                    logger.severe("Could not instantiate messenger provider class: " + providerClass);
                } catch (IllegalAccessException e) {
                    logger.severe("Could not instantiate (illegal access) messenger provider class: " + providerClass);
                }
            }
        }
    }

    /**
     * Processes the webhook validation {@code GET} request.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Validating webhook...");
        if (HUB_MODE_SUBSCRIBE_VALUE.equals(req.getParameter(HUB_MODE_PARAM_NAME)) &&
                messenger.verifyToken(req.getParameter(HUB_VERIFY_TOKEN_PARAM_NAME))) {
            logger.info("Validating webhook succeeded");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(req.getParameter(HUB_CHALLENGE_PARAM_NAME));
        } else {
            logger.warning("Validating webhook failed");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("Failed validation. Make sure the validation tokens match.");
        }
    }

    /**
     * Processes the webhook callback {@code POST} request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Webhook received");
        try {
            String payload = getRequestBodyAsString(req);
            String signature = req.getHeader(HUB_SIGNATURE_HEADER_NAME);
            messenger.handleCallback(payload, signature);
        } catch (Exception e) {
            logger.severe(format("Exception handling webhook: %s", e.getMessage()));
            throw e;
        }
    }

    /**
     * Retrieves the request body from the given request.
     */
    protected String getRequestBodyAsString(HttpServletRequest req) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    /**
     * Returns the {@code Messenger} instance.
     */
    protected Messenger getMessenger() {
        return messenger;
    }

    /**
     * Sets the {@code Messenger} instance.
     */
    protected void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }
}
