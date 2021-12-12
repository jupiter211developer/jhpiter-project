package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.*; // for static metamodels
import com.ccr.county_record_app.domain.CountyRecordParty;
import com.ccr.county_record_app.repository.CountyRecordPartyRepository;
import com.ccr.county_record_app.service.criteria.CountyRecordPartyCriteria;
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
 * Service for executing complex queries for {@link CountyRecordParty} entities in the database.
 * The main input is a {@link CountyRecordPartyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountyRecordParty} or a {@link Page} of {@link CountyRecordParty} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountyRecordPartyQueryService extends QueryService<CountyRecordParty> {

    private final Logger log = LoggerFactory.getLogger(CountyRecordPartyQueryService.class);

    private final CountyRecordPartyRepository countyRecordPartyRepository;

    public CountyRecordPartyQueryService(CountyRecordPartyRepository countyRecordPartyRepository) {
        this.countyRecordPartyRepository = countyRecordPartyRepository;
    }

    /**
     * Return a {@link List} of {@link CountyRecordParty} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountyRecordParty> findByCriteria(CountyRecordPartyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountyRecordParty> specification = createSpecification(criteria);
        return countyRecordPartyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CountyRecordParty} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyRecordParty> findByCriteria(CountyRecordPartyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountyRecordParty> specification = createSpecification(criteria);
        return countyRecordPartyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountyRecordPartyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountyRecordParty> specification = createSpecification(criteria);
        return countyRecordPartyRepository.count(specification);
    }

    /**
     * Function to convert {@link CountyRecordPartyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountyRecordParty> createSpecification(CountyRecordPartyCriteria criteria) {
        Specification<CountyRecordParty> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CountyRecordParty_.id));
            }
            if (criteria.getRecordKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecordKey(), CountyRecordParty_.recordKey));
            }
            if (criteria.getPartyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPartyName(), CountyRecordParty_.partyName));
            }
            if (criteria.getPartyRole() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPartyRole(), CountyRecordParty_.partyRole));
            }
            if (criteria.getCountyRecordId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyRecordId(),
                            root -> root.join(CountyRecordParty_.countyRecord, JoinType.LEFT).get(CountyRecord_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
