package com.ccr.county_record_app.repository;

import com.ccr.county_record_app.domain.CountyRecordParty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CountyRecordParty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountyRecordPartyRepository extends JpaRepository<CountyRecordParty, Long>, JpaSpecificationExecutor<CountyRecordParty> {}
