package com.github.ws.rs.explorer.persistence;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for web query.
 */
public final class Queries {

    /**
     * Default page number for pagination filter.
     */
    private static final int DEFAULT_PAGE_NUMBER = 1;

    /**
     * Default page size for pagination filter.
     */
    private static final int DEFAULT_PAGE_SIZE = 100;

    /**
     * String converter for basic types.
     */
    private static final Map<Class<?>, Function<String, Object>> CONVERTERS = Map.of(
            Boolean.class, Boolean::parseBoolean,
            Byte.class, Byte::parseByte,
            Short.class, Short::parseShort,
            Integer.class, Integer::parseInt,
            Long.class, Long::parseLong,
            Float.class, Float::parseFloat,
            Double.class, Double::parseDouble,
            String.class, String::valueOf
    );

    /**
     * Internal constructor.
     * Instance not allowed.
     */
    private Queries() {
        throw new UnsupportedOperationException("Instance not allowed");
    }

    /**
     * Convert a raw value in the correct type.
     *
     * @param type     Desired basic type
     * @param rawValue Raw value
     * @param <T>      Basic type
     * @return The raw value converted
     */
    static <T> T asValue(final Class<T> type, final String rawValue) {
        return (T) CONVERTERS.get(type).apply(rawValue);
    }

    /**
     * Convert a raw value in the correct type.
     *
     * @param type  Desired basic type
     * @param query Web query
     * @param <T>   Basic type
     * @return The raw value converted
     */
    static <T> T asValue(final Class<T> type, final FilterQuery query) {
        return (T) CONVERTERS.get(type).apply(query.getSingleValue());
    }

    /**
     * Convert a raw value in the correct type.
     *
     * @param type  Desired basic type
     * @param query Web query
     * @param <T>   Basic type
     * @return The raw value converted as list
     */
    static <T> List<T> asValues(final Class<T> type, final FilterQuery query) {

        return query.getValues()
                .stream()
                .map(s -> (T) CONVERTERS.get(type).apply(s))
                .collect(Collectors.toList());
    }

    /**
     * Check if web queries has a distinct query to {@code true}.
     *
     * @param queries Web queries
     * @return The value {@code true} if web queries has a distinct query otherwise the value {@code false} is returned
     */
    static boolean isDistinct(final Set<FilterQuery> queries) {
        return queries
                .stream()
                .filter(FilterQuery::isQueryDistinct)
                .map(q -> asValue(Boolean.class, q))
                .findFirst()
                .orElse(Boolean.FALSE);
    }

    /**
     * Check if web queries has a page number query to {@code true}.
     *
     * @param queries Web queries
     * @return The page number if web queries has a page number query otherwise the value {@link #DEFAULT_PAGE_NUMBER} is returned
     */
    public static int getPageNumber(final Set<FilterQuery> queries) {
        return queries
                .stream()
                .filter(FilterQuery::isPageNumberQuery)
                .map(q -> asValue(Integer.class, q))
                .findFirst()
                .orElse(DEFAULT_PAGE_NUMBER);
    }

    /**
     * Check if web queries has a page size query to {@code true}.
     *
     * @param queries Web queries
     * @return The value {@code true} if web queries has a page size query otherwise the value {@link #DEFAULT_PAGE_SIZE} is returned
     */
    public static int getPageSize(final Set<FilterQuery> queries) {
        return queries
                .stream()
                .filter(FilterQuery::isPageSizeQuery)
                .map(q -> asValue(Integer.class, q))
                .findFirst()
                .orElse(DEFAULT_PAGE_SIZE);
    }

    /**
     * Compute the page count based on the page size.
     *
     * @param queries Web queries
     * @param count   Number of element based of web queries
     * @return The page count
     */
    public static int getPageCount(final Set<FilterQuery> queries, final long count) {
        var pageSize = Queries.getPageSize(queries);
        return (int) ((count - 1) / pageSize) + 1;
    }

    /**
     * Convert raw parameters to web queries.
     *
     * @param parameters Raw parameters from URL
     * @return A sef of web queries
     */
    public static Set<FilterQuery> convertQueries(final Map<String, List<String>> parameters) {

        return parameters
                .entrySet()
                .stream()
                .map(e -> convertQuery(e.getKey(), e.getValue()))
                .collect(Collectors.toSet());
    }

    /**
     * Convert raw parameter to web query.
     *
     * @param parameterName Attribut name with or without operator symbol
     * @param values        Values
     * @return A web query
     */
    public static FilterQuery convertQuery(final String parameterName, final List<String> values) {

        var bracketOpen = parameterName.indexOf("[");
        var bracketClose = parameterName.indexOf("]");

        String name;
        Operator operator;

        if (bracketOpen == -1 || bracketClose == -1 || bracketOpen > bracketClose) {
            name = parameterName;
            operator = Operator.NONE;
        } else {
            name = parameterName.substring(0, bracketOpen);
            var rawOperator = parameterName.substring(bracketOpen + 1, bracketClose);
            operator = Operator.parse(rawOperator);
        }
        return new FilterQuery(name, values, operator);
    }


}
