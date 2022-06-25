package com.github.ws.rs.explorer.example.persistence;


import java.util.Optional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Singleton
public class GenericDAO {

    private final EntityManager em;

    @Inject
    public GenericDAO(final EntityManager em) {
        this.em = em;
    }

    public <E> Optional<E> find(Class<E> entityClass, Object id) {
        var entity = this.em.find(entityClass, id);
        return Optional.ofNullable(entity);
    }
}
