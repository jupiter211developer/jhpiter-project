package com.ccr.county_record_app.web.rest;

import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.repository.CountyRecordRepository;
import com.ccr.county_record_app.service.CountyRecordQueryService;
import com.ccr.county_record_app.service.CountyRecordService;
import com.ccr.county_record_app.service.criteria.CountyRecordCriteria;
import com.ccr.county_record_app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.ccr.county_record_app.domain.CountyRecord}.
 */
@RestController
@RequestMapping("/api")
public class CountyRecordResource {

    private final Logger log = LoggerFactory.getLogger(CountyRecordResource.class);

    private static final String ENTITY_NAME = "countyRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountyRecordService countyRecordService;

    private final CountyRecordRepository countyRecordRepository;

    private final CountyRecordQueryService countyRecordQueryService;

    public CountyRecordResource(
        CountyRecordService countyRecordService,
        CountyRecordRepository countyRecordRepository,
        CountyRecordQueryService countyRecordQueryService
    ) {
        this.countyRecordService = countyRecordService;
        this.countyRecordRepository = countyRecordRepository;
        this.countyRecordQueryService = countyRecordQueryService;
    }

    /**
     * {@code POST  /county-records} : Create a new countyRecord.
     *
     * @param countyRecord the countyRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countyRecord, or with status {@code 400 (Bad Request)} if the countyRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/county-records")
    public ResponseEntity<CountyRecord> createCountyRecord(@Valid @RequestBody CountyRecord countyRecord) throws URISyntaxException {
        log.debug("REST request to save CountyRecord : {}", countyRecord);
        if (countyRecord.getId() != null) {
            throw new BadRequestAlertException("A new countyRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountyRecord result = countyRecordService.save(countyRecord);
        return ResponseEntity
            .created(new URI("/api/county-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /county-records/:id} : Updates an existing countyRecord.
     *
     * @param id the id of the countyRecord to save.
     * @param countyRecord the countyRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyRecord,
     * or with status {@code 400 (Bad Request)} if the countyRecord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countyRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/county-records/{id}")
    public ResponseEntity<CountyRecord> updateCountyRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountyRecord countyRecord
    ) throws URISyntaxException {
        log.debug("REST request to update CountyRecord : {}, {}", id, countyRecord);
        if (countyRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountyRecord result = countyRecordService.save(countyRecord);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyRecord.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /county-records/:id} : Partial updates given fields of an existing countyRecord, field will ignore if it is null
     *
     * @param id the id of the countyRecord to save.
     * @param countyRecord the countyRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyRecord,
     * or with status {@code 400 (Bad Request)} if the countyRecord is not valid,
     * or with status {@code 404 (Not Found)} if the countyRecord is not found,
     * or with status {@code 500 (Internal Server Error)} if the countyRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/county-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountyRecord> partialUpdateCountyRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountyRecord countyRecord
    ) throws URISyntaxException {
        log.debug("REST request to partial update CountyRecord partially : {}, {}", id, countyRecord);
        if (countyRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountyRecord> result = countyRecordService.partialUpdate(countyRecord);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyRecord.getId().toString())
        );
    }

    /**
     * {@code GET  /county-records} : get all the countyRecords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countyRecords in body.
     */
    @GetMapping("/county-records")
    public ResponseEntity<List<CountyRecord>> getAllCountyRecords(CountyRecordCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CountyRecords by criteria: {}", criteria);
        Page<CountyRecord> page = countyRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /county-records/count} : count all the countyRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/county-records/count")
    public ResponseEntity<Long> countCountyRecords(CountyRecordCriteria criteria) {
        log.debug("REST request to count CountyRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(countyRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /county-records/:id} : get the "id" countyRecord.
     *
     * @param id the id of the countyRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countyRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/county-records/{id}")
    public ResponseEntity<CountyRecord> getCountyRecord(@PathVariable Long id) {
        log.debug("REST request to get CountyRecord : {}", id);
        Optional<CountyRecord> countyRecord = countyRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countyRecord);
    }

    /**
     * {@code DELETE  /county-records/:id} : delete the "id" countyRecord.
     *
     * @param id the id of the countyRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/county-records/{id}")
    public ResponseEntity<Void> deleteCountyRecord(@PathVariable Long id) {
        log.debug("REST request to delete CountyRecord : {}", id);
        countyRecordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
