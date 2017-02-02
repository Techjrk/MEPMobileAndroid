package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import com.lecet.app.interfaces.TrackingListObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: ProjectTrackingList Created: 11/3/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectTrackingList extends RealmObject implements TrackingListObject {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("userId")
    private long userId;

    @SerializedName("projects")
    private RealmList<Project> projects;

    public ProjectTrackingList(){}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public RealmList<Project> getProjects() {
        return projects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectTrackingList)) return false;

        ProjectTrackingList that = (ProjectTrackingList) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return projects != null ? projects.equals(that.projects) : that.projects == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (projects != null ? projects.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectTrackingList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", projects=" + projects +
                '}';
    }
}
