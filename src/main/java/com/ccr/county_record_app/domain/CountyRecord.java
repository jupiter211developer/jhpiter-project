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
 * A CountyRecord.
 */
@Entity
@Table(name = "ccr_county_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CountyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 30)
    @Column(name = "cat", length = 30)
    private String cat;

    @Size(max = 50)
    @Column(name = "doc_num", length = 50)
    private String docNum;

    @Size(max = 50)
    @Column(name = "doc_type", length = 50)
    private String docType;

    @Size(max = 10)
    @Column(name = "book", length = 10)
    private String book;

    @Size(max = 10)
    @Column(name = "set_abbr", length = 10)
    private String setAbbr;

    @Size(max = 10)
    @Column(name = "vol", length = 10)
    private String vol;

    @Size(max = 10)
    @Column(name = "pg", length = 10)
    private String pg;

    @Column(name = "filed_date")
    private Instant filedDate;

    @Column(name = "eff_date")
    private Instant effDate;

    @NotNull
    @Column(name = "record_key", nullable = false, unique = true)
    private String recordKey;

    @Column(name = "fips")
    private String fips;

    @Column(name = "pdf_path")
    private String pdfPath;

    @ManyToOne
    @JsonIgnoreProperties(value = { "state", "countiesAvaiable", "countyRecords" }, allowSetters = true)
    private County county;

    @JsonIgnoreProperties(value = { "countyRecord", "countyImagePages" }, allowSetters = true)
    @OneToOne(mappedBy = "countyRecord")
    private CountyImage countyImage;

    @OneToMany(mappedBy = "countyRecord")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "countyRecord", "countyImage" }, allowSetters = true)
    private Set<CountyImagePage> countyImagePages = new HashSet<>();

    @OneToMany(mappedBy = "countyRecord")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "countyRecord" }, allowSetters = true)
    private Set<CountyRecordParty> countyRecordParties = new HashSet<>();

    @OneToMany(mappedBy = "countyRecord")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "countyRecord" }, allowSetters = true)
    private Set<CountyRecordLegal> countyRecordLegals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CountyRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCat() {
        return this.cat;
    }

    public CountyRecord cat(String cat) {
        this.setCat(cat);
        return this;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getDocNum() {
        return this.docNum;
    }

    public CountyRecord docNum(String docNum) {
        this.setDocNum(docNum);
        return this;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getDocType() {
        return this.docType;
    }

    public CountyRecord docType(String docType) {
        this.setDocType(docType);
        return this;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getBook() {
        return this.book;
    }

    public CountyRecord book(String book) {
        this.setBook(book);
        return this;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getSetAbbr() {
        return this.setAbbr;
    }

    public CountyRecord setAbbr(String setAbbr) {
        this.setSetAbbr(setAbbr);
        return this;
    }

    public void setSetAbbr(String setAbbr) {
        this.setAbbr = setAbbr;
    }

    public String getVol() {
        return this.vol;
    }

    public CountyRecord vol(String vol) {
        this.setVol(vol);
        return this;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getPg() {
        return this.pg;
    }

    public CountyRecord pg(String pg) {
        this.setPg(pg);
        return this;
    }

    public void setPg(String pg) {
        this.pg = pg;
    }

    public Instant getFiledDate() {
        return this.filedDate;
    }

    public CountyRecord filedDate(Instant filedDate) {
        this.setFiledDate(filedDate);
        return this;
    }

    public void setFiledDate(Instant filedDate) {
        this.filedDate = filedDate;
    }

    public Instant getEffDate() {
        return this.effDate;
    }

    public CountyRecord effDate(Instant effDate) {
        this.setEffDate(effDate);
        return this;
    }

    public void setEffDate(Instant effDate) {
        this.effDate = effDate;
    }

    public String getRecordKey() {
        return this.recordKey;
    }

    public CountyRecord recordKey(String recordKey) {
        this.setRecordKey(recordKey);
        return this;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public String getFips() {
        return this.fips;
    }

    public CountyRecord fips(String fips) {
        this.setFips(fips);
        return this;
    }

    public void setFips(String fips) {
        this.fips = fips;
    }

    public String getPdfPath() {
        return this.pdfPath;
    }

    public CountyRecord pdfPath(String pdfPath) {
        this.setPdfPath(pdfPath);
        return this;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public County getCounty() {
        return this.county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public CountyRecord county(County county) {
        this.setCounty(county);
        return this;
    }

    public CountyImage getCountyImage() {
        return this.countyImage;
    }

    public void setCountyImage(CountyImage countyImage) {
        if (this.countyImage != null) {
            this.countyImage.setCountyRecord(null);
        }
        if (countyImage != null) {
            countyImage.setCountyRecord(this);
        }
        this.countyImage = countyImage;
    }

    public CountyRecord countyImage(CountyImage countyImage) {
        this.setCountyImage(countyImage);
        return this;
    }

    public Set<CountyImagePage> getCountyImagePages() {
        return this.countyImagePages;
    }

    public void setCountyImagePages(Set<CountyImagePage> countyImagePages) {
        if (this.countyImagePages != null) {
            this.countyImagePages.forEach(i -> i.setCountyRecord(null));
        }
        if (countyImagePages != null) {
            countyImagePages.forEach(i -> i.setCountyRecord(this));
        }
        this.countyImagePages = countyImagePages;
    }

    public CountyRecord countyImagePages(Set<CountyImagePage> countyImagePages) {
        this.setCountyImagePages(countyImagePages);
        return this;
    }

    public CountyRecord addCountyImagePage(CountyImagePage countyImagePage) {
        this.countyImagePages.add(countyImagePage);
        countyImagePage.setCountyRecord(this);
        return this;
    }

    public CountyRecord removeCountyImagePage(CountyImagePage countyImagePage) {
        this.countyImagePages.remove(countyImagePage);
        countyImagePage.setCountyRecord(null);
        return this;
    }

    public Set<CountyRecordParty> getCountyRecordParties() {
        return this.countyRecordParties;
    }

    public void setCountyRecordParties(Set<CountyRecordParty> countyRecordParties) {
        if (this.countyRecordParties != null) {
            this.countyRecordParties.forEach(i -> i.setCountyRecord(null));
        }
        if (countyRecordParties != null) {
            countyRecordParties.forEach(i -> i.setCountyRecord(this));
        }
        this.countyRecordParties = countyRecordParties;
    }

    public CountyRecord countyRecordParties(Set<CountyRecordParty> countyRecordParties) {
        this.setCountyRecordParties(countyRecordParties);
        return this;
    }

    public CountyRecord addCountyRecordParty(CountyRecordParty countyRecordParty) {
        this.countyRecordParties.add(countyRecordParty);
        countyRecordParty.setCountyRecord(this);
        return this;
    }

    public CountyRecord removeCountyRecordParty(CountyRecordParty countyRecordParty) {
        this.countyRecordParties.remove(countyRecordParty);
        countyRecordParty.setCountyRecord(null);
        return this;
    }

    public Set<CountyRecordLegal> getCountyRecordLegals() {
        return this.countyRecordLegals;
    }

    public void setCountyRecordLegals(Set<CountyRecordLegal> countyRecordLegals) {
        if (this.countyRecordLegals != null) {
            this.countyRecordLegals.forEach(i -> i.setCountyRecord(null));
        }
        if (countyRecordLegals != null) {
            countyRecordLegals.forEach(i -> i.setCountyRecord(this));
        }
        this.countyRecordLegals = countyRecordLegals;
    }

    public CountyRecord countyRecordLegals(Set<CountyRecordLegal> countyRecordLegals) {
        this.setCountyRecordLegals(countyRecordLegals);
        return this;
    }

    public CountyRecord addCountyRecordLegal(CountyRecordLegal countyRecordLegal) {
        this.countyRecordLegals.add(countyRecordLegal);
        countyRecordLegal.setCountyRecord(this);
        return this;
    }

    public CountyRecord removeCountyRecordLegal(CountyRecordLegal countyRecordLegal) {
        this.countyRecordLegals.remove(countyRecordLegal);
        countyRecordLegal.setCountyRecord(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountyRecord)) {
            return false;
        }
        return id != null && id.equals(((CountyRecord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountyRecord{" +
            "id=" + getId() +
            ", cat='" + getCat() + "'" +
            ", docNum='" + getDocNum() + "'" +
            ", docType='" + getDocType() + "'" +
            ", book='" + getBook() + "'" +
            ", setAbbr='" + getSetAbbr() + "'" +
            ", vol='" + getVol() + "'" +
            ", pg='" + getPg() + "'" +
            ", filedDate='" + getFiledDate() + "'" +
            ", effDate='" + getEffDate() + "'" +
            ", recordKey='" + getRecordKey() + "'" +
            ", fips='" + getFips() + "'" +
            ", pdfPath='" + getPdfPath() + "'" +
            "}";
    }
}
