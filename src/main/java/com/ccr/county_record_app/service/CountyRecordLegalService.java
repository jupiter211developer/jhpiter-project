package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.CountyRecordLegal;
import com.ccr.county_record_app.repository.CountyRecordLegalRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CountyRecordLegal}.
 */
@Service
@Transactional
public class CountyRecordLegalService {

    private final Logger log = LoggerFactory.getLogger(CountyRecordLegalService.class);

    private final CountyRecordLegalRepository countyRecordLegalRepository;

    public CountyRecordLegalService(CountyRecordLegalRepository countyRecordLegalRepository) {
        this.countyRecordLegalRepository = countyRecordLegalRepository;
    }

    /**
     * Save a countyRecordLegal.
     *
     * @param countyRecordLegal the entity to save.
     * @return the persisted entity.
     */
    public CountyRecordLegal save(CountyRecordLegal countyRecordLegal) {
        log.debug("Request to save CountyRecordLegal : {}", countyRecordLegal);
        return countyRecordLegalRepository.save(countyRecordLegal);
    }

    /**
     * Partially update a countyRecordLegal.
     *
     * @param countyRecordLegal the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountyRecordLegal> partialUpdate(CountyRecordLegal countyRecordLegal) {
        log.debug("Request to partially update CountyRecordLegal : {}", countyRecordLegal);

        return countyRecordLegalRepository
            .findById(countyRecordLegal.getId())
            .map(existingCountyRecordLegal -> {
                if (countyRecordLegal.getLegal() != null) {
                    existingCountyRecordLegal.setLegal(countyRecordLegal.getLegal());
                }
                if (countyRecordLegal.getRecordKey() != null) {
                    existingCountyRecordLegal.setRecordKey(countyRecordLegal.getRecordKey());
                }

                return existingCountyRecordLegal;
            })
            .map(countyRecordLegalRepository::save);
    }

    /**
     * Get all the countyRecordLegals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyRecordLegal> findAll(Pageable pageable) {
        log.debug("Request to get all CountyRecordLegals");
        return countyRecordLegalRepository.findAll(pageable);
    }

    /**
     * Get one countyRecordLegal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountyRecordLegal> findOne(Long id) {
        log.debug("Request to get CountyRecordLegal : {}", id);
        return countyRecordLegalRepository.findById(id);
    }

    /**
     * Delete the countyRecordLegal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CountyRecordLegal : {}", id);
        countyRecordLegalRepository.deleteById(id);
    }
}
