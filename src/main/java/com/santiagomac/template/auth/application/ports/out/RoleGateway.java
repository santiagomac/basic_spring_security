package com.santiagomac.template.auth.application.ports.out;

import com.santiagomac.template.auth.domain.model.user.RoleEnum;

import java.util.UUID;

public interface RoleGateway {

    RoleEnum getRole(UUID id);

    UUID getRoleId(RoleEnum role);
}
