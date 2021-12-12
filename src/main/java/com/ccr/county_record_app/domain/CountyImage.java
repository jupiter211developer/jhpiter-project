package com.ccr.county_record_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CountyImage.
 */
@Entity
@Table(name = "ccr_county_image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CountyImage implements Serializable {

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

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "page_cnt")
    private Integer pageCnt;

    @Column(name = "file_date")
    private Instant fileDate;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "md_5_hash")
    private String md5Hash;

    @JsonIgnoreProperties(
        value = { "county", "countyImage", "countyImagePages", "countyRecordParties", "countyRecordLegals" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private CountyRecord countyRecord;

    @OneToMany(mappedBy = "countyImage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "countyRecord", "countyImage" }, allowSetters = true)
    private Set<CountyImagePage> countyImagePages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CountyImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordKey() {
        return this.recordKey;
    }

    public CountyImage recordKey(String recordKey) {
        this.setRecordKey(recordKey);
        return this;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public CountyImage fileSize(Integer fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return this.fileName;
    }

    public CountyImage fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getPageCnt() {
        return this.pageCnt;
    }

    public CountyImage pageCnt(Integer pageCnt) {
        this.setPageCnt(pageCnt);
        return this;
    }

    public void setPageCnt(Integer pageCnt) {
        this.pageCnt = pageCnt;
    }

    public Instant getFileDate() {
        return this.fileDate;
    }

    public CountyImage fileDate(Instant fileDate) {
        this.setFileDate(fileDate);
        return this;
    }

    public void setFileDate(Instant fileDate) {
        this.fileDate = fileDate;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public CountyImage filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMd5Hash() {
        return this.md5Hash;
    }

    public CountyImage md5Hash(String md5Hash) {
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

    public CountyImage countyRecord(CountyRecord countyRecord) {
        this.setCountyRecord(countyRecord);
        return this;
    }

    public Set<CountyImagePage> getCountyImagePages() {
        return this.countyImagePages;
    }

    public void setCountyImagePages(Set<CountyImagePage> countyImagePages) {
        if (this.countyImagePages != null) {
            this.countyImagePages.forEach(i -> i.setCountyImage(null));
        }
        if (countyImagePages != null) {
            countyImagePages.forEach(i -> i.setCountyImage(this));
        }
        this.countyImagePages = countyImagePages;
    }

    public CountyImage countyImagePages(Set<CountyImagePage> countyImagePages) {
        this.setCountyImagePages(countyImagePages);
        return this;
    }

    public CountyImage addCountyImagePage(CountyImagePage countyImagePage) {
        this.countyImagePages.add(countyImagePage);
        countyImagePage.setCountyImage(this);
        return this;
    }

    public CountyImage removeCountyImagePage(CountyImagePage countyImagePage) {
        this.countyImagePages.remove(countyImagePage);
        countyImagePage.setCountyImage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountyImage)) {
            return false;
        }
        return id != null && id.equals(((CountyImage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyImage{" +
            "id=" + getId() +
            ", recordKey='" + getRecordKey() + "'" +
            ", fileSize=" + getFileSize() +
            ", fileName='" + getFileName() + "'" +
            ", pageCnt=" + getPageCnt() +
            ", fileDate='" + getFileDate() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", md5Hash='" + getMd5Hash() + "'" +
            "}";
    }
}
