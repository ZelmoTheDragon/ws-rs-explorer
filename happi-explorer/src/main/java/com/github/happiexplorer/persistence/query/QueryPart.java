package com.github.happiexplorer.persistence.query;

public final class QueryPart<R> extends BaseQuery<R, QueryPart<R>> {

    QueryPart() {
    }

    @Override
    protected QueryPart<R> self() {
        return this;
    }
}
