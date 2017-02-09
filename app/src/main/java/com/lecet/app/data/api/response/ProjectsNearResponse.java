package com.lecet.app.data.api.response;

import com.google.gson.annotations.SerializedName;

import com.lecet.app.data.models.Project;

import java.util.List;

/**
 * File: ProjectsNearResponse Created: 10/19/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ProjectsNearResponse {

    @SerializedName("total")
    private int total;

    @SerializedName("returned")
    private int returned;

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("results")
    private List<Project> results;

    public ProjectsNearResponse() {
    }

    public int getTotal() {
        return total;
    }

    public int getReturned() {
        return returned;
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public List<Project> getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectsNearResponse)) return false;

        ProjectsNearResponse that = (ProjectsNearResponse) o;

        if (total != that.total) return false;
        if (returned != that.returned) return false;
        if (page != that.page) return false;
        if (pages != that.pages) return false;
        return results != null ? results.equals(that.results) : that.results == null;

    }

    @Override
    public int hashCode() {
        int result = total;
        result = 31 * result + returned;
        result = 31 * result + page;
        result = 31 * result + pages;
        result = 31 * result + (results != null ? results.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectsNearResponse{" +
                "total=" + total +
                ", returned=" + returned +
                ", page=" + page +
                ", pages=" + pages +
                ", results=" + results +
                '}';
    }
}
