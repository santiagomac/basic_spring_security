package com.santiagomac.coworkingbooking.domain.service;

import com.santiagomac.coworkingbooking.application.dto.LoginUserDto;
import com.santiagomac.coworkingbooking.application.dto.RegisterUserDto;
import com.santiagomac.coworkingbooking.domain.repository.UserGateway;
import com.santiagomac.coworkingbooking.infrastructure.driven_adapter.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserGateway userGateway;
    private final AuthenticationManager authenticationManager;

    public User signUp(RegisterUserDto input) {
        User user = User.builder()
                .fullName(input.getFullName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .build();

        return this.userGateway.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return this.userGateway.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
