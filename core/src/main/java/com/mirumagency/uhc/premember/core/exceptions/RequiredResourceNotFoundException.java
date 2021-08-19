package com.mirumagency.uhc.premember.core.exceptions;

public class RequiredResourceNotFoundException extends RuntimeException {
    public RequiredResourceNotFoundException(String message) {
        super(message);
    }
}
