package com.github.ws.rs.explorer;

import java.util.Map;

import com.github.ws.rs.explorer.security.SecurityManager;

/**
 * Possible action on an entry point.
 */
public enum Action {

    /**
     * Filter action.
     */
    FILTER,

    /**
     * Find action.
     */
    FIND,

    /**
     * Create action.
     */
    CREATE,

    /**
     * Update action.
     */
    UPDATE,

    /**
     * Delete action.
     */
    DELETE;

    /**
     * Allows all actions on an entry point.
     * No role need
     */
    public static final Map<Action, String> ALL = Map.of(
            FILTER, SecurityManager.PERMIT_ALL,
            FIND, SecurityManager.PERMIT_ALL,
            CREATE, SecurityManager.PERMIT_ALL,
            UPDATE, SecurityManager.PERMIT_ALL,
            DELETE, SecurityManager.PERMIT_ALL
    );

    /**
     * Allows read only actions on an entry point.
     * No role need
     */
    public static final Map<Action, String> READ_ONLY = Map.of(
            FILTER, SecurityManager.PERMIT_ALL,
            FIND, SecurityManager.PERMIT_ALL
    );
}
