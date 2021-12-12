package com.ccr.county_record_app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ccr.county_record_app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountyImagePageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyImagePage.class);
        CountyImagePage countyImagePage1 = new CountyImagePage();
        countyImagePage1.setId(1L);
        CountyImagePage countyImagePage2 = new CountyImagePage();
        countyImagePage2.setId(countyImagePage1.getId());
        assertThat(countyImagePage1).isEqualTo(countyImagePage2);
        countyImagePage2.setId(2L);
        assertThat(countyImagePage1).isNotEqualTo(countyImagePage2);
        countyImagePage1.setId(null);
        assertThat(countyImagePage1).isNotEqualTo(countyImagePage2);
    }
}
