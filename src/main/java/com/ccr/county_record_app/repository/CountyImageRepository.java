package com.ccr.county_record_app.repository;

import com.ccr.county_record_app.domain.CountyImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CountyImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountyImageRepository extends JpaRepository<CountyImage, Long>, JpaSpecificationExecutor<CountyImage> {}
