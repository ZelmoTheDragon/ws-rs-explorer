package com.github.happiexplorer;

import com.github.happiexplorer.security.HappiSecurityManager;

import java.util.Map;

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
     * No role needs it.
     */
    public static final Map<Action, String> ALL = Map.of(
            FILTER, HappiSecurityManager.PERMIT_ALL,
            FIND, HappiSecurityManager.PERMIT_ALL,
            CREATE, HappiSecurityManager.PERMIT_ALL,
            UPDATE, HappiSecurityManager.PERMIT_ALL,
            DELETE, HappiSecurityManager.PERMIT_ALL
    );

    /**
     * Allows reading only actions on an entry point.
     * No role needs it.
     */
    public static final Map<Action, String> READ_ONLY = Map.of(
            FILTER, HappiSecurityManager.PERMIT_ALL,
            FIND, HappiSecurityManager.PERMIT_ALL
    );
}
