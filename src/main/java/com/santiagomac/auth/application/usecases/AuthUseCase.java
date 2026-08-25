package com.santiagomac.auth.application.usecases;

import com.santiagomac.auth.application.dto.AuthResponse;
import com.santiagomac.auth.domain.model.exceptions.InvalidRefreshToken;
import com.santiagomac.auth.domain.model.exceptions.NotFoundException;
import com.santiagomac.auth.domain.model.exceptions.RefreshTokenNotFound;
import com.santiagomac.auth.domain.model.session.Session;
import com.santiagomac.auth.domain.model.session.SessionGateway;
import com.santiagomac.auth.domain.model.user.RoleEnum;
import com.santiagomac.auth.domain.model.user.RoleGateway;
import com.santiagomac.auth.domain.model.user.UserGateway;
import com.santiagomac.auth.domain.model.user.UserModel;
import com.santiagomac.auth.infrastructure.driven_adapter.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUseCase {

    private final SessionGateway sessionGateway;
    private final JwtTokenService jwtTokenService;
    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    public AuthResponse refreshToken(String refreshToken) {
        Optional<Session> optionalSession = this.sessionGateway.findByRefreshToken(refreshToken);
        if (optionalSession.isEmpty()) {
            throw new RefreshTokenNotFound("The provided refresh token not exists");
        }

        Session session = optionalSession.get();
        if (!jwtTokenService.isValidToken(session.getRefreshToken())) {
            throw new InvalidRefreshToken("The refresh token is invalid");
        }

        String email = jwtTokenService.getSubjectFromToken(session.getRefreshToken());

        UserModel user = userGateway.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        RoleEnum role = roleGateway.getRole(user.getRoleId());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email, null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );

        String accessToken = jwtTokenService.generateToken(authentication, true);

        session.setAccessToken(accessToken);
        sessionGateway.updateSession(session);

        return AuthResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }
}
