package com.github.happiexplorer.example.endpoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.json.Json;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Exception> {

    public ApplicationExceptionMapper() {
        // NO-OP
    }

    @Override
    public Response toResponse(final Exception exception) {

        var json = Json.createObjectBuilder()
                .add("error", exception.getClass().getName())
                .add("message", exception.getMessage())
                .add("date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(json)
                .build();
    }
}
