package com.github.happiexplorer.persistence.query;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SelectQuery<R> extends BaseQuery<R, SelectQuery<R>> {

    private final EntityManager manager;

    private final CriteriaBuilder builder;

    private final CriteriaQuery<Object> query;

    private final Root<R> root;

    private final List<Order> orders;

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

    public <P> SelectQuery<R> fetch(final SingularAttribute<R, P> attribute) {
        this.root.fetch(attribute);
        return this;
    }

    public <P> SelectQuery<R> order(final SingularAttribute<R, P> attribute) {
        return this.order(attribute, OrderBy.ASC);
    }

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

    public List<R> findAll() {
        return this.buildQuery(this.root).getResultList();
    }

    public <P> List<P> findAll(final SingularAttribute<R, P> attribute) {
        return this.buildQuery(this.root.get(attribute)).getResultList();
    }

    public List<R> findPage(final int pageNumber, final int pageSize) {
        return this.buildQuery(this.root)
                .setFirstResult(Math.max(0, pageSize * (pageNumber - 1)))
                .setMaxResults(pageSize)
                .getResultList();
    }

    public <P> List<P> findPage(final SingularAttribute<R, P> attribute, final int pageNumber, final int pageSize) {
        return this.buildQuery(this.root.get(attribute))
                .setFirstResult(pageSize * (pageNumber - 1))
                .setMaxResults(pageSize)
                .getResultList();
    }

    public Optional<R> findOne() {
        return this.buildQuery(this.root).getResultStream().findFirst();
    }

    public <P> Optional<P> findOne(final SingularAttribute<R, P> attribute) {
        return this.buildQuery(this.root.get(attribute)).getResultStream().findFirst();
    }

    public R get() {
        return this.buildQuery(this.root).getSingleResult();
    }

    public <P> P get(final SingularAttribute<R, P> attribute) {
        return this.buildQuery(this.root.get(attribute)).getSingleResult();
    }

    public long count() {
        return this.buildQuery(this.builder.count(this.root)).getSingleResult();
    }

    public <P> long count(final SingularAttribute<R, P> attribute) {
        return this.buildQuery(this.builder.count(this.root.get(attribute))).getSingleResult();
    }

    public boolean exists() {
        var expression = this.builder.<Boolean>selectCase()
                .when(this.builder.greaterThan(this.builder.count(this.root), 0L), Boolean.TRUE)
                .otherwise(Boolean.FALSE);

        return this.buildQuery(expression).getSingleResult();
    }

    public <P> boolean exists(final SingularAttribute<R, P> attribute) {
        var expression = this.builder.<Boolean>selectCase()
                .when(this.builder.greaterThan(this.builder.count(this.root.get(attribute)), 0L), Boolean.TRUE)
                .otherwise(Boolean.FALSE);

        return this.buildQuery(expression).getSingleResult();
    }

    private <T> TypedQuery<T> buildQuery(final Selection<T> selection) {
        var resultQuery = (CriteriaQuery<T>) this.query.select(selection).orderBy(this.orders);
        if (!predicates.isEmpty()) {
            var computedPredicates = buildPredicates(this.builder, this.root, this.query, this.predicates);
            resultQuery = resultQuery.where(computedPredicates);
        }
        return this.manager.createQuery(resultQuery);
    }

}
