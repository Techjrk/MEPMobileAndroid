package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.annotations.PrimaryKey;

/**
 * Created by getdevsinc on 1/9/17.
 */

public class SearchFilterProjectTypesMain {
    @SerializedName("title")
    private String title;

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("projectCategories")
    private List<SearchFilterProjectTypesProjectCategory> projectCategories;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public List<SearchFilterProjectTypesProjectCategory> getProjectCategories() {
        return projectCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilterProjectTypesMain that = (SearchFilterProjectTypesMain) o;

        if (id != that.id) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return projectCategories != null ? projectCategories.equals(that.projectCategories) : that.projectCategories == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + (projectCategories != null ? projectCategories.hashCode() : 0);
        return result;
    }
}
