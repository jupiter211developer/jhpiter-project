package com.ccr.county_record_app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ccr.county_record_app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(County.class);
        County county1 = new County();
        county1.setId(1L);
        County county2 = new County();
        county2.setId(county1.getId());
        assertThat(county1).isEqualTo(county2);
        county2.setId(2L);
        assertThat(county1).isNotEqualTo(county2);
        county1.setId(null);
        assertThat(county1).isNotEqualTo(county2);
    }
}
