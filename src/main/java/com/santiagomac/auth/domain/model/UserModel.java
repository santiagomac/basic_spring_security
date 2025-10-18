package com.santiagomac.auth.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserModel {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private String name;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserModel(String name, String lastName, String email, String password, String phone, Boolean isActive) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isActive = isActive;
    }

    public static UserModel createUser(String name, String lastName, String email, String password, String phone, Boolean isActive) {
        validEmail(email);
        validPassword(password);

        return new UserModel(name, lastName, email, password, phone, isActive);
    }

    private static void validEmail(String email) {
        var pattern = Pattern.compile(EMAIL_REGEX);
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }

    private static void validPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
}
