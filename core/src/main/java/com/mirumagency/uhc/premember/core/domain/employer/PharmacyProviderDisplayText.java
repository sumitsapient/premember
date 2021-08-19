package com.mirumagency.uhc.premember.core.domain.employer;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum PharmacyProviderDisplayText {
    TEXT("Pharmacy Provider Text 1"),
    TEXT2("Pharmacy Provider Text 2"),
    TEXT3("Pharmacy Provider Text 3"),
    NOT_SELECTED("Not selected");

    private final String name;

    PharmacyProviderDisplayText(String name) {
        this.name = name;
    }

    public static PharmacyProviderDisplayText of(String value) {
        return Arrays.stream(PharmacyProviderDisplayText.values())
                .filter(it -> value.toUpperCase(Locale.ENGLISH).equals(it.name()))
                .findFirst()
                .orElse(NOT_SELECTED);
    }
}

