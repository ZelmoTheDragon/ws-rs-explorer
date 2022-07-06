package com.github.happi.explorer.persistence;

import java.text.Normalizer;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;

/**
 * Function for construct a database query with {@link FilterQuery}.
 *
 * @param <X> Type of persistent entity for root clause
 */
@FunctionalInterface
interface BasicCriteriaPredicate<X> {

    /**
     * Create a predicate with {@link FilterQuery} data.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @return A predicate with {@link FilterQuery} data
     */
    Predicate toPredicate(CriteriaBuilder builder, Root<X> root, FilterQuery query);

    /**
     * Create an <b>EQUAL</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate equal(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        var attribute = root.<V>get(query.getName());
        var type = attribute.getModel().getBindableJavaType();

        return Stream.of(query)
                .flatMap(q -> Queries.asValues(type, q).entrySet().stream())
                .map(e -> e.getValue()
                        .stream()
                        .map(v -> builder.equal(attribute, v))
                        .reduce((a, v) -> e.getKey().reducer().apply(builder, a, v))
                )
                .flatMap(Optional::stream)
                .reduce(builder::and)
                .orElseGet(() -> builder.isNull(attribute));
    }

    /**
     * Create an <b>NOT EQUAL</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate notEqual(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        return BasicCriteriaPredicate.equal(builder, root, query).not();
    }

    /**
     * Create a <b>LIKE</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @return A predicate with {@link FilterQuery} data
     */
    static <X> Predicate like(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        var attribute = root.<String>get(query.getName());

        return Stream.of(query)
                .flatMap(q -> Queries.asValues(String.class, q).entrySet().stream())
                .map(e -> e.getValue()
                        .stream()
                        .map(v -> String.valueOf(v).toLowerCase())
                        .map(BasicCriteriaPredicate::stripAccent)
                        .map(v -> "%" + v + "%")
                        .map(v -> builder.like(builder.lower(attribute), v))
                        .reduce((a, v) -> e.getKey().reducer().apply(builder, a, v))
                )
                .flatMap(Optional::stream)
                .reduce(builder::and)
                .orElseGet(() -> builder.isNull(attribute));
    }

    /**
     * Create a <b>NOT LIKE</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @return A predicate with {@link FilterQuery} data
     */
    static <X> Predicate notLike(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        return BasicCriteriaPredicate.like(builder, root, query).not();
    }

    /**
     * Create a <b>GREATER THAN</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate greaterThan(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        var attribute = root.<V>get(query.getName());
        var type = attribute.getModel().getBindableJavaType();

        return Stream.of(query)
                .flatMap(q -> Queries.asValues(type, q).entrySet().stream())
                .map(e -> e.getValue()
                        .stream()
                        .map(v -> builder.greaterThan(attribute, v))
                        .reduce((a, v) -> e.getKey().reducer().apply(builder, a, v))
                )
                .flatMap(Optional::stream)
                .reduce(builder::and)
                .orElseGet(() -> builder.isNull(attribute));
    }

    /**
     * Create a <b>GREATER THAN OR EQUAL</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate greaterThanOrEqual(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        var attribute = root.<V>get(query.getName());
        var type = attribute.getModel().getBindableJavaType();

        return Stream.of(query)
                .flatMap(q -> Queries.asValues(type, q).entrySet().stream())
                .map(e -> e.getValue()
                        .stream()
                        .map(v -> builder.greaterThanOrEqualTo(attribute, v))
                        .reduce((a, v) -> e.getKey().reducer().apply(builder, a, v))
                )
                .flatMap(Optional::stream)
                .reduce(builder::and)
                .orElseGet(() -> builder.isNull(attribute));
    }

    /**
     * Create a <b>LESS THAN</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate lessThan(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        var attribute = root.<V>get(query.getName());
        var type = attribute.getModel().getBindableJavaType();

        return Stream.of(query)
                .flatMap(q -> Queries.asValues(type, q).entrySet().stream())
                .map(e -> e.getValue()
                        .stream()
                        .map(v -> builder.lessThan(attribute, v))
                        .reduce((a, v) -> e.getKey().reducer().apply(builder, a, v))
                )
                .flatMap(Optional::stream)
                .reduce(builder::and)
                .orElseGet(() -> builder.isNull(attribute));
    }

    /**
     * Create a <b>LESS THAN OR EQUAL</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate lessThanOrEqual(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        var attribute = root.<V>get(query.getName());
        var type = attribute.getModel().getBindableJavaType();

        return Stream.of(query)
                .flatMap(q -> Queries.asValues(type, q).entrySet().stream())
                .map(e -> e.getValue()
                        .stream()
                        .map(v -> builder.lessThanOrEqualTo(attribute, v))
                        .reduce((a, v) -> e.getKey().reducer().apply(builder, a, v))
                )
                .flatMap(Optional::stream)
                .reduce(builder::and)
                .orElseGet(() -> builder.isNull(attribute));
    }

    /**
     * Create an <b>IN</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate in(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        var attribute = root.<V>get(query.getName());
        return attribute.in(query.getValues());
    }

    /**
     * Create a <b>NOT IN</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate notIn(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        return BasicCriteriaPredicate.in(builder, root, query).not();
    }

    /**
     * Create a <b>BETWEEN</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate between(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        var attribute = root.<V>get(query.getName());
        var type = attribute.getModel().getBindableJavaType();

        return builder.between(
                attribute,
                Queries.asValue(type, query.getBetweenFirstValue()),
                Queries.asValue(type, query.getBetweenSecondValue())
        );
    }

    /**
     * Create a <b>NOT BETWEEN</b> <i>JPA</i> criteria predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @param <V>     Type of attribut
     * @return A predicate with {@link FilterQuery} data
     */
    static <X, V extends Comparable<V>> Predicate notBetween(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        return BasicCriteriaPredicate.between(builder, root, query).not();
    }

    /**
     * Create a keyword search <i>JPA</i> criteria predicate.
     * The query filter only in string entity attribute.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @param <X>     Type of persistent entity for root clause
     * @return A predicate with {@link FilterQuery} data
     */
    static <X> Predicate keyword(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        return root
                .getModel()
                .getAttributes()
                .stream()
                .filter(a -> Objects.equals(a.getPersistentAttributeType(), Attribute.PersistentAttributeType.BASIC))
                .filter(a -> Objects.equals(a.getJavaType(), String.class))
                .map(a -> root.<String>get(a.getName()))
                .map(a -> builder.like(builder.lower(a), stripAccent(query.getSingleValue())))
                .reduce(builder::and)
                .orElseGet(builder::and);
    }

    /**
     * Escape a string.
     * Useful for {@link #keyword} method
     *
     * @param word Any string
     * @return A string in lower case and accent replaced by {@code _}
     */
    private static String stripAccent(final String word) {
        return Normalizer
                .normalize(word, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "_")
                .toLowerCase();
    }

}
