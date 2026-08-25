package com.santiagomac.auth.infrastructure.driven_adapter.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String generateToken(Authentication authentication, boolean isAccessToken) {
        Instant now = Instant.now();
        String scope = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth_service")
                .issuedAt(now)
                .expiresAt(isAccessToken ? now.plus(15, ChronoUnit.MINUTES) : now.plus(7, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("roles", scope)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return this.jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }

    public boolean isValidToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);

            return jwt.getExpiresAt() != null && jwt.getExpiresAt().isAfter(Instant.now());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);

            return jwt.getClaims()
                    .get("sub")
                    .toString();
        } catch (JwtException | IllegalArgumentException e) {
            throw e;
        }
    }

}
