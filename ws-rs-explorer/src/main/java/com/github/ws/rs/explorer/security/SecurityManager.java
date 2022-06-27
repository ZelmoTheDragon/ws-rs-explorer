package com.github.ws.rs.explorer.security;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Singleton;

@Singleton
public final class SecurityManager {

    public static final String PERMIT_ALL = "@PermitAll";

    private final Set<String> roles;

    private final Properties configuration;

    public SecurityManager() {
        this.roles = new HashSet<>();
        this.configuration = new Properties();
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

    public void setConfiguration(final Properties configuration) {
        this.configuration.putAll(configuration);
    }

    public void putConfiguration(final Configuration key, String value) {
        this.configuration.setProperty(key.name(), value);
    }

    public String getConfiguration(final Configuration key) {
        return this.configuration.getProperty(key.name());
    }

    public enum Configuration {

        TOKEN_CLAIM_USERNAME,

        TOKEN_CLAIM_GROUPS,

        MANAGER_ENDPOINT,

        SECRET
    }
}
