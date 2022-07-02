package com.github.ws.rs.explorer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;

import com.github.ws.rs.explorer.persistence.ExplorerDAO;
import com.github.ws.rs.explorer.ExplorerManager;

/**
 * A simple implementation of the service without specific behavior.
 */
@ApplicationScoped
@Transactional
public class BasicExplorerService extends AbstractExplorerService {

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    BasicExplorerService() {
        super(null, null, null);
    }

    /**
     * Injection constructor.
     * This class is injectable, don't call this constructor explicitly.
     *
     * @param securityContext Security manager for this module
     * @param explorerManager Manager for all entry point
     * @param dao             Dynamic and generic repository
     */
    @Inject
    public BasicExplorerService(
            final SecurityContext securityContext,
            final ExplorerManager explorerManager,
            final ExplorerDAO dao) {

        super(securityContext, explorerManager, dao);
    }
}
