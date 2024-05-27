package com.github.happiexplorer.example.persistence;

import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, String> {

    public static final String COLUMN_DEFINITION = "VARCHAR(36)";

    public UUIDConverter() {
        // NO-OP
    }

    @Override
    public String convertToDatabaseColumn(final UUID attribute) {
        String dbData;
        if (Objects.nonNull(attribute)) {
            dbData = attribute.toString();
        } else {
            dbData = null;
        }
        return dbData;
    }

    @Override
    public UUID convertToEntityAttribute(final String dbData) {
        UUID attribute;
        if (Objects.nonNull(dbData)) {
            attribute = UUID.fromString(dbData);
        } else {
            attribute = null;
        }
        return attribute;
    }

}
