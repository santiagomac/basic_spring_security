package com.santiagomac.auth.domain.model.session;

import java.util.Optional;

public interface SessionGateway {

    Session createSession(Session session);

    Optional<Session> findByRefreshToken(String refreshToken);

    void updateSession(Session session);
}
