package com.mirumagency.uhc.premember.core.converters;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Token {
    public static final Delimiters DELIMITERS_PLAINTEXT = new Delimiters("{", "}");
    public static final Delimiters DELIMITERS_ENCODED = new Delimiters("%7B", "%7D");
    public static final Delimiters DELIMITERS_SPECIAL = new Delimiters("#[", "]");

    private final String name;
    private final Delimiters delimiters;

    public String getNameWrappedWithDelimiters() {
        return delimiters.getStartSymbol() + name + delimiters.getEndSymbol();
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Delimiters {
        private final String startSymbol;
        private final String endSymbol;
    }
}
