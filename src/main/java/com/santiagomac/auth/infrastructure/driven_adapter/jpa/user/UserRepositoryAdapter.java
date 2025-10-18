package com.santiagomac.auth.infrastructure.driven_adapter.jpa.user;

import com.santiagomac.auth.domain.model.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserGateway {

    private final UserRepository userRepository;


    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }
}
