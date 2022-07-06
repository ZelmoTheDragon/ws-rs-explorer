package com.github.happy.explorer.example;

import java.util.Map;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import com.github.happy.explorer.Action;
import com.github.happy.explorer.DynamicEntry;
import com.github.happy.explorer.ExplorerManager;
import com.github.happy.explorer.example.customer.CustomerDTO;
import com.github.happy.explorer.example.customer.CustomerEntity;
import com.github.happy.explorer.example.customer.CustomerMapper;
import com.github.happy.explorer.example.endpoint.WebConfiguration;
import com.github.happy.explorer.example.gender.GenderEntity;
import com.github.happy.explorer.example.gender.GenderMapper;
import com.github.happy.explorer.example.security.Roles;
import com.github.happy.explorer.security.ExplorerSecurityManager;
import com.github.happy.explorer.example.gender.GenderDTO;
import com.github.happy.explorer.service.BasicExplorerService;

@ApplicationScoped
public class StartUp {

    @Inject
    private ExplorerSecurityManager explorerSecurityManager;

    @Inject
    private ExplorerManager explorerManager;

    public StartUp() {
    }

    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {

        this.explorerSecurityManager.scanRoleClassConfiguration(WebConfiguration.class);
        this.explorerSecurityManager.putConfiguration(ExplorerSecurityManager.Configuration.MANAGER_ENDPOINT, "true");
        this.explorerSecurityManager.putConfiguration(ExplorerSecurityManager.Configuration.SECRET, "secret");
        this.explorerSecurityManager.putConfiguration(ExplorerSecurityManager.Configuration.TOKEN_CLAIM_USERNAME, "preferred_username");
        this.explorerSecurityManager.putConfiguration(ExplorerSecurityManager.Configuration.TOKEN_CLAIM_GROUPS, "groups");

        this.explorerManager.register(new DynamicEntry<>(
                "gender",
                Map.of(
                        Action.FILTER, ExplorerSecurityManager.PUBLIC,
                        Action.FIND, ExplorerSecurityManager.PUBLIC
                ),
                GenderEntity.class,
                GenderDTO.class,
                GenderMapper.class,
                BasicExplorerService.class
        ));

        this.explorerManager.register(new DynamicEntry<>(
                "customer",
                Map.of(
                        Action.FILTER, ExplorerSecurityManager.PUBLIC,
                        Action.FIND, ExplorerSecurityManager.PERMIT_ALL,
                        Action.CREATE, Roles.CUSTOMER_MANAGER,
                        Action.UPDATE, Roles.CUSTOMER_MANAGER,
                        Action.DELETE, Roles.CUSTOMER_MANAGER
                ),
                CustomerEntity.class,
                CustomerDTO.class,
                CustomerMapper.class,
                BasicExplorerService.class
        ));
    }
}
