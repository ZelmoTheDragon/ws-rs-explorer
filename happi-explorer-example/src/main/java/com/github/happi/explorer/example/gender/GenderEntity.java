package com.github.happi.explorer.example.gender;

import java.io.Serial;
import java.util.Objects;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.github.happi.explorer.example.persistence.AbstractEntity;

@Entity
@Table(name = "gender")
@Access(AccessType.FIELD)
public class GenderEntity extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Size(max = 10)
    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    public GenderEntity() {
        super();
    }

    // Object identity...

    @Override
    public boolean equals(final Object o) {
        final boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else if (!super.equals(o)) {
            equality = false;
        } else {
            var that = (GenderEntity) o;
            equality = Objects.equals(name, that.name)
                    && Objects.equals(code, that.code)
                    && Objects.equals(description, that.description);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, code, description);
    }

    // Getters & Setters...

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
