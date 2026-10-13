package com.santiagomac.template.auth.domain.model.exceptions;

import com.santiagomac.template.common.exception.UnauthorizedException;

public class RefreshTokenNotFound extends UnauthorizedException {
    public RefreshTokenNotFound(String message) {
        super(message);
    }
}
