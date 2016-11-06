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

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.marsbits.restfbmessenger.Messenger.HUB_MODE_PARAM_NAME;
import static com.github.marsbits.restfbmessenger.Messenger.HUB_MODE_SUBSCRIBE_VALUE;
import static com.github.marsbits.restfbmessenger.Messenger.HUB_SIGNATURE_HEADER_NAME;
import static com.github.marsbits.restfbmessenger.Messenger.HUB_VERIFY_TOKEN_PARAM_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link WebhookServlet}.
 *
 * @author Marcel Overdijk
 */
public class WebhookServletTests {

    private WebhookServlet servlet;

    private static Messenger messengerStatic;

    private Messenger messenger;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;
    private ServletConfig servletConfig;

    @Before
    public void setUp() throws Exception {
        messengerStatic = mock(Messenger.class);
        this.messenger = mock(Messenger.class);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.writer = mock(PrintWriter.class);
        this.servletConfig = mock(ServletConfig.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testConstructorWithMessenger() throws Exception {
        servlet = new WebhookServlet(messenger);
        assertThat(servlet.getMessenger(), is(sameInstance(messenger)));
        servlet.init(servletConfig);
        servlet.init(); // let's explicitly call init()
        assertThat(servlet.getMessenger(), is(sameInstance(messenger)));
    }

    @Test
    public void testInitWithMessengerProviderClassInitParam() throws Exception {
        when(servletConfig.getInitParameter(WebhookServlet.MESSENGER_PROVIDER_CLASS_PARAM_NAME))
                .thenReturn(MyMessengerProvider.class.getName());
        servlet = new WebhookServlet();
        servlet.init(servletConfig);
        assertThat(servlet.getMessenger(), is(sameInstance(messengerStatic)));
    }

    @Test
    public void testGetWithoutHubModeReturnsForbidden() throws Exception {
        servlet = new WebhookServlet();
        servlet.init(servletConfig);
        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testGetWithInvalidHubModeReturnsForbidden() throws Exception {
        when(request.getParameter(HUB_MODE_PARAM_NAME)).thenReturn("invalid");
        servlet = new WebhookServlet(messenger);
        servlet.init(servletConfig);
        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testGetWithInvalidVerifyTokenReturnsForbidden() throws Exception {
        when(request.getParameter(HUB_MODE_PARAM_NAME)).thenReturn(HUB_MODE_SUBSCRIBE_VALUE);
        when(request.getParameter(HUB_VERIFY_TOKEN_PARAM_NAME)).thenReturn("Sesame Open");
        when(messenger.verifyToken("Sesame Open")).thenReturn(false);
        servlet = new WebhookServlet(messenger);
        servlet.init(servletConfig);
        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testGetWithValidVerifyTokenReturnsOK() throws Exception {
        when(request.getParameter(HUB_MODE_PARAM_NAME)).thenReturn(HUB_MODE_SUBSCRIBE_VALUE);
        when(request.getParameter(HUB_VERIFY_TOKEN_PARAM_NAME)).thenReturn("Sesame Open");
        when(messenger.verifyToken("Sesame Open")).thenReturn(true);
        servlet = new WebhookServlet(messenger);
        servlet.init(servletConfig);
        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testPostHandleWebhook() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("the-payload")));
        when(request.getHeader(HUB_SIGNATURE_HEADER_NAME)).thenReturn("the-signature");
        servlet = new WebhookServlet(messenger);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
        verify(messenger).handleCallback("the-payload", "the-signature");
    }

    @Test
    public void testPostWithoutSignature() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("the-payload")));
        servlet = new WebhookServlet(messenger);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
        verify(messenger).handleCallback("the-payload", null);
    }

    public static class MyMessengerProvider implements MessengerProvider {

        @Override
        public Messenger getMessenger() {
            return messengerStatic;
        }
    }
}
