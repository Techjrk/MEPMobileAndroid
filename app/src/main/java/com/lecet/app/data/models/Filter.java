package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by getdevsinc on 1/13/17.
 */
public class Filter
{
    @SerializedName("searchFilter")
    private SearchFilter searchFilter;

    @SerializedName("order")
    private String order;

    @SerializedName("limit")
    private int limit;

    @SerializedName("include")
    private List<String> include;

    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<String> getInclude() {
        return include;
    }

    public void setInclude(List<String> include) {
        this.include = include;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filter filter = (Filter) o;

        if (limit != filter.limit) return false;
        if (searchFilter != null ? !searchFilter.equals(filter.searchFilter) : filter.searchFilter != null)
            return false;
        if (order != null ? !order.equals(filter.order) : filter.order != null) return false;
        return include != null ? include.equals(filter.include) : filter.include == null;

    }

    @Override
    public int hashCode() {
        int result = searchFilter != null ? searchFilter.hashCode() : 0;
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + limit;
        result = 31 * result + (include != null ? include.hashCode() : 0);
        return result;
    }
}
