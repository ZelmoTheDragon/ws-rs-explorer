package com.github.happiexplorer.persistence;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for a web query.
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
     * Instance does not allow.
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
     * @param type  Desired a basic type
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
     * @param type  Desired a basic type
     * @param query Web query
     * @param <T>   Basic type
     * @return The raw value converted as a list
     */
    static <T> Map<WebOperator, List<T>> asValues(final Class<T> type, final FilterQuery query) {

        var map = new HashMap<WebOperator, List<T>>();
        for (var m : query.getValues().entrySet()) {
            var k = m.getKey();
            var v = m.getValue()
                    .stream()
                    .map(s -> (T) CONVERTERS.get(type).apply(s))
                    .toList();

            map.put(k, v);
        }

        return map;
    }

    /**
     * Check if web queries have a distinct query to {@code true}.
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
     * Check if web queries have a page number query to {@code true}.
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
     * Check if web queries have a page size query to {@code true}.
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
     * @param count   Number of elements based of web queries
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
     * Convert raw parameter to a web query.
     *
     * @param parameterName Attribut name with or without operator symbol
     * @param values        Web parameters values
     * @return A web query
     */
    public static FilterQuery convertQuery(final String parameterName, final List<String> values) {

        var bracketOpen = parameterName.indexOf("[");
        var bracketClose = parameterName.indexOf("]");

        String name;
        Map<WebOperator, List<String>> compositeValues;
        Operator operator;

        if (bracketOpen == -1 || bracketClose == -1 || bracketOpen > bracketClose) {
            name = parameterName;
            operator = Operator.NONE;
            compositeValues = Map.of(WebOperator.AND, values);
        } else {
            name = parameterName.substring(0, bracketOpen);
            var rawOperator = parameterName.substring(bracketOpen + 1, bracketClose);
            compositeValues = convertValues(values);
            operator = Operator.parse(rawOperator);
        }
        return new FilterQuery(name, compositeValues, operator);
    }

    /**
     * Convert web parameters values to composite web values.
     *
     * @param values Web parameters values
     * @return The values associated with the web operator
     */
    private static Map<WebOperator, List<String>> convertValues(final List<String> values) {
        var map = new HashMap<WebOperator, List<String>>();

        for (var v : values) {
            var or = v.split("\\|");
            if (or.length > 1) {
                for (var o : or) {
                    var orValues = map.getOrDefault(WebOperator.OR, new ArrayList<>());
                    orValues.add(o);
                    map.put(WebOperator.OR, orValues);
                }
            } else {
                var andValues = map.getOrDefault(WebOperator.AND, new ArrayList<>());
                andValues.add(v);
                map.put(WebOperator.AND, andValues);
            }
        }
        return map;
    }


}
