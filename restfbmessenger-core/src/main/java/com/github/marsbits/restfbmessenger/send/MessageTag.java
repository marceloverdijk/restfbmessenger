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

import static java.util.Objects.requireNonNull;

/**
 * Represents the
 * <a href="https://developers.facebook.com/docs/messenger-platform/send-api-reference/tags">message tags</a>.
 *
 * @author Marcel Overdijk
 * @since 1.3.0
 */
public class MessageTag {

    public static final MessageTag PAIRING_UPDATE = new MessageTag("PAIRING_UPDATE");
    public static final MessageTag APPLICATION_UPDATE = new MessageTag("APPLICATION_UPDATE");
    public static final MessageTag ACCOUNT_UPDATE = new MessageTag("ACCOUNT_UPDATE");
    public static final MessageTag PAYMENT_UPDATE = new MessageTag("PAYMENT_UPDATE");
    public static final MessageTag PERSONAL_FINANCE_UPDATE = new MessageTag("PERSONAL_FINANCE_UPDATE");
    public static final MessageTag SHIPPING_UPDATE = new MessageTag("SHIPPING_UPDATE");
    public static final MessageTag RESERVATION_UPDATE = new MessageTag("RESERVATION_UPDATE");
    public static final MessageTag ISSUE_RESOLUTION = new MessageTag("ISSUE_RESOLUTION");
    public static final MessageTag APPOINTMENT_UPDATE = new MessageTag("APPOINTMENT_UPDATE");
    public static final MessageTag GAME_EVENT = new MessageTag("GAME_EVENT");
    public static final MessageTag TRANSPORTATION_UPDATE = new MessageTag("TRANSPORTATION_UPDATE");
    public static final MessageTag FEATURE_FUNCTIONALITY_UPDATE = new MessageTag("FEATURE_FUNCTIONALITY_UPDATE");
    public static final MessageTag TICKET_UPDATE = new MessageTag("TICKET_UPDATE");

    private final String tag;

    public MessageTag(String tag) {
        this.tag = requireNonNull(tag, "'tag' must not be null");
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return tag;
    }
}
