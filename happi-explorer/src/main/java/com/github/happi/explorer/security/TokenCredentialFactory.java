package com.github.happi.explorer.security;

import java.util.Map;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.security.enterprise.credential.Credential;

/**
 * Factory class for construct token credential.
 */
final class TokenCredentialFactory {

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
     * Internal constructor.
     * Instance not allowed.
     */
    private TokenCredentialFactory() {
        throw new UnsupportedOperationException("Instance not allowed");
    }

    /**
     * Create a token credential from a raw encoded token.
     *
     * @param token Raw encoded token
     * @return A token credential
     */
    static Credential of(final String token) {
        var securityManader = CDI.current().select(ExplorerSecurityManager.class).get();
        var secret = securityManader.getConfiguration(ExplorerSecurityManager.Configuration.SECRET);
        return new TokenCredential(token, secret);
    }

    /**
     * Create an empty and invalid token credential.
     *
     * @return An empty and invalid token credential
     */
    static Credential of() {
        return new EmptyTokenCredential();
    }

}
