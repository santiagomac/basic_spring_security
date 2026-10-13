package com.santiagomac.template.auth.domain.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserModel {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 72; // BCrypt ignores anything beyond 72 bytes

    private UUID id;
    private String email;
    private String password;
    private boolean enabled;
    private UUID roleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserModel(UUID id, String email, String password, boolean enabled, UUID roleId,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** The password must already be encoded; validate the raw one with validatePlainPassword first. */
    public static UserModel createNewUser(String email, String passwordHash, UUID roleId) {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        var user = new UserModel();
        user.email = normalizeEmail(email);
        user.password = passwordHash;
        user.roleId = roleId;
        user.enabled = true;
        return user;
    }

    public static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    public static void validatePlainPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters");
        }
    }
}
