package com.santiagomac.coworkingbooking.domain.repository;

import com.santiagomac.coworkingbooking.infrastructure.driven_adapter.entity.User;

import java.util.Optional;

public interface UserGateway {
    Optional<User> findByEmail(String email);

    User save(User user);
}
