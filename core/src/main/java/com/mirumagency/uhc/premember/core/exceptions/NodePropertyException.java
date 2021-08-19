package com.mirumagency.uhc.premember.core.exceptions;

/**
 * Unchecked exception for JCR node's property processing failures
 */
public class NodePropertyException extends RuntimeException {

    public NodePropertyException() {
    }

    public NodePropertyException(String message) {
        super(message);
    }

    public NodePropertyException(Throwable cause) {
        super(cause);
    }
}
