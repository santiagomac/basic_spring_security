package com.santiagomac.auth.infrastructure.driven_adapter.jpa.token;

import com.santiagomac.auth.domain.model.session.Session;
import com.santiagomac.auth.domain.model.session.SessionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionEntityAdapter implements SessionGateway {
    private final SessionRepository sessionRepository;

    @Override
    public Session createSession(Session session) {
        SessionEntity entity = toEntity(session);
        return toModel(sessionRepository.save(entity));
    }

    private SessionEntity toEntity(Session session) {
        return SessionEntity.builder()
                .refreshToken(session.getRefreshToken())
                .accessToken(session.getAccessToken())
                .build();
    }

    private Session toModel(SessionEntity entity) {
        return Session.builder()
                .refreshToken(entity.getRefreshToken())
                .accessToken(entity.getAccessToken())
                .build();
    }
}
