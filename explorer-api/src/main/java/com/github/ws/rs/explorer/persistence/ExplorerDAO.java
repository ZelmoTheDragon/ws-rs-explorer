package com.github.ws.rs.explorer.persistence;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;


@Singleton
public class ExplorerDAO implements Serializable {

    private transient final EntityManager em;

    @Inject
    public ExplorerDAO(final EntityManager em) {
        this.em = em;
    }

    public <E> void remove(final Class<E> entityClass, final Object id) {
        var attachedEntity = this.em.getReference(entityClass, id);
        this.em.remove(attachedEntity);
    }

    public <E> E save(final E entity) {
        E managedEntity;
        if (this.em.contains(entity)) {
            managedEntity = this.em.merge(entity);
        } else {
            this.em.persist(entity);
            managedEntity = entity;
        }
        return managedEntity;
    }

    public <E> Optional<E> find(final Class<E> entityClass, final Object id) {
        var entity = this.em.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    public <E> List<E> find(
            final Class<E> entityClass,
            final Set<FilterQuery> queries) {

        var distinct = Queries.isDistinct(queries);

        CriteriaPredicate<E, E> predicate = (b, r, q) -> {
            q.distinct(distinct);
            q.select(r);
            var orders = buildOrder(queries, b, r);
            q.orderBy(orders);
            return buildPredicate(em, entityClass, b, r, queries);
        };

        var pageSize = Queries.getPageSize(queries);
        var pageNumber = Queries.getPageNumber(queries);
        var startPosition = Math.max(0, (pageNumber - 1) * pageSize);

        return createQuery(this.em, entityClass, predicate)
                .setFirstResult(startPosition)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public  <E> long size(
            final Class<E> entityClass,
            final Set<FilterQuery> queries) {

        CriteriaPredicate<E, Long> predicate = (b, r, q) -> {
            q.select(b.count(r));
            return buildPredicate(em, entityClass, b, r, queries);
        };

        return createQuery(this.em, entityClass, Long.class, predicate)
                .getSingleResult();
    }

    public  <E> boolean contains(final E entity) {

        var entityClass = (Class<E>) entity.getClass();
        CriteriaPredicate<E, Long> predicate = (b, r, q) -> {
            q.select(b.count(r));
            var id = getPrimaryKey(entity);
            var attribut = getPrimaryKeyAttribut(this.em, entityClass);
            return b.equal(r.get(attribut), id);
        };

        return createQuery(this.em, entityClass, Long.class, predicate)
                .getSingleResult() >= 1L;

    }

    public <E, K> K getPrimaryKey(final E entity) {
        var puu = this.em.getEntityManagerFactory().getPersistenceUnitUtil();
        return (K) puu.getIdentifier(entity);
    }

    private static <E> SingularAttribute<E, ?> getPrimaryKeyAttribut(
            final EntityManager em,
            final Class<E> entityClass) {

        return em.getMetamodel()
                .entity(entityClass)
                .getDeclaredSingularAttributes()
                .stream()
                .filter(SingularAttribute::isId)
                .findFirst()
                .orElseThrow(() -> new PersistenceException("No primary key attribut found in class: " + entityClass));
    }

    private static <E, R> TypedQuery<R> createQuery(
            final EntityManager em,
            final Class<E> entityClass,
            final Class<R> targetClass,
            final CriteriaPredicate<E, R> criteria) {

        var builder = em.getCriteriaBuilder();
        var query = builder.createQuery(targetClass);
        var root = query.from(entityClass);
        var predicate = criteria.toPredicate(builder, root, query);
        query.where(predicate);
        return em.createQuery(query);
    }

    private static <E> TypedQuery<E> createQuery(
            final EntityManager em,
            final Class<E> entityClass,
            final CriteriaPredicate<E, E> criteria) {

        return createQuery(em, entityClass, entityClass, criteria);
    }


    private static <E> List<Order> buildOrder(
            final Set<FilterQuery> queries,
            final CriteriaBuilder builder,
            final Root<E> root) {

        return queries
                .stream()
                .filter(FilterQuery::isSortQuery)
                .map(FilterQuery::getSortedValues)
                .map(Map::entrySet)
                .map(Set::stream)
                .map(e -> (Map.Entry<String, Boolean>) e)
                .map(e -> buildOrder(e, builder, root))
                .collect(Collectors.toList());
    }

    private static <E> Order buildOrder(
            final Map.Entry<String, Boolean> attribute,
            final CriteriaBuilder builder,
            final Root<E> root) {

        Order order;
        if (Objects.equals(attribute.getValue(), Boolean.TRUE)) {
            order = builder.asc(root.get(attribute.getKey()));
        } else {
            order = builder.desc(root.get(attribute.getKey()));
        }
        return order;
    }

    private static <E> Predicate buildPredicate(
            final EntityManager em,
            final Class<E> entityClass,
            final CriteriaBuilder builder,
            final Root<E> root,
            final Set<FilterQuery> queries) {


        return builder.and(queries
                .stream()
                .filter(q -> isSafeSearchQuery(em, entityClass, q))
                .map(q -> buildPredicate(builder, root, q))
                .filter(Optional::isPresent)
                .flatMap(Optional::stream)
                .toArray(Predicate[]::new)
        );
    }

    private static <X> Optional<Predicate> buildPredicate(
            final CriteriaBuilder builder,
            final Root<X> root,
            final FilterQuery query) {

        Predicate predicate;
        if (!query.isBasicQuery()) {
            predicate = switch (query.getOperator()) {
                case EQUAL -> BasicCriteriaPredicate.equal(builder, root, query);
                case NOT_EQUAL -> BasicCriteriaPredicate.notEqual(builder, root, query);
                case LIKE -> BasicCriteriaPredicate.like(builder, root, query);
                case NOT_LIKE -> BasicCriteriaPredicate.notLike(builder, root, query);
                case GREATER_THAN -> BasicCriteriaPredicate.greaterThan(builder, root, query);
                case GREATER_THAN_OR_EQUAL -> BasicCriteriaPredicate.greaterThanOrEqual(builder, root, query);
                case LESS_THAN -> BasicCriteriaPredicate.lessThan(builder, root, query);
                case LESS_THAN_OR_EQUAL -> BasicCriteriaPredicate.lessThanOrEqual(builder, root, query);
                case IN -> BasicCriteriaPredicate.in(builder, root, query);
                case NOT_IN -> BasicCriteriaPredicate.notIn(builder, root, query);
                case BETWEEN -> BasicCriteriaPredicate.between(builder, root, query);
                case NOT_BETWEEN -> BasicCriteriaPredicate.notBetween(builder, root, query);
                default -> null;
            };
        } else if (query.isKeywordQuery()) {
            predicate = BasicCriteriaPredicate.keyword(builder, root, query);
        } else {
            predicate = null;
        }
        return Optional.ofNullable(predicate);
    }

    private static <E> boolean isSafeSearchQuery(
            final EntityManager em,
            final Class<E> entityClass,
            final FilterQuery query) {

        return em
                .getMetamodel()
                .entity(entityClass)
                .getAttributes()
                .stream()
                .filter(a -> Objects.equals(a.getPersistentAttributeType(), Attribute.PersistentAttributeType.BASIC))
                .anyMatch(a -> Objects.equals(a.getName(), query.getName()));
    }

}
