package com.github.happiexplorer.example.gender;

import java.util.Objects;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
@XmlRootElement
public class GenderDTO {

    @Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")
    @JsonbProperty("id")
    @XmlElement(name = "id")
    private String id;

    @NotBlank
    @Size(max = 255)
    @JsonbProperty("name")
    @XmlElement(name = "name")
    private String name;

    @NotBlank
    @Size(max = 10)
    @JsonbProperty("code")
    @XmlElement(name = "code")
    private String code;

    @NotNull
    @Size(max = 255)
    @JsonbProperty("description")
    @XmlElement(name = "description")
    private String description;

    public GenderDTO() {
    }

    // Object identity...

    @Override
    public boolean equals(Object o) {
        boolean eq;
        if (this == o) {
            eq = true;
        } else if (o == null || getClass() != o.getClass()) {
            eq = false;
        } else {
            var that = (GenderDTO) o;
            eq = Objects.equals(id, that.id)
                    && Objects.equals(name, that.name)
                    && Objects.equals(code, that.code)
                    && Objects.equals(description, that.description) ;
        }
        return eq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, description);
    }

    // Getters & Setters...

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

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
