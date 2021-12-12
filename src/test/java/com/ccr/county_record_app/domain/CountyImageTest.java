package com.ccr.county_record_app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ccr.county_record_app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountyImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyImage.class);
        CountyImage countyImage1 = new CountyImage();
        countyImage1.setId(1L);
        CountyImage countyImage2 = new CountyImage();
        countyImage2.setId(countyImage1.getId());
        assertThat(countyImage1).isEqualTo(countyImage2);
        countyImage2.setId(2L);
        assertThat(countyImage1).isNotEqualTo(countyImage2);
        countyImage1.setId(null);
        assertThat(countyImage1).isNotEqualTo(countyImage2);
    }
}
