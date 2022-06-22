package com.github.ws.rs.explorer.endpoint;

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

@RequestScoped
@Path("manager")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManagerEndpoint {

    private final SecurityManager securityManager;

    private final ExplorerManager explorerManager;

    ManagerEndpoint() {
        this.securityManager = null;
        this.explorerManager = null;
    }

    @Inject
    public ManagerEndpoint(
            final SecurityManager securityManager,
            final ExplorerManager explorerManager) {

        this.securityManager = securityManager;
        this.explorerManager = explorerManager;
    }

    @GET
    @Path("entry")
    public Response entries() {

        Response response;
        if (this.securityManager.isManagerEndpointAllowed()) {
            var entries = this.explorerManager.entries();
            var document = DynamicEntryMapper.toJson(entries);
            response = Response.ok(document).build();
        } else {
            response = Response.status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    @GET
    @Path("role")
    public Response roles() {

        Response response;
        if (this.securityManager.isManagerEndpointAllowed()) {
            var roles = this.securityManager.roles();
            response = Response.ok(roles).build();
        } else {
            response = Response.status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

}
