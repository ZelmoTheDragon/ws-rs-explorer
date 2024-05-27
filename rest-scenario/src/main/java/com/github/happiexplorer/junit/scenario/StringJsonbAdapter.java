package com.github.happiexplorer.junit.scenario;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;

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
        JsonObject object;
        if (Objects.nonNull(data)) {
            var reader = new StringReader(data);
            try (var jsonReader = Json.createReader(reader)) {
                object = jsonReader.readObject();
            }
        } else {
            object = null;
        }
        return object;
    }

    @Override
    public String adaptFromJson(final JsonObject data) {
        String text;
        if (Objects.nonNull(data)) {
            var stringWriter = new StringWriter();
            try (var jsonWriter = Json.createWriter(stringWriter)) {
                jsonWriter.writeObject(data);
                text = stringWriter.toString();
            }
        } else {
            text = null;
        }
        return text;
    }
}
