package com.ccr.county_record_app.repository;

import com.ccr.county_record_app.domain.CountyRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CountyRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountyRecordRepository extends JpaRepository<CountyRecord, Long>, JpaSpecificationExecutor<CountyRecord> {}
