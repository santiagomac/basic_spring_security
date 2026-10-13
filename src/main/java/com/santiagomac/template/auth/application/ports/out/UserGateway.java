package com.santiagomac.template.auth.application.ports.out;

import com.santiagomac.template.auth.domain.model.user.UserModel;

import java.util.Optional;

public interface UserGateway {
    Optional<UserModel> findByEmail(String email);

    UserModel save(UserModel userModel);
}
