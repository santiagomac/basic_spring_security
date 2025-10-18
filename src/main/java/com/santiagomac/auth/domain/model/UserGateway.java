package com.santiagomac.auth.domain.model;

import com.santiagomac.auth.infrastructure.driven_adapter.jpa.user.User;

import java.util.Optional;

public interface UserGateway {
    Optional<User> findByEmail(String email);

    User save(User user);
}
