package com.ccr.county_record_app.repository;

import com.ccr.county_record_app.domain.CountiesAvaiable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CountiesAvaiable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountiesAvaiableRepository extends JpaRepository<CountiesAvaiable, Long>, JpaSpecificationExecutor<CountiesAvaiable> {}
