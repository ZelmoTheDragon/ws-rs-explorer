package com.github.happiexplorer.persistence.query;


import jakarta.persistence.criteria.*;

@FunctionalInterface
public interface SubQueryPredicate<R> {

    Subquery<R> apply(CriteriaBuilder builder, CommonAbstractCriteria criteria);
}
