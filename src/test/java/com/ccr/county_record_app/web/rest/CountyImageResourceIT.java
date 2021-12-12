package com.ccr.county_record_app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ccr.county_record_app.IntegrationTest;
import com.ccr.county_record_app.domain.CountyImage;
import com.ccr.county_record_app.domain.CountyImagePage;
import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.repository.CountyImageRepository;
import com.ccr.county_record_app.service.criteria.CountyImageCriteria;
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
 * Integration tests for the {@link CountyImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountyImageResourceIT {

    private static final String DEFAULT_RECORD_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_KEY = "BBBBBBBBBB";

    private static final Integer DEFAULT_FILE_SIZE = 1;
    private static final Integer UPDATED_FILE_SIZE = 2;
    private static final Integer SMALLER_FILE_SIZE = 1 - 1;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_CNT = 1;
    private static final Integer UPDATED_PAGE_CNT = 2;
    private static final Integer SMALLER_PAGE_CNT = 1 - 1;

    private static final Instant DEFAULT_FILE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FILE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_MD_5_HASH = "AAAAAAAAAA";
    private static final String UPDATED_MD_5_HASH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/county-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountyImageRepository countyImageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountyImageMockMvc;

    private CountyImage countyImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyImage createEntity(EntityManager em) {
        CountyImage countyImage = new CountyImage()
            .recordKey(DEFAULT_RECORD_KEY)
            .fileSize(DEFAULT_FILE_SIZE)
            .fileName(DEFAULT_FILE_NAME)
            .pageCnt(DEFAULT_PAGE_CNT)
            .fileDate(DEFAULT_FILE_DATE)
            .filePath(DEFAULT_FILE_PATH)
            .md5Hash(DEFAULT_MD_5_HASH);
        return countyImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyImage createUpdatedEntity(EntityManager em) {
        CountyImage countyImage = new CountyImage()
            .recordKey(UPDATED_RECORD_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .fileName(UPDATED_FILE_NAME)
            .pageCnt(UPDATED_PAGE_CNT)
            .fileDate(UPDATED_FILE_DATE)
            .filePath(UPDATED_FILE_PATH)
            .md5Hash(UPDATED_MD_5_HASH);
        return countyImage;
    }

    @BeforeEach
    public void initTest() {
        countyImage = createEntity(em);
    }

    @Test
    @Transactional
    void createCountyImage() throws Exception {
        int databaseSizeBeforeCreate = countyImageRepository.findAll().size();
        // Create the CountyImage
        restCountyImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyImage)))
            .andExpect(status().isCreated());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeCreate + 1);
        CountyImage testCountyImage = countyImageList.get(countyImageList.size() - 1);
        assertThat(testCountyImage.getRecordKey()).isEqualTo(DEFAULT_RECORD_KEY);
        assertThat(testCountyImage.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testCountyImage.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testCountyImage.getPageCnt()).isEqualTo(DEFAULT_PAGE_CNT);
        assertThat(testCountyImage.getFileDate()).isEqualTo(DEFAULT_FILE_DATE);
        assertThat(testCountyImage.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testCountyImage.getMd5Hash()).isEqualTo(DEFAULT_MD_5_HASH);
    }

    @Test
    @Transactional
    void createCountyImageWithExistingId() throws Exception {
        // Create the CountyImage with an existing ID
        countyImage.setId(1L);

        int databaseSizeBeforeCreate = countyImageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountyImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyImage)))
            .andExpect(status().isBadRequest());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecordKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyImageRepository.findAll().size();
        // set the field null
        countyImage.setRecordKey(null);

        // Create the CountyImage, which fails.

        restCountyImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyImage)))
            .andExpect(status().isBadRequest());

        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountyImages() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList
        restCountyImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].pageCnt").value(hasItem(DEFAULT_PAGE_CNT)))
            .andExpect(jsonPath("$.[*].fileDate").value(hasItem(DEFAULT_FILE_DATE.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].md5Hash").value(hasItem(DEFAULT_MD_5_HASH)));
    }

    @Test
    @Transactional
    void getCountyImage() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get the countyImage
        restCountyImageMockMvc
            .perform(get(ENTITY_API_URL_ID, countyImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countyImage.getId().intValue()))
            .andExpect(jsonPath("$.recordKey").value(DEFAULT_RECORD_KEY))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.pageCnt").value(DEFAULT_PAGE_CNT))
            .andExpect(jsonPath("$.fileDate").value(DEFAULT_FILE_DATE.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.md5Hash").value(DEFAULT_MD_5_HASH));
    }

    @Test
    @Transactional
    void getCountyImagesByIdFiltering() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        Long id = countyImage.getId();

        defaultCountyImageShouldBeFound("id.equals=" + id);
        defaultCountyImageShouldNotBeFound("id.notEquals=" + id);

        defaultCountyImageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountyImageShouldNotBeFound("id.greaterThan=" + id);

        defaultCountyImageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountyImageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountyImagesByRecordKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where recordKey equals to DEFAULT_RECORD_KEY
        defaultCountyImageShouldBeFound("recordKey.equals=" + DEFAULT_RECORD_KEY);

        // Get all the countyImageList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyImageShouldNotBeFound("recordKey.equals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagesByRecordKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where recordKey not equals to DEFAULT_RECORD_KEY
        defaultCountyImageShouldNotBeFound("recordKey.notEquals=" + DEFAULT_RECORD_KEY);

        // Get all the countyImageList where recordKey not equals to UPDATED_RECORD_KEY
        defaultCountyImageShouldBeFound("recordKey.notEquals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagesByRecordKeyIsInShouldWork() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where recordKey in DEFAULT_RECORD_KEY or UPDATED_RECORD_KEY
        defaultCountyImageShouldBeFound("recordKey.in=" + DEFAULT_RECORD_KEY + "," + UPDATED_RECORD_KEY);

        // Get all the countyImageList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyImageShouldNotBeFound("recordKey.in=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagesByRecordKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where recordKey is not null
        defaultCountyImageShouldBeFound("recordKey.specified=true");

        // Get all the countyImageList where recordKey is null
        defaultCountyImageShouldNotBeFound("recordKey.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagesByRecordKeyContainsSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where recordKey contains DEFAULT_RECORD_KEY
        defaultCountyImageShouldBeFound("recordKey.contains=" + DEFAULT_RECORD_KEY);

        // Get all the countyImageList where recordKey contains UPDATED_RECORD_KEY
        defaultCountyImageShouldNotBeFound("recordKey.contains=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagesByRecordKeyNotContainsSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where recordKey does not contain DEFAULT_RECORD_KEY
        defaultCountyImageShouldNotBeFound("recordKey.doesNotContain=" + DEFAULT_RECORD_KEY);

        // Get all the countyImageList where recordKey does not contain UPDATED_RECORD_KEY
        defaultCountyImageShouldBeFound("recordKey.doesNotContain=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileSize equals to DEFAULT_FILE_SIZE
        defaultCountyImageShouldBeFound("fileSize.equals=" + DEFAULT_FILE_SIZE);

        // Get all the countyImageList where fileSize equals to UPDATED_FILE_SIZE
        defaultCountyImageShouldNotBeFound("fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileSize not equals to DEFAULT_FILE_SIZE
        defaultCountyImageShouldNotBeFound("fileSize.notEquals=" + DEFAULT_FILE_SIZE);

        // Get all the countyImageList where fileSize not equals to UPDATED_FILE_SIZE
        defaultCountyImageShouldBeFound("fileSize.notEquals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileSize in DEFAULT_FILE_SIZE or UPDATED_FILE_SIZE
        defaultCountyImageShouldBeFound("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE);

        // Get all the countyImageList where fileSize equals to UPDATED_FILE_SIZE
        defaultCountyImageShouldNotBeFound("fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileSize is not null
        defaultCountyImageShouldBeFound("fileSize.specified=true");

        // Get all the countyImageList where fileSize is null
        defaultCountyImageShouldNotBeFound("fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileSize is greater than or equal to DEFAULT_FILE_SIZE
        defaultCountyImageShouldBeFound("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the countyImageList where fileSize is greater than or equal to UPDATED_FILE_SIZE
        defaultCountyImageShouldNotBeFound("fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileSize is less than or equal to DEFAULT_FILE_SIZE
        defaultCountyImageShouldBeFound("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the countyImageList where fileSize is less than or equal to SMALLER_FILE_SIZE
        defaultCountyImageShouldNotBeFound("fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileSize is less than DEFAULT_FILE_SIZE
        defaultCountyImageShouldNotBeFound("fileSize.lessThan=" + DEFAULT_FILE_SIZE);

        // Get all the countyImageList where fileSize is less than UPDATED_FILE_SIZE
        defaultCountyImageShouldBeFound("fileSize.lessThan=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileSize is greater than DEFAULT_FILE_SIZE
        defaultCountyImageShouldNotBeFound("fileSize.greaterThan=" + DEFAULT_FILE_SIZE);

        // Get all the countyImageList where fileSize is greater than SMALLER_FILE_SIZE
        defaultCountyImageShouldBeFound("fileSize.greaterThan=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileName equals to DEFAULT_FILE_NAME
        defaultCountyImageShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the countyImageList where fileName equals to UPDATED_FILE_NAME
        defaultCountyImageShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileName not equals to DEFAULT_FILE_NAME
        defaultCountyImageShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the countyImageList where fileName not equals to UPDATED_FILE_NAME
        defaultCountyImageShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultCountyImageShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the countyImageList where fileName equals to UPDATED_FILE_NAME
        defaultCountyImageShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileName is not null
        defaultCountyImageShouldBeFound("fileName.specified=true");

        // Get all the countyImageList where fileName is null
        defaultCountyImageShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileName contains DEFAULT_FILE_NAME
        defaultCountyImageShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the countyImageList where fileName contains UPDATED_FILE_NAME
        defaultCountyImageShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileName does not contain DEFAULT_FILE_NAME
        defaultCountyImageShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the countyImageList where fileName does not contain UPDATED_FILE_NAME
        defaultCountyImageShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllCountyImagesByPageCntIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where pageCnt equals to DEFAULT_PAGE_CNT
        defaultCountyImageShouldBeFound("pageCnt.equals=" + DEFAULT_PAGE_CNT);

        // Get all the countyImageList where pageCnt equals to UPDATED_PAGE_CNT
        defaultCountyImageShouldNotBeFound("pageCnt.equals=" + UPDATED_PAGE_CNT);
    }

    @Test
    @Transactional
    void getAllCountyImagesByPageCntIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where pageCnt not equals to DEFAULT_PAGE_CNT
        defaultCountyImageShouldNotBeFound("pageCnt.notEquals=" + DEFAULT_PAGE_CNT);

        // Get all the countyImageList where pageCnt not equals to UPDATED_PAGE_CNT
        defaultCountyImageShouldBeFound("pageCnt.notEquals=" + UPDATED_PAGE_CNT);
    }

    @Test
    @Transactional
    void getAllCountyImagesByPageCntIsInShouldWork() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where pageCnt in DEFAULT_PAGE_CNT or UPDATED_PAGE_CNT
        defaultCountyImageShouldBeFound("pageCnt.in=" + DEFAULT_PAGE_CNT + "," + UPDATED_PAGE_CNT);

        // Get all the countyImageList where pageCnt equals to UPDATED_PAGE_CNT
        defaultCountyImageShouldNotBeFound("pageCnt.in=" + UPDATED_PAGE_CNT);
    }

    @Test
    @Transactional
    void getAllCountyImagesByPageCntIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where pageCnt is not null
        defaultCountyImageShouldBeFound("pageCnt.specified=true");

        // Get all the countyImageList where pageCnt is null
        defaultCountyImageShouldNotBeFound("pageCnt.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagesByPageCntIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where pageCnt is greater than or equal to DEFAULT_PAGE_CNT
        defaultCountyImageShouldBeFound("pageCnt.greaterThanOrEqual=" + DEFAULT_PAGE_CNT);

        // Get all the countyImageList where pageCnt is greater than or equal to UPDATED_PAGE_CNT
        defaultCountyImageShouldNotBeFound("pageCnt.greaterThanOrEqual=" + UPDATED_PAGE_CNT);
    }

    @Test
    @Transactional
    void getAllCountyImagesByPageCntIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where pageCnt is less than or equal to DEFAULT_PAGE_CNT
        defaultCountyImageShouldBeFound("pageCnt.lessThanOrEqual=" + DEFAULT_PAGE_CNT);

        // Get all the countyImageList where pageCnt is less than or equal to SMALLER_PAGE_CNT
        defaultCountyImageShouldNotBeFound("pageCnt.lessThanOrEqual=" + SMALLER_PAGE_CNT);
    }

    @Test
    @Transactional
    void getAllCountyImagesByPageCntIsLessThanSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where pageCnt is less than DEFAULT_PAGE_CNT
        defaultCountyImageShouldNotBeFound("pageCnt.lessThan=" + DEFAULT_PAGE_CNT);

        // Get all the countyImageList where pageCnt is less than UPDATED_PAGE_CNT
        defaultCountyImageShouldBeFound("pageCnt.lessThan=" + UPDATED_PAGE_CNT);
    }

    @Test
    @Transactional
    void getAllCountyImagesByPageCntIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where pageCnt is greater than DEFAULT_PAGE_CNT
        defaultCountyImageShouldNotBeFound("pageCnt.greaterThan=" + DEFAULT_PAGE_CNT);

        // Get all the countyImageList where pageCnt is greater than SMALLER_PAGE_CNT
        defaultCountyImageShouldBeFound("pageCnt.greaterThan=" + SMALLER_PAGE_CNT);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileDateIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileDate equals to DEFAULT_FILE_DATE
        defaultCountyImageShouldBeFound("fileDate.equals=" + DEFAULT_FILE_DATE);

        // Get all the countyImageList where fileDate equals to UPDATED_FILE_DATE
        defaultCountyImageShouldNotBeFound("fileDate.equals=" + UPDATED_FILE_DATE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileDate not equals to DEFAULT_FILE_DATE
        defaultCountyImageShouldNotBeFound("fileDate.notEquals=" + DEFAULT_FILE_DATE);

        // Get all the countyImageList where fileDate not equals to UPDATED_FILE_DATE
        defaultCountyImageShouldBeFound("fileDate.notEquals=" + UPDATED_FILE_DATE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileDateIsInShouldWork() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileDate in DEFAULT_FILE_DATE or UPDATED_FILE_DATE
        defaultCountyImageShouldBeFound("fileDate.in=" + DEFAULT_FILE_DATE + "," + UPDATED_FILE_DATE);

        // Get all the countyImageList where fileDate equals to UPDATED_FILE_DATE
        defaultCountyImageShouldNotBeFound("fileDate.in=" + UPDATED_FILE_DATE);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFileDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where fileDate is not null
        defaultCountyImageShouldBeFound("fileDate.specified=true");

        // Get all the countyImageList where fileDate is null
        defaultCountyImageShouldNotBeFound("fileDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagesByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where filePath equals to DEFAULT_FILE_PATH
        defaultCountyImageShouldBeFound("filePath.equals=" + DEFAULT_FILE_PATH);

        // Get all the countyImageList where filePath equals to UPDATED_FILE_PATH
        defaultCountyImageShouldNotBeFound("filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFilePathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where filePath not equals to DEFAULT_FILE_PATH
        defaultCountyImageShouldNotBeFound("filePath.notEquals=" + DEFAULT_FILE_PATH);

        // Get all the countyImageList where filePath not equals to UPDATED_FILE_PATH
        defaultCountyImageShouldBeFound("filePath.notEquals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where filePath in DEFAULT_FILE_PATH or UPDATED_FILE_PATH
        defaultCountyImageShouldBeFound("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH);

        // Get all the countyImageList where filePath equals to UPDATED_FILE_PATH
        defaultCountyImageShouldNotBeFound("filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where filePath is not null
        defaultCountyImageShouldBeFound("filePath.specified=true");

        // Get all the countyImageList where filePath is null
        defaultCountyImageShouldNotBeFound("filePath.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagesByFilePathContainsSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where filePath contains DEFAULT_FILE_PATH
        defaultCountyImageShouldBeFound("filePath.contains=" + DEFAULT_FILE_PATH);

        // Get all the countyImageList where filePath contains UPDATED_FILE_PATH
        defaultCountyImageShouldNotBeFound("filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where filePath does not contain DEFAULT_FILE_PATH
        defaultCountyImageShouldNotBeFound("filePath.doesNotContain=" + DEFAULT_FILE_PATH);

        // Get all the countyImageList where filePath does not contain UPDATED_FILE_PATH
        defaultCountyImageShouldBeFound("filePath.doesNotContain=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByMd5HashIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where md5Hash equals to DEFAULT_MD_5_HASH
        defaultCountyImageShouldBeFound("md5Hash.equals=" + DEFAULT_MD_5_HASH);

        // Get all the countyImageList where md5Hash equals to UPDATED_MD_5_HASH
        defaultCountyImageShouldNotBeFound("md5Hash.equals=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByMd5HashIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where md5Hash not equals to DEFAULT_MD_5_HASH
        defaultCountyImageShouldNotBeFound("md5Hash.notEquals=" + DEFAULT_MD_5_HASH);

        // Get all the countyImageList where md5Hash not equals to UPDATED_MD_5_HASH
        defaultCountyImageShouldBeFound("md5Hash.notEquals=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByMd5HashIsInShouldWork() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where md5Hash in DEFAULT_MD_5_HASH or UPDATED_MD_5_HASH
        defaultCountyImageShouldBeFound("md5Hash.in=" + DEFAULT_MD_5_HASH + "," + UPDATED_MD_5_HASH);

        // Get all the countyImageList where md5Hash equals to UPDATED_MD_5_HASH
        defaultCountyImageShouldNotBeFound("md5Hash.in=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByMd5HashIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where md5Hash is not null
        defaultCountyImageShouldBeFound("md5Hash.specified=true");

        // Get all the countyImageList where md5Hash is null
        defaultCountyImageShouldNotBeFound("md5Hash.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyImagesByMd5HashContainsSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where md5Hash contains DEFAULT_MD_5_HASH
        defaultCountyImageShouldBeFound("md5Hash.contains=" + DEFAULT_MD_5_HASH);

        // Get all the countyImageList where md5Hash contains UPDATED_MD_5_HASH
        defaultCountyImageShouldNotBeFound("md5Hash.contains=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByMd5HashNotContainsSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        // Get all the countyImageList where md5Hash does not contain DEFAULT_MD_5_HASH
        defaultCountyImageShouldNotBeFound("md5Hash.doesNotContain=" + DEFAULT_MD_5_HASH);

        // Get all the countyImageList where md5Hash does not contain UPDATED_MD_5_HASH
        defaultCountyImageShouldBeFound("md5Hash.doesNotContain=" + UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void getAllCountyImagesByCountyRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);
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
        countyImage.setCountyRecord(countyRecord);
        countyImageRepository.saveAndFlush(countyImage);
        Long countyRecordId = countyRecord.getId();

        // Get all the countyImageList where countyRecord equals to countyRecordId
        defaultCountyImageShouldBeFound("countyRecordId.equals=" + countyRecordId);

        // Get all the countyImageList where countyRecord equals to (countyRecordId + 1)
        defaultCountyImageShouldNotBeFound("countyRecordId.equals=" + (countyRecordId + 1));
    }

    @Test
    @Transactional
    void getAllCountyImagesByCountyImagePageIsEqualToSomething() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);
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
        countyImage.addCountyImagePage(countyImagePage);
        countyImageRepository.saveAndFlush(countyImage);
        Long countyImagePageId = countyImagePage.getId();

        // Get all the countyImageList where countyImagePage equals to countyImagePageId
        defaultCountyImageShouldBeFound("countyImagePageId.equals=" + countyImagePageId);

        // Get all the countyImageList where countyImagePage equals to (countyImagePageId + 1)
        defaultCountyImageShouldNotBeFound("countyImagePageId.equals=" + (countyImagePageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountyImageShouldBeFound(String filter) throws Exception {
        restCountyImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].pageCnt").value(hasItem(DEFAULT_PAGE_CNT)))
            .andExpect(jsonPath("$.[*].fileDate").value(hasItem(DEFAULT_FILE_DATE.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].md5Hash").value(hasItem(DEFAULT_MD_5_HASH)));

        // Check, that the count call also returns 1
        restCountyImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountyImageShouldNotBeFound(String filter) throws Exception {
        restCountyImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountyImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountyImage() throws Exception {
        // Get the countyImage
        restCountyImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountyImage() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        int databaseSizeBeforeUpdate = countyImageRepository.findAll().size();

        // Update the countyImage
        CountyImage updatedCountyImage = countyImageRepository.findById(countyImage.getId()).get();
        // Disconnect from session so that the updates on updatedCountyImage are not directly saved in db
        em.detach(updatedCountyImage);
        updatedCountyImage
            .recordKey(UPDATED_RECORD_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .fileName(UPDATED_FILE_NAME)
            .pageCnt(UPDATED_PAGE_CNT)
            .fileDate(UPDATED_FILE_DATE)
            .filePath(UPDATED_FILE_PATH)
            .md5Hash(UPDATED_MD_5_HASH);

        restCountyImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCountyImage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCountyImage))
            )
            .andExpect(status().isOk());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeUpdate);
        CountyImage testCountyImage = countyImageList.get(countyImageList.size() - 1);
        assertThat(testCountyImage.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyImage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testCountyImage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testCountyImage.getPageCnt()).isEqualTo(UPDATED_PAGE_CNT);
        assertThat(testCountyImage.getFileDate()).isEqualTo(UPDATED_FILE_DATE);
        assertThat(testCountyImage.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testCountyImage.getMd5Hash()).isEqualTo(UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void putNonExistingCountyImage() throws Exception {
        int databaseSizeBeforeUpdate = countyImageRepository.findAll().size();
        countyImage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countyImage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountyImage() throws Exception {
        int databaseSizeBeforeUpdate = countyImageRepository.findAll().size();
        countyImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountyImage() throws Exception {
        int databaseSizeBeforeUpdate = countyImageRepository.findAll().size();
        countyImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyImage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountyImageWithPatch() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        int databaseSizeBeforeUpdate = countyImageRepository.findAll().size();

        // Update the countyImage using partial update
        CountyImage partialUpdatedCountyImage = new CountyImage();
        partialUpdatedCountyImage.setId(countyImage.getId());

        partialUpdatedCountyImage.recordKey(UPDATED_RECORD_KEY).fileName(UPDATED_FILE_NAME).fileDate(UPDATED_FILE_DATE);

        restCountyImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyImage))
            )
            .andExpect(status().isOk());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeUpdate);
        CountyImage testCountyImage = countyImageList.get(countyImageList.size() - 1);
        assertThat(testCountyImage.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyImage.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testCountyImage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testCountyImage.getPageCnt()).isEqualTo(DEFAULT_PAGE_CNT);
        assertThat(testCountyImage.getFileDate()).isEqualTo(UPDATED_FILE_DATE);
        assertThat(testCountyImage.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testCountyImage.getMd5Hash()).isEqualTo(DEFAULT_MD_5_HASH);
    }

    @Test
    @Transactional
    void fullUpdateCountyImageWithPatch() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        int databaseSizeBeforeUpdate = countyImageRepository.findAll().size();

        // Update the countyImage using partial update
        CountyImage partialUpdatedCountyImage = new CountyImage();
        partialUpdatedCountyImage.setId(countyImage.getId());

        partialUpdatedCountyImage
            .recordKey(UPDATED_RECORD_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .fileName(UPDATED_FILE_NAME)
            .pageCnt(UPDATED_PAGE_CNT)
            .fileDate(UPDATED_FILE_DATE)
            .filePath(UPDATED_FILE_PATH)
            .md5Hash(UPDATED_MD_5_HASH);

        restCountyImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyImage))
            )
            .andExpect(status().isOk());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeUpdate);
        CountyImage testCountyImage = countyImageList.get(countyImageList.size() - 1);
        assertThat(testCountyImage.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyImage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testCountyImage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testCountyImage.getPageCnt()).isEqualTo(UPDATED_PAGE_CNT);
        assertThat(testCountyImage.getFileDate()).isEqualTo(UPDATED_FILE_DATE);
        assertThat(testCountyImage.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testCountyImage.getMd5Hash()).isEqualTo(UPDATED_MD_5_HASH);
    }

    @Test
    @Transactional
    void patchNonExistingCountyImage() throws Exception {
        int databaseSizeBeforeUpdate = countyImageRepository.findAll().size();
        countyImage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countyImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountyImage() throws Exception {
        int databaseSizeBeforeUpdate = countyImageRepository.findAll().size();
        countyImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountyImage() throws Exception {
        int databaseSizeBeforeUpdate = countyImageRepository.findAll().size();
        countyImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyImageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(countyImage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyImage in the database
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountyImage() throws Exception {
        // Initialize the database
        countyImageRepository.saveAndFlush(countyImage);

        int databaseSizeBeforeDelete = countyImageRepository.findAll().size();

        // Delete the countyImage
        restCountyImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, countyImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CountyImage> countyImageList = countyImageRepository.findAll();
        assertThat(countyImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
