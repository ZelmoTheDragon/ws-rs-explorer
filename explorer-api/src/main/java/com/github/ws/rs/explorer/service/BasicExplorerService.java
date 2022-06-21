package com.github.ws.rs.explorer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import com.github.ws.rs.explorer.persistence.ExplorerDAO;
import com.github.ws.rs.explorer.ExplorerManager;

@ApplicationScoped
@Transactional
public class BasicExplorerService extends AbstractExplorerService {

    BasicExplorerService() {
        super(null, null);
    }

    @Inject
    public BasicExplorerService(final ExplorerManager explorerManager, final ExplorerDAO dao) {
        super(explorerManager, dao);
    }
}
