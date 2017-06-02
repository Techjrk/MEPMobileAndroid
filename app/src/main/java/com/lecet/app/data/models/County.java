package com.lecet.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jasonm on 5/30/17.
 */

public class County extends RealmObject {

    @SerializedName("countyName")
    @Expose
    private String countyName;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("fipsCountyId")
    @Expose
    private String fipsCountyId;

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFipsCountyId() {
        return fipsCountyId;
    }

    public void setFipsCountyId(String fipsCountyId) {
        this.fipsCountyId = fipsCountyId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "County{" +
                "countyName='" + countyName + '\'' +
                ", state='" + state + '\'' +
                ", fipsCountyId='" + fipsCountyId + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", displayName='" + displayName + '\'' +
                ", id=" + id +
                '}';
    }


}
