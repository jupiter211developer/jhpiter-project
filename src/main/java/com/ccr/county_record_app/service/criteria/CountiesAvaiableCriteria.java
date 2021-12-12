package com.ccr.county_record_app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ccr.county_record_app.domain.CountiesAvaiable} entity. This class is used
 * in {@link com.ccr.county_record_app.web.rest.CountiesAvaiableResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /counties-avaiables?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountiesAvaiableCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter earliest;

    private InstantFilter latest;

    private IntegerFilter recordCount;

    private StringFilter fips;

    private StringFilter countyName;

    private StringFilter stateAbbr;

    private LongFilter countyId;

    private Boolean distinct;

    public CountiesAvaiableCriteria() {}

    public CountiesAvaiableCriteria(CountiesAvaiableCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.earliest = other.earliest == null ? null : other.earliest.copy();
        this.latest = other.latest == null ? null : other.latest.copy();
        this.recordCount = other.recordCount == null ? null : other.recordCount.copy();
        this.fips = other.fips == null ? null : other.fips.copy();
        this.countyName = other.countyName == null ? null : other.countyName.copy();
        this.stateAbbr = other.stateAbbr == null ? null : other.stateAbbr.copy();
        this.countyId = other.countyId == null ? null : other.countyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountiesAvaiableCriteria copy() {
        return new CountiesAvaiableCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getEarliest() {
        return earliest;
    }

    public InstantFilter earliest() {
        if (earliest == null) {
            earliest = new InstantFilter();
        }
        return earliest;
    }

    public void setEarliest(InstantFilter earliest) {
        this.earliest = earliest;
    }

    public InstantFilter getLatest() {
        return latest;
    }

    public InstantFilter latest() {
        if (latest == null) {
            latest = new InstantFilter();
        }
        return latest;
    }

    public void setLatest(InstantFilter latest) {
        this.latest = latest;
    }

    public IntegerFilter getRecordCount() {
        return recordCount;
    }

    public IntegerFilter recordCount() {
        if (recordCount == null) {
            recordCount = new IntegerFilter();
        }
        return recordCount;
    }

    public void setRecordCount(IntegerFilter recordCount) {
        this.recordCount = recordCount;
    }

    public StringFilter getFips() {
        return fips;
    }

    public StringFilter fips() {
        if (fips == null) {
            fips = new StringFilter();
        }
        return fips;
    }

    public void setFips(StringFilter fips) {
        this.fips = fips;
    }

    public StringFilter getCountyName() {
        return countyName;
    }

    public StringFilter countyName() {
        if (countyName == null) {
            countyName = new StringFilter();
        }
        return countyName;
    }

    public void setCountyName(StringFilter countyName) {
        this.countyName = countyName;
    }

    public StringFilter getStateAbbr() {
        return stateAbbr;
    }

    public StringFilter stateAbbr() {
        if (stateAbbr == null) {
            stateAbbr = new StringFilter();
        }
        return stateAbbr;
    }

    public void setStateAbbr(StringFilter stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    public LongFilter getCountyId() {
        return countyId;
    }

    public LongFilter countyId() {
        if (countyId == null) {
            countyId = new LongFilter();
        }
        return countyId;
    }

    public void setCountyId(LongFilter countyId) {
        this.countyId = countyId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CountiesAvaiableCriteria that = (CountiesAvaiableCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(earliest, that.earliest) &&
            Objects.equals(latest, that.latest) &&
            Objects.equals(recordCount, that.recordCount) &&
            Objects.equals(fips, that.fips) &&
            Objects.equals(countyName, that.countyName) &&
            Objects.equals(stateAbbr, that.stateAbbr) &&
            Objects.equals(countyId, that.countyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, earliest, latest, recordCount, fips, countyName, stateAbbr, countyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountiesAvaiableCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (earliest != null ? "earliest=" + earliest + ", " : "") +
            (latest != null ? "latest=" + latest + ", " : "") +
            (recordCount != null ? "recordCount=" + recordCount + ", " : "") +
            (fips != null ? "fips=" + fips + ", " : "") +
            (countyName != null ? "countyName=" + countyName + ", " : "") +
            (stateAbbr != null ? "stateAbbr=" + stateAbbr + ", " : "") +
            (countyId != null ? "countyId=" + countyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
