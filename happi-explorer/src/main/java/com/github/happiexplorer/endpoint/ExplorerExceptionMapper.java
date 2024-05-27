package com.github.happiexplorer.endpoint;

import com.github.happiexplorer.ExplorerException;
import com.github.happiexplorer.ValidationException;
import jakarta.json.Json;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generic exception mapper for this module.
 */
@Provider
public final class ExplorerExceptionMapper implements ExceptionMapper<ExplorerException> {

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public ExplorerExceptionMapper() {
        // NO-OP
    }

    @Override
    public Response toResponse(final ExplorerException exception) {

        var json = Json.createObjectBuilder()
                .add("error", exception.getClass().getName())
                .add("message", exception.getMessage())
                .add("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        if (exception instanceof ValidationException validation) {
            var constraints = validation.getViolations();
            var violations = Json.createArrayBuilder();
            for (var c : constraints) {
                var violation = Json
                        .createObjectBuilder()
                        .add("property", String.valueOf(c.getPropertyPath()))
                        .add("message", c.getMessage())
                        .build();

                violations.add(violation);
            }
            json.add("violations", violations.build());
        }

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(json.build())
                .build();
    }
}
