package com.santiagomac.template.auth.application.usecases;

import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import com.santiagomac.template.auth.application.dto.AuthRequest;
import com.santiagomac.template.auth.application.dto.AuthResponse;
import com.santiagomac.template.auth.application.ports.out.JwtTokenPort;
import com.santiagomac.template.auth.application.ports.out.PasswordPort;
import com.santiagomac.template.auth.application.ports.out.RoleGateway;
import com.santiagomac.template.auth.application.ports.out.SessionGateway;
import com.santiagomac.template.auth.application.ports.out.UserGateway;
import com.santiagomac.template.auth.domain.model.exceptions.InvalidCredentials;
import com.santiagomac.template.auth.domain.model.session.Session;
import com.santiagomac.template.auth.domain.model.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnAuthEnabled
@RequiredArgsConstructor
public class LoginUseCase {

    private final JwtTokenPort jwtTokenPort;
    private final PasswordPort passwordPort;
    private final UserGateway userGateway;
    private final RoleGateway roleGateway;
    private final SessionGateway sessionGateway;

    @Transactional
    public AuthResponse authenticate(AuthRequest request) {
        UserModel user = userGateway.findByEmail(UserModel.normalizeEmail(request.email()))
                .filter(UserModel::isEnabled)
                .filter(u -> passwordPort.matches(request.password(), u.getPassword()))
                .orElseThrow(InvalidCredentials::new);

        String roles = "ROLE_" + roleGateway.getRole(user.getRoleId()).name();
        String accessToken = jwtTokenPort.generateAccessToken(user.getEmail(), roles);
        String refreshToken = jwtTokenPort.generateRefreshToken(user.getEmail(), roles);

        sessionGateway.save(Session.start(user.getId(), refreshToken));
        return new AuthResponse(accessToken, refreshToken);
    }
}
