package com.github.happiexplorer.persistence.query;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;

/**
 * Builder for <i>DELETE</i> query.
 *
 * @param <R> Target entity class
 */
public final class DeleteQuery<R> extends BaseQuery<R, DeleteQuery<R>> {

    /**
     * Entity manager.
     */
    private final EntityManager manager;

    /**
     * <i>JPA</i> query builder.
     */
    private final CriteriaBuilder builder;

    /**
     * Current query.
     */
    private final CriteriaDelete<R> query;

    /**
     * Root.
     */
    private final Root<R> root;

    /**
     * Constructor.
     *
     * @param manager      Entity manager
     * @param targetEntity Target entity class
     */
    DeleteQuery(final EntityManager manager, final Class<R> targetEntity) {
        this.manager = manager;
        this.builder = manager.getCriteriaBuilder();
        this.query = this.builder.createCriteriaDelete(targetEntity);
        this.root = this.query.from(targetEntity);
    }

    @Override
    protected DeleteQuery<R> self() {
        return this;
    }

    /**
     * Execute query.
     *
     * @return The number of entities deleted
     */
    public int execute() {
        if (!this.predicates.isEmpty()) {
            var computedPredicated = buildPredicates(this.builder, this.root, this.query, this.predicates);
            this.query.where(computedPredicated);
        }
        return this.manager.createQuery(query).executeUpdate();
    }
}
