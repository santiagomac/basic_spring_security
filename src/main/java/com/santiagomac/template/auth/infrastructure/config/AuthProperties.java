package com.santiagomac.template.auth.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.auth")
public record AuthProperties(
        @DefaultValue("false") boolean enabled,
        @DefaultValue Jwt jwt,
        @DefaultValue Cookie cookie) {

    public record Jwt(
            String secret,
            @DefaultValue("microservice") String issuer,
            @DefaultValue("15m") Duration accessTtl,
            @DefaultValue("7d") Duration refreshTtl) {
    }

    public record Cookie(
            @DefaultValue("true") boolean secure,
            @DefaultValue("Lax") String sameSite) {
    }
}
