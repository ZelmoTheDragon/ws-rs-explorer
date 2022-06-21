package com.github.ws.rs.explorer.security;

import java.util.Optional;
import java.util.regex.Pattern;
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

@ApplicationScoped
public class OAuth2Authentication implements HttpAuthenticationMechanism {

    private static final Pattern JWT_EXTRACTOR_PATTERN = Pattern.compile("^Bearer +([^ ]+) *$", Pattern.CASE_INSENSITIVE);

    private final IdentityStoreHandler identityStoreHandler;

    private final TokenProvider tokenProvider;

    @Inject
    public OAuth2Authentication(
            final IdentityStoreHandler identityStoreHandler,
            final TokenProvider tokenProvider) {

        this.identityStoreHandler = identityStoreHandler;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthenticationStatus validateRequest(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpMessageContext httpMessageContext) throws AuthenticationException {

        AuthenticationStatus authenticationStatus;

        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        var matcher = JWT_EXTRACTOR_PATTERN.matcher(Optional.ofNullable(authorization).orElse(""));
        if (!matcher.matches()) {
            authenticationStatus = httpMessageContext.doNothing();
        } else {
            var token = matcher.group(1);
            var credential = tokenProvider.of(token);
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
