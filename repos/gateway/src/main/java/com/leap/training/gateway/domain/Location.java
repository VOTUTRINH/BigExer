package com.leap.training.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Location.
 */
@Table("location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("street_address")
    private String streetAddress;

    @Column("postal_code")
    private String postalCode;

    @Column("city")
    private String city;

    @Column("state_province")
    private String stateProvince;

    @Transient
    @JsonIgnoreProperties(value = { "employees", "jobHistories", "manager", "location" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    @JsonIgnoreProperties(value = { "locations", "region" }, allowSetters = true)
    @Transient
    private Country country;

    @Column("country_id")
    private Long countryId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location id(Long id) {
        this.id = id;
        return this;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public Location streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Location postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return this.stateProvince;
    }

    public Location stateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public Location departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public Location addDepartments(Department department) {
        this.departments.add(department);
        department.setLocation(this);
        return this;
    }

    public Location removeDepartments(Department department) {
        this.departments.remove(department);
        department.setLocation(null);
        return this;
    }

    public void setDepartments(Set<Department> departments) {
        if (this.departments != null) {
            this.departments.forEach(i -> i.setLocation(null));
        }
        if (departments != null) {
            departments.forEach(i -> i.setLocation(this));
        }
        this.departments = departments;
    }

    public Country getCountry() {
        return this.country;
    }

    public Location country(Country country) {
        this.setCountry(country);
        this.countryId = country != null ? country.getId() : null;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
        this.countryId = country != null ? country.getId() : null;
    }

    public Long getCountryId() {
        return this.countryId;
    }

    public void setCountryId(Long country) {
        this.countryId = country;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            "}";
    }
}
