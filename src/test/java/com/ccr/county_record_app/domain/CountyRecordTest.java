package com.ccr.county_record_app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ccr.county_record_app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountyRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyRecord.class);
        CountyRecord countyRecord1 = new CountyRecord();
        countyRecord1.setId(1L);
        CountyRecord countyRecord2 = new CountyRecord();
        countyRecord2.setId(countyRecord1.getId());
        assertThat(countyRecord1).isEqualTo(countyRecord2);
        countyRecord2.setId(2L);
        assertThat(countyRecord1).isNotEqualTo(countyRecord2);
        countyRecord1.setId(null);
        assertThat(countyRecord1).isNotEqualTo(countyRecord2);
    }
}
