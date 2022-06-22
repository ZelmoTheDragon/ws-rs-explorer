package com.github.ws.rs.explorer.security;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Singleton;

@Singleton
public final class SecurityManager {

    public static final String PERMIT_ALL = "@PermitAll";

    private final Set<String> roles;

    public SecurityManager() {
        this.roles = new HashSet<>();
    }

    public void addRole(final String role) {
        if (Objects.equals(PERMIT_ALL, role)) {
            throw new IllegalArgumentException("Role not allow");
        }
        this.roles.add(role);
    }

    public void scanRoleClassConfiguration(final Class<?> configurationClass) {

        if (configurationClass.isAnnotationPresent(DeclareRoles.class)) {
            var annotation = configurationClass.getAnnotation(DeclareRoles.class);
            var roles = annotation.value();
            for (var r : roles) {
                this.addRole(r);
            }
        } else {
            throw new IllegalArgumentException("Missing annotation '@DeclareRoles'");
        }
    }

    public Set<String> roles() {
        return Set.copyOf(this.roles);
    }

}
