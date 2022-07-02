package com.github.ws.rs.explorer.security;

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
    private final IdentityStoreHandler identityStoreHandler;

    /**
     * Token provider service for decoding <i>JWT</i>.
     */
    private final TokenCredentialFactory tokenCredentialFactory;

    /**
     * Injection constructor.
     * This class is injectable, don't call this constructor explicitly.
     *
     * @param identityStoreHandler   Service handler for authentification validation
     * @param tokenCredentialFactory Token provider service for decoding <i>JWT</i>
     */
    @Inject
    public OAuth2Authentication(
            final IdentityStoreHandler identityStoreHandler,
            final TokenCredentialFactory tokenCredentialFactory) {

        this.identityStoreHandler = identityStoreHandler;
        this.tokenCredentialFactory = tokenCredentialFactory;
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

        if (!isBearer) {
            authenticationStatus = httpMessageContext.doNothing();
        } else {
            var token = authorization
                    .map(e -> e.replace(BEARER_TOKEN, ""))
                    .orElse("");

            var credential = tokenCredentialFactory.of(token);
            if (!credential.isValid()) {
                authenticationStatus = httpMessageContext.responseUnauthorized();
            } else {
                var result = identityStoreHandler.validate(credential);
                authenticationStatus = httpMessageContext.notifyContainerAboutLogin(result);
            }
        }

        return authenticationStatus;
    }

}
