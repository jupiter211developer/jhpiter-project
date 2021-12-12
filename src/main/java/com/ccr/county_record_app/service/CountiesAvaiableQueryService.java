package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.*; // for static metamodels
import com.ccr.county_record_app.domain.CountiesAvaiable;
import com.ccr.county_record_app.repository.CountiesAvaiableRepository;
import com.ccr.county_record_app.service.criteria.CountiesAvaiableCriteria;
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
 * Service for executing complex queries for {@link CountiesAvaiable} entities in the database.
 * The main input is a {@link CountiesAvaiableCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountiesAvaiable} or a {@link Page} of {@link CountiesAvaiable} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountiesAvaiableQueryService extends QueryService<CountiesAvaiable> {

    private final Logger log = LoggerFactory.getLogger(CountiesAvaiableQueryService.class);

    private final CountiesAvaiableRepository countiesAvaiableRepository;

    public CountiesAvaiableQueryService(CountiesAvaiableRepository countiesAvaiableRepository) {
        this.countiesAvaiableRepository = countiesAvaiableRepository;
    }

    /**
     * Return a {@link List} of {@link CountiesAvaiable} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountiesAvaiable> findByCriteria(CountiesAvaiableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountiesAvaiable> specification = createSpecification(criteria);
        return countiesAvaiableRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CountiesAvaiable} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountiesAvaiable> findByCriteria(CountiesAvaiableCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountiesAvaiable> specification = createSpecification(criteria);
        return countiesAvaiableRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountiesAvaiableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountiesAvaiable> specification = createSpecification(criteria);
        return countiesAvaiableRepository.count(specification);
    }

    /**
     * Function to convert {@link CountiesAvaiableCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountiesAvaiable> createSpecification(CountiesAvaiableCriteria criteria) {
        Specification<CountiesAvaiable> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CountiesAvaiable_.id));
            }
            if (criteria.getEarliest() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEarliest(), CountiesAvaiable_.earliest));
            }
            if (criteria.getLatest() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatest(), CountiesAvaiable_.latest));
            }
            if (criteria.getRecordCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRecordCount(), CountiesAvaiable_.recordCount));
            }
            if (criteria.getFips() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFips(), CountiesAvaiable_.fips));
            }
            if (criteria.getCountyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyName(), CountiesAvaiable_.countyName));
            }
            if (criteria.getStateAbbr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateAbbr(), CountiesAvaiable_.stateAbbr));
            }
            if (criteria.getCountyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyId(),
                            root -> root.join(CountiesAvaiable_.county, JoinType.LEFT).get(County_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
