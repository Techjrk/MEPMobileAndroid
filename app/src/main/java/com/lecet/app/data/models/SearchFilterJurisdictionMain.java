package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by getdevsinc on 1/5/17.
 */
    /*
    This class will be used as the main object for search filter jurisdiction section.
     */
public class SearchFilterJurisdictionMain extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("longName")
    private String longName;

    @SerializedName("abbreviation")
    private String abbreviation;

    @SerializedName("locals")
    private RealmList<SearchFilterJurisdictionLocal> locals;

    @SerializedName("districtCouncils")
    private RealmList<SearchFilterJurisdictionDistrictCouncil> districtCouncils;

    @SerializedName("localsWithNoDistrict")
    private RealmList<SearchFilterJurisdictionLocal> localsWithNoDistrict;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public RealmList<SearchFilterJurisdictionLocal> getLocals() {
        return locals;
    }

    public void setLocals(RealmList<SearchFilterJurisdictionLocal> locals) {
        this.locals = locals;
    }

    public RealmList<SearchFilterJurisdictionDistrictCouncil> getDistrictCouncils() {
        return districtCouncils;
    }

    public void setDistrictCouncils(RealmList<SearchFilterJurisdictionDistrictCouncil> districtCouncils) {
        this.districtCouncils = districtCouncils;
    }

    public RealmList<SearchFilterJurisdictionLocal> getLocalsWithNoDistrict() {
        return localsWithNoDistrict;
    }

    public void setLocalsWithNoDistrict(RealmList<SearchFilterJurisdictionLocal> localsWithNoDistrict) {
        this.localsWithNoDistrict = localsWithNoDistrict;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilterJurisdictionMain that = (SearchFilterJurisdictionMain) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (longName != null ? !longName.equals(that.longName) : that.longName != null)
            return false;
        if (abbreviation != null ? !abbreviation.equals(that.abbreviation) : that.abbreviation != null)
            return false;
        if (locals != null ? !locals.equals(that.locals) : that.locals != null) return false;
        if (districtCouncils != null ? !districtCouncils.equals(that.districtCouncils) : that.districtCouncils != null)
            return false;
        return localsWithNoDistrict != null ? localsWithNoDistrict.equals(that.localsWithNoDistrict) : that.localsWithNoDistrict == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (longName != null ? longName.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (locals != null ? locals.hashCode() : 0);
        result = 31 * result + (districtCouncils != null ? districtCouncils.hashCode() : 0);
        result = 31 * result + (localsWithNoDistrict != null ? localsWithNoDistrict.hashCode() : 0);
        return result;
    }
}
