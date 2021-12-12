package com.ccr.county_record_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CountyImagePage.
 */
@Entity
@Table(name = "ccr_county_image_page")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CountyImagePage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "record_key", nullable = false)
    private String recordKey;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "page_no")
    private Integer pageNo;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_date")
    private Instant fileDate;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "ocr_score")
    private Double ocrScore;

    @Column(name = "md_5_hash")
    private String md5Hash;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "county", "countyImage", "countyImagePages", "countyRecordParties", "countyRecordLegals" },
        allowSetters = true
    )
    private CountyRecord countyRecord;

    @ManyToOne
    @JsonIgnoreProperties(value = { "countyRecord", "countyImagePages" }, allowSetters = true)
    private CountyImage countyImage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CountyImagePage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordKey() {
        return this.recordKey;
    }

    public CountyImagePage recordKey(String recordKey) {
        this.setRecordKey(recordKey);
        return this;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public CountyImagePage fileSize(Integer fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public CountyImagePage pageNo(Integer pageNo) {
        this.setPageNo(pageNo);
        return this;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getFileName() {
        return this.fileName;
    }

    public CountyImagePage fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Instant getFileDate() {
        return this.fileDate;
    }

    public CountyImagePage fileDate(Instant fileDate) {
        this.setFileDate(fileDate);
        return this;
    }

    public void setFileDate(Instant fileDate) {
        this.fileDate = fileDate;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public CountyImagePage filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Double getOcrScore() {
        return this.ocrScore;
    }

    public CountyImagePage ocrScore(Double ocrScore) {
        this.setOcrScore(ocrScore);
        return this;
    }

    public void setOcrScore(Double ocrScore) {
        this.ocrScore = ocrScore;
    }

    public String getMd5Hash() {
        return this.md5Hash;
    }

    public CountyImagePage md5Hash(String md5Hash) {
        this.setMd5Hash(md5Hash);
        return this;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public CountyRecord getCountyRecord() {
        return this.countyRecord;
    }

    public void setCountyRecord(CountyRecord countyRecord) {
        this.countyRecord = countyRecord;
    }

    public CountyImagePage countyRecord(CountyRecord countyRecord) {
        this.setCountyRecord(countyRecord);
        return this;
    }

    public CountyImage getCountyImage() {
        return this.countyImage;
    }

    public void setCountyImage(CountyImage countyImage) {
        this.countyImage = countyImage;
    }

    public CountyImagePage countyImage(CountyImage countyImage) {
        this.setCountyImage(countyImage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountyImagePage)) {
            return false;
        }
        return id != null && id.equals(((CountyImagePage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyImagePage{" +
            "id=" + getId() +
            ", recordKey='" + getRecordKey() + "'" +
            ", fileSize=" + getFileSize() +
            ", pageNo=" + getPageNo() +
            ", fileName='" + getFileName() + "'" +
            ", fileDate='" + getFileDate() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", ocrScore=" + getOcrScore() +
            ", md5Hash='" + getMd5Hash() + "'" +
            "}";
    }
}
