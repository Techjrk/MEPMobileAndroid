package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: Region Created: 1/13/17 Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class Region {

    @SerializedName("name")
    private String name;

    @SerializedName("longName")

    private String longName;

    @SerializedName("abbreviation")
    private String abbreviation;

    @SerializedName("id")
    private long id;

    public Region(){}

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

    @Override
    public String toString() {
        return "Region{" +
                "name='" + name + '\'' +
                ", longName='" + longName + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Region)) return false;

        Region region = (Region) o;

        if (id != region.id) return false;
        if (name != null ? !name.equals(region.name) : region.name != null) return false;
        if (longName != null ? !longName.equals(region.longName) : region.longName != null)
            return false;
        return abbreviation != null ? abbreviation.equals(region.abbreviation) : region.abbreviation == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (longName != null ? longName.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
