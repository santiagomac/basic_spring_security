package com.santiagomac.template.auth.domain.model.exceptions;

import com.santiagomac.template.common.exception.UnauthorizedException;

public class InvalidRefreshToken extends UnauthorizedException {
    public InvalidRefreshToken(String message) {
        super(message);
    }
}
