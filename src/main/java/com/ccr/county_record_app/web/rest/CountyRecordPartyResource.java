package com.ccr.county_record_app.web.rest;

import com.ccr.county_record_app.domain.CountyRecordParty;
import com.ccr.county_record_app.repository.CountyRecordPartyRepository;
import com.ccr.county_record_app.service.CountyRecordPartyQueryService;
import com.ccr.county_record_app.service.CountyRecordPartyService;
import com.ccr.county_record_app.service.criteria.CountyRecordPartyCriteria;
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
 * REST controller for managing {@link com.ccr.county_record_app.domain.CountyRecordParty}.
 */
@RestController
@RequestMapping("/api")
public class CountyRecordPartyResource {

    private final Logger log = LoggerFactory.getLogger(CountyRecordPartyResource.class);

    private static final String ENTITY_NAME = "countyRecordParty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountyRecordPartyService countyRecordPartyService;

    private final CountyRecordPartyRepository countyRecordPartyRepository;

    private final CountyRecordPartyQueryService countyRecordPartyQueryService;

    public CountyRecordPartyResource(
        CountyRecordPartyService countyRecordPartyService,
        CountyRecordPartyRepository countyRecordPartyRepository,
        CountyRecordPartyQueryService countyRecordPartyQueryService
    ) {
        this.countyRecordPartyService = countyRecordPartyService;
        this.countyRecordPartyRepository = countyRecordPartyRepository;
        this.countyRecordPartyQueryService = countyRecordPartyQueryService;
    }

    /**
     * {@code POST  /county-record-parties} : Create a new countyRecordParty.
     *
     * @param countyRecordParty the countyRecordParty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countyRecordParty, or with status {@code 400 (Bad Request)} if the countyRecordParty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/county-record-parties")
    public ResponseEntity<CountyRecordParty> createCountyRecordParty(@Valid @RequestBody CountyRecordParty countyRecordParty)
        throws URISyntaxException {
        log.debug("REST request to save CountyRecordParty : {}", countyRecordParty);
        if (countyRecordParty.getId() != null) {
            throw new BadRequestAlertException("A new countyRecordParty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountyRecordParty result = countyRecordPartyService.save(countyRecordParty);
        return ResponseEntity
            .created(new URI("/api/county-record-parties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /county-record-parties/:id} : Updates an existing countyRecordParty.
     *
     * @param id the id of the countyRecordParty to save.
     * @param countyRecordParty the countyRecordParty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyRecordParty,
     * or with status {@code 400 (Bad Request)} if the countyRecordParty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countyRecordParty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/county-record-parties/{id}")
    public ResponseEntity<CountyRecordParty> updateCountyRecordParty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountyRecordParty countyRecordParty
    ) throws URISyntaxException {
        log.debug("REST request to update CountyRecordParty : {}, {}", id, countyRecordParty);
        if (countyRecordParty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyRecordParty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyRecordPartyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountyRecordParty result = countyRecordPartyService.save(countyRecordParty);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyRecordParty.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /county-record-parties/:id} : Partial updates given fields of an existing countyRecordParty, field will ignore if it is null
     *
     * @param id the id of the countyRecordParty to save.
     * @param countyRecordParty the countyRecordParty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyRecordParty,
     * or with status {@code 400 (Bad Request)} if the countyRecordParty is not valid,
     * or with status {@code 404 (Not Found)} if the countyRecordParty is not found,
     * or with status {@code 500 (Internal Server Error)} if the countyRecordParty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/county-record-parties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountyRecordParty> partialUpdateCountyRecordParty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountyRecordParty countyRecordParty
    ) throws URISyntaxException {
        log.debug("REST request to partial update CountyRecordParty partially : {}, {}", id, countyRecordParty);
        if (countyRecordParty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyRecordParty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyRecordPartyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountyRecordParty> result = countyRecordPartyService.partialUpdate(countyRecordParty);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyRecordParty.getId().toString())
        );
    }

    /**
     * {@code GET  /county-record-parties} : get all the countyRecordParties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countyRecordParties in body.
     */
    @GetMapping("/county-record-parties")
    public ResponseEntity<List<CountyRecordParty>> getAllCountyRecordParties(CountyRecordPartyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CountyRecordParties by criteria: {}", criteria);
        Page<CountyRecordParty> page = countyRecordPartyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /county-record-parties/count} : count all the countyRecordParties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/county-record-parties/count")
    public ResponseEntity<Long> countCountyRecordParties(CountyRecordPartyCriteria criteria) {
        log.debug("REST request to count CountyRecordParties by criteria: {}", criteria);
        return ResponseEntity.ok().body(countyRecordPartyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /county-record-parties/:id} : get the "id" countyRecordParty.
     *
     * @param id the id of the countyRecordParty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countyRecordParty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/county-record-parties/{id}")
    public ResponseEntity<CountyRecordParty> getCountyRecordParty(@PathVariable Long id) {
        log.debug("REST request to get CountyRecordParty : {}", id);
        Optional<CountyRecordParty> countyRecordParty = countyRecordPartyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countyRecordParty);
    }

    /**
     * {@code DELETE  /county-record-parties/:id} : delete the "id" countyRecordParty.
     *
     * @param id the id of the countyRecordParty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/county-record-parties/{id}")
    public ResponseEntity<Void> deleteCountyRecordParty(@PathVariable Long id) {
        log.debug("REST request to delete CountyRecordParty : {}", id);
        countyRecordPartyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
