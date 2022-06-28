package com.github.ws.rs.explorer;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Root converter for dynamic entry.
 *
 * @param <E> Type of persistent entity
 * @param <D> Type of data transfer object
 */
public interface EntityMapper<E, D> {

    /**
     * Convert the identifier to the correct type.
     *
     * @param id  Unique identifier of entity
     * @param <K> Real type of identifier
     * @return An instance of identifier with the right type
     */
    default <K> K mapId(String id) {
        return (K) UUID.fromString(id);
    }

    /**
     * Convert a data transfer objet to entity.
     *
     * @param data Data transfer objet
     * @return An entity with data transfer objet data
     */
    E toEntity(D data);

    /**
     * Convert a data transfer objet to entity.
     *
     * @param data List of data transfer objet
     * @return An entity list with data transfer object data
     */
    default List<E> toEntity(List<D> data) {
        return Optional
                .ofNullable(data)
                .map(Collection::stream)
                .map(d -> d.map(this::toEntity).toList())
                .orElseGet(List::of);

    }

    /**
     * Convert an entity to data transfer objet.
     *
     * @param entity Entity
     * @return A data transfer object with entity data
     */
    D fromEntity(E entity);

    /**
     * Convert an entity to data transfer objet.
     *
     * @param entities entity list
     * @return A data transfer object list with entities data
     */
    default List<D> fromEntity(List<E> entities) {
        return Optional
                .ofNullable(entities)
                .map(Collection::stream)
                .map(d -> d.map(this::fromEntity).toList())
                .orElseGet(List::of);

    }

    /**
     * Update an entity with data transfer object data.
     *
     * @param source Data transfer object
     * @param target Entity
     */
    void updateEntity(D source, E target);

}
