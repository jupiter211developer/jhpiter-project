package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.*; // for static metamodels
import com.ccr.county_record_app.domain.State;
import com.ccr.county_record_app.repository.StateRepository;
import com.ccr.county_record_app.service.criteria.StateCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link State} entities in the database.
 * The main input is a {@link StateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link State} or a {@link Page} of {@link State} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StateQueryService extends QueryService<State> {

    private final Logger log = LoggerFactory.getLogger(StateQueryService.class);

    private final StateRepository stateRepository;

    public StateQueryService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    /**
     * Return a {@link List} of {@link State} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<State> findByCriteria(StateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<State> specification = createSpecification(criteria);
        return stateRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link State} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<State> findByCriteria(StateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<State> specification = createSpecification(criteria);
        return stateRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<State> specification = createSpecification(criteria);
        return stateRepository.count(specification);
    }

    /**
     * Function to convert {@link StateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<State> createSpecification(StateCriteria criteria) {
        Specification<State> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), State_.id));
            }
            if (criteria.getStateName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateName(), State_.stateName));
            }
            if (criteria.getStateAbbr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateAbbr(), State_.stateAbbr));
            }
            if (criteria.getSubRegion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubRegion(), State_.subRegion));
            }
            if (criteria.getStFips() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStFips(), State_.stFips));
            }
            if (criteria.getCountyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCountyId(), root -> root.join(State_.counties, JoinType.LEFT).get(County_.id))
                    );
            }
        }
        return specification;
    }
}
