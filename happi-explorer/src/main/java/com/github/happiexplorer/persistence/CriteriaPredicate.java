package com.github.happiexplorer.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Function for construct a <i>JPA</i> criteria predicate using functional programming.
 *
 * @param <E> Type of persistent entity
 * @param <R> Type of query return
 */
@FunctionalInterface
interface CriteriaPredicate<E, R> {

    /**
     * Create a predicate.
     *
     * @param builder Criteria builder
     * @param root    Root clause of database query
     * @param query   Current query
     * @return A predicate
     */
    Predicate toPredicate(CriteriaBuilder builder, Root<E> root, CriteriaQuery<R> query);
}
