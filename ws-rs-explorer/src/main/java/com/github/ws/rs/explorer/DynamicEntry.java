package com.github.ws.rs.explorer;

import java.util.Map;
import java.util.Objects;

import com.github.ws.rs.explorer.service.ExplorerService;

/**
 * Dynamic entry point.
 *
 * @param <E> Type of persistent entity
 * @param <D> Type of data transfer object
 * @param <M> Type of mapper
 * @param <S> Type of service
 */
public class DynamicEntry<E, D, M extends EntityMapper<E, D>, S extends ExplorerService> {

    /**
     * Path of entry point.
     * Must be unique and following URL path name convention.
     */
    private final String path;

    /**
     * Actions with roles.
     * Must be a <i>CDI</i> injectable class.
     */
    private final Map<Action, String> actions;

    /**
     * Entity type.
     */
    private final Class<E> entityClass;

    /**
     * Data transfer object type.
     */
    private final Class<D> dataClass;

    /**
     * Mapper type.
     * Must be a <i>CDI</i> injectable class.
     */
    private final Class<M> mapperClass;

    /**
     * Service type.
     */
    private final Class<S> serviceClass;

    /**
     * Construct a new entry point for controller explorer.
     *
     * @param path         Path of entry point
     * @param actions      Actions with roles
     * @param entityClass  Entity type
     * @param dataClass    Data transfer object type
     * @param mapperClass  Mapper type
     * @param serviceClass Service type
     */
    public DynamicEntry(
            final String path,
            final Map<Action, String> actions,
            final Class<E> entityClass,
            final Class<D> dataClass,
            final Class<M> mapperClass,
            final Class<S> serviceClass) {

        this.path = path;
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
            eq = Objects.equals(path, entry.path)
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
        return Objects.hash(path, actions, entityClass, dataClass, mapperClass, serviceClass);
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getSimpleName())
                .append("{name='").append(path).append('\'')
                .append(", actions='").append(actions).append('\'')
                .append(", entityClass=").append(entityClass)
                .append(", dataClass=").append(dataClass)
                .append(", serviceClass=").append(serviceClass)
                .append('}').toString();
    }

    // Getter...

    public String getPath() {
        return path;
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
