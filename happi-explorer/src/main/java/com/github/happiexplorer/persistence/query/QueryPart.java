package com.github.happiexplorer.persistence.query;

/**
 * A simple part of query for adding predicate to overall query.
 *
 * @param <R> Target entity class
 */
public final class QueryPart<R> extends BaseQuery<R, QueryPart<R>> {

    /**
     * Constructor.
     * Use factor method {@link QueryPart#of()}.
     */
    QueryPart() {
        // NO-OP
    }

    @Override
    protected QueryPart<R> self() {
        return this;
    }

    /**
     * Create a new query part for predicate of the overall query.
     *
     * @param <R> Target entity class
     * @return A new query part
     */
    public static <R> QueryPart<R> of() {
        return new QueryPart<>();
    }

}
