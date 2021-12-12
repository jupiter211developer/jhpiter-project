package com.ccr.county_record_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A State.
 */
@Entity
@Table(name = "ccr_state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class State implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "state_name", length = 50)
    private String stateName;

    @Size(max = 2)
    @Column(name = "state_abbr", length = 2)
    private String stateAbbr;

    @Size(max = 18)
    @Column(name = "sub_region", length = 18)
    private String subRegion;

    @Size(max = 2)
    @Column(name = "st_fips", length = 2)
    private String stFips;

    @OneToMany(mappedBy = "state")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "state", "countiesAvaiable", "countyRecords" }, allowSetters = true)
    private Set<County> counties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public State id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStateName() {
        return this.stateName;
    }

    public State stateName(String stateName) {
        this.setStateName(stateName);
        return this;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateAbbr() {
        return this.stateAbbr;
    }

    public State stateAbbr(String stateAbbr) {
        this.setStateAbbr(stateAbbr);
        return this;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    public String getSubRegion() {
        return this.subRegion;
    }

    public State subRegion(String subRegion) {
        this.setSubRegion(subRegion);
        return this;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    public String getStFips() {
        return this.stFips;
    }

    public State stFips(String stFips) {
        this.setStFips(stFips);
        return this;
    }

    public void setStFips(String stFips) {
        this.stFips = stFips;
    }

    public Set<County> getCounties() {
        return this.counties;
    }

    public void setCounties(Set<County> counties) {
        if (this.counties != null) {
            this.counties.forEach(i -> i.setState(null));
        }
        if (counties != null) {
            counties.forEach(i -> i.setState(this));
        }
        this.counties = counties;
    }

    public State counties(Set<County> counties) {
        this.setCounties(counties);
        return this;
    }

    public State addCounty(County county) {
        this.counties.add(county);
        county.setState(this);
        return this;
    }

    public State removeCounty(County county) {
        this.counties.remove(county);
        county.setState(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof State)) {
            return false;
        }
        return id != null && id.equals(((State) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "State{" +
            "id=" + getId() +
            ", stateName='" + getStateName() + "'" +
            ", stateAbbr='" + getStateAbbr() + "'" +
            ", subRegion='" + getSubRegion() + "'" +
            ", stFips='" + getStFips() + "'" +
            "}";
    }
}
