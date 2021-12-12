package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.CountyImagePage;
import com.ccr.county_record_app.repository.CountyImagePageRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CountyImagePage}.
 */
@Service
@Transactional
public class CountyImagePageService {

    private final Logger log = LoggerFactory.getLogger(CountyImagePageService.class);

    private final CountyImagePageRepository countyImagePageRepository;

    public CountyImagePageService(CountyImagePageRepository countyImagePageRepository) {
        this.countyImagePageRepository = countyImagePageRepository;
    }

    /**
     * Save a countyImagePage.
     *
     * @param countyImagePage the entity to save.
     * @return the persisted entity.
     */
    public CountyImagePage save(CountyImagePage countyImagePage) {
        log.debug("Request to save CountyImagePage : {}", countyImagePage);
        return countyImagePageRepository.save(countyImagePage);
    }

    /**
     * Partially update a countyImagePage.
     *
     * @param countyImagePage the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountyImagePage> partialUpdate(CountyImagePage countyImagePage) {
        log.debug("Request to partially update CountyImagePage : {}", countyImagePage);

        return countyImagePageRepository
            .findById(countyImagePage.getId())
            .map(existingCountyImagePage -> {
                if (countyImagePage.getRecordKey() != null) {
                    existingCountyImagePage.setRecordKey(countyImagePage.getRecordKey());
                }
                if (countyImagePage.getFileSize() != null) {
                    existingCountyImagePage.setFileSize(countyImagePage.getFileSize());
                }
                if (countyImagePage.getPageNo() != null) {
                    existingCountyImagePage.setPageNo(countyImagePage.getPageNo());
                }
                if (countyImagePage.getFileName() != null) {
                    existingCountyImagePage.setFileName(countyImagePage.getFileName());
                }
                if (countyImagePage.getFileDate() != null) {
                    existingCountyImagePage.setFileDate(countyImagePage.getFileDate());
                }
                if (countyImagePage.getFilePath() != null) {
                    existingCountyImagePage.setFilePath(countyImagePage.getFilePath());
                }
                if (countyImagePage.getOcrScore() != null) {
                    existingCountyImagePage.setOcrScore(countyImagePage.getOcrScore());
                }
                if (countyImagePage.getMd5Hash() != null) {
                    existingCountyImagePage.setMd5Hash(countyImagePage.getMd5Hash());
                }

                return existingCountyImagePage;
            })
            .map(countyImagePageRepository::save);
    }

    /**
     * Get all the countyImagePages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyImagePage> findAll(Pageable pageable) {
        log.debug("Request to get all CountyImagePages");
        return countyImagePageRepository.findAll(pageable);
    }

    /**
     * Get one countyImagePage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountyImagePage> findOne(Long id) {
        log.debug("Request to get CountyImagePage : {}", id);
        return countyImagePageRepository.findById(id);
    }

    /**
     * Delete the countyImagePage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CountyImagePage : {}", id);
        countyImagePageRepository.deleteById(id);
    }
}
