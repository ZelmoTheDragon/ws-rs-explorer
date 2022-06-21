package com.github.ws.rs.explorer.security;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

    public Set<String> roles() {
        return Set.copyOf(this.roles);
    }

}
