package com.github.happiexplorer.security;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * Simple security manager.
 */
@ApplicationScoped
public class HappiSecurityManager {

    /**
     * Unauthorized operation.
     */
    public static final String DENY_ALL = "@DenyAll";

    /**
     * Role required only an authenticated user without specific privileges.
     */
    public static final String PERMIT_ALL = "@PermitAll";

    /**
     * Public access, no authenticate user required.
     */
    public static final String PUBLIC = "@Public";

    /**
     * Application roles.
     */
    private final Set<String> roles;

    /**
     * Additional configuration.
     */
    private final Properties configuration;

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public HappiSecurityManager() {
        this.roles = new HashSet<>();
        this.configuration = new Properties();
    }

    /**
     * Add a role in this security manager.
     *
     * @param role A role
     */
    public void addRole(final String role) {
        if (Objects.equals(PUBLIC, role)
                || Objects.equals(PERMIT_ALL, role)
                || Objects.equals(DENY_ALL, role)) {

            throw new IllegalArgumentException("Role name not allow");
        }
        this.roles.add(role);
    }

    /**
     * Scan a class for load roles from annotation {@link DeclareRoles}.
     *
     * @param configurationClass Class to scan
     */
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

    /**
     * Get all role registered in this security manager.
     *
     * @return A set of registered roles
     */
    public Set<String> roles() {
        return Set.copyOf(this.roles);
    }

    /**
     * Load configuration for this security manager.
     * Erase previous existing configuration.
     *
     * @param configuration New configuration
     */
    public void setConfiguration(final Properties configuration) {
        this.configuration.putAll(configuration);
    }

    /**
     * Add or replace a specific configuration.
     *
     * @param key   Name of the property
     * @param value The value associated with this key
     */
    public void putConfiguration(final Configuration key, String value) {
        this.configuration.setProperty(key.name(), value);
    }

    /**
     * Retrieve a specific configuration.
     *
     * @param key Key name of the property
     * @return The value associated with this key or {@code null}
     */
    public String getConfiguration(final Configuration key) {
        return this.configuration.getProperty(key.name());
    }

    /**
     * Available configuration key.
     */
    public enum Configuration {

        /**
         * Claim name contained in th <i>JWT</i> for retrieve the username.
         */
        TOKEN_CLAIM_USERNAME,

        /**
         * Claim name contained in th <i>JWT</i> for retrieve the roles.
         */
        TOKEN_CLAIM_GROUPS,

        /**
         * Indicates if the manager endpoint is available.
         */
        MANAGER_ENDPOINT,

        /**
         * Indicates if the discovery endpoint is available.
         */
        DISCOVERY_ENDPOINT,

        /**
         * Indicates if the security is enabled in this module.
         * If security is turned off, then role checking will be ignored.
         */
        JAKARTA_SECURITY,

        /**
         * Secret value for compute the <i>JWT</i> signature.
         */
        SECRET
    }
}
