package com.github.happiexplorer.example.persistence;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")
    @Id
    @Column(name = "id", nullable = false, unique = true, columnDefinition = UUIDConverter.COLUMN_DEFINITION)
    protected String id;

    @NotNull
    @Version
    @Column(name = "version", nullable = false, unique = true)
    protected Long version;

    protected AbstractEntity() {
        this.id = UUID.randomUUID().toString();
        this.version = 0L;
    }

    // Object identity...

    @Override
    public boolean equals(Object o) {
        final boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            var that = (AbstractEntity) o;
            equality = Objects.equals(id, that.id)
                    && Objects.equals(version, that.version);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    // Getters & Setters...

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }
}

