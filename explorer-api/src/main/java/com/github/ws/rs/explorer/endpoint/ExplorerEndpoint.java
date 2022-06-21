package com.github.ws.rs.explorer.endpoint;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import com.github.ws.rs.explorer.ExplorerManager;


@RequestScoped
@Path("entity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExplorerEndpoint {

    private final ExplorerManager explorerManager;

    ExplorerEndpoint() {
        this.explorerManager = null;
    }

    @Inject
    public ExplorerEndpoint(final ExplorerManager explorerManager) {
        this.explorerManager = explorerManager;
    }

    @GET
    @Path("{entity}")
    public Response filter(
            @Context final UriInfo info,
            @PathParam("entity") final String entity) {

        var parameters = info.getQueryParameters();
        var service = this.explorerManager.invokeService(entity);
        var paginationData = service.onFilter(entity, parameters);
        return Response.ok(paginationData).build();
    }

    @GET
    @Path("{entity}/{id}")
    public Response find(
            @PathParam("entity") final String entity,
            @PathParam("id") final String id) {
        var service = this.explorerManager.invokeService(entity);
        var data = service.onFind(entity, id);

        return data
                .map(d -> Response.ok(d).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("{entity}")
    public Response create(
            @Context final UriInfo info,
            @PathParam("entity") final String entity,
            final JsonObject document) {

        var service = explorerManager.invokeService(entity);
        var id = service.onCreate(entity, document);

        var uri = info
                .getAbsolutePathBuilder()
                .path(String.valueOf(id))
                .build();

        return Response.created(uri).build();
    }

    @PUT
    @Path("{entity}/{id}")
    public Response update(
            @PathParam("entity") final String entity,
            @PathParam("id") final String id,
            final JsonObject document) {

        var service = explorerManager.invokeService(entity);
        service.onUpdate(entity, document, id);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{entity}/{id}")
    public Response delete(
            @PathParam("entity") final String entity,
            @PathParam("id") final String id) {

        var service = explorerManager.invokeService(entity);
        service.onDelete(entity, id);
        return Response.noContent().build();
    }


}
