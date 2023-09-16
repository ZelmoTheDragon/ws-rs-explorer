package com.github.happiexplorer.persistence.query;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;

/**
 * Function for creating composite attribute.
 * Can simulate a join by combining multiple path.
 *
 * @param <R> Target entity class
 * @param <V> Target attribute type
 */
@FunctionalInterface
public interface CompositeAttribute<R, V> {

    Expression<V> apply(Path<R> root);

}
