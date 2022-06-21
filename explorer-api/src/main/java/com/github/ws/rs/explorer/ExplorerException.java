package com.github.ws.rs.explorer;

import java.io.Serial;

public class ExplorerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ExplorerException(final String message) {
        super(message);
    }

    public ExplorerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExplorerException(final Throwable cause) {
        super(cause);
    }

    public ExplorerException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
