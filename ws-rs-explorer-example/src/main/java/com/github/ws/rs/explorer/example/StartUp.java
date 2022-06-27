package com.github.ws.rs.explorer.example;

import java.util.Map;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.github.ws.rs.explorer.Action;
import com.github.ws.rs.explorer.DynamicEntry;
import com.github.ws.rs.explorer.ExplorerManager;
import com.github.ws.rs.explorer.example.customer.CustomerDTO;
import com.github.ws.rs.explorer.example.customer.CustomerEntity;
import com.github.ws.rs.explorer.example.customer.CustomerMapper;
import com.github.ws.rs.explorer.example.endpoint.WebConfiguration;
import com.github.ws.rs.explorer.example.gender.GenderDTO;
import com.github.ws.rs.explorer.example.gender.GenderEntity;
import com.github.ws.rs.explorer.example.gender.GenderMapper;
import com.github.ws.rs.explorer.example.security.Roles;
import com.github.ws.rs.explorer.security.SecurityManager;
import com.github.ws.rs.explorer.service.BasicExplorerService;

@Singleton
public class StartUp {

    private final SecurityManager securityManager;

    private final ExplorerManager explorerManager;

    @Inject
    public StartUp(final SecurityManager securityManager, final ExplorerManager explorerManager) {
        this.securityManager = securityManager;
        this.explorerManager = explorerManager;
    }

    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {

        this.securityManager.scanRoleClassConfiguration(WebConfiguration.class);
        this.securityManager.putConfiguration(SecurityManager.Configuration.MANAGER_ENDPOINT, "true");
        this.securityManager.putConfiguration(SecurityManager.Configuration.SECRET, "secret");
        this.securityManager.putConfiguration(SecurityManager.Configuration.TOKEN_CLAIM_USERNAME, "username");
        this.securityManager.putConfiguration(SecurityManager.Configuration.TOKEN_CLAIM_GROUPS, "groups");

        this.explorerManager.register(new DynamicEntry<>(
                "gender",
                Map.of(
                        Action.FILTER, Roles.GENDER_MANAGER,
                        Action.FIND, Roles.GENDER_MANAGER),
                GenderEntity.class,
                GenderDTO.class,
                GenderMapper.class,
                BasicExplorerService.class
        ));

        this.explorerManager.register(new DynamicEntry<>(
                "customer",
                Action.ALL,
                CustomerEntity.class,
                CustomerDTO.class,
                CustomerMapper.class,
                BasicExplorerService.class
        ));
    }
}
