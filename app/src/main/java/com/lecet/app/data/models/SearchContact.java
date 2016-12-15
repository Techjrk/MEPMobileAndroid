package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;

/**
 * Created by getdevsinc on 11/28/16.
 */

public class SearchContact {
    @SerializedName("total")
    private int total;

    @SerializedName("returned")
    private int returned;

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("results")
    private RealmList<Contact> results;

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

    public RealmList<Contact> getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchContact that = (SearchContact) o;

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
        return "SearchContact{" +
                "total=" + total +
                ", returned=" + returned +
                ", page=" + page +
                ", pages=" + pages +
                ", results=" + results +
                '}';
    }
}
