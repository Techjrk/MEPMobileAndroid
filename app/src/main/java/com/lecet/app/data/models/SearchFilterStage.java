package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by getdevsinc on 1/10/17.
 */

public class SearchFilterStage extends RealmObject {
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("parentId")
    private int parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilterStage that = (SearchFilterStage) o;

        if (id != that.id) return false;
        if (parentId != that.parentId) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + parentId;
        return result;
    }
}
