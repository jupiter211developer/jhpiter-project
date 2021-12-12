package com.ccr.county_record_app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ccr.county_record_app.IntegrationTest;
import com.ccr.county_record_app.domain.CountiesAvaiable;
import com.ccr.county_record_app.domain.County;
import com.ccr.county_record_app.repository.CountiesAvaiableRepository;
import com.ccr.county_record_app.service.criteria.CountiesAvaiableCriteria;
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
 * Integration tests for the {@link CountiesAvaiableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountiesAvaiableResourceIT {

    private static final Instant DEFAULT_EARLIEST = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EARLIEST = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LATEST = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LATEST = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RECORD_COUNT = 1;
    private static final Integer UPDATED_RECORD_COUNT = 2;
    private static final Integer SMALLER_RECORD_COUNT = 1 - 1;

    private static final String DEFAULT_FIPS = "AAAAA";
    private static final String UPDATED_FIPS = "BBBBB";

    private static final String DEFAULT_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_ABBR = "AA";
    private static final String UPDATED_STATE_ABBR = "BB";

    private static final String ENTITY_API_URL = "/api/counties-avaiables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountiesAvaiableRepository countiesAvaiableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountiesAvaiableMockMvc;

    private CountiesAvaiable countiesAvaiable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountiesAvaiable createEntity(EntityManager em) {
        CountiesAvaiable countiesAvaiable = new CountiesAvaiable()
            .earliest(DEFAULT_EARLIEST)
            .latest(DEFAULT_LATEST)
            .recordCount(DEFAULT_RECORD_COUNT)
            .fips(DEFAULT_FIPS)
            .countyName(DEFAULT_COUNTY_NAME)
            .stateAbbr(DEFAULT_STATE_ABBR);
        return countiesAvaiable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountiesAvaiable createUpdatedEntity(EntityManager em) {
        CountiesAvaiable countiesAvaiable = new CountiesAvaiable()
            .earliest(UPDATED_EARLIEST)
            .latest(UPDATED_LATEST)
            .recordCount(UPDATED_RECORD_COUNT)
            .fips(UPDATED_FIPS)
            .countyName(UPDATED_COUNTY_NAME)
            .stateAbbr(UPDATED_STATE_ABBR);
        return countiesAvaiable;
    }

    @BeforeEach
    public void initTest() {
        countiesAvaiable = createEntity(em);
    }

    @Test
    @Transactional
    void createCountiesAvaiable() throws Exception {
        int databaseSizeBeforeCreate = countiesAvaiableRepository.findAll().size();
        // Create the CountiesAvaiable
        restCountiesAvaiableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countiesAvaiable))
            )
            .andExpect(status().isCreated());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeCreate + 1);
        CountiesAvaiable testCountiesAvaiable = countiesAvaiableList.get(countiesAvaiableList.size() - 1);
        assertThat(testCountiesAvaiable.getEarliest()).isEqualTo(DEFAULT_EARLIEST);
        assertThat(testCountiesAvaiable.getLatest()).isEqualTo(DEFAULT_LATEST);
        assertThat(testCountiesAvaiable.getRecordCount()).isEqualTo(DEFAULT_RECORD_COUNT);
        assertThat(testCountiesAvaiable.getFips()).isEqualTo(DEFAULT_FIPS);
        assertThat(testCountiesAvaiable.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);
        assertThat(testCountiesAvaiable.getStateAbbr()).isEqualTo(DEFAULT_STATE_ABBR);
    }

    @Test
    @Transactional
    void createCountiesAvaiableWithExistingId() throws Exception {
        // Create the CountiesAvaiable with an existing ID
        countiesAvaiable.setId(1L);

        int databaseSizeBeforeCreate = countiesAvaiableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountiesAvaiableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countiesAvaiable))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiables() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList
        restCountiesAvaiableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countiesAvaiable.getId().intValue())))
            .andExpect(jsonPath("$.[*].earliest").value(hasItem(DEFAULT_EARLIEST.toString())))
            .andExpect(jsonPath("$.[*].latest").value(hasItem(DEFAULT_LATEST.toString())))
            .andExpect(jsonPath("$.[*].recordCount").value(hasItem(DEFAULT_RECORD_COUNT)))
            .andExpect(jsonPath("$.[*].fips").value(hasItem(DEFAULT_FIPS)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].stateAbbr").value(hasItem(DEFAULT_STATE_ABBR)));
    }

    @Test
    @Transactional
    void getCountiesAvaiable() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get the countiesAvaiable
        restCountiesAvaiableMockMvc
            .perform(get(ENTITY_API_URL_ID, countiesAvaiable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countiesAvaiable.getId().intValue()))
            .andExpect(jsonPath("$.earliest").value(DEFAULT_EARLIEST.toString()))
            .andExpect(jsonPath("$.latest").value(DEFAULT_LATEST.toString()))
            .andExpect(jsonPath("$.recordCount").value(DEFAULT_RECORD_COUNT))
            .andExpect(jsonPath("$.fips").value(DEFAULT_FIPS))
            .andExpect(jsonPath("$.countyName").value(DEFAULT_COUNTY_NAME))
            .andExpect(jsonPath("$.stateAbbr").value(DEFAULT_STATE_ABBR));
    }

    @Test
    @Transactional
    void getCountiesAvaiablesByIdFiltering() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        Long id = countiesAvaiable.getId();

        defaultCountiesAvaiableShouldBeFound("id.equals=" + id);
        defaultCountiesAvaiableShouldNotBeFound("id.notEquals=" + id);

        defaultCountiesAvaiableShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountiesAvaiableShouldNotBeFound("id.greaterThan=" + id);

        defaultCountiesAvaiableShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountiesAvaiableShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByEarliestIsEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where earliest equals to DEFAULT_EARLIEST
        defaultCountiesAvaiableShouldBeFound("earliest.equals=" + DEFAULT_EARLIEST);

        // Get all the countiesAvaiableList where earliest equals to UPDATED_EARLIEST
        defaultCountiesAvaiableShouldNotBeFound("earliest.equals=" + UPDATED_EARLIEST);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByEarliestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where earliest not equals to DEFAULT_EARLIEST
        defaultCountiesAvaiableShouldNotBeFound("earliest.notEquals=" + DEFAULT_EARLIEST);

        // Get all the countiesAvaiableList where earliest not equals to UPDATED_EARLIEST
        defaultCountiesAvaiableShouldBeFound("earliest.notEquals=" + UPDATED_EARLIEST);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByEarliestIsInShouldWork() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where earliest in DEFAULT_EARLIEST or UPDATED_EARLIEST
        defaultCountiesAvaiableShouldBeFound("earliest.in=" + DEFAULT_EARLIEST + "," + UPDATED_EARLIEST);

        // Get all the countiesAvaiableList where earliest equals to UPDATED_EARLIEST
        defaultCountiesAvaiableShouldNotBeFound("earliest.in=" + UPDATED_EARLIEST);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByEarliestIsNullOrNotNull() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where earliest is not null
        defaultCountiesAvaiableShouldBeFound("earliest.specified=true");

        // Get all the countiesAvaiableList where earliest is null
        defaultCountiesAvaiableShouldNotBeFound("earliest.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByLatestIsEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where latest equals to DEFAULT_LATEST
        defaultCountiesAvaiableShouldBeFound("latest.equals=" + DEFAULT_LATEST);

        // Get all the countiesAvaiableList where latest equals to UPDATED_LATEST
        defaultCountiesAvaiableShouldNotBeFound("latest.equals=" + UPDATED_LATEST);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByLatestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where latest not equals to DEFAULT_LATEST
        defaultCountiesAvaiableShouldNotBeFound("latest.notEquals=" + DEFAULT_LATEST);

        // Get all the countiesAvaiableList where latest not equals to UPDATED_LATEST
        defaultCountiesAvaiableShouldBeFound("latest.notEquals=" + UPDATED_LATEST);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByLatestIsInShouldWork() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where latest in DEFAULT_LATEST or UPDATED_LATEST
        defaultCountiesAvaiableShouldBeFound("latest.in=" + DEFAULT_LATEST + "," + UPDATED_LATEST);

        // Get all the countiesAvaiableList where latest equals to UPDATED_LATEST
        defaultCountiesAvaiableShouldNotBeFound("latest.in=" + UPDATED_LATEST);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByLatestIsNullOrNotNull() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where latest is not null
        defaultCountiesAvaiableShouldBeFound("latest.specified=true");

        // Get all the countiesAvaiableList where latest is null
        defaultCountiesAvaiableShouldNotBeFound("latest.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByRecordCountIsEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where recordCount equals to DEFAULT_RECORD_COUNT
        defaultCountiesAvaiableShouldBeFound("recordCount.equals=" + DEFAULT_RECORD_COUNT);

        // Get all the countiesAvaiableList where recordCount equals to UPDATED_RECORD_COUNT
        defaultCountiesAvaiableShouldNotBeFound("recordCount.equals=" + UPDATED_RECORD_COUNT);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByRecordCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where recordCount not equals to DEFAULT_RECORD_COUNT
        defaultCountiesAvaiableShouldNotBeFound("recordCount.notEquals=" + DEFAULT_RECORD_COUNT);

        // Get all the countiesAvaiableList where recordCount not equals to UPDATED_RECORD_COUNT
        defaultCountiesAvaiableShouldBeFound("recordCount.notEquals=" + UPDATED_RECORD_COUNT);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByRecordCountIsInShouldWork() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where recordCount in DEFAULT_RECORD_COUNT or UPDATED_RECORD_COUNT
        defaultCountiesAvaiableShouldBeFound("recordCount.in=" + DEFAULT_RECORD_COUNT + "," + UPDATED_RECORD_COUNT);

        // Get all the countiesAvaiableList where recordCount equals to UPDATED_RECORD_COUNT
        defaultCountiesAvaiableShouldNotBeFound("recordCount.in=" + UPDATED_RECORD_COUNT);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByRecordCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where recordCount is not null
        defaultCountiesAvaiableShouldBeFound("recordCount.specified=true");

        // Get all the countiesAvaiableList where recordCount is null
        defaultCountiesAvaiableShouldNotBeFound("recordCount.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByRecordCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where recordCount is greater than or equal to DEFAULT_RECORD_COUNT
        defaultCountiesAvaiableShouldBeFound("recordCount.greaterThanOrEqual=" + DEFAULT_RECORD_COUNT);

        // Get all the countiesAvaiableList where recordCount is greater than or equal to UPDATED_RECORD_COUNT
        defaultCountiesAvaiableShouldNotBeFound("recordCount.greaterThanOrEqual=" + UPDATED_RECORD_COUNT);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByRecordCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where recordCount is less than or equal to DEFAULT_RECORD_COUNT
        defaultCountiesAvaiableShouldBeFound("recordCount.lessThanOrEqual=" + DEFAULT_RECORD_COUNT);

        // Get all the countiesAvaiableList where recordCount is less than or equal to SMALLER_RECORD_COUNT
        defaultCountiesAvaiableShouldNotBeFound("recordCount.lessThanOrEqual=" + SMALLER_RECORD_COUNT);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByRecordCountIsLessThanSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where recordCount is less than DEFAULT_RECORD_COUNT
        defaultCountiesAvaiableShouldNotBeFound("recordCount.lessThan=" + DEFAULT_RECORD_COUNT);

        // Get all the countiesAvaiableList where recordCount is less than UPDATED_RECORD_COUNT
        defaultCountiesAvaiableShouldBeFound("recordCount.lessThan=" + UPDATED_RECORD_COUNT);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByRecordCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where recordCount is greater than DEFAULT_RECORD_COUNT
        defaultCountiesAvaiableShouldNotBeFound("recordCount.greaterThan=" + DEFAULT_RECORD_COUNT);

        // Get all the countiesAvaiableList where recordCount is greater than SMALLER_RECORD_COUNT
        defaultCountiesAvaiableShouldBeFound("recordCount.greaterThan=" + SMALLER_RECORD_COUNT);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByFipsIsEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where fips equals to DEFAULT_FIPS
        defaultCountiesAvaiableShouldBeFound("fips.equals=" + DEFAULT_FIPS);

        // Get all the countiesAvaiableList where fips equals to UPDATED_FIPS
        defaultCountiesAvaiableShouldNotBeFound("fips.equals=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByFipsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where fips not equals to DEFAULT_FIPS
        defaultCountiesAvaiableShouldNotBeFound("fips.notEquals=" + DEFAULT_FIPS);

        // Get all the countiesAvaiableList where fips not equals to UPDATED_FIPS
        defaultCountiesAvaiableShouldBeFound("fips.notEquals=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByFipsIsInShouldWork() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where fips in DEFAULT_FIPS or UPDATED_FIPS
        defaultCountiesAvaiableShouldBeFound("fips.in=" + DEFAULT_FIPS + "," + UPDATED_FIPS);

        // Get all the countiesAvaiableList where fips equals to UPDATED_FIPS
        defaultCountiesAvaiableShouldNotBeFound("fips.in=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByFipsIsNullOrNotNull() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where fips is not null
        defaultCountiesAvaiableShouldBeFound("fips.specified=true");

        // Get all the countiesAvaiableList where fips is null
        defaultCountiesAvaiableShouldNotBeFound("fips.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByFipsContainsSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where fips contains DEFAULT_FIPS
        defaultCountiesAvaiableShouldBeFound("fips.contains=" + DEFAULT_FIPS);

        // Get all the countiesAvaiableList where fips contains UPDATED_FIPS
        defaultCountiesAvaiableShouldNotBeFound("fips.contains=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByFipsNotContainsSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where fips does not contain DEFAULT_FIPS
        defaultCountiesAvaiableShouldNotBeFound("fips.doesNotContain=" + DEFAULT_FIPS);

        // Get all the countiesAvaiableList where fips does not contain UPDATED_FIPS
        defaultCountiesAvaiableShouldBeFound("fips.doesNotContain=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where countyName equals to DEFAULT_COUNTY_NAME
        defaultCountiesAvaiableShouldBeFound("countyName.equals=" + DEFAULT_COUNTY_NAME);

        // Get all the countiesAvaiableList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountiesAvaiableShouldNotBeFound("countyName.equals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByCountyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where countyName not equals to DEFAULT_COUNTY_NAME
        defaultCountiesAvaiableShouldNotBeFound("countyName.notEquals=" + DEFAULT_COUNTY_NAME);

        // Get all the countiesAvaiableList where countyName not equals to UPDATED_COUNTY_NAME
        defaultCountiesAvaiableShouldBeFound("countyName.notEquals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByCountyNameIsInShouldWork() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where countyName in DEFAULT_COUNTY_NAME or UPDATED_COUNTY_NAME
        defaultCountiesAvaiableShouldBeFound("countyName.in=" + DEFAULT_COUNTY_NAME + "," + UPDATED_COUNTY_NAME);

        // Get all the countiesAvaiableList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountiesAvaiableShouldNotBeFound("countyName.in=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByCountyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where countyName is not null
        defaultCountiesAvaiableShouldBeFound("countyName.specified=true");

        // Get all the countiesAvaiableList where countyName is null
        defaultCountiesAvaiableShouldNotBeFound("countyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByCountyNameContainsSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where countyName contains DEFAULT_COUNTY_NAME
        defaultCountiesAvaiableShouldBeFound("countyName.contains=" + DEFAULT_COUNTY_NAME);

        // Get all the countiesAvaiableList where countyName contains UPDATED_COUNTY_NAME
        defaultCountiesAvaiableShouldNotBeFound("countyName.contains=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByCountyNameNotContainsSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where countyName does not contain DEFAULT_COUNTY_NAME
        defaultCountiesAvaiableShouldNotBeFound("countyName.doesNotContain=" + DEFAULT_COUNTY_NAME);

        // Get all the countiesAvaiableList where countyName does not contain UPDATED_COUNTY_NAME
        defaultCountiesAvaiableShouldBeFound("countyName.doesNotContain=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByStateAbbrIsEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where stateAbbr equals to DEFAULT_STATE_ABBR
        defaultCountiesAvaiableShouldBeFound("stateAbbr.equals=" + DEFAULT_STATE_ABBR);

        // Get all the countiesAvaiableList where stateAbbr equals to UPDATED_STATE_ABBR
        defaultCountiesAvaiableShouldNotBeFound("stateAbbr.equals=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByStateAbbrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where stateAbbr not equals to DEFAULT_STATE_ABBR
        defaultCountiesAvaiableShouldNotBeFound("stateAbbr.notEquals=" + DEFAULT_STATE_ABBR);

        // Get all the countiesAvaiableList where stateAbbr not equals to UPDATED_STATE_ABBR
        defaultCountiesAvaiableShouldBeFound("stateAbbr.notEquals=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByStateAbbrIsInShouldWork() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where stateAbbr in DEFAULT_STATE_ABBR or UPDATED_STATE_ABBR
        defaultCountiesAvaiableShouldBeFound("stateAbbr.in=" + DEFAULT_STATE_ABBR + "," + UPDATED_STATE_ABBR);

        // Get all the countiesAvaiableList where stateAbbr equals to UPDATED_STATE_ABBR
        defaultCountiesAvaiableShouldNotBeFound("stateAbbr.in=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByStateAbbrIsNullOrNotNull() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where stateAbbr is not null
        defaultCountiesAvaiableShouldBeFound("stateAbbr.specified=true");

        // Get all the countiesAvaiableList where stateAbbr is null
        defaultCountiesAvaiableShouldNotBeFound("stateAbbr.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByStateAbbrContainsSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where stateAbbr contains DEFAULT_STATE_ABBR
        defaultCountiesAvaiableShouldBeFound("stateAbbr.contains=" + DEFAULT_STATE_ABBR);

        // Get all the countiesAvaiableList where stateAbbr contains UPDATED_STATE_ABBR
        defaultCountiesAvaiableShouldNotBeFound("stateAbbr.contains=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByStateAbbrNotContainsSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        // Get all the countiesAvaiableList where stateAbbr does not contain DEFAULT_STATE_ABBR
        defaultCountiesAvaiableShouldNotBeFound("stateAbbr.doesNotContain=" + DEFAULT_STATE_ABBR);

        // Get all the countiesAvaiableList where stateAbbr does not contain UPDATED_STATE_ABBR
        defaultCountiesAvaiableShouldBeFound("stateAbbr.doesNotContain=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesAvaiablesByCountyIsEqualToSomething() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);
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
        countiesAvaiable.setCounty(county);
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);
        Long countyId = county.getId();

        // Get all the countiesAvaiableList where county equals to countyId
        defaultCountiesAvaiableShouldBeFound("countyId.equals=" + countyId);

        // Get all the countiesAvaiableList where county equals to (countyId + 1)
        defaultCountiesAvaiableShouldNotBeFound("countyId.equals=" + (countyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountiesAvaiableShouldBeFound(String filter) throws Exception {
        restCountiesAvaiableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countiesAvaiable.getId().intValue())))
            .andExpect(jsonPath("$.[*].earliest").value(hasItem(DEFAULT_EARLIEST.toString())))
            .andExpect(jsonPath("$.[*].latest").value(hasItem(DEFAULT_LATEST.toString())))
            .andExpect(jsonPath("$.[*].recordCount").value(hasItem(DEFAULT_RECORD_COUNT)))
            .andExpect(jsonPath("$.[*].fips").value(hasItem(DEFAULT_FIPS)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].stateAbbr").value(hasItem(DEFAULT_STATE_ABBR)));

        // Check, that the count call also returns 1
        restCountiesAvaiableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountiesAvaiableShouldNotBeFound(String filter) throws Exception {
        restCountiesAvaiableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountiesAvaiableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountiesAvaiable() throws Exception {
        // Get the countiesAvaiable
        restCountiesAvaiableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountiesAvaiable() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        int databaseSizeBeforeUpdate = countiesAvaiableRepository.findAll().size();

        // Update the countiesAvaiable
        CountiesAvaiable updatedCountiesAvaiable = countiesAvaiableRepository.findById(countiesAvaiable.getId()).get();
        // Disconnect from session so that the updates on updatedCountiesAvaiable are not directly saved in db
        em.detach(updatedCountiesAvaiable);
        updatedCountiesAvaiable
            .earliest(UPDATED_EARLIEST)
            .latest(UPDATED_LATEST)
            .recordCount(UPDATED_RECORD_COUNT)
            .fips(UPDATED_FIPS)
            .countyName(UPDATED_COUNTY_NAME)
            .stateAbbr(UPDATED_STATE_ABBR);

        restCountiesAvaiableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCountiesAvaiable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCountiesAvaiable))
            )
            .andExpect(status().isOk());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeUpdate);
        CountiesAvaiable testCountiesAvaiable = countiesAvaiableList.get(countiesAvaiableList.size() - 1);
        assertThat(testCountiesAvaiable.getEarliest()).isEqualTo(UPDATED_EARLIEST);
        assertThat(testCountiesAvaiable.getLatest()).isEqualTo(UPDATED_LATEST);
        assertThat(testCountiesAvaiable.getRecordCount()).isEqualTo(UPDATED_RECORD_COUNT);
        assertThat(testCountiesAvaiable.getFips()).isEqualTo(UPDATED_FIPS);
        assertThat(testCountiesAvaiable.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCountiesAvaiable.getStateAbbr()).isEqualTo(UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void putNonExistingCountiesAvaiable() throws Exception {
        int databaseSizeBeforeUpdate = countiesAvaiableRepository.findAll().size();
        countiesAvaiable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountiesAvaiableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countiesAvaiable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countiesAvaiable))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountiesAvaiable() throws Exception {
        int databaseSizeBeforeUpdate = countiesAvaiableRepository.findAll().size();
        countiesAvaiable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountiesAvaiableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countiesAvaiable))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountiesAvaiable() throws Exception {
        int databaseSizeBeforeUpdate = countiesAvaiableRepository.findAll().size();
        countiesAvaiable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountiesAvaiableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countiesAvaiable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountiesAvaiableWithPatch() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        int databaseSizeBeforeUpdate = countiesAvaiableRepository.findAll().size();

        // Update the countiesAvaiable using partial update
        CountiesAvaiable partialUpdatedCountiesAvaiable = new CountiesAvaiable();
        partialUpdatedCountiesAvaiable.setId(countiesAvaiable.getId());

        partialUpdatedCountiesAvaiable
            .earliest(UPDATED_EARLIEST)
            .latest(UPDATED_LATEST)
            .countyName(UPDATED_COUNTY_NAME)
            .stateAbbr(UPDATED_STATE_ABBR);

        restCountiesAvaiableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountiesAvaiable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountiesAvaiable))
            )
            .andExpect(status().isOk());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeUpdate);
        CountiesAvaiable testCountiesAvaiable = countiesAvaiableList.get(countiesAvaiableList.size() - 1);
        assertThat(testCountiesAvaiable.getEarliest()).isEqualTo(UPDATED_EARLIEST);
        assertThat(testCountiesAvaiable.getLatest()).isEqualTo(UPDATED_LATEST);
        assertThat(testCountiesAvaiable.getRecordCount()).isEqualTo(DEFAULT_RECORD_COUNT);
        assertThat(testCountiesAvaiable.getFips()).isEqualTo(DEFAULT_FIPS);
        assertThat(testCountiesAvaiable.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCountiesAvaiable.getStateAbbr()).isEqualTo(UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void fullUpdateCountiesAvaiableWithPatch() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        int databaseSizeBeforeUpdate = countiesAvaiableRepository.findAll().size();

        // Update the countiesAvaiable using partial update
        CountiesAvaiable partialUpdatedCountiesAvaiable = new CountiesAvaiable();
        partialUpdatedCountiesAvaiable.setId(countiesAvaiable.getId());

        partialUpdatedCountiesAvaiable
            .earliest(UPDATED_EARLIEST)
            .latest(UPDATED_LATEST)
            .recordCount(UPDATED_RECORD_COUNT)
            .fips(UPDATED_FIPS)
            .countyName(UPDATED_COUNTY_NAME)
            .stateAbbr(UPDATED_STATE_ABBR);

        restCountiesAvaiableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountiesAvaiable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountiesAvaiable))
            )
            .andExpect(status().isOk());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeUpdate);
        CountiesAvaiable testCountiesAvaiable = countiesAvaiableList.get(countiesAvaiableList.size() - 1);
        assertThat(testCountiesAvaiable.getEarliest()).isEqualTo(UPDATED_EARLIEST);
        assertThat(testCountiesAvaiable.getLatest()).isEqualTo(UPDATED_LATEST);
        assertThat(testCountiesAvaiable.getRecordCount()).isEqualTo(UPDATED_RECORD_COUNT);
        assertThat(testCountiesAvaiable.getFips()).isEqualTo(UPDATED_FIPS);
        assertThat(testCountiesAvaiable.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCountiesAvaiable.getStateAbbr()).isEqualTo(UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void patchNonExistingCountiesAvaiable() throws Exception {
        int databaseSizeBeforeUpdate = countiesAvaiableRepository.findAll().size();
        countiesAvaiable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountiesAvaiableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countiesAvaiable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countiesAvaiable))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountiesAvaiable() throws Exception {
        int databaseSizeBeforeUpdate = countiesAvaiableRepository.findAll().size();
        countiesAvaiable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountiesAvaiableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countiesAvaiable))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountiesAvaiable() throws Exception {
        int databaseSizeBeforeUpdate = countiesAvaiableRepository.findAll().size();
        countiesAvaiable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountiesAvaiableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countiesAvaiable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountiesAvaiable in the database
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountiesAvaiable() throws Exception {
        // Initialize the database
        countiesAvaiableRepository.saveAndFlush(countiesAvaiable);

        int databaseSizeBeforeDelete = countiesAvaiableRepository.findAll().size();

        // Delete the countiesAvaiable
        restCountiesAvaiableMockMvc
            .perform(delete(ENTITY_API_URL_ID, countiesAvaiable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CountiesAvaiable> countiesAvaiableList = countiesAvaiableRepository.findAll();
        assertThat(countiesAvaiableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
