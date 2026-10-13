package com.santiagomac.template.auth.domain.model.exceptions;

import com.santiagomac.template.common.exception.UnauthorizedException;

/** Same message for unknown user, wrong password or disabled account to avoid user enumeration. */
public class InvalidCredentials extends UnauthorizedException {
    public InvalidCredentials() {
        super("Invalid credentials");
    }
}
