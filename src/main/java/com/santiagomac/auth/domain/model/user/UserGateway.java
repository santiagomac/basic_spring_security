package com.santiagomac.auth.domain.model.user;

import com.santiagomac.auth.infrastructure.driven_adapter.jpa.user.UserEntity;

import java.util.Optional;

public interface UserGateway {
    Optional<UserModel> findByEmail(String email);

    UserEntity save(UserEntity userEntity);
}
