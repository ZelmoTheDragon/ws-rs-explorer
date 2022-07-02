package com.github.ws.rs.explorer.security;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.security.enterprise.credential.Credential;

/**
 * Factory class for construct token credential.
 */
@Singleton
class TokenCredentialFactory {

    /**
     * Supporter token format.
     */
    static final String SUPPORTED_TYPE = "JWT";

    /**
     * Security manager for this module.
     */
    private final SecurityManager securityManager;

    /**
     * Injection constructor.
     * This class is injectable, don't call this constructor explicitly.
     *
     * @param securityManager Security manager for this module.
     */
    @Inject
    TokenCredentialFactory(final SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * Create a token credential from a raw encoded token.
     *
     * @param token Raw encoded token
     * @return a token credential
     */
    Credential of(final String token) {
        var secret = this.securityManager.getConfiguration(SecurityManager.Configuration.SECRET);
        return new TokenCredential(token, secret);
    }
}
