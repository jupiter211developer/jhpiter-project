package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.CountiesAvaiable;
import com.ccr.county_record_app.repository.CountiesAvaiableRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CountiesAvaiable}.
 */
@Service
@Transactional
public class CountiesAvaiableService {

    private final Logger log = LoggerFactory.getLogger(CountiesAvaiableService.class);

    private final CountiesAvaiableRepository countiesAvaiableRepository;

    public CountiesAvaiableService(CountiesAvaiableRepository countiesAvaiableRepository) {
        this.countiesAvaiableRepository = countiesAvaiableRepository;
    }

    /**
     * Save a countiesAvaiable.
     *
     * @param countiesAvaiable the entity to save.
     * @return the persisted entity.
     */
    public CountiesAvaiable save(CountiesAvaiable countiesAvaiable) {
        log.debug("Request to save CountiesAvaiable : {}", countiesAvaiable);
        return countiesAvaiableRepository.save(countiesAvaiable);
    }

    /**
     * Partially update a countiesAvaiable.
     *
     * @param countiesAvaiable the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountiesAvaiable> partialUpdate(CountiesAvaiable countiesAvaiable) {
        log.debug("Request to partially update CountiesAvaiable : {}", countiesAvaiable);

        return countiesAvaiableRepository
            .findById(countiesAvaiable.getId())
            .map(existingCountiesAvaiable -> {
                if (countiesAvaiable.getEarliest() != null) {
                    existingCountiesAvaiable.setEarliest(countiesAvaiable.getEarliest());
                }
                if (countiesAvaiable.getLatest() != null) {
                    existingCountiesAvaiable.setLatest(countiesAvaiable.getLatest());
                }
                if (countiesAvaiable.getRecordCount() != null) {
                    existingCountiesAvaiable.setRecordCount(countiesAvaiable.getRecordCount());
                }
                if (countiesAvaiable.getFips() != null) {
                    existingCountiesAvaiable.setFips(countiesAvaiable.getFips());
                }
                if (countiesAvaiable.getCountyName() != null) {
                    existingCountiesAvaiable.setCountyName(countiesAvaiable.getCountyName());
                }
                if (countiesAvaiable.getStateAbbr() != null) {
                    existingCountiesAvaiable.setStateAbbr(countiesAvaiable.getStateAbbr());
                }

                return existingCountiesAvaiable;
            })
            .map(countiesAvaiableRepository::save);
    }

    /**
     * Get all the countiesAvaiables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CountiesAvaiable> findAll(Pageable pageable) {
        log.debug("Request to get all CountiesAvaiables");
        return countiesAvaiableRepository.findAll(pageable);
    }

    /**
     * Get one countiesAvaiable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountiesAvaiable> findOne(Long id) {
        log.debug("Request to get CountiesAvaiable : {}", id);
        return countiesAvaiableRepository.findById(id);
    }

    /**
     * Delete the countiesAvaiable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CountiesAvaiable : {}", id);
        countiesAvaiableRepository.deleteById(id);
    }
}
