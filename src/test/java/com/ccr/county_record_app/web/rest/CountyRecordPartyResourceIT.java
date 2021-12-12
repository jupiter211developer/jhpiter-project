package com.ccr.county_record_app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ccr.county_record_app.IntegrationTest;
import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.domain.CountyRecordParty;
import com.ccr.county_record_app.repository.CountyRecordPartyRepository;
import com.ccr.county_record_app.service.criteria.CountyRecordPartyCriteria;
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
 * Integration tests for the {@link CountyRecordPartyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountyRecordPartyResourceIT {

    private static final String DEFAULT_RECORD_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PARTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARTY_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PARTY_ROLE = 1;
    private static final Integer UPDATED_PARTY_ROLE = 2;
    private static final Integer SMALLER_PARTY_ROLE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/county-record-parties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountyRecordPartyRepository countyRecordPartyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountyRecordPartyMockMvc;

    private CountyRecordParty countyRecordParty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyRecordParty createEntity(EntityManager em) {
        CountyRecordParty countyRecordParty = new CountyRecordParty()
            .recordKey(DEFAULT_RECORD_KEY)
            .partyName(DEFAULT_PARTY_NAME)
            .partyRole(DEFAULT_PARTY_ROLE);
        return countyRecordParty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyRecordParty createUpdatedEntity(EntityManager em) {
        CountyRecordParty countyRecordParty = new CountyRecordParty()
            .recordKey(UPDATED_RECORD_KEY)
            .partyName(UPDATED_PARTY_NAME)
            .partyRole(UPDATED_PARTY_ROLE);
        return countyRecordParty;
    }

    @BeforeEach
    public void initTest() {
        countyRecordParty = createEntity(em);
    }

    @Test
    @Transactional
    void createCountyRecordParty() throws Exception {
        int databaseSizeBeforeCreate = countyRecordPartyRepository.findAll().size();
        // Create the CountyRecordParty
        restCountyRecordPartyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecordParty))
            )
            .andExpect(status().isCreated());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeCreate + 1);
        CountyRecordParty testCountyRecordParty = countyRecordPartyList.get(countyRecordPartyList.size() - 1);
        assertThat(testCountyRecordParty.getRecordKey()).isEqualTo(DEFAULT_RECORD_KEY);
        assertThat(testCountyRecordParty.getPartyName()).isEqualTo(DEFAULT_PARTY_NAME);
        assertThat(testCountyRecordParty.getPartyRole()).isEqualTo(DEFAULT_PARTY_ROLE);
    }

    @Test
    @Transactional
    void createCountyRecordPartyWithExistingId() throws Exception {
        // Create the CountyRecordParty with an existing ID
        countyRecordParty.setId(1L);

        int databaseSizeBeforeCreate = countyRecordPartyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountyRecordPartyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecordParty))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecordKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyRecordPartyRepository.findAll().size();
        // set the field null
        countyRecordParty.setRecordKey(null);

        // Create the CountyRecordParty, which fails.

        restCountyRecordPartyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecordParty))
            )
            .andExpect(status().isBadRequest());

        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountyRecordParties() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList
        restCountyRecordPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyRecordParty.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)))
            .andExpect(jsonPath("$.[*].partyName").value(hasItem(DEFAULT_PARTY_NAME)))
            .andExpect(jsonPath("$.[*].partyRole").value(hasItem(DEFAULT_PARTY_ROLE)));
    }

    @Test
    @Transactional
    void getCountyRecordParty() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get the countyRecordParty
        restCountyRecordPartyMockMvc
            .perform(get(ENTITY_API_URL_ID, countyRecordParty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countyRecordParty.getId().intValue()))
            .andExpect(jsonPath("$.recordKey").value(DEFAULT_RECORD_KEY))
            .andExpect(jsonPath("$.partyName").value(DEFAULT_PARTY_NAME))
            .andExpect(jsonPath("$.partyRole").value(DEFAULT_PARTY_ROLE));
    }

    @Test
    @Transactional
    void getCountyRecordPartiesByIdFiltering() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        Long id = countyRecordParty.getId();

        defaultCountyRecordPartyShouldBeFound("id.equals=" + id);
        defaultCountyRecordPartyShouldNotBeFound("id.notEquals=" + id);

        defaultCountyRecordPartyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountyRecordPartyShouldNotBeFound("id.greaterThan=" + id);

        defaultCountyRecordPartyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountyRecordPartyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByRecordKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where recordKey equals to DEFAULT_RECORD_KEY
        defaultCountyRecordPartyShouldBeFound("recordKey.equals=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordPartyList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyRecordPartyShouldNotBeFound("recordKey.equals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByRecordKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where recordKey not equals to DEFAULT_RECORD_KEY
        defaultCountyRecordPartyShouldNotBeFound("recordKey.notEquals=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordPartyList where recordKey not equals to UPDATED_RECORD_KEY
        defaultCountyRecordPartyShouldBeFound("recordKey.notEquals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByRecordKeyIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where recordKey in DEFAULT_RECORD_KEY or UPDATED_RECORD_KEY
        defaultCountyRecordPartyShouldBeFound("recordKey.in=" + DEFAULT_RECORD_KEY + "," + UPDATED_RECORD_KEY);

        // Get all the countyRecordPartyList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyRecordPartyShouldNotBeFound("recordKey.in=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByRecordKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where recordKey is not null
        defaultCountyRecordPartyShouldBeFound("recordKey.specified=true");

        // Get all the countyRecordPartyList where recordKey is null
        defaultCountyRecordPartyShouldNotBeFound("recordKey.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByRecordKeyContainsSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where recordKey contains DEFAULT_RECORD_KEY
        defaultCountyRecordPartyShouldBeFound("recordKey.contains=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordPartyList where recordKey contains UPDATED_RECORD_KEY
        defaultCountyRecordPartyShouldNotBeFound("recordKey.contains=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByRecordKeyNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where recordKey does not contain DEFAULT_RECORD_KEY
        defaultCountyRecordPartyShouldNotBeFound("recordKey.doesNotContain=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordPartyList where recordKey does not contain UPDATED_RECORD_KEY
        defaultCountyRecordPartyShouldBeFound("recordKey.doesNotContain=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyName equals to DEFAULT_PARTY_NAME
        defaultCountyRecordPartyShouldBeFound("partyName.equals=" + DEFAULT_PARTY_NAME);

        // Get all the countyRecordPartyList where partyName equals to UPDATED_PARTY_NAME
        defaultCountyRecordPartyShouldNotBeFound("partyName.equals=" + UPDATED_PARTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyName not equals to DEFAULT_PARTY_NAME
        defaultCountyRecordPartyShouldNotBeFound("partyName.notEquals=" + DEFAULT_PARTY_NAME);

        // Get all the countyRecordPartyList where partyName not equals to UPDATED_PARTY_NAME
        defaultCountyRecordPartyShouldBeFound("partyName.notEquals=" + UPDATED_PARTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyNameIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyName in DEFAULT_PARTY_NAME or UPDATED_PARTY_NAME
        defaultCountyRecordPartyShouldBeFound("partyName.in=" + DEFAULT_PARTY_NAME + "," + UPDATED_PARTY_NAME);

        // Get all the countyRecordPartyList where partyName equals to UPDATED_PARTY_NAME
        defaultCountyRecordPartyShouldNotBeFound("partyName.in=" + UPDATED_PARTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyName is not null
        defaultCountyRecordPartyShouldBeFound("partyName.specified=true");

        // Get all the countyRecordPartyList where partyName is null
        defaultCountyRecordPartyShouldNotBeFound("partyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyNameContainsSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyName contains DEFAULT_PARTY_NAME
        defaultCountyRecordPartyShouldBeFound("partyName.contains=" + DEFAULT_PARTY_NAME);

        // Get all the countyRecordPartyList where partyName contains UPDATED_PARTY_NAME
        defaultCountyRecordPartyShouldNotBeFound("partyName.contains=" + UPDATED_PARTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyNameNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyName does not contain DEFAULT_PARTY_NAME
        defaultCountyRecordPartyShouldNotBeFound("partyName.doesNotContain=" + DEFAULT_PARTY_NAME);

        // Get all the countyRecordPartyList where partyName does not contain UPDATED_PARTY_NAME
        defaultCountyRecordPartyShouldBeFound("partyName.doesNotContain=" + UPDATED_PARTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyRole equals to DEFAULT_PARTY_ROLE
        defaultCountyRecordPartyShouldBeFound("partyRole.equals=" + DEFAULT_PARTY_ROLE);

        // Get all the countyRecordPartyList where partyRole equals to UPDATED_PARTY_ROLE
        defaultCountyRecordPartyShouldNotBeFound("partyRole.equals=" + UPDATED_PARTY_ROLE);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyRoleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyRole not equals to DEFAULT_PARTY_ROLE
        defaultCountyRecordPartyShouldNotBeFound("partyRole.notEquals=" + DEFAULT_PARTY_ROLE);

        // Get all the countyRecordPartyList where partyRole not equals to UPDATED_PARTY_ROLE
        defaultCountyRecordPartyShouldBeFound("partyRole.notEquals=" + UPDATED_PARTY_ROLE);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyRoleIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyRole in DEFAULT_PARTY_ROLE or UPDATED_PARTY_ROLE
        defaultCountyRecordPartyShouldBeFound("partyRole.in=" + DEFAULT_PARTY_ROLE + "," + UPDATED_PARTY_ROLE);

        // Get all the countyRecordPartyList where partyRole equals to UPDATED_PARTY_ROLE
        defaultCountyRecordPartyShouldNotBeFound("partyRole.in=" + UPDATED_PARTY_ROLE);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyRole is not null
        defaultCountyRecordPartyShouldBeFound("partyRole.specified=true");

        // Get all the countyRecordPartyList where partyRole is null
        defaultCountyRecordPartyShouldNotBeFound("partyRole.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyRoleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyRole is greater than or equal to DEFAULT_PARTY_ROLE
        defaultCountyRecordPartyShouldBeFound("partyRole.greaterThanOrEqual=" + DEFAULT_PARTY_ROLE);

        // Get all the countyRecordPartyList where partyRole is greater than or equal to UPDATED_PARTY_ROLE
        defaultCountyRecordPartyShouldNotBeFound("partyRole.greaterThanOrEqual=" + UPDATED_PARTY_ROLE);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyRoleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyRole is less than or equal to DEFAULT_PARTY_ROLE
        defaultCountyRecordPartyShouldBeFound("partyRole.lessThanOrEqual=" + DEFAULT_PARTY_ROLE);

        // Get all the countyRecordPartyList where partyRole is less than or equal to SMALLER_PARTY_ROLE
        defaultCountyRecordPartyShouldNotBeFound("partyRole.lessThanOrEqual=" + SMALLER_PARTY_ROLE);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyRoleIsLessThanSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyRole is less than DEFAULT_PARTY_ROLE
        defaultCountyRecordPartyShouldNotBeFound("partyRole.lessThan=" + DEFAULT_PARTY_ROLE);

        // Get all the countyRecordPartyList where partyRole is less than UPDATED_PARTY_ROLE
        defaultCountyRecordPartyShouldBeFound("partyRole.lessThan=" + UPDATED_PARTY_ROLE);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByPartyRoleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        // Get all the countyRecordPartyList where partyRole is greater than DEFAULT_PARTY_ROLE
        defaultCountyRecordPartyShouldNotBeFound("partyRole.greaterThan=" + DEFAULT_PARTY_ROLE);

        // Get all the countyRecordPartyList where partyRole is greater than SMALLER_PARTY_ROLE
        defaultCountyRecordPartyShouldBeFound("partyRole.greaterThan=" + SMALLER_PARTY_ROLE);
    }

    @Test
    @Transactional
    void getAllCountyRecordPartiesByCountyRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);
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
        countyRecordParty.setCountyRecord(countyRecord);
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);
        Long countyRecordId = countyRecord.getId();

        // Get all the countyRecordPartyList where countyRecord equals to countyRecordId
        defaultCountyRecordPartyShouldBeFound("countyRecordId.equals=" + countyRecordId);

        // Get all the countyRecordPartyList where countyRecord equals to (countyRecordId + 1)
        defaultCountyRecordPartyShouldNotBeFound("countyRecordId.equals=" + (countyRecordId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountyRecordPartyShouldBeFound(String filter) throws Exception {
        restCountyRecordPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyRecordParty.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)))
            .andExpect(jsonPath("$.[*].partyName").value(hasItem(DEFAULT_PARTY_NAME)))
            .andExpect(jsonPath("$.[*].partyRole").value(hasItem(DEFAULT_PARTY_ROLE)));

        // Check, that the count call also returns 1
        restCountyRecordPartyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountyRecordPartyShouldNotBeFound(String filter) throws Exception {
        restCountyRecordPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountyRecordPartyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountyRecordParty() throws Exception {
        // Get the countyRecordParty
        restCountyRecordPartyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountyRecordParty() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        int databaseSizeBeforeUpdate = countyRecordPartyRepository.findAll().size();

        // Update the countyRecordParty
        CountyRecordParty updatedCountyRecordParty = countyRecordPartyRepository.findById(countyRecordParty.getId()).get();
        // Disconnect from session so that the updates on updatedCountyRecordParty are not directly saved in db
        em.detach(updatedCountyRecordParty);
        updatedCountyRecordParty.recordKey(UPDATED_RECORD_KEY).partyName(UPDATED_PARTY_NAME).partyRole(UPDATED_PARTY_ROLE);

        restCountyRecordPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCountyRecordParty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCountyRecordParty))
            )
            .andExpect(status().isOk());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeUpdate);
        CountyRecordParty testCountyRecordParty = countyRecordPartyList.get(countyRecordPartyList.size() - 1);
        assertThat(testCountyRecordParty.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyRecordParty.getPartyName()).isEqualTo(UPDATED_PARTY_NAME);
        assertThat(testCountyRecordParty.getPartyRole()).isEqualTo(UPDATED_PARTY_ROLE);
    }

    @Test
    @Transactional
    void putNonExistingCountyRecordParty() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordPartyRepository.findAll().size();
        countyRecordParty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyRecordPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countyRecordParty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordParty))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountyRecordParty() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordPartyRepository.findAll().size();
        countyRecordParty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordParty))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountyRecordParty() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordPartyRepository.findAll().size();
        countyRecordParty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordPartyMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecordParty))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountyRecordPartyWithPatch() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        int databaseSizeBeforeUpdate = countyRecordPartyRepository.findAll().size();

        // Update the countyRecordParty using partial update
        CountyRecordParty partialUpdatedCountyRecordParty = new CountyRecordParty();
        partialUpdatedCountyRecordParty.setId(countyRecordParty.getId());

        partialUpdatedCountyRecordParty.recordKey(UPDATED_RECORD_KEY);

        restCountyRecordPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyRecordParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyRecordParty))
            )
            .andExpect(status().isOk());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeUpdate);
        CountyRecordParty testCountyRecordParty = countyRecordPartyList.get(countyRecordPartyList.size() - 1);
        assertThat(testCountyRecordParty.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyRecordParty.getPartyName()).isEqualTo(DEFAULT_PARTY_NAME);
        assertThat(testCountyRecordParty.getPartyRole()).isEqualTo(DEFAULT_PARTY_ROLE);
    }

    @Test
    @Transactional
    void fullUpdateCountyRecordPartyWithPatch() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        int databaseSizeBeforeUpdate = countyRecordPartyRepository.findAll().size();

        // Update the countyRecordParty using partial update
        CountyRecordParty partialUpdatedCountyRecordParty = new CountyRecordParty();
        partialUpdatedCountyRecordParty.setId(countyRecordParty.getId());

        partialUpdatedCountyRecordParty.recordKey(UPDATED_RECORD_KEY).partyName(UPDATED_PARTY_NAME).partyRole(UPDATED_PARTY_ROLE);

        restCountyRecordPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyRecordParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyRecordParty))
            )
            .andExpect(status().isOk());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeUpdate);
        CountyRecordParty testCountyRecordParty = countyRecordPartyList.get(countyRecordPartyList.size() - 1);
        assertThat(testCountyRecordParty.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
        assertThat(testCountyRecordParty.getPartyName()).isEqualTo(UPDATED_PARTY_NAME);
        assertThat(testCountyRecordParty.getPartyRole()).isEqualTo(UPDATED_PARTY_ROLE);
    }

    @Test
    @Transactional
    void patchNonExistingCountyRecordParty() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordPartyRepository.findAll().size();
        countyRecordParty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyRecordPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countyRecordParty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordParty))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountyRecordParty() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordPartyRepository.findAll().size();
        countyRecordParty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordParty))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountyRecordParty() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordPartyRepository.findAll().size();
        countyRecordParty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordPartyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordParty))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyRecordParty in the database
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountyRecordParty() throws Exception {
        // Initialize the database
        countyRecordPartyRepository.saveAndFlush(countyRecordParty);

        int databaseSizeBeforeDelete = countyRecordPartyRepository.findAll().size();

        // Delete the countyRecordParty
        restCountyRecordPartyMockMvc
            .perform(delete(ENTITY_API_URL_ID, countyRecordParty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CountyRecordParty> countyRecordPartyList = countyRecordPartyRepository.findAll();
        assertThat(countyRecordPartyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
