package com.github.happiexplorer.endpoint;

import com.github.happiexplorer.ExplorerManager;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.Objects;
import java.util.Optional;

/**
 * Dynamic controller for registered entry point.
 * Offers basic operations on entities like create, read, update and delete.
 * And an advance feature for filtered queries.
 */
@RequestScoped
@Path("entity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExplorerEndpoint {

    /**
     * Manager for entry point.
     */
    @Inject
    private ExplorerManager explorerManager;

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public ExplorerEndpoint() {
        // NO-OP
    }

    /**
     * Filter data with advance query parameters.
     *
     * @param info   URI information for query parameters
     * @param entity Unique path name
     * @return A pagination object with filtered data
     */
    @GET
    @Path("{entity}")
    public Response filter(
            @Context final UriInfo info,
            @PathParam("entity") final String entity) {

        var parameters = info.getQueryParameters();
        var service = this.explorerManager.invokeService(entity);
        var paginationData = service.filter(entity, parameters);
        return Response.ok(paginationData).build();
    }

    /**
     * Find by identifier.
     *
     * @param entity Unique path name
     * @param id     Unique identifier
     * @return The entity
     */
    @GET
    @Path("{entity}/{id}")
    public Response find(
            @PathParam("entity") final String entity,
            @PathParam("id") final String id) {

        var service = this.explorerManager.invokeService(entity);
        var data = service.find(entity, id);

        return data
                .map(d -> Response.ok(d).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Create a new entity.
     *
     * @param info     URI context information
     * @param entity   Unique path name
     * @param document <i>JSON</i> objet corresponding to the data transfer object
     * @return The location of the new entity created
     */
    @POST
    @Path("{entity}")
    public Response create(
            @Context final UriInfo info,
            @PathParam("entity") final String entity,
            final JsonObject document) {

        var service = explorerManager.invokeService(entity);
        var id = service.create(entity, document);

        var uri = info
                .getAbsolutePathBuilder()
                .path(String.valueOf(id))
                .build();

        return Response.created(uri).build();
    }

    /**
     * Update an entity.
     *
     * @param entity   Unique path name
     * @param id       Unique identifier
     * @param document <i>JSON</i> objet corresponding to the data transfer object
     * @return No content
     */
    @PUT
    @Path("{entity}/{id}")
    public Response update(
            @PathParam("entity") final String entity,
            @PathParam("id") final String id,
            final JsonObject document) {

        var service = explorerManager.invokeService(entity);
        service.update(entity, document, id);
        return Response.noContent().build();
    }

    /**
     * Delete an entity.
     *
     * @param entity Unique path name
     * @param id     Unique identifier
     * @return No content
     */
    @DELETE
    @Path("{entity}/{id}")
    public Response delete(
            @PathParam("entity") final String entity,
            @PathParam("id") final String id) {

        var service = explorerManager.invokeService(entity);
        service.delete(entity, id);
        return Response.noContent().build();
    }

    @OPTIONS
    @Path("{entity}")
    public Response options(@PathParam("entity") final String entity) {

        Response.ResponseBuilder result;

        var service = this.explorerManager.invokeService(entity);
        if (Objects.nonNull(service)) {
            var allows = String.join(
                    ", ",
                    OPTIONS.class.getSimpleName(),
                    GET.class.getSimpleName(),
                    POST.class.getSimpleName()
            );

            result = Response
                    .ok()
                    .header(HttpHeaders.ALLOW, allows);
        } else {
            result = Response.status(Response.Status.NOT_FOUND);
        }

        return result.build();
    }

    @OPTIONS
    @Path("{entity}/{id}")
    public Response options(
            @PathParam("entity") final String entity,
            @PathParam("id") final String id) {

        Response.ResponseBuilder result;

        var service = this.explorerManager.invokeService(entity);
        var exists = Optional
                .ofNullable(service)
                .map(s -> s.exists(entity, id))
                .orElse(Boolean.FALSE);

        if (exists) {
            var allows = String.join(
                    ", ",
                    OPTIONS.class.getSimpleName(),
                    PUT.class.getSimpleName(),
                    DELETE.class.getSimpleName()
            );

            result = Response
                    .ok()
                    .header(HttpHeaders.ALLOW, allows);
        } else {
            result = Response.status(Response.Status.NOT_FOUND);
        }

        return result.build();
    }


}
