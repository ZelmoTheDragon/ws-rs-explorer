package com.github.ws.rs.explorer.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@FunctionalInterface
public interface AdditionalCriteriaPredicate<E, R> {

    Predicate toPredicate(
            Predicate currentPredicate,
            CriteriaBuilder builder,
            Root<E> root,
            CriteriaQuery<R> query
    );

    static <E, R> Predicate empty(
            final Predicate currentPredicate,
            final CriteriaBuilder builder,
            final Root<E> root,
            final CriteriaQuery<R> query) {

        return currentPredicate;
    }

}
