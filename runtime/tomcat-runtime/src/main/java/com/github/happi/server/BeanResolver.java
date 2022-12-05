package com.github.happi.server;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;

import com.github.happi.security.HappiSecurityManager;
import com.github.happi.security.OAuth2Authentication;

@ApplicationScoped
public class BeanResolver {

    //FIXME: Debug only.

    public BeanResolver() {
        // NO-OP
    }

    @Produces
    public OAuth2Authentication createOAuth2Authentication() {
        var instance = new OAuth2Authentication();
        injectField(instance, "identityStoreHandler", createIdentityStoreHandler());
        injectField(instance, "securityManager", createHappiSecurityManager());

        return instance;
    }

    private static HappiSecurityManager createHappiSecurityManager() {
        return CDI.current().select(HappiSecurityManager.class).get();
    }

    private static IdentityStoreHandler createIdentityStoreHandler() {
        return CDI.current().select(IdentityStoreHandler.class).get();
    }

    private static <B> void injectField(
            final OAuth2Authentication instance,
            final String fieldName,
            final B bean) {

        try {
            var injectableField = OAuth2Authentication.class.getDeclaredField(fieldName);
            injectableField.setAccessible(true);
            injectableField.set(instance, bean);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
