package com.santiagomac.template.auth.application.ports.out;

public interface JwtTokenPort {
    String generateAccessToken(String subject, String roles);

    String generateRefreshToken(String subject, String roles);

    /** True only for a well-formed, signed, unexpired token issued as a refresh token. */
    boolean isValidRefreshToken(String token);

    String getSubjectFromToken(String token);
}
