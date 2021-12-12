package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.*; // for static metamodels
import com.ccr.county_record_app.domain.CountyRecordLegal;
import com.ccr.county_record_app.repository.CountyRecordLegalRepository;
import com.ccr.county_record_app.service.criteria.CountyRecordLegalCriteria;
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
 * Service for executing complex queries for {@link CountyRecordLegal} entities in the database.
 * The main input is a {@link CountyRecordLegalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountyRecordLegal} or a {@link Page} of {@link CountyRecordLegal} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountyRecordLegalQueryService extends QueryService<CountyRecordLegal> {

    private final Logger log = LoggerFactory.getLogger(CountyRecordLegalQueryService.class);

    private final CountyRecordLegalRepository countyRecordLegalRepository;

    public CountyRecordLegalQueryService(CountyRecordLegalRepository countyRecordLegalRepository) {
        this.countyRecordLegalRepository = countyRecordLegalRepository;
    }

    /**
     * Return a {@link List} of {@link CountyRecordLegal} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountyRecordLegal> findByCriteria(CountyRecordLegalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountyRecordLegal> specification = createSpecification(criteria);
        return countyRecordLegalRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CountyRecordLegal} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyRecordLegal> findByCriteria(CountyRecordLegalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountyRecordLegal> specification = createSpecification(criteria);
        return countyRecordLegalRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountyRecordLegalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountyRecordLegal> specification = createSpecification(criteria);
        return countyRecordLegalRepository.count(specification);
    }

    /**
     * Function to convert {@link CountyRecordLegalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountyRecordLegal> createSpecification(CountyRecordLegalCriteria criteria) {
        Specification<CountyRecordLegal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CountyRecordLegal_.id));
            }
            if (criteria.getLegal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLegal(), CountyRecordLegal_.legal));
            }
            if (criteria.getRecordKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecordKey(), CountyRecordLegal_.recordKey));
            }
            if (criteria.getCountyRecordId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyRecordId(),
                            root -> root.join(CountyRecordLegal_.countyRecord, JoinType.LEFT).get(CountyRecord_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
