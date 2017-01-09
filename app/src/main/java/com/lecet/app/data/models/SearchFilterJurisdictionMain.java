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
    public String longName;
    public String abbreviation;
    public int id;
    public List<SearchFilterJurisdictionLocal> locals;
    public List<SearchFilterJurisdictionDistrictCouncil> districtCouncils;
    public List<SearchFilterJurisdictionLocal> localsWithNoDistrict;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
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

    public List<SearchFilterJurisdictionDistrictCouncil> getDistrictCouncils() {
        return districtCouncils;
    }

    public void setDistrictCouncils(List<SearchFilterJurisdictionDistrictCouncil> districtCouncils) {
        this.districtCouncils = districtCouncils;
    }

    public List<SearchFilterJurisdictionLocal> getLocalsWithNoDistrict() {
        return localsWithNoDistrict;
    }

    public void setLocalsWithNoDistrict(List<SearchFilterJurisdictionLocal> localsWithNoDistrict) {
        this.localsWithNoDistrict = localsWithNoDistrict;
    }

}
