package com.ccr.county_record_app.web.rest;

import com.ccr.county_record_app.domain.County;
import com.ccr.county_record_app.repository.CountyRepository;
import com.ccr.county_record_app.service.CountyQueryService;
import com.ccr.county_record_app.service.CountyService;
import com.ccr.county_record_app.service.criteria.CountyCriteria;
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
 * REST controller for managing {@link com.ccr.county_record_app.domain.County}.
 */
@RestController
@RequestMapping("/api")
public class CountyResource {

    private final Logger log = LoggerFactory.getLogger(CountyResource.class);

    private static final String ENTITY_NAME = "county";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountyService countyService;

    private final CountyRepository countyRepository;

    private final CountyQueryService countyQueryService;

    public CountyResource(CountyService countyService, CountyRepository countyRepository, CountyQueryService countyQueryService) {
        this.countyService = countyService;
        this.countyRepository = countyRepository;
        this.countyQueryService = countyQueryService;
    }

    /**
     * {@code POST  /counties} : Create a new county.
     *
     * @param county the county to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new county, or with status {@code 400 (Bad Request)} if the county has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/counties")
    public ResponseEntity<County> createCounty(@Valid @RequestBody County county) throws URISyntaxException {
        log.debug("REST request to save County : {}", county);
        if (county.getId() != null) {
            throw new BadRequestAlertException("A new county cannot already have an ID", ENTITY_NAME, "idexists");
        }
        County result = countyService.save(county);
        return ResponseEntity
            .created(new URI("/api/counties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /counties/:id} : Updates an existing county.
     *
     * @param id the id of the county to save.
     * @param county the county to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated county,
     * or with status {@code 400 (Bad Request)} if the county is not valid,
     * or with status {@code 500 (Internal Server Error)} if the county couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/counties/{id}")
    public ResponseEntity<County> updateCounty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody County county
    ) throws URISyntaxException {
        log.debug("REST request to update County : {}, {}", id, county);
        if (county.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, county.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        County result = countyService.save(county);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, county.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /counties/:id} : Partial updates given fields of an existing county, field will ignore if it is null
     *
     * @param id the id of the county to save.
     * @param county the county to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated county,
     * or with status {@code 400 (Bad Request)} if the county is not valid,
     * or with status {@code 404 (Not Found)} if the county is not found,
     * or with status {@code 500 (Internal Server Error)} if the county couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/counties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<County> partialUpdateCounty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody County county
    ) throws URISyntaxException {
        log.debug("REST request to partial update County partially : {}, {}", id, county);
        if (county.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, county.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<County> result = countyService.partialUpdate(county);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, county.getId().toString())
        );
    }

    /**
     * {@code GET  /counties} : get all the counties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counties in body.
     */
    @GetMapping("/counties")
    public ResponseEntity<List<County>> getAllCounties(CountyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Counties by criteria: {}", criteria);
        Page<County> page = countyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /counties/count} : count all the counties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/counties/count")
    public ResponseEntity<Long> countCounties(CountyCriteria criteria) {
        log.debug("REST request to count Counties by criteria: {}", criteria);
        return ResponseEntity.ok().body(countyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /counties/:id} : get the "id" county.
     *
     * @param id the id of the county to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the county, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/counties/{id}")
    public ResponseEntity<County> getCounty(@PathVariable Long id) {
        log.debug("REST request to get County : {}", id);
        Optional<County> county = countyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(county);
    }

    /**
     * {@code DELETE  /counties/:id} : delete the "id" county.
     *
     * @param id the id of the county to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/counties/{id}")
    public ResponseEntity<Void> deleteCounty(@PathVariable Long id) {
        log.debug("REST request to delete County : {}", id);
        countyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
