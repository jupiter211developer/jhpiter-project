package com.ccr.county_record_app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ccr.county_record_app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountyRecordLegalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyRecordLegal.class);
        CountyRecordLegal countyRecordLegal1 = new CountyRecordLegal();
        countyRecordLegal1.setId(1L);
        CountyRecordLegal countyRecordLegal2 = new CountyRecordLegal();
        countyRecordLegal2.setId(countyRecordLegal1.getId());
        assertThat(countyRecordLegal1).isEqualTo(countyRecordLegal2);
        countyRecordLegal2.setId(2L);
        assertThat(countyRecordLegal1).isNotEqualTo(countyRecordLegal2);
        countyRecordLegal1.setId(null);
        assertThat(countyRecordLegal1).isNotEqualTo(countyRecordLegal2);
    }
}
