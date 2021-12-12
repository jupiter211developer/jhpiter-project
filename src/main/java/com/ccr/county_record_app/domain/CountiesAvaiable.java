package com.ccr.county_record_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CountiesAvaiable.
 */
@Entity
@Table(name = "ccr_countys_avaiable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CountiesAvaiable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "earliest")
    private Instant earliest;

    @Column(name = "latest")
    private Instant latest;

    @Column(name = "record_count")
    private Integer recordCount;

    @Size(max = 5)
    @Column(name = "fips", length = 5)
    private String fips;

    @Size(max = 50)
    @Column(name = "county_name", length = 50)
    private String countyName;

    @Size(max = 2)
    @Column(name = "state_abbr", length = 2)
    private String stateAbbr;

    @JsonIgnoreProperties(value = { "state", "countiesAvaiable", "countyRecords" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private County county;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CountiesAvaiable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEarliest() {
        return this.earliest;
    }

    public CountiesAvaiable earliest(Instant earliest) {
        this.setEarliest(earliest);
        return this;
    }

    public void setEarliest(Instant earliest) {
        this.earliest = earliest;
    }

    public Instant getLatest() {
        return this.latest;
    }

    public CountiesAvaiable latest(Instant latest) {
        this.setLatest(latest);
        return this;
    }

    public void setLatest(Instant latest) {
        this.latest = latest;
    }

    public Integer getRecordCount() {
        return this.recordCount;
    }

    public CountiesAvaiable recordCount(Integer recordCount) {
        this.setRecordCount(recordCount);
        return this;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public String getFips() {
        return this.fips;
    }

    public CountiesAvaiable fips(String fips) {
        this.setFips(fips);
        return this;
    }

    public void setFips(String fips) {
        this.fips = fips;
    }

    public String getCountyName() {
        return this.countyName;
    }

    public CountiesAvaiable countyName(String countyName) {
        this.setCountyName(countyName);
        return this;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getStateAbbr() {
        return this.stateAbbr;
    }

    public CountiesAvaiable stateAbbr(String stateAbbr) {
        this.setStateAbbr(stateAbbr);
        return this;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    public County getCounty() {
        return this.county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public CountiesAvaiable county(County county) {
        this.setCounty(county);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountiesAvaiable)) {
            return false;
        }
        return id != null && id.equals(((CountiesAvaiable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountiesAvaiable{" +
            "id=" + getId() +
            ", earliest='" + getEarliest() + "'" +
            ", latest='" + getLatest() + "'" +
            ", recordCount=" + getRecordCount() +
            ", fips='" + getFips() + "'" +
            ", countyName='" + getCountyName() + "'" +
            ", stateAbbr='" + getStateAbbr() + "'" +
            "}";
    }
}
