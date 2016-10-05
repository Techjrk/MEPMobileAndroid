package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: PrimaryProjectType Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class PrimaryProjectType {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("projectCategoryId")
    private long projectCategoryId;

    @SerializedName("buildingOrHighway")
    private String buildingOrHighway;

    @SerializedName("projectCategory")
    private ProjectCategory projectCategory;


    public PrimaryProjectType() {
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getProjectCategoryId() {
        return projectCategoryId;
    }

    public String getBuildingOrHighway() {
        return buildingOrHighway;
    }

    public void setBuildingOrHighway(String buildingOrHighway) {
        this.buildingOrHighway = buildingOrHighway;
    }

    public ProjectCategory getProjectCategory() {
        return projectCategory;
    }

    @Override
    public String toString() {
        return "PrimaryProjectType{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", projectCategoryId=" + projectCategoryId +
                ", buildingOrHighway='" + buildingOrHighway + '\'' +
                ", projectCategory=" + projectCategory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrimaryProjectType that = (PrimaryProjectType) o;

        if (id != that.id) return false;
        if (projectCategoryId != that.projectCategoryId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (buildingOrHighway != null ? !buildingOrHighway.equals(that.buildingOrHighway) : that.buildingOrHighway != null)
            return false;
        return projectCategory != null ? projectCategory.equals(that.projectCategory) : that.projectCategory == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (int) (projectCategoryId ^ (projectCategoryId >>> 32));
        result = 31 * result + (buildingOrHighway != null ? buildingOrHighway.hashCode() : 0);
        result = 31 * result + (projectCategory != null ? projectCategory.hashCode() : 0);
        return result;
    }
}
