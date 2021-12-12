package com.ccr.county_record_app.web.rest.ext;

import com.ccr.county_record_app.domain.*;
import com.ccr.county_record_app.repository.CountyRecordRepository;
import com.ccr.county_record_app.service.criteria.CountyRecordCriteria;
import java.text.MessageFormat;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.SetJoin;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class RealPropRecordService extends QueryService<CountyRecord> {

    private final CountyRecordRepository countyRecordRepository;

    public RealPropRecordService(CountyRecordRepository countyRecordRepository) {
        this.countyRecordRepository = countyRecordRepository;
    }

    public static Specification<CountyRecord> docNumberContains(String expression) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("docNum"), contains(expression)));
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

    protected Specification<CountyRecord> createSpecification(CountyRecordCriteria criteria) {
        Specification<CountyRecord> specification = Specification.where(null);

        specification =
            specification.and(
                buildSpecification(
                    criteria.getCountyRecordPartyId(),
                    root -> root.join(CountyRecord_.countyRecordParties).get(CountyRecordParty_.id)
                )
            );

        return specification;
    }

    public List<CountyRecord> getRealPropRec(String docNo) {
        System.out.println("getRealPropRec");
        return this.countyRecordRepository.findAll(docNumberContains(docNo));
    }
}
