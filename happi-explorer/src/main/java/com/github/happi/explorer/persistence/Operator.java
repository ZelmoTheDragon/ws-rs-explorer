package com.github.happi.explorer.persistence;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Logical web operator.
 */
enum Operator {

    /**
     * Equal.
     * Exact match.
     */
    EQUAL("eq"),

    /**
     * Not equal.
     * Same as {@link #EQUAL} but negate.
     */
    NOT_EQUAL("ne"),

    /**
     * Like.
     * Case-insensitive and ignore accent.
     * Contains the specified value.
     */
    LIKE("li"),

    /**
     * Not like.
     * Same as {@link #LIKE} but negate
     */
    NOT_LIKE("nl"),

    /**
     * Greater than.
     * Work with text, number and date.
     */
    GREATER_THAN("gt"),

    /**
     * Greater than or equal.
     * Work with text, number and date.
     */
    GREATER_THAN_OR_EQUAL("ge"),

    /**
     * Less than.
     * Work with text, number and date.
     */
    LESS_THAN("lt"),

    /**
     * Less than or equal.
     * Work with text, number and date.
     */
    LESS_THAN_OR_EQUAL("le"),

    /**
     * In.
     * Exact match.
     */
    IN("in"),

    /**
     * Not in.
     * Same as {@link #IN} but negate.
     */
    NOT_IN("ni"),

    /**
     * Between.
     * Exact match.
     * Work with text, number and date.
     */
    BETWEEN("bt"),

    /**
     * Not between.
     * Same as {@link #BETWEEN} but negate.
     */
    NOT_BETWEEN("nbt"),

    /**
     * None.
     * No operator, only for specific web query.
     */
    NONE("");

    /**
     * Web symbol.
     */
    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Convert a web symbol to an element in this enumeration.
     *
     * @param symbol Web symbol
     * @return A operator
     */
    static Operator parse(final String symbol) {
        return Stream
                .of(values())
                .filter(e -> Objects.equals(e.symbol, symbol))
                .findFirst()
                .orElse(Operator.NONE);
    }

    /**
     * Check if the symbol does not match anything.
     *
     * @param symbol Web symbol
     * @return The value {@code true} if the symbol does not match anything otherwise the value {@code false} is returned
     */
    static boolean isNone(final String symbol) {
        return Objects.isNull(symbol) || Objects.equals(NONE.symbol, symbol);
    }
}
