package com.ccr.county_record_app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ccr.county_record_app.IntegrationTest;
import com.ccr.county_record_app.domain.CountyImage;
import com.ccr.county_record_app.domain.CountyImagePage;
import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.repository.CountyImagePageRepository;
import com.ccr.county_record_app.service.criteria.CountyImagePageCriteria;
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
 * Integration tests for the {@link CountyImagePageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountyImagePageResourceIT {

    private static final String DEFAULT_RECORD_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_KEY = "BBBBBBBBBB";

    private static final Integer DEFAULT_FILE_SIZE = 1;
    private static final Integer UPDATED_FILE_SIZE = 2;
    private static final Integer SMALLER_FILE_SIZE = 1 - 1;

    private static final Integer DEFAULT_PAGE_NO = 1;
    private static final Integer UPDATED_PAGE_NO = 2;
    private static final Integer SMALLER_PAGE_NO = 1 - 1;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_FILE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FILE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Double DEFAULT_OCR_SCORE = 1D;
    private static final Double UPDATED_OCR_SCORE = 2D;
    private static final Double SMALLER_OCR_SCORE = 1D - 1D;

    private static final String DEFAULT_MD_5_HASH = "AAAAAAAAAA";
    private static final String UPDATED_MD_5_HASH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/county-image-pages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountyImagePageRepository countyImagePageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountyImagePageMockMvc;

    private CountyImagePage countyImagePage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyImagePage createEntity(EntityManager em) {
        CountyImagePage countyImagePage = new CountyImagePage()
            .recordKey(DEFAULT_RECORD_KEY)
            .fileSize(DEFAULT_FILE_SIZE)
            .pageNo(DEFAULT_PAGE_NO)
            .fileName(DEFAULT_FILE_NAME)
            .fileDate(DEFAULT_FILE_DATE)
            .filePath(DEFAULT_FILE_PATH)
            .ocrScore(DEFAULT_OCR_SCORE)
            .md5Hash(DEFAULT_MD_5_HASH);
        return countyImagePage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyImagePage createUpdatedEntity(EntityManager em) {
        CountyImagePage countyImagePage = new CountyImagePage()
            .recordKey(UPDATED_RECORD_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .pageNo(UPDATED_PAGE_NO)
            .fileName(UPDATED_FILE_NAME)
            .fileDate(UPDATED_FILE_DATE)
            .filePath(UPDATED_FILE_PATH)
            .ocrScore(UPDATED_OCR_SCORE)
            .md5Hash(UPDATED_MD_5_HASH);
        return countyImagePage;
    }

    @BeforeEach
    public void initTest() {
        countyImagePage = createEntity(em);
    }

    @Test
    @Transactional
    void createCountyImagePage() throws Exception {
        int databaseSizeBeforeCreate = countyImagePageRepository.findAll().size();
        // Create the CountyImagePage
        restCountyImagePageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyImagePage))
            )
            .andExpect(status().isCreated());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeCreate + 1);
        CountyImagePage testCountyImagePage = countyImagePageList.get(countyImagePageList.size() - 1);
        assertThat(testCountyImagePage.getRecordKey()).isEqualTo(DEFAULT_RECORD_KEY);
        assertThat(testCountyImagePage.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testCountyImagePage.getPageNo()).isEqualTo(DEFAULT_PAGE_NO);
        assertThat(testCountyImagePage.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testCountyImagePage.getFileDate()).isEqualTo(DEFAULT_FILE_DATE);
        assertThat(testCountyImagePage.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testCountyImagePage.getOcrScore()).isEqualTo(DEFAULT_OCR_SCORE);
        assertThat(testCountyImagePage.getMd5Hash()).isEqualTo(DEFAULT_MD_5_HASH);
    }

    @Test
    @Transactional
    void createCountyImagePageWithExistingId() throws Exception {
        // Create the CountyImagePage with an existing ID
        countyImagePage.setId(1L);

        int databaseSizeBeforeCreate = countyImagePageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountyImagePageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyImagePage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecordKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyImagePageRepository.findAll().size();
        // set the field null
        countyImagePage.setRecordKey(null);

        // Create the CountyImagePage, which fails.

        restCountyImagePageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyImagePage))
            )
            .andExpect(status().isBadRequest());

        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountyImagePages() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList
        restCountyImagePageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyImagePage.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].pageNo").value(hasItem(DEFAULT_PAGE_NO)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileDate").value(hasItem(DEFAULT_FILE_DATE.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].ocrScore").value(hasItem(DEFAULT_OCR_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].md5Hash").value(hasItem(DEFAULT_MD_5_HASH)));
    }

    @Test
    @Transactional
    void getCountyImagePage() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get the countyImagePage
        restCountyImagePageMockMvc
            .perform(get(ENTITY_API_URL_ID, countyImagePage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countyImagePage.getId().intValue()))
            .andExpect(jsonPath("$.recordKey").value(DEFAULT_RECORD_KEY))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.pageNo").value(DEFAULT_PAGE_NO))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileDate").value(DEFAULT_FILE_DATE.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.ocrScore").value(DEFAULT_OCR_SCORE.doubleValue()))
            .andExpect(jsonPath("$.md5Hash").value(DEFAULT_MD_5_HASH));
    }

    @Test
    @Transactional
    void getCountyImagePagesByIdFiltering() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        Long id = countyImagePage.getId();

        defaultCountyImagePageShouldBeFound("id.equals=" + id);
        defaultCountyImagePageShouldNotBeFound("id.notEquals=" + id);

        defaultCountyImagePageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountyImagePageShouldNotBeFound("id.greaterThan=" + id);

        defaultCountyImagePageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountyImagePageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByRecordKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where recordKey equals to DEFAULT_RECORD_KEY
        defaultCountyImagePageShouldBeFound("recordKey.equals=" + DEFAULT_RECORD_KEY);

        // Get all the countyImagePageList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyImagePageShouldNotBeFound("recordKey.equals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByRecordKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where recordKey not equals to DEFAULT_RECORD_KEY
        defaultCountyImagePageShouldNotBeFound("recordKey.notEquals=" + DEFAULT_RECORD_KEY);

        // Get all the countyImagePageList where recordKey not equals to UPDATED_RECORD_KEY
        defaultCountyImagePageShouldBeFound("recordKey.notEquals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByRecordKeyIsInShouldWork() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where recordKey in DEFAULT_RECORD_KEY or UPDATED_RECORD_KEY
        defaultCountyImagePageShouldBeFound("recordKey.in=" + DEFAULT_RECORD_KEY + "," + UPDATED_RECORD_KEY);

        // Get all the countyImagePageList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyImagePageShouldNotBeFound("recordKey.in=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByRecordKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where recordKey is not null
        defaultCountyImagePageShouldBeFound("recordKey.specified=true");

        // Get all the countyImagePageList where recordKey is null
        defaultCountyImagePageShouldNotBeFound("recordKey.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByRecordKeyContainsSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where recordKey contains DEFAULT_RECORD_KEY
        defaultCountyImagePageShouldBeFound("recordKey.contains=" + DEFAULT_RECORD_KEY);

        // Get all the countyImagePageList where recordKey contains UPDATED_RECORD_KEY
        defaultCountyImagePageShouldNotBeFound("recordKey.contains=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByRecordKeyNotContainsSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where recordKey does not contain DEFAULT_RECORD_KEY
        defaultCountyImagePageShouldNotBeFound("recordKey.doesNotContain=" + DEFAULT_RECORD_KEY);

        // Get all the countyImagePageList where recordKey does not contain UPDATED_RECORD_KEY
        defaultCountyImagePageShouldBeFound("recordKey.doesNotContain=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileSize equals to DEFAULT_FILE_SIZE
        defaultCountyImagePageShouldBeFound("fileSize.equals=" + DEFAULT_FILE_SIZE);

        // Get all the countyImagePageList where fileSize equals to UPDATED_FILE_SIZE
        defaultCountyImagePageShouldNotBeFound("fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileSize not equals to DEFAULT_FILE_SIZE
        defaultCountyImagePageShouldNotBeFound("fileSize.notEquals=" + DEFAULT_FILE_SIZE);

        // Get all the countyImagePageList where fileSize not equals to UPDATED_FILE_SIZE
        defaultCountyImagePageShouldBeFound("fileSize.notEquals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileSize in DEFAULT_FILE_SIZE or UPDATED_FILE_SIZE
        defaultCountyImagePageShouldBeFound("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE);

        // Get all the countyImagePageList where fileSize equals to UPDATED_FILE_SIZE
        defaultCountyImagePageShouldNotBeFound("fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileSize is not null
        defaultCountyImagePageShouldBeFound("fileSize.specified=true");

        // Get all the countyImagePageList where fileSize is null
        defaultCountyImagePageShouldNotBeFound("fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileSize is greater than or equal to DEFAULT_FILE_SIZE
        defaultCountyImagePageShouldBeFound("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the countyImagePageList where fileSize is greater than or equal to UPDATED_FILE_SIZE
        defaultCountyImagePageShouldNotBeFound("fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileSize is less than or equal to DEFAULT_FILE_SIZE
        defaultCountyImagePageShouldBeFound("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the countyImagePageList where fileSize is less than or equal to SMALLER_FILE_SIZE
        defaultCountyImagePageShouldNotBeFound("fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileSize is less than DEFAULT_FILE_SIZE
        defaultCountyImagePageShouldNotBeFound("fileSize.lessThan=" + DEFAULT_FILE_SIZE);

        // Get all the countyImagePageList where fileSize is less than UPDATED_FILE_SIZE
        defaultCountyImagePageShouldBeFound("fileSize.lessThan=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileSize is greater than DEFAULT_FILE_SIZE
        defaultCountyImagePageShouldNotBeFound("fileSize.greaterThan=" + DEFAULT_FILE_SIZE);

        // Get all the countyImagePageList where fileSize is greater than SMALLER_FILE_SIZE
        defaultCountyImagePageShouldBeFound("fileSize.greaterThan=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByPageNoIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where pageNo equals to DEFAULT_PAGE_NO
        defaultCountyImagePageShouldBeFound("pageNo.equals=" + DEFAULT_PAGE_NO);

        // Get all the countyImagePageList where pageNo equals to UPDATED_PAGE_NO
        defaultCountyImagePageShouldNotBeFound("pageNo.equals=" + UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByPageNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where pageNo not equals to DEFAULT_PAGE_NO
        defaultCountyImagePageShouldNotBeFound("pageNo.notEquals=" + DEFAULT_PAGE_NO);

        // Get all the countyImagePageList where pageNo not equals to UPDATED_PAGE_NO
        defaultCountyImagePageShouldBeFound("pageNo.notEquals=" + UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByPageNoIsInShouldWork() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where pageNo in DEFAULT_PAGE_NO or UPDATED_PAGE_NO
        defaultCountyImagePageShouldBeFound("pageNo.in=" + DEFAULT_PAGE_NO + "," + UPDATED_PAGE_NO);

        // Get all the countyImagePageList where pageNo equals to UPDATED_PAGE_NO
        defaultCountyImagePageShouldNotBeFound("pageNo.in=" + UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByPageNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where pageNo is not null
        defaultCountyImagePageShouldBeFound("pageNo.specified=true");

        // Get all the countyImagePageList where pageNo is null
        defaultCountyImagePageShouldNotBeFound("pageNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByPageNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where pageNo is greater than or equal to DEFAULT_PAGE_NO
        defaultCountyImagePageShouldBeFound("pageNo.greaterThanOrEqual=" + DEFAULT_PAGE_NO);

        // Get all the countyImagePageList where pageNo is greater than or equal to UPDATED_PAGE_NO
        defaultCountyImagePageShouldNotBeFound("pageNo.greaterThanOrEqual=" + UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByPageNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where pageNo is less than or equal to DEFAULT_PAGE_NO
        defaultCountyImagePageShouldBeFound("pageNo.lessThanOrEqual=" + DEFAULT_PAGE_NO);

        // Get all the countyImagePageList where pageNo is less than or equal to SMALLER_PAGE_NO
        defaultCountyImagePageShouldNotBeFound("pageNo.lessThanOrEqual=" + SMALLER_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByPageNoIsLessThanSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where pageNo is less than DEFAULT_PAGE_NO
        defaultCountyImagePageShouldNotBeFound("pageNo.lessThan=" + DEFAULT_PAGE_NO);

        // Get all the countyImagePageList where pageNo is less than UPDATED_PAGE_NO
        defaultCountyImagePageShouldBeFound("pageNo.lessThan=" + UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByPageNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where pageNo is greater than DEFAULT_PAGE_NO
        defaultCountyImagePageShouldNotBeFound("pageNo.greaterThan=" + DEFAULT_PAGE_NO);

        // Get all the countyImagePageList where pageNo is greater than SMALLER_PAGE_NO
        defaultCountyImagePageShouldBeFound("pageNo.greaterThan=" + SMALLER_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileName equals to DEFAULT_FILE_NAME
        defaultCountyImagePageShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the countyImagePageList where fileName equals to UPDATED_FILE_NAME
        defaultCountyImagePageShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileName not equals to DEFAULT_FILE_NAME
        defaultCountyImagePageShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the countyImagePageList where fileName not equals to UPDATED_FILE_NAME
        defaultCountyImagePageShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultCountyImagePageShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the countyImagePageList where fileName equals to UPDATED_FILE_NAME
        defaultCountyImagePageShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileName is not null
        defaultCountyImagePageShouldBeFound("fileName.specified=true");

        // Get all the countyImagePageList where fileName is null
        defaultCountyImagePageShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileName contains DEFAULT_FILE_NAME
        defaultCountyImagePageShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the countyImagePageList where fileName contains UPDATED_FILE_NAME
        defaultCountyImagePageShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileName does not contain DEFAULT_FILE_NAME
        defaultCountyImagePageShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the countyImagePageList where fileName does not contain UPDATED_FILE_NAME
        defaultCountyImagePageShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileDateIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileDate equals to DEFAULT_FILE_DATE
        defaultCountyImagePageShouldBeFound("fileDate.equals=" + DEFAULT_FILE_DATE);

        // Get all the countyImagePageList where fileDate equals to UPDATED_FILE_DATE
        defaultCountyImagePageShouldNotBeFound("fileDate.equals=" + UPDATED_FILE_DATE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileDate not equals to DEFAULT_FILE_DATE
        defaultCountyImagePageShouldNotBeFound("fileDate.notEquals=" + DEFAULT_FILE_DATE);

        // Get all the countyImagePageList where fileDate not equals to UPDATED_FILE_DATE
        defaultCountyImagePageShouldBeFound("fileDate.notEquals=" + UPDATED_FILE_DATE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileDateIsInShouldWork() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileDate in DEFAULT_FILE_DATE or UPDATED_FILE_DATE
        defaultCountyImagePageShouldBeFound("fileDate.in=" + DEFAULT_FILE_DATE + "," + UPDATED_FILE_DATE);

        // Get all the countyImagePageList where fileDate equals to UPDATED_FILE_DATE
        defaultCountyImagePageShouldNotBeFound("fileDate.in=" + UPDATED_FILE_DATE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFileDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where fileDate is not null
        defaultCountyImagePageShouldBeFound("fileDate.specified=true");

        // Get all the countyImagePageList where fileDate is null
        defaultCountyImagePageShouldNotBeFound("fileDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where filePath equals to DEFAULT_FILE_PATH
        defaultCountyImagePageShouldBeFound("filePath.equals=" + DEFAULT_FILE_PATH);

        // Get all the countyImagePageList where filePath equals to UPDATED_FILE_PATH
        defaultCountyImagePageShouldNotBeFound("filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFilePathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where filePath not equals to DEFAULT_FILE_PATH
        defaultCountyImagePageShouldNotBeFound("filePath.notEquals=" + DEFAULT_FILE_PATH);

        // Get all the countyImagePageList where filePath not equals to UPDATED_FILE_PATH
        defaultCountyImagePageShouldBeFound("filePath.notEquals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where filePath in DEFAULT_FILE_PATH or UPDATED_FILE_PATH
        defaultCountyImagePageShouldBeFound("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH);

        // Get all the countyImagePageList where filePath equals to UPDATED_FILE_PATH
        defaultCountyImagePageShouldNotBeFound("filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where filePath is not null
        defaultCountyImagePageShouldBeFound("filePath.specified=true");

        // Get all the countyImagePageList where filePath is null
        defaultCountyImagePageShouldNotBeFound("filePath.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFilePathContainsSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where filePath contains DEFAULT_FILE_PATH
        defaultCountyImagePageShouldBeFound("filePath.contains=" + DEFAULT_FILE_PATH);

        // Get all the countyImagePageList where filePath contains UPDATED_FILE_PATH
        defaultCountyImagePageShouldNotBeFound("filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where filePath does not contain DEFAULT_FILE_PATH
        defaultCountyImagePageShouldNotBeFound("filePath.doesNotContain=" + DEFAULT_FILE_PATH);

        // Get all the countyImagePageList where filePath does not contain UPDATED_FILE_PATH
        defaultCountyImagePageShouldBeFound("filePath.doesNotContain=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByOcrScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where ocrScore equals to DEFAULT_OCR_SCORE
        defaultCountyImagePageShouldBeFound("ocrScore.equals=" + DEFAULT_OCR_SCORE);

        // Get all the countyImagePageList where ocrScore equals to UPDATED_OCR_SCORE
        defaultCountyImagePageShouldNotBeFound("ocrScore.equals=" + UPDATED_OCR_SCORE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByOcrScoreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where ocrScore not equals to DEFAULT_OCR_SCORE
        defaultCountyImagePageShouldNotBeFound("ocrScore.notEquals=" + DEFAULT_OCR_SCORE);

        // Get all the countyImagePageList where ocrScore not equals to UPDATED_OCR_SCORE
        defaultCountyImagePageShouldBeFound("ocrScore.notEquals=" + UPDATED_OCR_SCORE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByOcrScoreIsInShouldWork() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where ocrScore in DEFAULT_OCR_SCORE or UPDATED_OCR_SCORE
        defaultCountyImagePageShouldBeFound("ocrScore.in=" + DEFAULT_OCR_SCORE + "," + UPDATED_OCR_SCORE);

        // Get all the countyImagePageList where ocrScore equals to UPDATED_OCR_SCORE
        defaultCountyImagePageShouldNotBeFound("ocrScore.in=" + UPDATED_OCR_SCORE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByOcrScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where ocrScore is not null
        defaultCountyImagePageShouldBeFound("ocrScore.specified=true");

        // Get all the countyImagePageList where ocrScore is null
        defaultCountyImagePageShouldNotBeFound("ocrScore.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByOcrScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where ocrScore is greater than or equal to DEFAULT_OCR_SCORE
        defaultCountyImagePageShouldBeFound("ocrScore.greaterThanOrEqual=" + DEFAULT_OCR_SCORE);

        // Get all the countyImagePageList where ocrScore is greater than or equal to UPDATED_OCR_SCORE
        defaultCountyImagePageShouldNotBeFound("ocrScore.greaterThanOrEqual=" + UPDATED_OCR_SCORE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByOcrScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where ocrScore is less than or equal to DEFAULT_OCR_SCORE
        defaultCountyImagePageShouldBeFound("ocrScore.lessThanOrEqual=" + DEFAULT_OCR_SCORE);

        // Get all the countyImagePageList where ocrScore is less than or equal to SMALLER_OCR_SCORE
        defaultCountyImagePageShouldNotBeFound("ocrScore.lessThanOrEqual=" + SMALLER_OCR_SCORE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByOcrScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where ocrScore is less than DEFAULT_OCR_SCORE
        defaultCountyImagePageShouldNotBeFound("ocrScore.lessThan=" + DEFAULT_OCR_SCORE);

        // Get all the countyImagePageList where ocrScore is less than UPDATED_OCR_SCORE
        defaultCountyImagePageShouldBeFound("ocrScore.lessThan=" + UPDATED_OCR_SCORE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByOcrScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where ocrScore is greater than DEFAULT_OCR_SCORE
        defaultCountyImagePageShouldNotBeFound("ocrScore.greaterThan=" + DEFAULT_OCR_SCORE);

        // Get all the countyImagePageList where ocrScore is greater than SMALLER_OCR_SCORE
        defaultCountyImagePageShouldBeFound("ocrScore.greaterThan=" + SMALLER_OCR_SCORE);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByMd5HashIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where md5Hash equals to DEFAULT_MD_5_HASH
        defaultCountyImagePageShouldBeFound("md5Hash.equals=" + DEFAULT_MD_5_HASH);

        // Get all the countyImagePageList where md5Hash equals to UPDATED_MD_5_HASH
        defaultCountyImagePageShouldNotBeFound("md5Hash.equals=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByMd5HashIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where md5Hash not equals to DEFAULT_MD_5_HASH
        defaultCountyImagePageShouldNotBeFound("md5Hash.notEquals=" + DEFAULT_MD_5_HASH);

        // Get all the countyImagePageList where md5Hash not equals to UPDATED_MD_5_HASH
        defaultCountyImagePageShouldBeFound("md5Hash.notEquals=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByMd5HashIsInShouldWork() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where md5Hash in DEFAULT_MD_5_HASH or UPDATED_MD_5_HASH
        defaultCountyImagePageShouldBeFound("md5Hash.in=" + DEFAULT_MD_5_HASH + "," + UPDATED_MD_5_HASH);

        // Get all the countyImagePageList where md5Hash equals to UPDATED_MD_5_HASH
        defaultCountyImagePageShouldNotBeFound("md5Hash.in=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByMd5HashIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where md5Hash is not null
        defaultCountyImagePageShouldBeFound("md5Hash.specified=true");

        // Get all the countyImagePageList where md5Hash is null
        defaultCountyImagePageShouldNotBeFound("md5Hash.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByMd5HashContainsSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where md5Hash contains DEFAULT_MD_5_HASH
        defaultCountyImagePageShouldBeFound("md5Hash.contains=" + DEFAULT_MD_5_HASH);

        // Get all the countyImagePageList where md5Hash contains UPDATED_MD_5_HASH
        defaultCountyImagePageShouldNotBeFound("md5Hash.contains=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByMd5HashNotContainsSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        // Get all the countyImagePageList where md5Hash does not contain DEFAULT_MD_5_HASH
        defaultCountyImagePageShouldNotBeFound("md5Hash.doesNotContain=" + DEFAULT_MD_5_HASH);

        // Get all the countyImagePageList where md5Hash does not contain UPDATED_MD_5_HASH
        defaultCountyImagePageShouldBeFound("md5Hash.doesNotContain=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByCountyRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);
        CountyRecord countyRecord;
        if (TestUtil.findAll(em, CountyRecord.class).isEmpty()) {
            countyRecord = CountyRecordResourceIT.createEntity(em);
            em.persist(countyRecord);
            em.flush();
        } else {
            countyRecord = TestUtil.findAll(em, CountyRecord.class).get(0);
        }
        em.persist(countyRecord);
        em.flush();
        countyImagePage.setCountyRecord(countyRecord);
        countyImagePageRepository.saveAndFlush(countyImagePage);
        Long countyRecordId = countyRecord.getId();

        // Get all the countyImagePageList where countyRecord equals to countyRecordId
        defaultCountyImagePageShouldBeFound("countyRecordId.equals=" + countyRecordId);

        // Get all the countyImagePageList where countyRecord equals to (countyRecordId + 1)
        defaultCountyImagePageShouldNotBeFound("countyRecordId.equals=" + (countyRecordId + 1));
    }

    @Test
    @Transactional
    void getAllCountyImagePagesByCountyImageIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);
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
        countyImagePage.setCountyImage(countyImage);
        countyImagePageRepository.saveAndFlush(countyImagePage);
        Long countyImageId = countyImage.getId();

        // Get all the countyImagePageList where countyImage equals to countyImageId
        defaultCountyImagePageShouldBeFound("countyImageId.equals=" + countyImageId);

        // Get all the countyImagePageList where countyImage equals to (countyImageId + 1)
        defaultCountyImagePageShouldNotBeFound("countyImageId.equals=" + (countyImageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountyImagePageShouldBeFound(String filter) throws Exception {
        restCountyImagePageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyImagePage.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].pageNo").value(hasItem(DEFAULT_PAGE_NO)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileDate").value(hasItem(DEFAULT_FILE_DATE.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].ocrScore").value(hasItem(DEFAULT_OCR_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].md5Hash").value(hasItem(DEFAULT_MD_5_HASH)));

        // Check, that the count call also returns 1
        restCountyImagePageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountyImagePageShouldNotBeFound(String filter) throws Exception {
        restCountyImagePageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountyImagePageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountyImagePage() throws Exception {
        // Get the countyImagePage
        restCountyImagePageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountyImagePage() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        int databaseSizeBeforeUpdate = countyImagePageRepository.findAll().size();

        // Update the countyImagePage
        CountyImagePage updatedCountyImagePage = countyImagePageRepository.findById(countyImagePage.getId()).get();
        // Disconnect from session so that the updates on updatedCountyImagePage are not directly saved in db
        em.detach(updatedCountyImagePage);
        updatedCountyImagePage
            .recordKey(UPDATED_RECORD_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .pageNo(UPDATED_PAGE_NO)
            .fileName(UPDATED_FILE_NAME)
            .fileDate(UPDATED_FILE_DATE)
            .filePath(UPDATED_FILE_PATH)
            .ocrScore(UPDATED_OCR_SCORE)
            .md5Hash(UPDATED_MD_5_HASH);

        restCountyImagePageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCountyImagePage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCountyImagePage))
            )
            .andExpect(status().isOk());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeUpdate);
        CountyImagePage testCountyImagePage = countyImagePageList.get(countyImagePageList.size() - 1);
        assertThat(testCountyImagePage.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyImagePage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testCountyImagePage.getPageNo()).isEqualTo(UPDATED_PAGE_NO);
        assertThat(testCountyImagePage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testCountyImagePage.getFileDate()).isEqualTo(UPDATED_FILE_DATE);
        assertThat(testCountyImagePage.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testCountyImagePage.getOcrScore()).isEqualTo(UPDATED_OCR_SCORE);
        assertThat(testCountyImagePage.getMd5Hash()).isEqualTo(UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void putNonExistingCountyImagePage() throws Exception {
        int databaseSizeBeforeUpdate = countyImagePageRepository.findAll().size();
        countyImagePage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyImagePageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countyImagePage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyImagePage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountyImagePage() throws Exception {
        int databaseSizeBeforeUpdate = countyImagePageRepository.findAll().size();
        countyImagePage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyImagePageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyImagePage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountyImagePage() throws Exception {
        int databaseSizeBeforeUpdate = countyImagePageRepository.findAll().size();
        countyImagePage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyImagePageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyImagePage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountyImagePageWithPatch() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        int databaseSizeBeforeUpdate = countyImagePageRepository.findAll().size();

        // Update the countyImagePage using partial update
        CountyImagePage partialUpdatedCountyImagePage = new CountyImagePage();
        partialUpdatedCountyImagePage.setId(countyImagePage.getId());

        partialUpdatedCountyImagePage
            .fileSize(UPDATED_FILE_SIZE)
            .pageNo(UPDATED_PAGE_NO)
            .fileName(UPDATED_FILE_NAME)
            .md5Hash(UPDATED_MD_5_HASH);

        restCountyImagePageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyImagePage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyImagePage))
            )
            .andExpect(status().isOk());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeUpdate);
        CountyImagePage testCountyImagePage = countyImagePageList.get(countyImagePageList.size() - 1);
        assertThat(testCountyImagePage.getRecordKey()).isEqualTo(DEFAULT_RECORD_KEY);
        assertThat(testCountyImagePage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testCountyImagePage.getPageNo()).isEqualTo(UPDATED_PAGE_NO);
        assertThat(testCountyImagePage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testCountyImagePage.getFileDate()).isEqualTo(DEFAULT_FILE_DATE);
        assertThat(testCountyImagePage.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testCountyImagePage.getOcrScore()).isEqualTo(DEFAULT_OCR_SCORE);
        assertThat(testCountyImagePage.getMd5Hash()).isEqualTo(UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void fullUpdateCountyImagePageWithPatch() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        int databaseSizeBeforeUpdate = countyImagePageRepository.findAll().size();

        // Update the countyImagePage using partial update
        CountyImagePage partialUpdatedCountyImagePage = new CountyImagePage();
        partialUpdatedCountyImagePage.setId(countyImagePage.getId());

        partialUpdatedCountyImagePage
            .recordKey(UPDATED_RECORD_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .pageNo(UPDATED_PAGE_NO)
            .fileName(UPDATED_FILE_NAME)
            .fileDate(UPDATED_FILE_DATE)
            .filePath(UPDATED_FILE_PATH)
            .ocrScore(UPDATED_OCR_SCORE)
            .md5Hash(UPDATED_MD_5_HASH);

        restCountyImagePageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyImagePage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyImagePage))
            )
            .andExpect(status().isOk());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeUpdate);
        CountyImagePage testCountyImagePage = countyImagePageList.get(countyImagePageList.size() - 1);
        assertThat(testCountyImagePage.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyImagePage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testCountyImagePage.getPageNo()).isEqualTo(UPDATED_PAGE_NO);
        assertThat(testCountyImagePage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testCountyImagePage.getFileDate()).isEqualTo(UPDATED_FILE_DATE);
        assertThat(testCountyImagePage.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testCountyImagePage.getOcrScore()).isEqualTo(UPDATED_OCR_SCORE);
        assertThat(testCountyImagePage.getMd5Hash()).isEqualTo(UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void patchNonExistingCountyImagePage() throws Exception {
        int databaseSizeBeforeUpdate = countyImagePageRepository.findAll().size();
        countyImagePage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyImagePageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countyImagePage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyImagePage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountyImagePage() throws Exception {
        int databaseSizeBeforeUpdate = countyImagePageRepository.findAll().size();
        countyImagePage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyImagePageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyImagePage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountyImagePage() throws Exception {
        int databaseSizeBeforeUpdate = countyImagePageRepository.findAll().size();
        countyImagePage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyImagePageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyImagePage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyImagePage in the database
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountyImagePage() throws Exception {
        // Initialize the database
        countyImagePageRepository.saveAndFlush(countyImagePage);

        int databaseSizeBeforeDelete = countyImagePageRepository.findAll().size();

        // Delete the countyImagePage
        restCountyImagePageMockMvc
            .perform(delete(ENTITY_API_URL_ID, countyImagePage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CountyImagePage> countyImagePageList = countyImagePageRepository.findAll();
        assertThat(countyImagePageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
