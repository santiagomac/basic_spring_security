package com.santiagomac.template.auth.domain.model.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

/** A refresh-token session. Only the SHA-256 of the refresh token is kept, never the token itself. */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Session {
    private UUID id;
    private String refreshTokenHash;
    private UUID userId;

    public static Session start(UUID userId, String refreshToken) {
        return Session.builder().userId(userId).refreshTokenHash(hash(refreshToken)).build();
    }

    /** Refresh token rotation: the previous refresh token stops working. */
    public void rotate(String newRefreshToken) {
        this.refreshTokenHash = hash(newRefreshToken);
    }

    public static String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
