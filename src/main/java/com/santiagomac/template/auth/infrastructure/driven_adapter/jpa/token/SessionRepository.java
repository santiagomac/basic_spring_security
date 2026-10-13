package com.santiagomac.template.auth.infrastructure.driven_adapter.jpa.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {

    Optional<SessionEntity> findByRefreshTokenHash(String refreshTokenHash);

    void deleteByRefreshTokenHash(String refreshTokenHash);
}
