package com.github.happiexplorer.persistence.query;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.function.Function;

/**
 * Sub query.
 *
 * @param <R> Target entity class of the sub query
 */
public final class SubQuery<R> extends BaseQuery<R, SubQuery<R>> {

    /**
     * Entity class of the sub query.
     */
    private final Class<R> targetType;

    /**
     * Constructor.
     *
     * @param targetType Entity class of the sub query
     */
    SubQuery(final Class<R> targetType) {
        this.targetType = targetType;
    }

    @Override
    protected SubQuery<R> self() {
        return this;
    }

    /**
     * @return
     */
    public SubQueryPredicate<R> select() {
        return this.buildPredicate(this.targetType, r -> r);
    }

    /**
     * @param attribute
     * @param <P>
     * @return
     */
    public <P> SubQueryPredicate<P> select(final SingularAttribute<R, P> attribute) {
        return this.buildPredicate(attribute.getJavaType(), r -> r.get(attribute));
    }

    /**
     * Construct the query with the given predicates.
     *
     * @param selectType   Entity class of the sub query selection
     * @param pathFunction Function for selection transformation
     * @param <P>          Type of the entity sub query
     * @return A sub query as predicate, can be included in the overall query
     */
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
