package com.github.happy.explorer.service;

import java.io.Serial;

import com.github.happy.explorer.ExplorerException;

/**
 * Common business exception.
 */
public class ServiceExplorerException extends ExplorerException {

    /**
     * Serial number.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construct an exception.
     *
     * @param message Specific message can be visible by the client
     */
    public ServiceExplorerException(final String message) {
        super(message);
    }
}
