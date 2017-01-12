package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: ProjectCategory Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class SearchFilterProjectTypesProjectCategory {

    @SerializedName("title")
    private String title;

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("projectGroupId")
    private long projectGroupId;

    @SerializedName("projectTypes")
    public List<PrimaryProjectType> projectTypes;

    public SearchFilterProjectTypesProjectCategory() {
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public long getProjectGroupId() {
        return projectGroupId;
    }

    public List<PrimaryProjectType> getProjectTypes() {
        return projectTypes;
    }

    @Override
    public String toString() {
        return "ProjectCategory{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", projectGroupId=" + projectGroupId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilterProjectTypesProjectCategory that = (SearchFilterProjectTypesProjectCategory) o;

        if (id != that.id) return false;
        if (projectGroupId != that.projectGroupId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return projectTypes != null ? projectTypes.equals(that.projectTypes) : that.projectTypes == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (projectTypes != null ? projectTypes.hashCode() : 0);
        result = 31 * result + (int) (projectGroupId ^ (projectGroupId >>> 32));
        return result;
    }
}