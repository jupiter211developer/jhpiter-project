package com.ccr.county_record_app.web.rest;

import com.ccr.county_record_app.domain.State;
import com.ccr.county_record_app.repository.StateRepository;
import com.ccr.county_record_app.service.StateQueryService;
import com.ccr.county_record_app.service.StateService;
import com.ccr.county_record_app.service.criteria.StateCriteria;
import com.ccr.county_record_app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ccr.county_record_app.domain.State}.
 */
@RestController
@RequestMapping("/api")
public class StateResource {

    private final Logger log = LoggerFactory.getLogger(StateResource.class);

    private static final String ENTITY_NAME = "state";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StateService stateService;

    private final StateRepository stateRepository;

    private final StateQueryService stateQueryService;

    public StateResource(StateService stateService, StateRepository stateRepository, StateQueryService stateQueryService) {
        this.stateService = stateService;
        this.stateRepository = stateRepository;
        this.stateQueryService = stateQueryService;
    }

    /**
     * {@code POST  /states} : Create a new state.
     *
     * @param state the state to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new state, or with status {@code 400 (Bad Request)} if the state has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/states")
    public ResponseEntity<State> createState(@Valid @RequestBody State state) throws URISyntaxException {
        log.debug("REST request to save State : {}", state);
        if (state.getId() != null) {
            throw new BadRequestAlertException("A new state cannot already have an ID", ENTITY_NAME, "idexists");
        }
        State result = stateService.save(state);
        return ResponseEntity
            .created(new URI("/api/states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /states/:id} : Updates an existing state.
     *
     * @param id the id of the state to save.
     * @param state the state to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated state,
     * or with status {@code 400 (Bad Request)} if the state is not valid,
     * or with status {@code 500 (Internal Server Error)} if the state couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/states/{id}")
    public ResponseEntity<State> updateState(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody State state)
        throws URISyntaxException {
        log.debug("REST request to update State : {}, {}", id, state);
        if (state.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, state.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        State result = stateService.save(state);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, state.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /states/:id} : Partial updates given fields of an existing state, field will ignore if it is null
     *
     * @param id the id of the state to save.
     * @param state the state to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated state,
     * or with status {@code 400 (Bad Request)} if the state is not valid,
     * or with status {@code 404 (Not Found)} if the state is not found,
     * or with status {@code 500 (Internal Server Error)} if the state couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/states/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<State> partialUpdateState(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody State state
    ) throws URISyntaxException {
        log.debug("REST request to partial update State partially : {}, {}", id, state);
        if (state.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, state.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<State> result = stateService.partialUpdate(state);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, state.getId().toString())
        );
    }

    /**
     * {@code GET  /states} : get all the states.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of states in body.
     */
    @GetMapping("/states")
    public ResponseEntity<List<State>> getAllStates(StateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get States by criteria: {}", criteria);
        Page<State> page = stateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /states/count} : count all the states.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/states/count")
    public ResponseEntity<Long> countStates(StateCriteria criteria) {
        log.debug("REST request to count States by criteria: {}", criteria);
        return ResponseEntity.ok().body(stateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /states/:id} : get the "id" state.
     *
     * @param id the id of the state to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the state, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/states/{id}")
    public ResponseEntity<State> getState(@PathVariable Long id) {
        log.debug("REST request to get State : {}", id);
        Optional<State> state = stateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(state);
    }

    /**
     * {@code DELETE  /states/:id} : delete the "id" state.
     *
     * @param id the id of the state to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/states/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Long id) {
        log.debug("REST request to delete State : {}", id);
        stateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
