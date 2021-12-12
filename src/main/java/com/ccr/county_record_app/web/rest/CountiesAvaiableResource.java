package com.ccr.county_record_app.web.rest;

import com.ccr.county_record_app.domain.CountiesAvaiable;
import com.ccr.county_record_app.repository.CountiesAvaiableRepository;
import com.ccr.county_record_app.service.CountiesAvaiableQueryService;
import com.ccr.county_record_app.service.CountiesAvaiableService;
import com.ccr.county_record_app.service.criteria.CountiesAvaiableCriteria;
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
 * REST controller for managing {@link com.ccr.county_record_app.domain.CountiesAvaiable}.
 */
@RestController
@RequestMapping("/api")
public class CountiesAvaiableResource {

    private final Logger log = LoggerFactory.getLogger(CountiesAvaiableResource.class);

    private static final String ENTITY_NAME = "countiesAvaiable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountiesAvaiableService countiesAvaiableService;

    private final CountiesAvaiableRepository countiesAvaiableRepository;

    private final CountiesAvaiableQueryService countiesAvaiableQueryService;

    public CountiesAvaiableResource(
        CountiesAvaiableService countiesAvaiableService,
        CountiesAvaiableRepository countiesAvaiableRepository,
        CountiesAvaiableQueryService countiesAvaiableQueryService
    ) {
        this.countiesAvaiableService = countiesAvaiableService;
        this.countiesAvaiableRepository = countiesAvaiableRepository;
        this.countiesAvaiableQueryService = countiesAvaiableQueryService;
    }

    /**
     * {@code POST  /counties-avaiables} : Create a new countiesAvaiable.
     *
     * @param countiesAvaiable the countiesAvaiable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countiesAvaiable, or with status {@code 400 (Bad Request)} if the countiesAvaiable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/counties-avaiables")
    public ResponseEntity<CountiesAvaiable> createCountiesAvaiable(@Valid @RequestBody CountiesAvaiable countiesAvaiable)
        throws URISyntaxException {
        log.debug("REST request to save CountiesAvaiable : {}", countiesAvaiable);
        if (countiesAvaiable.getId() != null) {
            throw new BadRequestAlertException("A new countiesAvaiable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountiesAvaiable result = countiesAvaiableService.save(countiesAvaiable);
        return ResponseEntity
            .created(new URI("/api/counties-avaiables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /counties-avaiables/:id} : Updates an existing countiesAvaiable.
     *
     * @param id the id of the countiesAvaiable to save.
     * @param countiesAvaiable the countiesAvaiable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countiesAvaiable,
     * or with status {@code 400 (Bad Request)} if the countiesAvaiable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countiesAvaiable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/counties-avaiables/{id}")
    public ResponseEntity<CountiesAvaiable> updateCountiesAvaiable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountiesAvaiable countiesAvaiable
    ) throws URISyntaxException {
        log.debug("REST request to update CountiesAvaiable : {}, {}", id, countiesAvaiable);
        if (countiesAvaiable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countiesAvaiable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countiesAvaiableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountiesAvaiable result = countiesAvaiableService.save(countiesAvaiable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countiesAvaiable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /counties-avaiables/:id} : Partial updates given fields of an existing countiesAvaiable, field will ignore if it is null
     *
     * @param id the id of the countiesAvaiable to save.
     * @param countiesAvaiable the countiesAvaiable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countiesAvaiable,
     * or with status {@code 400 (Bad Request)} if the countiesAvaiable is not valid,
     * or with status {@code 404 (Not Found)} if the countiesAvaiable is not found,
     * or with status {@code 500 (Internal Server Error)} if the countiesAvaiable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/counties-avaiables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountiesAvaiable> partialUpdateCountiesAvaiable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountiesAvaiable countiesAvaiable
    ) throws URISyntaxException {
        log.debug("REST request to partial update CountiesAvaiable partially : {}, {}", id, countiesAvaiable);
        if (countiesAvaiable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countiesAvaiable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countiesAvaiableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountiesAvaiable> result = countiesAvaiableService.partialUpdate(countiesAvaiable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countiesAvaiable.getId().toString())
        );
    }

    /**
     * {@code GET  /counties-avaiables} : get all the countiesAvaiables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countiesAvaiables in body.
     */
    @GetMapping("/counties-avaiables")
    public ResponseEntity<List<CountiesAvaiable>> getAllCountiesAvaiables(CountiesAvaiableCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CountiesAvaiables by criteria: {}", criteria);
        Page<CountiesAvaiable> page = countiesAvaiableQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /counties-avaiables/count} : count all the countiesAvaiables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/counties-avaiables/count")
    public ResponseEntity<Long> countCountiesAvaiables(CountiesAvaiableCriteria criteria) {
        log.debug("REST request to count CountiesAvaiables by criteria: {}", criteria);
        return ResponseEntity.ok().body(countiesAvaiableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /counties-avaiables/:id} : get the "id" countiesAvaiable.
     *
     * @param id the id of the countiesAvaiable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countiesAvaiable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/counties-avaiables/{id}")
    public ResponseEntity<CountiesAvaiable> getCountiesAvaiable(@PathVariable Long id) {
        log.debug("REST request to get CountiesAvaiable : {}", id);
        Optional<CountiesAvaiable> countiesAvaiable = countiesAvaiableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countiesAvaiable);
    }

    /**
     * {@code DELETE  /counties-avaiables/:id} : delete the "id" countiesAvaiable.
     *
     * @param id the id of the countiesAvaiable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/counties-avaiables/{id}")
    public ResponseEntity<Void> deleteCountiesAvaiable(@PathVariable Long id) {
        log.debug("REST request to delete CountiesAvaiable : {}", id);
        countiesAvaiableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
