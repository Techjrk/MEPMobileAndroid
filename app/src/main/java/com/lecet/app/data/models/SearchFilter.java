package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by getdevsinc on 11/21/16.
 */

public class SearchFilter {
    @SerializedName("searchFilter")
    private SearchFilter searchFilter;


    @SerializedName("jurisdictions")
    private SearchFilter jurisdictions;

    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    public SearchFilter getJurisdictions() {
        return jurisdictions;
    }


}

