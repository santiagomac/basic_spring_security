package com.santiagomac.auth.application.usecases;

import com.santiagomac.auth.application.dto.AuthRequest;
import com.santiagomac.auth.application.dto.AuthResponse;
import com.santiagomac.auth.infrastructure.driven_adapter.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthResponse authenticate(AuthRequest authRequest) {
        var token = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        String jwtToken = jwtTokenService.generateToken(authentication);
        Long expiresAt = jwtTokenService.extractExpirationTime(jwtToken);

        return AuthResponse.
                builder()
                .token(jwtToken)
                .username(authentication.getName())
                .expiresAt(expiresAt)
                .build();
    }
}
