package com.ccr.county_record_app.repository;

import com.ccr.county_record_app.domain.CountyImagePage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CountyImagePage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountyImagePageRepository extends JpaRepository<CountyImagePage, Long>, JpaSpecificationExecutor<CountyImagePage> {}
