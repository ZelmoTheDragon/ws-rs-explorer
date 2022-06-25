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

public interface ExplorerService {

    default <E, R> Predicate onFilter(
            final Predicate currentPredicate,
            final CriteriaBuilder builder,
            final Root<E> root,
            final CriteriaQuery<R> query) {

        return currentPredicate;
    }

    default <E> E onFind(E entity) {
        return entity;
    }

    default <E> E onCreate(E entity) {
        return entity;
    }

    default <E> void onUpdate(E entity) {
    }

    default <E> E onRemove(E entity) {
        return entity;
    }

    <E, D, M extends EntityMapper<E, D>> PaginationData<D> filter(String name, Map<String, List<String>> parameters);

    <E, D, M extends EntityMapper<E, D>> Optional<D> find(String name, String id);

    <E, D, M extends EntityMapper<E, D>, K> K create(String name, JsonObject document);

    <E, D, M extends EntityMapper<E, D>> void update(String name, JsonObject document, String id);

    <E, D, M extends EntityMapper<E, D>> void delete(String name, String id);

}
