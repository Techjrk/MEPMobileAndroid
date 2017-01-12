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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchFilter that = (SearchFilter) o;

        if (searchFilter != null ? !searchFilter.equals(that.searchFilter) : that.searchFilter != null)
            return false;
        return jurisdictions != null ? jurisdictions.equals(that.jurisdictions) : that.jurisdictions == null;

    }

    @Override
    public int hashCode() {
        int result = searchFilter != null ? searchFilter.hashCode() : 0;
        result = 31 * result + (jurisdictions != null ? jurisdictions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
                "searchFilter=" + searchFilter +
                ", jurisdictions=" + jurisdictions +
                '}';
    }
}

