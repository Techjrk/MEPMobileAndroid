package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: ProjectType Created: 1/17/17 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectType extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("parentId")
    private long parentId;

    @SerializedName("name")
    private String name;

    @SerializedName("childTypes")
    public RealmList<ProjectType> childTypes = new RealmList<>();

    public ProjectType() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<ProjectType> getChildTypes() {
        return childTypes;
    }

    public void setChildTypes(RealmList<ProjectType> childTypes) {
        this.childTypes = childTypes;
    }

    public void addChildType(ProjectType childType) {
        this.childTypes.add(childType);
    }

    /**
     * Helper for toString() output of child IDs
     */
    private String getChildTypeIds() {
        ArrayList<Long> arr = new ArrayList<>();
        for(ProjectType childType : childTypes) {
            arr.add(childType.getId());
        }
        return arr.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectType that = (ProjectType) o;

        if (id != that.id) return false;
        if (parentId != that.parentId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return childTypes != null ? childTypes.equals(that.childTypes) : that.childTypes == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (parentId ^ (parentId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (childTypes != null ? childTypes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "ProjectType{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", childTypes=" + getChildTypeIds() +
                '}';
    }

}
