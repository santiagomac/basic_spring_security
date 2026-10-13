package com.santiagomac.template.auth.infrastructure.driven_adapter.jwt;

import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import com.santiagomac.template.auth.application.ports.out.JwtTokenPort;
import com.santiagomac.template.auth.infrastructure.config.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
@ConditionalOnAuthEnabled
@RequiredArgsConstructor
public class JwtTokenAdapter implements JwtTokenPort {

    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final AuthProperties properties;

    @Override
    public String generateAccessToken(String subject, String roles) {
        return generateToken(subject, roles, ACCESS, properties.jwt().accessTtl());
    }

    @Override
    public String generateRefreshToken(String subject, String roles) {
        return generateToken(subject, roles, REFRESH, properties.jwt().refreshTtl());
    }

    private String generateToken(String subject, String roles, String type, Duration ttl) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.jwt().issuer())
                .issuedAt(now)
                .expiresAt(now.plus(ttl))
                .id(UUID.randomUUID().toString()) // unique per token, even when issued in the same second
                .subject(subject)
                .claim("roles", roles)
                .claim(TOKEN_TYPE, type)
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();
    }

    @Override
    public boolean isValidRefreshToken(String token) {
        try {
            return REFRESH.equals(jwtDecoder.decode(token).getClaimAsString(TOKEN_TYPE));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String getSubjectFromToken(String token) {
        return jwtDecoder.decode(token).getSubject();
    }
}
