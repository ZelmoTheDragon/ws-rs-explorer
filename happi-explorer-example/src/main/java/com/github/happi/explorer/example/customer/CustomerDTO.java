package com.github.happi.explorer.example.customer;

import java.util.Objects;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.github.happi.explorer.example.gender.GenderDTO;

@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
@XmlRootElement
public class CustomerDTO {

    @JsonbProperty("id")
    @XmlElement(name = "id")
    private String id;

    @NotBlank
    @Size(max = 255)
    @JsonbProperty("givenName")
    @XmlElement(name = "givenName")
    private String givenName;

    @NotBlank
    @Size(max = 255)
    @JsonbProperty("familyName")
    @XmlElement(name = "familyName")
    private String familyName;

    @NotBlank
    @Email
    @Size(max = 255)
    @JsonbProperty("email")
    @XmlElement(name = "email")
    private String email;

    @NotBlank
    @Size(max = 255)
    @JsonbProperty("phoneNumber")
    @XmlElement(name = "phoneNumber")
    private String phoneNumber;

    @NotNull
    @JsonbProperty("gender")
    @XmlElement(name = "gender")
    private GenderDTO gender;

    public CustomerDTO() {
    }

    @Override
    public boolean equals(Object o) {
        boolean eq;
        if (this == o) {
            eq = true;
        } else if (o == null || getClass() != o.getClass()) {
            eq = false;
        } else {
            var that = (CustomerDTO) o;
            eq = Objects.equals(id, that.id)
                    && Objects.equals(givenName, that.givenName)
                    && Objects.equals(familyName, that.familyName)
                    && Objects.equals(email, that.email)
                    && Objects.equals(phoneNumber, that.phoneNumber)
                    && Objects.equals(gender, that.gender);
        }
        return eq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, givenName, familyName, email, phoneNumber, gender);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
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

    public GenderDTO getGender() {
        return gender;
    }

    public void setGender(final GenderDTO gender) {
        this.gender = gender;
    }
}
