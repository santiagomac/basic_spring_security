package com.santiagomac.auth.infrastructure.driven_adapter.jpa.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, String> {
    Optional<SessionEntity> findByRefreshToken(String refreshToken);
}
