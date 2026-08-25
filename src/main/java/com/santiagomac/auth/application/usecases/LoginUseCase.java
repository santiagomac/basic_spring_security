package com.santiagomac.auth.application.usecases;

import com.santiagomac.auth.application.dto.AuthRequest;
import com.santiagomac.auth.application.dto.AuthResponse;
import com.santiagomac.auth.application.ports.out.JwtTokenPort;
import com.santiagomac.auth.domain.model.session.Session;
import com.santiagomac.auth.domain.model.session.SessionGateway;
import com.santiagomac.auth.domain.model.user.UserGateway;
import com.santiagomac.auth.domain.model.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenPort jwtTokenPort;
    private final UserGateway userGateway;
    private final SessionGateway sessionGateway;

    public AuthResponse authenticate(AuthRequest authRequest) {
        var token = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        String accessToken = jwtTokenPort.generateAccessToken(authentication.getName(), scope);
        String refreshToken = jwtTokenPort.generateRefreshToken(authentication.getName(), scope);

        Optional<UserModel> user = userGateway.findByEmail(authRequest.getEmail());

        Session session = Session.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .userId(user.get().getId())
                .build();

        Session sessionCreated = sessionGateway.createSession(session);

        return AuthResponse.
                builder()
                .accessToken(sessionCreated.getAccessToken())
                .refreshToken(sessionCreated.getRefreshToken())
                .build();
    }
}
