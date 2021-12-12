package com.ccr.county_record_app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ccr.county_record_app.IntegrationTest;
import com.ccr.county_record_app.domain.CountiesAvaiable;
import com.ccr.county_record_app.domain.County;
import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.domain.State;
import com.ccr.county_record_app.repository.CountyRepository;
import com.ccr.county_record_app.service.criteria.CountyCriteria;
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
 * Integration tests for the {@link CountyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountyResourceIT {

    private static final String DEFAULT_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNTY_FIPS = "AAA";
    private static final String UPDATED_CNTY_FIPS = "BBB";

    private static final String DEFAULT_STATE_ABBR = "AA";
    private static final String UPDATED_STATE_ABBR = "BB";

    private static final String DEFAULT_ST_FIPS = "AA";
    private static final String UPDATED_ST_FIPS = "BB";

    private static final String DEFAULT_FIPS = "AAAAA";
    private static final String UPDATED_FIPS = "BBBBB";

    private static final String ENTITY_API_URL = "/api/counties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountyMockMvc;

    private County county;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static County createEntity(EntityManager em) {
        County county = new County()
            .countyName(DEFAULT_COUNTY_NAME)
            .cntyFips(DEFAULT_CNTY_FIPS)
            .stateAbbr(DEFAULT_STATE_ABBR)
            .stFips(DEFAULT_ST_FIPS)
            .fips(DEFAULT_FIPS);
        return county;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static County createUpdatedEntity(EntityManager em) {
        County county = new County()
            .countyName(UPDATED_COUNTY_NAME)
            .cntyFips(UPDATED_CNTY_FIPS)
            .stateAbbr(UPDATED_STATE_ABBR)
            .stFips(UPDATED_ST_FIPS)
            .fips(UPDATED_FIPS);
        return county;
    }

    @BeforeEach
    public void initTest() {
        county = createEntity(em);
    }

    @Test
    @Transactional
    void createCounty() throws Exception {
        int databaseSizeBeforeCreate = countyRepository.findAll().size();
        // Create the County
        restCountyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(county)))
            .andExpect(status().isCreated());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeCreate + 1);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);
        assertThat(testCounty.getCntyFips()).isEqualTo(DEFAULT_CNTY_FIPS);
        assertThat(testCounty.getStateAbbr()).isEqualTo(DEFAULT_STATE_ABBR);
        assertThat(testCounty.getStFips()).isEqualTo(DEFAULT_ST_FIPS);
        assertThat(testCounty.getFips()).isEqualTo(DEFAULT_FIPS);
    }

    @Test
    @Transactional
    void createCountyWithExistingId() throws Exception {
        // Create the County with an existing ID
        county.setId(1L);

        int databaseSizeBeforeCreate = countyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(county)))
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCounties() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(county.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].cntyFips").value(hasItem(DEFAULT_CNTY_FIPS)))
            .andExpect(jsonPath("$.[*].stateAbbr").value(hasItem(DEFAULT_STATE_ABBR)))
            .andExpect(jsonPath("$.[*].stFips").value(hasItem(DEFAULT_ST_FIPS)))
            .andExpect(jsonPath("$.[*].fips").value(hasItem(DEFAULT_FIPS)));
    }

    @Test
    @Transactional
    void getCounty() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get the county
        restCountyMockMvc
            .perform(get(ENTITY_API_URL_ID, county.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(county.getId().intValue()))
            .andExpect(jsonPath("$.countyName").value(DEFAULT_COUNTY_NAME))
            .andExpect(jsonPath("$.cntyFips").value(DEFAULT_CNTY_FIPS))
            .andExpect(jsonPath("$.stateAbbr").value(DEFAULT_STATE_ABBR))
            .andExpect(jsonPath("$.stFips").value(DEFAULT_ST_FIPS))
            .andExpect(jsonPath("$.fips").value(DEFAULT_FIPS));
    }

    @Test
    @Transactional
    void getCountiesByIdFiltering() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        Long id = county.getId();

        defaultCountyShouldBeFound("id.equals=" + id);
        defaultCountyShouldNotBeFound("id.notEquals=" + id);

        defaultCountyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountyShouldNotBeFound("id.greaterThan=" + id);

        defaultCountyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName equals to DEFAULT_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.equals=" + DEFAULT_COUNTY_NAME);

        // Get all the countyList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.equals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName not equals to DEFAULT_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.notEquals=" + DEFAULT_COUNTY_NAME);

        // Get all the countyList where countyName not equals to UPDATED_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.notEquals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName in DEFAULT_COUNTY_NAME or UPDATED_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.in=" + DEFAULT_COUNTY_NAME + "," + UPDATED_COUNTY_NAME);

        // Get all the countyList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.in=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName is not null
        defaultCountyShouldBeFound("countyName.specified=true");

        // Get all the countyList where countyName is null
        defaultCountyShouldNotBeFound("countyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName contains DEFAULT_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.contains=" + DEFAULT_COUNTY_NAME);

        // Get all the countyList where countyName contains UPDATED_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.contains=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCountyNameNotContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where countyName does not contain DEFAULT_COUNTY_NAME
        defaultCountyShouldNotBeFound("countyName.doesNotContain=" + DEFAULT_COUNTY_NAME);

        // Get all the countyList where countyName does not contain UPDATED_COUNTY_NAME
        defaultCountyShouldBeFound("countyName.doesNotContain=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountiesByCntyFipsIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where cntyFips equals to DEFAULT_CNTY_FIPS
        defaultCountyShouldBeFound("cntyFips.equals=" + DEFAULT_CNTY_FIPS);

        // Get all the countyList where cntyFips equals to UPDATED_CNTY_FIPS
        defaultCountyShouldNotBeFound("cntyFips.equals=" + UPDATED_CNTY_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByCntyFipsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where cntyFips not equals to DEFAULT_CNTY_FIPS
        defaultCountyShouldNotBeFound("cntyFips.notEquals=" + DEFAULT_CNTY_FIPS);

        // Get all the countyList where cntyFips not equals to UPDATED_CNTY_FIPS
        defaultCountyShouldBeFound("cntyFips.notEquals=" + UPDATED_CNTY_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByCntyFipsIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where cntyFips in DEFAULT_CNTY_FIPS or UPDATED_CNTY_FIPS
        defaultCountyShouldBeFound("cntyFips.in=" + DEFAULT_CNTY_FIPS + "," + UPDATED_CNTY_FIPS);

        // Get all the countyList where cntyFips equals to UPDATED_CNTY_FIPS
        defaultCountyShouldNotBeFound("cntyFips.in=" + UPDATED_CNTY_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByCntyFipsIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where cntyFips is not null
        defaultCountyShouldBeFound("cntyFips.specified=true");

        // Get all the countyList where cntyFips is null
        defaultCountyShouldNotBeFound("cntyFips.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesByCntyFipsContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where cntyFips contains DEFAULT_CNTY_FIPS
        defaultCountyShouldBeFound("cntyFips.contains=" + DEFAULT_CNTY_FIPS);

        // Get all the countyList where cntyFips contains UPDATED_CNTY_FIPS
        defaultCountyShouldNotBeFound("cntyFips.contains=" + UPDATED_CNTY_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByCntyFipsNotContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where cntyFips does not contain DEFAULT_CNTY_FIPS
        defaultCountyShouldNotBeFound("cntyFips.doesNotContain=" + DEFAULT_CNTY_FIPS);

        // Get all the countyList where cntyFips does not contain UPDATED_CNTY_FIPS
        defaultCountyShouldBeFound("cntyFips.doesNotContain=" + UPDATED_CNTY_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByStateAbbrIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stateAbbr equals to DEFAULT_STATE_ABBR
        defaultCountyShouldBeFound("stateAbbr.equals=" + DEFAULT_STATE_ABBR);

        // Get all the countyList where stateAbbr equals to UPDATED_STATE_ABBR
        defaultCountyShouldNotBeFound("stateAbbr.equals=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesByStateAbbrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stateAbbr not equals to DEFAULT_STATE_ABBR
        defaultCountyShouldNotBeFound("stateAbbr.notEquals=" + DEFAULT_STATE_ABBR);

        // Get all the countyList where stateAbbr not equals to UPDATED_STATE_ABBR
        defaultCountyShouldBeFound("stateAbbr.notEquals=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesByStateAbbrIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stateAbbr in DEFAULT_STATE_ABBR or UPDATED_STATE_ABBR
        defaultCountyShouldBeFound("stateAbbr.in=" + DEFAULT_STATE_ABBR + "," + UPDATED_STATE_ABBR);

        // Get all the countyList where stateAbbr equals to UPDATED_STATE_ABBR
        defaultCountyShouldNotBeFound("stateAbbr.in=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesByStateAbbrIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stateAbbr is not null
        defaultCountyShouldBeFound("stateAbbr.specified=true");

        // Get all the countyList where stateAbbr is null
        defaultCountyShouldNotBeFound("stateAbbr.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesByStateAbbrContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stateAbbr contains DEFAULT_STATE_ABBR
        defaultCountyShouldBeFound("stateAbbr.contains=" + DEFAULT_STATE_ABBR);

        // Get all the countyList where stateAbbr contains UPDATED_STATE_ABBR
        defaultCountyShouldNotBeFound("stateAbbr.contains=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesByStateAbbrNotContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stateAbbr does not contain DEFAULT_STATE_ABBR
        defaultCountyShouldNotBeFound("stateAbbr.doesNotContain=" + DEFAULT_STATE_ABBR);

        // Get all the countyList where stateAbbr does not contain UPDATED_STATE_ABBR
        defaultCountyShouldBeFound("stateAbbr.doesNotContain=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllCountiesByStFipsIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stFips equals to DEFAULT_ST_FIPS
        defaultCountyShouldBeFound("stFips.equals=" + DEFAULT_ST_FIPS);

        // Get all the countyList where stFips equals to UPDATED_ST_FIPS
        defaultCountyShouldNotBeFound("stFips.equals=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByStFipsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stFips not equals to DEFAULT_ST_FIPS
        defaultCountyShouldNotBeFound("stFips.notEquals=" + DEFAULT_ST_FIPS);

        // Get all the countyList where stFips not equals to UPDATED_ST_FIPS
        defaultCountyShouldBeFound("stFips.notEquals=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByStFipsIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stFips in DEFAULT_ST_FIPS or UPDATED_ST_FIPS
        defaultCountyShouldBeFound("stFips.in=" + DEFAULT_ST_FIPS + "," + UPDATED_ST_FIPS);

        // Get all the countyList where stFips equals to UPDATED_ST_FIPS
        defaultCountyShouldNotBeFound("stFips.in=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByStFipsIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stFips is not null
        defaultCountyShouldBeFound("stFips.specified=true");

        // Get all the countyList where stFips is null
        defaultCountyShouldNotBeFound("stFips.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesByStFipsContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stFips contains DEFAULT_ST_FIPS
        defaultCountyShouldBeFound("stFips.contains=" + DEFAULT_ST_FIPS);

        // Get all the countyList where stFips contains UPDATED_ST_FIPS
        defaultCountyShouldNotBeFound("stFips.contains=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByStFipsNotContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where stFips does not contain DEFAULT_ST_FIPS
        defaultCountyShouldNotBeFound("stFips.doesNotContain=" + DEFAULT_ST_FIPS);

        // Get all the countyList where stFips does not contain UPDATED_ST_FIPS
        defaultCountyShouldBeFound("stFips.doesNotContain=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByFipsIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where fips equals to DEFAULT_FIPS
        defaultCountyShouldBeFound("fips.equals=" + DEFAULT_FIPS);

        // Get all the countyList where fips equals to UPDATED_FIPS
        defaultCountyShouldNotBeFound("fips.equals=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByFipsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where fips not equals to DEFAULT_FIPS
        defaultCountyShouldNotBeFound("fips.notEquals=" + DEFAULT_FIPS);

        // Get all the countyList where fips not equals to UPDATED_FIPS
        defaultCountyShouldBeFound("fips.notEquals=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByFipsIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where fips in DEFAULT_FIPS or UPDATED_FIPS
        defaultCountyShouldBeFound("fips.in=" + DEFAULT_FIPS + "," + UPDATED_FIPS);

        // Get all the countyList where fips equals to UPDATED_FIPS
        defaultCountyShouldNotBeFound("fips.in=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByFipsIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where fips is not null
        defaultCountyShouldBeFound("fips.specified=true");

        // Get all the countyList where fips is null
        defaultCountyShouldNotBeFound("fips.specified=false");
    }

    @Test
    @Transactional
    void getAllCountiesByFipsContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where fips contains DEFAULT_FIPS
        defaultCountyShouldBeFound("fips.contains=" + DEFAULT_FIPS);

        // Get all the countyList where fips contains UPDATED_FIPS
        defaultCountyShouldNotBeFound("fips.contains=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByFipsNotContainsSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where fips does not contain DEFAULT_FIPS
        defaultCountyShouldNotBeFound("fips.doesNotContain=" + DEFAULT_FIPS);

        // Get all the countyList where fips does not contain UPDATED_FIPS
        defaultCountyShouldBeFound("fips.doesNotContain=" + UPDATED_FIPS);
    }

    @Test
    @Transactional
    void getAllCountiesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);
        State state;
        if (TestUtil.findAll(em, State.class).isEmpty()) {
            state = StateResourceIT.createEntity(em);
            em.persist(state);
            em.flush();
        } else {
            state = TestUtil.findAll(em, State.class).get(0);
        }
        em.persist(state);
        em.flush();
        county.setState(state);
        countyRepository.saveAndFlush(county);
        Long stateId = state.getId();

        // Get all the countyList where state equals to stateId
        defaultCountyShouldBeFound("stateId.equals=" + stateId);

        // Get all the countyList where state equals to (stateId + 1)
        defaultCountyShouldNotBeFound("stateId.equals=" + (stateId + 1));
    }

    @Test
    @Transactional
    void getAllCountiesByCountiesAvaiableIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);
        CountiesAvaiable countiesAvaiable;
        if (TestUtil.findAll(em, CountiesAvaiable.class).isEmpty()) {
            countiesAvaiable = CountiesAvaiableResourceIT.createEntity(em);
            em.persist(countiesAvaiable);
            em.flush();
        } else {
            countiesAvaiable = TestUtil.findAll(em, CountiesAvaiable.class).get(0);
        }
        em.persist(countiesAvaiable);
        em.flush();
        county.setCountiesAvaiable(countiesAvaiable);
        countiesAvaiable.setCounty(county);
        countyRepository.saveAndFlush(county);
        Long countiesAvaiableId = countiesAvaiable.getId();

        // Get all the countyList where countiesAvaiable equals to countiesAvaiableId
        defaultCountyShouldBeFound("countiesAvaiableId.equals=" + countiesAvaiableId);

        // Get all the countyList where countiesAvaiable equals to (countiesAvaiableId + 1)
        defaultCountyShouldNotBeFound("countiesAvaiableId.equals=" + (countiesAvaiableId + 1));
    }

    @Test
    @Transactional
    void getAllCountiesByCountyRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);
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
        county.addCountyRecord(countyRecord);
        countyRepository.saveAndFlush(county);
        Long countyRecordId = countyRecord.getId();

        // Get all the countyList where countyRecord equals to countyRecordId
        defaultCountyShouldBeFound("countyRecordId.equals=" + countyRecordId);

        // Get all the countyList where countyRecord equals to (countyRecordId + 1)
        defaultCountyShouldNotBeFound("countyRecordId.equals=" + (countyRecordId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountyShouldBeFound(String filter) throws Exception {
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(county.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].cntyFips").value(hasItem(DEFAULT_CNTY_FIPS)))
            .andExpect(jsonPath("$.[*].stateAbbr").value(hasItem(DEFAULT_STATE_ABBR)))
            .andExpect(jsonPath("$.[*].stFips").value(hasItem(DEFAULT_ST_FIPS)))
            .andExpect(jsonPath("$.[*].fips").value(hasItem(DEFAULT_FIPS)));

        // Check, that the count call also returns 1
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountyShouldNotBeFound(String filter) throws Exception {
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCounty() throws Exception {
        // Get the county
        restCountyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCounty() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        int databaseSizeBeforeUpdate = countyRepository.findAll().size();

        // Update the county
        County updatedCounty = countyRepository.findById(county.getId()).get();
        // Disconnect from session so that the updates on updatedCounty are not directly saved in db
        em.detach(updatedCounty);
        updatedCounty
            .countyName(UPDATED_COUNTY_NAME)
            .cntyFips(UPDATED_CNTY_FIPS)
            .stateAbbr(UPDATED_STATE_ABBR)
            .stFips(UPDATED_ST_FIPS)
            .fips(UPDATED_FIPS);

        restCountyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCounty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCounty))
            )
            .andExpect(status().isOk());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCounty.getCntyFips()).isEqualTo(UPDATED_CNTY_FIPS);
        assertThat(testCounty.getStateAbbr()).isEqualTo(UPDATED_STATE_ABBR);
        assertThat(testCounty.getStFips()).isEqualTo(UPDATED_ST_FIPS);
        assertThat(testCounty.getFips()).isEqualTo(UPDATED_FIPS);
    }

    @Test
    @Transactional
    void putNonExistingCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, county.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(county))
            )
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(county))
            )
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(county)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountyWithPatch() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        int databaseSizeBeforeUpdate = countyRepository.findAll().size();

        // Update the county using partial update
        County partialUpdatedCounty = new County();
        partialUpdatedCounty.setId(county.getId());

        partialUpdatedCounty.countyName(UPDATED_COUNTY_NAME).cntyFips(UPDATED_CNTY_FIPS);

        restCountyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounty))
            )
            .andExpect(status().isOk());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCounty.getCntyFips()).isEqualTo(UPDATED_CNTY_FIPS);
        assertThat(testCounty.getStateAbbr()).isEqualTo(DEFAULT_STATE_ABBR);
        assertThat(testCounty.getStFips()).isEqualTo(DEFAULT_ST_FIPS);
        assertThat(testCounty.getFips()).isEqualTo(DEFAULT_FIPS);
    }

    @Test
    @Transactional
    void fullUpdateCountyWithPatch() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        int databaseSizeBeforeUpdate = countyRepository.findAll().size();

        // Update the county using partial update
        County partialUpdatedCounty = new County();
        partialUpdatedCounty.setId(county.getId());

        partialUpdatedCounty
            .countyName(UPDATED_COUNTY_NAME)
            .cntyFips(UPDATED_CNTY_FIPS)
            .stateAbbr(UPDATED_STATE_ABBR)
            .stFips(UPDATED_ST_FIPS)
            .fips(UPDATED_FIPS);

        restCountyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCounty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCounty))
            )
            .andExpect(status().isOk());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCounty.getCntyFips()).isEqualTo(UPDATED_CNTY_FIPS);
        assertThat(testCounty.getStateAbbr()).isEqualTo(UPDATED_STATE_ABBR);
        assertThat(testCounty.getStFips()).isEqualTo(UPDATED_ST_FIPS);
        assertThat(testCounty.getFips()).isEqualTo(UPDATED_FIPS);
    }

    @Test
    @Transactional
    void patchNonExistingCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, county.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(county))
            )
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(county))
            )
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();
        county.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(county)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCounty() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        int databaseSizeBeforeDelete = countyRepository.findAll().size();

        // Delete the county
        restCountyMockMvc
            .perform(delete(ENTITY_API_URL_ID, county.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
