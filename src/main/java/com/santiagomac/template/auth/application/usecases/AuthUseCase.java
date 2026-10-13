package com.santiagomac.template.auth.application.usecases;

import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import com.santiagomac.template.auth.application.dto.AuthResponse;
import com.santiagomac.template.auth.application.ports.out.JwtTokenPort;
import com.santiagomac.template.auth.application.ports.out.RoleGateway;
import com.santiagomac.template.auth.application.ports.out.SessionGateway;
import com.santiagomac.template.auth.application.ports.out.UserGateway;
import com.santiagomac.template.auth.domain.model.exceptions.InvalidRefreshToken;
import com.santiagomac.template.auth.domain.model.exceptions.RefreshTokenNotFound;
import com.santiagomac.template.auth.domain.model.session.Session;
import com.santiagomac.template.auth.domain.model.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnAuthEnabled
@RequiredArgsConstructor
public class AuthUseCase {

    private final SessionGateway sessionGateway;
    private final JwtTokenPort jwtTokenPort;
    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    /** Issues a new access token AND a new refresh token (rotation); the old refresh token is revoked. */
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenPort.isValidRefreshToken(refreshToken)) {
            throw new InvalidRefreshToken("The refresh token is invalid");
        }

        Session session = sessionGateway.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFound("The provided refresh token does not exist"));

        UserModel user = userGateway.findByEmail(jwtTokenPort.getSubjectFromToken(refreshToken))
                .filter(UserModel::isEnabled)
                .orElseThrow(() -> new InvalidRefreshToken("The refresh token is invalid"));

        String roles = "ROLE_" + roleGateway.getRole(user.getRoleId()).name();
        String accessToken = jwtTokenPort.generateAccessToken(user.getEmail(), roles);
        String newRefreshToken = jwtTokenPort.generateRefreshToken(user.getEmail(), roles);

        session.rotate(newRefreshToken);
        sessionGateway.save(session);

        return new AuthResponse(accessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        sessionGateway.deleteByRefreshToken(refreshToken);
    }
}
