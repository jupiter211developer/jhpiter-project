package com.ccr.county_record_app.web.rest;

import com.ccr.county_record_app.domain.CountyImagePage;
import com.ccr.county_record_app.repository.CountyImagePageRepository;
import com.ccr.county_record_app.service.CountyImagePageQueryService;
import com.ccr.county_record_app.service.CountyImagePageService;
import com.ccr.county_record_app.service.criteria.CountyImagePageCriteria;
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
 * REST controller for managing {@link com.ccr.county_record_app.domain.CountyImagePage}.
 */
@RestController
@RequestMapping("/api")
public class CountyImagePageResource {

    private final Logger log = LoggerFactory.getLogger(CountyImagePageResource.class);

    private static final String ENTITY_NAME = "countyImagePage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountyImagePageService countyImagePageService;

    private final CountyImagePageRepository countyImagePageRepository;

    private final CountyImagePageQueryService countyImagePageQueryService;

    public CountyImagePageResource(
        CountyImagePageService countyImagePageService,
        CountyImagePageRepository countyImagePageRepository,
        CountyImagePageQueryService countyImagePageQueryService
    ) {
        this.countyImagePageService = countyImagePageService;
        this.countyImagePageRepository = countyImagePageRepository;
        this.countyImagePageQueryService = countyImagePageQueryService;
    }

    /**
     * {@code POST  /county-image-pages} : Create a new countyImagePage.
     *
     * @param countyImagePage the countyImagePage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countyImagePage, or with status {@code 400 (Bad Request)} if the countyImagePage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/county-image-pages")
    public ResponseEntity<CountyImagePage> createCountyImagePage(@Valid @RequestBody CountyImagePage countyImagePage)
        throws URISyntaxException {
        log.debug("REST request to save CountyImagePage : {}", countyImagePage);
        if (countyImagePage.getId() != null) {
            throw new BadRequestAlertException("A new countyImagePage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountyImagePage result = countyImagePageService.save(countyImagePage);
        return ResponseEntity
            .created(new URI("/api/county-image-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /county-image-pages/:id} : Updates an existing countyImagePage.
     *
     * @param id the id of the countyImagePage to save.
     * @param countyImagePage the countyImagePage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyImagePage,
     * or with status {@code 400 (Bad Request)} if the countyImagePage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countyImagePage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/county-image-pages/{id}")
    public ResponseEntity<CountyImagePage> updateCountyImagePage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountyImagePage countyImagePage
    ) throws URISyntaxException {
        log.debug("REST request to update CountyImagePage : {}, {}", id, countyImagePage);
        if (countyImagePage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyImagePage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyImagePageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountyImagePage result = countyImagePageService.save(countyImagePage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyImagePage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /county-image-pages/:id} : Partial updates given fields of an existing countyImagePage, field will ignore if it is null
     *
     * @param id the id of the countyImagePage to save.
     * @param countyImagePage the countyImagePage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyImagePage,
     * or with status {@code 400 (Bad Request)} if the countyImagePage is not valid,
     * or with status {@code 404 (Not Found)} if the countyImagePage is not found,
     * or with status {@code 500 (Internal Server Error)} if the countyImagePage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/county-image-pages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountyImagePage> partialUpdateCountyImagePage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountyImagePage countyImagePage
    ) throws URISyntaxException {
        log.debug("REST request to partial update CountyImagePage partially : {}, {}", id, countyImagePage);
        if (countyImagePage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyImagePage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyImagePageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountyImagePage> result = countyImagePageService.partialUpdate(countyImagePage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyImagePage.getId().toString())
        );
    }

    /**
     * {@code GET  /county-image-pages} : get all the countyImagePages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countyImagePages in body.
     */
    @GetMapping("/county-image-pages")
    public ResponseEntity<List<CountyImagePage>> getAllCountyImagePages(CountyImagePageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CountyImagePages by criteria: {}", criteria);
        Page<CountyImagePage> page = countyImagePageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /county-image-pages/count} : count all the countyImagePages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/county-image-pages/count")
    public ResponseEntity<Long> countCountyImagePages(CountyImagePageCriteria criteria) {
        log.debug("REST request to count CountyImagePages by criteria: {}", criteria);
        return ResponseEntity.ok().body(countyImagePageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /county-image-pages/:id} : get the "id" countyImagePage.
     *
     * @param id the id of the countyImagePage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countyImagePage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/county-image-pages/{id}")
    public ResponseEntity<CountyImagePage> getCountyImagePage(@PathVariable Long id) {
        log.debug("REST request to get CountyImagePage : {}", id);
        Optional<CountyImagePage> countyImagePage = countyImagePageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countyImagePage);
    }

    /**
     * {@code DELETE  /county-image-pages/:id} : delete the "id" countyImagePage.
     *
     * @param id the id of the countyImagePage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/county-image-pages/{id}")
    public ResponseEntity<Void> deleteCountyImagePage(@PathVariable Long id) {
        log.debug("REST request to delete CountyImagePage : {}", id);
        countyImagePageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
