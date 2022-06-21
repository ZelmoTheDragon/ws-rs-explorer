package com.github.ws.rs.explorer.security;

import jakarta.security.enterprise.credential.Credential;

public interface TokenProvider {

    Credential of(String token);

}
