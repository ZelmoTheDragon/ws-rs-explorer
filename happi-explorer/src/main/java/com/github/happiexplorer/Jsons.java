package com.github.happiexplorer;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.bind.JsonbBuilder;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * Utility for <i>JSON</i>.
 */
public final class Jsons {

    /**
     * Internal constructor.
     * Instance does not allow.
     */
    private Jsons() {
        throw new UnsupportedOperationException("Instance not allowed");
    }

    /**
     * Parse a <i>JSON</i> string to data transfer object.
     *
     * @param type     Data type
     * @param document <i>JSON</i> as string
     * @param <T>      Generic data type
     * @return A data transfer object
     */
    public static <T> T parse(final Class<T> type, final String document) {
        var builder = JsonbBuilder.create();
        try (builder) {
            return builder.fromJson(document, type);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Parse a <i>JSON</i> object to data transfer object.
     *
     * @param type     Data type
     * @param document <i>JSON</i> object
     * @param <T>      Generic data type
     * @return A data transfer object
     */
    public static <T> T parse(final Class<T> type, final JsonObject document) {
        try (var stringWriter = new StringWriter();
             var writer = Json.createWriter(stringWriter)) {

            writer.writeObject(document);
            var text = stringWriter.toString();
            return parse(type, text);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Parse an object to <i>JSON</i> string.
     *
     * @param entity Any data transfer object
     * @return A <i>JSON</i> string
     */
    public static String parse(final Object entity) {
        var builder = JsonbBuilder.create();
        try (builder) {
            return builder.toJson(entity);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Parse a <i>JSON</i> string to <i>JSON</i> object.
     *
     * @param document <i>JSON</i> as string
     * @return A <i>JSON</i> object
     */
    public static JsonObject parse(final String document) {
        var reader = Json.createReader(new StringReader(document));
        try (reader) {
            return reader.readObject();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Parse a <i>JSON</i> object to <i>JSON</i> string.
     *
     * @param document <i>JSON</i> object
     * @return A <i>JSON</i> string
     */
    public static String parse(final JsonObject document) {
        var output = new StringWriter();
        var writer = Json.createWriter(output);
        try (output; writer) {
            writer.writeObject(document);
            return output.toString();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
