package com.github.happiexplorer.persistence.query;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builder for <i>SELECT</i> query.
 *
 * @param <R> Target entity class
 */
public final class SelectQuery<R> extends BaseQuery<R, SelectQuery<R>> {

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
    private final CriteriaQuery<Object> query;

    /**
     * Current query.
     */
    private final Root<R> root;

    /**
     * Order predicates.
     */
    private final List<Order> orders;

    /**
     * Constructor.
     *
     * @param manager      Entity manager
     * @param targetEntity Target entity class
     */
    SelectQuery(final EntityManager manager, final Class<R> targetEntity) {
        this.manager = manager;
        this.builder = manager.getCriteriaBuilder();
        this.query = this.builder.createQuery();
        this.root = this.query.from(targetEntity);
        this.orders = new ArrayList<>();
    }

    @Override
    protected SelectQuery<R> self() {
        return this;
    }

    /**
     * Fetch a relationship.
     *
     * @param attribute <i>JPA</i> column
     * @param <P>       <i>JPA</i> column type
     * @return The current instance of this builder
     */
    public <P> SelectQuery<R> fetch(final SingularAttribute<R, P> attribute) {
        this.root.fetch(attribute);
        return this;
    }

    /**
     * Add an ascending predicate order.
     *
     * @param attribute <i>JPA</i> column
     * @param <P>       <i>JPA</i> column type
     * @return The current instance of this builder
     */
    public <P> SelectQuery<R> order(final SingularAttribute<R, P> attribute) {
        return this.order(attribute, OrderBy.ASC);
    }

    /**
     * Add a predicate order.
     *
     * @param sort      How to order column
     * @param attribute <i>JPA</i> column
     * @param <P>       <i>JPA</i> column type
     * @return The current instance of this builder
     */
    public <P> SelectQuery<R> order(final SingularAttribute<R, P> attribute, final OrderBy sort) {
        Order order;
        if (Objects.equals(OrderBy.DESC, sort)) {
            order = this.builder.desc(this.root.get(attribute));
        } else {
            order = this.builder.asc(this.root.get(attribute));
        }
        this.orders.add(order);
        return this;
    }

    /**
     * Execute query.
     *
     * @return The list of all entities with the application of predicates
     */
    public List<R> findAll() {
        return this.buildQuery(this.root).getResultList();
    }

    /**
     * Execute query with a specific column return type.
     *
     * @param attribute <i>JPA</i> column return type
     * @param <P>       <i>JPA</i> column type
     * @return The list of all columns with the application of predicates
     */
    public <P> List<P> findAll(final SingularAttribute<R, P> attribute) {
        return this.buildQuery(this.root.get(attribute)).getResultList();
    }

    public List<R> findPage(final int pageNumber, final int pageSize) {
        return this.buildQuery(this.root)
                .setFirstResult(Math.max(0, pageSize * (pageNumber - 1)))
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Execute query with a pagination.
     *
     * @param attribute  <i>JPA</i> column return type
     * @param pageNumber Page index
     * @param pageSize   Number of element to fetch
     * @param <P>        <i>JPA</i> column type
     * @return The list of all columns with the application of predicates
     */
    public <P> List<P> findPage(final SingularAttribute<R, P> attribute, final int pageNumber, final int pageSize) {
        return this.buildQuery(this.root.get(attribute))
                .setFirstResult(pageSize * (pageNumber - 1))
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Execute query.
     *
     * @return An option with one entity
     */
    public Optional<R> findOne() {
        return this.buildQuery(this.root).getResultStream().findFirst();
    }

    /**
     * Execute query.
     *
     * @param attribute <i>JPA</i> column return type
     * @param <P>       <i>JPA</i> column type
     * @return An option with one column result
     */
    public <P> Optional<P> findOne(final SingularAttribute<R, P> attribute) {
        return this.buildQuery(this.root.get(attribute)).getResultStream().findFirst();
    }

    /**
     * Execute query.
     *
     * @return an entity, if not exists, an exception is throws
     */
    public R get() {
        return this.buildQuery(this.root).getSingleResult();
    }

    /**
     * Execute query.
     *
     * @param attribute <i>JPA</i> column return type
     * @param <P>       <i>JPA</i> column type
     * @return A column, if not exists, an exception is throws
     */
    public <P> P get(final SingularAttribute<R, P> attribute) {
        return this.buildQuery(this.root.get(attribute)).getSingleResult();
    }

    /**
     * Execute query.
     *
     * @return The number of entities with the application of predicates
     */
    public long count() {
        return this.buildQuery(this.builder.count(this.root)).getSingleResult();
    }

    /**
     * Execute query.
     *
     * @param attribute <i>JPA</i> column
     * @param <P>       <i>JPA</i> column type
     * @return The number of columns with the application of predicates
     */
    public <P> long count(final SingularAttribute<R, P> attribute) {
        return this.buildQuery(this.builder.count(this.root.get(attribute))).getSingleResult();
    }

    /**
     * Execute query.
     *
     * @return The value {@code true} if entities exists with the application of predicates,
     * otherwise the {@code false} is returned
     */
    public boolean exists() {
        var expression = this.builder.<Boolean>selectCase()
                .when(this.builder.greaterThan(this.builder.count(this.root), 0L), Boolean.TRUE)
                .otherwise(Boolean.FALSE);

        return this.buildQuery(expression).getSingleResult();
    }

    /**
     * Execute query.
     *
     * @param attribute <i>JPA</i> column
     * @param <P>       <i>JPA</i> column type
     * @return The value {@code true} if columns exists with the application of predicates,
     * otherwise the {@code false} is returned
     */
    public <P> boolean exists(final SingularAttribute<R, P> attribute) {
        var expression = this.builder.<Boolean>selectCase()
                .when(this.builder.greaterThan(this.builder.count(this.root.get(attribute)), 0L), Boolean.TRUE)
                .otherwise(Boolean.FALSE);

        return this.buildQuery(expression).getSingleResult();
    }

    /**
     * Construct the query with the given predicates.
     *
     * @param selection Entity or column to select
     * @param <T>       The <i>JPA</i> query result type
     * @return A <i>JPA</i> query
     */
    private <T> TypedQuery<T> buildQuery(final Selection<T> selection) {
        var resultQuery = (CriteriaQuery<T>) this.query.select(selection).orderBy(this.orders);
        if (!predicates.isEmpty()) {
            var computedPredicates = buildPredicates(this.builder, this.root, this.query, this.predicates);
            resultQuery = resultQuery.where(computedPredicates);
        }
        return this.manager.createQuery(resultQuery);
    }

}
