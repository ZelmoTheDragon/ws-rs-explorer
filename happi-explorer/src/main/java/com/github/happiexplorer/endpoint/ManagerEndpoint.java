package com.github.happiexplorer.endpoint;

import com.github.happiexplorer.ExplorerManager;
import com.github.happiexplorer.security.HappiSecurityManager;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

/**
 * Basic controller exposing registered dynamic entry.
 * This feature must be enabled in {@link HappiSecurityManager}.
 */
@RequestScoped
@Path("manager")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManagerEndpoint {

    /**
     * Security manager for this module.
     */
    @Inject
    private HappiSecurityManager securityManager;

    /**
     * Dynamic entry manager.
     */
    @Inject
    private ExplorerManager explorerManager;

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public ManagerEndpoint() {
        // NO-OP
    }

    /**
     * Show all registered entries.
     *
     * @return A <i>JSON</i> object of all registered entries.
     */
    @GET
    @Path("entry")
    public Response entries() {
        checkIfEndpointEnable();
        var entries = this.explorerManager.entries();
        var document = DynamicEntryMapper.toJson(entries);
        return Response.ok(document).build();
    }

    /**
     * Show all registered roles.
     *
     * @return A <i>JSON</i> object of all registered roles.
     */
    @GET
    @Path("role")
    public Response roles() {
        checkIfEndpointEnable();
        var roles = this.securityManager.roles();
        return Response.ok(roles).build();
    }

    /**
     * Check if this endpoint is enabled.
     *
     * @throws ForbiddenException if this endpoint is disabled
     */
    private void checkIfEndpointEnable() {
        var permission = this.securityManager
                .getConfiguration(HappiSecurityManager.Configuration.MANAGER_ENDPOINT);

        if (Objects.equals(Boolean.parseBoolean(permission), Boolean.FALSE)) {
            throw new ForbiddenException();
        }
    }

}
