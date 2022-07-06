package com.github.happy.explorer.persistence;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;


/**
 * Generic and dynamic repository for explorer logic.
 */
@ApplicationScoped
public class ExplorerDAO {

    /**
     * Default entity manager.
     */
    @Inject
    private transient EntityManager em;

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public ExplorerDAO() {
        // NO-OP
    }

    /**
     * Remove an entity.
     *
     * @param entityClass Entity class
     * @param id          Unique identifier
     * @param <E>         Type of persistent entity
     */
    public <E> void remove(final Class<E> entityClass, final Object id) {
        var attachedEntity = this.em.getReference(entityClass, id);
        this.em.remove(attachedEntity);
    }

    /**
     * Remove an entity.
     *
     * @param attachedEntity Entity referenced in persistence context
     * @param <E>            Type of persistent entity
     */
    public <E> void remove(final E attachedEntity) {
        this.em.remove(attachedEntity);
    }

    /**
     * Save an entity.
     * If the entity already exists it will be updated, otherwise it will be inserted.
     *
     * @param entity Persistent entity
     * @param <E>    Type of persistent entity
     * @return The entity referenced by the persistence context
     */
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

    /**
     * Find an entity by unique identifier.
     *
     * @param entityClass Entity class
     * @param id          Unique identifier
     * @param <E>         Type of persistent entity
     * @return An option of persistent entity
     */
    public <E> Optional<E> find(final Class<E> entityClass, final Object id) {
        var entity = this.em.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    /**
     * Search entities.
     *
     * @param entityClass                 Entity class
     * @param queries                     Web queries
     * @param additionalCriteriaPredicate additional function for filtering data
     * @param <E>                         Type of persistent entity
     * @return A list of entity filtered
     */
    public <E> List<E> find(
            final Class<E> entityClass,
            final Set<FilterQuery> queries,
            final AdditionalCriteriaPredicate<E, E> additionalCriteriaPredicate) {

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

        return createQuery(this.em, entityClass, predicate, additionalCriteriaPredicate)
                .setFirstResult(startPosition)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Count entities.
     *
     * @param entityClass                 Entity class
     * @param queries                     Web queries
     * @param additionalCriteriaPredicate additional function for filtering data
     * @param <E>                         Type of persistent entity
     * @return The number of entity filtered
     */
    public <E> long size(
            final Class<E> entityClass,
            final Set<FilterQuery> queries,
            final AdditionalCriteriaPredicate<E, Long> additionalCriteriaPredicate) {

        CriteriaPredicate<E, Long> predicate = (b, r, q) -> {
            q.select(b.count(r));
            return buildPredicate(em, entityClass, b, r, queries);
        };

        return createQuery(this.em, entityClass, Long.class, predicate, additionalCriteriaPredicate)
                .getSingleResult();
    }

    /**
     * Check if an entity exists.
     *
     * @param entityClass Entity class
     * @param id          Unique identifier
     * @param <E>         Type of persistent entity
     * @return The value {@code true} if the entity exists, otherwise {@code false} is returned
     */
    public <E> boolean contains(final Class<E> entityClass, final Object id) {

        CriteriaPredicate<E, Long> predicate = (b, r, q) -> {
            q.select(b.count(r));
            var attribut = getPrimaryKeyAttribut(this.em, entityClass);
            return b.equal(r.get(attribut), id);
        };

        return createQuery(this.em, entityClass, Long.class, predicate, AdditionalCriteriaPredicate::empty)
                .getSingleResult() >= 1L;
    }

    /**
     * Check if an entity exists.
     *
     * @param entity Persistent entity
     * @param <E>    Type of persistent entity
     * @return The value {@code true} if the entity exists, otherwise {@code false} is returned
     */
    public <E> boolean contains(final E entity) {

        var entityClass = (Class<E>) entity.getClass();
        CriteriaPredicate<E, Long> predicate = (b, r, q) -> {
            q.select(b.count(r));
            var id = getPrimaryKey(entity);
            var attribut = getPrimaryKeyAttribut(this.em, entityClass);
            return b.equal(r.get(attribut), id);
        };

        return createQuery(this.em, entityClass, Long.class, predicate, AdditionalCriteriaPredicate::empty)
                .getSingleResult() >= 1L;

    }

    /**
     * Get the unique identifier.
     *
     * @param entity Persistent entity
     * @param <E>    Type of persistent entity
     * @param <K>    Type of unique identifier
     * @return The unique identifier
     */
    public <E, K> K getPrimaryKey(final E entity) {
        var puu = this.em.getEntityManagerFactory().getPersistenceUnitUtil();
        return (K) puu.getIdentifier(entity);
    }

    /**
     * Get the unique identifier attribut.
     *
     * @param em          Entity manager
     * @param entityClass Entity class
     * @param <E>         Type of persistent entity
     * @return The unique identifier attribut
     */
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

    /**
     * Construct the <i>JPA</i> query.
     *
     * @param em                          Entity manager
     * @param entityClass                 Entity class
     * @param targetClass                 Result query class
     * @param criteria                    Current predicate
     * @param additionalCriteriaPredicate Additional predicate
     * @param <E>                         Type of persistent entity
     * @param <R>                         Type of query return
     * @return The <i>JPA</i> query
     */
    private static <E, R> TypedQuery<R> createQuery(
            final EntityManager em,
            final Class<E> entityClass,
            final Class<R> targetClass,
            final CriteriaPredicate<E, R> criteria,
            final AdditionalCriteriaPredicate<E, R> additionalCriteriaPredicate) {

        var builder = em.getCriteriaBuilder();
        var query = builder.createQuery(targetClass);
        var root = query.from(entityClass);
        var predicate = criteria.toPredicate(builder, root, query);
        additionalCriteriaPredicate.toPredicate(predicate, builder, root, query);
        query.where(predicate);
        return em.createQuery(query);
    }

    /**
     * Construct the <i>JPA</i> query.
     *
     * @param em                          Entity manager
     * @param entityClass                 Entity class
     * @param criteria                    Current predicate
     * @param additionalCriteriaPredicate Additional predicate
     * @param <E>                         Type of persistent entity
     * @return The <i>JPA</i> query
     */
    private static <E> TypedQuery<E> createQuery(
            final EntityManager em,
            final Class<E> entityClass,
            final CriteriaPredicate<E, E> criteria,
            final AdditionalCriteriaPredicate<E, E> additionalCriteriaPredicate) {

        return createQuery(em, entityClass, entityClass, criteria, additionalCriteriaPredicate);
    }


    /**
     * Construct a <i>JPA ORDER BY</i> clause.
     *
     * @param queries Web queries
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param <E>     Type of persistent entity
     * @return A list of <i>JPA ORDER BY</i> clause
     */
    private static <E> List<Order> buildOrder(
            final Set<FilterQuery> queries,
            final CriteriaBuilder builder,
            final Root<E> root) {

        return queries
                .stream()
                .filter(FilterQuery::isSortQuery)
                .map(FilterQuery::getSortedValues)
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .map(e -> buildOrder(e, builder, root))
                .collect(Collectors.toList());
    }

    /**
     * Construct a <i>JPA ORDER BY</i> clause.
     *
     * @param attribute Entity attribut
     * @param builder   Criteria builder
     * @param root      Root clause of database query
     * @param <E>       Type of persistent entity
     * @return A <i>JPA ORDER BY</i> clause
     */
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

    /**
     * Construct a <i>JPA</i> predicate with all web queries.
     *
     * @param em          Entity manager
     * @param entityClass Entity class
     * @param builder     Criteria builder
     * @param root        Root clause of database query
     * @param queries     Web queries
     * @param <E>         Type of persistent entity
     * @return A complex <i>JPA</i> predicate
     */
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

    /**
     * Construct a <i>JPA</i> predicate from a web query.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Web query
     * @param <E>     Type of persistent entity
     * @return An option of complex <i>JPA</i> predicate
     */
    private static <E> Optional<Predicate> buildPredicate(
            final CriteriaBuilder builder,
            final Root<E> root,
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

    /**
     * Check the web query is applicable on this persistent entity.
     *
     * @param em          Entity manager
     * @param entityClass Entity class
     * @param query       Web query
     * @param <E>         Type of persistent entity
     * @return The value {@code true} if the web query is  applicable on this persistent entity otherwise the value {@code false} is returned
     */
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
