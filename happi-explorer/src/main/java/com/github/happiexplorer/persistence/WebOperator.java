package com.github.happiexplorer.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

/**
 * Logical operator for a web query.
 */
enum WebOperator {

    /**
     * Logical <i>AND</i>.
     */
    AND(CriteriaBuilder::and),

    /**
     * Logical <i>OR</i>.
     */
    OR(CriteriaBuilder::or);

    /**
     * Reducer operation.
     */
    private final Reducer reducer;

    /**
     * Internal constructor
     *
     * @param reducer Reducer operation
     */
    WebOperator(final Reducer reducer) {
        this.reducer = reducer;
    }

    /**
     * Get the logical reducer operation.
     *
     * @return The reducer
     */
    public Reducer reducer() {
        return reducer;
    }

    /**
     * A function to combine predicate according to the web operator.
     */
    @FunctionalInterface
    interface Reducer {

        /**
         * Combine predicates according to the web operator.
         *
         * @param builder     Criteria builder
         * @param accumulator Previous predicate accumulator
         * @param next        Next predicate to combine
         * @return A combined predicate
         */
        Predicate apply(CriteriaBuilder builder, Predicate accumulator, Predicate next);
    }
}
