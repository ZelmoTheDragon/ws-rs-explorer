package com.github.happiexplorer.example;

import java.util.Map;

import com.github.happiexplorer.example.customer.CustomerDTO;
import com.github.happiexplorer.example.customer.CustomerEntity;
import com.github.happiexplorer.example.customer.CustomerMapper;
import com.github.happiexplorer.example.gender.GenderDTO;
import com.github.happiexplorer.example.gender.GenderEntity;
import com.github.happiexplorer.example.gender.GenderMapper;
import com.github.happiexplorer.example.security.Roles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import com.github.happiexplorer.Action;
import com.github.happiexplorer.DynamicEntry;
import com.github.happiexplorer.ExplorerManager;
import com.github.happiexplorer.example.endpoint.WebConfiguration;
import com.github.happiexplorer.security.HappiSecurityManager;
import com.github.happiexplorer.service.BasicExplorerService;

@ApplicationScoped
public class StartUp {

    @Inject
    private HappiSecurityManager securityManager;

    @Inject
    private ExplorerManager explorerManager;

    public StartUp() {
    }

    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {

        this.securityManager.scanRoleClassConfiguration(WebConfiguration.class);
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.MANAGER_ENDPOINT, "true");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.DISCOVERY_ENDPOINT, "true");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.JAKARTA_SECURITY, "true");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.SECRET, "secret");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.TOKEN_CLAIM_USERNAME, "preferred_username");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.TOKEN_CLAIM_GROUPS, "groups");

        this.explorerManager.register(new DynamicEntry<>(
                "gender",
                Map.of(
                        Action.FILTER, HappiSecurityManager.PUBLIC,
                        Action.FIND, HappiSecurityManager.PUBLIC
                ),
                GenderEntity.class,
                GenderDTO.class,
                GenderMapper.class,
                BasicExplorerService.class
        ));

        this.explorerManager.register(new DynamicEntry<>(
                "customer",
                Map.of(
                        Action.FILTER, HappiSecurityManager.PERMIT_ALL,
                        Action.FIND, HappiSecurityManager.PERMIT_ALL,
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
