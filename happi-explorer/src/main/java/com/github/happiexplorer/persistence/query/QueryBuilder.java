package com.github.happiexplorer.persistence.query;

import jakarta.persistence.EntityManager;

public final class QueryBuilder {

    private final EntityManager manager;

    private QueryBuilder(final EntityManager manager) {
        this.manager = manager;
    }


    public <T> SelectQuery<T> select(final Class<T> targetEntity) {
        return new SelectQuery<>(this.manager, targetEntity);
    }

    public <T> UpdateQuery<T> update(final Class<T> targetEntity) {
        return new UpdateQuery<>(this.manager, targetEntity);
    }

    public <T> DeleteQuery<T> delete(final Class<T> targetEntity) {
        return new DeleteQuery<>(this.manager, targetEntity);
    }

    public static QueryBuilder of(final EntityManager manager) {
        return new QueryBuilder(manager);
    }

    public static <R> SubQuery<R> subquery(final Class<R> type) {
        return new SubQuery<>(type);
    }

    public static <R> QueryPart<R> with() {
        return new QueryPart<>();
    }


}
