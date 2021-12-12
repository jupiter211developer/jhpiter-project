package com.ccr.county_record_app.web.rest.ext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Entity;

public class RealPropFormValues implements Serializable {

    public ArrayList<RealPropFormPartyValues> partyInputs;
    public String docNumber;
    public String bk;
    public String vol;
    public String pg;
    public String datePredicate;
    public Date fileDateMin;
    public Date fileDateMax;
    public String lglInputs;
    public Integer countyId;
}
