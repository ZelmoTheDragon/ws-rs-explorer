package com.github.ws.rs.explorer.security;

import java.util.Map;
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
     * Supported algorithm between <i>Java</i> and <i>JWT</i>.
     */
    static final Map<String, String> SUPPORTED_ALGORITHM = Map.of(
            "HS256", "HmacSHA256",
            "HS384", "HmacSHA384",
            "HS512", "HmacSHA512"
    );

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
     * @return A token credential
     */
    Credential of(final String token) {
        var secret = this.securityManager.getConfiguration(SecurityManager.Configuration.SECRET);
        return new TokenCredential(token, secret);
    }

    /**
     * Create an empty and invalid token credential.
     *
     * @return An empty and invalid token credential
     */
    Credential of() {
        return new EmptyTokenCredential();
    }

}
