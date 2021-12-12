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
 * Criteria class for the {@link com.ccr.county_record_app.domain.State} entity. This class is used
 * in {@link com.ccr.county_record_app.web.rest.StateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /states?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter stateName;

    private StringFilter stateAbbr;

    private StringFilter subRegion;

    private StringFilter stFips;

    private LongFilter countyId;

    private Boolean distinct;

    public StateCriteria() {}

    public StateCriteria(StateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.stateName = other.stateName == null ? null : other.stateName.copy();
        this.stateAbbr = other.stateAbbr == null ? null : other.stateAbbr.copy();
        this.subRegion = other.subRegion == null ? null : other.subRegion.copy();
        this.stFips = other.stFips == null ? null : other.stFips.copy();
        this.countyId = other.countyId == null ? null : other.countyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StateCriteria copy() {
        return new StateCriteria(this);
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

    public StringFilter getStateName() {
        return stateName;
    }

    public StringFilter stateName() {
        if (stateName == null) {
            stateName = new StringFilter();
        }
        return stateName;
    }

    public void setStateName(StringFilter stateName) {
        this.stateName = stateName;
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

    public StringFilter getSubRegion() {
        return subRegion;
    }

    public StringFilter subRegion() {
        if (subRegion == null) {
            subRegion = new StringFilter();
        }
        return subRegion;
    }

    public void setSubRegion(StringFilter subRegion) {
        this.subRegion = subRegion;
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
        final StateCriteria that = (StateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stateName, that.stateName) &&
            Objects.equals(stateAbbr, that.stateAbbr) &&
            Objects.equals(subRegion, that.subRegion) &&
            Objects.equals(stFips, that.stFips) &&
            Objects.equals(countyId, that.countyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stateName, stateAbbr, subRegion, stFips, countyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (stateName != null ? "stateName=" + stateName + ", " : "") +
            (stateAbbr != null ? "stateAbbr=" + stateAbbr + ", " : "") +
            (subRegion != null ? "subRegion=" + subRegion + ", " : "") +
            (stFips != null ? "stFips=" + stFips + ", " : "") +
            (countyId != null ? "countyId=" + countyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
