package com.github.ws.rs.explorer.endpoint;

import java.util.Collection;
import java.util.Map;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

import com.github.ws.rs.explorer.Action;
import com.github.ws.rs.explorer.DynamicEntry;

final class DynamicEntryMapper {

    private DynamicEntryMapper() {
        throw new UnsupportedOperationException("Instance not allowed");
    }

    static JsonArray toJson(final Collection<DynamicEntry<?, ?, ?, ?>> entries) {
        var array = Json.createArrayBuilder();
        for (var e : entries) {
            var o = toJson(e);
            array.add(o);
        }
        return array.build();

    }

    private static JsonObject toJson(final DynamicEntry<?, ?, ?, ?> entry) {
        var actions = toJson(entry.getActions());
        return Json
                .createObjectBuilder()
                .add("path", entry.getPath())
                .add("entity", entry.getEntityClass().getSimpleName())
                .add("data", entry.getDataClass().getSimpleName())
                .add("mapper", entry.getMapperClass().getSimpleName())
                .add("service", entry.getServiceClass().getSimpleName())
                .add("actions", actions)
                .build();
    }

    private static JsonArray toJson(final Map<Action, String> actions) {
        var array = Json.createArrayBuilder();
        for (var a : actions.entrySet()) {
            var o = Json
                    .createObjectBuilder()
                    .add("action", a.getKey().name())
                    .add("role", a.getValue())
                    .build();

            array.add(o);
        }
        return array.build();
    }
}
