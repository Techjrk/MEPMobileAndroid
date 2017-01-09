package com.lecet.app.data.models;

import java.util.List;

/**
 * Created by getdevsinc on 1/5/17.
 */
    /*
    This class will be used as the district council object for search filter jurisdiction section.
     */
public class SearchFilterJurisdictionDistrictCouncil {
    public String name;
    public String abbreviation;
    public int id;
    public int regionId;
    public List<SearchFilterJurisdictionLocal> locals;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public List<SearchFilterJurisdictionLocal> getLocals() {
        return locals;
    }

    public void setLocals(List<SearchFilterJurisdictionLocal> locals) {
        this.locals = locals;
    }

}
