package com.github.happiexplorer.persistence.query;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.function.Function;

public class SubQuery<R> extends BaseQuery<R, SubQuery<R>> {

    private final Class<R> targetType;

    SubQuery(final Class<R> targetType) {
        this.targetType = targetType;
    }

    @Override
    protected SubQuery<R> self() {
        return this;
    }

    public SubQueryPredicate<R> select() {
        return this.buildPredicate(this.targetType, r -> r);
    }

    public <P> SubQueryPredicate<P> select(final SingularAttribute<R, P> attribute) {
        return this.buildPredicate(attribute.getJavaType(), r -> r.get(attribute));
    }

    private <P> SubQueryPredicate<P> buildPredicate(
            final Class<P> selectType,
            final Function<Root<R>, Path<P>> pathFunction) {

        return (b, c) -> {
            var subquery = c.subquery(selectType);
            var subqueryRoot = subquery.from(targetType);
            var selection = pathFunction.apply(subqueryRoot);
            var computedPredicates = buildPredicates(b, subqueryRoot, c, this.predicates);
            return subquery
                    .select(selection)
                    .where(computedPredicates);
        };
    }

}
