package com.ccr.county_record_app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ccr.county_record_app.IntegrationTest;
import com.ccr.county_record_app.domain.County;
import com.ccr.county_record_app.domain.CountyImage;
import com.ccr.county_record_app.domain.CountyImagePage;
import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.domain.CountyRecordLegal;
import com.ccr.county_record_app.domain.CountyRecordParty;
import com.ccr.county_record_app.repository.CountyRecordRepository;
import com.ccr.county_record_app.service.criteria.CountyRecordCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CountyRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountyRecordResourceIT {

    private static final String DEFAULT_CAT = "AAAAAAAAAA";
    private static final String UPDATED_CAT = "BBBBBBBBBB";

    private static final String DEFAULT_DOC_NUM = "AAAAAAAAAA";
    private static final String UPDATED_DOC_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_DOC_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOC_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BOOK = "AAAAAAAAAA";
    private static final String UPDATED_BOOK = "BBBBBBBBBB";

    private static final String DEFAULT_SET_ABBR = "AAAAAAAAAA";
    private static final String UPDATED_SET_ABBR = "BBBBBBBBBB";

    private static final String DEFAULT_VOL = "AAAAAAAAAA";
    private static final String UPDATED_VOL = "BBBBBBBBBB";

    private static final String DEFAULT_PG = "AAAAAAAAAA";
    private static final String UPDATED_PG = "BBBBBBBBBB";

    private static final Instant DEFAULT_FILED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FILED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EFF_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EFF_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RECORD_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_FIPS = "AAAAAAAAAA";
    private static final String UPDATED_FIPS = "BBBBBBBBBB";

    private static final String DEFAULT_PDF_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PDF_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/county-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountyRecordRepository countyRecordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountyRecordMockMvc;

    private CountyRecord countyRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyRecord createEntity(EntityManager em) {
        CountyRecord countyRecord = new CountyRecord()
            .cat(DEFAULT_CAT)
            .docNum(DEFAULT_DOC_NUM)
            .docType(DEFAULT_DOC_TYPE)
            .book(DEFAULT_BOOK)
            .setAbbr(DEFAULT_SET_ABBR)
            .vol(DEFAULT_VOL)
            .pg(DEFAULT_PG)
            .filedDate(DEFAULT_FILED_DATE)
            .effDate(DEFAULT_EFF_DATE)
            .recordKey(DEFAULT_RECORD_KEY)
            .fips(DEFAULT_FIPS)
            .pdfPath(DEFAULT_PDF_PATH);
        return countyRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyRecord createUpdatedEntity(EntityManager em) {
        CountyRecord countyRecord = new CountyRecord()
            .cat(UPDATED_CAT)
            .docNum(UPDATED_DOC_NUM)
            .docType(UPDATED_DOC_TYPE)
            .book(UPDATED_BOOK)
            .setAbbr(UPDATED_SET_ABBR)
            .vol(UPDATED_VOL)
            .pg(UPDATED_PG)
            .filedDate(UPDATED_FILED_DATE)
            .effDate(UPDATED_EFF_DATE)
            .recordKey(UPDATED_RECORD_KEY)
            .fips(UPDATED_FIPS)
            .pdfPath(UPDATED_PDF_PATH);
        return countyRecord;
    }

    @BeforeEach
    public void initTest() {
        countyRecord = createEntity(em);
    }

    @Test
    @Transactional
    void createCountyRecord() throws Exception {
        int databaseSizeBeforeCreate = countyRecordRepository.findAll().size();
        // Create the CountyRecord
        restCountyRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecord)))
            .andExpect(status().isCreated());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeCreate + 1);
        CountyRecord testCountyRecord = countyRecordList.get(countyRecordList.size() - 1);
        assertThat(testCountyRecord.getCat()).isEqualTo(DEFAULT_CAT);
        assertThat(testCountyRecord.getDocNum()).isEqualTo(DEFAULT_DOC_NUM);
        assertThat(testCountyRecord.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testCountyRecord.getBook()).isEqualTo(DEFAULT_BOOK);
        assertThat(testCountyRecord.getSetAbbr()).isEqualTo(DEFAULT_SET_ABBR);
        assertThat(testCountyRecord.getVol()).isEqualTo(DEFAULT_VOL);
        assertThat(testCountyRecord.getPg()).isEqualTo(DEFAULT_PG);
        assertThat(testCountyRecord.getFiledDate()).isEqualTo(DEFAULT_FILED_DATE);
        assertThat(testCountyRecord.getEffDate()).isEqualTo(DEFAULT_EFF_DATE);
        assertThat(testCountyRecord.getRecordKey()).isEqualTo(DEFAULT_RECORD_KEY);
        assertThat(testCountyRecord.getFips()).isEqualTo(DEFAULT_FIPS);
        assertThat(testCountyRecord.getPdfPath()).isEqualTo(DEFAULT_PDF_PATH);
    }

    @Test
    @Transactional
    void createCountyRecordWithExistingId() throws Exception {
        // Create the CountyRecord with an existing ID
        countyRecord.setId(1L);

        int databaseSizeBeforeCreate = countyRecordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountyRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecord)))
            .andExpect(status().isBadRequest());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecordKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyRecordRepository.findAll().size();
        // set the field null
        countyRecord.setRecordKey(null);

        // Create the CountyRecord, which fails.

        restCountyRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecord)))
            .andExpect(status().isBadRequest());

        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountyRecords() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList
        restCountyRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].cat").value(hasItem(DEFAULT_CAT)))
            .andExpect(jsonPath("$.[*].docNum").value(hasItem(DEFAULT_DOC_NUM)))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE)))
            .andExpect(jsonPath("$.[*].book").value(hasItem(DEFAULT_BOOK)))
            .andExpect(jsonPath("$.[*].setAbbr").value(hasItem(DEFAULT_SET_ABBR)))
            .andExpect(jsonPath("$.[*].vol").value(hasItem(DEFAULT_VOL)))
            .andExpect(jsonPath("$.[*].pg").value(hasItem(DEFAULT_PG)))
            .andExpect(jsonPath("$.[*].filedDate").value(hasItem(DEFAULT_FILED_DATE.toString())))
            .andExpect(jsonPath("$.[*].effDate").value(hasItem(DEFAULT_EFF_DATE.toString())))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)))
            .andExpect(jsonPath("$.[*].fips").value(hasItem(DEFAULT_FIPS)))
            .andExpect(jsonPath("$.[*].pdfPath").value(hasItem(DEFAULT_PDF_PATH)));
    }

    @Test
    @Transactional
    void getCountyRecord() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get the countyRecord
        restCountyRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, countyRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countyRecord.getId().intValue()))
            .andExpect(jsonPath("$.cat").value(DEFAULT_CAT))
            .andExpect(jsonPath("$.docNum").value(DEFAULT_DOC_NUM))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE))
            .andExpect(jsonPath("$.book").value(DEFAULT_BOOK))
            .andExpect(jsonPath("$.setAbbr").value(DEFAULT_SET_ABBR))
            .andExpect(jsonPath("$.vol").value(DEFAULT_VOL))
            .andExpect(jsonPath("$.pg").value(DEFAULT_PG))
            .andExpect(jsonPath("$.filedDate").value(DEFAULT_FILED_DATE.toString()))
            .andExpect(jsonPath("$.effDate").value(DEFAULT_EFF_DATE.toString()))
            .andExpect(jsonPath("$.recordKey").value(DEFAULT_RECORD_KEY))
            .andExpect(jsonPath("$.fips").value(DEFAULT_FIPS))
            .andExpect(jsonPath("$.pdfPath").value(DEFAULT_PDF_PATH));
    }

    @Test
    @Transactional
    void getCountyRecordsByIdFiltering() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        Long id = countyRecord.getId();

        defaultCountyRecordShouldBeFound("id.equals=" + id);
        defaultCountyRecordShouldNotBeFound("id.notEquals=" + id);

        defaultCountyRecordShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountyRecordShouldNotBeFound("id.greaterThan=" + id);

        defaultCountyRecordShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountyRecordShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCatIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where cat equals to DEFAULT_CAT
        defaultCountyRecordShouldBeFound("cat.equals=" + DEFAULT_CAT);

        // Get all the countyRecordList where cat equals to UPDATED_CAT
        defaultCountyRecordShouldNotBeFound("cat.equals=" + UPDATED_CAT);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where cat not equals to DEFAULT_CAT
        defaultCountyRecordShouldNotBeFound("cat.notEquals=" + DEFAULT_CAT);

        // Get all the countyRecordList where cat not equals to UPDATED_CAT
        defaultCountyRecordShouldBeFound("cat.notEquals=" + UPDATED_CAT);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCatIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where cat in DEFAULT_CAT or UPDATED_CAT
        defaultCountyRecordShouldBeFound("cat.in=" + DEFAULT_CAT + "," + UPDATED_CAT);

        // Get all the countyRecordList where cat equals to UPDATED_CAT
        defaultCountyRecordShouldNotBeFound("cat.in=" + UPDATED_CAT);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCatIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where cat is not null
        defaultCountyRecordShouldBeFound("cat.specified=true");

        // Get all the countyRecordList where cat is null
        defaultCountyRecordShouldNotBeFound("cat.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCatContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where cat contains DEFAULT_CAT
        defaultCountyRecordShouldBeFound("cat.contains=" + DEFAULT_CAT);

        // Get all the countyRecordList where cat contains UPDATED_CAT
        defaultCountyRecordShouldNotBeFound("cat.contains=" + UPDATED_CAT);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCatNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where cat does not contain DEFAULT_CAT
        defaultCountyRecordShouldNotBeFound("cat.doesNotContain=" + DEFAULT_CAT);

        // Get all the countyRecordList where cat does not contain UPDATED_CAT
        defaultCountyRecordShouldBeFound("cat.doesNotContain=" + UPDATED_CAT);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocNumIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docNum equals to DEFAULT_DOC_NUM
        defaultCountyRecordShouldBeFound("docNum.equals=" + DEFAULT_DOC_NUM);

        // Get all the countyRecordList where docNum equals to UPDATED_DOC_NUM
        defaultCountyRecordShouldNotBeFound("docNum.equals=" + UPDATED_DOC_NUM);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docNum not equals to DEFAULT_DOC_NUM
        defaultCountyRecordShouldNotBeFound("docNum.notEquals=" + DEFAULT_DOC_NUM);

        // Get all the countyRecordList where docNum not equals to UPDATED_DOC_NUM
        defaultCountyRecordShouldBeFound("docNum.notEquals=" + UPDATED_DOC_NUM);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocNumIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docNum in DEFAULT_DOC_NUM or UPDATED_DOC_NUM
        defaultCountyRecordShouldBeFound("docNum.in=" + DEFAULT_DOC_NUM + "," + UPDATED_DOC_NUM);

        // Get all the countyRecordList where docNum equals to UPDATED_DOC_NUM
        defaultCountyRecordShouldNotBeFound("docNum.in=" + UPDATED_DOC_NUM);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docNum is not null
        defaultCountyRecordShouldBeFound("docNum.specified=true");

        // Get all the countyRecordList where docNum is null
        defaultCountyRecordShouldNotBeFound("docNum.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocNumContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docNum contains DEFAULT_DOC_NUM
        defaultCountyRecordShouldBeFound("docNum.contains=" + DEFAULT_DOC_NUM);

        // Get all the countyRecordList where docNum contains UPDATED_DOC_NUM
        defaultCountyRecordShouldNotBeFound("docNum.contains=" + UPDATED_DOC_NUM);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocNumNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docNum does not contain DEFAULT_DOC_NUM
        defaultCountyRecordShouldNotBeFound("docNum.doesNotContain=" + DEFAULT_DOC_NUM);

        // Get all the countyRecordList where docNum does not contain UPDATED_DOC_NUM
        defaultCountyRecordShouldBeFound("docNum.doesNotContain=" + UPDATED_DOC_NUM);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docType equals to DEFAULT_DOC_TYPE
        defaultCountyRecordShouldBeFound("docType.equals=" + DEFAULT_DOC_TYPE);

        // Get all the countyRecordList where docType equals to UPDATED_DOC_TYPE
        defaultCountyRecordShouldNotBeFound("docType.equals=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docType not equals to DEFAULT_DOC_TYPE
        defaultCountyRecordShouldNotBeFound("docType.notEquals=" + DEFAULT_DOC_TYPE);

        // Get all the countyRecordList where docType not equals to UPDATED_DOC_TYPE
        defaultCountyRecordShouldBeFound("docType.notEquals=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocTypeIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docType in DEFAULT_DOC_TYPE or UPDATED_DOC_TYPE
        defaultCountyRecordShouldBeFound("docType.in=" + DEFAULT_DOC_TYPE + "," + UPDATED_DOC_TYPE);

        // Get all the countyRecordList where docType equals to UPDATED_DOC_TYPE
        defaultCountyRecordShouldNotBeFound("docType.in=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docType is not null
        defaultCountyRecordShouldBeFound("docType.specified=true");

        // Get all the countyRecordList where docType is null
        defaultCountyRecordShouldNotBeFound("docType.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocTypeContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docType contains DEFAULT_DOC_TYPE
        defaultCountyRecordShouldBeFound("docType.contains=" + DEFAULT_DOC_TYPE);

        // Get all the countyRecordList where docType contains UPDATED_DOC_TYPE
        defaultCountyRecordShouldNotBeFound("docType.contains=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByDocTypeNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where docType does not contain DEFAULT_DOC_TYPE
        defaultCountyRecordShouldNotBeFound("docType.doesNotContain=" + DEFAULT_DOC_TYPE);

        // Get all the countyRecordList where docType does not contain UPDATED_DOC_TYPE
        defaultCountyRecordShouldBeFound("docType.doesNotContain=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByBookIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where book equals to DEFAULT_BOOK
        defaultCountyRecordShouldBeFound("book.equals=" + DEFAULT_BOOK);

        // Get all the countyRecordList where book equals to UPDATED_BOOK
        defaultCountyRecordShouldNotBeFound("book.equals=" + UPDATED_BOOK);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByBookIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where book not equals to DEFAULT_BOOK
        defaultCountyRecordShouldNotBeFound("book.notEquals=" + DEFAULT_BOOK);

        // Get all the countyRecordList where book not equals to UPDATED_BOOK
        defaultCountyRecordShouldBeFound("book.notEquals=" + UPDATED_BOOK);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByBookIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where book in DEFAULT_BOOK or UPDATED_BOOK
        defaultCountyRecordShouldBeFound("book.in=" + DEFAULT_BOOK + "," + UPDATED_BOOK);

        // Get all the countyRecordList where book equals to UPDATED_BOOK
        defaultCountyRecordShouldNotBeFound("book.in=" + UPDATED_BOOK);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByBookIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where book is not null
        defaultCountyRecordShouldBeFound("book.specified=true");

        // Get all the countyRecordList where book is null
        defaultCountyRecordShouldNotBeFound("book.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByBookContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where book contains DEFAULT_BOOK
        defaultCountyRecordShouldBeFound("book.contains=" + DEFAULT_BOOK);

        // Get all the countyRecordList where book contains UPDATED_BOOK
        defaultCountyRecordShouldNotBeFound("book.contains=" + UPDATED_BOOK);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByBookNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where book does not contain DEFAULT_BOOK
        defaultCountyRecordShouldNotBeFound("book.doesNotContain=" + DEFAULT_BOOK);

        // Get all the countyRecordList where book does not contain UPDATED_BOOK
        defaultCountyRecordShouldBeFound("book.doesNotContain=" + UPDATED_BOOK);
    }

    @Test
    @Transactional
    void getAllCountyRecordsBySetAbbrIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where setAbbr equals to DEFAULT_SET_ABBR
        defaultCountyRecordShouldBeFound("setAbbr.equals=" + DEFAULT_SET_ABBR);

        // Get all the countyRecordList where setAbbr equals to UPDATED_SET_ABBR
        defaultCountyRecordShouldNotBeFound("setAbbr.equals=" + UPDATED_SET_ABBR);
    }

    @Test
    @Transactional
    void getAllCountyRecordsBySetAbbrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where setAbbr not equals to DEFAULT_SET_ABBR
        defaultCountyRecordShouldNotBeFound("setAbbr.notEquals=" + DEFAULT_SET_ABBR);

        // Get all the countyRecordList where setAbbr not equals to UPDATED_SET_ABBR
        defaultCountyRecordShouldBeFound("setAbbr.notEquals=" + UPDATED_SET_ABBR);
    }

    @Test
    @Transactional
    void getAllCountyRecordsBySetAbbrIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where setAbbr in DEFAULT_SET_ABBR or UPDATED_SET_ABBR
        defaultCountyRecordShouldBeFound("setAbbr.in=" + DEFAULT_SET_ABBR + "," + UPDATED_SET_ABBR);

        // Get all the countyRecordList where setAbbr equals to UPDATED_SET_ABBR
        defaultCountyRecordShouldNotBeFound("setAbbr.in=" + UPDATED_SET_ABBR);
    }

    @Test
    @Transactional
    void getAllCountyRecordsBySetAbbrIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where setAbbr is not null
        defaultCountyRecordShouldBeFound("setAbbr.specified=true");

        // Get all the countyRecordList where setAbbr is null
        defaultCountyRecordShouldNotBeFound("setAbbr.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsBySetAbbrContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where setAbbr contains DEFAULT_SET_ABBR
        defaultCountyRecordShouldBeFound("setAbbr.contains=" + DEFAULT_SET_ABBR);

        // Get all the countyRecordList where setAbbr contains UPDATED_SET_ABBR
        defaultCountyRecordShouldNotBeFound("setAbbr.contains=" + UPDATED_SET_ABBR);
    }

    @Test
    @Transactional
    void getAllCountyRecordsBySetAbbrNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where setAbbr does not contain DEFAULT_SET_ABBR
        defaultCountyRecordShouldNotBeFound("setAbbr.doesNotContain=" + DEFAULT_SET_ABBR);

        // Get all the countyRecordList where setAbbr does not contain UPDATED_SET_ABBR
        defaultCountyRecordShouldBeFound("setAbbr.doesNotContain=" + UPDATED_SET_ABBR);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByVolIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where vol equals to DEFAULT_VOL
        defaultCountyRecordShouldBeFound("vol.equals=" + DEFAULT_VOL);

        // Get all the countyRecordList where vol equals to UPDATED_VOL
        defaultCountyRecordShouldNotBeFound("vol.equals=" + UPDATED_VOL);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByVolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where vol not equals to DEFAULT_VOL
        defaultCountyRecordShouldNotBeFound("vol.notEquals=" + DEFAULT_VOL);

        // Get all the countyRecordList where vol not equals to UPDATED_VOL
        defaultCountyRecordShouldBeFound("vol.notEquals=" + UPDATED_VOL);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByVolIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where vol in DEFAULT_VOL or UPDATED_VOL
        defaultCountyRecordShouldBeFound("vol.in=" + DEFAULT_VOL + "," + UPDATED_VOL);

        // Get all the countyRecordList where vol equals to UPDATED_VOL
        defaultCountyRecordShouldNotBeFound("vol.in=" + UPDATED_VOL);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByVolIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where vol is not null
        defaultCountyRecordShouldBeFound("vol.specified=true");

        // Get all the countyRecordList where vol is null
        defaultCountyRecordShouldNotBeFound("vol.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByVolContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where vol contains DEFAULT_VOL
        defaultCountyRecordShouldBeFound("vol.contains=" + DEFAULT_VOL);

        // Get all the countyRecordList where vol contains UPDATED_VOL
        defaultCountyRecordShouldNotBeFound("vol.contains=" + UPDATED_VOL);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByVolNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where vol does not contain DEFAULT_VOL
        defaultCountyRecordShouldNotBeFound("vol.doesNotContain=" + DEFAULT_VOL);

        // Get all the countyRecordList where vol does not contain UPDATED_VOL
        defaultCountyRecordShouldBeFound("vol.doesNotContain=" + UPDATED_VOL);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPgIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pg equals to DEFAULT_PG
        defaultCountyRecordShouldBeFound("pg.equals=" + DEFAULT_PG);

        // Get all the countyRecordList where pg equals to UPDATED_PG
        defaultCountyRecordShouldNotBeFound("pg.equals=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPgIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pg not equals to DEFAULT_PG
        defaultCountyRecordShouldNotBeFound("pg.notEquals=" + DEFAULT_PG);

        // Get all the countyRecordList where pg not equals to UPDATED_PG
        defaultCountyRecordShouldBeFound("pg.notEquals=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPgIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pg in DEFAULT_PG or UPDATED_PG
        defaultCountyRecordShouldBeFound("pg.in=" + DEFAULT_PG + "," + UPDATED_PG);

        // Get all the countyRecordList where pg equals to UPDATED_PG
        defaultCountyRecordShouldNotBeFound("pg.in=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPgIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pg is not null
        defaultCountyRecordShouldBeFound("pg.specified=true");

        // Get all the countyRecordList where pg is null
        defaultCountyRecordShouldNotBeFound("pg.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPgContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pg contains DEFAULT_PG
        defaultCountyRecordShouldBeFound("pg.contains=" + DEFAULT_PG);

        // Get all the countyRecordList where pg contains UPDATED_PG
        defaultCountyRecordShouldNotBeFound("pg.contains=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPgNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pg does not contain DEFAULT_PG
        defaultCountyRecordShouldNotBeFound("pg.doesNotContain=" + DEFAULT_PG);

        // Get all the countyRecordList where pg does not contain UPDATED_PG
        defaultCountyRecordShouldBeFound("pg.doesNotContain=" + UPDATED_PG);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFiledDateIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where filedDate equals to DEFAULT_FILED_DATE
        defaultCountyRecordShouldBeFound("filedDate.equals=" + DEFAULT_FILED_DATE);

        // Get all the countyRecordList where filedDate equals to UPDATED_FILED_DATE
        defaultCountyRecordShouldNotBeFound("filedDate.equals=" + UPDATED_FILED_DATE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFiledDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where filedDate not equals to DEFAULT_FILED_DATE
        defaultCountyRecordShouldNotBeFound("filedDate.notEquals=" + DEFAULT_FILED_DATE);

        // Get all the countyRecordList where filedDate not equals to UPDATED_FILED_DATE
        defaultCountyRecordShouldBeFound("filedDate.notEquals=" + UPDATED_FILED_DATE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFiledDateIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where filedDate in DEFAULT_FILED_DATE or UPDATED_FILED_DATE
        defaultCountyRecordShouldBeFound("filedDate.in=" + DEFAULT_FILED_DATE + "," + UPDATED_FILED_DATE);

        // Get all the countyRecordList where filedDate equals to UPDATED_FILED_DATE
        defaultCountyRecordShouldNotBeFound("filedDate.in=" + UPDATED_FILED_DATE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFiledDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where filedDate is not null
        defaultCountyRecordShouldBeFound("filedDate.specified=true");

        // Get all the countyRecordList where filedDate is null
        defaultCountyRecordShouldNotBeFound("filedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByEffDateIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where effDate equals to DEFAULT_EFF_DATE
        defaultCountyRecordShouldBeFound("effDate.equals=" + DEFAULT_EFF_DATE);

        // Get all the countyRecordList where effDate equals to UPDATED_EFF_DATE
        defaultCountyRecordShouldNotBeFound("effDate.equals=" + UPDATED_EFF_DATE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByEffDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where effDate not equals to DEFAULT_EFF_DATE
        defaultCountyRecordShouldNotBeFound("effDate.notEquals=" + DEFAULT_EFF_DATE);

        // Get all the countyRecordList where effDate not equals to UPDATED_EFF_DATE
        defaultCountyRecordShouldBeFound("effDate.notEquals=" + UPDATED_EFF_DATE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByEffDateIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where effDate in DEFAULT_EFF_DATE or UPDATED_EFF_DATE
        defaultCountyRecordShouldBeFound("effDate.in=" + DEFAULT_EFF_DATE + "," + UPDATED_EFF_DATE);

        // Get all the countyRecordList where effDate equals to UPDATED_EFF_DATE
        defaultCountyRecordShouldNotBeFound("effDate.in=" + UPDATED_EFF_DATE);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByEffDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where effDate is not null
        defaultCountyRecordShouldBeFound("effDate.specified=true");

        // Get all the countyRecordList where effDate is null
        defaultCountyRecordShouldNotBeFound("effDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByRecordKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where recordKey equals to DEFAULT_RECORD_KEY
        defaultCountyRecordShouldBeFound("recordKey.equals=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyRecordShouldNotBeFound("recordKey.equals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByRecordKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where recordKey not equals to DEFAULT_RECORD_KEY
        defaultCountyRecordShouldNotBeFound("recordKey.notEquals=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordList where recordKey not equals to UPDATED_RECORD_KEY
        defaultCountyRecordShouldBeFound("recordKey.notEquals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByRecordKeyIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where recordKey in DEFAULT_RECORD_KEY or UPDATED_RECORD_KEY
        defaultCountyRecordShouldBeFound("recordKey.in=" + DEFAULT_RECORD_KEY + "," + UPDATED_RECORD_KEY);

        // Get all the countyRecordList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyRecordShouldNotBeFound("recordKey.in=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByRecordKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where recordKey is not null
        defaultCountyRecordShouldBeFound("recordKey.specified=true");

        // Get all the countyRecordList where recordKey is null
        defaultCountyRecordShouldNotBeFound("recordKey.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByRecordKeyContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where recordKey contains DEFAULT_RECORD_KEY
        defaultCountyRecordShouldBeFound("recordKey.contains=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordList where recordKey contains UPDATED_RECORD_KEY
        defaultCountyRecordShouldNotBeFound("recordKey.contains=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByRecordKeyNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where recordKey does not contain DEFAULT_RECORD_KEY
        defaultCountyRecordShouldNotBeFound("recordKey.doesNotContain=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordList where recordKey does not contain UPDATED_RECORD_KEY
        defaultCountyRecordShouldBeFound("recordKey.doesNotContain=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFipsIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where fips equals to DEFAULT_FIPS
        defaultCountyRecordShouldBeFound("fips.equals=" + DEFAULT_FIPS);

        // Get all the countyRecordList where fips equals to UPDATED_FIPS
        defaultCountyRecordShouldNotBeFound("fips.equals=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFipsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where fips not equals to DEFAULT_FIPS
        defaultCountyRecordShouldNotBeFound("fips.notEquals=" + DEFAULT_FIPS);

        // Get all the countyRecordList where fips not equals to UPDATED_FIPS
        defaultCountyRecordShouldBeFound("fips.notEquals=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFipsIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where fips in DEFAULT_FIPS or UPDATED_FIPS
        defaultCountyRecordShouldBeFound("fips.in=" + DEFAULT_FIPS + "," + UPDATED_FIPS);

        // Get all the countyRecordList where fips equals to UPDATED_FIPS
        defaultCountyRecordShouldNotBeFound("fips.in=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFipsIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where fips is not null
        defaultCountyRecordShouldBeFound("fips.specified=true");

        // Get all the countyRecordList where fips is null
        defaultCountyRecordShouldNotBeFound("fips.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFipsContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where fips contains DEFAULT_FIPS
        defaultCountyRecordShouldBeFound("fips.contains=" + DEFAULT_FIPS);

        // Get all the countyRecordList where fips contains UPDATED_FIPS
        defaultCountyRecordShouldNotBeFound("fips.contains=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByFipsNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where fips does not contain DEFAULT_FIPS
        defaultCountyRecordShouldNotBeFound("fips.doesNotContain=" + DEFAULT_FIPS);

        // Get all the countyRecordList where fips does not contain UPDATED_FIPS
        defaultCountyRecordShouldBeFound("fips.doesNotContain=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPdfPathIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pdfPath equals to DEFAULT_PDF_PATH
        defaultCountyRecordShouldBeFound("pdfPath.equals=" + DEFAULT_PDF_PATH);

        // Get all the countyRecordList where pdfPath equals to UPDATED_PDF_PATH
        defaultCountyRecordShouldNotBeFound("pdfPath.equals=" + UPDATED_PDF_PATH);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPdfPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pdfPath not equals to DEFAULT_PDF_PATH
        defaultCountyRecordShouldNotBeFound("pdfPath.notEquals=" + DEFAULT_PDF_PATH);

        // Get all the countyRecordList where pdfPath not equals to UPDATED_PDF_PATH
        defaultCountyRecordShouldBeFound("pdfPath.notEquals=" + UPDATED_PDF_PATH);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPdfPathIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pdfPath in DEFAULT_PDF_PATH or UPDATED_PDF_PATH
        defaultCountyRecordShouldBeFound("pdfPath.in=" + DEFAULT_PDF_PATH + "," + UPDATED_PDF_PATH);

        // Get all the countyRecordList where pdfPath equals to UPDATED_PDF_PATH
        defaultCountyRecordShouldNotBeFound("pdfPath.in=" + UPDATED_PDF_PATH);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPdfPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pdfPath is not null
        defaultCountyRecordShouldBeFound("pdfPath.specified=true");

        // Get all the countyRecordList where pdfPath is null
        defaultCountyRecordShouldNotBeFound("pdfPath.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPdfPathContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pdfPath contains DEFAULT_PDF_PATH
        defaultCountyRecordShouldBeFound("pdfPath.contains=" + DEFAULT_PDF_PATH);

        // Get all the countyRecordList where pdfPath contains UPDATED_PDF_PATH
        defaultCountyRecordShouldNotBeFound("pdfPath.contains=" + UPDATED_PDF_PATH);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByPdfPathNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        // Get all the countyRecordList where pdfPath does not contain DEFAULT_PDF_PATH
        defaultCountyRecordShouldNotBeFound("pdfPath.doesNotContain=" + DEFAULT_PDF_PATH);

        // Get all the countyRecordList where pdfPath does not contain UPDATED_PDF_PATH
        defaultCountyRecordShouldBeFound("pdfPath.doesNotContain=" + UPDATED_PDF_PATH);
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCountyIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);
        County county;
        if (TestUtil.findAll(em, County.class).isEmpty()) {
            county = CountyResourceIT.createEntity(em);
            em.persist(county);
            em.flush();
        } else {
            county = TestUtil.findAll(em, County.class).get(0);
        }
        em.persist(county);
        em.flush();
        countyRecord.setCounty(county);
        countyRecordRepository.saveAndFlush(countyRecord);
        Long countyId = county.getId();

        // Get all the countyRecordList where county equals to countyId
        defaultCountyRecordShouldBeFound("countyId.equals=" + countyId);

        // Get all the countyRecordList where county equals to (countyId + 1)
        defaultCountyRecordShouldNotBeFound("countyId.equals=" + (countyId + 1));
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCountyImageIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);
        CountyImage countyImage;
        if (TestUtil.findAll(em, CountyImage.class).isEmpty()) {
            countyImage = CountyImageResourceIT.createEntity(em);
            em.persist(countyImage);
            em.flush();
        } else {
            countyImage = TestUtil.findAll(em, CountyImage.class).get(0);
        }
        em.persist(countyImage);
        em.flush();
        countyRecord.setCountyImage(countyImage);
        countyImage.setCountyRecord(countyRecord);
        countyRecordRepository.saveAndFlush(countyRecord);
        Long countyImageId = countyImage.getId();

        // Get all the countyRecordList where countyImage equals to countyImageId
        defaultCountyRecordShouldBeFound("countyImageId.equals=" + countyImageId);

        // Get all the countyRecordList where countyImage equals to (countyImageId + 1)
        defaultCountyRecordShouldNotBeFound("countyImageId.equals=" + (countyImageId + 1));
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCountyImagePageIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);
        CountyImagePage countyImagePage;
        if (TestUtil.findAll(em, CountyImagePage.class).isEmpty()) {
            countyImagePage = CountyImagePageResourceIT.createEntity(em);
            em.persist(countyImagePage);
            em.flush();
        } else {
            countyImagePage = TestUtil.findAll(em, CountyImagePage.class).get(0);
        }
        em.persist(countyImagePage);
        em.flush();
        countyRecord.addCountyImagePage(countyImagePage);
        countyRecordRepository.saveAndFlush(countyRecord);
        Long countyImagePageId = countyImagePage.getId();

        // Get all the countyRecordList where countyImagePage equals to countyImagePageId
        defaultCountyRecordShouldBeFound("countyImagePageId.equals=" + countyImagePageId);

        // Get all the countyRecordList where countyImagePage equals to (countyImagePageId + 1)
        defaultCountyRecordShouldNotBeFound("countyImagePageId.equals=" + (countyImagePageId + 1));
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCountyRecordPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);
        CountyRecordParty countyRecordParty;
        if (TestUtil.findAll(em, CountyRecordParty.class).isEmpty()) {
            countyRecordParty = CountyRecordPartyResourceIT.createEntity(em);
            em.persist(countyRecordParty);
            em.flush();
        } else {
            countyRecordParty = TestUtil.findAll(em, CountyRecordParty.class).get(0);
        }
        em.persist(countyRecordParty);
        em.flush();
        countyRecord.addCountyRecordParty(countyRecordParty);
        countyRecordRepository.saveAndFlush(countyRecord);
        Long countyRecordPartyId = countyRecordParty.getId();

        // Get all the countyRecordList where countyRecordParty equals to countyRecordPartyId
        defaultCountyRecordShouldBeFound("countyRecordPartyId.equals=" + countyRecordPartyId);

        // Get all the countyRecordList where countyRecordParty equals to (countyRecordPartyId + 1)
        defaultCountyRecordShouldNotBeFound("countyRecordPartyId.equals=" + (countyRecordPartyId + 1));
    }

    @Test
    @Transactional
    void getAllCountyRecordsByCountyRecordLegalIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);
        CountyRecordLegal countyRecordLegal;
        if (TestUtil.findAll(em, CountyRecordLegal.class).isEmpty()) {
            countyRecordLegal = CountyRecordLegalResourceIT.createEntity(em);
            em.persist(countyRecordLegal);
            em.flush();
        } else {
            countyRecordLegal = TestUtil.findAll(em, CountyRecordLegal.class).get(0);
        }
        em.persist(countyRecordLegal);
        em.flush();
        countyRecord.addCountyRecordLegal(countyRecordLegal);
        countyRecordRepository.saveAndFlush(countyRecord);
        Long countyRecordLegalId = countyRecordLegal.getId();

        // Get all the countyRecordList where countyRecordLegal equals to countyRecordLegalId
        defaultCountyRecordShouldBeFound("countyRecordLegalId.equals=" + countyRecordLegalId);

        // Get all the countyRecordList where countyRecordLegal equals to (countyRecordLegalId + 1)
        defaultCountyRecordShouldNotBeFound("countyRecordLegalId.equals=" + (countyRecordLegalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountyRecordShouldBeFound(String filter) throws Exception {
        restCountyRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].cat").value(hasItem(DEFAULT_CAT)))
            .andExpect(jsonPath("$.[*].docNum").value(hasItem(DEFAULT_DOC_NUM)))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE)))
            .andExpect(jsonPath("$.[*].book").value(hasItem(DEFAULT_BOOK)))
            .andExpect(jsonPath("$.[*].setAbbr").value(hasItem(DEFAULT_SET_ABBR)))
            .andExpect(jsonPath("$.[*].vol").value(hasItem(DEFAULT_VOL)))
            .andExpect(jsonPath("$.[*].pg").value(hasItem(DEFAULT_PG)))
            .andExpect(jsonPath("$.[*].filedDate").value(hasItem(DEFAULT_FILED_DATE.toString())))
            .andExpect(jsonPath("$.[*].effDate").value(hasItem(DEFAULT_EFF_DATE.toString())))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)))
            .andExpect(jsonPath("$.[*].fips").value(hasItem(DEFAULT_FIPS)))
            .andExpect(jsonPath("$.[*].pdfPath").value(hasItem(DEFAULT_PDF_PATH)));

        // Check, that the count call also returns 1
        restCountyRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountyRecordShouldNotBeFound(String filter) throws Exception {
        restCountyRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountyRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountyRecord() throws Exception {
        // Get the countyRecord
        restCountyRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountyRecord() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        int databaseSizeBeforeUpdate = countyRecordRepository.findAll().size();

        // Update the countyRecord
        CountyRecord updatedCountyRecord = countyRecordRepository.findById(countyRecord.getId()).get();
        // Disconnect from session so that the updates on updatedCountyRecord are not directly saved in db
        em.detach(updatedCountyRecord);
        updatedCountyRecord
            .cat(UPDATED_CAT)
            .docNum(UPDATED_DOC_NUM)
            .docType(UPDATED_DOC_TYPE)
            .book(UPDATED_BOOK)
            .setAbbr(UPDATED_SET_ABBR)
            .vol(UPDATED_VOL)
            .pg(UPDATED_PG)
            .filedDate(UPDATED_FILED_DATE)
            .effDate(UPDATED_EFF_DATE)
            .recordKey(UPDATED_RECORD_KEY)
            .fips(UPDATED_FIPS)
            .pdfPath(UPDATED_PDF_PATH);

        restCountyRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCountyRecord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCountyRecord))
            )
            .andExpect(status().isOk());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeUpdate);
        CountyRecord testCountyRecord = countyRecordList.get(countyRecordList.size() - 1);
        assertThat(testCountyRecord.getCat()).isEqualTo(UPDATED_CAT);
        assertThat(testCountyRecord.getDocNum()).isEqualTo(UPDATED_DOC_NUM);
        assertThat(testCountyRecord.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testCountyRecord.getBook()).isEqualTo(UPDATED_BOOK);
        assertThat(testCountyRecord.getSetAbbr()).isEqualTo(UPDATED_SET_ABBR);
        assertThat(testCountyRecord.getVol()).isEqualTo(UPDATED_VOL);
        assertThat(testCountyRecord.getPg()).isEqualTo(UPDATED_PG);
        assertThat(testCountyRecord.getFiledDate()).isEqualTo(UPDATED_FILED_DATE);
        assertThat(testCountyRecord.getEffDate()).isEqualTo(UPDATED_EFF_DATE);
        assertThat(testCountyRecord.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyRecord.getFips()).isEqualTo(UPDATED_FIPS);
        assertThat(testCountyRecord.getPdfPath()).isEqualTo(UPDATED_PDF_PATH);
    }

    @Test
    @Transactional
    void putNonExistingCountyRecord() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordRepository.findAll().size();
        countyRecord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countyRecord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountyRecord() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordRepository.findAll().size();
        countyRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountyRecord() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordRepository.findAll().size();
        countyRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecord)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountyRecordWithPatch() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        int databaseSizeBeforeUpdate = countyRecordRepository.findAll().size();

        // Update the countyRecord using partial update
        CountyRecord partialUpdatedCountyRecord = new CountyRecord();
        partialUpdatedCountyRecord.setId(countyRecord.getId());

        partialUpdatedCountyRecord.docNum(UPDATED_DOC_NUM).vol(UPDATED_VOL).pg(UPDATED_PG).filedDate(UPDATED_FILED_DATE).fips(UPDATED_FIPS);

        restCountyRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyRecord))
            )
            .andExpect(status().isOk());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeUpdate);
        CountyRecord testCountyRecord = countyRecordList.get(countyRecordList.size() - 1);
        assertThat(testCountyRecord.getCat()).isEqualTo(DEFAULT_CAT);
        assertThat(testCountyRecord.getDocNum()).isEqualTo(UPDATED_DOC_NUM);
        assertThat(testCountyRecord.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testCountyRecord.getBook()).isEqualTo(DEFAULT_BOOK);
        assertThat(testCountyRecord.getSetAbbr()).isEqualTo(DEFAULT_SET_ABBR);
        assertThat(testCountyRecord.getVol()).isEqualTo(UPDATED_VOL);
        assertThat(testCountyRecord.getPg()).isEqualTo(UPDATED_PG);
        assertThat(testCountyRecord.getFiledDate()).isEqualTo(UPDATED_FILED_DATE);
        assertThat(testCountyRecord.getEffDate()).isEqualTo(DEFAULT_EFF_DATE);
        assertThat(testCountyRecord.getRecordKey()).isEqualTo(DEFAULT_RECORD_KEY);
        assertThat(testCountyRecord.getFips()).isEqualTo(UPDATED_FIPS);
        assertThat(testCountyRecord.getPdfPath()).isEqualTo(DEFAULT_PDF_PATH);
    }

    @Test
    @Transactional
    void fullUpdateCountyRecordWithPatch() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        int databaseSizeBeforeUpdate = countyRecordRepository.findAll().size();

        // Update the countyRecord using partial update
        CountyRecord partialUpdatedCountyRecord = new CountyRecord();
        partialUpdatedCountyRecord.setId(countyRecord.getId());

        partialUpdatedCountyRecord
            .cat(UPDATED_CAT)
            .docNum(UPDATED_DOC_NUM)
            .docType(UPDATED_DOC_TYPE)
            .book(UPDATED_BOOK)
            .setAbbr(UPDATED_SET_ABBR)
            .vol(UPDATED_VOL)
            .pg(UPDATED_PG)
            .filedDate(UPDATED_FILED_DATE)
            .effDate(UPDATED_EFF_DATE)
            .recordKey(UPDATED_RECORD_KEY)
            .fips(UPDATED_FIPS)
            .pdfPath(UPDATED_PDF_PATH);

        restCountyRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyRecord))
            )
            .andExpect(status().isOk());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeUpdate);
        CountyRecord testCountyRecord = countyRecordList.get(countyRecordList.size() - 1);
        assertThat(testCountyRecord.getCat()).isEqualTo(UPDATED_CAT);
        assertThat(testCountyRecord.getDocNum()).isEqualTo(UPDATED_DOC_NUM);
        assertThat(testCountyRecord.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testCountyRecord.getBook()).isEqualTo(UPDATED_BOOK);
        assertThat(testCountyRecord.getSetAbbr()).isEqualTo(UPDATED_SET_ABBR);
        assertThat(testCountyRecord.getVol()).isEqualTo(UPDATED_VOL);
        assertThat(testCountyRecord.getPg()).isEqualTo(UPDATED_PG);
        assertThat(testCountyRecord.getFiledDate()).isEqualTo(UPDATED_FILED_DATE);
        assertThat(testCountyRecord.getEffDate()).isEqualTo(UPDATED_EFF_DATE);
        assertThat(testCountyRecord.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyRecord.getFips()).isEqualTo(UPDATED_FIPS);
        assertThat(testCountyRecord.getPdfPath()).isEqualTo(UPDATED_PDF_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingCountyRecord() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordRepository.findAll().size();
        countyRecord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countyRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountyRecord() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordRepository.findAll().size();
        countyRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountyRecord() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordRepository.findAll().size();
        countyRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(countyRecord))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyRecord in the database
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountyRecord() throws Exception {
        // Initialize the database
        countyRecordRepository.saveAndFlush(countyRecord);

        int databaseSizeBeforeDelete = countyRecordRepository.findAll().size();

        // Delete the countyRecord
        restCountyRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, countyRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CountyRecord> countyRecordList = countyRecordRepository.findAll();
        assertThat(countyRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
