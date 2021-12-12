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
 * Criteria class for the {@link com.ccr.county_record_app.domain.CountyImagePage} entity. This class is used
 * in {@link com.ccr.county_record_app.web.rest.CountyImagePageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /county-image-pages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountyImagePageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter recordKey;

    private IntegerFilter fileSize;

    private IntegerFilter pageNo;

    private StringFilter fileName;

    private InstantFilter fileDate;

    private StringFilter filePath;

    private DoubleFilter ocrScore;

    private StringFilter md5Hash;

    private LongFilter countyRecordId;

    private LongFilter countyImageId;

    private Boolean distinct;

    public CountyImagePageCriteria() {}

    public CountyImagePageCriteria(CountyImagePageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.recordKey = other.recordKey == null ? null : other.recordKey.copy();
        this.fileSize = other.fileSize == null ? null : other.fileSize.copy();
        this.pageNo = other.pageNo == null ? null : other.pageNo.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.fileDate = other.fileDate == null ? null : other.fileDate.copy();
        this.filePath = other.filePath == null ? null : other.filePath.copy();
        this.ocrScore = other.ocrScore == null ? null : other.ocrScore.copy();
        this.md5Hash = other.md5Hash == null ? null : other.md5Hash.copy();
        this.countyRecordId = other.countyRecordId == null ? null : other.countyRecordId.copy();
        this.countyImageId = other.countyImageId == null ? null : other.countyImageId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountyImagePageCriteria copy() {
        return new CountyImagePageCriteria(this);
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

    public IntegerFilter getFileSize() {
        return fileSize;
    }

    public IntegerFilter fileSize() {
        if (fileSize == null) {
            fileSize = new IntegerFilter();
        }
        return fileSize;
    }

    public void setFileSize(IntegerFilter fileSize) {
        this.fileSize = fileSize;
    }

    public IntegerFilter getPageNo() {
        return pageNo;
    }

    public IntegerFilter pageNo() {
        if (pageNo == null) {
            pageNo = new IntegerFilter();
        }
        return pageNo;
    }

    public void setPageNo(IntegerFilter pageNo) {
        this.pageNo = pageNo;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public StringFilter fileName() {
        if (fileName == null) {
            fileName = new StringFilter();
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public InstantFilter getFileDate() {
        return fileDate;
    }

    public InstantFilter fileDate() {
        if (fileDate == null) {
            fileDate = new InstantFilter();
        }
        return fileDate;
    }

    public void setFileDate(InstantFilter fileDate) {
        this.fileDate = fileDate;
    }

    public StringFilter getFilePath() {
        return filePath;
    }

    public StringFilter filePath() {
        if (filePath == null) {
            filePath = new StringFilter();
        }
        return filePath;
    }

    public void setFilePath(StringFilter filePath) {
        this.filePath = filePath;
    }

    public DoubleFilter getOcrScore() {
        return ocrScore;
    }

    public DoubleFilter ocrScore() {
        if (ocrScore == null) {
            ocrScore = new DoubleFilter();
        }
        return ocrScore;
    }

    public void setOcrScore(DoubleFilter ocrScore) {
        this.ocrScore = ocrScore;
    }

    public StringFilter getMd5Hash() {
        return md5Hash;
    }

    public StringFilter md5Hash() {
        if (md5Hash == null) {
            md5Hash = new StringFilter();
        }
        return md5Hash;
    }

    public void setMd5Hash(StringFilter md5Hash) {
        this.md5Hash = md5Hash;
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
        final CountyImagePageCriteria that = (CountyImagePageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(recordKey, that.recordKey) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(pageNo, that.pageNo) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(fileDate, that.fileDate) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(ocrScore, that.ocrScore) &&
            Objects.equals(md5Hash, that.md5Hash) &&
            Objects.equals(countyRecordId, that.countyRecordId) &&
            Objects.equals(countyImageId, that.countyImageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            recordKey,
            fileSize,
            pageNo,
            fileName,
            fileDate,
            filePath,
            ocrScore,
            md5Hash,
            countyRecordId,
            countyImageId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyImagePageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (recordKey != null ? "recordKey=" + recordKey + ", " : "") +
            (fileSize != null ? "fileSize=" + fileSize + ", " : "") +
            (pageNo != null ? "pageNo=" + pageNo + ", " : "") +
            (fileName != null ? "fileName=" + fileName + ", " : "") +
            (fileDate != null ? "fileDate=" + fileDate + ", " : "") +
            (filePath != null ? "filePath=" + filePath + ", " : "") +
            (ocrScore != null ? "ocrScore=" + ocrScore + ", " : "") +
            (md5Hash != null ? "md5Hash=" + md5Hash + ", " : "") +
            (countyRecordId != null ? "countyRecordId=" + countyRecordId + ", " : "") +
            (countyImageId != null ? "countyImageId=" + countyImageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
