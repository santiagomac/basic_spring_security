package com.santiagomac.template.auth.application.ports.out;

import com.santiagomac.template.auth.domain.model.session.Session;

import java.util.Optional;

public interface SessionGateway {

    Session save(Session session);

    Optional<Session> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
