package com.github.happy.explorer;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;

/**
 * Utility for mapper class.
 */
public final class EntityMappers {

    /**
     * Supported converter.
     */
    private static final Map<Class<?>, Function<String, Object>> ID_CONVERTERS = Map.of(
            Boolean.class, Boolean::parseBoolean,
            Byte.class, Byte::parseByte,
            Short.class, Short::parseShort,
            Integer.class, Integer::parseInt,
            Long.class, Long::parseLong,
            Float.class, Float::parseFloat,
            Double.class, Double::parseDouble,
            String.class, String::valueOf,
            UUID.class, UUID::fromString
    );

    /**
     * Internal constructor.
     * Instance not allowed.
     */
    private EntityMappers() {
        throw new UnsupportedOperationException("Instance not allowed");
    }

    /**
     * Introspect the entity class for finding the identifier class.
     *
     * @param entityClass Entity class
     * @param <E>         Type of persistent entity
     * @param <K>         Real type of identifier
     * @return The identifier class
     */
    public static <E, K> Class<K> findIdentifierType(Class<E> entityClass) {
        var em = CDI.current().select(EntityManager.class).get();
        var idClass = em.getMetamodel().entity(entityClass).getIdType().getJavaType();
        return (Class<K>) idClass;
    }

    /**
     * Convert a string identifier to the correct type.
     *
     * @param idClass Real type
     * @param id      Literal unique identifier
     * @param <K>     Type of unique identifier
     * @return The unique identifier converted
     */
    public static <K> K convert(Class<K> idClass, String id) {
        return (K) ID_CONVERTERS.get(idClass).apply(id);
    }
}
