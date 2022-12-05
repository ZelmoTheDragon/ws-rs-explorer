package com.github.happi.server;

import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.CDI;

import com.github.happi.security.OAuth2Authentication;

@ApplicationScoped
public class BeanResolver {

    private static final Logger LOG = Logger.getLogger(BeanResolver.class.getName());

    public BeanResolver() {
    }

    public void addMissingBean(@Observes final AfterBeanDiscovery event) {

        var authenticationMechanism = CDI.current().select(OAuth2Authentication.class);

        if (!authenticationMechanism.isResolvable()) {
            event
                    .addBean()
                    .types(OAuth2Authentication.class)
                    .scope(ApplicationScoped.class);
        }

        //TODO: add producer...
    }
}
