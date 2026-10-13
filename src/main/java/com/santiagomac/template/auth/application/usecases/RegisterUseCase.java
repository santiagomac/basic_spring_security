package com.santiagomac.template.auth.application.usecases;

import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import com.santiagomac.template.auth.application.dto.RegisterRequest;
import com.santiagomac.template.auth.application.dto.RegisterResponse;
import com.santiagomac.template.auth.application.ports.out.PasswordPort;
import com.santiagomac.template.auth.application.ports.out.RoleGateway;
import com.santiagomac.template.auth.application.ports.out.UserGateway;
import com.santiagomac.template.auth.domain.model.user.RoleEnum;
import com.santiagomac.template.auth.domain.model.user.UserModel;
import com.santiagomac.template.common.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnAuthEnabled
@RequiredArgsConstructor
public class RegisterUseCase {

    private final PasswordPort passwordPort;
    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    @Transactional
    public RegisterResponse signup(RegisterRequest request) {
        UserModel.validatePlainPassword(request.password());

        String email = UserModel.normalizeEmail(request.email());
        if (userGateway.findByEmail(email).isPresent()) {
            throw new ConflictException("Email already registered");
        }

        var newUser = UserModel.createNewUser(
                email,
                passwordPort.encryptPassword(request.password()),
                roleGateway.getRoleId(RoleEnum.USER));

        var created = userGateway.save(newUser);
        return new RegisterResponse(created.getId(), created.getEmail());
    }
}
