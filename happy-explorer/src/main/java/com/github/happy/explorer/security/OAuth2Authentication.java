package com.github.happy.explorer.security;

import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;

/**
 * An <i>HTTP</i> authentification mechanism for <i>JWT</i> flow.
 */
@ApplicationScoped
public class OAuth2Authentication implements HttpAuthenticationMechanism {

    /**
     * Keyword in <i>HTTP</i> header.
     */
    private static final String BEARER_TOKEN = "Bearer ";

    /**
     * Service handler for authentification validation.
     */
    @Inject
    private IdentityStoreHandler identityStoreHandler;

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public OAuth2Authentication() {
        // NO-OP
    }

    @Override
    public AuthenticationStatus validateRequest(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpMessageContext httpMessageContext) throws AuthenticationException {

        AuthenticationStatus authenticationStatus;

        var authorization = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
        var isBearer = authorization
                .map(e -> e.startsWith(BEARER_TOKEN))
                .orElse(Boolean.FALSE);

        if (isBearer) {

            var credential = authorization
                    .map(e -> e.replace(BEARER_TOKEN, ""))
                    .map(String::trim)
                    .filter(e -> !e.isBlank())
                    .map(TokenCredentialFactory::of)
                    .orElseGet(TokenCredentialFactory::of);

            if (credential.isValid()) {
                var result = identityStoreHandler.validate(credential);
                authenticationStatus = httpMessageContext.notifyContainerAboutLogin(result);

            } else {
                authenticationStatus = httpMessageContext.responseUnauthorized();
            }

            credential.clear();

        } else if (httpMessageContext.isProtected()) {
            authenticationStatus = httpMessageContext.responseUnauthorized();
        } else {
            authenticationStatus = httpMessageContext.doNothing();
        }

        return authenticationStatus;
    }

}
