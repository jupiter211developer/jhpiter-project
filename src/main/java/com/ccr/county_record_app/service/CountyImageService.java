package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.CountyImage;
import com.ccr.county_record_app.repository.CountyImageRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CountyImage}.
 */
@Service
@Transactional
public class CountyImageService {

    private final Logger log = LoggerFactory.getLogger(CountyImageService.class);

    private final CountyImageRepository countyImageRepository;

    public CountyImageService(CountyImageRepository countyImageRepository) {
        this.countyImageRepository = countyImageRepository;
    }

    /**
     * Save a countyImage.
     *
     * @param countyImage the entity to save.
     * @return the persisted entity.
     */
    public CountyImage save(CountyImage countyImage) {
        log.debug("Request to save CountyImage : {}", countyImage);
        return countyImageRepository.save(countyImage);
    }

    /**
     * Partially update a countyImage.
     *
     * @param countyImage the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountyImage> partialUpdate(CountyImage countyImage) {
        log.debug("Request to partially update CountyImage : {}", countyImage);

        return countyImageRepository
            .findById(countyImage.getId())
            .map(existingCountyImage -> {
                if (countyImage.getRecordKey() != null) {
                    existingCountyImage.setRecordKey(countyImage.getRecordKey());
                }
                if (countyImage.getFileSize() != null) {
                    existingCountyImage.setFileSize(countyImage.getFileSize());
                }
                if (countyImage.getFileName() != null) {
                    existingCountyImage.setFileName(countyImage.getFileName());
                }
                if (countyImage.getPageCnt() != null) {
                    existingCountyImage.setPageCnt(countyImage.getPageCnt());
                }
                if (countyImage.getFileDate() != null) {
                    existingCountyImage.setFileDate(countyImage.getFileDate());
                }
                if (countyImage.getFilePath() != null) {
                    existingCountyImage.setFilePath(countyImage.getFilePath());
                }
                if (countyImage.getMd5Hash() != null) {
                    existingCountyImage.setMd5Hash(countyImage.getMd5Hash());
                }

                return existingCountyImage;
            })
            .map(countyImageRepository::save);
    }

    /**
     * Get all the countyImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyImage> findAll(Pageable pageable) {
        log.debug("Request to get all CountyImages");
        return countyImageRepository.findAll(pageable);
    }

    /**
     * Get one countyImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountyImage> findOne(Long id) {
        log.debug("Request to get CountyImage : {}", id);
        return countyImageRepository.findById(id);
    }

    /**
     * Delete the countyImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CountyImage : {}", id);
        countyImageRepository.deleteById(id);
    }
}
