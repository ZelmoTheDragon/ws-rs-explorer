package com.github.ws.rs.explorer.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@FunctionalInterface
interface CriteriaPredicate<E, R> {

    Predicate toPredicate(CriteriaBuilder builder, Root<E> root, CriteriaQuery<R> query);
}
