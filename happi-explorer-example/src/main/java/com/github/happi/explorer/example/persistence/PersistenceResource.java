package com.github.happi.explorer.example.persistence;

import java.util.Objects;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class PersistenceResource {

    private static final String PERSISTENCE_UNIT_NAME = "explorer-pu";

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private transient EntityManager em;

    public PersistenceResource() {
    }

    @Produces
    public EntityManager createEntityManager() {
        EntityManager injectableEntityManager;
        if (Objects.isNull(em)) {
            injectableEntityManager = Persistence
                    .createEntityManagerFactory(PERSISTENCE_UNIT_NAME)
                    .createEntityManager();
        } else {
            injectableEntityManager = em;
        }
        return injectableEntityManager;
    }

}
