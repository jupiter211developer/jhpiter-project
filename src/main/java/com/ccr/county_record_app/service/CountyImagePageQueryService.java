package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.*; // for static metamodels
import com.ccr.county_record_app.domain.CountyImagePage;
import com.ccr.county_record_app.repository.CountyImagePageRepository;
import com.ccr.county_record_app.service.criteria.CountyImagePageCriteria;
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
 * Service for executing complex queries for {@link CountyImagePage} entities in the database.
 * The main input is a {@link CountyImagePageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountyImagePage} or a {@link Page} of {@link CountyImagePage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountyImagePageQueryService extends QueryService<CountyImagePage> {

    private final Logger log = LoggerFactory.getLogger(CountyImagePageQueryService.class);

    private final CountyImagePageRepository countyImagePageRepository;

    public CountyImagePageQueryService(CountyImagePageRepository countyImagePageRepository) {
        this.countyImagePageRepository = countyImagePageRepository;
    }

    /**
     * Return a {@link List} of {@link CountyImagePage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountyImagePage> findByCriteria(CountyImagePageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountyImagePage> specification = createSpecification(criteria);
        return countyImagePageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CountyImagePage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyImagePage> findByCriteria(CountyImagePageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountyImagePage> specification = createSpecification(criteria);
        return countyImagePageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountyImagePageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountyImagePage> specification = createSpecification(criteria);
        return countyImagePageRepository.count(specification);
    }

    /**
     * Function to convert {@link CountyImagePageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountyImagePage> createSpecification(CountyImagePageCriteria criteria) {
        Specification<CountyImagePage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CountyImagePage_.id));
            }
            if (criteria.getRecordKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecordKey(), CountyImagePage_.recordKey));
            }
            if (criteria.getFileSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileSize(), CountyImagePage_.fileSize));
            }
            if (criteria.getPageNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageNo(), CountyImagePage_.pageNo));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), CountyImagePage_.fileName));
            }
            if (criteria.getFileDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileDate(), CountyImagePage_.fileDate));
            }
            if (criteria.getFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilePath(), CountyImagePage_.filePath));
            }
            if (criteria.getOcrScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOcrScore(), CountyImagePage_.ocrScore));
            }
            if (criteria.getMd5Hash() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMd5Hash(), CountyImagePage_.md5Hash));
            }
            if (criteria.getCountyRecordId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyRecordId(),
                            root -> root.join(CountyImagePage_.countyRecord, JoinType.LEFT).get(CountyRecord_.id)
                        )
                    );
            }
            if (criteria.getCountyImageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyImageId(),
                            root -> root.join(CountyImagePage_.countyImage, JoinType.LEFT).get(CountyImage_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
