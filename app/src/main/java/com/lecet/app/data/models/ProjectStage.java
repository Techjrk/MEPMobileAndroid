package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: ProjectStage Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectStage {

    @SerializedName("id")
    private long id;

    @SerializedName("parentId")
    private long parentId;

    @SerializedName("name")
    private String name;


    public ProjectStage() {
    }

    public long getId() {
        return id;
    }

    public long getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ProjectStage{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectStage that = (ProjectStage) o;

        if (id != that.id) return false;
        if (parentId != that.parentId) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (parentId ^ (parentId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
