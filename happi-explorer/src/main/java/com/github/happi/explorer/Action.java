package com.github.happi.explorer;

import java.util.Map;

import com.github.happi.explorer.security.ExplorerSecurityManager;

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
     * No role need.
     */
    public static final Map<Action, String> ALL = Map.of(
            FILTER, ExplorerSecurityManager.PERMIT_ALL,
            FIND, ExplorerSecurityManager.PERMIT_ALL,
            CREATE, ExplorerSecurityManager.PERMIT_ALL,
            UPDATE, ExplorerSecurityManager.PERMIT_ALL,
            DELETE, ExplorerSecurityManager.PERMIT_ALL
    );

    /**
     * Allows read only actions on an entry point.
     * No role need.
     */
    public static final Map<Action, String> READ_ONLY = Map.of(
            FILTER, ExplorerSecurityManager.PERMIT_ALL,
            FIND, ExplorerSecurityManager.PERMIT_ALL
    );
}
