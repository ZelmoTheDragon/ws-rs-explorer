package com.github.happiexplorer.security;

import jakarta.security.enterprise.credential.Credential;

/**
 * Empty and invalid token credential.
 */
final class EmptyTokenCredential implements Credential {

    /**
     * Default package private constructor.
     */
    EmptyTokenCredential() {
        // NO-OP
    }

    @Override
    public boolean isCleared() {
        return false;
    }

    @Override
    public void clear() {
        // NO-OP
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
