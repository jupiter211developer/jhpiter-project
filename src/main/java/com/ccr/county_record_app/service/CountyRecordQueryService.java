package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.*; // for static metamodels
import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.repository.CountyRecordRepository;
import com.ccr.county_record_app.service.criteria.CountyRecordCriteria;
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
 * Service for executing complex queries for {@link CountyRecord} entities in the database.
 * The main input is a {@link CountyRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountyRecord} or a {@link Page} of {@link CountyRecord} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountyRecordQueryService extends QueryService<CountyRecord> {

    private final Logger log = LoggerFactory.getLogger(CountyRecordQueryService.class);

    private final CountyRecordRepository countyRecordRepository;

    public CountyRecordQueryService(CountyRecordRepository countyRecordRepository) {
        this.countyRecordRepository = countyRecordRepository;
    }

    /**
     * Return a {@link List} of {@link CountyRecord} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountyRecord> findByCriteria(CountyRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountyRecord> specification = createSpecification(criteria);
        return countyRecordRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CountyRecord} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyRecord> findByCriteria(CountyRecordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountyRecord> specification = createSpecification(criteria);
        return countyRecordRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountyRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountyRecord> specification = createSpecification(criteria);
        return countyRecordRepository.count(specification);
    }

    /**
     * Function to convert {@link CountyRecordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountyRecord> createSpecification(CountyRecordCriteria criteria) {
        Specification<CountyRecord> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CountyRecord_.id));
            }
            if (criteria.getCat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCat(), CountyRecord_.cat));
            }
            if (criteria.getDocNum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocNum(), CountyRecord_.docNum));
            }
            if (criteria.getDocType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocType(), CountyRecord_.docType));
            }
            if (criteria.getBook() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBook(), CountyRecord_.book));
            }
            if (criteria.getSetAbbr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSetAbbr(), CountyRecord_.setAbbr));
            }
            if (criteria.getVol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVol(), CountyRecord_.vol));
            }
            if (criteria.getPg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPg(), CountyRecord_.pg));
            }
            if (criteria.getFiledDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFiledDate(), CountyRecord_.filedDate));
            }
            if (criteria.getEffDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEffDate(), CountyRecord_.effDate));
            }
            if (criteria.getRecordKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecordKey(), CountyRecord_.recordKey));
            }
            if (criteria.getFips() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFips(), CountyRecord_.fips));
            }
            if (criteria.getPdfPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPdfPath(), CountyRecord_.pdfPath));
            }
            if (criteria.getCountyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCountyId(), root -> root.join(CountyRecord_.county, JoinType.LEFT).get(County_.id))
                    );
            }
            if (criteria.getCountyImageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyImageId(),
                            root -> root.join(CountyRecord_.countyImage, JoinType.LEFT).get(CountyImage_.id)
                        )
                    );
            }
            if (criteria.getCountyImagePageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyImagePageId(),
                            root -> root.join(CountyRecord_.countyImagePages, JoinType.LEFT).get(CountyImagePage_.id)
                        )
                    );
            }
            if (criteria.getCountyRecordPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyRecordPartyId(),
                            root -> root.join(CountyRecord_.countyRecordParties, JoinType.LEFT).get(CountyRecordParty_.id)
                        )
                    );
            }
            if (criteria.getCountyRecordLegalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyRecordLegalId(),
                            root -> root.join(CountyRecord_.countyRecordLegals, JoinType.LEFT).get(CountyRecordLegal_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
