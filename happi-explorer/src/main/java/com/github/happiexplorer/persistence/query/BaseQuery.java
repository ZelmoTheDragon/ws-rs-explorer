package com.github.happiexplorer.persistence.query;

import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

abstract class BaseQuery<R, Q extends BaseQuery<R, Q>> {

    protected final List<QueryPredicate<R>> predicates;

    BaseQuery() {
        this.predicates = new ArrayList<>();
    }

    protected abstract Q self();

    public Q isTrue(final SingularAttribute<R, Boolean> attribute) {
        QueryPredicate<R> predicate = (b, r, c) -> b.isTrue(r.get(attribute));
        this.predicates.add(predicate);
        return this.self();
    }

    public Q isTrue(final CompositeAttribute<R, Boolean> compositor) {
        QueryPredicate<R> predicate = (b, r, c) -> b.isTrue(compositor.apply(r));
        this.predicates.add(predicate);
        return this.self();
    }

    public Q isFalse(final SingularAttribute<R, Boolean> attribute) {
        QueryPredicate<R> predicate = (b, r, c) -> b.isFalse(r.get(attribute));
        this.predicates.add(predicate);
        return this.self();
    }

    public Q isFalse(final CompositeAttribute<R, Boolean> compositor) {
        QueryPredicate<R> predicate = (b, r, c) -> b.isFalse(compositor.apply(r));
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q isNull(final SingularAttribute<R, V> attribute) {
        QueryPredicate<R> predicate = (b, r, c) -> b.isNull(r.get(attribute));
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q isNull(final CompositeAttribute<R, V> compositor) {
        QueryPredicate<R> predicate = (b, r, c) -> b.isNull(compositor.apply(r));
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q isNotNull(final SingularAttribute<R, V> attribute) {
        QueryPredicate<R> predicate = (b, r, c) -> b.isNotNull(r.get(attribute));
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q isNotNull(final CompositeAttribute<R, V> compositor) {
        QueryPredicate<R> predicate = (b, r, c) -> b.isNotNull(compositor.apply(r));
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q equal(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.equal(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q equal(final CompositeAttribute<R, V> compositor, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.equal(compositor.apply(r), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q notEqual(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.notEqual(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q notEqual(final CompositeAttribute<R, V> compositor, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.notEqual(compositor.apply(r), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public Q like(final SingularAttribute<R, String> attribute, final String value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.like(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public Q like(final CompositeAttribute<R, String> compositor, final String value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.like(compositor.apply(r), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public Q notLike(final SingularAttribute<R, String> attribute, final String value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.notLike(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public Q notLike(final CompositeAttribute<R, String> compositor, final String value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.notLike(compositor.apply(r), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q between(final SingularAttribute<R, V> attribute, final V v0, final V v1) {
        QueryPredicate<R> predicate = (b, r, c) -> b.between(r.get(attribute), v0, v1);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q between(final CompositeAttribute<R, V> compositor, final V v0, final V v1) {
        QueryPredicate<R> predicate = (b, r, c) -> b.between(compositor.apply(r), v0, v1);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q greaterThan(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.greaterThan(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q greaterThan(final CompositeAttribute<R, V> compositor, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.greaterThan(compositor.apply(r), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Number> Q greaterThan(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.gt(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q greaterThanOrEqualTo(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.greaterThanOrEqualTo(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Number> Q greaterThanOrEqualTo(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.ge(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q greaterThanOrEqualTo(final CompositeAttribute<R, V> compositor, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.greaterThanOrEqualTo(compositor.apply(r), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q lessThan(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.lessThan(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Number> Q lessThan(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.lt(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q lessThan(final CompositeAttribute<R, V> compositor, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.lessThan(compositor.apply(r), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q lessThanOrEqualTo(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.lessThanOrEqualTo(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Number> Q lessThanOrEqualTo(final SingularAttribute<R, V> attribute, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.le(r.get(attribute), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V extends Comparable<? super V>> Q lessThanOrEqualTo(final CompositeAttribute<R, V> compositor, final V value) {
        QueryPredicate<R> predicate = (b, r, c) -> b.lessThanOrEqualTo(compositor.apply(r), value);
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q in(final SingularAttribute<R, V> attribute, final Collection<V> values) {
        QueryPredicate<R> predicate = (b, r, c) -> r.get(attribute).in(values);
        predicates.add(predicate);
        return this.self();
    }

    @SafeVarargs
    public final Q in(final SubQueryPredicate<R>... predicates) {
        QueryPredicate<R> predicate = (b, r, c) -> b.and(buildPredicates(b, r, c, predicates));
        this.predicates.add(predicate);
        return this.self();
    }

    public <V> Q in(final CompositeAttribute<R, V> compositor, final Collection<V> values) {
        QueryPredicate<R> predicate = (b, r, c) -> compositor.apply(r).in(values);
        this.predicates.add(predicate);
        return this.self();
    }

    @SafeVarargs
    public final Q and(final QueryPart<R>... queries) {
        QueryPredicate<R> predicate = (b, r, c) -> {
            var computedPredicates = Stream
                    .of(queries)
                    .map(q -> b.and(buildPredicates(b, r, c, predicates)))
                    .toArray(Predicate[]::new);
            return b.and(computedPredicates);
        };

        this.predicates.add(predicate);
        return this.self();
    }

    @SafeVarargs
    public final Q or(final QueryPart<R>... queries) {
        QueryPredicate<R> predicate = (b, r, c) -> {
            var computedPredicates = Stream
                    .of(queries)
                    .map(q -> b.or(buildPredicates(b, r, c, predicates)))
                    .toArray(Predicate[]::new);
            return b.or(computedPredicates);
        };

        this.predicates.add(predicate);
        return this.self();
    }


    protected static <T> Predicate[] buildPredicates(
            CriteriaBuilder builder,
            Path<T> path,
            CommonAbstractCriteria criteria,
            Collection<QueryPredicate<T>> predicates) {

        return predicates
                .stream()
                .map(q -> q.apply(builder, path, criteria))
                .toArray(Predicate[]::new);
    }

    protected static <T> Predicate[] buildPredicates(
            CriteriaBuilder builder,
            Path<T> path,
            CommonAbstractCriteria criteria,
            SubQueryPredicate<T>[] predicates) {

        return Stream.of(predicates)
                .map(q -> q.apply(builder, criteria))
                .map(path::in)
                .toArray(Predicate[]::new);
    }

}
