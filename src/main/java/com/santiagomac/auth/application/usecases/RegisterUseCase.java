package com.santiagomac.auth.application.usecases;

import com.santiagomac.auth.application.dto.RegisterRequest;
import com.santiagomac.auth.application.dto.RegisterResponse;
import com.santiagomac.auth.application.ports.out.PasswordPort;
import com.santiagomac.auth.domain.model.user.UserGateway;
import com.santiagomac.auth.infrastructure.driven_adapter.jpa.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUseCase {

    private final PasswordPort passwordPort;
    private final UserGateway userGateway;

    public RegisterResponse signup(RegisterRequest registerRequest) {
        var passwordEncrypted = this.passwordPort.encryptPassword(registerRequest.getPassword());
        var user = UserEntity
                .builder()
                .name(registerRequest.getName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncrypted)
                .phone(registerRequest.getPhone())
                .build();

        var userCreated = this.userGateway.save(user);
        return RegisterResponse
                .builder()
                .id(userCreated.getId())
                .email(userCreated.getEmail())
                .build();
    }
}
