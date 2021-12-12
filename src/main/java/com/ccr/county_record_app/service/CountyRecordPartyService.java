package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.CountyRecordParty;
import com.ccr.county_record_app.repository.CountyRecordPartyRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CountyRecordParty}.
 */
@Service
@Transactional
public class CountyRecordPartyService {

    private final Logger log = LoggerFactory.getLogger(CountyRecordPartyService.class);

    private final CountyRecordPartyRepository countyRecordPartyRepository;

    public CountyRecordPartyService(CountyRecordPartyRepository countyRecordPartyRepository) {
        this.countyRecordPartyRepository = countyRecordPartyRepository;
    }

    /**
     * Save a countyRecordParty.
     *
     * @param countyRecordParty the entity to save.
     * @return the persisted entity.
     */
    public CountyRecordParty save(CountyRecordParty countyRecordParty) {
        log.debug("Request to save CountyRecordParty : {}", countyRecordParty);
        return countyRecordPartyRepository.save(countyRecordParty);
    }

    /**
     * Partially update a countyRecordParty.
     *
     * @param countyRecordParty the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountyRecordParty> partialUpdate(CountyRecordParty countyRecordParty) {
        log.debug("Request to partially update CountyRecordParty : {}", countyRecordParty);

        return countyRecordPartyRepository
            .findById(countyRecordParty.getId())
            .map(existingCountyRecordParty -> {
                if (countyRecordParty.getRecordKey() != null) {
                    existingCountyRecordParty.setRecordKey(countyRecordParty.getRecordKey());
                }
                if (countyRecordParty.getPartyName() != null) {
                    existingCountyRecordParty.setPartyName(countyRecordParty.getPartyName());
                }
                if (countyRecordParty.getPartyRole() != null) {
                    existingCountyRecordParty.setPartyRole(countyRecordParty.getPartyRole());
                }

                return existingCountyRecordParty;
            })
            .map(countyRecordPartyRepository::save);
    }

    /**
     * Get all the countyRecordParties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyRecordParty> findAll(Pageable pageable) {
        log.debug("Request to get all CountyRecordParties");
        return countyRecordPartyRepository.findAll(pageable);
    }

    /**
     * Get one countyRecordParty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountyRecordParty> findOne(Long id) {
        log.debug("Request to get CountyRecordParty : {}", id);
        return countyRecordPartyRepository.findById(id);
    }

    /**
     * Delete the countyRecordParty by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CountyRecordParty : {}", id);
        countyRecordPartyRepository.deleteById(id);
    }
}
