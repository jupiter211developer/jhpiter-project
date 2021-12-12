package com.ccr.county_record_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CountyRecordLegal.
 */
@Entity
@Table(name = "ccr_county_record_legal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CountyRecordLegal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "legal", length = 255)
    private String legal;

    @NotNull
    @Column(name = "record_key", nullable = false)
    private String recordKey;

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

    public CountyRecordLegal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegal() {
        return this.legal;
    }

    public CountyRecordLegal legal(String legal) {
        this.setLegal(legal);
        return this;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getRecordKey() {
        return this.recordKey;
    }

    public CountyRecordLegal recordKey(String recordKey) {
        this.setRecordKey(recordKey);
        return this;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public CountyRecord getCountyRecord() {
        return this.countyRecord;
    }

    public void setCountyRecord(CountyRecord countyRecord) {
        this.countyRecord = countyRecord;
    }

    public CountyRecordLegal countyRecord(CountyRecord countyRecord) {
        this.setCountyRecord(countyRecord);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountyRecordLegal)) {
            return false;
        }
        return id != null && id.equals(((CountyRecordLegal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyRecordLegal{" +
            "id=" + getId() +
            ", legal='" + getLegal() + "'" +
            ", recordKey='" + getRecordKey() + "'" +
            "}";
    }
}
