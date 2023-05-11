package com.github.happiexplorer.persistence.query;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;

@FunctionalInterface
public interface CompositeAttribute<R, V> {

    Expression<V> apply(Path<R> root);

}
