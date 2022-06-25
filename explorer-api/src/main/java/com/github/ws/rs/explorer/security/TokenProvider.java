package com.github.ws.rs.explorer.security;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.security.enterprise.credential.Credential;

@Singleton
class TokenProvider {

    static final String SUPPORTED_TYPE = "JWT";

    private final SecurityManager securityManager;

    @Inject
    TokenProvider(final SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    Credential of(final String token) {

        return new JsonWebTokenSignedHashMac(token, "");
    }
}
