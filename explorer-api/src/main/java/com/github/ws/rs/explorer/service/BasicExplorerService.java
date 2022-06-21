package com.github.ws.rs.explorer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;

import com.github.ws.rs.explorer.persistence.ExplorerDAO;
import com.github.ws.rs.explorer.ExplorerManager;

@ApplicationScoped
@Transactional
public class BasicExplorerService extends AbstractExplorerService {

    BasicExplorerService() {
        super(null, null, null);
    }

    @Inject
    public BasicExplorerService(
            final SecurityContext securityContext,
            final ExplorerManager explorerManager,
            final ExplorerDAO dao) {

        super(securityContext, explorerManager, dao);
    }
}
