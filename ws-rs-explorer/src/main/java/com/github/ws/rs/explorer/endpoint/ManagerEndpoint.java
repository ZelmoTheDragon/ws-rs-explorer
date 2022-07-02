package com.github.ws.rs.explorer.endpoint;

import java.util.Objects;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.github.ws.rs.explorer.ExplorerManager;
import com.github.ws.rs.explorer.security.SecurityManager;

/**
 * Basic controller exposing registered dynamic entry.
 * This feature must be enabled in {@link SecurityManager}.
 */
@RequestScoped
@Path("manager")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManagerEndpoint {

    /**
     * Security manager for this module.
     */
    private final SecurityManager securityManager;

    /**
     * Dynamic entry manager.
     */
    private final ExplorerManager explorerManager;

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    ManagerEndpoint() {
        this.securityManager = null;
        this.explorerManager = null;
    }

    /**
     * Injection constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    @Inject
    public ManagerEndpoint(
            final SecurityManager securityManager,
            final ExplorerManager explorerManager) {

        this.securityManager = securityManager;
        this.explorerManager = explorerManager;
    }

    /**
     * Show all registered entries.
     *
     * @return A <i>JSON</i> object of all registered entries.
     */
    @GET
    @Path("entry")
    public Response entries() {

        Response response;
        var permission = this.securityManager
                .getConfiguration(SecurityManager.Configuration.MANAGER_ENDPOINT);

        if (Objects.equals(Boolean.parseBoolean(permission), Boolean.TRUE)) {
            var entries = this.explorerManager.entries();
            var document = DynamicEntryMapper.toJson(entries);
            response = Response.ok(document).build();
        } else {
            response = Response.status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    /**
     * Show all registered roles.
     *
     * @return A <i>JSON</i> object of all registered roles.
     */
    @GET
    @Path("role")
    public Response roles() {

        Response response;
        var permission = this.securityManager
                .getConfiguration(SecurityManager.Configuration.MANAGER_ENDPOINT);

        if (Objects.equals(Boolean.parseBoolean(permission), Boolean.TRUE)) {
            var roles = this.securityManager.roles();
            response = Response.ok(roles).build();
        } else {
            response = Response.status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

}
