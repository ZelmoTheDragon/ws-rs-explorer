package com.github.happi.explorer.example.persistence;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    protected String id;

    @Version
    @Column(name = "version", nullable = false, unique = true)
    protected Long version;

    protected AbstractEntity() {
        this.id = UUID.randomUUID().toString();
        this.version = 0L;
    }

    @Override
    public boolean equals(Object o) {
        boolean eq;
        if (this == o) {
            eq = true;
        } else if (o == null || getClass() != o.getClass()) {
            eq = false;
        } else {
            var that = (AbstractEntity) o;
            eq = Objects.equals(id, that.id)
                    && Objects.equals(version, that.version);
        }
        return eq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }


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

