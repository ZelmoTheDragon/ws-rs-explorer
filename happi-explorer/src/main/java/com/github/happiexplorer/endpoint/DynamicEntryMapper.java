package com.github.happiexplorer.endpoint;

import com.github.happiexplorer.Action;
import com.github.happiexplorer.DynamicEntry;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

import java.util.Collection;
import java.util.Map;

/**
 * Simple <i>JSON</i> object mapper for {@link DynamicEntry} class.
 */
final class DynamicEntryMapper {

    /**
     * Internal constructor.
     * Instance does not allow.
     */
    private DynamicEntryMapper() {
        throw new UnsupportedOperationException("Instance not allowed");
    }

    /**
     * Convert to <i>JSON</i> array.
     *
     * @param entries Collection of entry points
     * @return A <i>JSON</i> array of entry point
     */
    static JsonArray toJson(final Collection<DynamicEntry<?, ?, ?, ?>> entries) {
        var array = Json.createArrayBuilder();
        for (var e : entries) {
            var o = toJson(e);
            array.add(o);
        }
        return array.build();

    }

    /**
     * Convert to <i>JSON</i> object.
     *
     * @param entry Entry point
     * @return A <i>JSON</i> object of entry point
     */
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

    /**
     * Convert to <i>JSON</i> array.
     *
     * @param actions Action with a role
     * @return A <i>JSON</i> array of action
     */
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
