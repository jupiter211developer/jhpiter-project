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
 * A County.
 */
@Entity
@Table(name = "ccr_county")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class County implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "county_name", length = 50)
    private String countyName;

    @Size(max = 3)
    @Column(name = "cnty_fips", length = 3)
    private String cntyFips;

    @Size(max = 2)
    @Column(name = "state_abbr", length = 2)
    private String stateAbbr;

    @Size(max = 2)
    @Column(name = "st_fips", length = 2)
    private String stFips;

    @Size(max = 5)
    @Column(name = "fips", length = 5)
    private String fips;

    @ManyToOne
    @JsonIgnoreProperties(value = { "counties" }, allowSetters = true)
    private State state;

    @JsonIgnoreProperties(value = { "county" }, allowSetters = true)
    @OneToOne(mappedBy = "county")
    private CountiesAvaiable countiesAvaiable;

    @OneToMany(mappedBy = "county")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "county", "countyImage", "countyImagePages", "countyRecordParties", "countyRecordLegals" },
        allowSetters = true
    )
    private Set<CountyRecord> countyRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public County id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountyName() {
        return this.countyName;
    }

    public County countyName(String countyName) {
        this.setCountyName(countyName);
        return this;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCntyFips() {
        return this.cntyFips;
    }

    public County cntyFips(String cntyFips) {
        this.setCntyFips(cntyFips);
        return this;
    }

    public void setCntyFips(String cntyFips) {
        this.cntyFips = cntyFips;
    }

    public String getStateAbbr() {
        return this.stateAbbr;
    }

    public County stateAbbr(String stateAbbr) {
        this.setStateAbbr(stateAbbr);
        return this;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    public String getStFips() {
        return this.stFips;
    }

    public County stFips(String stFips) {
        this.setStFips(stFips);
        return this;
    }

    public void setStFips(String stFips) {
        this.stFips = stFips;
    }

    public String getFips() {
        return this.fips;
    }

    public County fips(String fips) {
        this.setFips(fips);
        return this;
    }

    public void setFips(String fips) {
        this.fips = fips;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public County state(State state) {
        this.setState(state);
        return this;
    }

    public CountiesAvaiable getCountiesAvaiable() {
        return this.countiesAvaiable;
    }

    public void setCountiesAvaiable(CountiesAvaiable countiesAvaiable) {
        if (this.countiesAvaiable != null) {
            this.countiesAvaiable.setCounty(null);
        }
        if (countiesAvaiable != null) {
            countiesAvaiable.setCounty(this);
        }
        this.countiesAvaiable = countiesAvaiable;
    }

    public County countiesAvaiable(CountiesAvaiable countiesAvaiable) {
        this.setCountiesAvaiable(countiesAvaiable);
        return this;
    }

    public Set<CountyRecord> getCountyRecords() {
        return this.countyRecords;
    }

    public void setCountyRecords(Set<CountyRecord> countyRecords) {
        if (this.countyRecords != null) {
            this.countyRecords.forEach(i -> i.setCounty(null));
        }
        if (countyRecords != null) {
            countyRecords.forEach(i -> i.setCounty(this));
        }
        this.countyRecords = countyRecords;
    }

    public County countyRecords(Set<CountyRecord> countyRecords) {
        this.setCountyRecords(countyRecords);
        return this;
    }

    public County addCountyRecord(CountyRecord countyRecord) {
        this.countyRecords.add(countyRecord);
        countyRecord.setCounty(this);
        return this;
    }

    public County removeCountyRecord(CountyRecord countyRecord) {
        this.countyRecords.remove(countyRecord);
        countyRecord.setCounty(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof County)) {
            return false;
        }
        return id != null && id.equals(((County) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "County{" +
            "id=" + getId() +
            ", countyName='" + getCountyName() + "'" +
            ", cntyFips='" + getCntyFips() + "'" +
            ", stateAbbr='" + getStateAbbr() + "'" +
            ", stFips='" + getStFips() + "'" +
            ", fips='" + getFips() + "'" +
            "}";
    }
}
