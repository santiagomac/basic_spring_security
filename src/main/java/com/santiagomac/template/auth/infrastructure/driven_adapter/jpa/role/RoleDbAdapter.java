package com.santiagomac.template.auth.infrastructure.driven_adapter.jpa.role;

import com.santiagomac.template.auth.ConditionalOnAuthEnabled;
import com.santiagomac.template.auth.application.ports.out.RoleGateway;
import com.santiagomac.template.auth.domain.model.user.RoleEnum;
import com.santiagomac.template.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnAuthEnabled
@RequiredArgsConstructor
public class RoleDbAdapter implements RoleGateway {

    private final RoleRepository roleRepository;

    @Override
    public RoleEnum getRole(UUID id) {
        return roleRepository.findById(id)
                .map(role -> RoleEnum.getRoleByName(role.getName()))
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }

    @Override
    public UUID getRoleId(RoleEnum role) {
        return roleRepository.findByName(role.name())
                .map(RoleEntity::getId)
                .orElseThrow(() -> new NotFoundException("Role not found: " + role.name()));
    }
}
