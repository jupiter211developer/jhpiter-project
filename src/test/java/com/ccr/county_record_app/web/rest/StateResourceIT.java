package com.ccr.county_record_app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ccr.county_record_app.IntegrationTest;
import com.ccr.county_record_app.domain.County;
import com.ccr.county_record_app.domain.State;
import com.ccr.county_record_app.repository.StateRepository;
import com.ccr.county_record_app.service.criteria.StateCriteria;
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
 * Integration tests for the {@link StateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StateResourceIT {

    private static final String DEFAULT_STATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_ABBR = "AA";
    private static final String UPDATED_STATE_ABBR = "BB";

    private static final String DEFAULT_SUB_REGION = "AAAAAAAAAA";
    private static final String UPDATED_SUB_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_ST_FIPS = "AA";
    private static final String UPDATED_ST_FIPS = "BB";

    private static final String ENTITY_API_URL = "/api/states";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStateMockMvc;

    private State state;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static State createEntity(EntityManager em) {
        State state = new State()
            .stateName(DEFAULT_STATE_NAME)
            .stateAbbr(DEFAULT_STATE_ABBR)
            .subRegion(DEFAULT_SUB_REGION)
            .stFips(DEFAULT_ST_FIPS);
        return state;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static State createUpdatedEntity(EntityManager em) {
        State state = new State()
            .stateName(UPDATED_STATE_NAME)
            .stateAbbr(UPDATED_STATE_ABBR)
            .subRegion(UPDATED_SUB_REGION)
            .stFips(UPDATED_ST_FIPS);
        return state;
    }

    @BeforeEach
    public void initTest() {
        state = createEntity(em);
    }

    @Test
    @Transactional
    void createState() throws Exception {
        int databaseSizeBeforeCreate = stateRepository.findAll().size();
        // Create the State
        restStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(state)))
            .andExpect(status().isCreated());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate + 1);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getStateName()).isEqualTo(DEFAULT_STATE_NAME);
        assertThat(testState.getStateAbbr()).isEqualTo(DEFAULT_STATE_ABBR);
        assertThat(testState.getSubRegion()).isEqualTo(DEFAULT_SUB_REGION);
        assertThat(testState.getStFips()).isEqualTo(DEFAULT_ST_FIPS);
    }

    @Test
    @Transactional
    void createStateWithExistingId() throws Exception {
        // Create the State with an existing ID
        state.setId(1L);

        int databaseSizeBeforeCreate = stateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(state)))
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStates() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList
        restStateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(state.getId().intValue())))
            .andExpect(jsonPath("$.[*].stateName").value(hasItem(DEFAULT_STATE_NAME)))
            .andExpect(jsonPath("$.[*].stateAbbr").value(hasItem(DEFAULT_STATE_ABBR)))
            .andExpect(jsonPath("$.[*].subRegion").value(hasItem(DEFAULT_SUB_REGION)))
            .andExpect(jsonPath("$.[*].stFips").value(hasItem(DEFAULT_ST_FIPS)));
    }

    @Test
    @Transactional
    void getState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get the state
        restStateMockMvc
            .perform(get(ENTITY_API_URL_ID, state.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(state.getId().intValue()))
            .andExpect(jsonPath("$.stateName").value(DEFAULT_STATE_NAME))
            .andExpect(jsonPath("$.stateAbbr").value(DEFAULT_STATE_ABBR))
            .andExpect(jsonPath("$.subRegion").value(DEFAULT_SUB_REGION))
            .andExpect(jsonPath("$.stFips").value(DEFAULT_ST_FIPS));
    }

    @Test
    @Transactional
    void getStatesByIdFiltering() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        Long id = state.getId();

        defaultStateShouldBeFound("id.equals=" + id);
        defaultStateShouldNotBeFound("id.notEquals=" + id);

        defaultStateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStateShouldNotBeFound("id.greaterThan=" + id);

        defaultStateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStatesByStateNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateName equals to DEFAULT_STATE_NAME
        defaultStateShouldBeFound("stateName.equals=" + DEFAULT_STATE_NAME);

        // Get all the stateList where stateName equals to UPDATED_STATE_NAME
        defaultStateShouldNotBeFound("stateName.equals=" + UPDATED_STATE_NAME);
    }

    @Test
    @Transactional
    void getAllStatesByStateNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateName not equals to DEFAULT_STATE_NAME
        defaultStateShouldNotBeFound("stateName.notEquals=" + DEFAULT_STATE_NAME);

        // Get all the stateList where stateName not equals to UPDATED_STATE_NAME
        defaultStateShouldBeFound("stateName.notEquals=" + UPDATED_STATE_NAME);
    }

    @Test
    @Transactional
    void getAllStatesByStateNameIsInShouldWork() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateName in DEFAULT_STATE_NAME or UPDATED_STATE_NAME
        defaultStateShouldBeFound("stateName.in=" + DEFAULT_STATE_NAME + "," + UPDATED_STATE_NAME);

        // Get all the stateList where stateName equals to UPDATED_STATE_NAME
        defaultStateShouldNotBeFound("stateName.in=" + UPDATED_STATE_NAME);
    }

    @Test
    @Transactional
    void getAllStatesByStateNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateName is not null
        defaultStateShouldBeFound("stateName.specified=true");

        // Get all the stateList where stateName is null
        defaultStateShouldNotBeFound("stateName.specified=false");
    }

    @Test
    @Transactional
    void getAllStatesByStateNameContainsSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateName contains DEFAULT_STATE_NAME
        defaultStateShouldBeFound("stateName.contains=" + DEFAULT_STATE_NAME);

        // Get all the stateList where stateName contains UPDATED_STATE_NAME
        defaultStateShouldNotBeFound("stateName.contains=" + UPDATED_STATE_NAME);
    }

    @Test
    @Transactional
    void getAllStatesByStateNameNotContainsSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateName does not contain DEFAULT_STATE_NAME
        defaultStateShouldNotBeFound("stateName.doesNotContain=" + DEFAULT_STATE_NAME);

        // Get all the stateList where stateName does not contain UPDATED_STATE_NAME
        defaultStateShouldBeFound("stateName.doesNotContain=" + UPDATED_STATE_NAME);
    }

    @Test
    @Transactional
    void getAllStatesByStateAbbrIsEqualToSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateAbbr equals to DEFAULT_STATE_ABBR
        defaultStateShouldBeFound("stateAbbr.equals=" + DEFAULT_STATE_ABBR);

        // Get all the stateList where stateAbbr equals to UPDATED_STATE_ABBR
        defaultStateShouldNotBeFound("stateAbbr.equals=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllStatesByStateAbbrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateAbbr not equals to DEFAULT_STATE_ABBR
        defaultStateShouldNotBeFound("stateAbbr.notEquals=" + DEFAULT_STATE_ABBR);

        // Get all the stateList where stateAbbr not equals to UPDATED_STATE_ABBR
        defaultStateShouldBeFound("stateAbbr.notEquals=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllStatesByStateAbbrIsInShouldWork() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateAbbr in DEFAULT_STATE_ABBR or UPDATED_STATE_ABBR
        defaultStateShouldBeFound("stateAbbr.in=" + DEFAULT_STATE_ABBR + "," + UPDATED_STATE_ABBR);

        // Get all the stateList where stateAbbr equals to UPDATED_STATE_ABBR
        defaultStateShouldNotBeFound("stateAbbr.in=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllStatesByStateAbbrIsNullOrNotNull() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateAbbr is not null
        defaultStateShouldBeFound("stateAbbr.specified=true");

        // Get all the stateList where stateAbbr is null
        defaultStateShouldNotBeFound("stateAbbr.specified=false");
    }

    @Test
    @Transactional
    void getAllStatesByStateAbbrContainsSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateAbbr contains DEFAULT_STATE_ABBR
        defaultStateShouldBeFound("stateAbbr.contains=" + DEFAULT_STATE_ABBR);

        // Get all the stateList where stateAbbr contains UPDATED_STATE_ABBR
        defaultStateShouldNotBeFound("stateAbbr.contains=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllStatesByStateAbbrNotContainsSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stateAbbr does not contain DEFAULT_STATE_ABBR
        defaultStateShouldNotBeFound("stateAbbr.doesNotContain=" + DEFAULT_STATE_ABBR);

        // Get all the stateList where stateAbbr does not contain UPDATED_STATE_ABBR
        defaultStateShouldBeFound("stateAbbr.doesNotContain=" + UPDATED_STATE_ABBR);
    }

    @Test
    @Transactional
    void getAllStatesBySubRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where subRegion equals to DEFAULT_SUB_REGION
        defaultStateShouldBeFound("subRegion.equals=" + DEFAULT_SUB_REGION);

        // Get all the stateList where subRegion equals to UPDATED_SUB_REGION
        defaultStateShouldNotBeFound("subRegion.equals=" + UPDATED_SUB_REGION);
    }

    @Test
    @Transactional
    void getAllStatesBySubRegionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where subRegion not equals to DEFAULT_SUB_REGION
        defaultStateShouldNotBeFound("subRegion.notEquals=" + DEFAULT_SUB_REGION);

        // Get all the stateList where subRegion not equals to UPDATED_SUB_REGION
        defaultStateShouldBeFound("subRegion.notEquals=" + UPDATED_SUB_REGION);
    }

    @Test
    @Transactional
    void getAllStatesBySubRegionIsInShouldWork() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where subRegion in DEFAULT_SUB_REGION or UPDATED_SUB_REGION
        defaultStateShouldBeFound("subRegion.in=" + DEFAULT_SUB_REGION + "," + UPDATED_SUB_REGION);

        // Get all the stateList where subRegion equals to UPDATED_SUB_REGION
        defaultStateShouldNotBeFound("subRegion.in=" + UPDATED_SUB_REGION);
    }

    @Test
    @Transactional
    void getAllStatesBySubRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where subRegion is not null
        defaultStateShouldBeFound("subRegion.specified=true");

        // Get all the stateList where subRegion is null
        defaultStateShouldNotBeFound("subRegion.specified=false");
    }

    @Test
    @Transactional
    void getAllStatesBySubRegionContainsSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where subRegion contains DEFAULT_SUB_REGION
        defaultStateShouldBeFound("subRegion.contains=" + DEFAULT_SUB_REGION);

        // Get all the stateList where subRegion contains UPDATED_SUB_REGION
        defaultStateShouldNotBeFound("subRegion.contains=" + UPDATED_SUB_REGION);
    }

    @Test
    @Transactional
    void getAllStatesBySubRegionNotContainsSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where subRegion does not contain DEFAULT_SUB_REGION
        defaultStateShouldNotBeFound("subRegion.doesNotContain=" + DEFAULT_SUB_REGION);

        // Get all the stateList where subRegion does not contain UPDATED_SUB_REGION
        defaultStateShouldBeFound("subRegion.doesNotContain=" + UPDATED_SUB_REGION);
    }

    @Test
    @Transactional
    void getAllStatesByStFipsIsEqualToSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stFips equals to DEFAULT_ST_FIPS
        defaultStateShouldBeFound("stFips.equals=" + DEFAULT_ST_FIPS);

        // Get all the stateList where stFips equals to UPDATED_ST_FIPS
        defaultStateShouldNotBeFound("stFips.equals=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllStatesByStFipsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stFips not equals to DEFAULT_ST_FIPS
        defaultStateShouldNotBeFound("stFips.notEquals=" + DEFAULT_ST_FIPS);

        // Get all the stateList where stFips not equals to UPDATED_ST_FIPS
        defaultStateShouldBeFound("stFips.notEquals=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllStatesByStFipsIsInShouldWork() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stFips in DEFAULT_ST_FIPS or UPDATED_ST_FIPS
        defaultStateShouldBeFound("stFips.in=" + DEFAULT_ST_FIPS + "," + UPDATED_ST_FIPS);

        // Get all the stateList where stFips equals to UPDATED_ST_FIPS
        defaultStateShouldNotBeFound("stFips.in=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllStatesByStFipsIsNullOrNotNull() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stFips is not null
        defaultStateShouldBeFound("stFips.specified=true");

        // Get all the stateList where stFips is null
        defaultStateShouldNotBeFound("stFips.specified=false");
    }

    @Test
    @Transactional
    void getAllStatesByStFipsContainsSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stFips contains DEFAULT_ST_FIPS
        defaultStateShouldBeFound("stFips.contains=" + DEFAULT_ST_FIPS);

        // Get all the stateList where stFips contains UPDATED_ST_FIPS
        defaultStateShouldNotBeFound("stFips.contains=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllStatesByStFipsNotContainsSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList where stFips does not contain DEFAULT_ST_FIPS
        defaultStateShouldNotBeFound("stFips.doesNotContain=" + DEFAULT_ST_FIPS);

        // Get all the stateList where stFips does not contain UPDATED_ST_FIPS
        defaultStateShouldBeFound("stFips.doesNotContain=" + UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void getAllStatesByCountyIsEqualToSomething() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);
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
        state.addCounty(county);
        stateRepository.saveAndFlush(state);
        Long countyId = county.getId();

        // Get all the stateList where county equals to countyId
        defaultStateShouldBeFound("countyId.equals=" + countyId);

        // Get all the stateList where county equals to (countyId + 1)
        defaultStateShouldNotBeFound("countyId.equals=" + (countyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStateShouldBeFound(String filter) throws Exception {
        restStateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(state.getId().intValue())))
            .andExpect(jsonPath("$.[*].stateName").value(hasItem(DEFAULT_STATE_NAME)))
            .andExpect(jsonPath("$.[*].stateAbbr").value(hasItem(DEFAULT_STATE_ABBR)))
            .andExpect(jsonPath("$.[*].subRegion").value(hasItem(DEFAULT_SUB_REGION)))
            .andExpect(jsonPath("$.[*].stFips").value(hasItem(DEFAULT_ST_FIPS)));

        // Check, that the count call also returns 1
        restStateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStateShouldNotBeFound(String filter) throws Exception {
        restStateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingState() throws Exception {
        // Get the state
        restStateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        int databaseSizeBeforeUpdate = stateRepository.findAll().size();

        // Update the state
        State updatedState = stateRepository.findById(state.getId()).get();
        // Disconnect from session so that the updates on updatedState are not directly saved in db
        em.detach(updatedState);
        updatedState.stateName(UPDATED_STATE_NAME).stateAbbr(UPDATED_STATE_ABBR).subRegion(UPDATED_SUB_REGION).stFips(UPDATED_ST_FIPS);

        restStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedState.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedState))
            )
            .andExpect(status().isOk());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getStateName()).isEqualTo(UPDATED_STATE_NAME);
        assertThat(testState.getStateAbbr()).isEqualTo(UPDATED_STATE_ABBR);
        assertThat(testState.getSubRegion()).isEqualTo(UPDATED_SUB_REGION);
        assertThat(testState.getStFips()).isEqualTo(UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void putNonExistingState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        state.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, state.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(state))
            )
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        state.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(state))
            )
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        state.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(state)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStateWithPatch() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        int databaseSizeBeforeUpdate = stateRepository.findAll().size();

        // Update the state using partial update
        State partialUpdatedState = new State();
        partialUpdatedState.setId(state.getId());

        partialUpdatedState.stateName(UPDATED_STATE_NAME).stateAbbr(UPDATED_STATE_ABBR);

        restStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedState))
            )
            .andExpect(status().isOk());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getStateName()).isEqualTo(UPDATED_STATE_NAME);
        assertThat(testState.getStateAbbr()).isEqualTo(UPDATED_STATE_ABBR);
        assertThat(testState.getSubRegion()).isEqualTo(DEFAULT_SUB_REGION);
        assertThat(testState.getStFips()).isEqualTo(DEFAULT_ST_FIPS);
    }

    @Test
    @Transactional
    void fullUpdateStateWithPatch() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        int databaseSizeBeforeUpdate = stateRepository.findAll().size();

        // Update the state using partial update
        State partialUpdatedState = new State();
        partialUpdatedState.setId(state.getId());

        partialUpdatedState
            .stateName(UPDATED_STATE_NAME)
            .stateAbbr(UPDATED_STATE_ABBR)
            .subRegion(UPDATED_SUB_REGION)
            .stFips(UPDATED_ST_FIPS);

        restStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedState))
            )
            .andExpect(status().isOk());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getStateName()).isEqualTo(UPDATED_STATE_NAME);
        assertThat(testState.getStateAbbr()).isEqualTo(UPDATED_STATE_ABBR);
        assertThat(testState.getSubRegion()).isEqualTo(UPDATED_SUB_REGION);
        assertThat(testState.getStFips()).isEqualTo(UPDATED_ST_FIPS);
    }

    @Test
    @Transactional
    void patchNonExistingState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        state.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, state.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(state))
            )
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        state.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(state))
            )
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        state.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(state)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        int databaseSizeBeforeDelete = stateRepository.findAll().size();

        // Delete the state
        restStateMockMvc
            .perform(delete(ENTITY_API_URL_ID, state.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
