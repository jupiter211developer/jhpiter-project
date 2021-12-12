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
 * Criteria class for the {@link com.ccr.county_record_app.domain.CountyRecord} entity. This class is used
 * in {@link com.ccr.county_record_app.web.rest.CountyRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /county-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountyRecordCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cat;

    private StringFilter docNum;

    private StringFilter docType;

    private StringFilter book;

    private StringFilter setAbbr;

    private StringFilter vol;

    private StringFilter pg;

    private InstantFilter filedDate;

    private InstantFilter effDate;

    private StringFilter recordKey;

    private StringFilter fips;

    private StringFilter pdfPath;

    private LongFilter countyId;

    private LongFilter countyImageId;

    private LongFilter countyImagePageId;

    private LongFilter countyRecordPartyId;

    private LongFilter countyRecordLegalId;

    private Boolean distinct;

    public CountyRecordCriteria() {}

    public CountyRecordCriteria(CountyRecordCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cat = other.cat == null ? null : other.cat.copy();
        this.docNum = other.docNum == null ? null : other.docNum.copy();
        this.docType = other.docType == null ? null : other.docType.copy();
        this.book = other.book == null ? null : other.book.copy();
        this.setAbbr = other.setAbbr == null ? null : other.setAbbr.copy();
        this.vol = other.vol == null ? null : other.vol.copy();
        this.pg = other.pg == null ? null : other.pg.copy();
        this.filedDate = other.filedDate == null ? null : other.filedDate.copy();
        this.effDate = other.effDate == null ? null : other.effDate.copy();
        this.recordKey = other.recordKey == null ? null : other.recordKey.copy();
        this.fips = other.fips == null ? null : other.fips.copy();
        this.pdfPath = other.pdfPath == null ? null : other.pdfPath.copy();
        this.countyId = other.countyId == null ? null : other.countyId.copy();
        this.countyImageId = other.countyImageId == null ? null : other.countyImageId.copy();
        this.countyImagePageId = other.countyImagePageId == null ? null : other.countyImagePageId.copy();
        this.countyRecordPartyId = other.countyRecordPartyId == null ? null : other.countyRecordPartyId.copy();
        this.countyRecordLegalId = other.countyRecordLegalId == null ? null : other.countyRecordLegalId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountyRecordCriteria copy() {
        return new CountyRecordCriteria(this);
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

    public StringFilter getCat() {
        return cat;
    }

    public StringFilter cat() {
        if (cat == null) {
            cat = new StringFilter();
        }
        return cat;
    }

    public void setCat(StringFilter cat) {
        this.cat = cat;
    }

    public StringFilter getDocNum() {
        return docNum;
    }

    public StringFilter docNum() {
        if (docNum == null) {
            docNum = new StringFilter();
        }
        return docNum;
    }

    public void setDocNum(StringFilter docNum) {
        this.docNum = docNum;
    }

    public StringFilter getDocType() {
        return docType;
    }

    public StringFilter docType() {
        if (docType == null) {
            docType = new StringFilter();
        }
        return docType;
    }

    public void setDocType(StringFilter docType) {
        this.docType = docType;
    }

    public StringFilter getBook() {
        return book;
    }

    public StringFilter book() {
        if (book == null) {
            book = new StringFilter();
        }
        return book;
    }

    public void setBook(StringFilter book) {
        this.book = book;
    }

    public StringFilter getSetAbbr() {
        return setAbbr;
    }

    public StringFilter setAbbr() {
        if (setAbbr == null) {
            setAbbr = new StringFilter();
        }
        return setAbbr;
    }

    public void setSetAbbr(StringFilter setAbbr) {
        this.setAbbr = setAbbr;
    }

    public StringFilter getVol() {
        return vol;
    }

    public StringFilter vol() {
        if (vol == null) {
            vol = new StringFilter();
        }
        return vol;
    }

    public void setVol(StringFilter vol) {
        this.vol = vol;
    }

    public StringFilter getPg() {
        return pg;
    }

    public StringFilter pg() {
        if (pg == null) {
            pg = new StringFilter();
        }
        return pg;
    }

    public void setPg(StringFilter pg) {
        this.pg = pg;
    }

    public InstantFilter getFiledDate() {
        return filedDate;
    }

    public InstantFilter filedDate() {
        if (filedDate == null) {
            filedDate = new InstantFilter();
        }
        return filedDate;
    }

    public void setFiledDate(InstantFilter filedDate) {
        this.filedDate = filedDate;
    }

    public InstantFilter getEffDate() {
        return effDate;
    }

    public InstantFilter effDate() {
        if (effDate == null) {
            effDate = new InstantFilter();
        }
        return effDate;
    }

    public void setEffDate(InstantFilter effDate) {
        this.effDate = effDate;
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

    public StringFilter getPdfPath() {
        return pdfPath;
    }

    public StringFilter pdfPath() {
        if (pdfPath == null) {
            pdfPath = new StringFilter();
        }
        return pdfPath;
    }

    public void setPdfPath(StringFilter pdfPath) {
        this.pdfPath = pdfPath;
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

    public LongFilter getCountyImageId() {
        return countyImageId;
    }

    public LongFilter countyImageId() {
        if (countyImageId == null) {
            countyImageId = new LongFilter();
        }
        return countyImageId;
    }

    public void setCountyImageId(LongFilter countyImageId) {
        this.countyImageId = countyImageId;
    }

    public LongFilter getCountyImagePageId() {
        return countyImagePageId;
    }

    public LongFilter countyImagePageId() {
        if (countyImagePageId == null) {
            countyImagePageId = new LongFilter();
        }
        return countyImagePageId;
    }

    public void setCountyImagePageId(LongFilter countyImagePageId) {
        this.countyImagePageId = countyImagePageId;
    }

    public LongFilter getCountyRecordPartyId() {
        return countyRecordPartyId;
    }

    public LongFilter countyRecordPartyId() {
        if (countyRecordPartyId == null) {
            countyRecordPartyId = new LongFilter();
        }
        return countyRecordPartyId;
    }

    public void setCountyRecordPartyId(LongFilter countyRecordPartyId) {
        this.countyRecordPartyId = countyRecordPartyId;
    }

    public LongFilter getCountyRecordLegalId() {
        return countyRecordLegalId;
    }

    public LongFilter countyRecordLegalId() {
        if (countyRecordLegalId == null) {
            countyRecordLegalId = new LongFilter();
        }
        return countyRecordLegalId;
    }

    public void setCountyRecordLegalId(LongFilter countyRecordLegalId) {
        this.countyRecordLegalId = countyRecordLegalId;
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
        final CountyRecordCriteria that = (CountyRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cat, that.cat) &&
            Objects.equals(docNum, that.docNum) &&
            Objects.equals(docType, that.docType) &&
            Objects.equals(book, that.book) &&
            Objects.equals(setAbbr, that.setAbbr) &&
            Objects.equals(vol, that.vol) &&
            Objects.equals(pg, that.pg) &&
            Objects.equals(filedDate, that.filedDate) &&
            Objects.equals(effDate, that.effDate) &&
            Objects.equals(recordKey, that.recordKey) &&
            Objects.equals(fips, that.fips) &&
            Objects.equals(pdfPath, that.pdfPath) &&
            Objects.equals(countyId, that.countyId) &&
            Objects.equals(countyImageId, that.countyImageId) &&
            Objects.equals(countyImagePageId, that.countyImagePageId) &&
            Objects.equals(countyRecordPartyId, that.countyRecordPartyId) &&
            Objects.equals(countyRecordLegalId, that.countyRecordLegalId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            cat,
            docNum,
            docType,
            book,
            setAbbr,
            vol,
            pg,
            filedDate,
            effDate,
            recordKey,
            fips,
            pdfPath,
            countyId,
            countyImageId,
            countyImagePageId,
            countyRecordPartyId,
            countyRecordLegalId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyRecordCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cat != null ? "cat=" + cat + ", " : "") +
            (docNum != null ? "docNum=" + docNum + ", " : "") +
            (docType != null ? "docType=" + docType + ", " : "") +
            (book != null ? "book=" + book + ", " : "") +
            (setAbbr != null ? "setAbbr=" + setAbbr + ", " : "") +
            (vol != null ? "vol=" + vol + ", " : "") +
            (pg != null ? "pg=" + pg + ", " : "") +
            (filedDate != null ? "filedDate=" + filedDate + ", " : "") +
            (effDate != null ? "effDate=" + effDate + ", " : "") +
            (recordKey != null ? "recordKey=" + recordKey + ", " : "") +
            (fips != null ? "fips=" + fips + ", " : "") +
            (pdfPath != null ? "pdfPath=" + pdfPath + ", " : "") +
            (countyId != null ? "countyId=" + countyId + ", " : "") +
            (countyImageId != null ? "countyImageId=" + countyImageId + ", " : "") +
            (countyImagePageId != null ? "countyImagePageId=" + countyImagePageId + ", " : "") +
            (countyRecordPartyId != null ? "countyRecordPartyId=" + countyRecordPartyId + ", " : "") +
            (countyRecordLegalId != null ? "countyRecordLegalId=" + countyRecordLegalId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
