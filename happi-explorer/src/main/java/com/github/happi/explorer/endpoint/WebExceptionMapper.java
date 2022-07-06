package com.github.happi.explorer.endpoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.json.Json;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Generic exception mapper for <i>JAX-RS</i> exception.
 */
@Provider
public final class WebExceptionMapper implements ExceptionMapper<WebApplicationException> {

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public WebExceptionMapper() {
        // NO-OP
    }

    @Override
    public Response toResponse(final WebApplicationException exception) {

        var json = Json.createObjectBuilder()
                .add("error", exception.getClass().getName())
                .add("message", exception.getMessage())
                .add("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();

        return Response
                .status(exception.getResponse().getStatusInfo())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(json)
                .build();
    }
}
