package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by getdevsinc on 11/28/16.
 */

public class SearchProject {
    @SerializedName("total")
    private int total;

    @SerializedName("returned")
    private int returned;

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("results")
    private RealmList<Project> results;

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

    public RealmList<Project> getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchProject that = (SearchProject) o;

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
        return "SearchProject{" +
                "total=" + total +
                ", returned=" + returned +
                ", page=" + page +
                ", pages=" + pages +
                ", results=" + results +
                '}';
    }
}
