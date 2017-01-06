package com.lecet.app.data.models;

import java.util.List;

/**
 * Created by getdevsinc on 1/5/17.
 */
    /*
    This class will be used as the main object for search filter jurisdiction section.
     */
public class SearchFilterJurisdictionMain {
    public String name;
    public Object longName;
    public String abbreviation;
    public int id;
    public List<SearchFilterJurisdictionLocal> locals;
    public List<Object> districtCouncils;
    public List<Object> localsWithNoDistrict;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLongName() {
        return longName;
    }

    public void setLongName(Object longName) {
        this.longName = longName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SearchFilterJurisdictionLocal> getLocals() {
        return locals;
    }

    public void setLocals(List<SearchFilterJurisdictionLocal> locals) {
        this.locals = locals;
    }

    public List<Object> getDistrictCouncils() {
        return districtCouncils;
    }

    public void setDistrictCouncils(List<Object> districtCouncils) {
        this.districtCouncils = districtCouncils;
    }

    public List<Object> getLocalsWithNoDistrict() {
        return localsWithNoDistrict;
    }

    public void setLocalsWithNoDistrict(List<Object> localsWithNoDistrict) {
        this.localsWithNoDistrict = localsWithNoDistrict;
    }

}
