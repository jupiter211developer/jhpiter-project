package com.ccr.county_record_app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ccr.county_record_app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountyRecordPartyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyRecordParty.class);
        CountyRecordParty countyRecordParty1 = new CountyRecordParty();
        countyRecordParty1.setId(1L);
        CountyRecordParty countyRecordParty2 = new CountyRecordParty();
        countyRecordParty2.setId(countyRecordParty1.getId());
        assertThat(countyRecordParty1).isEqualTo(countyRecordParty2);
        countyRecordParty2.setId(2L);
        assertThat(countyRecordParty1).isNotEqualTo(countyRecordParty2);
        countyRecordParty1.setId(null);
        assertThat(countyRecordParty1).isNotEqualTo(countyRecordParty2);
    }
}
