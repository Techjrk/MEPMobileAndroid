package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by getdevsinc on 1/5/17.
 */
    /*
    This class will be used as the main object for search filter jurisdiction section.
     */
public class SearchFilterJurisdictionNoDistrictCouncil extends RealmObject {

    @SerializedName("name")
    private String name;

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("districtCouncilId")
    private String districtCouncilId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistrictCouncilId() {
        return districtCouncilId;
    }

    public void setDistrictCouncilId(String districtCouncilId) {
        this.districtCouncilId = districtCouncilId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilterJurisdictionNoDistrictCouncil that = (SearchFilterJurisdictionNoDistrictCouncil) o;

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

    @Override
    public String toString() {
        return "SearchFilterJurisdictionNoDistrictCouncil{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", districtCouncilId='" + districtCouncilId + '\'' +
                '}';
    }
}
