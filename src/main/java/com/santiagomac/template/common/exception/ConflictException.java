package com.santiagomac.template.common.exception;

/** The operation conflicts with the current state, e.g. duplicated data (HTTP 409). */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
