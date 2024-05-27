package com.github.happiexplorer.persistence.query;

import jakarta.persistence.EntityManager;

/**
 * A <i>JPA</i> Query builder over <i>Criteria API</i>.
 * Simple query like <i>SELECT</i>, <i>UPDATE</i> or <i>DELETE</i> can be performed with this builder.
 * The syntax is fluent and more readable as SQL-like.
 */
public final class QueryBuilder {

    /**
     * Entity manager.
     */
    private final EntityManager manager;

    /**
     * Internal constructor.
     * Use the factory method {@link QueryBuilder#of(EntityManager)}.
     *
     * @param manager Entity manager
     */
    private QueryBuilder(final EntityManager manager) {
        this.manager = manager;
    }

    /**
     * Start the builder as <i>SELECT</i> query.
     *
     * @param targetEntity Target entity
     * @param <T>          Target entity type
     * @return A new builder for <i>SELECT</i> query
     */
    public <T> SelectQuery<T> select(final Class<T> targetEntity) {
        return new SelectQuery<>(this.manager, targetEntity);
    }

    /**
     * Start the builder as <i>UPDATE</i> query.
     *
     * @param targetEntity Target entity
     * @param <T>          Target entity type
     * @return A new builder for <i>UPDATE</i> query
     */
    public <T> UpdateQuery<T> update(final Class<T> targetEntity) {
        return new UpdateQuery<>(this.manager, targetEntity);
    }

    /**
     * Start the builder as <i>DELETE</i> query.
     *
     * @param targetEntity Target entity
     * @param <T>          Target entity type
     * @return A new builder for <i>DELETE</i> query
     */
    public <T> DeleteQuery<T> delete(final Class<T> targetEntity) {
        return new DeleteQuery<>(this.manager, targetEntity);
    }

    /**
     * Create a new query builder for <i>SELECT</i>, <i>UPDATE</i> or <i>DELETE</i> query.
     *
     * @param manager Entity manager
     * @return A new query builder
     */
    public static QueryBuilder of(final EntityManager manager) {
        return new QueryBuilder(manager);
    }

    /**
     * Create a new builder for sub query.
     *
     * @param type Entity of sub query
     * @param <R>  Entity type of sub query
     * @return A new sub query builder
     */
    public static <R> SubQuery<R> subquery(final Class<R> type) {
        return new SubQuery<>(type);
    }

    /**
     * Create a new query part for predicate of the overall query.
     *
     * @param <R> Target entity class
     * @return A new query part
     */
    public static <R> QueryPart<R> with() {
        return new QueryPart<>();
    }


}
