package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * File: ContactType Created: 11/9/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public class ContactType extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("code")
    private String code;

    @SerializedName("description")
    private String description;

    @SerializedName("category")
    private String category;

    @SerializedName("group")
    private String group;

    public ContactType() {
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactType)) return false;

        ContactType that = (ContactType) o;

        if (id != that.id) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (category != null ? !category.equals(that.category) : that.category != null)
            return false;
        return group != null ? group.equals(that.group) : that.group == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContactType{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
