package com.github.happy.explorer;

import java.io.Serial;

/**
 * Root exception for this module.
 */
public class ExplorerException extends RuntimeException {

    /**
     * Serial number.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    public ExplorerException(final String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public ExplorerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public ExplorerException(final Throwable cause) {
        super(cause);
    }

    /**
     * {@inheritDoc}
     */
    public ExplorerException(
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
