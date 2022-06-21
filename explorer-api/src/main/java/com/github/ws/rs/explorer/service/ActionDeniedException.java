package com.github.ws.rs.explorer.service;

import java.io.Serial;

import com.github.ws.rs.explorer.Action;
import com.github.ws.rs.explorer.ExplorerException;

public final class ActionDeniedException extends ExplorerException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Action action;

    public ActionDeniedException(final Action action) {
        super(String.format("Action type [%s] denied !", action));
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
