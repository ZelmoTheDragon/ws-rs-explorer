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
    }

    @Override
    public JsonObject adaptToJson(final String data) throws Exception {
        var jsonReader = Json.createReader(new StringReader(data));
        return jsonReader.readObject();
    }

    @Override
    public String adaptFromJson(final JsonObject data) throws Exception {
        var stringWriter = new StringWriter();
        var jsonWriter = Json.createWriter(stringWriter);
        jsonWriter.writeObject(data);
        return stringWriter.toString();
    }
}
