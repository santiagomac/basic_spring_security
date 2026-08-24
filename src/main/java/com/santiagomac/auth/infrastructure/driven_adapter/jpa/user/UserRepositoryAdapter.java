package com.santiagomac.auth.infrastructure.driven_adapter.jpa.user;

import com.santiagomac.auth.domain.model.user.UserModel;
import com.santiagomac.auth.domain.model.user.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserGateway {

    private final UserRepository userRepository;


    @Override
    public Optional<UserModel> findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .map(entity -> UserModel.builder()
                        .email(entity.getEmail())
                        .password(entity.getPassword())
                        .enabled(entity.isEnabled())
                        .role(entity.getRole())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .build()
                );
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return this.userRepository.save(userEntity);
    }
}
