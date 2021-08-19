package com.mirumagency.uhc.premember.core.domain.employer;

import com.google.common.collect.Lists;

import java.util.List;

import static org.apache.http.util.Asserts.notNull;

public class ContactLink {
    private static final List<String> VALID_PREFIXES = Lists.newArrayList("tel:", "mailto:", "http://", "https://");

    private final String link;

    public static ContactLink of(String contactLink) {
        notNull(contactLink, "contactLink");
        return new ContactLink(contactLink);
    }

    public static ContactLink empty() {
        return new ContactLink("");
    }

    private ContactLink(String contactLink) {
        this.link = contactLink;
    }

    private static boolean isValid(String contactLink) {
        notNull(contactLink, "contactLink");
        return contactLink.isEmpty() || startsWithValidPrefix(contactLink);
    }

    private static boolean startsWithValidPrefix(String contactLink) {
        return VALID_PREFIXES.stream().anyMatch(contactLink::startsWith);
    }

    public String getLink() {
        return link;
    }
}
