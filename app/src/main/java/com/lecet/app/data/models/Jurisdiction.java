package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * File: Jurisdiction Created: 1/13/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class Jurisdiction {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private long id;

    @SerializedName("districtCouncilId")
    private long districtCouncilId;

    @SerializedName("districtCouncil")
    private DistrictCouncil districtCouncil;

    @SerializedName("regions")
    private List<Region> regions;

    public Jurisdiction(){}

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public long getDistrictCouncilId() {
        return districtCouncilId;
    }

    public DistrictCouncil getDistrictCouncil() {
        return districtCouncil;
    }

    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public String toString() {
        return "Jurisdiction{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", districtCouncilId=" + districtCouncilId +
                ", districtCouncil=" + districtCouncil +
                ", regions=" + regions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jurisdiction)) return false;

        Jurisdiction that = (Jurisdiction) o;

        if (id != that.id) return false;
        if (districtCouncilId != that.districtCouncilId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (districtCouncil != null ? !districtCouncil.equals(that.districtCouncil) : that.districtCouncil != null)
            return false;
        return regions != null ? regions.equals(that.regions) : that.regions == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (districtCouncilId ^ (districtCouncilId >>> 32));
        result = 31 * result + (districtCouncil != null ? districtCouncil.hashCode() : 0);
        result = 31 * result + (regions != null ? regions.hashCode() : 0);
        return result;
    }
}
