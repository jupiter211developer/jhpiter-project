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
 * Criteria class for the {@link com.ccr.county_record_app.domain.CountyRecordLegal} entity. This class is used
 * in {@link com.ccr.county_record_app.web.rest.CountyRecordLegalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /county-record-legals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountyRecordLegalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter legal;

    private StringFilter recordKey;

    private LongFilter countyRecordId;

    private Boolean distinct;

    public CountyRecordLegalCriteria() {}

    public CountyRecordLegalCriteria(CountyRecordLegalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.legal = other.legal == null ? null : other.legal.copy();
        this.recordKey = other.recordKey == null ? null : other.recordKey.copy();
        this.countyRecordId = other.countyRecordId == null ? null : other.countyRecordId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountyRecordLegalCriteria copy() {
        return new CountyRecordLegalCriteria(this);
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

    public StringFilter getLegal() {
        return legal;
    }

    public StringFilter legal() {
        if (legal == null) {
            legal = new StringFilter();
        }
        return legal;
    }

    public void setLegal(StringFilter legal) {
        this.legal = legal;
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
        final CountyRecordLegalCriteria that = (CountyRecordLegalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(legal, that.legal) &&
            Objects.equals(recordKey, that.recordKey) &&
            Objects.equals(countyRecordId, that.countyRecordId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, legal, recordKey, countyRecordId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyRecordLegalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (legal != null ? "legal=" + legal + ", " : "") +
            (recordKey != null ? "recordKey=" + recordKey + ", " : "") +
            (countyRecordId != null ? "countyRecordId=" + countyRecordId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
