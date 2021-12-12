package com.ccr.county_record_app.service;

import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.repository.CountyRecordRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CountyRecord}.
 */
@Service
@Transactional
public class CountyRecordService {

    protected final Logger log = LoggerFactory.getLogger(CountyRecordService.class);

    private final CountyRecordRepository countyRecordRepository;

    public CountyRecordService(CountyRecordRepository countyRecordRepository) {
        this.countyRecordRepository = countyRecordRepository;
    }

    /**
     * Save a countyRecord.
     *
     * @param countyRecord the entity to save.
     * @return the persisted entity.
     */
    public CountyRecord save(CountyRecord countyRecord) {
        log.debug("Request to save CountyRecord : {}", countyRecord);
        return countyRecordRepository.save(countyRecord);
    }

    /**
     * Partially update a countyRecord.
     *
     * @param countyRecord the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountyRecord> partialUpdate(CountyRecord countyRecord) {
        log.debug("Request to partially update CountyRecord : {}", countyRecord);

        return countyRecordRepository
            .findById(countyRecord.getId())
            .map(existingCountyRecord -> {
                if (countyRecord.getCat() != null) {
                    existingCountyRecord.setCat(countyRecord.getCat());
                }
                if (countyRecord.getDocNum() != null) {
                    existingCountyRecord.setDocNum(countyRecord.getDocNum());
                }
                if (countyRecord.getDocType() != null) {
                    existingCountyRecord.setDocType(countyRecord.getDocType());
                }
                if (countyRecord.getBook() != null) {
                    existingCountyRecord.setBook(countyRecord.getBook());
                }
                if (countyRecord.getSetAbbr() != null) {
                    existingCountyRecord.setSetAbbr(countyRecord.getSetAbbr());
                }
                if (countyRecord.getVol() != null) {
                    existingCountyRecord.setVol(countyRecord.getVol());
                }
                if (countyRecord.getPg() != null) {
                    existingCountyRecord.setPg(countyRecord.getPg());
                }
                if (countyRecord.getFiledDate() != null) {
                    existingCountyRecord.setFiledDate(countyRecord.getFiledDate());
                }
                if (countyRecord.getEffDate() != null) {
                    existingCountyRecord.setEffDate(countyRecord.getEffDate());
                }
                if (countyRecord.getRecordKey() != null) {
                    existingCountyRecord.setRecordKey(countyRecord.getRecordKey());
                }
                if (countyRecord.getFips() != null) {
                    existingCountyRecord.setFips(countyRecord.getFips());
                }
                if (countyRecord.getPdfPath() != null) {
                    existingCountyRecord.setPdfPath(countyRecord.getPdfPath());
                }

                return existingCountyRecord;
            })
            .map(countyRecordRepository::save);
    }

    /**
     * Get all the countyRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyRecord> findAll(Pageable pageable) {
        log.debug("Request to get all CountyRecords");
        return countyRecordRepository.findAll(pageable);
    }

    /**
     *  Get all the countyRecords where CountyImage is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CountyRecord> findAllWhereCountyImageIsNull() {
        log.debug("Request to get all countyRecords where CountyImage is null");
        return StreamSupport
            .stream(countyRecordRepository.findAll().spliterator(), false)
            .filter(countyRecord -> countyRecord.getCountyImage() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one countyRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountyRecord> findOne(Long id) {
        log.debug("Request to get CountyRecord : {}", id);
        return countyRecordRepository.findById(id);
    }

    /**
     * Delete the countyRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CountyRecord : {}", id);
        countyRecordRepository.deleteById(id);
    }
}
