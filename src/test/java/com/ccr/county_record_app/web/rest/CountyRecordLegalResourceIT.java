package com.ccr.county_record_app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ccr.county_record_app.IntegrationTest;
import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.domain.CountyRecordLegal;
import com.ccr.county_record_app.repository.CountyRecordLegalRepository;
import com.ccr.county_record_app.service.criteria.CountyRecordLegalCriteria;
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
 * Integration tests for the {@link CountyRecordLegalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountyRecordLegalResourceIT {

    private static final String DEFAULT_LEGAL = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL = "BBBBBBBBBB";

    private static final String DEFAULT_RECORD_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_KEY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/county-record-legals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountyRecordLegalRepository countyRecordLegalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountyRecordLegalMockMvc;

    private CountyRecordLegal countyRecordLegal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyRecordLegal createEntity(EntityManager em) {
        CountyRecordLegal countyRecordLegal = new CountyRecordLegal().legal(DEFAULT_LEGAL).recordKey(DEFAULT_RECORD_KEY);
        return countyRecordLegal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyRecordLegal createUpdatedEntity(EntityManager em) {
        CountyRecordLegal countyRecordLegal = new CountyRecordLegal().legal(UPDATED_LEGAL).recordKey(UPDATED_RECORD_KEY);
        return countyRecordLegal;
    }

    @BeforeEach
    public void initTest() {
        countyRecordLegal = createEntity(em);
    }

    @Test
    @Transactional
    void createCountyRecordLegal() throws Exception {
        int databaseSizeBeforeCreate = countyRecordLegalRepository.findAll().size();
        // Create the CountyRecordLegal
        restCountyRecordLegalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecordLegal))
            )
            .andExpect(status().isCreated());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeCreate + 1);
        CountyRecordLegal testCountyRecordLegal = countyRecordLegalList.get(countyRecordLegalList.size() - 1);
        assertThat(testCountyRecordLegal.getLegal()).isEqualTo(DEFAULT_LEGAL);
        assertThat(testCountyRecordLegal.getRecordKey()).isEqualTo(DEFAULT_RECORD_KEY);
    }

    @Test
    @Transactional
    void createCountyRecordLegalWithExistingId() throws Exception {
        // Create the CountyRecordLegal with an existing ID
        countyRecordLegal.setId(1L);

        int databaseSizeBeforeCreate = countyRecordLegalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountyRecordLegalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecordLegal))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecordKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyRecordLegalRepository.findAll().size();
        // set the field null
        countyRecordLegal.setRecordKey(null);

        // Create the CountyRecordLegal, which fails.

        restCountyRecordLegalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecordLegal))
            )
            .andExpect(status().isBadRequest());

        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegals() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList
        restCountyRecordLegalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyRecordLegal.getId().intValue())))
            .andExpect(jsonPath("$.[*].legal").value(hasItem(DEFAULT_LEGAL)))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)));
    }

    @Test
    @Transactional
    void getCountyRecordLegal() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get the countyRecordLegal
        restCountyRecordLegalMockMvc
            .perform(get(ENTITY_API_URL_ID, countyRecordLegal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countyRecordLegal.getId().intValue()))
            .andExpect(jsonPath("$.legal").value(DEFAULT_LEGAL))
            .andExpect(jsonPath("$.recordKey").value(DEFAULT_RECORD_KEY));
    }

    @Test
    @Transactional
    void getCountyRecordLegalsByIdFiltering() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        Long id = countyRecordLegal.getId();

        defaultCountyRecordLegalShouldBeFound("id.equals=" + id);
        defaultCountyRecordLegalShouldNotBeFound("id.notEquals=" + id);

        defaultCountyRecordLegalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountyRecordLegalShouldNotBeFound("id.greaterThan=" + id);

        defaultCountyRecordLegalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountyRecordLegalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByLegalIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where legal equals to DEFAULT_LEGAL
        defaultCountyRecordLegalShouldBeFound("legal.equals=" + DEFAULT_LEGAL);

        // Get all the countyRecordLegalList where legal equals to UPDATED_LEGAL
        defaultCountyRecordLegalShouldNotBeFound("legal.equals=" + UPDATED_LEGAL);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByLegalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where legal not equals to DEFAULT_LEGAL
        defaultCountyRecordLegalShouldNotBeFound("legal.notEquals=" + DEFAULT_LEGAL);

        // Get all the countyRecordLegalList where legal not equals to UPDATED_LEGAL
        defaultCountyRecordLegalShouldBeFound("legal.notEquals=" + UPDATED_LEGAL);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByLegalIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where legal in DEFAULT_LEGAL or UPDATED_LEGAL
        defaultCountyRecordLegalShouldBeFound("legal.in=" + DEFAULT_LEGAL + "," + UPDATED_LEGAL);

        // Get all the countyRecordLegalList where legal equals to UPDATED_LEGAL
        defaultCountyRecordLegalShouldNotBeFound("legal.in=" + UPDATED_LEGAL);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByLegalIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where legal is not null
        defaultCountyRecordLegalShouldBeFound("legal.specified=true");

        // Get all the countyRecordLegalList where legal is null
        defaultCountyRecordLegalShouldNotBeFound("legal.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByLegalContainsSomething() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where legal contains DEFAULT_LEGAL
        defaultCountyRecordLegalShouldBeFound("legal.contains=" + DEFAULT_LEGAL);

        // Get all the countyRecordLegalList where legal contains UPDATED_LEGAL
        defaultCountyRecordLegalShouldNotBeFound("legal.contains=" + UPDATED_LEGAL);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByLegalNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where legal does not contain DEFAULT_LEGAL
        defaultCountyRecordLegalShouldNotBeFound("legal.doesNotContain=" + DEFAULT_LEGAL);

        // Get all the countyRecordLegalList where legal does not contain UPDATED_LEGAL
        defaultCountyRecordLegalShouldBeFound("legal.doesNotContain=" + UPDATED_LEGAL);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByRecordKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where recordKey equals to DEFAULT_RECORD_KEY
        defaultCountyRecordLegalShouldBeFound("recordKey.equals=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordLegalList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyRecordLegalShouldNotBeFound("recordKey.equals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByRecordKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where recordKey not equals to DEFAULT_RECORD_KEY
        defaultCountyRecordLegalShouldNotBeFound("recordKey.notEquals=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordLegalList where recordKey not equals to UPDATED_RECORD_KEY
        defaultCountyRecordLegalShouldBeFound("recordKey.notEquals=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByRecordKeyIsInShouldWork() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where recordKey in DEFAULT_RECORD_KEY or UPDATED_RECORD_KEY
        defaultCountyRecordLegalShouldBeFound("recordKey.in=" + DEFAULT_RECORD_KEY + "," + UPDATED_RECORD_KEY);

        // Get all the countyRecordLegalList where recordKey equals to UPDATED_RECORD_KEY
        defaultCountyRecordLegalShouldNotBeFound("recordKey.in=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByRecordKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where recordKey is not null
        defaultCountyRecordLegalShouldBeFound("recordKey.specified=true");

        // Get all the countyRecordLegalList where recordKey is null
        defaultCountyRecordLegalShouldNotBeFound("recordKey.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByRecordKeyContainsSomething() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where recordKey contains DEFAULT_RECORD_KEY
        defaultCountyRecordLegalShouldBeFound("recordKey.contains=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordLegalList where recordKey contains UPDATED_RECORD_KEY
        defaultCountyRecordLegalShouldNotBeFound("recordKey.contains=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByRecordKeyNotContainsSomething() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        // Get all the countyRecordLegalList where recordKey does not contain DEFAULT_RECORD_KEY
        defaultCountyRecordLegalShouldNotBeFound("recordKey.doesNotContain=" + DEFAULT_RECORD_KEY);

        // Get all the countyRecordLegalList where recordKey does not contain UPDATED_RECORD_KEY
        defaultCountyRecordLegalShouldBeFound("recordKey.doesNotContain=" + UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void getAllCountyRecordLegalsByCountyRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);
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
        countyRecordLegal.setCountyRecord(countyRecord);
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);
        Long countyRecordId = countyRecord.getId();

        // Get all the countyRecordLegalList where countyRecord equals to countyRecordId
        defaultCountyRecordLegalShouldBeFound("countyRecordId.equals=" + countyRecordId);

        // Get all the countyRecordLegalList where countyRecord equals to (countyRecordId + 1)
        defaultCountyRecordLegalShouldNotBeFound("countyRecordId.equals=" + (countyRecordId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountyRecordLegalShouldBeFound(String filter) throws Exception {
        restCountyRecordLegalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyRecordLegal.getId().intValue())))
            .andExpect(jsonPath("$.[*].legal").value(hasItem(DEFAULT_LEGAL)))
            .andExpect(jsonPath("$.[*].recordKey").value(hasItem(DEFAULT_RECORD_KEY)));

        // Check, that the count call also returns 1
        restCountyRecordLegalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountyRecordLegalShouldNotBeFound(String filter) throws Exception {
        restCountyRecordLegalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountyRecordLegalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountyRecordLegal() throws Exception {
        // Get the countyRecordLegal
        restCountyRecordLegalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountyRecordLegal() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        int databaseSizeBeforeUpdate = countyRecordLegalRepository.findAll().size();

        // Update the countyRecordLegal
        CountyRecordLegal updatedCountyRecordLegal = countyRecordLegalRepository.findById(countyRecordLegal.getId()).get();
        // Disconnect from session so that the updates on updatedCountyRecordLegal are not directly saved in db
        em.detach(updatedCountyRecordLegal);
        updatedCountyRecordLegal.legal(UPDATED_LEGAL).recordKey(UPDATED_RECORD_KEY);

        restCountyRecordLegalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCountyRecordLegal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCountyRecordLegal))
            )
            .andExpect(status().isOk());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeUpdate);
        CountyRecordLegal testCountyRecordLegal = countyRecordLegalList.get(countyRecordLegalList.size() - 1);
        assertThat(testCountyRecordLegal.getLegal()).isEqualTo(UPDATED_LEGAL);
        assertThat(testCountyRecordLegal.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void putNonExistingCountyRecordLegal() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordLegalRepository.findAll().size();
        countyRecordLegal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyRecordLegalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countyRecordLegal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordLegal))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountyRecordLegal() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordLegalRepository.findAll().size();
        countyRecordLegal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordLegalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordLegal))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountyRecordLegal() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordLegalRepository.findAll().size();
        countyRecordLegal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordLegalMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyRecordLegal))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountyRecordLegalWithPatch() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        int databaseSizeBeforeUpdate = countyRecordLegalRepository.findAll().size();

        // Update the countyRecordLegal using partial update
        CountyRecordLegal partialUpdatedCountyRecordLegal = new CountyRecordLegal();
        partialUpdatedCountyRecordLegal.setId(countyRecordLegal.getId());

        partialUpdatedCountyRecordLegal.legal(UPDATED_LEGAL);

        restCountyRecordLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyRecordLegal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyRecordLegal))
            )
            .andExpect(status().isOk());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeUpdate);
        CountyRecordLegal testCountyRecordLegal = countyRecordLegalList.get(countyRecordLegalList.size() - 1);
        assertThat(testCountyRecordLegal.getLegal()).isEqualTo(UPDATED_LEGAL);
        assertThat(testCountyRecordLegal.getRecordKey()).isEqualTo(DEFAULT_RECORD_KEY);
    }

    @Test
    @Transactional
    void fullUpdateCountyRecordLegalWithPatch() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        int databaseSizeBeforeUpdate = countyRecordLegalRepository.findAll().size();

        // Update the countyRecordLegal using partial update
        CountyRecordLegal partialUpdatedCountyRecordLegal = new CountyRecordLegal();
        partialUpdatedCountyRecordLegal.setId(countyRecordLegal.getId());

        partialUpdatedCountyRecordLegal.legal(UPDATED_LEGAL).recordKey(UPDATED_RECORD_KEY);

        restCountyRecordLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyRecordLegal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyRecordLegal))
            )
            .andExpect(status().isOk());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeUpdate);
        CountyRecordLegal testCountyRecordLegal = countyRecordLegalList.get(countyRecordLegalList.size() - 1);
        assertThat(testCountyRecordLegal.getLegal()).isEqualTo(UPDATED_LEGAL);
        assertThat(testCountyRecordLegal.getRecordKey()).isEqualTo(UPDATED_RECORD_KEY);
    }

    @Test
    @Transactional
    void patchNonExistingCountyRecordLegal() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordLegalRepository.findAll().size();
        countyRecordLegal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyRecordLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countyRecordLegal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordLegal))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountyRecordLegal() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordLegalRepository.findAll().size();
        countyRecordLegal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordLegal))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountyRecordLegal() throws Exception {
        int databaseSizeBeforeUpdate = countyRecordLegalRepository.findAll().size();
        countyRecordLegal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyRecordLegalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyRecordLegal))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyRecordLegal in the database
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountyRecordLegal() throws Exception {
        // Initialize the database
        countyRecordLegalRepository.saveAndFlush(countyRecordLegal);

        int databaseSizeBeforeDelete = countyRecordLegalRepository.findAll().size();

        // Delete the countyRecordLegal
        restCountyRecordLegalMockMvc
            .perform(delete(ENTITY_API_URL_ID, countyRecordLegal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CountyRecordLegal> countyRecordLegalList = countyRecordLegalRepository.findAll();
        assertThat(countyRecordLegalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
