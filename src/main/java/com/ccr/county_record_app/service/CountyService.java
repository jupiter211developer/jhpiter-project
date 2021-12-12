package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.County;
import com.ccr.county_record_app.repository.CountyRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link County}.
 */
@Service
@Transactional
public class CountyService {

    private final Logger log = LoggerFactory.getLogger(CountyService.class);

    private final CountyRepository countyRepository;

    public CountyService(CountyRepository countyRepository) {
        this.countyRepository = countyRepository;
    }

    /**
     * Save a county.
     *
     * @param county the entity to save.
     * @return the persisted entity.
     */
    public County save(County county) {
        log.debug("Request to save County : {}", county);
        return countyRepository.save(county);
    }

    /**
     * Partially update a county.
     *
     * @param county the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<County> partialUpdate(County county) {
        log.debug("Request to partially update County : {}", county);

        return countyRepository
            .findById(county.getId())
            .map(existingCounty -> {
                if (county.getCountyName() != null) {
                    existingCounty.setCountyName(county.getCountyName());
                }
                if (county.getCntyFips() != null) {
                    existingCounty.setCntyFips(county.getCntyFips());
                }
                if (county.getStateAbbr() != null) {
                    existingCounty.setStateAbbr(county.getStateAbbr());
                }
                if (county.getStFips() != null) {
                    existingCounty.setStFips(county.getStFips());
                }
                if (county.getFips() != null) {
                    existingCounty.setFips(county.getFips());
                }

                return existingCounty;
            })
            .map(countyRepository::save);
    }

    /**
     * Get all the counties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<County> findAll(Pageable pageable) {
        log.debug("Request to get all Counties");
        return countyRepository.findAll(pageable);
    }

    /**
     *  Get all the counties where CountiesAvaiable is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<County> findAllWhereCountiesAvaiableIsNull() {
        log.debug("Request to get all counties where CountiesAvaiable is null");
        return StreamSupport
            .stream(countyRepository.findAll().spliterator(), false)
            .filter(county -> county.getCountiesAvaiable() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one county by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<County> findOne(Long id) {
        log.debug("Request to get County : {}", id);
        return countyRepository.findById(id);
    }

    /**
     * Delete the county by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete County : {}", id);
        countyRepository.deleteById(id);
    }
}
