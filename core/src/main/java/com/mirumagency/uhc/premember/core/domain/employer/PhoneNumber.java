package com.mirumagency.uhc.premember.core.domain.employer;

import org.apache.http.util.Asserts;

import static org.apache.http.util.Asserts.notNull;

public class PhoneNumber {
    private final String label;
    private final String number;

    private PhoneNumber(String phoneNumber) {
        this.label = phoneNumber;
        this.number = phoneNumber;
    }

    public static PhoneNumber of(String number) {
        notNull(number, "number");
        return new PhoneNumber(number);
    }

    public static PhoneNumber empty() {
        return new PhoneNumber("");
    }

    public String getLabel() {
        return label;
    }

    public String getNumber() {
        return number;
    }
}
