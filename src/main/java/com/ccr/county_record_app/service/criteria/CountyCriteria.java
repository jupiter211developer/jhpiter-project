package com.ccr.county_record_app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ccr.county_record_app.domain.County} entity. This class is used
 * in {@link com.ccr.county_record_app.web.rest.CountyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /counties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter countyName;

    private StringFilter cntyFips;

    private StringFilter stateAbbr;

    private StringFilter stFips;

    private StringFilter fips;

    private LongFilter stateId;

    private LongFilter countiesAvaiableId;

    private LongFilter countyRecordId;

    private Boolean distinct;

    public CountyCriteria() {}

    public CountyCriteria(CountyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.countyName = other.countyName == null ? null : other.countyName.copy();
        this.cntyFips = other.cntyFips == null ? null : other.cntyFips.copy();
        this.stateAbbr = other.stateAbbr == null ? null : other.stateAbbr.copy();
        this.stFips = other.stFips == null ? null : other.stFips.copy();
        this.fips = other.fips == null ? null : other.fips.copy();
        this.stateId = other.stateId == null ? null : other.stateId.copy();
        this.countiesAvaiableId = other.countiesAvaiableId == null ? null : other.countiesAvaiableId.copy();
        this.countyRecordId = other.countyRecordId == null ? null : other.countyRecordId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountyCriteria copy() {
        return new CountyCriteria(this);
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

    public StringFilter getCntyFips() {
        return cntyFips;
    }

    public StringFilter cntyFips() {
        if (cntyFips == null) {
            cntyFips = new StringFilter();
        }
        return cntyFips;
    }

    public void setCntyFips(StringFilter cntyFips) {
        this.cntyFips = cntyFips;
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

    public StringFilter getStFips() {
        return stFips;
    }

    public StringFilter stFips() {
        if (stFips == null) {
            stFips = new StringFilter();
        }
        return stFips;
    }

    public void setStFips(StringFilter stFips) {
        this.stFips = stFips;
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

    public LongFilter getStateId() {
        return stateId;
    }

    public LongFilter stateId() {
        if (stateId == null) {
            stateId = new LongFilter();
        }
        return stateId;
    }

    public void setStateId(LongFilter stateId) {
        this.stateId = stateId;
    }

    public LongFilter getCountiesAvaiableId() {
        return countiesAvaiableId;
    }

    public LongFilter countiesAvaiableId() {
        if (countiesAvaiableId == null) {
            countiesAvaiableId = new LongFilter();
        }
        return countiesAvaiableId;
    }

    public void setCountiesAvaiableId(LongFilter countiesAvaiableId) {
        this.countiesAvaiableId = countiesAvaiableId;
    }

    public LongFilter getCountyRecordId() {
        return countyRecordId;
    }

    public LongFilter countyRecordId() {
        if (countyRecordId == null) {
            countyRecordId = new LongFilter();
        }
        return countyRecordId;
    }

    public void setCountyRecordId(LongFilter countyRecordId) {
        this.countyRecordId = countyRecordId;
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
        final CountyCriteria that = (CountyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(countyName, that.countyName) &&
            Objects.equals(cntyFips, that.cntyFips) &&
            Objects.equals(stateAbbr, that.stateAbbr) &&
            Objects.equals(stFips, that.stFips) &&
            Objects.equals(fips, that.fips) &&
            Objects.equals(stateId, that.stateId) &&
            Objects.equals(countiesAvaiableId, that.countiesAvaiableId) &&
            Objects.equals(countyRecordId, that.countyRecordId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, countyName, cntyFips, stateAbbr, stFips, fips, stateId, countiesAvaiableId, countyRecordId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (countyName != null ? "countyName=" + countyName + ", " : "") +
            (cntyFips != null ? "cntyFips=" + cntyFips + ", " : "") +
            (stateAbbr != null ? "stateAbbr=" + stateAbbr + ", " : "") +
            (stFips != null ? "stFips=" + stFips + ", " : "") +
            (fips != null ? "fips=" + fips + ", " : "") +
            (stateId != null ? "stateId=" + stateId + ", " : "") +
            (countiesAvaiableId != null ? "countiesAvaiableId=" + countiesAvaiableId + ", " : "") +
            (countyRecordId != null ? "countyRecordId=" + countyRecordId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
