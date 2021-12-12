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
 * Criteria class for the {@link com.ccr.county_record_app.domain.CountyRecordParty} entity. This class is used
 * in {@link com.ccr.county_record_app.web.rest.CountyRecordPartyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /county-record-parties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountyRecordPartyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter recordKey;

    private StringFilter partyName;

    private IntegerFilter partyRole;

    private LongFilter countyRecordId;

    private Boolean distinct;

    public CountyRecordPartyCriteria() {}

    public CountyRecordPartyCriteria(CountyRecordPartyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.recordKey = other.recordKey == null ? null : other.recordKey.copy();
        this.partyName = other.partyName == null ? null : other.partyName.copy();
        this.partyRole = other.partyRole == null ? null : other.partyRole.copy();
        this.countyRecordId = other.countyRecordId == null ? null : other.countyRecordId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountyRecordPartyCriteria copy() {
        return new CountyRecordPartyCriteria(this);
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

    public StringFilter getRecordKey() {
        return recordKey;
    }

    public StringFilter recordKey() {
        if (recordKey == null) {
            recordKey = new StringFilter();
        }
        return recordKey;
    }

    public void setRecordKey(StringFilter recordKey) {
        this.recordKey = recordKey;
    }

    public StringFilter getPartyName() {
        return partyName;
    }

    public StringFilter partyName() {
        if (partyName == null) {
            partyName = new StringFilter();
        }
        return partyName;
    }

    public void setPartyName(StringFilter partyName) {
        this.partyName = partyName;
    }

    public IntegerFilter getPartyRole() {
        return partyRole;
    }

    public IntegerFilter partyRole() {
        if (partyRole == null) {
            partyRole = new IntegerFilter();
        }
        return partyRole;
    }

    public void setPartyRole(IntegerFilter partyRole) {
        this.partyRole = partyRole;
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
        final CountyRecordPartyCriteria that = (CountyRecordPartyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(recordKey, that.recordKey) &&
            Objects.equals(partyName, that.partyName) &&
            Objects.equals(partyRole, that.partyRole) &&
            Objects.equals(countyRecordId, that.countyRecordId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recordKey, partyName, partyRole, countyRecordId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyRecordPartyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (recordKey != null ? "recordKey=" + recordKey + ", " : "") +
            (partyName != null ? "partyName=" + partyName + ", " : "") +
            (partyRole != null ? "partyRole=" + partyRole + ", " : "") +
            (countyRecordId != null ? "countyRecordId=" + countyRecordId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
