package com.github.happiexplorer.persistence.query;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.Collection;

public final class QueryBuilder {

    private final EntityManager manager;

    private QueryBuilder(final EntityManager manager) {
        this.manager = manager;
    }


    public <T> SelectQuery<T> select(final Class<T> targetEntity) {
        return new SelectQuery<>(this.manager, targetEntity);
    }

    public <T> UpdateQuery<T> update(final Class<T> targetEntity) {
        return new UpdateQuery<>(this.manager, targetEntity);
    }

    public <T> DeleteQuery<T> delete(final Class<T> targetEntity) {
        return new DeleteQuery<>(this.manager, targetEntity);
    }

    public static QueryBuilder of(final EntityManager manager) {
        return new QueryBuilder(manager);
    }

    public static <R> SubQuery<R> subquery(final Class<R> type) {
        return new SubQuery<>(type);
    }

    public static <R> QueryPart<R> isTrue(final SingularAttribute<R, Boolean> attribute) {
        return new QueryPart<R>().isTrue(attribute);
    }

    public static <R> QueryPart<R> isFalse(final SingularAttribute<R, Boolean> attribute) {
        return new QueryPart<R>().isFalse(attribute);
    }

    public static <R, V> QueryPart<R> isNull(final SingularAttribute<R, V> attribute) {
        return new QueryPart<R>().isNull(attribute);
    }

    public static <R, V> QueryPart<R> isNotNull(final SingularAttribute<R, V> attribute) {
        return new QueryPart<R>().isNotNull(attribute);
    }

    public static <R, V> QueryPart<R> equal(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().equal(attribute, value);
    }

    public static <R, V> QueryPart<R> notEqual(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().notEqual(attribute, value);
    }

    public static <R> QueryPart<R> like(final SingularAttribute<R, String> attribute, final String value) {
        return new QueryPart<R>().like(attribute, value);
    }

    public static <R> QueryPart<R> notLike(final SingularAttribute<R, String> attribute, final String value) {
        return new QueryPart<R>().notLike(attribute, value);
    }

    public static <R, V extends Comparable<? super V>> QueryPart<R> between(final SingularAttribute<R, V> attribute, final V v0, final V v1) {
        return new QueryPart<R>().between(attribute, v0, v1);
    }

    public static <R, V extends Comparable<? super V>> QueryPart<R> greaterThan(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().greaterThan(attribute, value);
    }

    public static <R, V extends Number> QueryPart<R> greaterThan(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().greaterThan(attribute, value);
    }

    public static <R, V extends Comparable<? super V>> QueryPart<R> greaterThanOrEqualTo(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().greaterThanOrEqualTo(attribute, value);
    }

    public static <R, V extends Number> QueryPart<R> greaterThanOrEqualTo(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().greaterThanOrEqualTo(attribute, value);
    }

    public static <R, V extends Comparable<? super V>> QueryPart<R> lessThan(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().lessThan(attribute, value);
    }

    public static <R, V extends Number> QueryPart<R> lessThan(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().lessThan(attribute, value);
    }

    public static <R, V extends Comparable<? super V>> QueryPart<R> lessThanOrEqualTo(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().lessThanOrEqualTo(attribute, value);
    }

    public static <R, V extends Number> QueryPart<R> lessThanOrEqualTo(final SingularAttribute<R, V> attribute, final V value) {
        return new QueryPart<R>().lessThanOrEqualTo(attribute, value);
    }

    public static <R, V> QueryPart<R> in(final SingularAttribute<R, V> attribute, final Collection<V> values) {
        return new QueryPart<R>().in(attribute, values);
    }

    @SafeVarargs
    public static <R> QueryPart<R> in(final SubQueryPredicate<R>... predicates) {
        return new QueryPart<R>().in(predicates);
    }

    @SafeVarargs
    public static <R> QueryPart<R> and(final QueryPart<R>... queries) {
        return new QueryPart<R>().and(queries);
    }

    @SafeVarargs
    public static <R> QueryPart<R> or(final QueryPart<R>... queries) {
        return new QueryPart<R>().or(queries);
    }

}
