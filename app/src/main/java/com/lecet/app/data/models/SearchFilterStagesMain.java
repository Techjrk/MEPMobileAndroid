package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by getdevsinc on 1/10/17.
 */

public class SearchFilterStagesMain {
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("stages")
    private List<SearchFilterStage> stages;

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

    public List<SearchFilterStage> getStages() {
        return stages;
    }

    public void setStages(List<SearchFilterStage> stages) {
        this.stages = stages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilterStagesMain that = (SearchFilterStagesMain) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return stages != null ? stages.equals(that.stages) : that.stages == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + (stages != null ? stages.hashCode() : 0);
        return result;
    }
}
