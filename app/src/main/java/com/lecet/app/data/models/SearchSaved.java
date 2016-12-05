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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchSaved that = (SearchSaved) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (modelName != null ? !modelName.equals(that.modelName) : that.modelName != null)
            return false;
        if (query != null ? !query.equals(that.query) : that.query != null) return false;
        if (filter != null ? !filter.equals(that.filter) : that.filter != null) return false;
        return optIn != null ? optIn.equals(that.optIn) : that.optIn == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (modelName != null ? modelName.hashCode() : 0);
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        result = 31 * result + (optIn != null ? optIn.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + userId;
        return result;
    }

    @Override
    public String toString() {
        return "SearchSaved{" +
                "title='" + title + '\'' +
                ", modelName='" + modelName + '\'' +
                ", query='" + query + '\'' +
                ", filter=" + filter +
                ", optIn='" + optIn + '\'' +
                ", id=" + id +
                ", userId=" + userId +
                '}';
    }
}
