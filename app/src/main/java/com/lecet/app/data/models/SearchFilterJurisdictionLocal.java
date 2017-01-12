package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by getdevsinc on 1/5/17.
 */
    /*
    This class will be used as the local object for search filter jurisdiction section.
     */
public class SearchFilterJurisdictionLocal {
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("districtCouncilId")
    private Integer districtCouncilId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilterJurisdictionLocal that = (SearchFilterJurisdictionLocal) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return districtCouncilId != null ? districtCouncilId.equals(that.districtCouncilId) : that.districtCouncilId == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + (districtCouncilId != null ? districtCouncilId.hashCode() : 0);
        return result;
    }
}
