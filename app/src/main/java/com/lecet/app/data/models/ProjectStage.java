package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: ProjectStage Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectStage extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("parentId")
    private long parentId;

    @SerializedName("name")
    private String name;

    @SerializedName("childStages")
    public RealmList<ProjectStage> childStages = new RealmList<>();

    public ProjectStage() {
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
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

    public RealmList<ProjectStage> getChildStages() {
        return childStages;
    }

    public void setChildStages(RealmList<ProjectStage> childStages) {
        this.childStages = childStages;
    }

    public void addChildStage(ProjectStage childStage) {
        this.childStages.add(childStage);
    }

    /**
     * Helper for toString() output of child IDs
     */
    private String getChildStageIds() {
        ArrayList<Long> arr = new ArrayList<>();
        for(ProjectStage childStage : childStages) {
            arr.add(childStage.getId());
        }
        return arr.toString();
    }

    @Override
    public String toString() {
        return "ProjectStage{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", childStages=" + getChildStageIds() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectStage that = (ProjectStage) o;

        if (id != that.id) return false;
        if (parentId != that.parentId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return childStages != null ? childStages.equals(that.childStages) : that.childStages == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (parentId ^ (parentId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (childStages != null ? childStages.hashCode() : 0);
        return result;
    }

}
