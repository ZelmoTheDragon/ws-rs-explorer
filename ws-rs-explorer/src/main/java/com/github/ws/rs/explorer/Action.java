package com.github.ws.rs.explorer;

import java.util.Map;

import com.github.ws.rs.explorer.security.SecurityManager;

public enum Action {

    FILTER,
    FIND,
    CREATE,
    UPDATE,
    DELETE;

    public static final Map<Action, String> ALL = Map.of(
            FILTER, SecurityManager.PERMIT_ALL,
            FIND, SecurityManager.PERMIT_ALL,
            CREATE, SecurityManager.PERMIT_ALL,
            UPDATE, SecurityManager.PERMIT_ALL,
            DELETE, SecurityManager.PERMIT_ALL
    );

    public static final Map<Action, String> READ_ONLY = Map.of(
            FILTER, SecurityManager.PERMIT_ALL,
            FIND, SecurityManager.PERMIT_ALL
    );
}
