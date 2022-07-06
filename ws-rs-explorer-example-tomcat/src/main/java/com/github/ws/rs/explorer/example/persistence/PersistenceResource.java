package com.github.ws.rs.explorer.example.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class PersistenceResource {

    public PersistenceResource() {
    }

    @Produces
    public EntityManager createEntityManager() {
        return Persistence
                .createEntityManagerFactory("explorer-pu")
                .createEntityManager();
    }

}
