package com.github.ws.rs.explorer.service;

import java.io.Serial;

import com.github.ws.rs.explorer.ExplorerException;

public class ServiceExplorerException extends ExplorerException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ServiceExplorerException(final String message) {
        super(message);
    }
}
