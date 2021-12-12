package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.State;
import com.ccr.county_record_app.repository.StateRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link State}.
 */
@Service
@Transactional
public class StateService {

    private final Logger log = LoggerFactory.getLogger(StateService.class);

    private final StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    /**
     * Save a state.
     *
     * @param state the entity to save.
     * @return the persisted entity.
     */
    public State save(State state) {
        log.debug("Request to save State : {}", state);
        return stateRepository.save(state);
    }

    /**
     * Partially update a state.
     *
     * @param state the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<State> partialUpdate(State state) {
        log.debug("Request to partially update State : {}", state);

        return stateRepository
            .findById(state.getId())
            .map(existingState -> {
                if (state.getStateName() != null) {
                    existingState.setStateName(state.getStateName());
                }
                if (state.getStateAbbr() != null) {
                    existingState.setStateAbbr(state.getStateAbbr());
                }
                if (state.getSubRegion() != null) {
                    existingState.setSubRegion(state.getSubRegion());
                }
                if (state.getStFips() != null) {
                    existingState.setStFips(state.getStFips());
                }

                return existingState;
            })
            .map(stateRepository::save);
    }

    /**
     * Get all the states.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<State> findAll(Pageable pageable) {
        log.debug("Request to get all States");
        return stateRepository.findAll(pageable);
    }

    /**
     * Get one state by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<State> findOne(Long id) {
        log.debug("Request to get State : {}", id);
        return stateRepository.findById(id);
    }

    /**
     * Delete the state by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete State : {}", id);
        stateRepository.deleteById(id);
    }
}
