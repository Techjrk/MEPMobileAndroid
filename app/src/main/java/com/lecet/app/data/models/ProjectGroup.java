package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * File: ProjectGroup Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */


public class ProjectGroup {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    public ProjectGroup() {
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "ProjectGroup{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectGroup that = (ProjectGroup) o;

        if (id != that.id) return false;
        return title != null ? title.equals(that.title) : that.title == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}

