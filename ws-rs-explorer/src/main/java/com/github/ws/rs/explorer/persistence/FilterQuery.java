package com.github.ws.rs.explorer.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Web query.
 */
public final class FilterQuery {

    /**
     * Web query parameter for order by.
     */
    private static final String ORDER_BY_QUERY = "orderBy";

    /**
     * Web query parameter for page size.
     */
    private static final String PAGE_SIZE_QUERY = "pageSize";

    /**
     * Web query parameter for page number.
     */
    private static final String PAGE_NUMBER_QUERY = "pageNumber";

    /**
     * Web query parameter for distinct result.
     */
    private static final String DISTINCT_QUERY = "distinct";

    /**
     * Web parameter for keyword search.
     */
    private static final String KEYWORD_QUERY = "keyword";

    /**
     * Web parameter for attribut selection.
     */
    private static final String SELECT_QUERY = "select";

    /**
     * Prefix symbol for ascending order.
     */
    private static final String ORDER_ASC_SYMBOL = "+";

    /**
     * Prefix symbol for descending order.
     */
    private static final String ORDER_DESC_SYMBOL = "-";

    /**
     * Logical operator <i>OR</i> symbol in value.
     */
    private static final String OR_SYMBOL = "|";

    /**
     * Index of the first element in between web query.
     */
    private static final int BETWEEN_FIRST_ARGUMENT = 0;

    /**
     * Index of the second element in between web query.
     */
    private static final int BETWEEN_SECOND_ARGUMENT = 1;

    /**
     * Attribut name.
     */
    private final String name;

    /**
     * Values.
     */
    private final List<String> values;

    /**
     * Logical operator.
     */
    private final Operator operator;

    /**
     * Construct a web query.
     *
     * @param name     Attribut name
     * @param values   Values
     * @param operator Logical operator
     */
    FilterQuery(final String name, final List<String> values, final Operator operator) {
        this.name = name;
        this.values = List.copyOf(values);
        this.operator = operator;
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq;
        if (this == obj) {
            eq = true;
        } else if (obj == null || getClass() != obj.getClass()) {
            eq = false;
        } else {
            var webQuery = (FilterQuery) obj;
            eq = Objects.equals(name, webQuery.name)
                    && Objects.equals(values, webQuery.values)
                    && operator == webQuery.operator;
        }
        return eq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, values, operator);
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getSimpleName())
                .append("{name='").append(name).append('\'')
                .append(", values='").append(values).append('\'')
                .append(", operator=").append(operator)
                .append('}').toString();
    }

    // Getter...

    String getName() {
        return name;
    }

    List<String> getValues() {
        return List.copyOf(values);
    }

    Operator getOperator() {
        return operator;
    }

    boolean isBasicQuery() {
        return Objects.equals(this.operator, Operator.NONE);
    }

    boolean isSortQuery() {
        return this.isBasicQuery() && Objects.equals(this.name, ORDER_BY_QUERY);
    }

    boolean isPageNumberQuery() {
        return this.isBasicQuery() && Objects.equals(this.name, PAGE_NUMBER_QUERY);
    }

    boolean isPageSizeQuery() {
        return this.isBasicQuery() && Objects.equals(this.name, PAGE_SIZE_QUERY);
    }

    boolean isQueryDistinct() {
        return this.isBasicQuery() && Objects.equals(this.name, DISTINCT_QUERY);
    }

    boolean isKeywordQuery() {
        return this.isBasicQuery() && Objects.equals(this.name, KEYWORD_QUERY);
    }

    boolean isSelectQuery() {
        return this.isBasicQuery() && Objects.equals(this.name, SELECT_QUERY);
    }

    String getBetweenFirstValue() {
        return this.values.get(BETWEEN_FIRST_ARGUMENT);
    }

    String getBetweenSecondValue() {
        return this.values.get(BETWEEN_SECOND_ARGUMENT);
    }

    Map<String, Boolean> getSortedValues() {
        var orders = new HashMap<String, Boolean>();
        for (var v : this.values) {
            var name = String.valueOf(v);
            if (name.startsWith(ORDER_DESC_SYMBOL)) {
                name = name.replace(ORDER_DESC_SYMBOL, "");
                orders.put(name, Boolean.FALSE);
            } else if (name.startsWith(ORDER_ASC_SYMBOL)) {
                name = name.replace(ORDER_ASC_SYMBOL, "");
                orders.put(name, Boolean.TRUE);
            } else {
                orders.put(name, Boolean.TRUE);
            }
        }
        return orders;
    }

    String getSingleValue() {
        String value;
        if (!this.values.isEmpty()) {
            value = this.values.get(0);
        } else {
            value = null;
        }
        return value;
    }

}
