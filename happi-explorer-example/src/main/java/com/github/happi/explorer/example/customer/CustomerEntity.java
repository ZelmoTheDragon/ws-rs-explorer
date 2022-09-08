package com.github.happi.explorer.example.customer;

import java.io.Serial;
import java.util.Objects;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.github.happi.explorer.example.gender.GenderEntity;
import com.github.happi.explorer.example.persistence.AbstractEntity;

@Entity
@Table(name = "customer")
@Access(AccessType.FIELD)
public class CustomerEntity extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 255)
    @Column(name = "given_name", nullable = false)
    private String givenName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "family_name", nullable = false)
    private String familyName;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "gender_id", nullable = false)
    private GenderEntity gender;

    public CustomerEntity() {
        super();
    }

    @Override
    public boolean equals(final Object o) {
        final boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            var that = (CustomerEntity) o;
            equality = Objects.equals(id, that.id)
                    && Objects.equals(version, that.version)
                    && Objects.equals(givenName, that.givenName)
                    && Objects.equals(familyName, that.familyName)
                    && Objects.equals(email, that.email)
                    && Objects.equals(phoneNumber, that.phoneNumber)
                    && Objects.equals(gender, that.gender);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, givenName, familyName, email, phoneNumber, gender);
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(final String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(final String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public GenderEntity getGender() {
        return gender;
    }

    public void setGender(final GenderEntity gender) {
        this.gender = gender;
    }
}
