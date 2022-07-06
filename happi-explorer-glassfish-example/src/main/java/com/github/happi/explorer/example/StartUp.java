package com.github.happi.explorer.example;

import java.util.Map;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.github.happi.explorer.Action;
import com.github.happi.explorer.DynamicEntry;
import com.github.happi.explorer.ExplorerManager;
import com.github.happi.explorer.example.customer.CustomerDTO;
import com.github.happi.explorer.example.customer.CustomerEntity;
import com.github.happi.explorer.example.customer.CustomerMapper;
import com.github.happi.explorer.example.endpoint.WebConfiguration;
import com.github.happi.explorer.example.gender.GenderDTO;
import com.github.happi.explorer.example.gender.GenderEntity;
import com.github.happi.explorer.example.gender.GenderMapper;
import com.github.happi.explorer.example.security.Roles;
import com.github.happi.explorer.security.ExplorerSecurityManager;
import com.github.happi.explorer.service.BasicExplorerService;

@Singleton
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
                        Action.FIND, ExplorerSecurityManager.PUBLIC,
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
