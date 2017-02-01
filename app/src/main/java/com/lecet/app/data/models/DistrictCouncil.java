package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: DistrictCouncil Created: 1/13/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class DistrictCouncil {

    @SerializedName("name")
    private String name;

    @SerializedName("abbreviation")
    private String abbreviation;

    @SerializedName("id")
    private long id;

    @SerializedName("regionId")
    private long regionId;

    public DistrictCouncil(){}

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public long getId() {
        return id;
    }

    public long getRegionId() {
        return regionId;
    }

    @Override
    public String toString() {
        return "DistrictCouncil{" +
                "name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", id=" + id +
                ", regionId=" + regionId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DistrictCouncil)) return false;

        DistrictCouncil that = (DistrictCouncil) o;

        if (id != that.id) return false;
        if (regionId != that.regionId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return abbreviation != null ? abbreviation.equals(that.abbreviation) : that.abbreviation == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (regionId ^ (regionId >>> 32));
        return result;
    }
}
