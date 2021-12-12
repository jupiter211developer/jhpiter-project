package com.ccr.county_record_app.repository;

import com.ccr.county_record_app.domain.CountyRecordLegal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CountyRecordLegal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountyRecordLegalRepository extends JpaRepository<CountyRecordLegal, Long>, JpaSpecificationExecutor<CountyRecordLegal> {}
