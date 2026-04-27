package com.tenpo.challenge.service.exceptions;

/**
 * Exception thrown when an external service fails.
 */
public class ExternalServiceException extends RuntimeException {

    /**
     * Constructs a new exception with the specified message.
     * @param message the detail message
     */
    public ExternalServiceException(final String message) {
        super(message);
    }
}
