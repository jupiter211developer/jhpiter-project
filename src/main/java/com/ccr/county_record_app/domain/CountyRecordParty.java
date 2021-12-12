package com.ccr.county_record_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CountyRecordParty.
 */
@Entity
@Table(name = "ccr_county_record_party")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CountyRecordParty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "record_key", nullable = false)
    private String recordKey;

    @Column(name = "party_name")
    private String partyName;

    @Column(name = "party_role")
    private Integer partyRole;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "county", "countyImage", "countyImagePages", "countyRecordParties", "countyRecordLegals" },
        allowSetters = true
    )
    private CountyRecord countyRecord;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CountyRecordParty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordKey() {
        return this.recordKey;
    }

    public CountyRecordParty recordKey(String recordKey) {
        this.setRecordKey(recordKey);
        return this;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public String getPartyName() {
        return this.partyName;
    }

    public CountyRecordParty partyName(String partyName) {
        this.setPartyName(partyName);
        return this;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Integer getPartyRole() {
        return this.partyRole;
    }

    public CountyRecordParty partyRole(Integer partyRole) {
        this.setPartyRole(partyRole);
        return this;
    }

    public void setPartyRole(Integer partyRole) {
        this.partyRole = partyRole;
    }

    public CountyRecord getCountyRecord() {
        return this.countyRecord;
    }

    public void setCountyRecord(CountyRecord countyRecord) {
        this.countyRecord = countyRecord;
    }

    public CountyRecordParty countyRecord(CountyRecord countyRecord) {
        this.setCountyRecord(countyRecord);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountyRecordParty)) {
            return false;
        }
        return id != null && id.equals(((CountyRecordParty) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyRecordParty{" +
            "id=" + getId() +
            ", recordKey='" + getRecordKey() + "'" +
            ", partyName='" + getPartyName() + "'" +
            ", partyRole=" + getPartyRole() +
            "}";
    }
}
