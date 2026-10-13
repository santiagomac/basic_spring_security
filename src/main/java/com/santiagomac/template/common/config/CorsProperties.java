package com.santiagomac.template.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/** CORS is disabled unless {@code app.cors.allowed-origins} (APP_CORS_ALLOWED_ORIGINS) lists origins. */
@ConfigurationProperties(prefix = "app.cors")
public record CorsProperties(List<String> allowedOrigins, List<String> allowedMethods) {

    public CorsProperties {
        allowedOrigins = allowedOrigins == null ? List.of() : allowedOrigins;
        allowedMethods = allowedMethods == null || allowedMethods.isEmpty()
                ? List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") : allowedMethods;
    }
}
