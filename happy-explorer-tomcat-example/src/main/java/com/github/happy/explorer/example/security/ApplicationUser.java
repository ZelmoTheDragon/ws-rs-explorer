package com.github.happy.explorer.example.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "application_user")
public class ApplicationUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @NotNull
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @NotBlank
    @Size(max = 255)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Size(max = 512)
    @Column(name = "password", length = 512)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "role", nullable = false)
    @CollectionTable(
            name = "application_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<String> roles;

    public ApplicationUser() {
        this.id = UUID.randomUUID();
        this.version = 0L;
        this.roles = new HashSet<>();
    }

    @Override
    public boolean equals(final Object o) {
        boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            var that = (ApplicationUser) o;
            equality = Objects.equals(id, that.id)
                    && Objects.equals(version, that.version)
                    && Objects.equals(username, that.username)
                    && Objects.equals(password, that.password)
                    && Objects.equals(roles, that.roles);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, username, password, roles);
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(final Set<String> roles) {
        this.roles = roles;
    }
}
