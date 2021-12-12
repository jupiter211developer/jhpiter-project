package com.ccr.county_record_app.web.rest.ext;

import com.ccr.county_record_app.domain.CountyRecord;
import com.ccr.county_record_app.repository.CountyRecordRepository;
import com.ccr.county_record_app.service.CountyRecordQueryService;
import com.ccr.county_record_app.service.CountyRecordService;
import com.ccr.county_record_app.service.criteria.CountyRecordCriteria;
import com.ccr.county_record_app.web.rest.CountyRecordResource;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CountyRecordResourceExt {

    private final Logger log = LoggerFactory.getLogger(CountyRecordResource.class);
    private final RealPropRecordService realPropRecordService;
    private final CountyRecordService countyRecordService;

    private final CountyRecordRepository countyRecordRepository;

    private final CountyRecordQueryService countyRecordQueryService;

    public CountyRecordResourceExt(
        RealPropRecordService realPropRecordService,
        CountyRecordService countyRecordService,
        CountyRecordRepository countyRecordRepository,
        CountyRecordQueryService countyRecordQueryService
    ) {
        this.realPropRecordService = realPropRecordService;
        this.countyRecordService = countyRecordService;
        this.countyRecordRepository = countyRecordRepository;
        this.countyRecordQueryService = countyRecordQueryService;
    }

    //    /**
    //     * {@code GET  /county-records} : get all the countyRecords.
    //     *
    //     * @param pageable the pagination information.
    //     * @param criteria the criteria which the requested entities should match.
    //     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countyRecords in body.
    //     */
    //    @PostMapping("/real_property_records")
    //    public ResponseEntity<List<CountyRecord>> getMatchingRealPropertyRecords(@Valid @RequestBody RealPropFormValues realPropVals) throws URISyntaxException {
    //        log.debug("REST request to get matching CountyRecords : {}");
    ////        List<CountyRecord> r = this.
    //        return ResponseEntity.ok().body(this.realPropRecordService.getRealPropRec(realPropVals.docNumber));
    //    }
    /**
     * {@code GET  /county-records} : get all the countyRecords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countyRecords in body.
     */
    @PostMapping("/real_property_records")
    public ResponseEntity<List<CountyRecord>> getMatchingRealPropertyRecords(CountyRecordCriteria criteria) {
        log.debug("REST request to get matching CountyRecords : {}");
        //        CountyRecordCriteria criteria =  new CountyRecordCriteria();
        List<CountyRecord> list = this.countyRecordQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(list);
    }
    //    public ResponseEntity<List<CountyRecord>> getAllCountyRecords(CountyRecordCriteria criteria, Pageable pageable) {
    //        log.debug("REST request to get CountyRecords by criteria: {}", criteria);
    //        Page<CountyRecord> page = this.countyRecordQueryService.findByCriteria(criteria, pageable);
    //        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    //        return ResponseEntity.ok().headers(headers).body(page.getContent());
    //    }
}
