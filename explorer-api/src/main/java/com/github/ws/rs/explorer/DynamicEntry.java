package com.github.ws.rs.explorer;

import java.util.Map;
import java.util.Objects;

import com.github.ws.rs.explorer.service.ExplorerService;

public class DynamicEntry<E, D, M extends EntityMapper<E, D>, S extends ExplorerService> {

    private final String name;

    private final Map<Action, String> actions;

    private final Class<E> entityClass;

    private final Class<D> dataClass;

    private final Class<M> mapperClass;

    private final Class<S> serviceClass;

    public DynamicEntry(
            final String name,
            final Map<Action, String> actions,
            final Class<E> entityClass,
            final Class<D> dataClass,
            final Class<M> mapperClass,
            final Class<S> serviceClass) {

        this.name = name;
        this.actions = Map.copyOf(actions);
        this.entityClass = entityClass;
        this.dataClass = dataClass;
        this.mapperClass = mapperClass;
        this.serviceClass = serviceClass;
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq;
        if (this == obj) {
            eq = true;
        } else if (obj == null || getClass() != obj.getClass()) {
            eq = false;
        } else {
            var entry = (DynamicEntry) obj;
            eq = Objects.equals(name, entry.name)
                    && Objects.equals(actions, entry.actions)
                    && Objects.equals(entityClass, entry.entityClass)
                    && Objects.equals(dataClass, entry.dataClass)
                    && Objects.equals(mapperClass, entry.mapperClass)
                    && Objects.equals(serviceClass, entry.serviceClass);
        }
        return eq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, actions, entityClass, dataClass, mapperClass, serviceClass);
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getSimpleName())
                .append("{name='").append(name).append('\'')
                .append(", actions='").append(actions).append('\'')
                .append(", entityClass=").append(entityClass)
                .append(", dataClass=").append(dataClass)
                .append(", serviceClass=").append(serviceClass)
                .append('}').toString();
    }

    // Accesseurs

    public String getName() {
        return name;
    }

    public Map<Action, String> getActions() {
        return Map.copyOf(actions);
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public Class<D> getDataClass() {
        return dataClass;
    }

    public Class<M> getMapperClass() {
        return mapperClass;
    }

    public Class<S> getServiceClass() {
        return serviceClass;
    }
}
