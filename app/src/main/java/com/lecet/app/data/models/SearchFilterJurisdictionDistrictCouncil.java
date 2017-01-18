package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by getdevsinc on 1/5/17.
 */
    /*
    This class will be used as the district council object for search filter jurisdiction section.
     */
public class SearchFilterJurisdictionDistrictCouncil extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("abbreviation")
    private String abbreviation;

    @SerializedName("regionId")
    private int regionId;

    @SerializedName("locals")
    private RealmList<SearchFilterJurisdictionLocal> locals;

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public RealmList<SearchFilterJurisdictionLocal> getLocals() {
        return locals;
    }

    public void setLocals(RealmList<SearchFilterJurisdictionLocal> locals) {
        this.locals = locals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilterJurisdictionDistrictCouncil that = (SearchFilterJurisdictionDistrictCouncil) o;

        if (id != that.id) return false;
        if (regionId != that.regionId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (abbreviation != null ? !abbreviation.equals(that.abbreviation) : that.abbreviation != null)
            return false;
        return locals != null ? locals.equals(that.locals) : that.locals == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + regionId;
        result = 31 * result + (locals != null ? locals.hashCode() : 0);
        return result;
    }
}
