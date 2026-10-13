package com.santiagomac.template.auth.application.ports.out;

public interface PasswordPort {

    String encryptPassword(String password);

    boolean matches(String rawPassword, String encodedPassword);
}
