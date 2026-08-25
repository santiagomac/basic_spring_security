package com.santiagomac.auth.application.usecases;

import com.santiagomac.auth.application.dto.AuthRequest;
import com.santiagomac.auth.application.dto.AuthResponse;
import com.santiagomac.auth.domain.model.session.Session;
import com.santiagomac.auth.domain.model.session.SessionGateway;
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
    private final SessionGateway sessionGateway;

    public AuthResponse authenticate(AuthRequest authRequest) {
        var token = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        String accessToken = jwtTokenService.generateAccessToken(authentication, true);
        String refreshToken = jwtTokenService.generateAccessToken(authentication, false);

        Session session = Session.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();

        Session sessionCreated = sessionGateway.createSession(session);

        return AuthResponse.
                builder()
                .accessToken(sessionCreated.getAccessToken())
                .refreshToken(sessionCreated.getRefreshToken())
                .build();
    }
}
