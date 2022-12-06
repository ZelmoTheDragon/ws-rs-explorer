package com.github.happi.explorer.junit.scenario;

import java.io.StringReader;
import java.io.StringWriter;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.bind.adapter.JsonbAdapter;

/**
 * JSON adapter for type {@link String}.
 */
public class StringJsonbAdapter implements JsonbAdapter<String, JsonObject> {

    /**
     * Default constructor.
     * Don't call this constructor explicitly.
     */
    public StringJsonbAdapter() {
        // NO-OP
    }

    @Override
    public JsonObject adaptToJson(final String data) {
        var reader = new StringReader(data);
        try (var jsonReader = Json.createReader(reader)) {
            return jsonReader.readObject();
        }
    }

    @Override
    public String adaptFromJson(final JsonObject data) {
        var stringWriter = new StringWriter();
        try (var jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.writeObject(data);
            return stringWriter.toString();
        }
    }
}
