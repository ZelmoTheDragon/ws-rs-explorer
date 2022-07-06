package com.github.happy.explorer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

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
   public BasicExplorerService() {
        super();
    }

}
