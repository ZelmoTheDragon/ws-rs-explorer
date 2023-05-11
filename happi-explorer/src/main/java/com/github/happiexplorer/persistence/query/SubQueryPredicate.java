package com.github.happiexplorer.persistence.query;


import jakarta.persistence.criteria.*;

@FunctionalInterface
interface SubQueryPredicate<R> {

    Subquery<R> apply(CriteriaBuilder builder, CommonAbstractCriteria criteria);
}
