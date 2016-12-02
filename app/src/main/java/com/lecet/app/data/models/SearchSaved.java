package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by getdevsinc on 11/23/16.
 */

public class SearchSaved{
    @SerializedName("title")
    private String title;

    @SerializedName("modelName")
    private String modelName;

    @SerializedName("query")
    private String query;

    @SerializedName("filter")
    private SearchFilter filter;

    @SerializedName("optIn")
    private String optIn;

    @SerializedName("id")
    private int id;

    @SerializedName("userId")
    private int userId;

    public String getTitle() {
        return title;
    }

    public String getModelName() {
        return modelName;
    }

    public String getQuery() {
        return query;
    }

    public SearchFilter getFilter() {
        return filter;
    }

    public String getOptIn() {
        return optIn;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }


}