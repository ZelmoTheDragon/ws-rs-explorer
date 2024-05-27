package com.github.happiexplorer.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Function for additional <i>JPA</i> criteria predicate.
 *
 * @param <E> Type of persistent entity
 * @param <R> Type of query return
 */
@FunctionalInterface
public interface AdditionalCriteriaPredicate<E, R> {

    /**
     * Append additional predicates to the current predicate.
     *
     * @param currentPredicate Current predicate
     * @param builder          Criteria builder
     * @param root             Root clause of database query
     * @param query            Current query
     * @return A new predicate combining the current predicate and additional predicates
     */
    Predicate toPredicate(
            Predicate currentPredicate,
            CriteriaBuilder builder,
            Root<E> root,
            CriteriaQuery<R> query
    );

    /**
     * Function without behavior.
     *
     * @param currentPredicate Current predicate
     * @param builder          Criteria builder
     * @param root             Root clause of database query
     * @param query            Current query
     * @param <E>              Type of persistent entity
     * @param <R>              Type of query return
     * @return The current predicate without modification
     */
    static <E, R> Predicate empty(
            final Predicate currentPredicate,
            final CriteriaBuilder builder,
            final Root<E> root,
            final CriteriaQuery<R> query) {

        return currentPredicate;
    }

}
