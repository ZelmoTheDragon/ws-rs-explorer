package com.github.happiexplorer.persistence.query;


import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

@FunctionalInterface
interface QueryPredicate<R> {

    Predicate apply(CriteriaBuilder builder, Path<R> root, CommonAbstractCriteria criteria);
}
