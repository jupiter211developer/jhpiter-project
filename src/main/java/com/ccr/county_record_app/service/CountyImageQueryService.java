package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.*; // for static metamodels
import com.ccr.county_record_app.domain.CountyImage;
import com.ccr.county_record_app.repository.CountyImageRepository;
import com.ccr.county_record_app.service.criteria.CountyImageCriteria;
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
 * Service for executing complex queries for {@link CountyImage} entities in the database.
 * The main input is a {@link CountyImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountyImage} or a {@link Page} of {@link CountyImage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountyImageQueryService extends QueryService<CountyImage> {

    private final Logger log = LoggerFactory.getLogger(CountyImageQueryService.class);

    private final CountyImageRepository countyImageRepository;

    public CountyImageQueryService(CountyImageRepository countyImageRepository) {
        this.countyImageRepository = countyImageRepository;
    }

    /**
     * Return a {@link List} of {@link CountyImage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountyImage> findByCriteria(CountyImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountyImage> specification = createSpecification(criteria);
        return countyImageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CountyImage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyImage> findByCriteria(CountyImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountyImage> specification = createSpecification(criteria);
        return countyImageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountyImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountyImage> specification = createSpecification(criteria);
        return countyImageRepository.count(specification);
    }

    /**
     * Function to convert {@link CountyImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountyImage> createSpecification(CountyImageCriteria criteria) {
        Specification<CountyImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CountyImage_.id));
            }
            if (criteria.getRecordKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecordKey(), CountyImage_.recordKey));
            }
            if (criteria.getFileSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileSize(), CountyImage_.fileSize));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), CountyImage_.fileName));
            }
            if (criteria.getPageCnt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageCnt(), CountyImage_.pageCnt));
            }
            if (criteria.getFileDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileDate(), CountyImage_.fileDate));
            }
            if (criteria.getFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilePath(), CountyImage_.filePath));
            }
            if (criteria.getMd5Hash() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMd5Hash(), CountyImage_.md5Hash));
            }
            if (criteria.getCountyRecordId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyRecordId(),
                            root -> root.join(CountyImage_.countyRecord, JoinType.LEFT).get(CountyRecord_.id)
                        )
                    );
            }
            if (criteria.getCountyImagePageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyImagePageId(),
                            root -> root.join(CountyImage_.countyImagePages, JoinType.LEFT).get(CountyImagePage_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
