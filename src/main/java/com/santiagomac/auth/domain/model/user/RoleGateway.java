package com.santiagomac.auth.domain.model.user;

import java.util.UUID;

public interface RoleGateway {

    RoleEnum getRole(UUID id);
}
