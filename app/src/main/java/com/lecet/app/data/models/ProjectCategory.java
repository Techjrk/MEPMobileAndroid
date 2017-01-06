package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: ProjectCategory Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectCategory extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("projectGroup")
    private ProjectGroup projectGroup;

    @SerializedName("projectGroupId")
    private long projectGroupId;


    public ProjectCategory() {
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ProjectGroup getProjectGroup() {
        return projectGroup;
    }


    public long getProjectGroupId() {
        return projectGroupId;
    }

    @Override
    public String toString() {
        return "ProjectCategory{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", projectGroup=" + projectGroup +
                ", projectGroupId=" + projectGroupId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectCategory that = (ProjectCategory) o;

        if (id != that.id) return false;
        if (projectGroupId != that.projectGroupId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return projectGroup != null ? projectGroup.getId() != that.getProjectGroup().getId() : that.projectGroup == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (projectGroup != null ? projectGroup.hashCode() : 0);
        result = 31 * result + (int) (projectGroupId ^ (projectGroupId >>> 32));
        return result;
    }
}