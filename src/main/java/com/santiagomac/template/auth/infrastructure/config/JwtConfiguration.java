package com.santiagomac.template.auth.infrastructure.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@ConditionalOnAuthEnabled
public class JwtConfiguration {

    private static final int MIN_SECRET_BYTES = 32; // HS256 needs a 256-bit key

    private final byte[] secret;
    private final String issuer;

    public JwtConfiguration(AuthProperties properties) {
        String key = properties.jwt().secret();
        if (key == null || key.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "APP_AUTH_JWT_SECRET must be set and at least " + MIN_SECRET_BYTES + " bytes long");
        }
        this.secret = key.getBytes(StandardCharsets.UTF_8);
        this.issuer = properties.jwt().issuer();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secret));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(new SecretKeySpec(secret, "HmacSHA256"))
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(), new JwtIssuerValidator(issuer)));
        return decoder;
    }
}
