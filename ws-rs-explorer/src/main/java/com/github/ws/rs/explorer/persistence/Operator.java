package com.github.ws.rs.explorer.persistence;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Logical web operator.
 */
enum Operator {

    /**
     * Equal.
     */
    EQUAL("eq"),

    /**
     * Not equal.
     */
    NOT_EQUAL("neq"),

    /**
     * Like.
     */
    LIKE("li"),

    /**
     * Not like.
     */
    NOT_LIKE("nli"),

    /**
     * Greater than.
     */
    GREATER_THAN("gt"),

    /**
     * Greater than or equal.
     */
    GREATER_THAN_OR_EQUAL("ge"),

    /**
     * Less than.
     */
    LESS_THAN("lt"),

    /**
     * Less than or equal.
     */
    LESS_THAN_OR_EQUAL("le"),

    /**
     * In.
     */
    IN("in"),

    /**
     * Not in.
     */
    NOT_IN("nin"),

    /**
     * Between.
     */
    BETWEEN("bt"),

    /**
     * Not between.
     */
    NOT_BETWEEN("nbt"),

    /**
     * None.
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
