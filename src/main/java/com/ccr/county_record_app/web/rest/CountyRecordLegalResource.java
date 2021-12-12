package com.ccr.county_record_app.web.rest;

import com.ccr.county_record_app.domain.CountyRecordLegal;
import com.ccr.county_record_app.repository.CountyRecordLegalRepository;
import com.ccr.county_record_app.service.CountyRecordLegalQueryService;
import com.ccr.county_record_app.service.CountyRecordLegalService;
import com.ccr.county_record_app.service.criteria.CountyRecordLegalCriteria;
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
 * REST controller for managing {@link com.ccr.county_record_app.domain.CountyRecordLegal}.
 */
@RestController
@RequestMapping("/api")
public class CountyRecordLegalResource {

    private final Logger log = LoggerFactory.getLogger(CountyRecordLegalResource.class);

    private static final String ENTITY_NAME = "countyRecordLegal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountyRecordLegalService countyRecordLegalService;

    private final CountyRecordLegalRepository countyRecordLegalRepository;

    private final CountyRecordLegalQueryService countyRecordLegalQueryService;

    public CountyRecordLegalResource(
        CountyRecordLegalService countyRecordLegalService,
        CountyRecordLegalRepository countyRecordLegalRepository,
        CountyRecordLegalQueryService countyRecordLegalQueryService
    ) {
        this.countyRecordLegalService = countyRecordLegalService;
        this.countyRecordLegalRepository = countyRecordLegalRepository;
        this.countyRecordLegalQueryService = countyRecordLegalQueryService;
    }

    /**
     * {@code POST  /county-record-legals} : Create a new countyRecordLegal.
     *
     * @param countyRecordLegal the countyRecordLegal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countyRecordLegal, or with status {@code 400 (Bad Request)} if the countyRecordLegal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/county-record-legals")
    public ResponseEntity<CountyRecordLegal> createCountyRecordLegal(@Valid @RequestBody CountyRecordLegal countyRecordLegal)
        throws URISyntaxException {
        log.debug("REST request to save CountyRecordLegal : {}", countyRecordLegal);
        if (countyRecordLegal.getId() != null) {
            throw new BadRequestAlertException("A new countyRecordLegal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountyRecordLegal result = countyRecordLegalService.save(countyRecordLegal);
        return ResponseEntity
            .created(new URI("/api/county-record-legals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /county-record-legals/:id} : Updates an existing countyRecordLegal.
     *
     * @param id the id of the countyRecordLegal to save.
     * @param countyRecordLegal the countyRecordLegal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyRecordLegal,
     * or with status {@code 400 (Bad Request)} if the countyRecordLegal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countyRecordLegal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/county-record-legals/{id}")
    public ResponseEntity<CountyRecordLegal> updateCountyRecordLegal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountyRecordLegal countyRecordLegal
    ) throws URISyntaxException {
        log.debug("REST request to update CountyRecordLegal : {}, {}", id, countyRecordLegal);
        if (countyRecordLegal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyRecordLegal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyRecordLegalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountyRecordLegal result = countyRecordLegalService.save(countyRecordLegal);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyRecordLegal.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /county-record-legals/:id} : Partial updates given fields of an existing countyRecordLegal, field will ignore if it is null
     *
     * @param id the id of the countyRecordLegal to save.
     * @param countyRecordLegal the countyRecordLegal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyRecordLegal,
     * or with status {@code 400 (Bad Request)} if the countyRecordLegal is not valid,
     * or with status {@code 404 (Not Found)} if the countyRecordLegal is not found,
     * or with status {@code 500 (Internal Server Error)} if the countyRecordLegal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/county-record-legals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountyRecordLegal> partialUpdateCountyRecordLegal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountyRecordLegal countyRecordLegal
    ) throws URISyntaxException {
        log.debug("REST request to partial update CountyRecordLegal partially : {}, {}", id, countyRecordLegal);
        if (countyRecordLegal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyRecordLegal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyRecordLegalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountyRecordLegal> result = countyRecordLegalService.partialUpdate(countyRecordLegal);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyRecordLegal.getId().toString())
        );
    }

    /**
     * {@code GET  /county-record-legals} : get all the countyRecordLegals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countyRecordLegals in body.
     */
    @GetMapping("/county-record-legals")
    public ResponseEntity<List<CountyRecordLegal>> getAllCountyRecordLegals(CountyRecordLegalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CountyRecordLegals by criteria: {}", criteria);
        Page<CountyRecordLegal> page = countyRecordLegalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /county-record-legals/count} : count all the countyRecordLegals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/county-record-legals/count")
    public ResponseEntity<Long> countCountyRecordLegals(CountyRecordLegalCriteria criteria) {
        log.debug("REST request to count CountyRecordLegals by criteria: {}", criteria);
        return ResponseEntity.ok().body(countyRecordLegalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /county-record-legals/:id} : get the "id" countyRecordLegal.
     *
     * @param id the id of the countyRecordLegal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countyRecordLegal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/county-record-legals/{id}")
    public ResponseEntity<CountyRecordLegal> getCountyRecordLegal(@PathVariable Long id) {
        log.debug("REST request to get CountyRecordLegal : {}", id);
        Optional<CountyRecordLegal> countyRecordLegal = countyRecordLegalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countyRecordLegal);
    }

    /**
     * {@code DELETE  /county-record-legals/:id} : delete the "id" countyRecordLegal.
     *
     * @param id the id of the countyRecordLegal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/county-record-legals/{id}")
    public ResponseEntity<Void> deleteCountyRecordLegal(@PathVariable Long id) {
        log.debug("REST request to delete CountyRecordLegal : {}", id);
        countyRecordLegalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
