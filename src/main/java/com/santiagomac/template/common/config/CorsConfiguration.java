package com.santiagomac.template.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfiguration {

    @Bean
    CorsConfigurationSource corsConfigurationSource(CorsProperties properties) {
        var source = new UrlBasedCorsConfigurationSource();
        if (!properties.allowedOrigins().isEmpty()) {
            var configuration = new org.springframework.web.cors.CorsConfiguration();
            configuration.setAllowedOrigins(properties.allowedOrigins());
            configuration.setAllowedMethods(properties.allowedMethods());
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(true);
            source.registerCorsConfiguration("/**", configuration);
        }
        return source;
    }
}
