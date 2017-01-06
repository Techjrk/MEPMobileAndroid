package com.lecet.app.data.models;

/**
 * Created by getdevsinc on 1/5/17.
 */
    /*
    This class will be used as the local object for search filter jurisdiction section.
     */
public class SearchFilterJurisdictionLocal {
    public String name;
    public int id;
    public Integer districtCouncilId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDistrictCouncilId() {
        return districtCouncilId;
    }

    public void setDistrictCouncilId(Integer districtCouncilId) {
        this.districtCouncilId = districtCouncilId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
