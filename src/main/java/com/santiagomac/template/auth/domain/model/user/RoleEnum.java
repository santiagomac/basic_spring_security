package com.santiagomac.template.auth.domain.model.user;

import com.santiagomac.template.common.exception.NotFoundException;

/** Keep in sync with the roles seeded in db/migration/auth-true. */
public enum RoleEnum {
    ADMIN,
    USER;

    public static RoleEnum getRoleByName(String name) {
        for (RoleEnum role : values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }
        throw new NotFoundException("Role not found: " + name);
    }
}
