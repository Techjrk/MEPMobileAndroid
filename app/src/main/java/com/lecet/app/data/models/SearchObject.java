package com.lecet.app.data.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by getdevsinc on 11/21/16.
 */

public class SearchObject extends RealmObject {
    @SerializedName("itemobject")
    private String itemobject;

    public String getItemobject() {
        return itemobject;
    }

    @Override
    public String toString() {
        return "SearchObject{" +
                "itemobject='" + itemobject + '\'' +
                '}';
    }
}

