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
 * Criteria class for the {@link com.ccr.county_record_app.domain.CountyImage} entity. This class is used
 * in {@link com.ccr.county_record_app.web.rest.CountyImageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /county-images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountyImageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter recordKey;

    private IntegerFilter fileSize;

    private StringFilter fileName;

    private IntegerFilter pageCnt;

    private InstantFilter fileDate;

    private StringFilter filePath;

    private StringFilter md5Hash;

    private LongFilter countyRecordId;

    private LongFilter countyImagePageId;

    private Boolean distinct;

    public CountyImageCriteria() {}

    public CountyImageCriteria(CountyImageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.recordKey = other.recordKey == null ? null : other.recordKey.copy();
        this.fileSize = other.fileSize == null ? null : other.fileSize.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.pageCnt = other.pageCnt == null ? null : other.pageCnt.copy();
        this.fileDate = other.fileDate == null ? null : other.fileDate.copy();
        this.filePath = other.filePath == null ? null : other.filePath.copy();
        this.md5Hash = other.md5Hash == null ? null : other.md5Hash.copy();
        this.countyRecordId = other.countyRecordId == null ? null : other.countyRecordId.copy();
        this.countyImagePageId = other.countyImagePageId == null ? null : other.countyImagePageId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountyImageCriteria copy() {
        return new CountyImageCriteria(this);
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

    public IntegerFilter getPageCnt() {
        return pageCnt;
    }

    public IntegerFilter pageCnt() {
        if (pageCnt == null) {
            pageCnt = new IntegerFilter();
        }
        return pageCnt;
    }

    public void setPageCnt(IntegerFilter pageCnt) {
        this.pageCnt = pageCnt;
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
        final CountyImageCriteria that = (CountyImageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(recordKey, that.recordKey) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(pageCnt, that.pageCnt) &&
            Objects.equals(fileDate, that.fileDate) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(md5Hash, that.md5Hash) &&
            Objects.equals(countyRecordId, that.countyRecordId) &&
            Objects.equals(countyImagePageId, that.countyImagePageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            recordKey,
            fileSize,
            fileName,
            pageCnt,
            fileDate,
            filePath,
            md5Hash,
            countyRecordId,
            countyImagePageId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyImageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (recordKey != null ? "recordKey=" + recordKey + ", " : "") +
            (fileSize != null ? "fileSize=" + fileSize + ", " : "") +
            (fileName != null ? "fileName=" + fileName + ", " : "") +
            (pageCnt != null ? "pageCnt=" + pageCnt + ", " : "") +
            (fileDate != null ? "fileDate=" + fileDate + ", " : "") +
            (filePath != null ? "filePath=" + filePath + ", " : "") +
            (md5Hash != null ? "md5Hash=" + md5Hash + ", " : "") +
            (countyRecordId != null ? "countyRecordId=" + countyRecordId + ", " : "") +
            (countyImagePageId != null ? "countyImagePageId=" + countyImagePageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
