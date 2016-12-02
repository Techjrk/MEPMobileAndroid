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


}
