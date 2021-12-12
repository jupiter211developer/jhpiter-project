package com.ccr.county_record_app.web.rest;

import com.ccr.county_record_app.domain.CountyImage;
import com.ccr.county_record_app.repository.CountyImageRepository;
import com.ccr.county_record_app.service.CountyImageQueryService;
import com.ccr.county_record_app.service.CountyImageService;
import com.ccr.county_record_app.service.criteria.CountyImageCriteria;
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
 * REST controller for managing {@link com.ccr.county_record_app.domain.CountyImage}.
 */
@RestController
@RequestMapping("/api")
public class CountyImageResource {

    private final Logger log = LoggerFactory.getLogger(CountyImageResource.class);

    private static final String ENTITY_NAME = "countyImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountyImageService countyImageService;

    private final CountyImageRepository countyImageRepository;

    private final CountyImageQueryService countyImageQueryService;

    public CountyImageResource(
        CountyImageService countyImageService,
        CountyImageRepository countyImageRepository,
        CountyImageQueryService countyImageQueryService
    ) {
        this.countyImageService = countyImageService;
        this.countyImageRepository = countyImageRepository;
        this.countyImageQueryService = countyImageQueryService;
    }

    /**
     * {@code POST  /county-images} : Create a new countyImage.
     *
     * @param countyImage the countyImage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countyImage, or with status {@code 400 (Bad Request)} if the countyImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/county-images")
    public ResponseEntity<CountyImage> createCountyImage(@Valid @RequestBody CountyImage countyImage) throws URISyntaxException {
        log.debug("REST request to save CountyImage : {}", countyImage);
        if (countyImage.getId() != null) {
            throw new BadRequestAlertException("A new countyImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountyImage result = countyImageService.save(countyImage);
        return ResponseEntity
            .created(new URI("/api/county-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /county-images/:id} : Updates an existing countyImage.
     *
     * @param id the id of the countyImage to save.
     * @param countyImage the countyImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyImage,
     * or with status {@code 400 (Bad Request)} if the countyImage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countyImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/county-images/{id}")
    public ResponseEntity<CountyImage> updateCountyImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountyImage countyImage
    ) throws URISyntaxException {
        log.debug("REST request to update CountyImage : {}, {}", id, countyImage);
        if (countyImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountyImage result = countyImageService.save(countyImage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyImage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /county-images/:id} : Partial updates given fields of an existing countyImage, field will ignore if it is null
     *
     * @param id the id of the countyImage to save.
     * @param countyImage the countyImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyImage,
     * or with status {@code 400 (Bad Request)} if the countyImage is not valid,
     * or with status {@code 404 (Not Found)} if the countyImage is not found,
     * or with status {@code 500 (Internal Server Error)} if the countyImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/county-images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountyImage> partialUpdateCountyImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountyImage countyImage
    ) throws URISyntaxException {
        log.debug("REST request to partial update CountyImage partially : {}, {}", id, countyImage);
        if (countyImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountyImage> result = countyImageService.partialUpdate(countyImage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyImage.getId().toString())
        );
    }

    /**
     * {@code GET  /county-images} : get all the countyImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countyImages in body.
     */
    @GetMapping("/county-images")
    public ResponseEntity<List<CountyImage>> getAllCountyImages(CountyImageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CountyImages by criteria: {}", criteria);
        Page<CountyImage> page = countyImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /county-images/count} : count all the countyImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/county-images/count")
    public ResponseEntity<Long> countCountyImages(CountyImageCriteria criteria) {
        log.debug("REST request to count CountyImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(countyImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /county-images/:id} : get the "id" countyImage.
     *
     * @param id the id of the countyImage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countyImage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/county-images/{id}")
    public ResponseEntity<CountyImage> getCountyImage(@PathVariable Long id) {
        log.debug("REST request to get CountyImage : {}", id);
        Optional<CountyImage> countyImage = countyImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countyImage);
    }

    /**
     * {@code DELETE  /county-images/:id} : delete the "id" countyImage.
     *
     * @param id the id of the countyImage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/county-images/{id}")
    public ResponseEntity<Void> deleteCountyImage(@PathVariable Long id) {
        log.debug("REST request to delete CountyImage : {}", id);
        countyImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
