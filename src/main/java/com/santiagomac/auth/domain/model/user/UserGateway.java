package com.santiagomac.auth.domain.model.user;

import java.util.Optional;

public interface UserGateway {
    Optional<UserModel> findByEmail(String email);

    UserModel save(UserModel userModel);
}
