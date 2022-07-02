package com.github.ws.rs.explorer.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.json.JsonObject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.github.ws.rs.explorer.EntityMapper;

/**
 * Main contract service for this module.
 * Generically prose the operations of creation reading deletion and update.
 */
public interface ExplorerService {

    /**
     * Change search behavior.
     * By default, no modification is performed.
     *
     * @param currentPredicate Current predicate
     * @param builder          Criteria builder
     * @param root             Root clause of database query
     * @param query            Current database query
     * @param <E>              Type of persistent entity
     * @param <R>              Type of query return
     * @return A new predicate with additional restriction
     */
    default <E, R> Predicate onFilter(
            final Predicate currentPredicate,
            final CriteriaBuilder builder,
            final Root<E> root,
            final CriteriaQuery<R> query) {

        return currentPredicate;
    }

    /**
     * Change find behavior.
     * By default, no modification is performed.
     *
     * @param entity Current entity found in database
     * @param <E>    Type of persistent entity
     * @return The same entity edited
     */
    default <E> E onFind(E entity) {
        return entity;
    }

    /**
     * Change create behavior.
     * By default, no modification is performed.
     *
     * @param entity Current entity before insert in database
     * @param <E>    Type of persistent entity
     * @return The same entity edited
     */
    default <E> E onCreate(E entity) {
        return entity;
    }

    /**
     * Change update behavior.
     * By default, no modification is performed.
     *
     * @param entity Current entity before update in database
     * @param <E>    Type of persistent entity
     */
    default <E> void onUpdate(E entity) {
    }

    /**
     * Change remove behavior.
     *
     * @param entity Current entity before remove in database
     * @param <E>    Type of persistent entity
     * @return The same entity edited
     */
    default <E> E onRemove(E entity) {
        return entity;
    }

    /**
     * Filter entity with web parameters.
     *
     * @param name       Web entity name
     * @param parameters Web parameters
     * @param <E>        Type of persistent entity
     * @param <D>        Type of data transfer object
     * @param <M>        Type of mapper
     * @return A pagination object that contains data based on the filtered result
     */
    <E, D, M extends EntityMapper<E, D>> PaginationData<D> filter(String name, Map<String, List<String>> parameters);

    /**
     * Find a unique resource.
     *
     * @param name Web entity name
     * @param id   Unique identifier of entity
     * @param <E>  Type of persistent entity
     * @param <D>  Type of data transfer object
     * @param <M>  Type of mapper
     * @return An option that contains or not tha data
     */
    <E, D, M extends EntityMapper<E, D>> Optional<D> find(String name, String id);

    /**
     * Create a new entity.
     *
     * @param name     Web entity name
     * @param document <i>JSON</i> object of the new entity
     * @param <E>      Type of persistent entity
     * @param <D>      Type of data transfer object
     * @param <M>      Type of mapper
     * @param <K>      Type of unique identifier
     * @return The unique identifier of the new resource
     */
    <E, D, M extends EntityMapper<E, D>, K> K create(String name, JsonObject document);

    /**
     * Update an existing entity.
     *
     * @param name     Web entity name
     * @param document <i>JSON</i> object of the existing entity
     * @param id       Unique identifier
     * @param <E>      Type of persistent entity
     * @param <D>      Type of data transfer object
     * @param <M>      Type of mapper
     */
    <E, D, M extends EntityMapper<E, D>> void update(String name, JsonObject document, String id);

    /**
     * Delete an existing entity.
     *
     * @param name Web entity name
     * @param id   Unique identifier
     * @param <E>  Type of persistent entity
     * @param <D>  Type of data transfer object
     * @param <M>  Type of mapper
     */
    <E, D, M extends EntityMapper<E, D>> void delete(String name, String id);

    /**
     * Check if the unique identifier is associated with an entity.
     *
     * @param name Web entity name
     * @param id   Unique identifier
     * @param <E>  Type of persistent entity
     * @param <D>  Type of data transfer object
     * @param <M>  Type of mapper
     * @return The value {@code true} if the unique identifier is associated with an entity
     * otherwise the value {@code false} is returned
     */
    <E, D, M extends EntityMapper<E, D>> boolean exists(String name, String id);
}
