package com.santiagomac.auth.domain.model.user;

public enum RoleEnum {
    ADMIN,
    PROFESSIONAL;


    public static RoleEnum getRoleByName(String name) {
        for (RoleEnum role : values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }

        return null;
    }
}
