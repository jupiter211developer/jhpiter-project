package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.*; // for static metamodels
import com.ccr.county_record_app.domain.County;
import com.ccr.county_record_app.repository.CountyRepository;
import com.ccr.county_record_app.service.criteria.CountyCriteria;
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
 * Service for executing complex queries for {@link County} entities in the database.
 * The main input is a {@link CountyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link County} or a {@link Page} of {@link County} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountyQueryService extends QueryService<County> {

    private final Logger log = LoggerFactory.getLogger(CountyQueryService.class);

    private final CountyRepository countyRepository;

    public CountyQueryService(CountyRepository countyRepository) {
        this.countyRepository = countyRepository;
    }

    /**
     * Return a {@link List} of {@link County} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<County> findByCriteria(CountyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<County> specification = createSpecification(criteria);
        return countyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link County} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<County> findByCriteria(CountyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<County> specification = createSpecification(criteria);
        return countyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<County> specification = createSpecification(criteria);
        return countyRepository.count(specification);
    }

    /**
     * Function to convert {@link CountyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<County> createSpecification(CountyCriteria criteria) {
        Specification<County> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), County_.id));
            }
            if (criteria.getCountyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyName(), County_.countyName));
            }
            if (criteria.getCntyFips() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCntyFips(), County_.cntyFips));
            }
            if (criteria.getStateAbbr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateAbbr(), County_.stateAbbr));
            }
            if (criteria.getStFips() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStFips(), County_.stFips));
            }
            if (criteria.getFips() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFips(), County_.fips));
            }
            if (criteria.getStateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStateId(), root -> root.join(County_.state, JoinType.LEFT).get(State_.id))
                    );
            }
            if (criteria.getCountiesAvaiableId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountiesAvaiableId(),
                            root -> root.join(County_.countiesAvaiable, JoinType.LEFT).get(CountiesAvaiable_.id)
                        )
                    );
            }
            if (criteria.getCountyRecordId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyRecordId(),
                            root -> root.join(County_.countyRecords, JoinType.LEFT).get(CountyRecord_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
