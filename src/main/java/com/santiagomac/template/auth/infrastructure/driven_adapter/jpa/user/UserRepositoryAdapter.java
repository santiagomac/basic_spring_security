package com.santiagomac.template.auth.infrastructure.driven_adapter.jpa.user;

import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import com.santiagomac.template.auth.application.ports.out.UserGateway;
import com.santiagomac.template.auth.domain.model.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnAuthEnabled
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserGateway {

    private final UserRepository userRepository;

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return toModel(userRepository.save(toEntity(userModel)));
    }

    private UserEntity toEntity(UserModel model) {
        return UserEntity.builder()
                .id(model.getId())
                .email(model.getEmail())
                .password(model.getPassword())
                .roleId(model.getRoleId())
                .enabled(model.isEnabled())
                .build();
    }

    private UserModel toModel(UserEntity entity) {
        return UserModel.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .enabled(entity.isEnabled())
                .roleId(entity.getRoleId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
