package com.santiagomac.template.auth.infrastructure.driven_adapter.jpa.token;

import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import com.santiagomac.template.auth.application.ports.out.SessionGateway;
import com.santiagomac.template.auth.domain.model.session.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnAuthEnabled
@RequiredArgsConstructor
public class SessionRepositoryAdapter implements SessionGateway {

    private final SessionRepository sessionRepository;

    @Override
    public Session save(Session session) {
        return toModel(sessionRepository.save(toEntity(session)));
    }

    @Override
    public Optional<Session> findByRefreshToken(String refreshToken) {
        return sessionRepository.findByRefreshTokenHash(Session.hash(refreshToken)).map(this::toModel);
    }

    @Override
    public void deleteByRefreshToken(String refreshToken) {
        sessionRepository.deleteByRefreshTokenHash(Session.hash(refreshToken));
    }

    private SessionEntity toEntity(Session session) {
        return SessionEntity.builder()
                .id(session.getId())
                .refreshTokenHash(session.getRefreshTokenHash())
                .userId(session.getUserId())
                .build();
    }

    private Session toModel(SessionEntity entity) {
        return Session.builder()
                .id(entity.getId())
                .refreshTokenHash(entity.getRefreshTokenHash())
                .userId(entity.getUserId())
                .build();
    }
}
