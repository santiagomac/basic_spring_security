package com.santiagomac.template.common.exception;

/** The caller could not be authenticated (HTTP 401). */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
