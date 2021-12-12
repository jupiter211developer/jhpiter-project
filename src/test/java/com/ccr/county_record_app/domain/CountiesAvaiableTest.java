package com.ccr.county_record_app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ccr.county_record_app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountiesAvaiableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountiesAvaiable.class);
        CountiesAvaiable countiesAvaiable1 = new CountiesAvaiable();
        countiesAvaiable1.setId(1L);
        CountiesAvaiable countiesAvaiable2 = new CountiesAvaiable();
        countiesAvaiable2.setId(countiesAvaiable1.getId());
        assertThat(countiesAvaiable1).isEqualTo(countiesAvaiable2);
        countiesAvaiable2.setId(2L);
        assertThat(countiesAvaiable1).isNotEqualTo(countiesAvaiable2);
        countiesAvaiable1.setId(null);
        assertThat(countiesAvaiable1).isNotEqualTo(countiesAvaiable2);
    }
}
