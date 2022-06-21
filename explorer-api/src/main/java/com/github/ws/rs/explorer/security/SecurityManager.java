package com.github.ws.rs.explorer.security;

import java.util.HashSet;
import java.util.Set;
import jakarta.inject.Singleton;

@Singleton
public final class SecurityManager {

    public static final String PERMIT_ALL = "@PermitAll";

    private final Set<String> groups;

    public SecurityManager() {
        this.groups = new HashSet<>();
    }

    public void addGroup(final String role) {
        this.groups.add(role);
    }

    public Set<String> groups() {
        return Set.copyOf(this.groups);
    }

}
