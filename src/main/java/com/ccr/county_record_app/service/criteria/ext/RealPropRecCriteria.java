package com.ccr.county_record_app.service.criteria.ext;

import com.ccr.county_record_app.service.criteria.CountyRecordCriteria;
import com.ccr.county_record_app.service.criteria.CountyRecordPartyCriteria;
import java.util.ArrayList;

public class RealPropRecCriteria extends CountyRecordCriteria {

    private ArrayList<CountyRecordPartyCriteria> partyCriteria;

    public RealPropRecCriteria(CountyRecordCriteria other, ArrayList<CountyRecordPartyCriteria> partyCriteria) {
        super(other);
        this.partyCriteria = partyCriteria;
    }
}
