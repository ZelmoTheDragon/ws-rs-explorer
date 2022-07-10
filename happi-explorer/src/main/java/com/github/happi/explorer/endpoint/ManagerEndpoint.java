package com.github.happi.explorer.endpoint;

import java.util.Objects;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.github.happi.explorer.ExplorerManager;
import com.github.happi.security.HappiSecurityManager;

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

        Response response;
        var permission = this.securityManager
                .getConfiguration(HappiSecurityManager.Configuration.MANAGER_ENDPOINT);

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
                .getConfiguration(HappiSecurityManager.Configuration.MANAGER_ENDPOINT);

        if (Objects.equals(Boolean.parseBoolean(permission), Boolean.TRUE)) {
            var roles = this.securityManager.roles();
            response = Response.ok(roles).build();
        } else {
            response = Response.status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

}
